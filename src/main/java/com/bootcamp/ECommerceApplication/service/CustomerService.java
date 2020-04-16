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
import com.bootcamp.ECommerceApplication.entity.Product;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.exception.AddressNotFoundException;
import com.bootcamp.ECommerceApplication.exception.CategoryNotFoundException;
import com.bootcamp.ECommerceApplication.exception.PasswordDoesNotMatchException;
import com.bootcamp.ECommerceApplication.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.*;

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
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;
    @Autowired
    private ImageUploaderService imageUploaderService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//-------------------------------------------CUSTOMER ACCOUNT API'S-----------------------------------------------------

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
    public ResponseEntity<Object> customerUpdateAddress(String email, AddressCO addressCO, Long id) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            if (address.getUser().getId().equals(user.getId())) {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.map(addressCO, address);
                addressRepository.save(address);
                AddressDTO addressDTO = converterService.convertToAddressDto(address);
                return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            }
            else
                throw new AddressNotFoundException("Address not found: " + id);
        }
        else
            throw new AddressNotFoundException("Address not found: "+id);
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


//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------

    //Upload Profile Image
    public ResponseEntity<Object> uploadProfileImage(MultipartFile multipartFile, String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        try {
            String imageUri = imageUploaderService.uploadUserImage(multipartFile, email);
            user.setProfileImage(imageUri);
            userRepository.save(user);
            return new ResponseEntity<>(new MessageResponseEntity<>(imageUri, HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseEntity<>(HttpStatus.BAD_REQUEST, "Try again"), HttpStatus.BAD_REQUEST);
        }
    }


    //Get the Profile Image
    public ResponseEntity<Object> getProfileImage(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user.getProfileImage() != null) {
            return new ResponseEntity<>(new MessageResponseEntity<>(user.getProfileImage(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponseEntity<>(HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

//-------------------------------------------CUSTOMER CATEGORY API'S-----------------------------------------------------

    //List All Category
    public ResponseEntity<Object> listAllCustomerCategories(Long id) {
        boolean exists;
        if (id != null) {
            exists = categoryRepository.existsById(id);
            if (exists) {
                List<Map<Object, Object>> categoryList = categoryRepository.findByParentCategory(id);
                return new ResponseEntity<>(new MessageResponseEntity<>(categoryList, HttpStatus.OK), HttpStatus.OK);
            }
            else
                throw new CategoryNotFoundException("Category Not Found Exception:"+id);
        }
        else {
            List<Map<Object, Object>> categoryList = categoryRepository.findByCategoryIsNull();
            return new ResponseEntity<>(new MessageResponseEntity<>(categoryList, HttpStatus.OK), HttpStatus.OK);
        }
    }

    //API to fetch filtering details for a category
    public ResponseEntity<Object> getFilterDetails(Long id) {

        boolean exists = categoryRepository.existsById(id);

        if (!exists) {
            throw new CategoryNotFoundException("invalid Category Id");
        }

        List<Object> responseList = new ArrayList();
//    All metadata field along with possible values for that category
        responseList.add(categoryMetadataFieldValuesRepository.findByCategoryId(id));
        responseList.add(productRepository.findAllBrandsByCategoryId(id));
        responseList.add(productRepository.findMinimum(id));
        responseList.add(productRepository.findMaximum(id));

        return new ResponseEntity<>(new MessageResponseEntity<>(responseList, HttpStatus.OK), HttpStatus.OK);
    }

//-------------------------------------------CUSTOMER PRODUCT API'S-----------------------------------------------------

    //Customer Function to view a product
    public  ResponseEntity<Object>  listOneProduct(Long id) {
        List<Map<Object, Object>> product = productRepository.listOneProductCustomer(id);
        return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
    }

    //Customer Function to view all products
    public ResponseEntity<Object> listAllProducts(Integer pageNo, Integer pageSize, String sortBy, Long id) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Product> pagedResult = productRepository.listAllProductCustomer(paging,id);
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult.getContent(), HttpStatus.OK), HttpStatus.OK);
    }
}
