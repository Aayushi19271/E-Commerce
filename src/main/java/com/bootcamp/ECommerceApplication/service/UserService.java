package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.dto.PasswordDto;
import com.bootcamp.ECommerceApplication.dto.UserDto;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.exception.TokenNotFoundException;
import com.bootcamp.ECommerceApplication.exception.UserAlreadyExistsException;
import com.bootcamp.ECommerceApplication.exception.UserNotFoundException;
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

        if (user == null) {
            seller.setActive(false);
            seller.setDeleted(false);
            seller.setPassword(passwordEncoder.encode(seller.getPassword()));
            String companyName = seller.getCompany_name().toLowerCase();
            seller.setCompany_name(companyName);
            ArrayList<Role> tempRole = new ArrayList<>();
            Role role = roleRepository.findByAuthority("ROLE_SELLER");
            tempRole.add(role);
            seller.setRoles(tempRole);
            userRepository.save(seller);
            try {
                smtpMailSender.send(seller.getEmail(), "Pending Approval",
                        "The Account has been Registered but is Pending Approval! ");
                return new ResponseEntity<Object>("Mail Send Successfully!", HttpStatus.CREATED);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.err.println("Failed to send email to: " + seller.getEmail() + " reason: " + ex.getMessage());
                return new ResponseEntity<Object>("Failed To Send Email", HttpStatus.BAD_GATEWAY);
            }
        } else
            throw new UserAlreadyExistsException("The Seller's EmailID Already Exist: " + seller.getEmail());
    }



//--------------------------------------------------CUSTOMER REGISTRATION METHOD'S--------------------------------------

    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    public ResponseEntity<Object> createCustomer(Customer customer) throws MessagingException {
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
            userRepository.save(customer);
            ConfirmationToken confirmationToken = createConfirmationToken(customer);
            try {
                return sendMailCustomer(customer, confirmationToken);
            } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to send email to: " + customer.getEmail() + " reason: " + ex.getMessage());
            return new ResponseEntity<Object>("Failed To Send Email", HttpStatus.BAD_GATEWAY);
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
            return sendMailCustomer(user, newConfirmationToken);
        }
        //IF THE TOKEN IS CORRECT
        else {
            User savedUser = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            savedUser.setActive(true);
            userRepository.save(savedUser);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            smtpMailSender.send(savedUser.getEmail(), "Account Activated",
                    "Dear "+savedUser.getFirstName()+", Your Account Has Been Activated!!");
            return new ResponseEntity<Object>("Your Account Has Been Activated!!", HttpStatus.CREATED);
        }
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @Transactional
    public ResponseEntity<Object> reSendActivationLink(UserDto user) throws MessagingException {
        String email = user.getEmail();
        Customer customer= findCustomerByEmail(email);
        if (customer == null)
            throw new UserNotFoundException("EmailID:-"+email);
        if(customer.isActive()) {
            return new ResponseEntity<Object>("User Already Active!", HttpStatus.CREATED);
        }
        else {
            deleteConfirmationToken(email);
            ConfirmationToken newConfirmationToken = createConfirmationToken(customer);
            return sendMailCustomer(customer, newConfirmationToken);
        }
    }

    //SEND REGISTRATION MAIL TO CUSTOMER
    public ResponseEntity<Object> sendMailCustomer(User user,ConfirmationToken newConfirmationToken )
    {
        try {
            smtpMailSender.send(user.getEmail(), "Complete Registration",
                    "Dear " + user.getFirstName() + ",To activate your account, please click the link here : " +
                            "http://localhost:8080/users/customers/confirm-account?token=" + newConfirmationToken.getConfirmationToken());
        }catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to send email to: " + user.getEmail() + " reason: " + ex.getMessage());
            return new ResponseEntity<Object>("Failed To Send Email", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<Object>("Mail Send Successfully!", HttpStatus.CREATED);
    }

//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------

    //FORGOT PASSWORD REQUEST
    public ResponseEntity<Object> sendPasswordResetLink(UserDto userDto) {
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
            return new ResponseEntity<Object>("The User is Not An Active User", HttpStatus.BAD_REQUEST);
        }
    }


    //SEND RESET PASSWORD MAIL TO USERS
    public ResponseEntity<Object> sendResetPasswordMailUsers(User user,ConfirmationToken newConfirmationToken ) {
        try {
            smtpMailSender.send(user.getEmail(), "PASSWORD RESET",
                    "Dear " + user.getFirstName() + ",To reset your account's password, please click the link here : " +
                            "http://localhost:8080/users/reset-password?token="
                            + newConfirmationToken.getConfirmationToken());
        }catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to send email to: " + user.getEmail() + " reason: " + ex.getMessage());
            return new ResponseEntity<Object>("Failed To Send Email", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<Object>("Mail Send Successfully!", HttpStatus.CREATED);
    }


    //SEND RESET PASSWORD SUCCESS MAIL TO USER
    public ResponseEntity<Object> sendResetPasswordSuccessMail(User user) {
        try {
            smtpMailSender.send(user.getEmail(), "SUCCESSFUL PASSWORD RESET",
                    "Dear " + user.getFirstName() + ", You're Password has been successfully changed! ");
        }catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to send email to: " + user.getEmail() + " reason: " + ex.getMessage());
            return new ResponseEntity<Object>("Failed To Send Email", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<Object>("Password Changed Successfully!", HttpStatus.CREATED);
    }

    //PASSWORD RESET METHOD
    @Transactional
    public ResponseEntity<Object> resetPassword(String confirmationToken, PasswordDto passwordDto) {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-" + confirmationToken);
        Calendar calendar = Calendar.getInstance();
        User user = token.getUser();

        //IF THE TOKEN EXPIRES
        if((token.getExpiryDate().getTime()-calendar.getTime().getTime())<=0) {
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            return new ResponseEntity<Object>("Token Expired!", HttpStatus.BAD_REQUEST);
        }
        //IF THE TOKEN IS NOT EXPIRED
        else {
            if (user.isActive()) {
                if(passwordDto.getPassword().equals(passwordDto.getConfirmPassword()))
                {
                    user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
                    userRepository.save(user);
                    confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
                    return sendResetPasswordSuccessMail(user);
                }
                else {
                    return new ResponseEntity<Object>("Password does not match!", HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return new ResponseEntity<Object>("User is not Active!", HttpStatus.BAD_REQUEST);
            }
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
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}