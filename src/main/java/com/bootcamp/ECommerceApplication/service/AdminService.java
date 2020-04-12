package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.Seller;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.MailSendFailedException;
import com.bootcamp.ECommerceApplication.exception.UserActiveException;
import com.bootcamp.ECommerceApplication.exception.UserDeactiveException;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.repository.CustomerRepository;
import com.bootcamp.ECommerceApplication.repository.SellerRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.util.*;

@Service
public class AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private SmtpMailSender smtpMailSender;

//---------------------------------------------------TEST METHODS-------------------------------------------------------

    //CREATE THE CUSTOMER USING GETTER AND SETTERS - TEST METHOD
    public Customer createCustomerManually() {
        Customer customer = new Customer();
        customer.setEmail("aayushithani27@gmail.in");
        customer.setFirstName("abcd");
        customer.setMiddleName("Kumar");
        customer.setLastName("Thani");
        customer.setPassword("Aayushi12@");
        customer.setActive(false);
        customer.setDeleted(false);
        customer.setContact("8130170780");
        userRepository.save(customer);
        return customer;
    }

//----------------------------------------------ADMIN SPECIFIED METHODS-------------------------------------------------
//------------------------------------------LIST OF USERS,SELLERS,CUSTOMERS---------------------------------------------

    //List Of All Users
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    //LIST OF CUSTOMERS - PAGING (0,10) AND SORTING ASC "ID"
    public List<Object[]> findAllCustomers() {
        PageRequest pageable = PageRequest.of(0,10, Sort.Direction.ASC,"id");
        return customerRepository.findAllCustomers(pageable);
    }

    //LIST OF SELLERS - PAGING (0,10) AND SORTING ASC "ID"
    public List<Object[]> findAllSellers() {
        PageRequest pageable = PageRequest.of(0,10, Sort.Direction.ASC,"id");
        return sellerRepository.findAllSellers(pageable);
    }

//------------------------------------------FIND/SAVE A USERS,SELLERS,CUSTOMERS-----------------------------------------
    //FIND A CUSTOMER
    public void findCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(!customer.isPresent())
            throw new UserNotFoundException("Customer Not Found Id-"+id);
    }

    //FIND A SELLER
    public void findSeller(Long id) {
        Optional<Seller> seller = sellerRepository.findById(id);
        if(!seller.isPresent())
            throw new UserNotFoundException("Seller Not Found Id-"+id);
    }

    //FIND A USER
    public User getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    //SAVE THE USER
    public void updateUser(User user) {
        userRepository.save(user);
    }

//----------------------------------------ACTIVATE AND DE-ACTIVATE THE ACCOUNT------------------------------------------
    //ACTIVATE AN ACCOUNT
    public ResponseEntity<Object> activateAccount(Long id) throws MessagingException {
        User user = getUser(id);
        boolean flag = user.isActive();
        if (!flag) {
            user.setActive(true);
            String updatedBy = "admin@aayushi";
            user.setUpdatedBy(updatedBy);
            user.setLastUpdated(new Date());
            updateUser(user);
            try {
                smtpMailSender.send(user.getEmail(), "Account Activated!", "Dear  " + user.getFirstName() + ", You're Account Has Been Activated!");
                return new ResponseEntity<>(new MessageResponseEntity<>("Account Activated Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
            }
            catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+user.getEmail());
            }
        }
        else
            throw  new UserActiveException("User is already Activated: "+user.getEmail());
    }


    //DE-ACTIVATE THE ACCOUNT
    public ResponseEntity<Object> deactivateAccount(Long id) throws MessagingException {
        User user = getUser(id);
        boolean flag = user.isActive();
        if (flag) {
            user.setActive(false);
            String updatedBy = "admin@aayushi";
            user.setUpdatedBy(updatedBy);
            user.setLastUpdated(new Date());
            updateUser(user);
            try {
                smtpMailSender.send(user.getEmail(), "Account De-activated!", "Dear  "+user.getFirstName()+", You're Account Has Been De-activated!");
                return new ResponseEntity<>(new MessageResponseEntity<>("Account De-activated Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
            }catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+user.getEmail());
            }
        }
        else
            throw new UserDeactiveException("User is already De-activated: "+user.getEmail());
    }
}
