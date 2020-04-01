package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    private SmtpMailSender smtpMailSender;


    //******************************* FETCH THE LIST OF CUSTOMERS AND SELLERS ******************************************
    //LIST ALL ACTIVATED CUSTOMERS
    @GetMapping("/customers")
    public List<Object[]> findAllActivatedCustomers()
    {
        return adminService.findAllCustomers();
    }

    //LIST ALL ACTIVATED SELLERS
    @GetMapping("/sellers")
    public List<Object[]> findAllActivatedSellers()
    {
        return adminService.findAllSellers();
    }


    //****************************** ACTIVATE AND DE-ACTIVATE THE CUSTOMER *********************************************
    //ACTIVATE A CUSTOMER
    @PatchMapping(value = "/customers/activate/{id}")
    public String activateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findCustomer(id);
        return adminService.activateAccount(id,fields);

    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping(value = "/customers/deactivate/{id}")
    public String deactivateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findCustomer(id);
        return adminService.deactivateAccount(id,fields);
    }


    //****************************** ACTIVATE AND DE-ACTIVATE THE SELLER *********************************************

    //ACTIVATE A SELLER
    @PatchMapping(value = "/sellers/activate/{id}")
    public String activateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findSeller(id);
        return adminService.activateAccount(id,fields);
    }

    //DE-ACTIVE A SELLER
    @PatchMapping(value = "/sellers/deactivate/{id}")
    public String deactivateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id,fields);
    }
}






















