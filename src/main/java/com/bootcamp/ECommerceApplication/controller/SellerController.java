package com.bootcamp.ECommerceApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @GetMapping("/home")
    public String sellerHome() {
        return "Welcome Seller!";
    }
}
