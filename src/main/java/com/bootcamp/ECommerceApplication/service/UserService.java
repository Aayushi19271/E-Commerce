package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.PasswordCO;
import com.bootcamp.ECommerceApplication.component.SmtpMailSender;
import com.bootcamp.ECommerceApplication.co.UserCO;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.dto.UserDTO;
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
import java.util.logging.Logger;

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
    public MessageResponseEntity<Object> createSeller(Seller seller) throws MessagingException {
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
            seller.setCreatedBy("user@"+seller.getFirstName());
            seller.setDateCreated(new Date());
            userRepository.save(seller);
            try {
                smtpMailSender.send(seller.getEmail(), "Pending Approval",
                        "The Account has been Registered but is Pending Approval! ");
                return new MessageResponseEntity<>(sellerDTO, HttpStatus.CREATED);
            } catch (Exception ex) {
                return new MessageResponseEntity<>("Failed to send email!",HttpStatus.BAD_GATEWAY,null);
            }
        } else
            throw new UserAlreadyExistsException("The Seller's EmailID Already Exist: " + seller.getEmail());
    }



//--------------------------------------------------CUSTOMER REGISTRATION METHOD'S--------------------------------------

    Logger logger;

    //REGISTER A CUSTOMER AND SEND AN ACTIVATION LINK
    public MessageResponseEntity<Object> createCustomer(Customer customer) {
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
                return new MessageResponseEntity<>("Failed to send email!",HttpStatus.BAD_GATEWAY,null);
            }
        }
        else
            throw new UserAlreadyExistsException("The Customer's EmailID Already Exist: "+customer.getEmail());
    }


    //ACTIVATE THE CUSTOMER ACCOUNT - VERIFY THE TOKEN SEND USING ACTIVATION LINK
    @Transactional
    public MessageResponseEntity<Object> confirmUserAccountToken(String confirmationToken) throws MessagingException {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-"+confirmationToken);
        return confirmTokenExpiry(confirmationToken);
    }


    //CHECK THE TOKEN EXPIRY (3 CONDITIONS-- token correct, token expires, token wrong)
    @Transactional
    public MessageResponseEntity<Object> confirmTokenExpiry(String confirmationToken) throws MessagingException {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        UserDTO userDTO = converterService.convertToUserDto(user);

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
            savedUser.setUpdatedBy("user@"+savedUser.getFirstName());
            savedUser.setLastUpdated(new Date());
            userRepository.save(savedUser);
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            smtpMailSender.send(savedUser.getEmail(), "Account Activated",
                    "Dear "+savedUser.getFirstName()+", Your Account Has Been Activated!!");
            return new MessageResponseEntity<>(userDTO, HttpStatus.CREATED);
        }
    }


    //RE-SEND ACTIVATION LINK TO THE CUSTOMER
    @Transactional
    public MessageResponseEntity<Object> reSendActivationLink(UserCO userCO){
        Customer customer= findCustomerByEmail(userCO.getEmail());
        if (customer == null)
            throw new UserNotFoundException("EmailID:-"+userCO.getEmail());
        if(customer.isActive()) {
            return new MessageResponseEntity<>("No Data", HttpStatus.CREATED,"User Already Active!");
        }
        else {
            deleteConfirmationToken(customer.getEmail());
            ConfirmationToken newConfirmationToken = createConfirmationToken(customer);
            return sendMailCustomer(customer, newConfirmationToken);
        }
    }

    //SEND REGISTRATION MAIL TO CUSTOMER
    public MessageResponseEntity<Object> sendMailCustomer(User user,ConfirmationToken newConfirmationToken )
    {
        UserDTO userDTO = converterService.convertToUserDto(user);
        try {
            smtpMailSender.send(user.getEmail(), "Complete Registration",
                    "Dear " + user.getFirstName() + ",To activate your account, please click the link here : " +
                            "http://localhost:8080/users/customers/confirm-account?token=" + newConfirmationToken.getConfirmationToken());
        }catch (Exception ex) {
            return new MessageResponseEntity<>("Failed to send email!",HttpStatus.BAD_GATEWAY,null);
        }
        return new MessageResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------

    //FORGOT PASSWORD REQUEST
    public MessageResponseEntity<Object> sendPasswordResetLink(UserCO userDto) {
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
            return new MessageResponseEntity<>("The User is Not An Active User", HttpStatus.BAD_REQUEST,null);
        }
    }


    //SEND RESET PASSWORD MAIL TO USERS
    public MessageResponseEntity<Object> sendResetPasswordMailUsers(User user,ConfirmationToken newConfirmationToken ) {
        try {
            smtpMailSender.send(user.getEmail(), "PASSWORD RESET",
                    "Dear " + user.getFirstName() + ",To reset your account's password, please click the link here : " +
                            "http://localhost:8080/users/reset-password?token="
                            + newConfirmationToken.getConfirmationToken());
        }catch (Exception ex) {
            return new MessageResponseEntity<>("Failed to send email!",HttpStatus.BAD_GATEWAY,null);
        }
        return new MessageResponseEntity<>("No Data", HttpStatus.CREATED,"Mail Send Successfully!");
    }


    //SEND RESET PASSWORD SUCCESS MAIL TO USER
    public MessageResponseEntity<Object> sendResetPasswordSuccessMail(User user) {
        try {
            smtpMailSender.send(user.getEmail(), "SUCCESSFUL PASSWORD RESET",
                    "Dear " + user.getFirstName() + ", You're Password has been successfully changed! ");
        }catch (Exception ex) {
            return new MessageResponseEntity<>("Failed to send email!",HttpStatus.BAD_GATEWAY,null);
        }
        return new MessageResponseEntity<>("No Data", HttpStatus.CREATED,"Password Changed Successfully!");
    }

    //PASSWORD RESET METHOD
    @Transactional
    public MessageResponseEntity<Object> resetPassword(String confirmationToken, PasswordCO passwordDto) {
        //IF THE TOKEN IS NOT FOUND/WRONG
        ConfirmationToken token = findConfirmationToken(confirmationToken);
        if (token == null)
            throw new TokenNotFoundException("token-" + confirmationToken);
        Calendar calendar = Calendar.getInstance();
        User user = token.getUser();

        //IF THE TOKEN EXPIRES
        if((token.getExpiryDate().getTime()-calendar.getTime().getTime())<=0) {
            confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
            return new MessageResponseEntity<>("Token Expired!", HttpStatus.BAD_REQUEST,null);
        }
        //IF THE TOKEN IS NOT EXPIRED
        else {
            if (user.isActive()) {
                if(passwordDto.getPassword().equals(passwordDto.getConfirmPassword()))
                {
                    user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
                    user.setUpdatedBy("user@"+user.getFirstName());
                    user.setLastUpdated(new Date());
                    userRepository.save(user);
                    confirmationTokenRepository.deleteByConfirmationToken(token.getConfirmationToken());
                    return sendResetPasswordSuccessMail(user);
                }
                else {
                    return new MessageResponseEntity<>("Password does not match!", HttpStatus.BAD_REQUEST,null);
                }
            }
            else {
                return new MessageResponseEntity<>("User is not Active!", HttpStatus.BAD_REQUEST,null);
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