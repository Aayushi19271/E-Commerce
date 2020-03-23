package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name="customer_user_id")
public class Cart extends Customer{

    private Integer quantity;
    private Boolean is_wishlist_item;

    @ManyToOne
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;

}