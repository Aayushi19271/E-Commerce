package com.bootcamp.ECommerceApplication.service;

import com.bootcamp.ECommerceApplication.co.*;
import com.bootcamp.ECommerceApplication.dto.*;
import com.bootcamp.ECommerceApplication.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

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

    public Seller convertToSeller(@Valid SellerCO sellerCO){ return modelMapper.map(sellerCO,Seller.class);   }

    public SellerCO convertToSellerCO(Seller seller){ return modelMapper.map(seller,SellerCO.class);   }


//-------------------------------------------CUSTOMER ENTITY AND CUSTOMER CO/DTO----------------------------------------
    public CustomerDTO convertToCustomerDto(Customer customer){
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer convertToCustomer(CustomerCO customerCO){ return modelMapper.map(customerCO,Customer.class); }

//-------------------------------------------Category Metadata Field CO/DTO---------------------------------------------

    public CategoryMetadataFieldDTO convertToCategoryMetadataFieldDTO(CategoryMetadataField categoryMetadataField){
        return modelMapper.map(categoryMetadataField, CategoryMetadataFieldDTO.class);
    }

    public CategoryMetadataField convertToCategoryMetadataField(CategoryMetadataFieldCO categoryMetadataFieldCO){
        return modelMapper.map(categoryMetadataFieldCO,CategoryMetadataField.class);
    }

//------------------------------------------------Category CO/DTO-------------------------------------------------------
    public CategoryDTO convertToCategoryDTO(Category category){ return modelMapper.map(category, CategoryDTO.class); }

    public Category convertToCategory(CategoryCO categoryCO){ return modelMapper.map(categoryCO,Category.class); }
}
