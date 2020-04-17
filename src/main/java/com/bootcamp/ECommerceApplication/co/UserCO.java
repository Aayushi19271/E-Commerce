package com.bootcamp.ECommerceApplication.co;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserCO {
    @Email(message = "Please provide valid Email ID")
    @NotNull(message = "Please provide Email ID")
    @NotBlank(message = "Please provide valid Email ID")
    private String email;

    @NotNull(message = "Please provide first name")
    @NotBlank(message = "Please provide valid first name")
    private String firstName;
    private String middleName;

    @NotNull(message = "Please provide last name")
    @NotBlank(message = "Please provide valid last name")
    private String lastName;

    @NotNull(message = "Please provide password")
    @Length(min = 8,max = 15,message = "The Length of the password should be between 8 to 15 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$",
            message = "The Password should be 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;

    @NotNull(message = "Please provide confirm password")
    private String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
