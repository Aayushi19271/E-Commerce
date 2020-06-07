package com.bootcamp.ECommerceApplication.controller;

import com.bootcamp.ECommerceApplication.co.CustomerCO;
import com.bootcamp.ECommerceApplication.co.SellerCO;
import com.bootcamp.ECommerceApplication.co.UserCO;
import com.bootcamp.ECommerceApplication.configuration.MessageResponseEntity;
import com.bootcamp.ECommerceApplication.dto.UserDTO;
import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.service.ConverterService;
import com.bootcamp.ECommerceApplication.service.UserService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@ApiModel(description = "User Controller Class")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConverterService converterService;

//---------------------------------------REGISTER CUSTOMER AND SELLER---------------------------------------------------
@ApiOperation(value = "API to register a seller")
@PostMapping("/users/sellers-registration")
public ResponseEntity<MessageResponseEntity<UserDTO>> createSeller(@Valid @RequestBody SellerCO sellerCO)
        throws MessagingException {
    Seller seller = converterService.convertToSeller(sellerCO);
    return userService.createSeller(seller);
}

    @ApiOperation(value = "API to register a customer")
    @PostMapping("/users/customers-registration")
    public ResponseEntity<MessageResponseEntity<UserDTO>> createCustomerToken(@Valid @RequestBody CustomerCO customerCO) {
        Customer customer = converterService.convertToCustomer(customerCO);
        return userService.createCustomer(customer);
    }


    @ApiOperation(value = "API to activate the customer")
    @GetMapping("/users/customers/confirm-account")
    public ResponseEntity<MessageResponseEntity<String>> confirmUserAccount(@RequestParam("token") String confirmationToken)
            throws MessagingException {
        return userService.confirmUserAccountToken(confirmationToken);
    }


    @ApiOperation(value = "API to activate the customer using PUT method")
    @PutMapping("/users/customers/confirm-account/{token}")
    public ResponseEntity<MessageResponseEntity<String>> confirmUserAccountToken(@PathVariable String token)
            throws MessagingException {
        return userService.confirmUserAccountToken(token);
    }


    @ApiOperation(value = "API to re-send activation link")
    @PostMapping("/users/customers/re-send-activation-link")
    public ResponseEntity<MessageResponseEntity<UserDTO>> reSendActivationLink(@RequestBody UserCO userCO){
        return userService.reSendActivationLink(userCO);
    }


//--------------------------------------------------FORGOT PASSWORD METHOD'S--------------------------------------------
    @ApiOperation(value = "API to receive a token based URL in mail for forgot password")
    @PostMapping("/users/password/forgot")
    public ResponseEntity<MessageResponseEntity<String>> sendPasswordResetLink(@RequestBody UserCO userCO) {
        return userService.sendPasswordResetLink(userCO);
    }

    @ApiOperation(value = "API to reset the password using the token")
    @PatchMapping("/users/password/reset")
    public ResponseEntity<Object> resetPassword(@RequestParam String token,
                                                @RequestBody Map<Object,Object> fields) {
        return userService.resetPassword(token,fields);
    }

//---------------------------------------VIEW ALL CATEGORIES AND PRODUCT VARIATIONS--------------------------------------

    @GetMapping(value={"/users/categories"})
    public List<Map<Object, Object>> getRootCategories(){
        return userService.getRootCategories();
    }

    @GetMapping(value={"/users/categories/{categoryID}"})
    public List<Map<Object, Object>> getProductVariations(@PathVariable Long categoryID ){
        return userService.getProductVariations(categoryID);
    }

    @GetMapping(value={"/users/products"})
    public List<Map<Object, Object>> getAllProductVariations(){
        return userService.getAllProductVariations();
    }

    @GetMapping(value={"/users/products/{id}"})
    public List<Map<Object, Object>> getOneProductVariation(@PathVariable Long id){
        return userService.getOneProductVariation(id);
    }


//--------------------------------------------------LOGOUT METHOD'S-----------------------------------------------------
    @ApiOperation(value = "API to display Main Welcome page")
    @GetMapping("/")
    public String test() {
        return "Welcome to the E-Commerce Application!!!";
    }

    @ApiOperation(value = "API to logout of account")
//    @GetMapping("/doLogout")
//    public ResponseEntity<MessageResponseEntity<String>> logout(HttpServletRequest request){
//        return userService.logout(request);
//    }

    @PostMapping(path = "/doLogout")
    public ResponseEntity<MessageResponseEntity> userLogout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return userService.doLogout(authHeader);
    }

//--------------------------------------------------USER ROLE VERIFY METHOD'S-----------------------------------------------------

    @PostMapping("/users/role")
    public ResponseEntity<MessageResponseEntity<String>> verifyUserRole(@RequestBody UserCO userCO){
        return userService.verifyUserRole(userCO);
    }
}
