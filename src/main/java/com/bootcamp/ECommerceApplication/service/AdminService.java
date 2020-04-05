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

//        ArrayList<Role> tempRole = new ArrayList<>();
//        Role role = roleRepository.findById((long) 3).get();
//        tempRole.add(role);
//        customer.setRoles(tempRole);

        userRepository.save(customer);
        return customer;
    }


    //List Of All Users
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    //GET A SINGLE USER
    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

//----------------------------------------------ADMIN SPECIFIED METHODS-------------------------------------------------

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

    //GET A USER
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    //SAVE THE USER
    public void updateUser(User user) {
        userRepository.save(user);
    }


    //ACTIVATE A ACCOUNT
    public String activateAccount(Long id, Map<Object,Object> fields) throws MessagingException {
        User user = getUser(id);
        Boolean flag = user.isActive();

        if (!flag) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(User.class, (String) k);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, v);
            });
            updateUser(user);

            smtpMailSender.send(user.getEmail(), "Account Activated!", "Dear  " + user.getFirstName() + ", You're Account Has Been Activated!");
            return "Account Successfully Activated";
        } else
            return "The Customer's Account is Already Active!";
    }

    //DE-ACTIVATE THE ACCOUNT
    public String deactivateAccount(Long id, Map<Object,Object> fields) throws MessagingException {
        User user = getUser(id);
        Boolean flag = user.isActive();

        if (!flag) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(User.class, (String) k);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, v);
            });
            updateUser(user);

            smtpMailSender.send(user.getEmail(), "Account De-activated!", "Dear  "+user.getFirstName()+", You're Account Has Been De-activated!");
            return "Account Successfully De-activated";
        } else
            return "The Customer's Account is Already De-active!";
    }
}
