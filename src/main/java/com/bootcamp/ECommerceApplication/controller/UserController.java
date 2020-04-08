package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.CustomerCO;
import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.co.SellerCO;
import com.bootcamp.ECommerceApplication.co.UserCO;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.service.ConverterService;
import com.bootcamp.ECommerceApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ConverterService converterService;

//---------------------------------------REGISTER CUSTOMER AND SELLER---------------------------------------------------

    //REGISTER A SELLER - SET THE ACCOUNT AS INACTIVE ACCOUNT, WAIT FOR ADMIN APPROVAL
    @PostMapping("/users/sellers-registration")
    public MessageResponseEntity<Object> createSeller(@Valid @RequestBody SellerCO sellerCO) throws MessagingException {
        Seller seller = converterService.convertToSeller(sellerCO);
        return userService.createSeller(seller);
    }


    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    @PostMapping("/users/customers-registration")
    public MessageResponseEntity<Object> createCustomerToken(@Valid @RequestBody CustomerCO customerCO) {
        Customer customer = converterService.convertToCustomer(customerCO);
        return userService.createCustomer(customer);
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @GetMapping("/users/customers/confirm-account")
    public MessageResponseEntity<Object> confirmUserAccount(@RequestParam("token") String confirmationToken) throws MessagingException {
        return userService.confirmUserAccountToken(confirmationToken);
    }


    //ACTIVATE THE CUSTOMER - SAME AS ABOVE USING PUT METHOD
    @PutMapping("/users/customers/confirm-account/{token}")
    public MessageResponseEntity<Object> confirmUserAccountToken(@PathVariable String token) throws MessagingException {
        return userService.confirmUserAccountToken(token);
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @PostMapping("/users/customers/re-send-activation-link")
    public MessageResponseEntity<Object> reSendActivationLink(@RequestBody UserCO userCO){
        return userService.reSendActivationLink(userCO);
    }


//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------
    //FORGOT PASSWORD REQUEST
    @PostMapping("/users/forgot-password")
    public MessageResponseEntity<Object> sendPasswordResetLink(@RequestBody UserCO userCO) {
        return userService.sendPasswordResetLink(userCO);
    }

    //RESET THE PASSWORD
    @PatchMapping("/users/reset-password")
    public MessageResponseEntity<Object> resetPassword(@RequestParam String token,@Valid @RequestBody PasswordCO passwordCO) {
        return userService.resetPassword(token,passwordCO);
    }

//--------------------------------------------------LOGOUT METHOD'S-----------------------------------------------------
    //WELCOME METHOD
    @GetMapping("/")
    public String test() {
        return "Welcome to the E-Commerce Application!!!";
    }

    //LOGOUT REQUEST
    @GetMapping("/doLogout")
    public ResponseEntity<Object> logout(HttpServletRequest request){
        return userService.logout(request);
    }
}
