package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    private SmtpMailSender smtpMailSender;

    @Autowired
    private TokenStore tokenStore;


    //----------------------------------------------ADMIN HOME----------------------------------------------------------
    @GetMapping("/admin/home")
    public String adminHome() {
        return "Welcome Admin!";
    }

    //-----------------------------------------------GET USERS----------------------------------------------------------
    //GET THE LIST OF USERS
    @GetMapping
    public List<User> listOfUsers() {
        return adminService.findAllUsers();
    }

    //GET A SINGLE USER
    @GetMapping("admin/{id}")
    public Optional<User> retrieveCustomer(@PathVariable Long id)
    {
        Optional<User> user = adminService.findOne(id);
        if (!user.isPresent())
            throw new UserNotFoundException("id-"+id);
        return user;
    }

    //---------------------------------------------------TEST METHOD----------------------------------------------------
    //CREATING THE CUSTOMER MANUALLY -- USING GETTER AND SETTERS
    @GetMapping("/admin/create-customer-manually")
    public Customer createCustomerManually() {
        return adminService.createCustomerManually();
    }


    //-----------------------------------------FETCH THE LIST OF CUSTOMERS AND SELLERS----------------------------------
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


    //-------------------------------------------ACTIVATE AND DE-ACTIVATE THE CUSTOMER----------------------------------
    //ACTIVATE A CUSTOMER
    @PatchMapping(value = "/admin/customers/activate/{id}")
    public String activateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        adminService.findCustomer(id);
        return adminService.activateAccount(id,fields);

    }

    //DE-ACTIVE A CUSTOMER
    @PatchMapping(value = "/admin/customers/deactivate/{id}")
    public String deactivateCustomer(@PathVariable Long id, @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        adminService.findCustomer(id);
        return adminService.deactivateAccount(id,fields);
    }


    //----------------------------------------ACTIVATE AND DE-ACTIVATE THE SELLER---------------------------------------

    //ACTIVATE A SELLER
    @PatchMapping(value = "/admin/sellers/activate/{id}")
    public String activateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.activateAccount(id,fields);
    }

    //DE-ACTIVE A SELLER
    @PatchMapping(value = "/admin/sellers/deactivate/{id}")
    public String deactivateSeller(@PathVariable Long id, @RequestBody Map<Object,Object> fields)
            throws MessagingException {
        adminService.findSeller(id);
        return adminService.deactivateAccount(id,fields);
    }

    //----------------------------------------------------LOGOUT--------------------------------------------------------
    @GetMapping("/")
    public String test() {
        return "Welcome to the E-Commerce Application!!!";
    }

    @GetMapping({"/customers/doLogout","/admin/doLogout","/sellers/doLogout","/doLogout"})
    public String logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }

}






















