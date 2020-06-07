package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.SellerCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.co.UserCO;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.UserDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.*;
import com.bootcamp.ECommerceApplication.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SmtpMailSender smtpMailSender;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ConverterService converterService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductVariationRepository productVariationRepository;
    @Autowired
    private SellerRepository sellerRepository;


    //------------------------------------------------FIND CUSTOMER METHOD--------------------------------------------------
    //FIND CUSTOMER BY EMAIL ID
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email);
    }


    //-----------------------------------------------CONFIRMATION TOKEN METHOD'S--------------------------------------------
    //FIND ONE TOKEN
    public ConfirmationToken findConfirmationToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }


    //DELETE THE EXISTING TOKEN
    @Transactional
    public void deleteConfirmationToken(String email) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email);
        ConfirmationToken token = customer.getConfirmationToken();
        confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
    }


    //CREATE & RETURN CONFIRMATION TOKEN
    public ConfirmationToken createConfirmationToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationToken.setExpiryDate(new Date());
        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

//------------------------------------------------SELLER REGISTRATION METHOD'S------------------------------------------

    //REGISTER A SELLER - SET THE ACCOUNT AS INACTIVE ACCOUNT, WAIT FOR ADMIN APPROVAL
    public ResponseEntity<MessageResponseEntity<UserDTO>> createSeller(Seller seller) throws MessagingException {
        String customerEmail = seller.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);
        UserDTO userDTO = converterService.convertToSellerDto(seller);

        if (user == null) {
            if (seller.getConfirmPassword().equals(seller.getPassword())) {
                seller.setActive(false);
                seller.setDeleted(false);
                seller.setPassword(passwordEncoder.encode(seller.getPassword()));
                String companyName = seller.getCompanyName().toLowerCase();
                seller.setCompanyName(companyName);
                ArrayList<Role> tempRole = new ArrayList<>();
                Role role = roleRepository.findByAuthority("ROLE_SELLER");
                tempRole.add(role);
                seller.setRoles(tempRole);
                seller.setCreatedBy("user@" + seller.getFirstName());
                seller.setDateCreated(new Date());
                userRepository.save(seller);
                try {
                    String goodMorning = messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
                    String subject = messageSource.getMessage("pending.approval.subject", null, LocaleContextHolder.getLocale());
                    String text = messageSource.getMessage("pending.approval.message", null, LocaleContextHolder.getLocale());
                    String message = goodMorning+" "+seller.getFirstName()+", "+text;

                    smtpMailSender.send(seller.getEmail(), subject, message);
                    return new ResponseEntity<>(new MessageResponseEntity<>(userDTO, HttpStatus.CREATED), HttpStatus.CREATED);
                } catch (Exception ex) {
                    throw new MailSendFailedException("Failed to Send Mail: " + seller.getEmail());
                }
            } else
                throw new PasswordDoesNotMatchException("Password And Confirm Password Does Not Match!");
        }
        else
            throw new UserFoundException("The Seller's EmailID Already Exist: " + seller.getEmail());
    }


//--------------------------------------------------CUSTOMER REGISTRATION METHOD'S--------------------------------------

    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    public ResponseEntity<MessageResponseEntity<UserDTO>> createCustomer(Customer customer) {
        String customerEmail = customer.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);

        if (user == null) {
            if (customer.getConfirmPassword().equals(customer.getPassword())) {
                customer.setDeleted(false);
                customer.setActive(false);
                customer.setPassword(passwordEncoder.encode(customer.getPassword()));
                ArrayList<Role> tempRole = new ArrayList<>();
                Role role = roleRepository.findByAuthority("ROLE_CUSTOMER");
                tempRole.add(role);
                customer.setRoles(tempRole);
                customer.setCreatedBy("user@" + customer.getFirstName());
                customer.setDateCreated(new Date());
                userRepository.save(customer);
                ConfirmationToken confirmationToken = createConfirmationToken(customer);

                try {
                    return sendMailCustomer(customer, confirmationToken);
                } catch (Exception ex) {
                    throw new MailSendFailedException("Failed to Send Mail: " + customer.getEmail());
                }
            } else
                throw new PasswordDoesNotMatchException("Password And Confirm Password Does Not Match!");
        } else
            throw new UserFoundException("The Customer's EmailID Already Exist: " + customer.getEmail());
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @Transactional
    public ResponseEntity<MessageResponseEntity<String>> confirmUserAccountToken(String confirmationToken) throws MessagingException {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-" + confirmationToken);
        return confirmTokenExpiry(confirmationToken);
    }


    //CHECK THE TOKEN EXPIRY (3 CONDITIONS-- token correct, token expires, token wrong)
    @Transactional
    public ResponseEntity<MessageResponseEntity<String>> confirmTokenExpiry(String confirmationToken) throws MessagingException {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();

        //IF THE TOKEN EXPIRES
        if ((token.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            ConfirmationToken newConfirmationToken = new ConfirmationToken(token.getUser());
            newConfirmationToken.setExpiryDate(new Date());
            confirmationTokenRepository.save(newConfirmationToken);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            sendMailCustomer(user, newConfirmationToken);
            throw new TokenNotFoundException("token-" + confirmationToken);
        }
        //IF THE TOKEN IS CORRECT
        else {
            User savedUser = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            savedUser.setActive(true);
            savedUser.setUpdatedBy("user@" + savedUser.getFirstName());
            savedUser.setLastUpdated(new Date());
            userRepository.save(savedUser);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            String subject = messageSource.getMessage("account.activated.subject", null, LocaleContextHolder.getLocale());
            String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
            String message = messageSource.getMessage("account.activated.message", null, LocaleContextHolder.getLocale());
            smtpMailSender.send(user.getEmail(), subject, dear + " " + user.getFirstName() + ", " + message);
            return new ResponseEntity<>(new MessageResponseEntity<>("Account Successfully Activated!", HttpStatus.CREATED), HttpStatus.CREATED);
        }
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @Transactional
    public ResponseEntity<MessageResponseEntity<UserDTO>> reSendActivationLink(UserCO userCO) {
        Customer customer = findCustomerByEmail(userCO.getEmail());
        if (customer == null)
            throw new UserNotFoundException("EmailID:-" + userCO.getEmail());
        if (customer.isActive()) {
            throw new UserActiveException("User Already Active Exception: " + userCO.getFirstName());
        } else {
            deleteConfirmationToken(customer.getEmail());
            ConfirmationToken newConfirmationToken = createConfirmationToken(customer);
            return sendMailCustomer(customer, newConfirmationToken);
        }
    }

    //SEND REGISTRATION MAIL TO CUSTOMER
    public ResponseEntity<MessageResponseEntity<UserDTO>> sendMailCustomer(User user, ConfirmationToken newConfirmationToken) {
        String subject = messageSource.getMessage("complete.registration", null, LocaleContextHolder.getLocale());
        String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("registration.message", null, LocaleContextHolder.getLocale());

        try {
            smtpMailSender.send(user.getEmail(), subject,
                    dear + " " + user.getFirstName() + ", " + message +
                            "http://localhost:3000/users/customers/confirm-account?token=" + newConfirmationToken.getConfirmationToken());
        } catch (Exception ex) {
            throw new MailSendFailedException("Failed to Send Mail: " + user.getEmail());
        }
        UserDTO userDTO = converterService.convertToUserDto(user);
        return new ResponseEntity<>(new MessageResponseEntity<>(userDTO, HttpStatus.CREATED), HttpStatus.CREATED);
    }

//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------

    //FORGOT PASSWORD REQUEST
    public ResponseEntity<MessageResponseEntity<String>> sendPasswordResetLink(UserCO userDto) {
        String email = userDto.getEmail();
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null)
            throw new UserNotFoundException("The User's EmailID Does Not Exist: " + userDto.getEmail());
        boolean active = user.isActive();
        if (active) {
            ConfirmationToken confirmationToken = createConfirmationToken(user);
            return sendResetPasswordMailUsers(user, confirmationToken);
        } else {
            throw new UserDeactiveException("The User is Not An Active User:" + user.getEmail());
        }
    }


    //SEND RESET PASSWORD MAIL TO USERS
    public ResponseEntity<MessageResponseEntity<String>> sendResetPasswordMailUsers(User user, ConfirmationToken newConfirmationToken) {
        String subject = messageSource.getMessage("password.reset.subject", null, LocaleContextHolder.getLocale());
        String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("password.reset.message", null, LocaleContextHolder.getLocale());
        String token = newConfirmationToken.getConfirmationToken();
        try {
            smtpMailSender.send(user.getEmail(), subject, dear + " " + user.getFirstName() + ", " + message +
                    "http://localhost:3000/users/password/reset?token="
                    + token);
        } catch (Exception ex) {
            throw new MailSendFailedException("Failed to Send Mail: " + user.getEmail());
        }
        return new ResponseEntity<>(new MessageResponseEntity<>(token, HttpStatus.CREATED), HttpStatus.CREATED);
    }


    //SEND RESET PASSWORD SUCCESS MAIL TO USER
    public ResponseEntity<Object> sendResetPasswordSuccessMail(User user) {
        String subject = messageSource.getMessage("successful.password.reset.subject", null, LocaleContextHolder.getLocale());
        String dear = messageSource.getMessage("dear", null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage("successful.password.reset.message", null, LocaleContextHolder.getLocale());
        try {
            smtpMailSender.send(user.getEmail(), subject, dear + " " + user.getFirstName() + ", " + message);
        } catch (Exception ex) {
            throw new MailSendFailedException("Failed to Send Mail: " + user.getEmail());
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Password Changed Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //PASSWORD RESET METHOD
    @Transactional
    public ResponseEntity<Object> resetPassword(String confirmationToken, Map<Object, Object> fields) {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-" + confirmationToken);
        Calendar calendar = Calendar.getInstance();
        User user = token.getUser();

        //IF THE TOKEN EXPIRES
        if ((token.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            throw new TokenExpiredException("The Token is Expired! Please Provide a Valid Token.");
        }
        //IF THE TOKEN IS NOT EXPIRED
        else {
            if (user.isActive()) {
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
                    return new ResponseEntity<>(new MessageResponseEntity<>(errors, HttpStatus.BAD_REQUEST, null), HttpStatus.BAD_REQUEST);


                if (user.getPassword().equals(user.getConfirmPassword())) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setUpdatedBy("user@" + user.getFirstName());
                    user.setLastUpdated(new Date());
                    userRepository.save(user);
                    confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
                    return sendResetPasswordSuccessMail(user);
                } else
                    throw new PasswordDoesNotMatchException("Password and Confirm Password does not match!");
            } else
                throw new UserDeactiveException("The User is Not An Active User:" + user.getEmail());
        }
    }


    //-------------------------------------------------LOGOUT THE ACCOUNT---------------------------------------------------
    //LOGOUT METHOD
//    public ResponseEntity<MessageResponseEntity<String>> logout(HttpServletRequest request){
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null) {
//            String tokenValue = authHeader.replace("Bearer", "").trim();
//            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
//            tokenStore.removeAccessToken(accessToken);
//        }
//        return new ResponseEntity<>(new MessageResponseEntity<>("Logout Successful!", HttpStatus.OK), HttpStatus.OK);
//    }
    public ResponseEntity<MessageResponseEntity> doLogout(String authHeader) {
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return new ResponseEntity<>(
                new MessageResponseEntity<>(HttpStatus.OK, "Logged out successfully"), HttpStatus.OK);
    }


    //---------------------------------------VIEW ALL CATEGORIES AND PRODUCT VARITIONS--------------------------------------

    public List<Map<Object, Object>> getRootCategories() {
        return categoryRepository.findAllRootCategories();
    }

    public List<Map<Object, Object>> getProductVariations(Long categoryID) {
        return productVariationRepository.findProductVariationByCategoryID(categoryID);
    }

    public List<Map<Object, Object>> getAllProductVariations() {
        return productVariationRepository.findAllProductVariation();
    }

    public List<Map<Object, Object>> getOneProductVariation(Long id) {
        return productVariationRepository.findOneProductVariation(id);
    }

    public ResponseEntity<MessageResponseEntity<String>> verifyUserRole(UserCO userCO) {
        String email = userCO.getEmail();
        String userRole = userRepository.getUserRole(email);
        return new ResponseEntity<>(new MessageResponseEntity<>(userRole, HttpStatus.CREATED), HttpStatus.CREATED);
    }
}