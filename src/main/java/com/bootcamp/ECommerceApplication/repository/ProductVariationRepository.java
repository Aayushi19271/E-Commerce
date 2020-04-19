package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;
import java.util.Map;

public interface ProductVariationRepository extends PagingAndSortingRepository<ProductVariation,Long> {

    @Query(value = "SELECT a.id AS ProductVariationID,a.quantity_available,a.price,a.is_active,b.name As ProductName," +
            "b.description,b.brand FROM product_variation a INNER JOIN product b " +
            "ON a.product_id=b.id " +
            "WHERE b.seller_user_id=:sellerId AND " +
            "a.id=:id",nativeQuery = true)
    List<Map<Object,Object>> findOneProductVariationById(Long sellerId,Long id);

    @Query(value = "SELECT a.id AS ProductVariationID,a.quantity_available,a.price,a.is_active,b.name As ProductName," +
            "b.description,b.brand FROM product_variation a INNER JOIN product b " +
            "ON a.product_id=b.id " +
            "WHERE b.seller_user_id=:sellerId",nativeQuery = true)
    List<Map<Object,Object>> findAll(Pageable paging,Long sellerId);

    @Query(value = "SELECT a.id " +
            "FROM product_variation a INNER JOIN product b " +
            "ON a.product_id=b.id " +
            "WHERE b.seller_user_id=:sellerId AND a.id=:id",nativeQuery = true)
    Long findAllBySellerId(Long sellerId, Long id);


}
