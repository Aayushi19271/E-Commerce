package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.co.CustomerUpdateProfileCO;
import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //LoggedIn Customer's Welcome Page
    @GetMapping("/home")
    public String customerHome( Principal principal) {
        return "Welcome " +principal.getName();
    }

    //Get the LoggedIn Customer's Profile Details
    @GetMapping("/profile")
    public ResponseEntity<Object> customerProfile(Principal principal) {
        String email = principal.getName();
        return customerService.customerProfile(email);
    }

    //Get the list of LoggedIn Customer's Addresses
    @GetMapping("/addresses")
    public ResponseEntity<Object> customerAddresses(Principal principal) {
        String email = principal.getName();
        return customerService.customerAddresses(email);
    }

    //Change the LoggedIn Customer's Password And Send Mail Upon Change
    @PatchMapping("/change-password")
    public ResponseEntity<Object> customerUpdatePassword(Principal principal, @Valid @RequestBody PasswordCO passwordCO) throws MessagingException {
        String email = principal.getName();
        return customerService.customerUpdatePassword(email,passwordCO);
    }

    //Add the New Address to the LoggedIn Customer
    @PostMapping("/add-address")
    public ResponseEntity<Object> customerAddAddress(Principal principal, @Valid @RequestBody AddressCO addressCO){
        String email = principal.getName();
        return customerService.customerAddAddress(email,addressCO);
    }

    //Delete the already existing Address of the LoggedIn Customer
    @DeleteMapping("/delete-address/{id}")
    public ResponseEntity<Object> customerDeleteAddress(Principal principal, @PathVariable("id") Long id) {
        String email = principal.getName();
        return customerService.customerDeleteAddress(email,id);
    }

    //Update the already existing Address of LoggedIn Customer
    @PatchMapping("/update-address")
    public ResponseEntity<Object> customerUpdateAddress(Principal principal, @Valid @RequestBody AddressCO addressCO){
        String email = principal.getName();
        return customerService.customerUpdateAddress(email,addressCO);
    }

    //Update the Profile of LoggedIn Customer
    @PatchMapping("/update-profile")
    public ResponseEntity<Object> customerUpdateProfile(Principal principal, @Valid @RequestBody CustomerUpdateProfileCO customerUpdateProfileCO){
        String email = principal.getName();
        return customerService.customerUpdateProfile(email,customerUpdateProfileCO);
    }

    //list all Categories
    @GetMapping({"/list-all-categories-customer/{id}","/list-all-categories-customer"})
    public List<Map<Object, Object>> listAllCustomerCategories(@PathVariable(name = "id", required = false) Long id){
        return customerService.listAllCustomerCategories(id);
    }
}
