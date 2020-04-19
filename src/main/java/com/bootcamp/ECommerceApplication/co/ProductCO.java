package com.bootcamp.ECommerceApplication.co;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class ProductCO {
    @NotNull(message = "Please provide Name")
    @NotBlank(message = "Please provide valid Name")
    private String name;
    private String description;
    @JsonProperty
    private boolean isCancellable=false;
    @JsonProperty
    private boolean isReturnable=false;

    @NotNull(message = "Please provide Category")
    private Long category;

    @NotNull(message = "Please provide Brand")
    @NotBlank(message = "Please provide valid Brand")
    private String brand;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public void setCancellable(boolean cancellable) {
        isCancellable = cancellable;
    }

    public boolean isReturnable() {
        return isReturnable;
    }

    public void setReturnable(boolean returnable) {
        isReturnable = returnable;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "ProductCO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isCancellable=" + isCancellable +
                ", isReturnable=" + isReturnable +
                ", category=" + category +
                ", brand='" + brand + '\'' +
                '}';
    }
}
