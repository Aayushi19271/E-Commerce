package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.co.UserCO;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.dto.UserDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.*;
import com.bootcamp.ECommerceApplication.repository.ConfirmationTokenRepository;
import com.bootcamp.ECommerceApplication.repository.CustomerRepository;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    private SmtpMailSender smtpMailSender;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    ConverterService converterService;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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
    public ResponseEntity<Object> createSeller(Seller seller) throws MessagingException {
        String customerEmail = seller.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);
        SellerDTO sellerDTO = converterService.convertToSellerDto(seller);

        if (user == null) {
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
                smtpMailSender.send(seller.getEmail(), "Pending Approval",
                        "The Account has been Registered but is Pending Approval! ");
                return new ResponseEntity<>(new MessageResponseEntity<>(sellerDTO, HttpStatus.CREATED), HttpStatus.CREATED);
            } catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+seller.getEmail());
            }
        } else
            throw new UserAlreadyExistsException("The Seller's EmailID Already Exist: " + seller.getEmail());
    }




//--------------------------------------------------CUSTOMER REGISTRATION METHOD'S--------------------------------------

    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    public ResponseEntity<Object> createCustomer(Customer customer) {
        String customerEmail = customer.getEmail();
        final User user = userRepository.findByEmailIgnoreCase(customerEmail);

        if(user == null) {
            customer.setDeleted(false);
            customer.setActive(false);
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            ArrayList<Role> tempRole = new ArrayList<>();
            Role role = roleRepository.findByAuthority("ROLE_CUSTOMER");
            tempRole.add(role);
            customer.setRoles(tempRole);
            customer.setCreatedBy("user@"+customer.getFirstName());
            customer.setDateCreated(new Date());
            userRepository.save(customer);
            ConfirmationToken confirmationToken = createConfirmationToken(customer);
            try {
                return sendMailCustomer(customer, confirmationToken);
            } catch (Exception ex) {
                throw new MailSendFailedException("Failed to Send Mail: "+customer.getEmail());
            }
        }
        else
            throw new UserAlreadyExistsException("The Customer's EmailID Already Exist: "+customer.getEmail());
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @Transactional
    public ResponseEntity<Object> confirmUserAccountToken(String confirmationToken) throws MessagingException {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-"+confirmationToken);
        return confirmTokenExpiry(confirmationToken);
    }


    //CHECK THE TOKEN EXPIRY (3 CONDITIONS-- token correct, token expires, token wrong)
    @Transactional
    public ResponseEntity<Object> confirmTokenExpiry(String confirmationToken) throws MessagingException {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();

        //IF THE TOKEN EXPIRES
        if((token.getExpiryDate().getTime()-calendar.getTime().getTime())<=0) {
            ConfirmationToken newConfirmationToken = new ConfirmationToken(token.getUser());
            newConfirmationToken.setExpiryDate(new Date());
            confirmationTokenRepository.save(newConfirmationToken);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            sendMailCustomer(user, newConfirmationToken);
            throw new TokenNotFoundException("token-"+confirmationToken);
        }
        //IF THE TOKEN IS CORRECT
        else {
            User savedUser = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            savedUser.setActive(true);
            savedUser.setUpdatedBy("user@"+savedUser.getFirstName());
            savedUser.setLastUpdated(new Date());
            userRepository.save(savedUser);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            smtpMailSender.send(savedUser.getEmail(), "Account Activated",
                    "Dear "+savedUser.getFirstName()+", Your Account Has Been Activated!!");
            return new ResponseEntity<>(new MessageResponseEntity<>("Account Successfully Activated!", HttpStatus.CREATED), HttpStatus.CREATED);
        }
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @Transactional
    public ResponseEntity<Object> reSendActivationLink(UserCO userCO){
        Customer customer= findCustomerByEmail(userCO.getEmail());
        if (customer == null)
            throw new UserNotFoundException("EmailID:-"+userCO.getEmail());
        if(customer.isActive()) {
            return new ResponseEntity<>( new MessageResponseEntity<>(HttpStatus.OK,"User Already Active!"),HttpStatus.OK);
        }
        else {
            deleteConfirmationToken(customer.getEmail());
            ConfirmationToken newConfirmationToken = createConfirmationToken(customer);
            return sendMailCustomer(customer, newConfirmationToken);
        }
    }

    //SEND REGISTRATION MAIL TO CUSTOMER
    public ResponseEntity<Object> sendMailCustomer(User user, ConfirmationToken newConfirmationToken )
    {
        try {
            smtpMailSender.send(user.getEmail(), "Complete Registration",
                    "Dear " + user.getFirstName() + ",To activate your account, please click the link here : " +
                            "http://localhost:8080/users/customers/confirm-account?token=" + newConfirmationToken.getConfirmationToken());
        }catch (Exception ex) {
            throw new MailSendFailedException("Failed to Send Mail: "+user.getEmail());
        }
        UserDTO userDTO = converterService.convertToUserDto(user);
        return new ResponseEntity<>(new MessageResponseEntity<>(userDTO,
                HttpStatus.CREATED),HttpStatus.CREATED);
    }

//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------

    //FORGOT PASSWORD REQUEST
    public ResponseEntity<Object> sendPasswordResetLink(UserCO userDto) {
        String email = userDto.getEmail();
        User user = userRepository.findByEmailIgnoreCase(email);
        if(user==null)
            throw new UserNotFoundException("The User's EmailID Does Not Exist: "+userDto.getEmail());
        boolean active = user.isActive();
        if(active) {
            ConfirmationToken confirmationToken = createConfirmationToken(user);
            return sendResetPasswordMailUsers(user, confirmationToken);
        }
        else {
            throw new UserDeactiveException("The User is Not An Active User:"+user.getEmail());
        }
    }


    //SEND RESET PASSWORD MAIL TO USERS
    public ResponseEntity<Object> sendResetPasswordMailUsers(User user, ConfirmationToken newConfirmationToken ) {
        try {
            smtpMailSender.send(user.getEmail(), "PASSWORD RESET",
                    "Dear " + user.getFirstName() + ",To reset your account's password, please click the link here : " +
                            "http://localhost:8080/users/reset-password?token="
                            + newConfirmationToken.getConfirmationToken());
        }catch (Exception ex) {
            throw new MailSendFailedException("Failed to Send Mail: "+user.getEmail());
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Mail Send Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
    }


    //SEND RESET PASSWORD SUCCESS MAIL TO USER
    public ResponseEntity<Object> sendResetPasswordSuccessMail(User user) {
        try {
            smtpMailSender.send(user.getEmail(), "SUCCESSFUL PASSWORD RESET",
                    "Dear " + user.getFirstName() + ", You're Password has been successfully changed! ");
        }catch (Exception ex) {
            throw new MailSendFailedException("Failed to Send Mail: "+user.getEmail());
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Password Changed Successfully!", HttpStatus.CREATED), HttpStatus.CREATED);
    }

    //PASSWORD RESET METHOD
    @Transactional
    public ResponseEntity<Object> resetPassword(String confirmationToken, PasswordCO passwordCo) {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-" + confirmationToken);
        Calendar calendar = Calendar.getInstance();
        User user = token.getUser();

        //IF THE TOKEN EXPIRES
        if((token.getExpiryDate().getTime()-calendar.getTime().getTime())<=0) {
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            throw new TokenExpiredException("The Token is Expired! Please Provide a Valid Token.");
        }
        //IF THE TOKEN IS NOT EXPIRED
        else {
            if (user.isActive()) {
                if(passwordCo.getPassword().equals(passwordCo.getConfirmPassword()))
                {
                    user.setPassword(passwordEncoder.encode(passwordCo.getPassword()));
                    user.setUpdatedBy("user@"+user.getFirstName());
                    user.setLastUpdated(new Date());
                    userRepository.save(user);
                    confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
                    return sendResetPasswordSuccessMail(user);
                }
                else
                    throw new PasswordDoesNotMatchException("Password and Confirm Password does not match!");
            }
            else
                throw new UserDeactiveException("The User is Not An Active User:"+user.getEmail());
        }
    }


//-------------------------------------------------LOGOUT THE ACCOUNT---------------------------------------------------
    //LOGOUT METHOD
    public ResponseEntity<Object> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return new ResponseEntity<>(new MessageResponseEntity<>("Logout Successful!", HttpStatus.OK), HttpStatus.OK);
    }
}