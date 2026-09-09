package com.catalog.seller.service.implementation;

import com.catalog.exception.ResourceNotFoundException;
import com.catalog.exception.SellerAlreadyExistException;
import com.catalog.seller.dto.CreateSellerRequestDto;
import com.catalog.seller.dto.SellerResponseDto;
import com.catalog.seller.entity.Seller;
import com.catalog.seller.mapper.SellerMapper;
import com.catalog.seller.repository.SellerRepository;
import com.catalog.seller.service.IsellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServiceImpl implements IsellerService {

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public void createSeller(CreateSellerRequestDto sellerRequestDto) {
        Optional<Seller> seller = sellerRepository.findByEmail(sellerRequestDto.email());
        if(seller.isPresent()){
            throw  new SellerAlreadyExistException("seller already registered with given mail");
        }
        Seller sellerToSave = SellerMapper.dtoToSellerMapper(sellerRequestDto,new Seller());
        sellerRepository.save(sellerToSave);

    }

    @Override
    public SellerResponseDto fetchSeller(String email) {
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if(seller.isEmpty()){
            throw  new ResourceNotFoundException(
                    "Seller","email",email
            );
        }
        return SellerMapper.sellertoDtoMapper(seller.get());
    }

    @Override
    public SellerResponseDto updateSeller(CreateSellerRequestDto sellerRequestDto, String email) {
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if(seller.isEmpty()){
            throw  new ResourceNotFoundException(
                    "Seller","email",email
            );
        }
        Seller sellerToUpdate = SellerMapper.dtoToSellerMapper(sellerRequestDto,seller.get());
        sellerToUpdate = sellerRepository.save(sellerToUpdate);
        return SellerMapper.sellertoDtoMapper(sellerToUpdate);
    }

    @Override
    public boolean deleteSeller(String email) {
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if(seller.isEmpty()){
            throw  new ResourceNotFoundException(
                    "Seller","email",email
            );
        }
        sellerRepository.deleteByEmail(email);
        return  true;
    }
}
