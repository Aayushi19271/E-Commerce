package com.bootcamp.ECommerceApplication.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class Customer extends User{

    private String contact;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<ProductReview> productReviews;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "contact='" + contact + '\'' +
                ", productReviews=" + productReviews +
                '}';
    }
}
