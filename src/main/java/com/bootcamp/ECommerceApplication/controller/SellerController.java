package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.entity.ProductVariation;
import com.bootcamp.ECommerceApplication.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
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

//-------------------------------------------SELLER ACCOUNT API'S-------------------------------------------------------

    //Get the LoggedIn Seller's Profile Details
    @GetMapping("/profile")
    public ResponseEntity<Object> sellerProfile(Principal principal)
    {
        String email = principal.getName();
        return sellerService.sellerProfile(email);
    }

    //Update the Profile of LoggedIn Seller
    @PatchMapping("/update-profile")
    public ResponseEntity<Object> sellerUpdateProfile(Principal principal,@RequestBody Map<Object,Object> fields)
    {
        String email = principal.getName();
        return sellerService.sellerUpdateProfile(email,fields);
    }


    //Update the LoggedIn Seller's Password And Send Mail Upon Change
    @PatchMapping("/change-password")
    public ResponseEntity<Object> sellerUpdatePassword(Principal principal,@RequestBody Map<Object,Object> fields) throws MessagingException {
        String email = principal.getName();
        return sellerService.sellerUpdatePassword(email,fields);
    }

    //Update the already existing Address of LoggedIn Seller
    @PatchMapping("/update-address")
    public ResponseEntity<Object> sellerUpdateAddress(Principal principal,@RequestBody Map<Object,Object> fields){
        String email = principal.getName();
        return sellerService.sellerUpdateAddress(email,fields);
    }

//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------
    //Upload Profile Image
    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadProfileImage(@RequestParam(value = "upload", required = true) MultipartFile multipartFile, Principal principal) {
        String email = principal.getName();
        return sellerService.uploadProfileImage(multipartFile, email);
    }

    //Get the Profile Image
    @GetMapping(value = "/profile-image")
    public ResponseEntity<Object> getProfileImage(Principal principal) {
        String email = principal.getName();
        return sellerService.getProfileImage(email);
    }
//-------------------------------------------SELLER CATEGORY API'S-------------------------------------------------------

    //List All Category
    @GetMapping("/category")
    public ResponseEntity<Object> listAllCategory(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        return sellerService.listAllCategory(pageNo, pageSize, sortBy);
    }

//-------------------------------------------SELLER PRODUCT API'S-------------------------------------------------------

    //Add A Product
    @PostMapping("/add-product")
    public ResponseEntity<Object> addProduct(Principal principal,@Valid @RequestBody ProductCO productCO) throws MessagingException {
        String email = principal.getName();
        return sellerService.addProduct(productCO,email);
    }

    //Fetch Details Of One Product
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> listOneProduct(Principal principal,@PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProduct(email,id);
    }

    //Fetch Details Of One Product
    @GetMapping("/products")
    public ResponseEntity<Object> listAllProduct(Principal principal){
        String email = principal.getName();
        return sellerService.listAllProduct(email);
    }

    //List Details of One Product Variation
    @GetMapping("/product-variations/{id}")
    public ResponseEntity<Object> listOneProductVariation(Principal principal,@PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProductVariation(email,id);
    }

    //List Details All Product Variation
    @GetMapping("/product-variations")
    public ResponseEntity<Object> listAllProductVariation(Principal principal,@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy){

        String email = principal.getName();
        List<ProductVariation> list = sellerService.listAllProductVariation(email,pageNo,pageSize,sortBy);
        return new ResponseEntity<>(new MessageResponseEntity<>(list, HttpStatus.OK), HttpStatus.OK);
    }

    //Delete One Product
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Object> deleteOneProduct(Principal principal,@PathVariable Long id){
        String email = principal.getName();
        return sellerService.deleteOneProduct(email,id);
    }

    //Update One Product  --- NOT WORKING
    @PutMapping("/update-product/{id}")
    public ResponseEntity<Object> updateOneProduct(Principal principal, @PathVariable Long id, @Valid @RequestBody ProductUpdateCO productUpdateCO){
        String email = principal.getName();
        return sellerService.updateOneProduct(email,id,productUpdateCO);
    }
}














