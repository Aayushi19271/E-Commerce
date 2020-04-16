package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {

    Category findByNameIgnoreCase(String name);

    @Query(value = "select * from category where name=:name",nativeQuery = true)
    List<Map<Object,Object>> findCategory(String name);

    @Query(value = "select * from category where leaf_node = true", nativeQuery = true)
    List<Category> findAllCategory(Pageable pageable);

    @Query(value = "select a.id as CategoryID,a.name as CategoryName,b.id as SubCategoryID,b.name as SubCategoryName " +
            "from category a inner join category b " +
            "on a.id=b.parent_id " +
            "where a.id=:value",nativeQuery = true)
    List<Map<Object,Object>> findByRootCategory(Long value);

    @Query(value = "select * from category where name=:name and parent_id=:id", nativeQuery = true)
    Category findByNameAndParent(String name, Long id);


    @Query(value = "select id as CategoryID,name as CategoryName " +
            "from category " +
            "where parent_id is null",nativeQuery = true)
    List<Map<Object,Object>> findByCategoryIsNull();

    @Query(value = "select id, name from category where parent_id=:value", nativeQuery = true)
    List<Map<Object, Object>> findByParentCategory(Long value);


    @Query(value = "select leaf_node from category where id=:id",nativeQuery = true)
    Boolean findByCategoryLeafNode(Long id);


    @Query(value = "select id, name, leaf_node from category where parent_id=:id", nativeQuery = true)
    List<Object> findAllChildrenAdmin(Long id);




}
