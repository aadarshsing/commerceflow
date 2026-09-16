package com.order.mapper;

import com.order.dto.*;
import com.order.dto.cart.CartItemResponseDto;
import com.order.dto.cart.CartResponseDto;
import com.order.dto.order.BuyNowRequest;
import com.order.dto.order.OrderItemResponseDto;
import com.order.dto.order.OrderResponseDto;
import com.order.entity.Order;
import com.order.entity.OrderAddress;
import com.order.entity.OrderItem;
import com.order.entity.enums.OrderStatus;

import java.math.BigDecimal;

public class OrderMapper {


    public  static Order cartToOrderMapper(CartResponseDto cartResponseDto, Order order){

        order.setOrderStatus(OrderStatus.CREATED);
        order.setCustomerId(cartResponseDto.getCustomerId());
        return order;

    }

    public static OrderItem cartItemToOrderItemMapper(CartItemResponseDto cartItem, OrderItem orderItem, ProductResponseDto product){

        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setUnitPrice(product.price());
        orderItem.setProductId(product.id());
        BigDecimal totalPrice = product.price().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        orderItem.setTotalPrice(totalPrice);

        return  orderItem;

    }
    public  static OrderItem buyNowRequestToOrderMapper(BuyNowRequest buyNowRequest, OrderItem orderItem, ProductResponseDto product){

        orderItem.setQuantity(buyNowRequest.quantity());orderItem.setUnitPrice(product.price());
        orderItem.setProductId(product.id());
        BigDecimal totalPrice = product.price().multiply(BigDecimal.valueOf(buyNowRequest.quantity()));
        orderItem.setTotalPrice(totalPrice);

        return  orderItem;

    }
    public  static OrderAddress shippingAddressToOrderAddress(AddressResponseDto address,OrderAddress orderAddress){

        orderAddress.setAddressLine1(address.addressLine1());
        orderAddress.setAddressLine2(address.addressLine2());
        orderAddress.setCity(address.city());
        orderAddress.setCountry(address.country());
        orderAddress.setState(address.state());
        orderAddress.setPostalCode(address.postalCode());
        orderAddress.setPhoneNumber(address.phoneNumber());

        return  orderAddress;
    }

    public  static OrderResponseDto orderToOrderResponseDtoMapper(Order order){

       return new OrderResponseDto(
                order.getId(),
                order.getCustomerId(),
                order.getOrderStatus(),
                order.getTotalAmount(),
                AddressMapper.orderAddressToAddressResponseDtoMapper(order.getShippingAddress()),
                order.getOrderItems().stream().map(OrderMapper::orderItemToOrderItemResponseDtoMapper).toList(),
                order.getCreatedAt()
        );
    }
    public static OrderItemResponseDto orderItemToOrderItemResponseDtoMapper(OrderItem item){

        return  new OrderItemResponseDto(
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}
