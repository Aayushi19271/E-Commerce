package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.CustomerDTO;
import com.bootcamp.ECommerceApplication.service.CustomerService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@ApiModel(description = "Customer Controller Class")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

//-------------------------------------------CUSTOMER ACCOUNT API'S-----------------------------------------------------

    @ApiOperation(value = "Customer Home Page")
    @GetMapping("/home")
    public String customerHome( Principal principal) {
        return "Welcome " +principal.getName();
    }

    @ApiOperation(value = "API to view my profile")
    @GetMapping("/profile")
    public ResponseEntity<MessageResponseEntity<CustomerDTO>> customerProfile(Principal principal) {
        String email = principal.getName();
        return customerService.customerProfile(email);
    }

    @ApiOperation(value = "API to view my addresses")
    @GetMapping("/addresses")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> customerAddresses(Principal principal) {
        String email = principal.getName();
        return customerService.customerAddresses(email);
    }

    @ApiOperation(value = "API to update my password")
    @PatchMapping("/password/change")
    public ResponseEntity<Object> customerUpdatePassword(Principal principal,
                                                         @RequestBody Map<Object,Object> fields) throws MessagingException {
        String email = principal.getName();
        return customerService.customerUpdatePassword(email,fields);
    }

    @ApiOperation(value = "API to add a new address")
    @PostMapping("/addresses")
    public ResponseEntity<MessageResponseEntity<AddressDTO>> customerAddAddress(Principal principal,
                                                                                @Valid @RequestBody AddressCO addressCO){
        String email = principal.getName();
        return customerService.customerAddAddress(email,addressCO);
    }

    @ApiOperation(value = "API to delete an address")
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<MessageResponseEntity<String>> customerDeleteAddress(Principal principal,
                                                                               @PathVariable("id") Long id) {
        String email = principal.getName();
        return customerService.customerDeleteAddress(email,id);
    }

    @ApiOperation(value = "API to update an address")
    @PatchMapping("/addresses/{id}")
    public ResponseEntity<MessageResponseEntity<AddressDTO>> customerUpdateAddress(Principal principal,
                                                                                   @RequestBody Map<Object,Object> fields,
                                                                                   @PathVariable Long id){
        String email = principal.getName();
        return customerService.customerUpdateAddress(email,fields,id);
    }

    @ApiOperation(value = "API to update my profile")
    @PatchMapping("/profile")
    public ResponseEntity<Object> customerUpdateProfile(Principal principal,@RequestBody Map<Object,Object> fields){
        String email = principal.getName();
        return customerService.customerUpdateProfile(email,fields);
    }
//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------
    @ApiOperation(value = "API to upload the profile image")
    @PostMapping(value = "/profile/image")
    public ResponseEntity<MessageResponseEntity<String>> uploadProfileImage(@RequestParam(value = "upload", required = true) MultipartFile multipartFile,
                                                                            Principal principal) {
        String email = principal.getName();
        return customerService.uploadProfileImage(multipartFile, email);
    }

    @ApiOperation(value = "API to view the profile image")
    @GetMapping(value = "/profile/image")
    public ResponseEntity<MessageResponseEntity<String>> getProfileImage(Principal principal) {
            String email = principal.getName();
            return customerService.getProfileImage(email);
    }

//-------------------------------------------CUSTOMER CATEGORY API'S-----------------------------------------------------

    @ApiOperation(value = "API to list all categories")
    @GetMapping({"/categories/{id}","/categories"})
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllCustomerCategories(@PathVariable(name = "id", required = false) Long id){
        return customerService.listAllCustomerCategories(id);
    }

    @ApiOperation(value = "API to fetch filtering details for a category")
    @GetMapping("/categories/filtering/{id}")
    public ResponseEntity<MessageResponseEntity<List<Object>>> getFilterDetails(@PathVariable(name = "id", required = false) Long id){
        return customerService.getFilterDetails(id);
    }


//-------------------------------------------CUSTOMER PRODUCT API'S-----------------------------------------------------

    @ApiOperation(value = "API to view a product")
    @GetMapping("/products/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(@PathVariable Long id){
        return customerService.listOneProduct(id);
    }


    @ApiOperation(value = "API to view all products")
    @GetMapping("/products/categories/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProduct(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                                                           @PathVariable Long id){

        return customerService.listAllProducts(pageNo,pageSize,sortBy,id);
    }

    @ApiOperation(value = "API to view the product variation image")
    @GetMapping(value = "/products/variations/image/{id}")
    public ResponseEntity<MessageResponseEntity<String>> getProductVariationImage(@PathVariable(value = "id") Long id) {
        return customerService.getProductVariationImage(id);
    }
}
