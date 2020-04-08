package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.AddressCO;
import com.bootcamp.ECommerceApplication.co.CustomerCO;
import com.bootcamp.ECommerceApplication.co.SellerCO;
import com.bootcamp.ECommerceApplication.co.UserCO;
import com.bootcamp.ECommerceApplication.dto.AddressDTO;
import com.bootcamp.ECommerceApplication.dto.CustomerDTO;
import com.bootcamp.ECommerceApplication.dto.SellerDTO;
import com.bootcamp.ECommerceApplication.dto.UserDTO;
import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.Seller;
import com.bootcamp.ECommerceApplication.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConverterService {

    @Autowired
    private ModelMapper modelMapper;
//-------------------------------------------USER ENTITY AND USER CO/DTO------------------------------------------------
    public User convertToUser(UserCO userCO){ return modelMapper.map(userCO,User.class); }

    public UserDTO convertToUserDto(User user){ return modelMapper.map(user, UserDTO.class);}

//-------------------------------------------ADDRESS ENTITY AND ADDRESS CO/DTO------------------------------------------

    public AddressDTO convertToAddressDto(Address address){
        return modelMapper.map(address, AddressDTO.class);
    }

    public Address convertToAddress(AddressCO addressCO){ return modelMapper.map(addressCO, Address.class); }


//-------------------------------------------SELLER ENTITY AND SELLER CO/DTO--------------------------------------------
    public SellerDTO convertToSellerDto(Seller seller){
        return modelMapper.map(seller, SellerDTO.class);
    }

    public Seller convertToSeller(SellerCO sellerCO){ return modelMapper.map(sellerCO,Seller.class);   }


//-------------------------------------------CUSTOMER ENTITY AND CUSTOMER CO/DTO----------------------------------------
    public CustomerDTO convertToCustomerDto(Customer customer){
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer convertToCustomer(CustomerCO customerCO){ return modelMapper.map(customerCO,Customer.class); }
}
