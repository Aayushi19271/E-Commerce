package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    private Boolean is_cancellable;
    private Boolean is_returnable;
    private String brand;
    private Boolean is_active;

    @ManyToOne
    @JoinColumn(name="seller_user_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    List<ProductVariation> product_variations;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    List<ProductReview> product_reviews;

//    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
//    List<ProductReviewID> productReviewIDS;

}
