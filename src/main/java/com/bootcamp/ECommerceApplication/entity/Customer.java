package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
public class Customer extends User{

    @Pattern(regexp = "^(?:\\s+|)((0|(?:(\\+|)91))(?:\\s|-)*(?:(?:\\d(?:\\s|-)*\\d{9})|(?:\\d{2}(?:\\s|-)*\\d{8})|(?:\\d{3}(?:\\s|-)*\\d{7}))|\\d{10})(?:\\s+|)$"
            ,message = "The Contact No. is not valid")
    @NotEmpty(message = "Contact No is a mandatory field")
    private String contact;

    @OneToOne(mappedBy = "customer",cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<ProductReview> productReviews;

    @OneToOne(mappedBy = "customer")
    private ConfirmationToken confirmationToken;

    public ConfirmationToken getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(ConfirmationToken confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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
                ", cart=" + cart +
                ", productReviews=" + productReviews +
                ", confirmationToken=" + confirmationToken +
                '}';
    }
}
