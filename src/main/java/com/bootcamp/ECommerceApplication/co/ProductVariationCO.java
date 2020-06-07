package com.bootcamp.ECommerceApplication.co;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class ProductVariationCO {

    @NotNull(message = "Please provide product Id")
    private Long productId;
    @NotNull(message = "Please provide Quantity Available")
    private Integer quantityAvailable;
    @NotNull(message = "Please provide  price")
    private Integer price;
    private HashMap metadataHashmap;

    private boolean isActive;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public HashMap getMetadataHashmap() {
        return metadataHashmap;
    }

    public void setMetadataHashmap(HashMap metadataHashmap) {
        this.metadataHashmap = metadataHashmap;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
