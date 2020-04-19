package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.CustomerDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.AddressNotFoundException;
import com.bootcamp.ECommerceApplication.exception.CategoryNotFoundException;
import com.bootcamp.ECommerceApplication.exception.PasswordDoesNotMatchException;
import com.bootcamp.ECommerceApplication.exception.ProductNotFoundException;
import com.bootcamp.ECommerceApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
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
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private MessageSource messageSource;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


//-------------------------------------------CUSTOMER ACCOUNT API'S-----------------------------------------------------

    //Get the LoggedIn Customer's Profile Details
    public ResponseEntity<MessageResponseEntity<CustomerDTO>> customerProfile(String email)
    {
        Customer customer = (Customer) userRepository.findByEmailIgnoreCase(email);
        CustomerDTO customerDTO = converterService.convertToCustomerDto(customer);
        return new ResponseEntity<>(new MessageResponseEntity<>(customerDTO, HttpStatus.OK), HttpStatus.OK);
    }


    //Get the list of LoggedIn Customer's Addresses
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> customerAddresses(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> result =  userRepository.findAllAddress(user.getId());
        if(result==null)
            throw new AddressNotFoundException("Not Address Found: "+user.getEmail());
        return new ResponseEntity<>(new MessageResponseEntity<>(result, HttpStatus.OK), HttpStatus.OK);
    }

    //Change the LoggedIn Customer's Password And Send Mail Upon Change
    public ResponseEntity<Object> customerUpdatePassword(String email, Map<Object,Object> fields) throws MessagingException {
        User user=userRepository.findByEmailIgnoreCase(email);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findRequiredField(User.class, (String) k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, user, v);
        });

        UserCO userCO = converterService.convertToUserCO(user);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserCO>> violations = validator.validate(userCO);

        final List<String> errors = new ArrayList<>();
        for (ConstraintViolation<UserCO> violation : violations) {
            errors.add(violation.getMessage());
        }

        if (!errors.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(errors, HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);

        if(user.getPassword().equals(user.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUpdatedBy("user@"+user.getFirstName());
            user.setLastUpdated(new Date());
            userRepository.save(user);

            String subject = messageSource.getMessage("successful.password.changed.subject", null, LocaleContextHolder.getLocale());
            String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
            String message = messageSource.getMessage("successful.password.reset.message", null, LocaleContextHolder.getLocale());

            smtpMailSender.send(user.getEmail(), subject, dear+" "+user.getFirstName()+" ,"+message);
        }
        else {
            throw new PasswordDoesNotMatchException("Password and Confirm Password does not match!");
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Password Changed Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
    }


    //Add the New Address to the LoggedIn Customer
    public ResponseEntity<MessageResponseEntity<AddressDTO>> customerAddAddress(String email, AddressCO addressCO) {
        Address address = converterService.convertToAddress(addressCO);
        User user = userRepository.findByEmailIgnoreCase(email);
        address.setUser(user);
        addressRepository.save(address);
        AddressDTO addressDTO = converterService.convertToAddressDto(address);
        return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //Delete the already existing Address of the LoggedIn Customer
    @Transactional
    public ResponseEntity<MessageResponseEntity<String>> customerDeleteAddress(String email, Long id) {
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
    public ResponseEntity<MessageResponseEntity<AddressDTO>> customerUpdateAddress(String email, Map<Object,Object> fields, Long id) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent())
        {
            Address address = optionalAddress.get();
            if (address.getUser().getId().equals(user.getId())) {

                fields.forEach((k, v) -> {
                    Field field = ReflectionUtils.findRequiredField(Address.class, (String)k);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, address, v);
                });

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
    public ResponseEntity<Object> customerUpdateProfile(String email,Map<Object,Object> fields) {
        Customer customer= (Customer) userRepository.findByEmailIgnoreCase(email);

        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findRequiredField(Customer.class, (String)k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, customer, v);
        });
        CustomerUpdateProfileCO customerCO = converterService.convertToCustomerUpdateProfileCO(customer);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<CustomerUpdateProfileCO>> violations = validator.validate(customerCO);

        final List<String> errors = new ArrayList<>();
        for (ConstraintViolation<CustomerUpdateProfileCO> violation : violations) {
            errors.add(violation.getMessage());
        }

        if (!errors.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(errors, HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
        customerRepository.save(customer);
        CustomerDTO customerDTO = converterService.convertToCustomerDto(customer);
        return new ResponseEntity<>(new MessageResponseEntity<>(customerDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }


//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------

    //Upload Profile Image
    public ResponseEntity<MessageResponseEntity<String>> uploadProfileImage(MultipartFile multipartFile, String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        try {
            String imageUri = imageUploaderService.uploadImage(multipartFile, email);
            user.setProfileImage(imageUri);
            userRepository.save(user);
            return new ResponseEntity<>(new MessageResponseEntity<>(imageUri, HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseEntity<>("Image Upload Failed.Try Again!", HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
        }
    }

    //Get the Profile Image
    public ResponseEntity<MessageResponseEntity<String>> getProfileImage(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user.getProfileImage() != null) {
            return new ResponseEntity<>(new MessageResponseEntity<>(user.getProfileImage(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Try Again!", HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
    }

//-------------------------------------------CUSTOMER CATEGORY API'S-----------------------------------------------------

    //List All Category
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllCustomerCategories(Long id) {
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
    public ResponseEntity<MessageResponseEntity<List<Object>>> getFilterDetails(Long id) {

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
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(Long id) {
        List<Map<Object, Object>> product = productRepository.listOneProductCustomer(id);
        return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
    }

    //Customer Function to view all products
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProducts(Integer pageNo, Integer pageSize, String sortBy, Long id) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Map<Object,Object>> pagedResult = productRepository.listAllProductCustomer(paging,id);
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult, HttpStatus.OK), HttpStatus.OK);
    }


    //Get the Product variation Image
    public ResponseEntity<MessageResponseEntity<String>> getProductVariationImage(Long productVariationId) {

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(productVariationId);
        if (optionalProductVariation.isPresent()) {
            ProductVariation productVariation = optionalProductVariation.get();
            if (productVariation.getPrimaryImageName() != null)
                return new ResponseEntity<>(new MessageResponseEntity<>(productVariation.getPrimaryImageName(), HttpStatus.OK), HttpStatus.OK);
            else
                throw new ProductNotFoundException("The Product Does Not Exist: " + productVariationId);
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Try again!",HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
    }
}
