package com.catalog.product.service.implementation;

import com.catalog.category.entity.Category;
import com.catalog.category.repository.CategoryRepository;
import com.catalog.exception.DuplicateResourceException;
import com.catalog.exception.ResourceNotActiveException;
import com.catalog.exception.ResourceNotFoundException;
import com.catalog.product.dto.inventory.CreateInventoryDto;
import com.catalog.product.dto.product.CreateProductRequestDto;
import com.catalog.product.dto.product.ProductResponseDto;
import com.catalog.product.dto.product.UpdateProductRequestDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements IproductService {

    private static  final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private  final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final InventoryFeignClient inventoryFeignClient;

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto productRequestDto, String idempotencyKey, String correlationId) {
        logger.info(
                "Inside Product Service: Creating product, sellerId={}, sku={}, correlationId={}",
                productRequestDto.sellerId(),
                productRequestDto.sku(),
                correlationId
        );

        Optional<Product> productForIdempotencyKey = productRepository.findByIdempotencyKey(idempotencyKey);
        if(productForIdempotencyKey.isPresent()){
            logger.info(
                    "Inside Product Service: Idempotent product request detected, productId={}, correlationId={}",
                    productForIdempotencyKey.get().getId(),
                    correlationId
            );
            return ProductMapper.productEntityTOResponseDto(productForIdempotencyKey.get());
        }
        Optional<Seller> seller = sellerRepository.findById(productRequestDto.sellerId());
        if(seller.isEmpty()){
            logger.warn(
                    "Inside Product Service: Seller not found, sellerId={}, correlationId={}",
                    productRequestDto.sellerId(),
                    correlationId
            );
            throw new ResourceNotFoundException("Seller","SellerId" ,productRequestDto.sellerId().toString());
        }
        Optional<Category> category = categoryRepository.findById(productRequestDto.categoryId());
        if(category.isEmpty()){
            logger.warn(
                    "Inside Product Service: Category not found, categoryId={}, correlationId={}",
                    productRequestDto.categoryId(),
                    correlationId
            );
            throw new ResourceNotFoundException("Category","categoryId" ,productRequestDto.categoryId().toString());
        }

        if(productRepository.existsBySellerIdAndSku(productRequestDto.sellerId(),productRequestDto.sku())){
            logger.warn(
                    "Inside Product Service: Product SKU already exists, sellerId={}, sku={}, correlationId={}",
                    productRequestDto.sellerId(),
                    productRequestDto.sku(),
                    correlationId
            );
            throw new DuplicateResourceException(
                    "SKU '" + productRequestDto.sku() +
                            "' already exists for seller with id: " +
                            productRequestDto.sellerId()
            );
        }
        Product product = ProductMapper.productCreateDtoToEntity(productRequestDto, new Product());
        try {
            product.setCategory(category.get());
            product.setSeller(seller.get());
            product.setIdempotencyKey(idempotencyKey);
            productRepository.save(product);

            logger.info(
                    "Inside Product Service: Product created, productId={}, correlationId={}",
                    product.getId(),
                    correlationId
            );


            inventoryFeignClient.createInventory(
                    new CreateInventoryDto(
                            product.getId(),
                            productRequestDto.availableQuantity(),
                            productRequestDto.lowStockThreshold()
                    )
            );
            logger.info(
                    "Inside Product Service: Inventory created successfully, productId={}, correlationId={}",
                    product.getId(),
                    correlationId
            );
        } catch (Exception e) {
            logger.error(
                    "Inside Product Service: Product/Inventory creation failed, productId={}, correlationId={}, message={}",
                    product.getId(),
                    correlationId,
                    e.getMessage(),
                    e
            );
            productRepository.deleteById(product.getId());
            throw new RuntimeException("Product creation failed: " + e.getMessage());

        }
        return ProductMapper.productEntityTOResponseDto(product);

    }

    @Override
    public void createProductInBulk(
            List<CreateProductRequestDto> createProductRequestDtoList,
            String idempotencyKey,
            String correlationId) {

        logger.info(
                "Inside Product Service: Starting bulk product creation, productCount={}, correlationId={}",
                createProductRequestDtoList.size(),
                correlationId
        );

        for (int i = 0; i < createProductRequestDtoList.size(); i++) {

            CreateProductRequestDto request =
                    createProductRequestDtoList.get(i);

            try {
                String productIdempotencyKey =
                        idempotencyKey + "_product_" + i;

                createProduct(
                        request,
                        productIdempotencyKey,
                        correlationId
                );

                logger.info(
                        "Inside Product Service: Bulk product created successfully, index={}, sellerId={}, sku={}, correlationId={}",
                        i,
                        request.sellerId(),
                        request.sku(),
                        correlationId
                );

            } catch (Exception e) {

                logger.warn(
                        "Inside Product Service: Bulk product creation failed, index={}, sellerId={}, sku={}, correlationId={}, message={}",
                        i,
                        request.sellerId(),
                        request.sku(),
                        correlationId,
                        e.getMessage()
                );
            }
        }

        logger.info(
                "Inside Product Service: Bulk product creation completed, productCount={}, correlationId={}",
                createProductRequestDtoList.size(),
                correlationId
        );
    }

    @Override
    public ProductResponseDto updateProduct(
            UpdateProductRequestDto productRequestDto,
            Long id,
            String correlationId) {

        logger.info(
                "Inside Product Service: Updating product, productId={}, correlationId={}",
                id,
                correlationId
        );

        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            logger.warn(
                    "Inside Product Service: Product not found, productId={}, correlationId={}",
                    id,
                    correlationId
            );

            throw new ResourceNotFoundException(
                    "Product",
                    "Id",
                    id.toString()
            );
        }

        Optional<Category> category =
                categoryRepository.findById(productRequestDto.categoryId());

        if (category.isEmpty()) {
            logger.warn(
                    "Inside Product Service: Category not found while updating product, categoryId={}, productId={}, correlationId={}",
                    productRequestDto.categoryId(),
                    id,
                    correlationId
            );

            throw new ResourceNotFoundException(
                    "Category",
                    "categoryId",
                    productRequestDto.categoryId().toString()
            );
        }

        ProductMapper.productUpdateDtoToEntity(
                productRequestDto,
                product.get()
        );

        product.get().setCategory(category.get());

        productRepository.save(product.get());

        logger.info(
                "Inside Product Service: Product updated successfully, productId={}, correlationId={}",
                id,
                correlationId
        );

        return ProductMapper.productEntityTOResponseDto(product.get());
    }

    @Override
    public ProductResponseDto getProductById(Long id, String correlationId) {

        logger.info(
                "Inside Product Service: Fetching product, productId={}, correlationId={}",
                id,
                correlationId
        );

        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            logger.warn(
                    "Inside Product Service: Product not found, productId={}, correlationId={}",
                    id,
                    correlationId
            );

            throw new ResourceNotFoundException(
                    "Product",
                    "Id",
                    id.toString()
            );
        }

        if (!ProductStatus.ACTIVE.equals(product.get().getStatus())) {
            logger.warn(
                    "Inside Product Service: Product is not active, productId={}, status={}, correlationId={}",
                    id,
                    product.get().getStatus(),
                    correlationId
            );

            throw new ResourceNotActiveException(
                    "product",
                    "productId",
                    id.toString()
            );
        }

        logger.info(
                "Inside Product Service: Product fetched successfully, productId={}, correlationId={}",
                id,
                correlationId
        );

        return ProductMapper.productEntityTOResponseDto(product.get());
    }

    @Override
    public Slice<ProductResponseDto> listProduduct(String correlationId, ProductStatus status, Long sellerId, Long categoryId, String name, BigDecimal minPrice, BigDecimal maxPrice, Long cursor, int limit) {

        Specification<Product> specification =  Specification
                .where(ProductSpecification.hasStatus(status))
                .and(ProductSpecification.hasCategoryId(categoryId))
                .and(ProductSpecification.hasSellerId(sellerId))
                .and(ProductSpecification.nameContains(name))
                .and(ProductSpecification.priceGreaterThanOrEqual(minPrice))
                .and(ProductSpecification.priceLessThanOrEqual(maxPrice))
                .and(ProductSpecification.hasCursor(cursor));

        logger.info(
                "Inside Product Service: Fetching products, status={}, sellerId={}, categoryId={}, cursor={}, limit={}, correlationId={}",
                status,
                sellerId,
                categoryId,
                cursor,
                limit,
                correlationId
        );

        Pageable pageable = PageRequest.of(
                0,limit,
                Sort.by(Sort.Direction.ASC,"id")
        );

        Slice<Product> listProducts = productRepository.findAll(
                specification,
                pageable
        );
        logger.info(
                "Inside Product Service: Products fetched successfully, resultCount={}, hasNext={}, correlationId={}",
                listProducts.getNumberOfElements(),
                listProducts.hasNext(),
                correlationId
        );
        return  listProducts.map(ProductMapper::productEntityTOResponseDto);

    }

    @Override
    public boolean deleteProduct(Long id, String correlationId) {
        logger.info(
                "Inside Product Service: Deactivating product, productId={}, correlationId={}",
                id,
                correlationId
        );
        Product product = productRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn(
                            "Inside Product Service: Product not found, productId={}, correlationId={}",
                            id,
                            correlationId
                    );
                    return new ResourceNotFoundException("product", "id", id.toString());
                }
        );
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
        logger.info(
                "Inside Product Service: Product deactivated successfully, productId={}, correlationId={}",
                id,
                correlationId
        );
        return true;
    }

//    @Override
//    public Page<ProductResponseDto> listProduduct(Pageable pageable) {
//        Page<Product> products = productRepository.findByStatus(Status.ACTIVE,pageable);
//        return products.map(ProductMapper::productEntityTOResponseDto);
//    }
}
