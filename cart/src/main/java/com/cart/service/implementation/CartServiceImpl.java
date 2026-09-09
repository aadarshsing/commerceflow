package com.cart.service.implementation;

import com.cart.dto.CartResponseDto;
import com.cart.dto.CreateCartDto;
import com.cart.entity.Cart;
import com.cart.entity.enums.CartStatus;
import com.cart.exception.DuplicateResourceException;
import com.cart.exception.ResourceNotFoundException;
import com.cart.mapper.CartMapper;
import com.cart.repository.CartRepository;
import com.cart.service.IcartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class CartServiceImpl implements IcartService {

    CartRepository cartRepository;

    @Override
    public void createCart(CreateCartDto createCartDto) {

        Optional<Cart> cart = cartRepository.findByCustomerId(createCartDto.customerId());

        if(cart.isPresent() && cart.get().getCartStatus().equals(CartStatus.ACTIVE)){
            throw new DuplicateResourceException("Cart is already Created and Active for given customerId "+createCartDto.customerId());
        }
        Cart cartToSave = CartMapper.cartDtoTOCart(createCartDto,new Cart());
        cartRepository.save(cartToSave);
    }

    @Override
    public CartResponseDto getCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart","customerId",customerId.toString())
        );
        return CartMapper.CartToDtoMapper(cart);

    }


}
