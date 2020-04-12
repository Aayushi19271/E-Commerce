package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    SellerService sellerService;

    //LoggedIn Seller's Welcome Page
    @GetMapping("/home")
    public String sellerHome(Principal principal) {
        return "Welcome " +principal.getName();
    }

    //Get the LoggedIn Seller's Profile Details
    @GetMapping("/profile")
    public ResponseEntity<Object> sellerProfile(Principal principal)
    {
        String email = principal.getName();
        return sellerService.sellerProfile(email);
    }

    //Update the Profile of LoggedIn Seller
    @PatchMapping("/update-profile")
    public ResponseEntity<Object> sellerUpdateProfile(Principal principal, @Valid @RequestBody Map<Object,Object> fields)
    {
        String email = principal.getName();
        return sellerService.sellerUpdateProfile(email,fields);
    }

    //Update the LoggedIn Seller's Password And Send Mail Upon Change
    @PatchMapping("/change-password")
    public ResponseEntity<Object> sellerUpdatePassword(Principal principal, @Valid @RequestBody PasswordCO passwordCO) throws MessagingException {
        String email = principal.getName();
        return sellerService.sellerUpdatePassword(email,passwordCO);
    }

    //Update the already existing Address of LoggedIn Seller
    @PatchMapping("/update-address")
    public ResponseEntity<Object> sellerUpdateAddress(Principal principal, @Valid @RequestBody AddressCO addressCO){
        String email = principal.getName();
        return sellerService.sellerUpdateAddress(email,addressCO);
    }
}














