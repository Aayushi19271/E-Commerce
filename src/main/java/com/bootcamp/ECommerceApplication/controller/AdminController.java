package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.CategoryCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldValuesCO;
import com.bootcamp.ECommerceApplication.co.CategoryUpdateCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

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
    @GetMapping
    public List<User> listOfUsers() {
        return adminService.findAllUsers();
    }

    //GET A SINGLE USER
    @GetMapping("/admin/{id}")
    public User retrieveCustomer(@PathVariable Long id)
    {
        User user = adminService.getUser(id);
        if (user==null)
            throw new UserNotFoundException("id-"+id);
        return user;
    }

//---------------------------------------------------TEST METHOD--------------------------------------------------------
    //CREATING THE CUSTOMER MANUALLY -- USING GETTER AND SETTERS
    @GetMapping("/admin/create-customer-manually")
    public Customer createCustomerManually() {
        return adminService.createCustomerManually();
    }


//-----------------------------------------FETCH THE LIST OF CUSTOMERS AND SELLERS--------------------------------------
    //LIST ALL REGISTERED CUSTOMERS
    @GetMapping("/admin/customers")
    public ResponseEntity<Object> findAllRegisteredCustomers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                            @RequestParam(defaultValue = "id") String sortBy) {
        return adminService.findAllCustomers(pageNo,pageSize,sortBy);
    }

    //LIST ALL REGISTERED SELLERS
    @GetMapping("/admin/sellers")
    public ResponseEntity<Object> findAllRegisteredSellers(@RequestParam(defaultValue = "0") Integer pageNo,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @RequestParam(defaultValue = "id")String sortBy){
        return adminService.findAllSellers(pageNo,pageSize,sortBy);
    }


//-------------------------------------------ACTIVATE AND DE-ACTIVATE THE CUSTOMER-------------------------------------
    //ACTIVATE A CUSTOMER
    @PatchMapping("/admin/customers/activate/{id}")
    public ResponseEntity<Object> activateCustomer(@PathVariable Long id)
            throws MessagingException {
        adminService.findCustomer(id);
        return adminService.activateAccount(id);

    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping("/admin/customers/deactivate/{id}")
    public ResponseEntity<Object> deactivateCustomer(@PathVariable Long id)
            throws MessagingException {
        adminService.findCustomer(id);
        return adminService.deactivateAccount(id);
    }


//----------------------------------------ACTIVATE AND DE-ACTIVATE THE SELLER-------------------------------------------

    //ACTIVATE A SELLER
    @PatchMapping("/admin/sellers/activate/{id}")
    public ResponseEntity<Object> activateSeller(@PathVariable Long id)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.activateAccount(id);
    }

    //DE-ACTIVE A SELLER
    @PatchMapping("/admin/sellers/deactivate/{id}")
    public ResponseEntity<Object> deactivateSeller(@PathVariable Long id)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id);
    }

//-------------------------------------------ADMIN CATEGORY API'S-------------------------------------------------------

    //Admin Function to Add Category Metadata Field
    @PostMapping("/admin/add-metadata-field")
    public ResponseEntity<Object> addMetadataField(@Valid @RequestBody CategoryMetadataFieldCO categoryMetadataFieldCO){
        return adminService.addMetadataField(categoryMetadataFieldCO);
    }

    //Admin Function to List All Category Metadata Field
    @GetMapping("/admin/metadata")
    public ResponseEntity<Object> listAllMetadata(@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllMetadata(pageNo,pageSize,sortBy);
    }

    //Admin Function to Add New Category
    @PostMapping("/admin/add-category")
    public ResponseEntity<Object> addCategory(@Valid @RequestBody CategoryCO categoryCO){
        return adminService.addCategory(categoryCO);
    }

    //Admin Function to List One Category
    @GetMapping("/admin/category/{id}")
    public ResponseEntity<Object> listOneCategory(@PathVariable Long id){
        return adminService.listOneCategory(id);
    }


    //Admin Function to list All Category
    @GetMapping("/admin/category")
    public ResponseEntity<Object> listAllCategory(@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllCategory(pageNo,pageSize,sortBy);
    }

    //Admin Function to Update One Category
    @PutMapping("/admin/update-category")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CategoryUpdateCO categoryUpdateCO){
        return adminService.updateCategory(categoryUpdateCO);
    }

    //Admin Function to Add Metadata Field Values
    @PostMapping("/admin/add-metadata-field-value")
    public ResponseEntity<Object> addMetadataFieldValues(@Valid @RequestBody CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO){
        return adminService.addMetadataFieldValues(categoryMetadataFieldValuesCO);
    }

    //Admin Function to Update Metadata Field Values
    @PutMapping("/admin/update-metadata-field-value")
    public ResponseEntity<Object> updateMetadataFieldValues(@Valid @RequestBody CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO){
        return adminService.updateMetadataFieldValues(categoryMetadataFieldValuesCO);
    }

//-------------------------------------------ADMIN PRODUCT API'S-------------------------------------------------------
    //Admin Function to view a product
    @GetMapping("/admin/view-product/{id}")
    public ResponseEntity<Object> listOneProduct(@PathVariable Long id){
        return adminService.listOneProduct(id);
    }

    //Admin Function to list All Category
    @GetMapping("/admin/view-product")
    public ResponseEntity<Object> listAllProducts(@RequestParam(defaultValue = "0") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllProducts(pageNo,pageSize,sortBy);
    }

    //Admin Function to Activate A Product
    @PutMapping("/admin/product/activate/{id}")
    public ResponseEntity<Object> activateProduct(@PathVariable Long id) {
        return adminService.activateProduct(id);
    }

    //Admin Function to De-activate A Product
    @PutMapping("/admin/product/deactivate/{id}")
    public ResponseEntity<Object> deactivateProduct(@PathVariable Long id){
        return adminService.deactivateProduct(id);
    }

}






















