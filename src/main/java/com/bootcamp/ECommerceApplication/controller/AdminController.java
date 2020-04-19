package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.CategoryCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldValuesCO;
import com.bootcamp.ECommerceApplication.co.CategoryUpdateCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.CategoryDTO;
import com.bootcamp.ECommerceApplication.dto.CategoryMetadataFieldDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    private SmtpMailSender smtpMailSender;


//-----------------------------------------------ADMIN HOME-------------------------------------------------------------
    @GetMapping("/admin/home")
    public String adminHome() {
        return "Welcome Admin!";
    }

//-----------------------------------------------GET USERS--------------------------------------------------------------
    //GET THE LIST OF USERS
    @GetMapping("/admin/users")
    public List<User> listOfUsers() {
        return adminService.findAllUsers();
    }

    //GET A SINGLE USER
    @GetMapping("/admin/users/{id}")
    public User retrieveCustomer(@PathVariable Long id)
    {
        User user = adminService.getUser(id);
        if (user==null)
            throw new UserNotFoundException("id-"+id);
        return user;
    }


//-----------------------------------------FETCH THE LIST OF CUSTOMERS AND SELLERS--------------------------------------
    //LIST ALL REGISTERED CUSTOMERS
    @GetMapping("/admin/customers")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
    findAllRegisteredCustomers(@RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return adminService.findAllCustomers(pageNo,pageSize,sortBy);
    }

    //LIST ALL REGISTERED SELLERS
    @GetMapping("/admin/sellers")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
    findAllRegisteredSellers(@RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id")String sortBy){
        return adminService.findAllSellers(pageNo,pageSize,sortBy);
    }


//-------------------------------------------ACTIVATE AND DE-ACTIVATE THE CUSTOMER-------------------------------------
    //ACTIVATE A CUSTOMER
    @PatchMapping("/admin/customers/activate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> activateCustomer(@PathVariable Long id,
                                                                          @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        return adminService.activateAccount(id,fields);
    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping("/admin/customers/deactivate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deactivateCustomer(@PathVariable Long id,
                                                                            @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        return adminService.deactivateAccount(id,fields);
    }


//----------------------------------------ACTIVATE AND DE-ACTIVATE THE SELLER-------------------------------------------

    //ACTIVATE A SELLER
    @PatchMapping("/admin/sellers/activate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> activateSeller(@PathVariable Long id,
                                                                        @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        return adminService.activateAccount(id,fields);
    }

    //DE-ACTIVE A SELLER
    @PatchMapping("/admin/sellers/deactivate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deactivateSeller(@PathVariable Long id,
                                                                          @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id,fields);
    }

//-------------------------------------------ADMIN CATEGORY API'S-------------------------------------------------------

    //Admin Function to add a Metadata field
    @PostMapping("/admin/metadata-field")
    public ResponseEntity<MessageResponseEntity<CategoryMetadataFieldDTO>>
    addMetadataField(@Valid @RequestBody CategoryMetadataFieldCO categoryMetadataFieldCO){
        return adminService.addMetadataField(categoryMetadataFieldCO);
    }

    //Admin Function to view all Metadata fields
    @GetMapping("/admin/metadata-fields")
    public ResponseEntity<MessageResponseEntity<List<CategoryMetadataField>>>
    listAllMetadata(@RequestParam(defaultValue = "0") Integer pageNo,
                    @RequestParam(defaultValue = "10") Integer pageSize,
                    @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllMetadata(pageNo,pageSize,sortBy);
    }

    //Admin Function to Add New Category
    @PostMapping("/admin/categories")
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> addCategory(@Valid @RequestBody CategoryCO categoryCO){
        return adminService.addCategory(categoryCO);
    }

    //Admin Function to List One Category
    @GetMapping("/admin/categories/{id}")
    public ResponseEntity<MessageResponseEntity<Map<String, Object>>> listOneCategory(@PathVariable Long id){
        return adminService.listOneCategory(id);
    }


    //Admin Function to list All Category
    @GetMapping("/admin/categories")
    public ResponseEntity<MessageResponseEntity<List<Category>>> listAllCategory(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                 @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllCategory(pageNo,pageSize,sortBy);
    }

    //Admin Function to Update One Category
    @PutMapping("/admin/categories")
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> updateCategory(@Valid @RequestBody CategoryUpdateCO categoryUpdateCO){
        return adminService.updateCategory(categoryUpdateCO);
    }

    //Admin Function to Add Metadata Field Values
    @PostMapping("/admin/metadata-field-values")
    public ResponseEntity<MessageResponseEntity<String>> addMetadataFieldValues(@Valid @RequestBody CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO){
        return adminService.addMetadataFieldValues(categoryMetadataFieldValuesCO);
    }

    //Admin Function to Update Metadata Field Values
    @PutMapping("/admin/metadata-field-values")
    public ResponseEntity<MessageResponseEntity<CategoryMetadataFieldValuesCO>>
    updateMetadataFieldValues(@Valid @RequestBody CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO){
        return adminService.updateMetadataFieldValues(categoryMetadataFieldValuesCO);
    }

//-------------------------------------------ADMIN PRODUCT API'S-------------------------------------------------------
    //Admin Function to view a product
    @GetMapping("/admin/products/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(@PathVariable Long id){
        return adminService.listOneProduct(id);
    }

    //Admin Function to list All Category
    @GetMapping("/admin/products")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProducts(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                            @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllProducts(pageNo,pageSize,sortBy);
    }

    //Admin Function to Activate A Product
    @PutMapping("/admin/products/activate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> activateProduct(@PathVariable Long id) {
        return adminService.activateProduct(id);
    }

    //Admin Function to De-activate A Product
    @PutMapping("/admin/products/deactivate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deactivateProduct(@PathVariable Long id){
        return adminService.deactivateProduct(id);
    }

    //Get the Product variation Image
    @GetMapping(value = "/admin/products/variations/image/{id}")
    public ResponseEntity<Object> getProductVariationImage(@PathVariable(value = "id") Long id) {
        return adminService.getProductVariationImage(id);
    }

}






















