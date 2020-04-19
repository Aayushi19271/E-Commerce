package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.CategoryMetadataFieldValues;
import com.bootcamp.ECommerceApplication.entity.CategoryMetadataFieldValuesID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface CategoryMetadataFieldValuesRepository extends CrudRepository<CategoryMetadataFieldValues, CategoryMetadataFieldValuesID> {

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
