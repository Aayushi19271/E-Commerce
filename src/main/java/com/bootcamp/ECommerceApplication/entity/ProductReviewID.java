package com.bootcamp.ECommerceApplication.entity;

import java.io.Serializable;

public class ProductReviewID implements Serializable {
    private Product product;
    private Customer customer;

    public ProductReviewID(Product product, Customer customer) {
        this.product = product;
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
