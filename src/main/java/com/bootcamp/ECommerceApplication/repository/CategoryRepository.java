package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Long> {

    Category findByNameIgnoreCase(String name);

    @Query(value = "select * from category where name=:name",nativeQuery = true)
    List<Map<Object,Object>> findCategory(String name);

    Page<Category> findAll(Pageable paging);

    @Query(value = "select a.id as CategoryID,a.name as CategoryName,b.id as SubCategoryID,b.name as SubCategoryName " +
            "from category a inner join category b " +
            "on a.id=b.parent_id " +
            "where a.id=:value",nativeQuery = true)
    List<Map<Object,Object>> findByRootCategory(Long value);


    @Query(value = "select id as CategoryID,name as CategoryName " +
            "from category " +
            "where parent_id is null",nativeQuery = true)
    List<Map<Object,Object>> findByCategoryIsNull();

}
