package com.bootcamp.ECommerceApplication.dto;

import javax.persistence.Transient;
import java.util.HashMap;
import java.util.HashSet;

public class ProductVariationDTO {
    private Integer quantityAvailable;
    private Float price;
    private String metadata;
    private boolean isActive;
    @Transient
    private HashMap metadataHashmap;

    private HashSet<String> primaryImageName;

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public HashMap getMetadataHashmap() {
        return metadataHashmap;
    }

    public void setMetadataHashmap(HashMap metadataHashmap) {
        this.metadataHashmap = metadataHashmap;
    }

    public HashSet<String> getPrimaryImageName() {
        return primaryImageName;
    }

    public void setPrimaryImageName(HashSet<String> primaryImageName) {
        this.primaryImageName = primaryImageName;
    }

    @Override
    public String toString() {
        return "ProductVariationDTO{" +
                "quantityAvailable=" + quantityAvailable +
                ", price=" + price +
                ", metadata='" + metadata + '\'' +
                ", isActive=" + isActive +
                ", metadataHashmap=" + metadataHashmap +
                ", primaryImageName=" + primaryImageName +
                '}';
    }
}
