package com.bootcamp.ECommerceApplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
public class UserDto {
    @Email(message = "The Email ID is not valid or already exist")
    @NotNull(message = "Email Id is a mandatory field")
    private String email;

    @NotNull(message = "First Name is a mandatory field")
    private String firstName;
    private String middleName;

    @NotNull(message = "Last Name is a mandatory field")
    private String lastName;

    @NotNull(message = "Password is a mandatory field")
    @Length(min = 8,max = 15,message = "The Length of the password should be between 8 to 15 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$",
            message = "The Password should be 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;

    @OneToMany(mappedBy = "userDto",cascade = CascadeType.ALL)
    private List<AddressDto> addresses;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        addresses.forEach(e -> e.setUserDto(this));
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
