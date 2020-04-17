package com.bootcamp.ECommerceApplication.co;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.bootcamp.ECommerceApplication.constant.Constants.CONTACT;

public class CustomerUpdateProfileCO extends UserUpdateProfileCO {
    @Pattern(regexp = CONTACT
            ,message = "The Contact No. is not valid")
    @NotNull(message = "Please provide Contact No.")
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "CustomerUpdateProfileCO{" +
                "contact='" + contact + '\'' +
                '}';
    }
}
