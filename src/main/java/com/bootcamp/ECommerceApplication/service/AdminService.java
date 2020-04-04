package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
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
import javax.transaction.Transactional;
import java.lang.reflect.Field;
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
