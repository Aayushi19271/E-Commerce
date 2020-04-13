package com.bootcamp.ECommerceApplication.dto;

public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }
}
