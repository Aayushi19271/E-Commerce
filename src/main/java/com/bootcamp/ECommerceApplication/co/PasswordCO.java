package com.bootcamp.ECommerceApplication.co;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PasswordCO {
    @NotNull(message = "Please provide Password")
    @Length(min = 8, max = 15, message = "The Length of the password should be between 8 to 15 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$",
            message = "The Password should be 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;

    @NotNull(message = "Please provide Confirm Password")
    @Length(min = 8, max = 15, message = "The Length of the password should be between 8 to 15 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{8,15}$",
            message = "The Password should be 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String confirmPassword;

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

    @Override
    public String toString() {
        return "PasswordCO{" +
                "password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
