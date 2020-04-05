package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.dto.CustomerDto;
import com.bootcamp.ECommerceApplication.dto.SellerDto;
import com.bootcamp.ECommerceApplication.dto.UserDto;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.service.ConverterService;
import com.bootcamp.ECommerceApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ConverterService converterService;

    //---------------------------------------REGISTER CUSTOMER AND SELLER-----------------------------------------------

    //REGISTER A SELLER - SET THE ACCOUNT AS INACTIVE ACCOUNT, WAIT FOR ADMIN APPROVAL
    @PostMapping("/sellers-registration")
    public ResponseEntity<Object> createSeller(@Valid @RequestBody SellerDto sellerDto) throws MessagingException {
        Seller seller = converterService.convertToSeller(sellerDto);
        return userService.createSeller(seller);
    }


    //REGISTER A INACTIVE CUSTOMER AND SEND AN ACTIVATION LINK
    @PostMapping("/customers-registration")
    public Object createCustomerToken(@Valid @RequestBody CustomerDto customerDto) throws MessagingException {
        Customer customer = converterService.convertToCustomer(customerDto);
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
    public String reSendActivationLink(@RequestBody UserDto userDto) throws MessagingException {
//        User user = converterService.convertToUser(userDto);   //not working
        return userService.reSendActivationLink(userDto);
    }
}
