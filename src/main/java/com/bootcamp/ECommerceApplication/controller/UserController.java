package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.dto.CustomerDto;
import com.bootcamp.ECommerceApplication.dto.PasswordDto;
import com.bootcamp.ECommerceApplication.dto.SellerDto;
import com.bootcamp.ECommerceApplication.dto.UserDto;
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
    public ResponseEntity<Object> createSeller(@Valid @RequestBody SellerDto sellerDto) throws MessagingException {
        Seller seller = converterService.convertToSeller(sellerDto);
        return userService.createSeller(seller);
    }


    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    @PostMapping("/users/customers-registration")
    public Object createCustomerToken(@Valid @RequestBody CustomerDto customerDto) throws MessagingException {
        Customer customer = converterService.convertToCustomer(customerDto);
        return userService.createCustomer(customer);
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @GetMapping("/users/customers/confirm-account")
    public ResponseEntity<Object> confirmUserAccount(@RequestParam("token") String confirmationToken) throws MessagingException {
        return userService.confirmUserAccountToken(confirmationToken);
    }


    //ACTIVATE THE CUSTOMER - SAME AS ABOVE USING PUT METHOD
    @PutMapping("/users/customers/confirm-account/{token}")
    public String confirmUserAccountToken(@PathVariable String token) throws MessagingException {
        userService.confirmUserAccountToken(token);
        return "Account Successfully Active";
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @PostMapping("/users/customers/re-send-activation-link")
    public ResponseEntity<Object> reSendActivationLink(@RequestBody UserDto userDto) throws MessagingException {
//        User user = converterService.convertToUser(userDto);   //not working
        return userService.reSendActivationLink(userDto);
    }


//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------
    //FORGOT PASSWORD REQUEST
    @PostMapping("/users/forgot-password")
    public ResponseEntity<Object> sendPasswordResetLink(@RequestBody UserDto userDto) {
        return userService.sendPasswordResetLink(userDto);
    }

    //RESET THE PASSWORD
    @PatchMapping("/users/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam String token,@Valid @RequestBody PasswordDto passwordDto) {
        return userService.resetPassword(token,passwordDto);
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
