package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.co.ProductCO;
import com.bootcamp.ECommerceApplication.co.ProductUpdateCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.AddressNotFoundException;
import com.bootcamp.ECommerceApplication.exception.PasswordDoesNotMatchException;
import com.bootcamp.ECommerceApplication.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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


import javax.mail.MessagingException;
import javax.transaction.Transactional;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;

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
                return new ResponseEntity<>("The Category ID Does Not exist!", HttpStatus.BAD_REQUEST);

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
                    smtpMailSender.send("aayushithani@yahoo.in", "Product Added!",
                            "Dear Admin, New Product Has Been Added,Please Active the product! Added Product Name Is:"+product.getName());
                    return new ResponseEntity<>(new MessageResponseEntity<>("Product Successfully Added!", HttpStatus.CREATED), HttpStatus.CREATED);
                }
                else
                    return new ResponseEntity<>("The Product Already Exist!", HttpStatus.BAD_REQUEST);
            }
        return new ResponseEntity<>("The Category is not a leaf node!", HttpStatus.BAD_REQUEST);
    }

    //Fetch Details Of One Product
    public ResponseEntity<Object> listOneProduct(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productRepository.listOneProduct(seller.getId(), id);
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            return new ResponseEntity<>("The Product Does Not Exist!", HttpStatus.BAD_REQUEST);
    }

    //Fetch Details Of All Products
    public ResponseEntity<Object> listAllProduct(String email) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productRepository.listAllProduct(seller.getId());
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            return new ResponseEntity<>("The Product Does Not Exist!", HttpStatus.BAD_REQUEST);
    }

    //Fetch Details of One Product Variation
    public ResponseEntity<Object> listOneProductVariation(String email, Long id) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        List<Map<Object, Object>> product = productVariationRepository.findOneProductVariationById(seller.getId(), id);
        if(!product.isEmpty())
            return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
        else
            return new ResponseEntity<>("The Product Does Not Exist!", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("The Product Does Not Exist!", HttpStatus.BAD_REQUEST);
        }
    }

    //Update One Product
    public ResponseEntity<Object> updateOneProduct(String email, Long id, ProductUpdateCO productUpdateCO) {
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        Optional<Product> optionalProduct = productRepository.findById(id);
        List<Map<Object, Object>> optionalSellerProduct = productRepository.listOneProduct(seller.getId(), id);

        if(!optionalProduct.isPresent()) {      //product ID exist or not
            if (optionalSellerProduct.isEmpty())        //productID belong to the logged in seller or not
                return new ResponseEntity<>("The Product Does Not Exist!", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("The Product Does Not Exist!", HttpStatus.BAD_REQUEST);
        }
        Product product = optionalProduct.get();
        ModelMapper mapper = new ModelMapper();
        mapper.map(productUpdateCO,product);
        productRepository.save(product);
        return new ResponseEntity<>(new MessageResponseEntity<>("Product Successfully Added!", HttpStatus.CREATED), HttpStatus.CREATED);
    }


}
