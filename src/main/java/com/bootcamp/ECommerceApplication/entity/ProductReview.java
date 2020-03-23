package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "customer_user_id")
public class ProductReview extends User{

    private String review;
    private String rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
