package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.*;
import com.bootcamp.ECommerceApplication.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private ImageUploaderService imageUploaderService;
    @Autowired
    private MessageSource messageSource;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


//-------------------------------------------SELLER ACCOUNT API'S-------------------------------------------------------

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
        SellerUpdateProfileCO sellerCO = converterService.convertToSellerProfileUpdateCO(seller);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SellerUpdateProfileCO>> violations = validator.validate(sellerCO);

        final List<String> errors = new ArrayList<>();
        for (ConstraintViolation<SellerUpdateProfileCO> violation : violations) {
            errors.add(violation.getMessage());
        }

        if (!errors.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(errors, HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);

        sellerRepository.save(seller);
        SellerDTO sellerDTO = converterService.convertToSellerDto(seller);
        return new ResponseEntity<>(new MessageResponseEntity<>(sellerDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }


    //Update the LoggedIn Seller's Password And Send Mail Upon Change
    public ResponseEntity<Object> sellerUpdatePassword(String email,Map<Object,Object> fields) throws MessagingException {
        User user=  userRepository.findByEmailIgnoreCase(email);
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


    //Update the already existing Address of LoggedIn Seller
    public ResponseEntity<Object> sellerUpdateAddress(String email, Map<Object,Object> fields) {
        Seller seller= (Seller) userRepository.findByEmailIgnoreCase(email);
        List<Address> addresses = seller.getAddresses();
        Address address = addresses.get(0);

        Optional<Address> optionalAddress = addressRepository.findById(address.getId());

        if(optionalAddress.isPresent()) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findRequiredField(Address.class, (String) k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, address, v);
            });

            addressRepository.save(address);
            AddressDTO addressDTO = converterService.convertToAddressDto(address);
            return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new AddressNotFoundException("Address not found: " + address.getId());
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

//-------------------------------------------SELLER CATEGORY API'S-------------------------------------------------------

    //List All Category
    public ResponseEntity<Object> listAllCategory(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Map<Category, Object> response = new HashMap<>();

        List<Category> categoryList = categoryRepository.findAllCategory(paging);
        categoryList.forEach(category -> {
            List<Map<Object, Object>> metadataValueList = categoryMetadataFieldValuesRepository.findByCategoryId(category.getId());
            response.put(category, metadataValueList);
        });
        return new ResponseEntity<>(new MessageResponseEntity<>(response, HttpStatus.OK), HttpStatus.OK);
    }

//-------------------------------------------SELLER PRODUCT API'S-------------------------------------------------------

    //Add A Product
    public ResponseEntity<Object> addProduct(ProductCO productCO, String email) throws MessagingException {
            Long categoryId = productCO.getCategory();
            String brand = productCO.getBrand();
            Seller seller = sellerRepository.findByEmailIgnoreCase(email);
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            Category category;
            if(optionalCategory.isPresent())
                category=optionalCategory.get();
            else
                throw new CategoryNotFoundException("The Category does not Exist."+productCO.getCategory());

            Boolean leafNodeCategory = categoryRepository.findByCategoryLeafNode(categoryId);
            Product savedProduct = productRepository.findOneProduct(brand,categoryId,seller.getId());
            if(leafNodeCategory){
                if(savedProduct==null){
                    Product product = new Product();
                    product.setActive(false);
                    product.setBrand(brand);
                    product.setName(productCO.getName());
                    product.setCategory(category);
                    product.setDescription(productCO.getDescription());
                    product.setSeller(seller);
                    productRepository.save(product);

                    String subject = messageSource.getMessage("product.added.subject", null, LocaleContextHolder.getLocale());
                    String message = messageSource.getMessage("product.added.message", null, LocaleContextHolder.getLocale());

                    smtpMailSender.send("aayushithani@yahoo.in", subject, message+" "+product.getName());
                    return new ResponseEntity<>(new MessageResponseEntity<>("Product Successfully Added!", HttpStatus.CREATED), HttpStatus.CREATED);
                }
                else
                    throw new ProductFoundException("Product already Exist Exception: "+productCO.getName());
            }
            throw new CategoryNotLeafNodeException("Category Not a Leaf Node Exception: "+productCO.getCategory());
    }

    //Fetch Details Of One Product
    public ResponseEntity<Object> listOneProduct(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productRepository.listOneProduct(seller.getId(), id);
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);
    }

    //Fetch Details Of All Products
    public ResponseEntity<Object> listAllProduct(String email) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productRepository.listAllProduct(seller.getId());
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            throw new ProductNotFoundException("The Product Does Not Exist");
    }

    //Fetch Details of One Product Variation
    public ResponseEntity<Object> listOneProductVariation(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productVariationRepository.findOneProductVariationById(seller.getId(), id);
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);
    }

    //List Details All Product Variation
    public List<ProductVariation> listAllProductVariation(String email, Integer pageNo, Integer pageSize, String sortBy) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<ProductVariation> pagedResult = productVariationRepository.findAll(paging,seller.getId());
        return pagedResult.getContent();
    }


    //Delete One Product
    @Transactional
    public ResponseEntity<Object> deleteOneProduct(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productRepository.listOneProduct(seller.getId(), id);
        if(!product.isEmpty()){
            productRepository.deleteByProductID(id);
            return new ResponseEntity<>(new MessageResponseEntity<>("Product Successfully Deleted!", HttpStatus.OK), HttpStatus.OK);
        }
        else {
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);
        }
    }

    //Update One Product
    public ResponseEntity<Object> updateOneProduct(String email, Long id, ProductUpdateCO productUpdateCO) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Optional<Product> optionalProduct = productRepository.findById(id);
        List<Map<Object, Object>> optionalSellerProduct = productRepository.listOneProduct(seller.getId(), id);

        if(!optionalProduct.isPresent()) {
            if (optionalSellerProduct.isEmpty())
                throw new ProductNotFoundException("The Product Does Not Exist: "+id);
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);
        }
        Product product = optionalProduct.get();
        ModelMapper mapper = new ModelMapper();
        mapper.map(productUpdateCO,product);
        productRepository.save(product);
        return new ResponseEntity<>(new MessageResponseEntity<>("Product Successfully Added!", HttpStatus.CREATED), HttpStatus.CREATED);
    }
}
