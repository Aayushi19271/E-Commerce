package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    public List<Object[]> findAllActivatedCustomers()
    {
        return adminService.findAllCustomers();
    }

    //LIST ALL REGISTERED SELLERS
    @GetMapping("/admin/sellers")
    public List<Object[]> findAllActivatedSellers()
    {
        return adminService.findAllSellers();
    }


//-------------------------------------------ACTIVATE AND DE-ACTIVATE THE CUSTOMER-------------------------------------
    //ACTIVATE A CUSTOMER
    @PatchMapping(value = "/admin/customers/activate/{id}")
    public ResponseEntity<Object> activateCustomer(@PathVariable Long id)
            throws MessagingException {
        adminService.findCustomer(id);
        return adminService.activateAccount(id);

    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping(value = "/admin/customers/deactivate/{id}")
    public ResponseEntity<Object> deactivateCustomer(@PathVariable Long id)
            throws MessagingException {
        adminService.findCustomer(id);
        return adminService.deactivateAccount(id);
    }


//----------------------------------------ACTIVATE AND DE-ACTIVATE THE SELLER-------------------------------------------

    //ACTIVATE A SELLER
    @PatchMapping(value = "/admin/sellers/activate/{id}")
    public ResponseEntity<Object> activateSeller(@PathVariable Long id)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.activateAccount(id);
    }

    //DE-ACTIVE A SELLER
    @PatchMapping(value = "/admin/sellers/deactivate/{id}")
    public ResponseEntity<Object> deactivateSeller(@PathVariable Long id)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id);
    }
}






















