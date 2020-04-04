package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.TokenNotFoundException;
import com.bootcamp.ECommerceApplication.exception.UserAlreadyExists;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
import com.bootcamp.ECommerceApplication.repository.ConfirmationTokenRepository;
import com.bootcamp.ECommerceApplication.repository.CustomerRepository;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.net.URI;
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

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //************************************USER'S METHOD - FINDERS AND CREATE CUSTOMER***********************************

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

    //FIND CUSTOMER BY EMAIL ID
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }


//*******************************CONFIRMATION TOKEN METHOD'S************************************************************
    //FIND ONE TOKEN
    public ConfirmationToken findConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }


    //DELETE THE EXISTING TOKEN
    @Transactional
    public void deleteConfirmationToken(String email) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        ConfirmationToken token = customer.getConfirmationToken();
        confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
    }


    //CREATE & RETURN CONFIRMATION TOKEN
    public ConfirmationToken createConfirmationToken(Customer customer) {
        ConfirmationToken confirmationToken = new ConfirmationToken(customer);
        confirmationToken.setExpiryDate(new Date());

        confirmationTokenRepository.save(confirmationToken);
        return  confirmationToken;
    }

//*****************************************SELLER METHOD***************************************************************

    //REGISTER A SELLER - SET THE ACCOUNT AS INACTIVE ACCOUNT, WAIT FOR ADMIN APPROVAL
    public ResponseEntity<Object> createSeller(Seller seller) throws MessagingException {
        String customerEmail = seller.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);

        if(user == null)
        {
            seller.setActive(false);
            seller.setDeleted(false);
            seller.setPassword(passwordEncoder.encode(seller.getPassword()));
            String companyName = seller.getCompany_name().toLowerCase();
            seller.setCompany_name(companyName);

            ArrayList<Role> tempRole = new ArrayList<>();
            Role role = roleRepository.findById((long) 2).get();
            tempRole.add(role);
            seller.setRoles(tempRole);

            Seller saveSeller = userRepository.save(seller);
            smtpMailSender.send(seller.getEmail(), "Pending Approval",
                    "The Account has been Registered but is Pending Approval! ");

            //HTTP RESPONSE CREATED STATUS - 201 CREATED
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(saveSeller.getId()).toUri();

            ResponseEntity.created(location).build();

            return ResponseEntity.created(location).build();
        }
        else
            throw new UserAlreadyExists("The Seller's EmailID Already Exist: "+seller.getEmail());
    }



//***********************************************CUSTOMER REGISTRATION METHOD*******************************************

    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    public ResponseEntity<Object> createCustomer(Customer customer) throws MessagingException {
        String customerEmail = customer.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);
        if(user == null)
        {
            customer.setDeleted(false);
            customer.setActive(false);
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));

            ArrayList<Role> tempRole = new ArrayList<>();
            Role role = roleRepository.findById((long) 3).get();
            tempRole.add(role);
            customer.setRoles(tempRole);

            Customer saveCustomer = userRepository.save(customer);
            ConfirmationToken confirmationToken = createConfirmationToken(customer);

            smtpMailSender.send(customer.getEmail(), "Complete Registration", "To activate your account, please click the link here : " +
                    "http://localhost:8080/users/customers/confirm-account?token="+confirmationToken.getConfirmationToken());

            //HTTP RESPONSE CREATED STATUS - 201 CREATED
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(saveCustomer.getId()).toUri();

            ResponseEntity.created(location).build();
            return ResponseEntity.created(location).build();
        }
        else
            throw new UserAlreadyExists("The Customer's EmailID Already Exist: "+customer.getEmail());
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @Transactional
    public String confirmUserAccountToken(String confirmationToken) throws MessagingException {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-"+confirmationToken);

        //IF THE TOKEN IS FOUND
        Integer flag = confirmTokenExpiry(confirmationToken);
        if(flag==0)
            return "The Previous Activation Link Is Expired, Please Verify The Account Using The New Activation Link";
        else
            return "The Account Has Been Successfully Activated";
    }


    //CHECK THE TOKEN EXPIRY (3 CONDITIONS-- token correct, token expires, token wrong)
    @Transactional
    public Integer confirmTokenExpiry(String confirmationToken) throws MessagingException {
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
                    "http://localhost:8080/users/customers/confirm-account?token="+newConfirmationToken.getConfirmationToken());

            return 0;
        }
        //IF THE TOKEN IS CORRECT
        else
        {
            User user = userRepository.findByEmailIgnoreCase(token.getCustomer().getEmail());
            user.setActive(true);
            userRepository.save(user);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());

            smtpMailSender.send(customer.getEmail(), "Account Activated",
                    "Dear "+customer.getFirstName()+", Your Account Has Been Activated!!");
            return 2;
        }
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @Transactional
    public String reSendActivationLink(User user) throws MessagingException {
        String email = user.getEmail();
        Customer customer= findCustomerByEmail(email);
        if (customer == null)
            throw new UserNotFoundException("EmailID:-"+email);

        if(customer.isActive())
        {
            return "The Account is already Activated";
        }
        else
        {
            deleteConfirmationToken(email);
            ConfirmationToken newConfirmationToken = createConfirmationToken(customer);

            smtpMailSender.send(customer.getEmail(), "Complete Registration", "To activate your account, please click the link here : " +
                    "http://localhost:8080/users/customers/confirm-account?token="+newConfirmationToken.getConfirmationToken());

            return "The Activation Link Is Send Again";
        }
    }
}
