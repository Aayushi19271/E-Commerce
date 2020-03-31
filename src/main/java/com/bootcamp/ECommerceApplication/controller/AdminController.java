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

    //ACTIVATE A CUSTOMER
    @PatchMapping(value = "/customers/activate/{id}")
    public String activateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findCustomer(id);
        User user = adminService.getUser(id);
        Boolean flag = adminService.activateAccount(user,fields);

        if (flag)
            return "Account Successfully Activated";
        else
            return "The Customer's Account is Already Active!";
    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping(value = "/customers/deactivate/{id}")
    public String deactivateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findCustomer(id);
        User user = adminService.getUser(id);
        Boolean flag = adminService.deactivateAccount(user,fields);

        if (flag)
            return "Account Successfully De-activated";
        else
            return "The Customer's Account is Already De-active!";
    }

    //ACTIVATE A SELLER
    @PatchMapping(value = "/sellers/activate/{id}")
    public String activateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findSeller(id);
        User user = adminService.getUser(id);
        Boolean flag = adminService.activateAccount(user,fields);

        if (flag)
            return "Account Successfully Activated";
        else
            return "The Seller's Account is Already Active!";
    }

    //DE-ACTIVE A SELLER
    @PatchMapping(value = "/sellers/deactivate/{id}")
    public String deactivateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findSeller(id);
        User user = adminService.getUser(id);
        Boolean flag = adminService.deactivateAccount(user,fields);

        if (flag)
            return "Account Successfully De-activated";
        else
            return "The Seller's Account is Already De-active!";
    }
}






















