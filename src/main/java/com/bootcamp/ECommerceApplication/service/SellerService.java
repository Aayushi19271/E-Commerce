package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.*;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.*;
import com.bootcamp.ECommerceApplication.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
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
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
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
    public ResponseEntity<MessageResponseEntity<SellerDTO>> sellerProfile(String email)
    {
        Seller seller = (Seller) userRepository.findByEmailIgnoreCase(email);
        SellerDTO sellerDTO = converterService.convertToSellerDto(seller);
        return new ResponseEntity<>(new MessageResponseEntity<>(sellerDTO, HttpStatus.OK), HttpStatus.OK);
    }

    //Get the LoggedIn Seller's Address Details
    public ResponseEntity<MessageResponseEntity<AddressDTO>> sellerAddress(String email) {
        Seller seller = (Seller) userRepository.findByEmailIgnoreCase(email);
        Long sellerId = seller.getId();
        Address address = addressRepository.ListByAddressID(sellerId);
        AddressDTO addressDTO = converterService.convertToAddressDto(address);
        return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.OK), HttpStatus.OK);
    }

    //Update the Profile of LoggedIn Seller
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdateProfile(String email, Map<Object,Object> fields)
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
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdatePassword(String email,Map<Object,Object> fields) throws MessagingException {
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
    public ResponseEntity<MessageResponseEntity<Object>> sellerUpdateAddress(String email, AddressCO addressCO) {
        Seller seller= (Seller) userRepository.findByEmailIgnoreCase(email);
        List<Address> addresses = seller.getAddresses();
        Address address = addresses.get(0);

        Optional<Address> optionalAddress = addressRepository.findById(address.getId());

        if(optionalAddress.isPresent()) {
            Address savedAddress = optionalAddress.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(addressCO, savedAddress);
            AddressDTO addressDTO = converterService.convertToAddressDto(address);
            return new ResponseEntity<>(new MessageResponseEntity<>(addressDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new AddressNotFoundException("Address not found: " + address.getId());
    }

//---------------------------------------CUSTOMER PROFILE IMAGE API'S---------------------------------------------------

    //Upload Profile Image
    public ResponseEntity<MessageResponseEntity<Object>> uploadProfileImage(MultipartFile multipartFile, String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        try {
            String imageUri = imageUploaderService.uploadImage(multipartFile, email);
            user.setProfileImage(imageUri);
            userRepository.save(user);
            return new ResponseEntity<>(new MessageResponseEntity<>(imageUri, HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseEntity<>("Try again!",HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
        }
    }


    //Get the Profile Image
    public ResponseEntity<MessageResponseEntity<Serializable>> getProfileImage(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user.getProfileImage() != null) {
            return new ResponseEntity<>(new MessageResponseEntity<>(user.getProfileImage(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Try again!",HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
    }

//-------------------------------------------SELLER CATEGORY API'S-------------------------------------------------------

    //List All Category
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllCategory() {
        List<Map<Object,Object>> pagedResult = categoryRepository.findAllLeafCategories();
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult, HttpStatus.OK), HttpStatus.OK);
    }

//-------------------------------------------SELLER PRODUCT API'S-------------------------------------------------------

    //Add A Product
    public ResponseEntity<MessageResponseEntity<ProductDTO>> addProduct(ProductCO productCO, String email) throws MessagingException {
        Long categoryId = productCO.getCategory();
        String brand = productCO.getBrand();
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category category;
        if (optionalCategory.isPresent())
            category = optionalCategory.get();
        else
            throw new CategoryNotFoundException("The Category does not Exist." + productCO.getCategory());

        Boolean leafNodeCategory = categoryRepository.findByCategoryLeafNode(categoryId);
        Product savedProduct = productRepository.findOneProductByCombination(brand, categoryId, seller.getId());
        if (leafNodeCategory) {
            if (savedProduct == null) {
                Product product = new Product();
                ModelMapper mapper = new ModelMapper();
                mapper.map(productCO, product);
                product.setCategory(category);
                product.setSeller(seller);
                product.setCancellable(productCO.isCancellable());
                product.setReturnable(productCO.isReturnable());
                productRepository.save(product);

                String subject = messageSource.getMessage("product.added.subject", null, LocaleContextHolder.getLocale());
                String message = messageSource.getMessage("product.added.message", null, LocaleContextHolder.getLocale());
                ProductDTO productDTO = converterService.convertToProductDTO(product);
                smtpMailSender.send("aayushithani@yahoo.in", subject, message + " " + product.getName());
                return new ResponseEntity<>(new MessageResponseEntity<>(productDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            } else
                throw new ProductFoundException("Product already Exist Exception: " + productCO.getName());
        }
        throw new CategoryNotLeafNodeException("Category Not a Leaf Node Exception: " + productCO.getCategory());
    }

    //Get Details Of One Product
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent())
            throw new ProductNotFoundException("The Product Is Deleted: "+id);

        List<Map<Object,Object>> savedProduct = productRepository.listOneProduct(seller.getId(), id);
        if(savedProduct.isEmpty())
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);

        return new ResponseEntity<>(new MessageResponseEntity<>(savedProduct, HttpStatus.OK), HttpStatus.OK);
    }

    //Get Details Of All Products
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProduct(String email,
                                                                                           Integer pageNo,
                                                                                           Integer pageSize,
                                                                                           String sortBy) {

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productRepository.listAllProduct(seller.getId(),paging);
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            throw new ProductNotFoundException("The Product Does Not Exist");
    }

    //Delete One Product
    public ResponseEntity<MessageResponseEntity<String>> deleteOneProduct(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Long productId = productRepository.findOneProduct(seller.getId(), id);
        if (productId==null)
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product savedProduct = optionalProduct.get();
            savedProduct.setDeleted(true);
            productRepository.save(savedProduct);
            return new ResponseEntity<>(new MessageResponseEntity<>("Product Successfully Deleted!", HttpStatus.OK), HttpStatus.OK);
        }
        else {
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);
        }
    }

    //Update One Product
    public ResponseEntity<MessageResponseEntity<ProductDTO>> updateOneProduct(String email, Long id, ProductUpdateCO productUpdateCO) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Optional<Product> optionalProduct = productRepository.findById(id);
        List<Map<Object,Object>> optionalSellerProduct = productRepository.listOneProduct(seller.getId(), id);

        if(optionalProduct.isPresent()) {
            if (optionalSellerProduct.isEmpty())
                throw new ProductNotFoundException("The Product Does Not Exist: "+id);
        }
        else
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);

        Product product = optionalProduct.get();
        ModelMapper mapper = new ModelMapper();
        mapper.map(productUpdateCO,product);
        productRepository.save(product);
        ProductDTO productDTO = converterService.convertToProductDTO(product);
        return new ResponseEntity<>(new MessageResponseEntity<>(productDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }


    //Fetch Details of One Product Variation
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProductVariation(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productVariationRepository.findOneProductVariationById(seller.getId(), id);
        if(product.isEmpty())
            throw new ProductNotFoundException("The Product Does Not Exist: "+id);

        return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
    }



    //List Details All Product Variation
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>>
    listAllProductVariation(String email, Integer pageNo, Integer pageSize, String sortBy, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Map<Object, Object>> pagedResult = productVariationRepository.findAll(paging,seller.getId(),id);
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult, HttpStatus.OK), HttpStatus.OK);
    }


    //Add a product Variation
    public ResponseEntity<MessageResponseEntity<ProductVariationDTO>> addProductVariation(
            ProductVariationCO productVariationCO) throws JsonProcessingException {

        Long productId = productVariationCO.getProductId();
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (!optionalProduct.isPresent())
            throw new ProductNotFoundException("The Product Does Not Exist: "+productId);


        ProductVariation productVariation = new ProductVariation();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(productVariationCO, productVariation);
        productVariation.jsonMetadataStringSerialize();
        productVariationRepository.save(productVariation);
        ProductVariationDTO productVariationDTO = converterService.convertToProductVariationDTO(productVariation);
        return new ResponseEntity<>(new MessageResponseEntity<>(productVariationDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //Upload Product Variation Image
    public ResponseEntity<MessageResponseEntity<String>> uploadProductVariationImage(MultipartFile multipartFile, String email, Long productVariationId) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Long productVariationRepositoryId = productVariationRepository.findAllBySellerId(user.getId(), productVariationId);
        if(productVariationRepositoryId==null)
            throw new ProductNotFoundException("The Product Does Not Exist: "+productVariationId);

        try {
            String imageUri = imageUploaderService.uploadImage(multipartFile, email);
            Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(productVariationRepositoryId);
            if (optionalProductVariation.isPresent()) {
                ProductVariation productVariation = optionalProductVariation.get();
                productVariation.setPrimaryImageName(imageUri);
                productVariationRepository.save(productVariation);
                return new ResponseEntity<>(new MessageResponseEntity<>(imageUri, HttpStatus.CREATED), HttpStatus.CREATED);
            }
            else
                throw new ProductNotFoundException("The Product Does Not Exist: "+productVariationId);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseEntity<>("Image Upload Failed.Try Again!", HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
        }
    }

    //Get the Product Variation Image
    public ResponseEntity<MessageResponseEntity<String>> getProductVariationImage(String email, Long productVariationId) {

        User user = userRepository.findByEmailIgnoreCase(email);
        Long productVariationRepositoryId = productVariationRepository.findAllBySellerId(user.getId(), productVariationId);
        if(productVariationRepositoryId==null)
            throw new ProductNotFoundException("The Product Does Not Exist: "+productVariationId);

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(productVariationRepositoryId);
        if (optionalProductVariation.isPresent()) {
            ProductVariation productVariation = optionalProductVariation.get();
            if (productVariation.getPrimaryImageName() != null)
                return new ResponseEntity<>(new MessageResponseEntity<>(productVariation.getPrimaryImageName(), HttpStatus.OK), HttpStatus.OK);
            else
                throw new ProductNotFoundException("The Product Does Not Exist: " + productVariationId);
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Try again!",HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);
    }

    //Update the product Variation
    public ResponseEntity<Object> updateProductVariation(String email, Map<Object, Object> fields, Long productVariationId) {
        User user = userRepository.findByEmailIgnoreCase(email);
        Long productVariationRepositoryId = productVariationRepository.findAllBySellerId(user.getId(), productVariationId);
        if(productVariationRepositoryId==null)
            throw new ProductNotFoundException("The Product Does Not Exist: "+productVariationId);

        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(productVariationRepositoryId);
        if (optionalProductVariation.isPresent()) {
            ProductVariation productVariation = optionalProductVariation.get();

            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findRequiredField(ProductVariation.class, (String)k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, productVariation, v);
            });
            ProductVariationUpdateCO productVariationCO = converterService.convertToProductVariationCO(productVariation);

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<ProductVariationUpdateCO>> violations = validator.validate(productVariationCO);

            final List<String> errors = new ArrayList<>();
            for (ConstraintViolation<ProductVariationUpdateCO> violation : violations) {
                errors.add(violation.getMessage());
            }

            if (!errors.isEmpty())
                return new ResponseEntity<>(new MessageResponseEntity<>(errors, HttpStatus.BAD_REQUEST,null), HttpStatus.BAD_REQUEST);
            productVariationRepository.save(productVariation);
            ProductVariationDTO productVariationDTO = converterService.convertToProductVariationDTO(productVariation);
            return new ResponseEntity<>(new MessageResponseEntity<>(productVariationDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        throw new ProductNotFoundException("The Product Does Not Exist: " + productVariationId);
    }


}
