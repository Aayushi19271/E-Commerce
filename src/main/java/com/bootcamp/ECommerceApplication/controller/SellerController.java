package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.ProductDTO;
import com.bootcamp.ECommerceApplication.dto.ProductVariationDTO;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.entity.Category;
import com.bootcamp.ECommerceApplication.service.SellerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@ApiModel(description = "Seller Controller Class")
@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @ApiOperation(value = "Seller Home Page")
    @GetMapping("/home")
    public String sellerHome(Principal principal) {
        return "Welcome " +principal.getName();
    }

//-------------------------------------------SELLER ACCOUNT API'S-------------------------------------------------------

    @ApiOperation(value = "API to view my profile")
    @GetMapping("/profile")
    public ResponseEntity<MessageResponseEntity<SellerDTO>> sellerProfile(Principal principal)
    {
        String email = principal.getName();
        return sellerService.sellerProfile(email);
    }

    @ApiOperation(value = "API to view my Address")
    @GetMapping("/address")
    public ResponseEntity<MessageResponseEntity<AddressDTO>> sellerAddress(Principal principal) {
        String email = principal.getName();
        return sellerService.sellerAddress(email);
    }

    @ApiOperation(value = "API to update my profile")
    @PatchMapping("/profile")
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdateProfile(Principal principal,
                                                                             @RequestBody Map<Object,Object> fields)
    {
        String email = principal.getName();
        return sellerService.sellerUpdateProfile(email,fields);
    }


    @ApiOperation(value = "API to update my password")
    @PatchMapping("/password/change")
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdatePassword(Principal principal,
                                                                              @RequestBody Map<Object,Object> fields)
                                                                              throws MessagingException {
        String email = principal.getName();
        return sellerService.sellerUpdatePassword(email,fields);
    }

    @ApiOperation(value = "API to update an address")
    @PatchMapping("/addresses")
    public ResponseEntity<MessageResponseEntity<AddressDTO>> sellerUpdateAddress(Principal principal,
                                                                                 @RequestBody Map<Object,Object> fields){
        String email = principal.getName();
        return sellerService.sellerUpdateAddress(email,fields);
    }

//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------
    @ApiOperation(value = "API to upload the profile image")
    @PostMapping(value = "/profile/image")
    public ResponseEntity<MessageResponseEntity<Object>> uploadProfileImage(@RequestParam(value = "upload", required = true)
                                                                                        MultipartFile multipartFile,
                                                                            Principal principal) {
        String email = principal.getName();
        return sellerService.uploadProfileImage(multipartFile, email);
    }

    @ApiOperation(value = "API to view the profile image")
    @GetMapping(value = "/profile/image")
    public ResponseEntity<MessageResponseEntity<Serializable>> getProfileImage(Principal principal) {
        String email = principal.getName();
        return sellerService.getProfileImage(email);
    }
//-------------------------------------------SELLER CATEGORY API'S-------------------------------------------------------

    @ApiOperation(value = "API to list all categories")
    @GetMapping("/categories")
    public ResponseEntity<MessageResponseEntity<Map<Category, Object>>> listAllCategory(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        return sellerService.listAllCategory(pageNo, pageSize, sortBy);
    }

//-------------------------------------------SELLER PRODUCT API'S-------------------------------------------------------

    @ApiOperation(value = "API add a product")
    @PostMapping("/products")
    public ResponseEntity<MessageResponseEntity<ProductDTO>> addProduct(Principal principal,
                                                                        @Valid @RequestBody ProductCO productCO)
                                                                    throws MessagingException {
        String email = principal.getName();
        return sellerService.addProduct(productCO,email);
    }

    @ApiOperation(value = "API to view a product")
    @GetMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(Principal principal,
                                                                                           @PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProduct(email,id);
    }

    @ApiOperation(value = "API to view all products")
    @GetMapping("/products")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProduct(Principal principal,
                                                                                           @RequestParam(defaultValue = "0") Integer pageNo,
                                                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                           @RequestParam(defaultValue = "id") String sortBy){
        String email = principal.getName();
        return sellerService.listAllProduct(email,pageNo,pageSize,sortBy);
    }

    @ApiOperation(value = "API to delete a product")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deleteOneProduct(Principal principal,
                                                                          @PathVariable Long id){
        String email = principal.getName();
        return sellerService.deleteOneProduct(email,id);
    }

    @ApiOperation(value = "API to update a product")
    @PutMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<ProductDTO>> updateOneProduct(Principal principal,
                                                                              @PathVariable Long id,
                                                                              @Valid @RequestBody ProductUpdateCO productUpdateCO){
        String email = principal.getName();
        return sellerService.updateOneProduct(email,id,productUpdateCO);
    }

    @ApiOperation(value = "API to view a product variation")
    @GetMapping("/products/variations/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProductVariation(Principal principal,
                                                                                                    @PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProductVariation(email,id);
    }

    @ApiOperation(value = "API to view all product variations")
    @GetMapping("/products/variations")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
                listAllProductVariation(Principal principal,@RequestParam(defaultValue = "0") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortBy){

        String email = principal.getName();
        return sellerService.listAllProductVariation(email,pageNo,pageSize,sortBy);
    }

    @ApiOperation(value = "API to add a product variation")
    @PostMapping("/products/variations")
    public ResponseEntity<MessageResponseEntity<ProductVariationDTO>> addProductVariation(
            @Valid @RequestBody ProductVariationCO productVariationCO) {
        try {
            return sellerService.addProductVariation(productVariationCO);
        }
        catch (JsonProcessingException jpe) {
            return new ResponseEntity(new MessageResponseEntity<>("Something went wrong.Try Again!", HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "API to update a product variation")
    @PatchMapping("/products/variations/{id}")
    public ResponseEntity<Object> updateProductVariation(Principal principal,
                                                        @RequestBody Map<Object,Object> fields,
                                                        @PathVariable(value = "id") Long productVariationId){
        String email = principal.getName();
        return sellerService.updateProductVariation(email,fields,productVariationId);
    }

    @ApiOperation(value = "API to upload the product variation image")
    @PostMapping(value = "/products/variations/image/{id}")
    public ResponseEntity<MessageResponseEntity<String>> uploadProductVariationImage(@RequestParam(value = "upload", required = true) MultipartFile multipartFile,
                                                                                     Principal principal,
                                                                                     @PathVariable(value = "id") Long id) {
        String email = principal.getName();
        return sellerService.uploadProductVariationImage(multipartFile, email,id);
    }

    @ApiOperation(value = "API to view the product variation image")
    @GetMapping(value = "/products/variations/image/{id}")
    public ResponseEntity<MessageResponseEntity<String>> getProductVariationImage(Principal principal, @PathVariable(value = "id") Long id) {
        String email = principal.getName();
        return sellerService.getProductVariationImage(email,id);
    }
}














