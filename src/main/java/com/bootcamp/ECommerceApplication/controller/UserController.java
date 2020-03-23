package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.Seller;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public List<User> listOfUsers()
    {
        List<User> users = userService.findAllUsers();
        return users;
    }

    @GetMapping("/create-customer-manually")
    public Customer createCustomerManually()
    {
        Customer customer =userService.createCustomerManually();
        return customer;
    }


    @PostMapping("/customer")
    public String createCustomer(@RequestBody Customer customer)
    {
        userService.createCustomer(customer);
        return "The Customer Account is Successfully Registered with the inactive Account, Please verify Account using activation link";
    }

    @PostMapping("/seller")
    public String createSeller(@RequestBody Seller seller)
    {
        userService.createSeller(seller);
        return "The Seller Account is Successfully created with the inactive account ";
    }

}
