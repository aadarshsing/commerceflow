package com.order.service.implementation;
import com.order.dto.*;
import com.order.entity.Order;
import com.order.entity.OrderAddress;
import com.order.entity.OrderItem;
import com.order.entity.enums.*;
import com.order.exception.ResourceNotActiveException;
import com.order.exception.ResourceNotFoundException;
import com.order.mapper.OrderMapper;
import com.order.repository.OrderRepository;
import com.order.service.IOrderService;
import com.order.service.client.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    CartFeignClient cartFeignClient;
    InventoryFeignClient inventoryFeignClient;
    CustomerFeignClient customerFeignClient;
    OrderRepository orderRepository;
    CatalogFeignClient catalogFeignClient;
    PaymentFeignClient paymentFeignClient;

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Order","orderId",id.toString())
        );
        return  OrderMapper.orderToOrderResponseDtoMapper(order);
    }

    @Override
    public Page<OrderResponseDto> getOrdersByCustomerId(Long customerId, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC,"createdAt")
        );
        Page<Order> orderList = orderRepository.findAllOrderByCustomerId(customerId,pageable);
        return orderList.map(OrderMapper::orderToOrderResponseDtoMapper);

    }

    @Override
    public void createOrderFromCart(CartCheckOutRequest cartCheckOutRequest) {

        CartResponseDto cart = cartFeignClient.getCart(cartCheckOutRequest.cartId()).getBody();
        if(cart.getItems().size() == 0){
            throw new IllegalStateException("Cart is Empty "+cart.getItems());
        }
        Boolean isExist = customerFeignClient.checkCustomerExist(cartCheckOutRequest.customerId()).getBody();
        if(!isExist){
            throw  new ResourceNotFoundException("Customer","CustomerId",cartCheckOutRequest.customerId().toString());
        }
        if(cart == null){
            throw new ResourceNotFoundException("Cart","cartId",cartCheckOutRequest.cartId().toString());
        }
        else if(!cart.getCustomerId().equals(cartCheckOutRequest.customerId())){
            throw new IllegalStateException("CustomerID does not match with cart's customer's ID");
        }
        if(!cart.getCartStatus().equals(CartStatus.ACTIVE)) {
            throw new ResourceNotActiveException("Cart", "cartId", cartCheckOutRequest.cartId().toString());
        }

        Order order = OrderMapper.cartToOrderMapper(cart,new Order());
        AddressResponseDto address = customerFeignClient.getAddress(cartCheckOutRequest.shippingAddressId()).getBody();
        if(!address.customerId().equals(cartCheckOutRequest.customerId())){
            throw new IllegalStateException("CustomerID does not match with Address's customer's ID");
        }
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrder(order);
        order.setShippingAddress(OrderMapper.shippingAddressToOrderAddress(address,orderAddress));

        List<CartItemResponseDto> cartItemList = cart.getItems();
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItemResponseDto cartItem : cartItemList) {
            ProductResponseDto product = cartItem.getProductResponseDto();

            if (product.status().equals(ProductStatus.INACTIVE)) {
                throw new ResourceNotActiveException("Product", "productId", product.id().toString());
            }

            inventoryFeignClient.updateStock(
                    product.id(),
                    new UpdateInventoryDto(
                            cartItem.getQuantity(),
                            InventoryOperation.RESERVE
                    )
            );
            OrderItem orderItem = OrderMapper.cartItemToOrderItemMapper(cartItem,new OrderItem(),product);
            orderItems.add(orderItem);
            orderItem.setOrder(order);
            totalAmount = totalAmount.add(product.price().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);
//         it will call payment then once payment is confirmed that order transition move to confirmed if payment failed
//         then we will again release inventory product and make transition to order not created
        try {
            ResponseDto paymentResponseDto = paymentFeignClient.createPayment(
                    new CreatePaymentDto(
                            order.getId(),
                            cartCheckOutRequest.customerId(),
                            PaymentMethod.UPI
                    )
            ).getBody();
            if (paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED)) {
                for (int i = 1; i < 5; i++) {
                    paymentResponseDto = paymentFeignClient.createPayment(
                            new CreatePaymentDto(
                                    order.getId(),
                                    cartCheckOutRequest.customerId(),
                                    PaymentMethod.UPI)).getBody();
                    if(paymentResponseDto.statusMsg().equals(PaymentStatus.PENDING)
                            || paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS)
                    ) break;

                }
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED)){
                for(CartItemResponseDto cartItem:cartItemList){
                    ProductResponseDto product = cartItem.getProductResponseDto();

                    inventoryFeignClient.updateStock(
                            product.id(),
                            new UpdateInventoryDto(
                                    cartItem.getQuantity(),
                                    InventoryOperation.RELEASE
                            )
                    );
                }
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS)){
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }
        } catch (Exception e) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            for(CartItemResponseDto cartItem : cartItemList) {
                ProductResponseDto product = cartItem.getProductResponseDto();

                if (product.status().equals(ProductStatus.INACTIVE)) {
                    throw new ResourceNotActiveException("Product", "productId", product.id().toString());
                }

                inventoryFeignClient.updateStock(
                        product.id(),
                        new UpdateInventoryDto(
                                cartItem.getQuantity(),
                                InventoryOperation.RELEASE
                        )
                );
            }
            throw new RuntimeException(
                    "Unable to create Payment, order creation deleted and inventory is released ",
                    e
            );
        }
        ResponseDto responseDto = cartFeignClient.deleteCartItems(cart.getId()).getBody();
        if(!responseDto.statusCode().equals(HttpStatus.OK)){
            //will use retries pattern after while implementing fault torlence
        }
//         we will remove items after begin dispatch or delivered



    }

    @Override
    public void createOrderFromBuyNow(BuyNowRequest buyNowRequest) {

        Boolean isExist = customerFeignClient.checkCustomerExist(buyNowRequest.customerId()).getBody();
        if(!isExist){
            throw  new ResourceNotFoundException("Customer","CustomerId",buyNowRequest.customerId().toString());
        }
        Order order = new Order();
        AddressResponseDto address = customerFeignClient.getAddress(buyNowRequest.shippingAddressId()).getBody();

        ProductResponseDto product = catalogFeignClient.getProductById(buyNowRequest.productId()).getBody();

        if (product.status().equals(ProductStatus.INACTIVE)) {
            throw new ResourceNotActiveException("Product", "productId", buyNowRequest.productId().toString());
        }
        inventoryFeignClient.updateStock(
                product.id(),
                new UpdateInventoryDto(
                        buyNowRequest.quantity(),
                        InventoryOperation.RESERVE
                )
        );
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrder(order);
        order.setShippingAddress(OrderMapper.shippingAddressToOrderAddress(address,orderAddress));
        OrderItem orderItem = OrderMapper.buyNowRequestToOrderMapper(buyNowRequest,new OrderItem(),product);
        orderItem.setOrder(order);
        order.setTotalAmount(orderItem.getTotalPrice());
        order.setOrderItems(List.of(orderItem));
        orderRepository.save(order);

        // it will call payment then once payment is confirmed that order transition move to confirmed if payment failed
        // then we will again release inventory product and make transition to order not created
        try{
            ResponseDto paymentResponseDto = paymentFeignClient.createPayment(
                    new CreatePaymentDto(
                            order.getId(),
                            buyNowRequest.customerId(),
                            PaymentMethod.UPI
                    )
            ).getBody();
            if (paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED)) {
                for (int i = 1; i < 6; i++) {
                    paymentResponseDto = paymentFeignClient.createPayment(new CreatePaymentDto(order.getId(), buyNowRequest.customerId(), PaymentMethod.UPI)).getBody();
                    if(paymentResponseDto.statusMsg().equals(PaymentStatus.PENDING)
                            || paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS)
                    ) break;

                }
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED)){
                inventoryFeignClient.updateStock(
                        product.id(),
                        new UpdateInventoryDto(
                                buyNowRequest.quantity(),
                                InventoryOperation.RELEASE
                        )
                );
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS)){
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
            }
        }
        catch (Exception e){
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            inventoryFeignClient.updateStock(
                    product.id(),
                    new UpdateInventoryDto(
                            buyNowRequest.quantity(),
                            InventoryOperation.RELEASE
                    )
            );
            throw new RuntimeException(
                    "Unable to create Payment, order creation deleted and inventory is released ",
                    e
            );
        }

        // we will remove items after begin dispatch or delivered

    }

    @Override
    public void makeOrderTransition(Long orderId, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new ResourceNotFoundException("Order","orderId",orderId.toString())
        );
        OrderStatus currentOrderStatus = order.getOrderStatus();
        if (currentOrderStatus == OrderStatus.CREATED) {

            if (orderStatus != OrderStatus.CONFIRMED &&
                    orderStatus != OrderStatus.CANCELLED) {
                throw new IllegalStateException(
                        "Order can only be CONFIRMED or CANCELLED from CREATED"
                );
            }

        } else if (currentOrderStatus == OrderStatus.CONFIRMED) {

            if (orderStatus != OrderStatus.PROCESSING &&
                    orderStatus != OrderStatus.CANCELLED) {
                throw new IllegalStateException(
                        "Order can only be PROCESSING or CANCELLED from CONFIRMED"
                );
            }

        } else if (currentOrderStatus == OrderStatus.PROCESSING) {

            if (orderStatus != OrderStatus.SHIPPED &&
                    orderStatus != OrderStatus.CANCELLED) {
                throw new IllegalStateException(
                        "Order can only be SHIPPED or CANCELLED from PROCESSING"
                );
            }

        } else if (currentOrderStatus == OrderStatus.SHIPPED) {

            if (orderStatus != OrderStatus.DELIVERED) {
                throw new IllegalStateException(
                        "Order can only be DELIVERED from SHIPPED"
                );
            }

        } else if (currentOrderStatus == OrderStatus.DELIVERED) {

            throw new IllegalStateException(
                    "Delivered order cannot change its status"
            );

        } else if (currentOrderStatus == OrderStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Cancelled order cannot change its status"
            );
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }
}
