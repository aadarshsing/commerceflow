package com.cart.service.implementation;


import com.cart.dto.*;
import com.cart.entity.Cart;
import com.cart.entity.CartItem;
import com.cart.entity.enums.CartStatus;
import com.cart.entity.enums.ProductStatus;
import com.cart.exception.ResourceNotActiveException;
import com.cart.exception.ResourceNotAvailableException;
import com.cart.exception.ResourceNotFoundException;
import com.cart.mapper.CartItemMapper;
import com.cart.mapper.CartMapper;
import com.cart.repository.CartItemRepository;
import com.cart.repository.CartRepository;
import com.cart.service.ICartItemService;
import com.cart.service.client.CatalogFeignClient;
import com.cart.service.client.InventoryFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements ICartItemService {

    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    CatalogFeignClient catalogFeignClient;
    InventoryFeignClient inventoryFeignClient;


    @Override
    public void createCartItem(Long cartId, CreateCartItemDto createCartItemDto) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()->new ResourceNotFoundException("Cart","cartId",cartId.toString())

        );
        if(!cart.getCartStatus().equals(CartStatus.ACTIVE)){
            throw new ResourceNotActiveException("Cart","CartId",cartId.toString());
        }
        ProductResponseDto product = catalogFeignClient.getProductById(createCartItemDto.productId()).getBody();
        if(product == null){
            throw new ResourceNotFoundException("Product","productId",createCartItemDto.productId().toString());
        }
        if(product.status().equals(ProductStatus.INACTIVE)){
            throw  new ResourceNotActiveException("Product","productId",createCartItemDto.productId().toString());
        }

        InventoryResponseDto inventoryResponseDto = inventoryFeignClient.getInventory(createCartItemDto.productId()).getBody();
        int availableQuantity = inventoryResponseDto.availableQuantity();
        if(createCartItemDto.quantity() > availableQuantity){
            throw new ResourceNotAvailableException("Inventory","availableQuantity",String.valueOf(availableQuantity));
        }

        Optional<CartItem> cartItem = cartItemRepository.findByProductIdAndCart(createCartItemDto.productId(),cartId);
        if(cartItem.isPresent()){
                int quantity = cartItem.get().getQuantity()  + createCartItemDto.quantity();
                cartItem.get().setQuantity(quantity);
                cartItemRepository.save(cartItem.get());
        }
        else{
            CartItem cartItem1 = CartItemMapper.cartItemDtoToCartMapper(new CartItem(),createCartItemDto);
            cartItem1.setCart(cart);
            cartItem1.setProductId(createCartItemDto.productId());
            cartItem1.setUnitPrice(product.price());
            cartItemRepository.save(cartItem1);
        }
    }

    @Override
    public CartResponseDto getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()->new ResourceNotFoundException("Cart","cartId",cartId.toString())
        );
        CartResponseDto cartResponseDto = CartMapper.CartToDtoMapper(cart);
        List<CartItemResponseDto> cartItemList = new ArrayList<>();
        for(CartItem cartItem : cart.getItems()){
            ProductResponseDto product = catalogFeignClient.getProductById(cartItem.getProductId()).getBody();
            if(product == null){
                throw new ResourceNotFoundException("Product","productId",cartItem.getProductId().toString());
            }
            CartItemResponseDto cartItemResponseDto = CartItemMapper.cartItemToDtoMapper(cartItem);
            cartItemResponseDto.setProductResponseDto(product);
            cartItemList.add(cartItemResponseDto);

        }
        cartResponseDto.setItems(cartItemList);
        return cartResponseDto;

    }

    @Override
    public CartResponseDto updateCartItem(Long cartId, Long itemId, UpdateCartItemDto cartItemDto) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()->new ResourceNotFoundException("Cart","cartId",cartId.toString())

        );
        if(cart.getCartStatus().equals(CartStatus.ACTIVE)){
            CartItem cartItem = cartItemRepository.findByIdAndCart(itemId,cartId).orElseThrow(
                    ()->new ResourceNotFoundException("CartItem","itemId",itemId.toString())
            );
            CartItemMapper.updateCartItemDtoToCartMapper(cartItem, cartItemDto);
            cartItemRepository.save(cartItem);
            return CartMapper.CartToDtoMapper(cart);
        }
        else {
            throw new ResourceNotActiveException("Cart","CartId",cartId.toString());
        }

    }

    @Override
    public void deleteCartItem(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()->new ResourceNotFoundException("Cart","cartId",cartId.toString())

        );
        if(cart.getCartStatus().equals(CartStatus.ACTIVE)){
            CartItem cartItem = cartItemRepository.findByIdAndCart(itemId,cartId).orElseThrow(
                    ()->new ResourceNotFoundException("CartItem","itemId & cartId",itemId.toString() + " " +cartId)
            );
            cartItemRepository.deleteById(itemId);
        }
        else {
            throw new ResourceNotActiveException("Cart","CartId",cartId.toString());
        }
    }


}
