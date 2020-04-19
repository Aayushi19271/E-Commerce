package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.ProductDTO;
import com.bootcamp.ECommerceApplication.dto.ProductVariationDTO;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.entity.Category;
import com.bootcamp.ECommerceApplication.entity.ProductVariation;
import com.bootcamp.ECommerceApplication.exception.ProductNotFoundException;
import com.bootcamp.ECommerceApplication.service.SellerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
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
    public ResponseEntity<MessageResponseEntity<SellerDTO>> sellerProfile(Principal principal)
    {
        String email = principal.getName();
        return sellerService.sellerProfile(email);
    }

    //Update the Profile of LoggedIn Seller
    @PatchMapping("/profile")
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdateProfile(Principal principal,
                                                                             @RequestBody Map<Object,Object> fields)
    {
        String email = principal.getName();
        return sellerService.sellerUpdateProfile(email,fields);
    }


    //Update the LoggedIn Seller's Password And Send Mail Upon Change
    @PatchMapping("/password/change")
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdatePassword(Principal principal,
                                                                              @RequestBody Map<Object,Object> fields)
                                                                              throws MessagingException {
        String email = principal.getName();
        return sellerService.sellerUpdatePassword(email,fields);
    }

    //Update the already existing Address of LoggedIn Seller
    @PatchMapping("/addresses")
    public ResponseEntity<MessageResponseEntity<AddressDTO>> sellerUpdateAddress(Principal principal,
                                                                                 @RequestBody Map<Object,Object> fields){
        String email = principal.getName();
        return sellerService.sellerUpdateAddress(email,fields);
    }

//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------
    //Upload Profile Image
    @PostMapping(value = "/profile/image")
    public ResponseEntity<MessageResponseEntity<Object>> uploadProfileImage(@RequestParam(value = "upload", required = true)
                                                                                        MultipartFile multipartFile,
                                                                            Principal principal) {
        String email = principal.getName();
        return sellerService.uploadProfileImage(multipartFile, email);
    }

    //Get the Profile Image
    @GetMapping(value = "/profile/image")
    public ResponseEntity<MessageResponseEntity<Serializable>> getProfileImage(Principal principal) {
        String email = principal.getName();
        return sellerService.getProfileImage(email);
    }
//-------------------------------------------SELLER CATEGORY API'S-------------------------------------------------------

    //List All Category
    @GetMapping("/categories")
    public ResponseEntity<MessageResponseEntity<Map<Category, Object>>> listAllCategory(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        return sellerService.listAllCategory(pageNo, pageSize, sortBy);
    }

//-------------------------------------------SELLER PRODUCT API'S-------------------------------------------------------

    //Add A Product
    @PostMapping("/products")
    public ResponseEntity<MessageResponseEntity<ProductDTO>> addProduct(Principal principal,
                                                                        @Valid @RequestBody ProductCO productCO)
                                                                    throws MessagingException {
        String email = principal.getName();
        return sellerService.addProduct(productCO,email);
    }

    //Get Details Of One Product
    @GetMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(Principal principal,
                                                                                           @PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProduct(email,id);
    }

    //Get Details Of All Product
    @GetMapping("/products")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProduct(Principal principal,
                                                                                           @RequestParam(defaultValue = "0") Integer pageNo,
                                                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                           @RequestParam(defaultValue = "id") String sortBy){
        String email = principal.getName();
        return sellerService.listAllProduct(email,pageNo,pageSize,sortBy);
    }

    //Delete One Product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deleteOneProduct(Principal principal,
                                                                          @PathVariable Long id){
        String email = principal.getName();
        return sellerService.deleteOneProduct(email,id);
    }

    //Update One Product
    @PutMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<ProductDTO>> updateOneProduct(Principal principal,
                                                                              @PathVariable Long id,
                                                                              @Valid @RequestBody ProductUpdateCO productUpdateCO){
        String email = principal.getName();
        return sellerService.updateOneProduct(email,id,productUpdateCO);
    }

    //List Details of One Product Variation
    @GetMapping("/products/variations/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProductVariation(Principal principal,
                                                                                                    @PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProductVariation(email,id);
    }

    //List Details All Product Variation
    @GetMapping("/products/variations")
    public ResponseEntity<MessageResponseEntity<List<ProductVariation>>>
                listAllProductVariation(Principal principal,@RequestParam(defaultValue = "0") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortBy){

        String email = principal.getName();
        return sellerService.listAllProductVariation(email,pageNo,pageSize,sortBy);
    }

    //Add a product variation
    @PostMapping("/products/variations")
    public ResponseEntity<MessageResponseEntity<ProductVariationDTO>> addProductVariation(
            @Valid @RequestBody ProductVariationCO productVariationCO) {
        try {
            return sellerService.addProductVariation(productVariationCO);
        } catch (ProductNotFoundException pe) {
            throw pe;
        } catch (JsonProcessingException jpe) {
            return new ResponseEntity(new MessageResponseEntity<>("Something went wrong.Try Again!", HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
        }
    }


    //add variation of a product Image
    @PostMapping(path = "/products/variations/images/{id}")
    public ResponseEntity<MessageResponseEntity<String>> addProductVariationImages(@PathVariable(value = "id") Long id,
                                                    List<MultipartFile> imageFiles) {
        try {
            return sellerService.addProductVariationImages(id, imageFiles);
        } catch (ProductNotFoundException pe) {
            throw pe;
        } catch (IOException ioe) {
            return new ResponseEntity(new MessageResponseEntity<>("Something went wrong.Try Again!", HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
        }
    }
}














