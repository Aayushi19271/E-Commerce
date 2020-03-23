package com.bootcamp.ECommerceApplication.entity;


import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    private Double price;
    private String status;
    private String product_variation_metadata;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder customerOrder;

    @ManyToOne
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;

}
