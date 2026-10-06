package com.catalog.seller.service.implementation;

import com.catalog.exception.ResourceNotFoundException;
import com.catalog.exception.SellerAlreadyExistException;
import com.catalog.seller.dto.CreateSellerRequestDto;
import com.catalog.seller.dto.SellerResponseDto;
import com.catalog.seller.entity.Seller;
import com.catalog.seller.mapper.SellerMapper;
import com.catalog.seller.repository.SellerRepository;
import com.catalog.seller.service.IsellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServiceImpl implements IsellerService {
    Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public SellerResponseDto createSeller(CreateSellerRequestDto sellerRequestDto, String idempotencyKey, String correlationId) {
        logger.info(
                "Inside Seller Service: Creating seller, email={}, correlationId={}",
                sellerRequestDto.email(),
                correlationId
        );

        Optional<Seller> checkWithIdempotencykey = sellerRepository.findByIdempotencyKey(idempotencyKey);
        if(checkWithIdempotencykey.isPresent()){
            logger.info(
                    "Inside Seller Service: Idempotent request detected, sellerId={}, correlationId={}",
                    checkWithIdempotencykey.get().getId(),
                    correlationId
            );
            return SellerMapper.sellertoDtoMapper(checkWithIdempotencykey.get());
        }
        Optional<Seller> seller = sellerRepository.findByEmail(sellerRequestDto.email());
        if(seller.isPresent()){
            logger.warn(
                    "Inside Seller Service: Seller already registered, email={}, correlationId={}",
                    sellerRequestDto.email(),
                    correlationId
            );
            return SellerMapper.sellertoDtoMapper(seller.get());
        }
        Seller sellerToSave = SellerMapper.dtoToSellerMapper(sellerRequestDto,new Seller());
        sellerToSave.setIdempotencyKey(idempotencyKey);
        sellerRepository.save(sellerToSave);

        logger.info(
                "Inside Seller Service: Seller created successfully, sellerId={}, email={}, correlationId={}",
                sellerToSave.getId(),
                sellerToSave.getEmail(),
                correlationId
        );
        return SellerMapper.sellertoDtoMapper(sellerToSave);
    }

    @Override
    public SellerResponseDto fetchSeller(String email, String correlationId) {
        logger.info(
                "Inside Seller Service: Fetching seller, email={}, correlationId={}",
                email,
                correlationId
        );
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if(seller.isEmpty()){
            logger.warn(
                    "Inside Seller Service: Seller not found, email={}, correlationId={}",
                    email,
                    correlationId
            );

            throw  new ResourceNotFoundException(
                    "Seller","email",email
            );
        }
        logger.info(
                "Inside Seller Service: Seller fetched successfully, sellerId={}, correlationId={}",
                seller.get().getId(),
                correlationId
        );
        return SellerMapper.sellertoDtoMapper(seller.get());
    }

    @Override
    public SellerResponseDto updateSeller(CreateSellerRequestDto sellerRequestDto, String email, String correlationId) {
        logger.info(
                "Inside Seller Service: Updating seller, email={}, correlationId={}",
                email,
                correlationId
        );
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if(seller.isEmpty()){
            logger.warn(
                    "Inside Seller Service: Seller not found for update, email={}, correlationId={}",
                    email,
                    correlationId
            );

            throw  new ResourceNotFoundException(
                    "Seller","email",email
            );
        }
        Seller sellerToUpdate = SellerMapper.dtoToSellerMapper(sellerRequestDto,seller.get());
        sellerToUpdate = sellerRepository.save(sellerToUpdate);

        logger.info(
                "Inside Seller Service: Seller updated successfully, sellerId={}, correlationId={}",
                sellerToUpdate.getId(),
                correlationId
        );
        return SellerMapper.sellertoDtoMapper(sellerToUpdate);
    }

    @Override
    public boolean deleteSeller(String email, String correlationId) {
        logger.info(
                "Inside Seller Service: Deleting seller, email={}, correlationId={}",
                email,
                correlationId
        );
        Optional<Seller> seller = sellerRepository.findByEmail(email);
        if(seller.isEmpty()){
            logger.warn(
                    "Inside Seller Service: Seller not found for deletion, email={}, correlationId={}",
                    email,
                    correlationId
            );
            throw  new ResourceNotFoundException(
                    "Seller","email",email
            );
        }
        sellerRepository.deleteByEmail(email);
        logger.info(
                "Inside Seller Service: Seller deleted successfully, sellerId={}, correlationId={}",
                seller.get().getId(),
                correlationId
        );
        return  true;
    }
}
