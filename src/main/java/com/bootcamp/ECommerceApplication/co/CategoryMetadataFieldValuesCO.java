package com.bootcamp.ECommerceApplication.co;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryMetadataFieldValuesCO {
    @NotNull(message = "Please provide valid Category Metadata Field ")
    private Long categoryMetadataFieldId;
    @NotNull(message = "Please provide valid Category")
    private Long categoryId;
    @NotNull(message = "Please provide value")
    @NotBlank(message = "Please provide valid value")
    private String value;

    public Long getCategoryMetadataFieldId() {
        return categoryMetadataFieldId;
    }

    public void setCategoryMetadataFieldId(Long categoryMetadataFieldId) {
        this.categoryMetadataFieldId = categoryMetadataFieldId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
