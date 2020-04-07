package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.Seller;
import com.bootcamp.ECommerceApplication.entity.User;
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
import org.springframework.util.ReflectionUtils;

import javax.mail.MessagingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        customer.setEmail("yeodbdvdj@yahoo.in");
        customer.setFirstName("abcd");
        customer.setMiddleName("Kumar");
        customer.setLastName("Thani");
        customer.setPassword("Aayushi12@");
        customer.setActive(false);
        customer.setDeleted(false);
        customer.setContact("8130170780");

        List<Address> list = new ArrayList<>();
        Address address = new Address();
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        address.setAddress("B7- Pitmapura");
        address.setZipCode(110085);
        address.setLabel("Home");
        address.setUser(customer);
        list.add(address);
        customer.setAddresses(list);
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
        return userRepository.findById(id).get();
    }

    //SAVE THE USER
    public void updateUser(User user) {
        userRepository.save(user);
    }

//----------------------------------------ACTIVATE AND DE-ACTIVATE THE ACCOUNT------------------------------------------
    //ACTIVATE AN ACCOUNT
    public ResponseEntity<Object> activateAccount(Long id, Map<Object,Object> fields) throws MessagingException {
        User user = getUser(id);
        boolean flag = user.isActive();
        if (!flag) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(User.class, (String) k);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, v);
            });
            updateUser(user);
            try {
                smtpMailSender.send(user.getEmail(), "Account Activated!", "Dear  " + user.getFirstName() + ", You're Account Has Been Activated!");
                return new ResponseEntity<Object>("Account Activated Successfully!", HttpStatus.CREATED);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("Failed to send email to: " + user.getEmail() + " reason: " + ex.getMessage());
                return new ResponseEntity<Object>("Failed To Send Email!", HttpStatus.BAD_GATEWAY);
            }
        }
        else
            return new ResponseEntity<Object>("Account Already Active!", HttpStatus.CREATED);
    }


    //DE-ACTIVATE THE ACCOUNT
    public ResponseEntity<Object> deactivateAccount(Long id, Map<Object,Object> fields) throws MessagingException {
        User user = getUser(id);
        boolean flag = user.isActive();

        if (!flag) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(User.class, (String) k);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, v);
            });
            updateUser(user);
            try {
                smtpMailSender.send(user.getEmail(), "Account De-activated!", "Dear  "+user.getFirstName()+", You're Account Has Been De-activated!");
                return new ResponseEntity<Object>("Account Successfully De-activated!", HttpStatus.CREATED);
            }catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("Failed to send email to: " + user.getEmail() + " reason: " + ex.getMessage());
                return new ResponseEntity<Object>("Failed To Send Email!", HttpStatus.BAD_GATEWAY);
            }
        }
        else
        return new ResponseEntity<Object>("Account Already De-active!", HttpStatus.CREATED);
    }
}
