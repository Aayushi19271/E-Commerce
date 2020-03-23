package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    //List Of All Users
    public List<User> findAllUsers()
    {
        List<User> users = (List<User>) userRepository.findAll();
        return users;
    }

    public Customer createCustomerManually()
    {
        Customer customer = new Customer();
        customer.setEmail("aayushithani@yahoo.in");
        customer.setFirst_name("abcd");
        customer.setMiddle_name("Kumar");
        customer.setLast_name("Thani");
        customer.setPassword("abcd");
        customer.setIs_active(false);
        customer.setIs_deleted(false);
        customer.setContact(983753788);

//        List<Address> list = new ArrayList<>();
//        Address address = new Address();
//        address.setCity("Delhi");
//        address.setState("Delhi");
//        address.setCountry("India");
//        address.setAddress("B7- Pitmapura");
//        address.setZip_code(110085);
//        address.setLabel("Home");
//        list.add(address);
//        customer.setAddresses(list);

        userRepository.save(customer);
        return customer;
    }

    //Create a New Customer
    public void createCustomer(Customer customer)
    {
        Customer saveCustomer = userRepository.save(customer);
    }

    //Create a New Seller
    public void createSeller(Seller seller)
    {
        Seller saveseller = userRepository.save(seller);
    }
}
