package com.bootcamp.ECommerceApplication.dto;

public class CustomerDTO extends UserDTO {
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}