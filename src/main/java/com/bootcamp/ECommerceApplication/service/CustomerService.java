package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.co.CustomerUpdateProfileCO;
import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.CustomerDTO;
import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.AddressNotFoundException;
import com.bootcamp.ECommerceApplication.exception.PasswordDoesNotMatchException;
import com.bootcamp.ECommerceApplication.repository.AddressRepository;
import com.bootcamp.ECommerceApplication.repository.CustomerRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private SmtpMailSender smtpMailSender;
    @Autowired
    private CustomerRepository customerRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //Get the LoggedIn Customer's Profile Details
    public ResponseEntity<Object> customerProfile(String email)
    {
        Customer customer = (Customer) userRepository.findByEmailIgnoreCase(email);
        CustomerDTO customerDTO = converterService.convertToCustomerDto(customer);
        return new ResponseEntity<>(new MessageResponseEntity<>(customerDTO, HttpStatus.OK), HttpStatus.OK);
    }


    //Get the list of LoggedIn Customer's Addresses
    public ResponseEntity<Object> customerAddresses(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> result =  userRepository.findAllAddress(user.getId());
        if(result==null)
            throw new AddressNotFoundException("Not Address Found: "+user.getEmail());
        return new ResponseEntity<>(new MessageResponseEntity<>(result, HttpStatus.OK), HttpStatus.OK);
    }

    //Change the LoggedIn Customer's Password And Send Mail Upon Change
    public ResponseEntity<Object> customerUpdatePassword(String email, PasswordCO passwordCO) throws MessagingException {
        Customer customer= (Customer) userRepository.findByEmailIgnoreCase(email);
        if(passwordCO.getPassword().equals(passwordCO.getConfirmPassword())) {
            customer.setPassword(passwordEncoder.encode(passwordCO.getPassword()));
            customer.setUpdatedBy("user@"+customer.getFirstName());
            customer.setLastUpdated(new Date());
            userRepository.save(customer);
            smtpMailSender.send(customer.getEmail(), "Password Changed!",
                    "Dear "+customer.getFirstName()+" ,Your Password has been changed Successfully!");
        }
        else {
            throw new PasswordDoesNotMatchException("Password and Confirm Password does not match!");
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Password Changed Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //Add the New Address to the LoggedIn Customer
    public ResponseEntity<Object> customerAddAddress(String email, AddressCO addressCO) {
        Address address = converterService.convertToAddress(addressCO);
        User user = userRepository.findByEmailIgnoreCase(email);
        address.setUser(user);
        addressRepository.save(address);
        AddressDTO addressDTO = converterService.convertToAddressDto(address);
        return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //Delete the already existing Address of the LoggedIn Customer
    @Transactional
    public ResponseEntity<Object> customerDeleteAddress(String email, Long id) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Optional<Address> optionalAddress = addressRepository.findById(id);
        Address address;
        if(optionalAddress.isPresent())
        {
            address = optionalAddress.get();
            User savedUser = address.getUser();
            if (user.getId().equals(savedUser.getId())) {
                addressRepository.deleteByAddressID(id);
                return new ResponseEntity<>(new MessageResponseEntity<>("Address Deleted!", HttpStatus.OK), HttpStatus.OK);
            }
            else
                throw new AddressNotFoundException("Address not found: " + id);
        }
        else
            throw new AddressNotFoundException("Address not found: "+id);
    }

    //Update the already existing Address of LoggedIn Customer
    public ResponseEntity<Object> customerUpdateAddress(String email, AddressCO addressCO) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Address updatedAddress = converterService.convertToAddress(addressCO);
        Optional<Address> optionalAddress = addressRepository.findById(addressCO.getId());
        if (optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            User savedUser = address.getUser();
            if (savedUser.getId().equals(user.getId()))
            {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.map(updatedAddress, address);
                address.setUser(user);
                addressRepository.save(address);
                AddressDTO addressDTO = converterService.convertToAddressDto(address);
                return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            }
            else
                throw new AddressNotFoundException("Address not found: " + addressCO.getId());
        }
        else
            throw new AddressNotFoundException("Address not found: "+addressCO.getId());
    }


    //Update the Profile of LoggedIn Customer
    public ResponseEntity<Object> customerUpdateProfile(String email, CustomerUpdateProfileCO customerUpdateProfileCO) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(customerUpdateProfileCO,customer);
        customerRepository.save(customer);
        CustomerDTO customerDTO = converterService.convertToCustomerDto(customer);
        return new ResponseEntity<>(new MessageResponseEntity<>(customerDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }
}
