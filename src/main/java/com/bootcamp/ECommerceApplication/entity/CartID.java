package com.bootcamp.ECommerceApplication.entity;

import java.io.Serializable;


public class CartID implements Serializable {

    private Long customer;

    private Long productVariation;

    public CartID(Long customer, Long productVariation) {
        this.customer = customer;
        this.productVariation = productVariation;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(Long productVariation) {
        this.productVariation = productVariation;
    }
}
