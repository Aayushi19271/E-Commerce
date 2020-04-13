package com.bootcamp.ECommerceApplication.entity;


import javax.persistence.*;

@Entity
@IdClass(CategoryMetadataFieldValuesID.class)
public class CategoryMetadataFieldValues {

    @Id
    @ManyToOne
    @JoinColumn(name = "CategoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    @Id
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;

    private String value;

    public CategoryMetadataField getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(CategoryMetadataField categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CategoryMetadataFieldValues{" +
                "categoryMetadataField=" + categoryMetadataField +
                ", category=" + category +
                ", value='" + value + '\'' +
                '}';
    }
}
