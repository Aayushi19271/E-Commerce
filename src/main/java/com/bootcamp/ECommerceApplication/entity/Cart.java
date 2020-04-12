package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;

@Entity
public class Cart {
    @EmbeddedId
    private CartID cartID;

    private Integer quantity;
    private Boolean isWishlistItem;


    public CartID getCartID() { return cartID; }

    public void setCartID(CartID cartID) { this.cartID = cartID; }

    public Customer getCustomer() {
        return cartID.getCustomer();
    }

//    public List<ProductVariation> getProductVariation() {
//        return cartID.getProductVariation();
//    }

    public ProductVariation getProductVariation() {
        return cartID.getProductVariation();
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

}