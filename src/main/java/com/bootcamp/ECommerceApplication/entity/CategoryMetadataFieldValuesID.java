package com.bootcamp.ECommerceApplication.entity;

import java.io.Serializable;

public class CategoryMetadataFieldValuesID implements Serializable {

    private Long categoryMetadataField;

    private Long category;

    public CategoryMetadataFieldValuesID() {
    }

    public CategoryMetadataFieldValuesID(Long categoryMetadataField, Long category) {
        this.categoryMetadataField = categoryMetadataField;
        this.category = category;
    }

    public Long getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(Long categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }
}
