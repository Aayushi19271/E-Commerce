package com.bootcamp.ECommerceApplication.co;

import javax.persistence.*;

public class AddressCO {
    private String city;
    private String state;
    private String country;
    private String address;
    private Integer zipCode;
    private String label;

    @ManyToOne(cascade = CascadeType.ALL)
    private UserCO userCO;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UserCO getUserCO() {
        return userCO;
    }

    public void setUserCO(UserCO userCO) {
        this.userCO = userCO;
    }
}
