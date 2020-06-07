package com.bootcamp.ECommerceApplication.co;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddressCO {
    @NotNull(message = "Please provide City")
    @NotBlank(message = "Please provide valid City")
    private String city;

    @NotNull(message = "Please provide State")
    @NotBlank(message = "Please provide valid State")
    private String state;

    @NotNull(message = "Please provide Country")
    @NotBlank(message = "Please provide valid Country")
    private String country;

    @NotNull(message = "Please provide Address Line")
    @NotBlank(message = "Please provide valid  Address Line")
    private String addressLine;

    @NotBlank(message = "Please provide valid  Zip-Code")
    @NotNull(message = "Please provide Zip-Code")
    private String zipCode;

    @NotNull(message = "Please provide Label")
    @NotBlank(message = "Please provide valid Label")
    private String label;
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

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
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

    @Override
    public String toString() {
        return "AddressCO{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", addressLine='" + addressLine + '\'' +
                ", zipCode=" + zipCode +
                ", label='" + label + '\'' +
                ", userCO=" + userCO +
                '}';
    }
}
