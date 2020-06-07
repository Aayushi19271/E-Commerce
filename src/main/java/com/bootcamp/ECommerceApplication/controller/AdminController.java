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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@ApiModel(description = "Admin Controller Class")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SmtpMailSender smtpMailSender;


//-----------------------------------------------ADMIN HOME-------------------------------------------------------------
    @ApiOperation(value = "Admin Home Page")
    @GetMapping("/admin/home")
    public String adminHome() {
        return "Welcome Admin!";
    }

//-----------------------------------------------GET USERS--------------------------------------------------------------
    @ApiOperation(value = "Get the list of all Users")
    @GetMapping("/admin/users")
    public List<User> listOfUsers() {
        return adminService.findAllUsers();
    }


    @ApiOperation(value = "Get the Single User")
    @GetMapping("/admin/users/{id}")
    public User retrieveCustomer(@PathVariable Long id)
    {
        User user = adminService.getUser(id);
        if (user==null)
            throw new UserNotFoundException("id-"+id);
        return user;
    }


//-----------------------------------------FETCH THE LIST OF CUSTOMERS AND SELLERS--------------------------------------
    @ApiOperation(value = "API to register customer")
    @GetMapping("/admin/customers")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
    findAllRegisteredCustomers(@RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        return adminService.findAllCustomers(pageNo,pageSize,sortBy);
    }

    @ApiOperation(value = "API to register seller")
    @GetMapping("/admin/sellers")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
    findAllRegisteredSellers(@RequestParam(defaultValue = "0") Integer pageNo,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(defaultValue = "id")String sortBy){
        return adminService.findAllSellers(pageNo,pageSize,sortBy);
    }


//-------------------------------------------ACTIVATE AND DE-ACTIVATE THE CUSTOMER-------------------------------------
    @ApiOperation(value = "API to activate a customer")
    @PatchMapping("/admin/customers/activate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> activateCustomer(@PathVariable Long id,
                                                                          @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        return adminService.activateAccount(id,fields);
    }


    @ApiOperation(value = "API to de-activate a customer")
    @PatchMapping("/admin/customers/deactivate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deactivateCustomer(@PathVariable Long id,
                                                                            @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        return adminService.deactivateAccount(id,fields);
    }


//----------------------------------------ACTIVATE AND DE-ACTIVATE THE SELLER-------------------------------------------

    @ApiOperation(value = "API to activate a seller")
    @PatchMapping("/admin/sellers/activate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> activateSeller(@PathVariable Long id,
                                                                        @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        return adminService.activateAccount(id,fields);
    }


    @ApiOperation(value = "API to de-activate a seller")
    @PatchMapping("/admin/sellers/deactivate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deactivateSeller(@PathVariable Long id,
                                                                          @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id,fields);
    }

//-------------------------------------------ADMIN CATEGORY API'S-------------------------------------------------------

    @ApiOperation(value = "API add a Metadata field")
    @PostMapping("/admin/metadata-field")
    public ResponseEntity<MessageResponseEntity<CategoryMetadataFieldDTO>>
    addMetadataField(@Valid @RequestBody CategoryMetadataFieldCO categoryMetadataFieldCO){
        return adminService.addMetadataField(categoryMetadataFieldCO);
    }

    @ApiOperation(value = "API to view all Metadata fields")
    @GetMapping("/admin/metadata-fields")
    public ResponseEntity<MessageResponseEntity<List<CategoryMetadataField>>>
    listAllMetadata(@RequestParam(defaultValue = "0") Integer pageNo,
                    @RequestParam(defaultValue = "10") Integer pageSize,
                    @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllMetadata(pageNo,pageSize,sortBy);
    }

    @ApiOperation(value = "API to view all Metadata fields Values")
    @GetMapping("/admin/metadata-fields-values")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
    listAllMetadataFieldValues(){
        return adminService.listAllMetadataFieldValues();
    }


    @ApiOperation(value = "API to add a category")
    @PostMapping("/admin/categories")
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> addCategory(@Valid @RequestBody CategoryCO categoryCO){
        return adminService.addCategory(categoryCO);
    }


    @ApiOperation(value = "API to view a category")
    @GetMapping("/admin/categories/{id}")
    public ResponseEntity<MessageResponseEntity<Map<String, Object>>> listOneCategory(@PathVariable Long id){
        return adminService.listOneCategory(id);
    }


    @ApiOperation(value = "API to view all categories")
    @GetMapping("/admin/categories")
    public ResponseEntity<MessageResponseEntity<List<Category>>> listAllCategory(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                 @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllCategory(pageNo,pageSize,sortBy);
    }

    @ApiOperation(value = "API to view all categories")
    @GetMapping("/admin/rootCategories/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllSubCategory(@PathVariable Long id){
        return adminService.listAllSubCategory(id);
    }

    @ApiOperation(value = "API to view all categories")
    @PostMapping("/admin/rootCategories/{id}")
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> updateRootCategory(@PathVariable Long id,
                                                                             @RequestBody CategoryUpdateCO categoryUpdateCO){
        return adminService.updateRootCategory(id,categoryUpdateCO);
    }


    @ApiOperation(value = "API to update a category")
    @PutMapping("/admin/categories")
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> updateCategory(@Valid @RequestBody CategoryUpdateCO categoryUpdateCO){
        return adminService.updateCategory(categoryUpdateCO);
    }


    @ApiOperation(value = "API to add new category metadata field for category")
    @PostMapping("/admin/metadata-field-values")
    public ResponseEntity<MessageResponseEntity<String>> addMetadataFieldValues(@Valid @RequestBody CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO){
        return adminService.addMetadataFieldValues(categoryMetadataFieldValuesCO);
    }


    @ApiOperation(value = "API to update values for an existing metadata field")
    @PutMapping("/admin/metadata-field-values")
    public ResponseEntity<MessageResponseEntity<CategoryMetadataFieldValuesCO>>
    updateMetadataFieldValues(@Valid @RequestBody CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO){
        return adminService.updateMetadataFieldValues(categoryMetadataFieldValuesCO);
    }

//-------------------------------------------ADMIN PRODUCT API'S-------------------------------------------------------
    @ApiOperation(value = "API to view a product")
    @GetMapping("/admin/products/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(@PathVariable Long id){
        return adminService.listOneProduct(id);
    }


    @ApiOperation(value = "API to view all products")
    @GetMapping("/admin/products/variations")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProductsVariations(@RequestParam(defaultValue = "0") Integer pageNo,
                                                                                            @RequestParam(defaultValue = "10") Integer pageSize,
                                                                                            @RequestParam(defaultValue = "id") String sortBy){

        return adminService.listAllProductsVariations(pageNo,pageSize,sortBy);
    }

    @ApiOperation(value = "API to view all products")
    @GetMapping("/admin/products")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProducts(){

        return adminService.listAllProducts();
    }

    @ApiOperation(value = "API to view all products variations")
    @GetMapping("/admin/variations/{id}")
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllVariation(@PathVariable Long id){
        return adminService.listAllVariation(id);
    }


    @ApiOperation(value = "API to activate a product")
    @PutMapping("/admin/products/activate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> activateProduct(@PathVariable Long id) {
        return adminService.activateProduct(id);
    }


    @ApiOperation(value = "API to deactivate a product")
    @PutMapping("/admin/products/deactivate/{id}")
    public ResponseEntity<MessageResponseEntity<String>> deactivateProduct(@PathVariable Long id){
        return adminService.deactivateProduct(id);
    }


    @ApiOperation(value = "API to get Product Variation Image")
    @GetMapping(value = "/admin/products/variations/image/{id}")
    public ResponseEntity<Object> getProductVariationImage(@PathVariable(value = "id") Long id) {
        return adminService.getProductVariationImage(id);
    }
}






















