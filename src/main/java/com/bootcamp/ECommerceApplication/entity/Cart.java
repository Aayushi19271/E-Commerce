package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;

@Entity
public class Cart{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    private Boolean is_wishlist_item;

    @ManyToOne
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;

    @ManyToOne
    @JoinColumn(name = "customer_user_id")
    private Customer customer;

}