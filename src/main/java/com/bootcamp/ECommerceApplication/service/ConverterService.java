package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.dto.AddressDto;
import com.bootcamp.ECommerceApplication.dto.CustomerDto;
import com.bootcamp.ECommerceApplication.dto.SellerDto;
import com.bootcamp.ECommerceApplication.dto.UserDto;
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
//-------------------------------------------USER ENTITY AND USER DTO---------------------------------------------------
    public UserDto convertToUserDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    public User convertToUser(UserDto userDto){ return modelMapper.map(userDto,User.class); }


//-------------------------------------------ADDRESS ENTITY AND ADDRESS DTO---------------------------------------------

    public AddressDto convertToAddressDto(Address address){
        return modelMapper.map(address,AddressDto.class);
    }

    public Address convertToAddress(AddressDto addressDto){
        return modelMapper.map(addressDto, Address.class);
    }



//-------------------------------------------SELLER ENTITY AND SELLER DTO-----------------------------------------------
    public SellerDto convertToSellerDto(Seller seller){
        return modelMapper.map(seller,SellerDto.class);
    }

    public Seller convertToSeller(SellerDto sellerDto){
        return modelMapper.map(sellerDto,Seller.class);
    }



//-------------------------------------------CUSTOMER ENTITY AND CUSTOMER DTO-------------------------------------------
    public CustomerDto convertToCustomerDto(Customer customer){
        return modelMapper.map(customer,CustomerDto.class);
    }

    public Customer convertToCustomer(CustomerDto customerDto){ return modelMapper.map(customerDto,Customer.class); }
}
