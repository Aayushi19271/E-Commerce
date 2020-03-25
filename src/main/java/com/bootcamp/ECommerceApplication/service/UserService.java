package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.repository.ConfirmationTokenRepository;
import com.bootcamp.ECommerceApplication.repository.CustomerRepository;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private SmtpMailSender smtpMailSender;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    //List Of All Users
    public List<User> findAllUsers()
    {
        List<User> users = (List<User>) userRepository.findAll();
        return users;
    }

    public Customer createCustomerManually()
    {
        Customer customer = new Customer();
        customer.setEmail("yeodbdvdj@yahoo.in");
        customer.setFirst_name("abcd");
        customer.setMiddle_name("Kumar");
        customer.setLast_name("Thani");
        customer.setPassword("Aayushi12@");
        customer.setIs_active(false);
        customer.setIs_deleted(false);
        customer.setContact("8130170780");

        List<Address> list = new ArrayList<>();
        Address address = new Address();
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        address.setAddress("B7- Pitmapura");
        address.setZip_code(110085);
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

    //FIND ONE CUSTOMER
    public Optional<User> findOne(Long id)
    {
        return userRepository.findById(id);
    }

    //FIND ONE TOKEN
    public ConfirmationToken findToken(String token)
    {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    public Customer findCustomerByEmail(String email)
    {
       return customerRepository.findByEmailIgnoreCase(email);
    }

    //Create a New Customer
    public Customer createCustomer(Customer customer)
    {
        String customerEmail = customer.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);
        if(user == null)
        {
            customer.setIs_deleted(false);
            customer.setIs_active(false);

            ArrayList<Role> tempRole = new ArrayList<>();
            Role role = roleRepository.findById((long) 3).get();
            tempRole.add(role);
            customer.setRoles(tempRole);

            Customer saveCustomer = userRepository.save(customer);
            return saveCustomer;
        }
        else
            return null;
    }

    //Create a New Seller
    public void createSeller(Seller seller)
    {
        seller.setIs_active(false);
        seller.setIs_deleted(false);

        String companyname = seller.getCompany_name().toLowerCase();
        seller.setCompany_name(companyname);

        ArrayList<Role> tempRole = new ArrayList<>();
        Role role = roleRepository.findById((long) 2).get();
        tempRole.add(role);
        seller.setRoles(tempRole);

        Seller saveseller = userRepository.save(seller);
    }

    //CREATE CUSTOMER WHERE IS_ACTIVE = FALSE AND SAVE THE CONFIRMATION TOKEN IN ENTITY
    public ConfirmationToken createCustomerToken(Customer customer)
    {
        ConfirmationToken confirmationToken = new ConfirmationToken(customer);
        confirmationToken.setExpiryDate(new Date());

        confirmationTokenRepository.save(confirmationToken);
        return  confirmationToken;
    }

    //DELETE THE EXISTING TOKEN
    @Transactional
    public void deleteConfirmationToken(String email)
    {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        ConfirmationToken token = customer.getConfirmationToken();
        confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
    }

    //ACTIVATE THE CUSTOMER ACCOUNT AND DELETE THE TOKEN -- token correct, token expires, token wrong
    @Transactional
    public Integer confirmUserAccount(String confirmationToken) throws MessagingException {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        Customer customer = token.getCustomer();
        Calendar calendar = Calendar.getInstance();

        //IF THE TOKEN EXPIRES
        if((token.getExpiryDate().getTime()-calendar.getTime().getTime())<=0)
        {
            ConfirmationToken newConfirmationToken = new ConfirmationToken(token.getCustomer());
            newConfirmationToken.setExpiryDate(new Date());
            confirmationTokenRepository.save(newConfirmationToken);

            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());

            smtpMailSender.send(customer.getEmail(), "Complete Registration", "To activate your account, please click the link here : " +
                    "http://localhost:8080/users/confirm-account?token="+newConfirmationToken.getConfirmationToken());

            return 0;
        }
        //IF THE TOKEN IS CORRECT
        else
        {
            User user = userRepository.findByEmailIgnoreCase(token.getCustomer().getEmail());
            user.setIs_active(true);
            userRepository.save(user);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());

            smtpMailSender.send(customer.getEmail(), "Account Activated",
                    "Dear "+customer.getFirst_name()+", Your Account Has Been Activated!!");
            return 2;
        }
    }
}
