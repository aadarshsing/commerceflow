package com.catalog.product.service.implementation;

import com.catalog.category.entity.Category;
import com.catalog.category.repository.CategoryRepository;
import com.catalog.exception.DuplicateResourceException;
import com.catalog.exception.ResourceNotActiveException;
import com.catalog.exception.ResourceNotFoundException;
import com.catalog.product.dto.CreateInventoryDto;
import com.catalog.product.dto.CreateProductRequestDto;
import com.catalog.product.dto.ProductResponseDto;
import com.catalog.product.dto.UpdateProductRequestDto;
import com.catalog.product.entity.Product;
import com.catalog.product.entity.enums.ProductStatus;
import com.catalog.product.mapper.ProductMapper;
import com.catalog.product.repository.ProductRepository;
import com.catalog.product.repository.specification.ProductSpecification;
import com.catalog.product.service.IproductService;
import com.catalog.product.service.client.InventoryFeignClient;
import com.catalog.seller.entity.Seller;
import com.catalog.seller.repository.SellerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements IproductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    SellerRepository sellerRepository;
    InventoryFeignClient inventoryFeignClient;

    @Override
    public void createProduct(CreateProductRequestDto productRequestDto) {

        Optional<Seller> seller = sellerRepository.findById(productRequestDto.sellerId());
        if(seller.isEmpty()){
            throw new ResourceNotFoundException("Seller","SellerId" ,productRequestDto.sellerId().toString());
        }
        Optional<Category> category = categoryRepository.findById(productRequestDto.categoryId());
        if(category.isEmpty()){
            throw new ResourceNotFoundException("Category","categoryId" ,productRequestDto.categoryId().toString());
        }

        if(productRepository.existsBySellerIdAndSku(productRequestDto.sellerId(),productRequestDto.sku())){
            throw new DuplicateResourceException(
                    "SKU '" + productRequestDto.sku() +
                            "' already exists for seller with id: " +
                            productRequestDto.sellerId()
            );
        }
        Product product = ProductMapper.productCreateDtoToEntity(productRequestDto, new Product());
        product.setCategory(category.get());
        product.setSeller(seller.get());
        productRepository.save(product);
        try {
            inventoryFeignClient.createInventory(
                    new CreateInventoryDto(
                        product.getId(),
                        productRequestDto.availableQuantity(),
                        productRequestDto.lowStockThreshold()
                    )
            );
        } catch (Exception e) {
            productRepository.deleteById(product.getId());
            throw new RuntimeException(
                    "Unable to create Inventory, product creation deleted ",
                    e
            );
        }

    }

    @Override
    public ProductResponseDto updateProduct(UpdateProductRequestDto productRequestDto, Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ResourceNotFoundException("Product","Id",id.toString());
        }
        Optional<Category> category = categoryRepository.findById(productRequestDto.categoryId());
        if(category.isEmpty()){
            throw new ResourceNotFoundException("Category","categoryId" ,productRequestDto.categoryId().toString());
        }
        ProductMapper.productUpdateDtoToEntity(productRequestDto,product.get());
        product.get().setCategory(category.get());
        productRepository.save(product.get());

        return  ProductMapper.productEntityTOResponseDto(product.get());

    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ResourceNotFoundException("Product","Id",id.toString());
        }
        else if(!product.get().getStatus().equals(ProductStatus.ACTIVE)){
            throw new ResourceNotActiveException("product","productId",id.toString());
        }
        return ProductMapper.productEntityTOResponseDto(product.get());
    }

    @Override
    public Page<ProductResponseDto> listProduduct(ProductStatus status, Long sellerId, Long categoryId, String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        Specification<Product> specification =  Specification
                .where(ProductSpecification.hasStatus(status))
                .and(ProductSpecification.hasCategoryId(categoryId))
                .and(ProductSpecification.hasSellerId(sellerId))
                .and(ProductSpecification.nameContains(name))
                .and(ProductSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ProductSpecification.priceLessThanOrEqual(maxPrice));

        Pageable pageable1 = PageRequest.of(
                4,10,
                Sort.by(Sort.Direction.ASC,"name")
        );

        Page<Product> listProducts = productRepository.findAll(
                specification,
                pageable
        );
        return  listProducts.map(ProductMapper::productEntityTOResponseDto);

    }

    @Override
    public boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () ->    new ResourceNotFoundException("product","id",id.toString())
        );
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
        return true;
    }

//    @Override
//    public Page<ProductResponseDto> listProduduct(Pageable pageable) {
//        Page<Product> products = productRepository.findByStatus(Status.ACTIVE,pageable);
//        return products.map(ProductMapper::productEntityTOResponseDto);
//    }
}
