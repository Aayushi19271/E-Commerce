package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

//-------------------------------------------CUSTOMER ACCOUNT API'S-----------------------------------------------------

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
    public ResponseEntity<Object> customerUpdatePassword(Principal principal,@RequestBody Map<Object,Object> fields) throws MessagingException {
        String email = principal.getName();
        return customerService.customerUpdatePassword(email,fields);
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
    @PatchMapping("/update-address/{id}")
    public ResponseEntity<Object> customerUpdateAddress(Principal principal,
                                                        @RequestBody Map<Object,Object> fields,
                                                        @PathVariable Long id){
        String email = principal.getName();
        return customerService.customerUpdateAddress(email,fields,id);
    }

    //Update the Profile of LoggedIn Customer
    @PatchMapping("/update-profile")
    public ResponseEntity<Object> customerUpdateProfile(Principal principal,@RequestBody Map<Object,Object> fields){
        String email = principal.getName();
        return customerService.customerUpdateProfile(email,fields);
    }
//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------
    //Upload Profile Image
    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadProfileImage(@RequestParam(value = "upload", required = true) MultipartFile multipartFile, Principal principal) {
        String email = principal.getName();
        return customerService.uploadProfileImage(multipartFile, email);
    }

    //Get the Profile Image
    @GetMapping(value = "/profile-image")
    public ResponseEntity<Object> getProfileImage(Principal principal) {
            String email = principal.getName();
            return customerService.getProfileImage(email);
    }

//-------------------------------------------CUSTOMER CATEGORY API'S-----------------------------------------------------

    //list all Categories
    @GetMapping({"/category/{id}","/category"})
    public ResponseEntity<Object> listAllCustomerCategories(@PathVariable(name = "id", required = false) Long id){
        return customerService.listAllCustomerCategories(id);
    }

    //API to fetch filtering details for a category
    @GetMapping({"/category-filtering-details/{id}","/category-filtering-details"})
    public ResponseEntity<Object> getFilterDetails(@PathVariable(name = "id", required = false) Long id){
        return customerService.getFilterDetails(id);
    }


//-------------------------------------------CUSTOMER PRODUCT API'S-----------------------------------------------------

    //Customer Function to view a product  -Product Id
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> listOneProduct(@PathVariable Long id){
        return customerService.listOneProduct(id);
    }

    //Customer Function to view all products  -Category Id
    @GetMapping("/products/categories/{id}")
    public ResponseEntity<Object> listAllProduct(@RequestParam(defaultValue = "0") Integer pageNo,
                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                 @PathVariable Long id){

        return customerService.listAllProducts(pageNo,pageSize,sortBy,id);
    }



}
