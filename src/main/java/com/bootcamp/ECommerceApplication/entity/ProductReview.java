package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;

@Entity
public class ProductReview{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String review;
    private String rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_user_id")
    private Customer customer;


}
