package com.bootcamp.ECommerceApplication.dto;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.List;

public class UserDTO {
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;

    @OneToMany(mappedBy = "userDTO",cascade = CascadeType.ALL)
    private List<AddressDTO> addresses;

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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }
}
