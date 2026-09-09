package com.catalog.product.repository.specification;

import com.catalog.product.entity.Product;
import com.catalog.product.entity.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasStatus(ProductStatus status) {

        return ((root, query, criteriaBuilder) ->
                status == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("status"),
                        status
                )

        );
    }
    public static Specification<Product> hasCategoryId(Long categoryId) {

        return (root, query, criteriaBuilder) ->
                categoryId == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("category").get("id"),
                        categoryId
                );
    }

    public static Specification<Product> hasSellerId(Long sellerId) {

        return (root, query, criteriaBuilder) ->
                sellerId == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("seller").get("id"),
                        sellerId
                );
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null
                        ? null
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }
    public static Specification<Product> priceGreaterThanOrEqual(BigDecimal minPrice) {

        return (root, query, criteriaBuilder) ->
                minPrice == null
                        ? null
                        : criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        minPrice
                );
    }
    public static Specification<Product> priceLessThanOrEqual(BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) ->
                maxPrice == null
                        ? null
                        : criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        maxPrice
                );
    }

}
