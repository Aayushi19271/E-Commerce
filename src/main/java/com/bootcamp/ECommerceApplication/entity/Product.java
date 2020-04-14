package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sellerUserId")
    private Seller seller;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;

    private Boolean isCancellable=false;
    private Boolean isReturnable=false;
    private String brand;
    private Boolean isActive;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    List<ProductVariation> productVariations;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    List<ProductReview> productReviews;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getCancellable() {
        return isCancellable;
    }

    public void setCancellable(Boolean cancellable) {
        isCancellable = cancellable;
    }

    public Boolean getReturnable() {
        return isReturnable;
    }

    public void setReturnable(Boolean returnable) {
        isReturnable = returnable;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<ProductVariation> getProductVariations() {
        return productVariations;
    }

    public void setProductVariations(List<ProductVariation> productVariations) {
        this.productVariations = productVariations;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", seller=" + seller +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", isCancellable=" + isCancellable +
                ", isReturnable=" + isReturnable +
                ", brand='" + brand + '\'' +
                ", isActive=" + isActive +
                ", productVariations=" + productVariations +
                ", productReviews=" + productReviews +
                '}';
    }
}
