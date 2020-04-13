package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.CategoryCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldValuesCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.CategoryDTO;
import com.bootcamp.ECommerceApplication.dto.CategoryMetadataFieldDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.*;
import com.bootcamp.ECommerceApplication.repository.*;
import jdk.jfr.TransitionTo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Autowired
    ConverterService converterService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

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
    public List<Customer> findAllCustomers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Customer> pagedResult = customerRepository.findAll(paging);
        return pagedResult.getContent();
    }

    //LIST OF SELLERS - PAGING (0,10) AND SORTING ASC "ID"
    public List<Seller> findAllSellers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Seller> pagedResult = sellerRepository.findAll(paging);
        return pagedResult.getContent();
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
//-------------------------------------------ADMIN CATEGORY API'S-------------------------------------------------------


    //Admin Function to Add Category Metadata Field
    public ResponseEntity<Object> addMetadataField(CategoryMetadataFieldCO categoryMetadataFieldCO) {
        CategoryMetadataField categoryMetadataField = converterService.convertToCategoryMetadataField(categoryMetadataFieldCO);
        String name = categoryMetadataField.getName();

        CategoryMetadataField savedCategoryMetadataField = categoryMetadataFieldRepository.findByNameIgnoreCase(name);
        if (savedCategoryMetadataField==null){
            categoryMetadataFieldRepository.save(categoryMetadataField);
            CategoryMetadataFieldDTO categoryMetadataFieldDTO = converterService.convertToCategoryMetadataFieldDTO(categoryMetadataField);
            return new ResponseEntity<>(new MessageResponseEntity<>(categoryMetadataFieldDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        else {
            throw new MetadataFieldExistsException("The Metadata Field Already Exists: "+categoryMetadataField.getName());
        }
    }


    //Admin Function to List All Category Metadata Field
    public List<CategoryMetadataField> listAllMetadata(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<CategoryMetadataField> pagedResult = categoryMetadataFieldRepository.findAll(paging);
        return pagedResult.getContent();
    }


    //Admin Function to Add New Category
    public ResponseEntity<Object> addCategory(CategoryCO categoryCO) {
        if(categoryCO.getParentId()==null){
            Category category = new Category();
            category.setLeafNode(false);
            category.setName(categoryCO.getName());
            categoryRepository.save(category);
            CategoryDTO categoryDTO = converterService.convertToCategoryDTO(category);
            return new ResponseEntity<>(new MessageResponseEntity<>(categoryDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        else {
            Optional<Category> optionalParentCategory = categoryRepository.findById(categoryCO.getParentId());
            if (optionalParentCategory.isPresent()) {
                Category parentCategory = optionalParentCategory.get();
                Category category = new Category();
                category.setLeafNode(true);
                category.setName(categoryCO.getName());
                category.setParent(parentCategory);
                categoryRepository.save(category);
                CategoryDTO categoryDTO = converterService.convertToCategoryDTO(category);
                return new ResponseEntity<>(new MessageResponseEntity<>(categoryDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            }
            else
                return new ResponseEntity<>("Parent Category Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class
        }
    }

    //Admin Function to list All Category
    public List<Category> listAllCategory(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Category> pagedResult = categoryRepository.findAll(paging);
        return pagedResult.getContent();
    }

   //Admin Function to List One Category
    public ResponseEntity<Category> listOneCategory(Long id) {
        return categoryRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Admin Function to Update One Category
    public ResponseEntity<Object> updateCategory(CategoryCO categoryCO, Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()){
            Category savedCategory = optionalCategory.get();
            ModelMapper mapper = new ModelMapper();
            mapper.map(categoryCO,savedCategory);
            categoryRepository.save(savedCategory);
            CategoryDTO categoryDTO = converterService.convertToCategoryDTO(savedCategory);
            return new ResponseEntity<>(new MessageResponseEntity<>(categoryDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("Category Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class
        }
    }


    //Admin Function to Add Metadata Field Values
    public ResponseEntity<Object> addMetadataFieldValues(CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO) {
        Long categoryId = categoryMetadataFieldValuesCO.getCategoryId();
        Long categoryMetadataFieldId = categoryMetadataFieldValuesCO.getCategoryMetadataFieldId();
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> optionalCategoryMetadataField= categoryMetadataFieldRepository.findById(categoryMetadataFieldId);

        if(!optionalCategory.isPresent())
            return new ResponseEntity<>("Message: Category Id Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class
        if (!optionalCategoryMetadataField.isPresent())
            return new ResponseEntity<>("Message: Category Metadata Field Id Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class

        Category category = optionalCategory.get();
        CategoryMetadataField categoryMetadataField = optionalCategoryMetadataField.get();
        CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();  //create
        categoryMetadataFieldValues.setCategory(category);
        categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
        categoryMetadataFieldValues.setValue(categoryMetadataFieldValuesCO.getValue());
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
        return new ResponseEntity<>(new MessageResponseEntity<>("Category Metadata Field Values Successfully Added!", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //Admin Function to Update Metadata Field Values
    public ResponseEntity<Object> updateMetadataFieldValues(CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO) {
        Long categoryId = categoryMetadataFieldValuesCO.getCategoryId();
        Long categoryMetadataFieldId = categoryMetadataFieldValuesCO.getCategoryMetadataFieldId();
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> optionalCategoryMetadataField= categoryMetadataFieldRepository.findById(categoryMetadataFieldId);
        if(!optionalCategory.isPresent())
            return new ResponseEntity<>("Message: Category Id Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class
        if (!optionalCategoryMetadataField.isPresent())
            return new ResponseEntity<>("Message: Category Metadata Field Id Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class
        Category category = optionalCategory.get();
        CategoryMetadataField categoryMetadataField = optionalCategoryMetadataField.get();
        CategoryMetadataFieldValues categoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findCategoryMetadataFieldValues(category.getId(),categoryMetadataField.getId());
        if (categoryMetadataFieldValues!=null){
            CategoryMetadataFieldValues newCategoryMetadataFieldValues = new CategoryMetadataFieldValues();  //create
            newCategoryMetadataFieldValues.setCategory(category);
            newCategoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
            newCategoryMetadataFieldValues.setValue(categoryMetadataFieldValuesCO.getValue());
            ModelMapper mapper = new ModelMapper();
            mapper.map(newCategoryMetadataFieldValues,categoryMetadataFieldValues);
            categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
            return new ResponseEntity<>(new MessageResponseEntity<>("Category Metadata Field Values Successfully Updated!", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>("Message: Category Metadata Field ID Not Found!", HttpStatus.NOT_FOUND);  //replace with exception class
    }

}
