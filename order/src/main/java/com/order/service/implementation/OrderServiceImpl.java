package com.order.service.implementation;

import com.order.dto.CartCheckOutRequest;
import com.order.dto.BuyNowRequest;
import com.order.dto.OrderResponseDto;
import com.order.dto.ResponseDto;
import com.order.entity.Order;
import com.order.entity.OrderAddress;
import com.order.entity.OrderItem;
import com.order.entity.enums.*;
import com.order.exception.ResourceNotActiveException;
import com.order.exception.ResourceNotFoundException;
import com.order.mapper.AddressMapper;
import com.order.mapper.OrderMapper;
import com.order.repository.OrderRepository;
import com.order.service.IOrderService;
import com.order.service.client.*;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.commerceflow.dto.cart.CartItemResponseDto;
import org.commerceflow.dto.cart.CartResponseDto;
import org.commerceflow.dto.catalog.ProductResponseDto;
import org.commerceflow.dto.customer.AddressResponseDto;
import org.commerceflow.dto.inventory.UpdateInventoryDto;
import org.commerceflow.dto.notification.CreateNotificationDto;
import org.commerceflow.dto.payment.CreatePaymentDto;
import org.commerceflow.dto.payment.PaymentResponseDto;
import org.commerceflow.dto.shipment.CreateShipmentDto;
import org.commerceflow.enums.cart.CartStatus;
import org.commerceflow.enums.inventory.InventoryOperation;
import org.commerceflow.enums.notification.NotificationType;
import org.commerceflow.enums.payment.PaymentMethod;
import org.commerceflow.enums.payment.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final CartFeignClient cartFeignClient;
    private final InventoryFeignClient inventoryFeignClient;
    private final CustomerFeignClient customerFeignClient;
    private final OrderRepository orderRepository;
    private final CatalogFeignClient catalogFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final NotificationFeignClient notificationFeignClient;
    private final ShipmentFeignClient shipmentFeignClient;


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
    public OrderResponseDto createOrderFromCart(CartCheckOutRequest cartCheckOutRequest, String correlationId, String idempotencyKey) {

        Optional<Order> checkOrderExist = orderRepository.findByIdempotencyKey(idempotencyKey);
        if(checkOrderExist.isPresent()){
            logger.info("Order Already Existed. correlationId={}, orderId={}, orderStatus={}",
                    correlationId,
                    checkOrderExist.get().getId(),
                    checkOrderExist.get().getOrderStatus());

            return OrderMapper.orderToOrderResponseDtoMapper(checkOrderExist.get());
        }

        logger.info("Starting order creation. correlationId={}, cartId={}, customerId={}, shippingAddressId={}",
                correlationId,
                cartCheckOutRequest.cartId(),
                cartCheckOutRequest.customerId(),
                cartCheckOutRequest.shippingAddressId());

        CartResponseDto cart = cartFeignClient.getCart(cartCheckOutRequest.cartId()).getBody();

        if(cart != null && cart.getItems().isEmpty()){
            logger.warn("Cart is empty. correlationId={}, cartId={}",
                    correlationId,
                    cartCheckOutRequest.cartId());
            throw new IllegalStateException("Cart is Empty "+cart.getItems());
        }

        logger.debug("Cart fetched successfully. correlationId={}, cartId={}, itemCount={}",
                correlationId,
                cartCheckOutRequest.cartId(),
                cart != null ? cart.getItems().size() : null);

        Boolean isExist = customerFeignClient.checkCustomerExist(cartCheckOutRequest.customerId()).getBody();
        logger.debug("Customer validation completed. correlationId={}, customerId={}, exists={}",
                correlationId,
                cartCheckOutRequest.customerId(),
                isExist);
        if(!isExist){
            logger.warn("Customer not found. correlationId={}, customerId={}",
                    correlationId,
                    cartCheckOutRequest.customerId());
            throw  new ResourceNotFoundException("Customer","CustomerId",cartCheckOutRequest.customerId().toString());
        }

        else if(cart!=null && !cart.getCustomerId().equals(cartCheckOutRequest.customerId())){
            logger.warn("Cart customer mismatch. correlationId={}, cartId={}, requestCustomerId={}, cartCustomerId={}",
                    correlationId,
                    cart.getId(),
                    cartCheckOutRequest.customerId(),
                    cart.getCustomerId());
            throw new IllegalStateException("CustomerID does not match with cart's customer's ID");
        }
        if(cart != null && !cart.getCartStatus().equals(CartStatus.ACTIVE)) {
            logger.warn("Cart is not active. correlationId={}, cartId={}, cartStatus={}",
                    correlationId,
                    cart.getId(),
                    cart.getCartStatus());
            throw new ResourceNotActiveException("Cart", "cartId", cartCheckOutRequest.cartId().toString());
        }

        Order order = OrderMapper.cartToOrderMapper(cart,new Order());
        AddressResponseDto address = null;
        try {
            logger.info("Fetching shipping address. correlationId={}, addressId={}, customerId={}",
                    correlationId,
                    cartCheckOutRequest.shippingAddressId(),
                    cartCheckOutRequest.customerId());

            address =
                    customerFeignClient.getAddress(
                            cartCheckOutRequest.shippingAddressId()
                    ).getBody();

            logger.debug("Shipping address fetched successfully. correlationId={}, addressId={}, addressCustomerId={}",
                    correlationId,
                    cartCheckOutRequest.shippingAddressId(),
                    address.customerId());

            if(!address.customerId().equals(cartCheckOutRequest.customerId())){
                logger.warn("Address customer mismatch. correlationId={}, addressCustomerId={}, requestCustomerId={}",
                        correlationId,
                        address.customerId(),
                        cartCheckOutRequest.customerId());
                throw new IllegalStateException("CustomerID does not match with Address's customer's ID");
            }

        } catch (FeignException e) {
            logger.error("Failed to fetch shipping address. correlationId={}, addressId={}, status={}, message={}",
                    correlationId,
                    cartCheckOutRequest.shippingAddressId(),
                    e.status(),
                    e.getMessage(),
                    e);

            throw e;
        }


        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrder(order);
        order.setShippingAddress(OrderMapper.shippingAddressToOrderAddress(address,orderAddress));

        List<CartItemResponseDto> cartItemList = cart.getItems();
        BigDecimal totalAmount = BigDecimal.valueOf(0);
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            for(CartItemResponseDto cartItem : cartItemList) {
                ProductResponseDto product = cartItem.getProductResponseDto();

                if (product.status().toString().equals(ProductStatus.INACTIVE.toString())) {
                    logger.warn("Product is inactive. correlationId={}, productId={}",
                            correlationId,
                            product.id());
                    throw new ResourceNotActiveException("Product", "productId", product.id().toString());
                }
                OrderItem orderItem = OrderMapper.cartItemToOrderItemMapper(cartItem,new OrderItem(),product);
                orderItems.add(orderItem);
                orderItem.setOrder(order);
                totalAmount = totalAmount.add(product.price().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }
        } catch (ResourceNotActiveException e) {
            throw new IllegalStateException("something happened wrong.Please try again", e);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        logger.info("Saving order. correlationId={}, customerId={}, totalAmount={}, itemCount={}",
                correlationId,
                cartCheckOutRequest.customerId(),
                totalAmount,
                orderItems.size());

        order = orderRepository.save(order);

        logger.info("Order created successfully in database. correlationId={}, orderId={}, totalAmount={}",
                correlationId,
                order.getId(),
                order.getTotalAmount());

        notificationFeignClient.createNotification(
                new CreateNotificationDto(
                        cartCheckOutRequest.customerId(),
                        NotificationType.ORDER_CREATED,
                        "POP UP",
                        "Order",
                        "Order is Created Successfully",
                        order.getId().toString()
                )
        );
        logger.debug("ORDER_CREATED notification sent. correlationId={}, orderId={}",
                correlationId,
                order.getId());

//
        try {
            for(CartItemResponseDto cartItem : cartItemList) {
                ProductResponseDto product = cartItem.getProductResponseDto();

                logger.debug("Reserving inventory. correlationId={}, productId={}, quantity={}",
                        correlationId,
                        product.id(),
                        cartItem.getQuantity());
                String inventoryReserveKey =
                        "RESERVE_ORDER_" + order.getId() + "_PRODUCT_" + product.id();
                inventoryFeignClient.updateStock(
                        product.id(),
                        inventoryReserveKey,
                        new UpdateInventoryDto(
                                cartItem.getQuantity(),
                                InventoryOperation.RESERVE,
                                order.getId()
                        )
                );

                logger.debug("Inventory reserved. correlationId={}, inventoryReserveKey={}, productId={}, quantity={}",
                        correlationId,
                        inventoryReserveKey,
                        product.id(),
                        cartItem.getQuantity());
            }
        } catch (ResourceNotActiveException e) {
            logger.error("Inventory reservation failed. correlationId={}, orderId={}, error={}",
                    correlationId,
                    order.getId(),
                    e.getMessage(),
                    e);
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            logger.warn("Order marked as CANCELLED. correlationId={}, orderId={} as Inventory Reservation failed",
                    correlationId,
                    order.getId());
            throw new RuntimeException(e);
        }

        try {
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            order.getCustomerId(),
                            NotificationType.PAYMENT_PENDING,
                            "POP UP",
                            "Payment",
                            "Payment Pending",
                            order.getId().toString()
                    )
            );
            logger.info("Starting payment processing. correlationId={}, orderId={}, paymentMethod={}",
                    correlationId,
                    order.getId(),
                    PaymentMethod.UPI);
            String paymentIdempotencyKey =
                    "PAYMENT_ORDER_" + order.getId();
            ResponseDto paymentResponseDto = paymentFeignClient.createPayment(
                    paymentIdempotencyKey,
                    new CreatePaymentDto(
                            order.getId(),
                            cartCheckOutRequest.customerId(),
                            PaymentMethod.UPI
                    )).getBody();

            logger.info("Payment response received. correlationId={}, orderId={}, paymentStatus={}",
                    correlationId,
                    order.getId(),
                    paymentResponseDto.statusMsg());

            if (paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED.toString())) {
                for (int i = 1; i < 5; i++) {
                    logger.warn("Retrying payment. correlationId={}, orderId={}, attempt={}",
                            correlationId,
                            order.getId(),
                            i);
                    paymentResponseDto = paymentFeignClient.createPayment(
                            paymentIdempotencyKey,
                            new CreatePaymentDto(
                                    order.getId(),
                                    cartCheckOutRequest.customerId(),
                                    PaymentMethod.UPI)).getBody();
                    if((PaymentStatus.FAILED.toString()).equals(paymentResponseDto.statusMsg())
                            || paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS.toString())
                    ) break;

                }
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED.toString())){
                logger.error("Payment failed after retries. correlationId={}, orderId={}",
                        correlationId,
                        order.getId());
                for(CartItemResponseDto cartItem:cartItemList){
                    ProductResponseDto product = cartItem.getProductResponseDto();

                    logger.info("Releasing reserved inventory after payment failure. correlationId={}, orderId={}, productId={}, quantity={}",
                            correlationId,
                            order.getId(),
                            product.id(),
                            cartItem.getQuantity());
                    String inventoryReserveKey =
                            "RELEASE_ORDER_" + order.getId() + "_PRODUCT_" + product.id();
                    inventoryFeignClient.updateStock(
                            product.id(),
                            inventoryReserveKey
                            ,
                            new UpdateInventoryDto(
                                    cartItem.getQuantity(),
                                    InventoryOperation.RELEASE,
                                    order.getId()
                            ));
                }

            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS.toString())){
                logger.info("Payment successful. Confirming order. correlationId={}, orderId={}",
                        correlationId,
                        order.getId());
                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
                notificationFeignClient.createNotification(
                        new CreateNotificationDto(
                                cartCheckOutRequest.customerId(),
                                NotificationType.ORDER_CONFIRMED,
                                "POP UP",
                                "Order",
                                "Order Confirmed",
                                order.getId().toString()
                        )
                );
                logger.debug("ORDER_CONFIRMED notification sent. correlationId={}, orderId={}",
                        correlationId,
                        order.getId());

                logger.info("Creating shipment. correlationId={}, orderId={}, customerId={}",
                        correlationId,
                        order.getId(),
                        cartCheckOutRequest.customerId());
                String shipmentIdempotencyKey = "SHIPMENT_ORDER_" + order.getId();
                ResponseDto responseDto = shipmentFeignClient.createShipment(
                        shipmentIdempotencyKey,
                        new CreateShipmentDto(
                                order.getId(),
                                cartCheckOutRequest.customerId(),
                                AddressMapper.orderAddressToAddressResponseDtoMapper(orderAddress)
                        )
                ).getBody();
                logger.info("Shipment creation response received. correlationId={}, orderId={}, statusCode={}",
                        correlationId,
                        order.getId(),
                        responseDto != null ? responseDto.statusCode() : null);
                if (responseDto == null) {
                    throw new RuntimeException("Payment response is null");
                }

                if (HttpStatus.CREATED.toString().equals(responseDto.statusCode())) {
                    cartFeignClient.deleteCartItems(cart.getId());
                    logger.info("Cart cleared after successful order creation. correlationId={}, cartId={}, orderId={}",
                            correlationId,
                            cart.getId(),
                            order.getId());
                }
                else{
                    throw new RuntimeException("Shipment cannot be created");
                }


            }
        } catch (Exception e) {
            logger.error("Order processing failed. correlationId={}, orderId={}, error={}",
                    correlationId,
                    order.getId(),
                    e.getMessage(),
                    e);
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            logger.warn("Order marked as CANCELLED. correlationId={}, orderId={}",
                    correlationId,
                    order.getId());
            PaymentResponseDto paymentSuccessful = Objects.requireNonNull(paymentFeignClient.getPaymentByOrder(order.getId())
                            .getBody())
                    .stream()
                    .filter(payment -> Objects.equals(payment.status().toString(), PaymentStatus.SUCCESS.toString())).findFirst().orElse(null);

            if(paymentSuccessful!=null){

                logger.info("Initiating payment refund. correlationId={}, orderId={}, paymentId={}",
                        correlationId,
                        order.getId(),
                        paymentSuccessful.paymentId());
                String refundPaymentIdempotencyKey =
                        "PAYMENT_ORDER_" + order.getId() + "_REFUND_" + paymentSuccessful.paymentId();
                paymentFeignClient.createRefundPayment(
                        paymentSuccessful.paymentId(),
                        paymentSuccessful.orderId(),
                        refundPaymentIdempotencyKey);

                logger.info("Payment refund initiated successfully. correlationId={}, orderId={}, paymentId={}",
                        correlationId,
                        order.getId(),
                        paymentSuccessful.paymentId());
            }

            logger.debug("Sending ORDER_CANCELLED notification. correlationId={}, orderId={}",
                    correlationId,
                    order.getId());
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            order.getCustomerId(),
                            NotificationType.ORDER_CANCELLED,
                            "POP UP",
                            "Order",
                            "Order Cancelled " + e,
                            order.getId().toString()
                    )
            );
            for(CartItemResponseDto cartItem : cartItemList) {
                ProductResponseDto product = cartItem.getProductResponseDto();

                if (product.status().equals(ProductStatus.INACTIVE)) {
                    throw new ResourceNotActiveException("Product", "productId", product.id().toString());
                }
                logger.info("Releasing inventory during order rollback. correlationId={}, orderId={}, productId={}, quantity={}",
                        correlationId,
                        order.getId(),
                        product.id(),
                        cartItem.getQuantity());
                String inventoryReserveKey =
                        "RELEASE_ORDER_" + order.getId() + "_PRODUCT_" + product.id();
                inventoryFeignClient.updateStock(
                        product.id(),
                        inventoryReserveKey,
                        new UpdateInventoryDto(
                                cartItem.getQuantity(),
                                InventoryOperation.RELEASE,
                                order.getId()
                        ));
            }
            throw new RuntimeException(
                    "Unable to process order, order creation deleted and inventory is released and if money if deducted it will be refunded within 24 hours",
                    e
            );
        }
        logger.info("Order creation completed successfully. correlationId={}, orderId={}, orderStatus={}",
                correlationId,
                order.getId(),
                order.getOrderStatus());

        return OrderMapper.orderToOrderResponseDtoMapper(order);

    }

    @Override
    public void createOrderFromBuyNow(String idempotencyKey, BuyNowRequest buyNowRequest) {
        Optional<Order> checkOrderExist = orderRepository.findByIdempotencyKey(idempotencyKey);
        if(checkOrderExist.isPresent()){
            logger.info("Order Already Existed. orderId={}, orderStatus={}",
                    checkOrderExist.get().getId(),
                    checkOrderExist.get().getOrderStatus());

            return ;
        }

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
        try {
            String inventoryReserveKey =
                    "RESERVE_ORDER_" + order.getId() + "_PRODUCT_" + product.id();
            inventoryFeignClient.updateStock(
                    product.id(),
                    inventoryReserveKey,
                    new UpdateInventoryDto(
                            buyNowRequest.quantity(),
                            InventoryOperation.RESERVE,
                            order.getId()
                    ));
        } catch (Exception e) {
            throw new IllegalStateException("Something happend wrong. Please try again",e);
        }
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrder(order);
        order.setShippingAddress(OrderMapper.shippingAddressToOrderAddress(address,orderAddress));
        OrderItem orderItem = OrderMapper.buyNowRequestToOrderMapper(buyNowRequest,new OrderItem(),product);
        orderItem.setOrder(order);
        order.setTotalAmount(orderItem.getTotalPrice());
        order.setOrderItems(List.of(orderItem));
        order.setCustomerId(buyNowRequest.customerId());
        order.setOrderStatus(OrderStatus.CREATED);
        orderRepository.save(order);

        notificationFeignClient.createNotification(
                new CreateNotificationDto(
                        order.getCustomerId(),
                        NotificationType.ORDER_CREATED,
                        "POP UP",
                        "Order",
                        "Order is Created Successfully",
                        order.getId().toString()
                )
        );
        // it will call payment then once payment is confirmed that order transition move to confirmed if payment failed
        // then we will again release inventory product and make transition to order not created
        try{
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            order.getCustomerId(),
                            NotificationType.PAYMENT_PENDING,
                            "POP UP",
                            "Payment",
                            "Payment Pending",
                            order.getId().toString()
                    )
            );
            String paymentIdempotencyKey =
                    "PAYMENT_ORDER_" + order.getId();
            ResponseDto paymentResponseDto = paymentFeignClient.createPayment(
                    paymentIdempotencyKey,
                    new CreatePaymentDto(
                            order.getId(),
                            buyNowRequest.customerId(),
                            PaymentMethod.UPI
                    )).getBody();
            if(paymentResponseDto == null){
                throw  new RuntimeException("Payment ResponseDto is null");
            }
            if (paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED.toString())) {
                for (int i = 1; i < 6; i++) {
                    paymentResponseDto = paymentFeignClient.createPayment(
                            paymentIdempotencyKey,
                            new CreatePaymentDto(
                                    order.getId(),
                                    buyNowRequest.customerId(),
                                    PaymentMethod.UPI)).getBody();
                    if (paymentResponseDto == null) {
                        throw new RuntimeException("Payment ResponseDto is null");
                    }
                    if (paymentResponseDto.statusMsg().equals(PaymentStatus.PENDING.toString())
                            || paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS.toString())
                    ) break;

                }
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.FAILED.toString())){
                String inventoryReleaseKey =
                        "RELEASE_ORDER_" + order.getId() + "_PRODUCT_" + product.id();
                inventoryFeignClient.updateStock(
                        product.id(),
                        inventoryReleaseKey,
                        new UpdateInventoryDto(
                                buyNowRequest.quantity(),
                                InventoryOperation.RELEASE,
                                order.getId()
                        ));
            }
            if(paymentResponseDto.statusMsg().equals(PaymentStatus.SUCCESS.toString())){

                order.setOrderStatus(OrderStatus.CONFIRMED);
                orderRepository.save(order);
                notificationFeignClient.createNotification(
                        new CreateNotificationDto(
                                order.getCustomerId(),
                                NotificationType.ORDER_CONFIRMED,
                                "POP UP",
                                "Order",
                                "Order is confirmed Successfully",
                                order.getId().toString()
                        )
                );
                String shipmentIdempotencyKey = "SHIPMENT_ORDER_" + order.getId();
                ResponseDto responseDto = shipmentFeignClient.createShipment(
                        shipmentIdempotencyKey,
                        new CreateShipmentDto(
                                order.getId(),
                                order.getCustomerId(),
                                AddressMapper.orderAddressToAddressResponseDtoMapper(orderAddress)
                        )
                ).getBody();
                if (responseDto == null) {
                    throw new RuntimeException("Shipment does not created ");
                }
            }
        }
        catch (Exception e){
            PaymentResponseDto paymentSuccessful = Objects.requireNonNull(paymentFeignClient.getPaymentByOrder(order.getId())
                            .getBody())
                    .stream()
                    .filter(payment -> payment.status() == PaymentStatus.SUCCESS).findFirst().orElse(null);
            if(paymentSuccessful!=null){
                String refundPaymentIdempotencyKey =
                        "PAYMENT_ORDER_" + order.getId() + "_REFUND_" + paymentSuccessful.paymentId();
                paymentFeignClient.createRefundPayment(
                        paymentSuccessful.paymentId(),paymentSuccessful.orderId(),refundPaymentIdempotencyKey );
            }
            notificationFeignClient.createNotification(
                    new CreateNotificationDto(
                            order.getCustomerId(),
                            NotificationType.ORDER_CANCELLED,
                            "POP UP",
                            "Order",
                            "Order is Cancelled due to some issue" + e,
                            order.getId().toString()
                    )
            );
            order.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            String inventoryReleaseKey =
                    "RELEASE_ORDER_" + order.getId() + "_PRODUCT_" + product.id();
            inventoryFeignClient.updateStock(
                    product.id(),
                    inventoryReleaseKey,
                    new UpdateInventoryDto(
                            buyNowRequest.quantity(),
                            InventoryOperation.RELEASE,
                            order.getId()
                    ));
            throw new RuntimeException(
                    "Unable to create Payment, order creation deleted and inventory is released and Money Refunded ",
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
        sendOrderNotification(order,orderStatus);
    }
    private void sendOrderNotification(Order order,OrderStatus orderStatus){
        NotificationType notificationType = switch (orderStatus){

            case CONFIRMED -> NotificationType.ORDER_CONFIRMED;
            case PROCESSING -> NotificationType.ORDER_PROCESSING;
            case SHIPPED -> NotificationType.ORDER_SHIPPED;
            case DELIVERED -> NotificationType.ORDER_DELIVERED;
            case CANCELLED -> NotificationType.ORDER_CANCELLED;

            default -> null;

        };
        notificationFeignClient.createNotification(
                new CreateNotificationDto(
                        order.getCustomerId(),
                        notificationType,
                        "SMS",
                        "Order",
                        "Order state transition",
                        order.getId().toString()
                        )
        );
    }

    @Override
    public Boolean checkOrder(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent() && order.get().getOrderStatus().equals(OrderStatus.CONFIRMED)){
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }
}
