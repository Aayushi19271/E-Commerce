package com.bootcamp.ECommerceApplication.entity;

import java.io.Serializable;

public class ProductReviewID implements Serializable {
    private Long product;
    private Long customer;

    public ProductReviewID(Long product, Long customer) {
        this.product = product;
        this.customer = customer;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }
}
