package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Embeddable
public class CartID implements Serializable {
    @OneToOne
    @JoinColumn(name = "customerUserId")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "productVariationId")
    private ProductVariation productVariation;

//    public CartID(Customer customer, List<ProductVariation> productVariation) {
//        this.customer = customer;
//        this.productVariation = productVariation;
//    }


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

//    public List<ProductVariation> getProductVariation() {
//        return productVariation;
//    }
//
//    public void setProductVariation(List<ProductVariation> productVariation) {
//        this.productVariation = productVariation;
//    }


    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartID)) return false;
        CartID cartID = (CartID) o;
        return getCustomer().equals(cartID.getCustomer()) &&
                getProductVariation().equals(cartID.getProductVariation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getProductVariation());
    }
}
