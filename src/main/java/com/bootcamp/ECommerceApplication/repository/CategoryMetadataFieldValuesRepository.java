package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.CategoryMetadataFieldValues;
import com.bootcamp.ECommerceApplication.entity.CategoryMetadataFieldValuesID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesID> {

    @Query(value = "select a.id AS ID,a.name AS Name,b.name AS MetadataField_Name,c.value AS Value from category a,category_metadata_field b,category_metadata_field_values c\n" +
            "where a.id=c.category_id and b.id=c.category_metadata_field_id",nativeQuery = true)
    List<Map<Object,Object>> findAllCategories();

    @Query(value = "select * from category_metadata_field_values where category_id=:categoryId " +
            "AND category_metadata_field_id=:categoryMetadataFieldId",nativeQuery = true)
    CategoryMetadataFieldValues findCategoryMetadataFieldValues(Long categoryId,Long categoryMetadataFieldId);

    @Query(value = "select "
            + "c.category_metadata_field_id, "
            + "c.value, "
            + "d.name "
            + "from "
            + "category_metadata_field_values c "
            + "join category_metadata_field d ON c.category_metadata_field_id = d.id "
            + "where "
            + "c.category_id=:id", nativeQuery = true)
    List<Map<Object, Object>> findByCategoryId(Long id);

}
