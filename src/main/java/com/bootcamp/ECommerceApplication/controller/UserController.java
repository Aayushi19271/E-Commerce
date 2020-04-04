package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    //******************************* GET USERS *****************************************
    //GET THE LIST OF USERS
    @GetMapping
    public List<User> listOfUsers() {
        return userService.findAllUsers();
    }

    //GET A SINGLE USER
    @GetMapping("/{id}")
    public Optional<User> retrieveCustomer(@PathVariable Long id)
    {
        Optional<User> user = userService.findOne(id);
        if (!user.isPresent())
            throw new UserNotFoundException("id-"+id);
        return user;
    }

    //******************************* TEST METHOD ***************************************
    //CREATING THE CUSTOMER MANUALLY -- USING GETTER AND SETTERS
    @GetMapping("/create-customer-manually")
    public Customer createCustomerManually() {
        return userService.createCustomerManually();
    }



    //******************************* REGISTER CUSTOMER AND SELLER  *********************
    //REGISTER A SELLER - SET THE ACCOUNT AS INACTIVE ACCOUNT, WAIT FOR ADMIN APPROVAL
    @PostMapping("/sellers-registration")
    public ResponseEntity<Object> createSeller(@Valid @RequestBody Seller seller) throws MessagingException {
        return userService.createSeller(seller);
    }


    //REGISTER A INACTIVE CUSTOMER AND SEND AN ACTIVATION LINK
    @PostMapping("/customers-registration")
    public Object createCustomerToken(@Valid @RequestBody Customer customer) throws MessagingException {
        return userService.createCustomer(customer);
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @GetMapping("/customers/confirm-account")
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) throws MessagingException {
        return userService.confirmUserAccountToken(confirmationToken);
    }


    //ACTIVATE THE CUSTOMER - SAME AS ABOVE USING PUT METHOD
    @PutMapping("/customers/confirm-account/{token}")
    public String confirmUserAccountToken(@PathVariable String token) throws MessagingException {
        userService.confirmUserAccountToken(token);
        return "Account Successfully Active";
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @PostMapping("/customers/re-send-activation-link")
    public String reSendActivationLink(@RequestBody User user) throws MessagingException {
        return userService.reSendActivationLink(user);
    }
}
