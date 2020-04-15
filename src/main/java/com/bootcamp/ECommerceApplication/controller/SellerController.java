package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.entity.ProductVariation;
import com.bootcamp.ECommerceApplication.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<Object> sellerUpdateProfile(Principal principal, @Valid @RequestBody SellerProfileUpdateCO sellerProfileUpdateCO)
    {
        String email = principal.getName();
        return sellerService.sellerUpdateProfile(email,sellerProfileUpdateCO);
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
//-------------------------------------------SELLER CATEGORY API'S-------------------------------------------------------

    //List All Category
    @GetMapping("/all-categories")
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
    @GetMapping("/list-a-product/{id}")
    public ResponseEntity<Object> listOneProduct(Principal principal,@PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProduct(email,id);
    }

    //Fetch Details Of One Product
    @GetMapping("/list-all-product")
    public ResponseEntity<Object> listAllProduct(Principal principal){
        String email = principal.getName();
        return sellerService.listAllProduct(email);
    }

    //List Details of One Product Variation
    @GetMapping("/list-a-product-variation/{id}")
    public ResponseEntity<Object> listOneProductVariation(Principal principal,@PathVariable Long id){
        String email = principal.getName();
        return sellerService.listOneProductVariation(email,id);
    }

    //List Details All Product Variation
    @GetMapping("/list-all-product-variation")
    public ResponseEntity<Object> listAllProductVariation(Principal principal,@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy){

        String email = principal.getName();
        List<ProductVariation> list = sellerService.listAllProductVariation(email,pageNo,pageSize,sortBy);
        return new ResponseEntity<>(new MessageResponseEntity<>(list, HttpStatus.OK), HttpStatus.OK);
    }

    //Delete One Product
    @DeleteMapping("/delete-a-product/{id}")
    public ResponseEntity<Object> deleteOneProduct(Principal principal,@PathVariable Long id){
        String email = principal.getName();
        return sellerService.deleteOneProduct(email,id);
    }

    //Update One Product  --- NOT WORKING
    @PutMapping("/update-a-product/{id}")
    public ResponseEntity<Object> updateOneProduct(Principal principal, @PathVariable Long id, @Valid @RequestBody ProductUpdateCO productUpdateCO){
        String email = principal.getName();
        return sellerService.updateOneProduct(email,id,productUpdateCO);
    }
}














