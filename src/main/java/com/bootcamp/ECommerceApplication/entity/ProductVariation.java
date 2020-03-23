package com.bootcamp.ECommerceApplication.entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity_available;
    private Integer price;
    private String primary_image_name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productVariation")
    private List<OrderProduct> orderProducts;

    @OneToMany(mappedBy = "productVariation")
    private List<Cart> carts;

}
