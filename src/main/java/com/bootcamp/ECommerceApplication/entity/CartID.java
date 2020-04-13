package com.bootcamp.ECommerceApplication.entity;

import java.io.Serializable;


public class CartID implements Serializable {

    private Customer customer;

    private ProductVariation productVariation;


    public CartID(Customer customer, ProductVariation productVariation) {
        this.customer = customer;
        this.productVariation = productVariation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }
}
