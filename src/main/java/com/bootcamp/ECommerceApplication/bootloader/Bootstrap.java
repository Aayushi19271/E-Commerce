package com.bootcamp.ECommerceApplication.bootloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    private BootstrapData bootstrapData;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        bootstrapData.addRoles();
        bootstrapData.addAdmin();
        bootstrapData.addData();

//---------------ADD DATA TO CATEGORY METADATA FIELD TABLE------------------------
//
//        CategoryMetadataField size = new CategoryMetadataField();
//        size.setName("size");
//        categoryMetadataFieldRepository.save(size);
//
//        CategoryMetadataField color = new CategoryMetadataField();
//        color.setName("color");
//        categoryMetadataFieldRepository.save(color);
//
//        CategoryMetadataField brand = new CategoryMetadataField();
//        brand.setName("brand");
//        categoryMetadataFieldRepository.save(brand);
//
//        CategoryMetadataField price = new CategoryMetadataField();
//        price.setName("price");
//        categoryMetadataFieldRepository.save(price);

//---------------ADD DATA TO CATEGORY METADATA FIELD VALUES TABLE------------------

//        CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues.setCategory(sofa);
//        categoryMetadataFieldValues.setCategoryMetadataField(color);
//        categoryMetadataFieldValues.setValue("orange,white,brown");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues2 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues2.setCategory(diningTable);
//        categoryMetadataFieldValues2.setCategoryMetadataField(size);
//        categoryMetadataFieldValues2.setValue("small,large");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues2);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues3 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues3.setCategory(diningChairs);
//        categoryMetadataFieldValues3.setCategoryMetadataField(brand);
//        categoryMetadataFieldValues3.setValue("Wooden Street,Urban Ladder");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues3);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues4 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues4.setCategory(menShoes);
//        categoryMetadataFieldValues4.setCategoryMetadataField(color);
//        categoryMetadataFieldValues4.setValue("black,brown");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues4);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues7 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues7.setCategory(menShoes);
//        categoryMetadataFieldValues7.setCategoryMetadataField(size);
//        categoryMetadataFieldValues7.setValue("8,8.5,9,9.5,10");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues7);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues5 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues5.setCategory(menShirts);
//        categoryMetadataFieldValues5.setCategoryMetadataField(size);
//        categoryMetadataFieldValues5.setValue("S,M,L,XL,XXL");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues5);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues8 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues8.setCategory(menShirts);
//        categoryMetadataFieldValues8.setCategoryMetadataField(price);
//        categoryMetadataFieldValues8.setValue("fine cotton,linen");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues8);
//
//        CategoryMetadataFieldValues categoryMetadataFieldValues6 = new CategoryMetadataFieldValues();
//        categoryMetadataFieldValues6.setCategory(womenShoes);
//        categoryMetadataFieldValues6.setCategoryMetadataField(brand);
//        categoryMetadataFieldValues6.setValue("crocs,sparx,Puma");
//        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues6);
    }

}




























