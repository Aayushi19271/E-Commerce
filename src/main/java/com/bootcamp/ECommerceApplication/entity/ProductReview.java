package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@IdClass(ProductReviewID.class)
public class ProductReview{
    @Id
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "customerUserId")
    private Customer customer;

    private String review;

    @Min(value = 0 ,message = "The minimum rating is 0")
    @Max(value = 5 ,message = "The maximum rating is 5")
    private Integer rating;


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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProductReview{" +
                "product=" + product +
                ", customer=" + customer +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }
}
