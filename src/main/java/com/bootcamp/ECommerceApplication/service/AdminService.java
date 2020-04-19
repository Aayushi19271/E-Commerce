package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.CategoryCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldCO;
import com.bootcamp.ECommerceApplication.co.CategoryMetadataFieldValuesCO;
import com.bootcamp.ECommerceApplication.co.CategoryUpdateCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.CategoryDTO;
import com.bootcamp.ECommerceApplication.dto.CategoryMetadataFieldDTO;
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
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class AdminService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SmtpMailSender smtpMailSender;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;
    @Autowired
    private MessageSource messageSource;


//------------------------------------------ADMIN CUSTOMER/SELLERS METHODS----------------------------------------------
//------------------------------------------LIST OF USERS,SELLERS,CUSTOMERS---------------------------------------------

    //List Of All Users
    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    //LIST OF CUSTOMERS - PAGING (0,10) AND SORTING ASC "ID"
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> findAllCustomers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Map<Object, Object>> pagedResult = customerRepository.findAllCustomers(paging);
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult, HttpStatus.OK), HttpStatus.OK);
    }

    //LIST OF SELLERS - PAGING (0,10) AND SORTING ASC "ID"
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> findAllSellers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Map<Object, Object>> pagedResult = sellerRepository.findAllSellers(paging);
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult, HttpStatus.OK), HttpStatus.OK);

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
    public ResponseEntity<MessageResponseEntity<String>> activateAccount(Long id, Map<Object,Object> fields) throws MessagingException {
        User user = getUser(id);
        boolean flag = user.isActive();

        if (!flag) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findRequiredField(User.class, (String) k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, v);
            });
            String updatedBy = "admin@aayushi";
            user.setUpdatedBy(updatedBy);
            user.setLastUpdated(new Date());
            updateUser(user);
            try {
                String subject = messageSource.getMessage("account.activated.subject", null, LocaleContextHolder.getLocale());
                String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
                String message = messageSource.getMessage("account.activated.message", null, LocaleContextHolder.getLocale());

                smtpMailSender.send(user.getEmail(), subject, dear+" "+user.getFirstName() + ", "+message);
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
    public ResponseEntity<MessageResponseEntity<String>> deactivateAccount(Long id, Map<Object,Object> fields) throws MessagingException {
        User user = getUser(id);
        boolean flag = user.isActive();
        if (flag) {
            fields.forEach((k, v) -> {
                Field field = ReflectionUtils.findRequiredField(User.class, (String) k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, v);
            });
            String updatedBy = "admin@aayushi";
            user.setUpdatedBy(updatedBy);
            user.setLastUpdated(new Date());
            updateUser(user);
            try {
                String subject = messageSource.getMessage("account.deactivated.subject", null, LocaleContextHolder.getLocale());
                String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
                String message = messageSource.getMessage("account.deactivated.message", null, LocaleContextHolder.getLocale());

                smtpMailSender.send(user.getEmail(), subject, dear+" "+user.getFirstName()+", "+message);
                return new ResponseEntity<>(new MessageResponseEntity<>("Account De-activated Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
            }catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+user.getEmail());
            }
        }
        else
            throw new UserDeactiveException("User is already De-activated: "+user.getEmail());
    }
//-------------------------------------------ADMIN CATEGORY API'S-------------------------------------------------------


    //Admin Function to add a Metadata field
    public ResponseEntity<MessageResponseEntity<CategoryMetadataFieldDTO>> addMetadataField(CategoryMetadataFieldCO categoryMetadataFieldCO) {
        CategoryMetadataField categoryMetadataField = converterService.convertToCategoryMetadataField(categoryMetadataFieldCO);
        String name = categoryMetadataField.getName();

        CategoryMetadataField savedCategoryMetadataField = categoryMetadataFieldRepository.findByNameIgnoreCase(name);
        if (savedCategoryMetadataField==null){
            categoryMetadataFieldRepository.save(categoryMetadataField);
            CategoryMetadataFieldDTO categoryMetadataFieldDTO = converterService.convertToCategoryMetadataFieldDTO(categoryMetadataField);
            return new ResponseEntity<>(new MessageResponseEntity<>(categoryMetadataFieldDTO, HttpStatus.CREATED), HttpStatus.CREATED);
        }
        else {
            throw new MetadataFieldFoundException("The Metadata Field Already Exists: "+categoryMetadataField.getName());
        }
    }


    //Admin Function to view all Metadata fields
    public ResponseEntity<MessageResponseEntity<List<CategoryMetadataField>>> listAllMetadata(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<CategoryMetadataField> metadataFieldsList = categoryMetadataFieldRepository.findAll(paging);
        return new ResponseEntity<>(new MessageResponseEntity<>(metadataFieldsList.getContent(), HttpStatus.OK), HttpStatus.OK);
    }


    //Admin Function to Add New Category
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> addCategory(CategoryCO categoryCO) {
        Category savedCategory = categoryRepository.findByNameAndParent(categoryCO.getName(), categoryCO.getParentId());
        if (savedCategory != null)
            throw new CategoryFoundException("Category Already Exists: "+categoryCO.getName());


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
                parentCategory.setLeafNode(false);
                Category category = new Category();
                category.setLeafNode(true);
                category.setName(categoryCO.getName());
                category.setParent(parentCategory);
                categoryRepository.save(category);
                categoryRepository.save(parentCategory);
                CategoryDTO categoryDTO = converterService.convertToCategoryDTO(category);
                return new ResponseEntity<>(new MessageResponseEntity<>(categoryDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            }
            else
                throw new ParentCategoryNotFoundException("Parent Category Not Found: " +categoryCO.getParentId());
        }
    }

    //Admin Function to list All Category
    public ResponseEntity<MessageResponseEntity<List<Category>>> listAllCategory(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Category> categoryList = categoryRepository.findAllCategory(paging);
        return new ResponseEntity<>(new MessageResponseEntity<>(categoryList, HttpStatus.OK), HttpStatus.OK);
    }


   //Admin Function to List One Category
    public ResponseEntity<MessageResponseEntity<Map<String, Object>>> listOneCategory(Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            List<Object> subCategory = categoryRepository.findAllChildrenAdmin(id);

            response.put("SUB-CATEGORY", subCategory);
            response.put("ROOT-CATEGORY", category);

            return new ResponseEntity<>(new MessageResponseEntity<>(response, HttpStatus.OK), HttpStatus.OK);
        }
        throw new CategoryNotFoundException("Category Not Found:"+id);
    }

    //Admin Function to Update One Category
    public ResponseEntity<MessageResponseEntity<CategoryDTO>> updateCategory(CategoryUpdateCO categoryUpdateCO) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryUpdateCO.getId());
        if (optionalCategory.isPresent()) {
            Category savedCategory = optionalCategory.get();
            Category oldCategory = categoryRepository.findByNameAndParent(categoryUpdateCO.getName(), savedCategory.getParent().getId());

            if (oldCategory != null)
                throw new CategoryFoundException("Category with similar name already exists".toUpperCase());

                ModelMapper mapper = new ModelMapper();
                mapper.map(categoryUpdateCO, savedCategory);
                categoryRepository.save(savedCategory);
                CategoryDTO categoryDTO = converterService.convertToCategoryDTO(savedCategory);
                return new ResponseEntity<>(new MessageResponseEntity<>(categoryDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            }
            else
                throw new CategoryNotFoundException("Category Not Found:"+categoryUpdateCO.getId());
        }



    //Admin Function to Add Metadata Field Values
    public ResponseEntity<MessageResponseEntity<String>> addMetadataFieldValues(CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO) {
        Long categoryId = categoryMetadataFieldValuesCO.getCategoryId();
        Long categoryMetadataFieldId = categoryMetadataFieldValuesCO.getCategoryMetadataFieldId();
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> optionalCategoryMetadataField = categoryMetadataFieldRepository.findById(categoryMetadataFieldId);

        if (!optionalCategory.isPresent())
            throw new CategoryNotFoundException("Category Not Found:"+categoryId);

        if (!optionalCategoryMetadataField.isPresent())
            throw new CategoryMetadataFieldNotFoundException("Category Metadata Field Id Not Found: "+categoryMetadataFieldId);

        CategoryMetadataFieldValuesID categoryMetadataFieldValuesId =
                new CategoryMetadataFieldValuesID(optionalCategoryMetadataField.get().getId(), optionalCategory.get().getId());

        Optional<CategoryMetadataFieldValues> optionalCategoryMetadataFieldValues =
                categoryMetadataFieldValuesRepository.findById(categoryMetadataFieldValuesId);

        if (optionalCategoryMetadataFieldValues.isPresent()) {
            throw new CategoryMetadataFieldValuesFoundException("Category meta data already exists:"+categoryMetadataFieldValuesId);
        }
        else{
            Category category = optionalCategory.get();
            CategoryMetadataField categoryMetadataField = optionalCategoryMetadataField.get();

            CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
            categoryMetadataFieldValues.setCategory(category);
            categoryMetadataFieldValues.setCategoryMetadataField(categoryMetadataField);
            categoryMetadataFieldValues.setValue(categoryMetadataFieldValuesCO.getValue());
            categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
            return new ResponseEntity<>(new MessageResponseEntity<>("Category Metadata Field Values Successfully Added!", HttpStatus.CREATED), HttpStatus.CREATED);
        }
    }


    //Admin Function to Update Metadata Field Values
    public ResponseEntity<MessageResponseEntity<CategoryMetadataFieldValuesCO>> updateMetadataFieldValues(CategoryMetadataFieldValuesCO categoryMetadataFieldValuesCO) {
        Long categoryId = categoryMetadataFieldValuesCO.getCategoryId();
        Long categoryMetadataFieldId = categoryMetadataFieldValuesCO.getCategoryMetadataFieldId();

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> optionalCategoryMetadataField = categoryMetadataFieldRepository.findById(categoryMetadataFieldId);

        if (!optionalCategory.isPresent())
            throw new CategoryNotFoundException("Category Not Found:"+categoryId);
        if (!optionalCategoryMetadataField.isPresent())
            throw new CategoryMetadataFieldNotFoundException("Category Metadata Field Id Not Found: "+categoryMetadataFieldId);

        CategoryMetadataFieldValuesID categoryMetadataFieldValuesId = new CategoryMetadataFieldValuesID(optionalCategoryMetadataField.get().getId(), optionalCategory.get().getId());

        Optional<CategoryMetadataFieldValues> optionalCategoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findById(categoryMetadataFieldValuesId);

        CategoryMetadataFieldValues categoryMetadataFieldValues;
        if (optionalCategoryMetadataFieldValues.isPresent())
            categoryMetadataFieldValues = optionalCategoryMetadataFieldValues.get();
        else
            throw new CategoryMetadataFieldNotFoundException("Category Metadata Field Id Not Found: "+categoryMetadataFieldValuesId);


        if (categoryMetadataFieldValues.getValue()
                .contains(categoryMetadataFieldValuesCO.getValue())) {
            throw new CategoryMetadataFieldValuesFoundException("Category meta data already exists:"+categoryMetadataFieldValuesId);
        }

        String newValue = categoryMetadataFieldValues.getValue()
                .concat("," + categoryMetadataFieldValuesCO.getValue());
        categoryMetadataFieldValues.setValue(newValue);
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);

        categoryMetadataFieldValuesCO.setValue(newValue);
        return new ResponseEntity<>(new MessageResponseEntity<>(categoryMetadataFieldValuesCO, HttpStatus.OK, "Metadata Field Values Updated"), HttpStatus.OK);

    }


//-------------------------------------------ADMIN PRODUCT API'S--------------------------------------------------------

    //Admin Function to view a product
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listOneProduct(Long id) {
         List<Map<Object, Object>> product = productRepository.listOneProductAdmin(id);
         if (product.isEmpty())
             throw new ProductNotFoundException("Product Not Found: "+id);
        return new ResponseEntity<>(new MessageResponseEntity<>(product, HttpStatus.OK), HttpStatus.OK);
    }

    //Admin Function to list All products
    public ResponseEntity<MessageResponseEntity<List<Map<Object, Object>>>> listAllProducts(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Map<Object,Object>> pagedResult = productRepository.listAllProductAdmin(paging);
        if (pagedResult.isEmpty())
            throw new ProductNotFoundException("Product Not Found");
        return new ResponseEntity<>(new MessageResponseEntity<>(pagedResult, HttpStatus.OK), HttpStatus.OK);
    }

    //Admin Function to Activate A Product
    public ResponseEntity<MessageResponseEntity<String>> activateProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent())
            throw new ProductNotFoundException("Product Not Found Exception:"+id);

        Product product = optionalProduct.get();
        Seller seller = product.getSeller();
        boolean flag = product.getActive();
        if (!flag) {
            product.setActive(true);
            productRepository.save(product);
            try {
                String subject = messageSource.getMessage("product.activated.subject", null, LocaleContextHolder.getLocale());
                String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
                String message = messageSource.getMessage("product.activated.message", null, LocaleContextHolder.getLocale());

                smtpMailSender.send(seller.getEmail(), subject, dear+" " + seller.getFirstName() + ", "+message);
                return new ResponseEntity<>(new MessageResponseEntity<>("Product Activated Successfully!", HttpStatus.OK), HttpStatus.OK);
            }
            catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+seller.getEmail());
            }
        }
        else
            throw new ProductActiveException("Product Already Activated:"+id);
    }


    //Admin Function to De-activate A Product
    public ResponseEntity<MessageResponseEntity<String>> deactivateProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent())
            throw new ProductNotFoundException("Product Not Found Exception:"+id);

        Product product = optionalProduct.get();
        Seller seller = product.getSeller();
        boolean flag = product.getActive();
        if (flag) {
            product.setActive(false);
            productRepository.save(product);
            try {
                String subject = messageSource.getMessage("product.deactivated.subject", null, LocaleContextHolder.getLocale());
                String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
                String message = messageSource.getMessage("product.deactivated.message", null, LocaleContextHolder.getLocale());

                smtpMailSender.send(seller.getEmail(), subject, dear+" " + seller.getFirstName() + ", "+message);
                return new ResponseEntity<>(new MessageResponseEntity<>("Product Deactivated Successfully!", HttpStatus.OK), HttpStatus.OK);
            }
            catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+seller.getEmail());
            }
        }
        else
            throw new ProductDeactiveException(" Product Already De-activated: "+id);
    }

    //Get the Product variation Image
    public ResponseEntity<Object> getProductVariationImage(Long productVariationId) {

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
