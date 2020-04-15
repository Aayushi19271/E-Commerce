package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Category;
import com.bootcamp.ECommerceApplication.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends CrudRepository<Product,Long> {

    @Query(value = "select * from product where brand=:brand AND category_id=:category AND seller_user_id=:seller",nativeQuery = true)
    Product findOneProduct(String brand,Long category,Long seller);

    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active,b.name AS CategoryName " +
            "FROM product a inner join category b " +
            "ON a.category_id = b.id " +
            "Where a.seller_user_id=:sellerId " +
            "AND a.id=:productId",nativeQuery = true)
    List<Map<Object,Object>> listOneProduct(Long sellerId, Long productId);

    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active,b.name AS CategoryName " +
            "FROM product a inner join category b " +
            "ON a.category_id = b.id " +
            "Where a.seller_user_id=:sellerId",nativeQuery = true)
    List<Map<Object,Object>> listAllProduct(Long sellerId);


    @Modifying
    @Query(value = "delete from product where id=:id",nativeQuery = true)
    void deleteByProductID(Long id);


    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active As ProductActive," +
            "b.quantity_available,b.price,b.primary_image_name,b.is_active As ProductVariationActive," +
            "c.name As CategoryName from " +
            "product_variation b INNER JOIN product a " +
            "ON b.product_id=a.id " +
            "INNER JOIN category c " +
            "ON a.category_id=c.id " +
            "where a.id=:productId",nativeQuery = true)
    List<Map<Object,Object>> listOneProductAdmin(Long productId);


    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active As ProductActive," +
            "b.quantity_available,b.price,b.primary_image_name,b.is_active As ProductVariationActive," +
            "c.name As CategoryName from " +
            "product_variation b INNER JOIN product a " +
            "ON b.product_id=a.id " +
            "INNER JOIN category c " +
            "ON a.category_id=c.id",nativeQuery = true)
    Page<Product> listAllProductAdmin(Pageable paging);


    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active As ProductActive," +
            "b.quantity_available,b.price,b.primary_image_name,b.is_active As ProductVariationActive," +
            "c.name As CategoryName from " +
            "product_variation b INNER JOIN product a " +
            "ON b.product_id=a.id " +
            "INNER JOIN category c " +
            "ON a.category_id=c.id " +
            "where a.id=:productId AND a.is_active=true",nativeQuery = true)
    List<Map<Object,Object>> listOneProductCustomer(Long productId);



    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active As ProductActive," +
            "b.quantity_available,b.price,b.primary_image_name,b.is_active As ProductVariationActive," +
            "c.name As CategoryName from " +
            "product_variation b INNER JOIN product a " +
            "ON b.product_id=a.id " +
            "INNER JOIN category c " +
            "ON a.category_id=c.id " +
            "WHERE c.leaf_node=true AND c.id=:id",nativeQuery = true)
    Page<Product> listAllProductCustomer(Pageable paging, Long id);
}
