package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {

    @Query(value = "select * from product where brand=:brand AND category_id=:category AND seller_user_id=:seller",nativeQuery = true)
    Product findOneProductByCombination(String brand,Long category,Long seller);

    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active,b.name AS CategoryName " +
            "FROM product a inner join category b " +
            "ON a.category_id = b.id " +
            "Where a.seller_user_id=:sellerId " +
            "AND a.id=:productId AND is_deleted=false",nativeQuery = true)
    List<Map<Object,Object>> listOneProduct(Long sellerId, Long productId);

    @Query(value = "select a.name AS ProductName,a.description,a.brand,a.is_cancellable,a.is_returnable,a.is_active,b.name AS CategoryName " +
            "FROM product a inner join category b " +
            "ON a.category_id = b.id " +
            "Where a.seller_user_id=:sellerId  AND is_deleted=false",nativeQuery = true)
    List<Map<Object,Object>> listAllProduct(Long sellerId, Pageable paging);


    @Query(value = "select id from product where id=:productId AND seller_user_id=:sellerId  AND is_deleted=false ",nativeQuery = true)
    Long findOneProduct(Long sellerId, Long productId);



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
    List<Map<Object,Object>> listAllProductAdmin(Pageable paging);


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
    List<Map<Object,Object>> listAllProductCustomer(Pageable paging, Long id);

    @Query(value = "select brand from product where category_id=:id", nativeQuery = true)
    List<Object> findAllBrandsByCategoryId(Long id);

    @Query(value = "select min(a.price) "
            + "from product_variation a "
            + "join product b "
            + "on a.product_id=b.id "
            + "where b.category_id=:id", nativeQuery = true)
    Object findMinimum(Long id);

    @Query(value = "select max(a.price) "
            + "from product_variation a "
            + "join product b "
            + "on a.product_id=b.id "
            + "where b.category_id=:id", nativeQuery = true)
    Object findMaximum(Long id);


}
