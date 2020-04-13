package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;

@Entity
@IdClass(CartID.class)
public class Cart {

    @Id
    @ManyToOne
    @JoinColumn(name = "CustomerUserId")
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "ProductVariationId")
    private ProductVariation productVariation;

    private Integer quantity;
    private Boolean isWishlistItem;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getWishlistItem() {
        return isWishlistItem;
    }

    public void setWishlistItem(Boolean wishlistItem) {
        isWishlistItem = wishlistItem;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "customer=" + customer +
                ", productVariation=" + productVariation +
                ", quantity=" + quantity +
                ", isWishlistItem=" + isWishlistItem +
                '}';
    }
}