package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.TokenNotFoundException;
import com.bootcamp.ECommerceApplication.exception.UserAlreadyExists;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private SmtpMailSender smtpMailSender;

    //FETCH THE LIST OF USERS
    @GetMapping
    public List<User> listOfUsers() {
        List<User> users = userService.findAllUsers();
        return users;
    }

    //CREATING THE CUSTOMER MANUALLY -- USING GETTER AND SETTERS
    @GetMapping("/create-customer-manually")
    public Customer createCustomerManually() {
        Customer customer = userService.createCustomerManually();
        return customer;
    }

    //REQUEST TO GET A CUSTOMER
    @GetMapping("/{id}")
    public Optional<User> retrieveCustomer(@PathVariable Long id)
    {
        Optional<User> user = userService.findOne(id);
        if (!user.isPresent())
            throw new UserNotFoundException("id-"+id);

        return user;
    }

    //CREATE A SELLER
    @PostMapping("/seller")
    public String createSeller(@Valid @RequestBody Seller seller) {
        userService.createSeller(seller);
        return "The Seller Account is Successfully created with the inactive account ";
    }


    //CREATE A CUSTOMER AND SEND AN ACTIVATION LINK
    @PostMapping("/customer")
    public Object createCustomerToken(@Valid @RequestBody Customer customer) throws MessagingException {

        Customer saveCustomer = userService.createCustomer(customer);

        if(saveCustomer!= null)
        {
            ConfirmationToken confirmationToken = userService.createCustomerToken(customer);

            smtpMailSender.send(customer.getEmail(), "Complete Registration", "To activate your account, please click the link here : " +
                    "http://localhost:8080/users/confirm-account?token="+confirmationToken.getConfirmationToken());

            //HTTP RESPONSE CREATED STATUS - 201 CREATED
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(saveCustomer.getId()).toUri();

            ResponseEntity.created(location).build();

            return ResponseEntity.created(location).build();
        }
        else
        {
            throw new UserAlreadyExists("The User's EmailID Already Exist: "+customer.getEmail());
        }
    }

    //ACTIVATE THE CUSTOMER ACCOUNT GET METHOD
    @GetMapping("/confirm-account")
    public String confirmUserAccount(@RequestParam("token") String confirmationToken) throws MessagingException {

        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = userService.findToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-"+confirmationToken);

        //IF THE TOKEN IS FOUND
        Integer flag = userService.confirmUserAccount(confirmationToken);
        if(flag==0)
            return "The Previous Activation Link Is Expired, Please Verify The Account Using The New Activation Link";
        else
            return "The Account Has Been Successfully Activated";
    }

    //ACTIVATE THE CUSTOMER USING PUT METHOD
    @PutMapping("/confirm-account/{token}")
    public String confirmUserAccountToken(@PathVariable String token) throws MessagingException {
        userService.confirmUserAccount(token);
        return "Account Successfully Active";
    }

    //POST METHOD TO RE-SEND ACTIVATION LINK
    @PostMapping("/re-send-activation-link/{email}")
    public String reSendActivationLink(@PathVariable String email) throws MessagingException {
        Customer customer= userService.findCustomerByEmail(email);
        if (customer == null)
            throw new UserNotFoundException("EmailID:-"+email);

        userService.deleteConfirmationToken(email);

        ConfirmationToken newConfirmationToken = userService.createCustomerToken(customer);

        smtpMailSender.send(customer.getEmail(), "Complete Registration", "To activate your account, please click the link here : " +
                "http://localhost:8080/users/confirm-account?token="+newConfirmationToken.getConfirmationToken());

        return "The Activation Link Is Send Again";
    }

}
