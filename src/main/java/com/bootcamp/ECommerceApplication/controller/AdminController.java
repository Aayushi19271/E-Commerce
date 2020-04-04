package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    private SmtpMailSender smtpMailSender;

    @Autowired
    private TokenStore tokenStore;

    //******************************* LOGOUT ********************************************
    @GetMapping("/")
    public String test() {
        return "Welcome to the E-Commerce Application!!!";
    }

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }

    //******************************* ADMIN HOME ******************************************

    @GetMapping("/admin/home")
    public String adminHome() {
        return "Welcome Admin!";
    }


    //******************************* FETCH THE LIST OF CUSTOMERS AND SELLERS *************
    //LIST ALL ACTIVATED CUSTOMERS
    @GetMapping("/admin/customers")
    public List<Object[]> findAllActivatedCustomers()
    {
        return adminService.findAllCustomers();
    }

    //LIST ALL ACTIVATED SELLERS
    @GetMapping("/admin/sellers")
    public List<Object[]> findAllActivatedSellers()
    {
        return adminService.findAllSellers();
    }


    //****************************** ACTIVATE AND DE-ACTIVATE THE CUSTOMER *****************
    //ACTIVATE A CUSTOMER
    @PatchMapping(value = "/admin/customers/activate/{id}")
    public String activateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findCustomer(id);
        return adminService.activateAccount(id,fields);

    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping(value = "/admin/customers/deactivate/{id}")
    public String deactivateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findCustomer(id);
        return adminService.deactivateAccount(id,fields);
    }


    //****************************** ACTIVATE AND DE-ACTIVATE THE SELLER *******************

    //ACTIVATE A SELLER
    @PatchMapping(value = "/admin/sellers/activate/{id}")
    public String activateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findSeller(id);
        return adminService.activateAccount(id,fields);
    }

    //DE-ACTIVE A SELLER
    @PatchMapping(value = "/admin/sellers/deactivate/{id}")
    public String deactivateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields) throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id,fields);
    }
}






















