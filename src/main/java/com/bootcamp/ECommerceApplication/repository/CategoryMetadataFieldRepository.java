package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.CategoryMetadataField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryMetadataFieldRepository extends PagingAndSortingRepository<CategoryMetadataField,Long> {

    CategoryMetadataField findByNameIgnoreCase(String name);

    Page<CategoryMetadataField> findAll(Pageable paging);
}
