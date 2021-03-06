package com.bootcamp.ECommerceApplication.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Entity
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    private Integer quantityAvailable;
    private Integer price;
    private String metadata;
    private boolean isActive;
    @Transient
    private HashMap metadataHashmap;

    private String primaryImageName;

    @OneToMany(mappedBy = "productVariation")
    private List<OrderProduct> orderProducts;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public String getPrimaryImageName() {
        return primaryImageName;
    }

    public void setPrimaryImageName(String primaryImageName) {
        this.primaryImageName = primaryImageName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public HashMap<String, String> getMetadataHashmap() {
        return metadataHashmap;
    }

    public void setMetadataHashmap(HashMap<String, String> metadataHashmap) {
        this.metadataHashmap = metadataHashmap;
    }

    public void jsonMetadataStringSerialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.metadata = objectMapper.writeValueAsString(metadataHashmap);
    }

    public void jsonMetadataStringDeserialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        metadataHashmap = objectMapper.readValue(this.metadata, HashMap.class);
    }

    @Override
    public String toString() {
        return "ProductVariation{" +
                "id=" + id +
                ", product=" + product +
                ", quantityAvailable=" + quantityAvailable +
                ", price=" + price +
                ", metadata='" + metadata + '\'' +
                ", isActive=" + isActive +
                ", metadataHashmap=" + metadataHashmap +
                ", primaryImageName='" + primaryImageName + '\'' +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
