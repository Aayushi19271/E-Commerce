package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Seller;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.AddressNotFoundException;
import com.bootcamp.ECommerceApplication.exception.PasswordDoesNotMatchException;
import com.bootcamp.ECommerceApplication.repository.AddressRepository;
import com.bootcamp.ECommerceApplication.repository.CategoryMetadataFieldValuesRepository;
import com.bootcamp.ECommerceApplication.repository.SellerRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private SmtpMailSender smtpMailSender;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //Get the LoggedIn Seller's Profile Details
    public ResponseEntity<Object> sellerProfile(String email)
    {
        Seller seller = (Seller) userRepository.findByEmailIgnoreCase(email);
        SellerDTO sellerDTO = converterService.convertToSellerDto(seller);
        return new ResponseEntity<>(new MessageResponseEntity<>(sellerDTO, HttpStatus.OK), HttpStatus.OK);
    }

    //Update the Profile of LoggedIn Seller
    public ResponseEntity<Object> sellerUpdateProfile(String email, Map<Object,Object> fields)
    {
        Seller seller= (Seller) userRepository.findByEmailIgnoreCase(email);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findRequiredField(Seller.class, (String)k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, seller, v);
        });
        sellerRepository.save(seller);
        SellerDTO sellerDTO = converterService.convertToSellerDto(seller);
        return new ResponseEntity<>(new MessageResponseEntity<>(sellerDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //Update the LoggedIn Seller's Password And Send Mail Upon Change
    public ResponseEntity<Object> sellerUpdatePassword(String email, PasswordCO passwordCO) throws MessagingException {
        Seller seller= (Seller) userRepository.findByEmailIgnoreCase(email);
        if(passwordCO.getPassword().equals(passwordCO.getConfirmPassword())) {
            seller.setPassword(passwordEncoder.encode(passwordCO.getPassword()));
            seller.setUpdatedBy("user@"+seller.getFirstName());
            seller.setLastUpdated(new Date());
            userRepository.save(seller);
            smtpMailSender.send(seller.getEmail(), "Password Changed!",
                    "Dear "+seller.getFirstName()+" ,Your Password has been changed Successfully!");
        }
        else {
            throw new PasswordDoesNotMatchException("Password and Confirm Password does not match!");
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Password Changed Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
    }


    //Update the already existing Address of LoggedIn Seller
    public ResponseEntity<Object> sellerUpdateAddress(String email, AddressCO addressCO) {
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

    //List All Category
    public List<Map<Object, Object>> listAllCategories() {
        return categoryMetadataFieldValuesRepository.findAllCategories();
    }

}
