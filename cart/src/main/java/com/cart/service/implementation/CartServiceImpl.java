package com.cart.service.implementation;

import com.cart.dto.cart.CartItemResponseDto;
import com.cart.dto.cart.CartResponseDto;
import com.cart.dto.cart.CreateCartDto;
import com.cart.dto.cart.ResponseDto;
import com.cart.dto.catalog.ProductResponseDto;
import com.cart.entity.Cart;
import com.cart.entity.CartItem;
import com.cart.entity.enums.CartStatus;
import com.cart.exception.DuplicateResourceException;
import com.cart.exception.ResourceNotFoundException;
import com.cart.mapper.CartItemMapper;
import com.cart.mapper.CartMapper;
import com.cart.repository.CartRepository;
import com.cart.service.IcartService;
import com.cart.service.client.CatalogFeignClient;
import com.cart.service.client.CustomerFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CartServiceImpl implements IcartService {

    CartRepository cartRepository;
    CustomerFeignClient customerFeignClient;
    CatalogFeignClient catalogFeignClient;

    @Override
    public void createCart(CreateCartDto createCartDto) {

        Optional<Cart> cart = cartRepository.findByCustomerId(createCartDto.customerId());

        if(cart.isPresent() && cart.get().getCartStatus().equals(CartStatus.ACTIVE)){
            throw new DuplicateResourceException("Cart is already Created and Active for given customerId "+createCartDto.customerId());
        }
        Boolean isPresent  = customerFeignClient.checkCustomerExist(createCartDto.customerId()).getBody();
        if(isPresent.equals(Boolean.FALSE)){
            throw new ResourceNotFoundException("Customer","customerId",createCartDto.customerId().toString());
        }
        Cart cartToSave = CartMapper.cartDtoTOCart(createCartDto,new Cart());
        cartRepository.save(cartToSave);
    }

    @Override
    public CartResponseDto getCart(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart","customerId",customerId.toString())
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
    @Transactional
    @Override
    public ResponseDto deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart","cartId",cartId.toString())
        );
        cart.getItems().clear();
        return new ResponseDto(
                HttpStatus.OK.toString(),
                "cart deleted Successfully"
        );
    }


}
