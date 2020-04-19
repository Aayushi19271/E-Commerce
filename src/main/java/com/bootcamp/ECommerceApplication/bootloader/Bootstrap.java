package com.bootcamp.ECommerceApplication.bootloader;

import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;

@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {


//--------------------SET THE ROLES IN ROLE TABLE-----------------------------------------------------------------------
        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setAuthority("ROLE_SELLER");
        Role role3 = new Role();
        role3.setAuthority("ROLE_CUSTOMER");
        roleRepository.save(role);
        roleRepository.save(role2);
        roleRepository.save(role3);


////---------------------CREATE ADMIN ACCOUNT-----------------------------------------------------------------------------
        User user = new User();
        user.setEmail("aayushithani@yahoo.in");
        user.setFirstName("Aayushi");
        user.setLastName("Thani");
        user.setPassword(passwordEncoder.encode("Aayushi12#"));
        user.setActive(true);
        user.setDeleted(false);

        ArrayList<Role> tempRole = new ArrayList<>();
        Role role1 = roleRepository.findByAuthority("ROLE_ADMIN");
        tempRole.add(role1);
        user.setRoles(tempRole);
        user.setCreatedBy("admin@" + user.getFirstName());
        user.setDateCreated(new Date());
        userRepository.save(user);

        Address address = new Address();
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        address.setAddress("B7- Pitmapura");
        address.setZipCode(110085);
        address.setLabel("Home");
        address.setUser(user);
        addressRepository.save(address);

//-----------INSERTING THE DATA INTO CATEGORY TABLE---------------------------------------------------------------------

//------------ROOT CATEGORY----------------------------
        Category electronics = new Category();
        electronics.setName("Electronics");
        categoryRepository.save(electronics);

        Category furniture = new Category();
        furniture.setName("Home & Furniture");
        categoryRepository.save(furniture);

        Category fashion = new Category();
        fashion.setName("Fashion");
        categoryRepository.save(fashion);

//------------SUB CATEGORY LEVEL-1----------------------
//-------------SUBCATEGORY:Home & Furniture-------------
        Category bedRoom = new Category();
        bedRoom.setName("Bed Room Furniture");
        bedRoom.setParent(furniture);
        categoryRepository.save(bedRoom);

        Category livingRoom = new Category();
        livingRoom.setName("Living Room Furniture");
        livingRoom.setParent(furniture);
        categoryRepository.save(livingRoom);

        Category officeAndStudy = new Category();
        officeAndStudy.setName("Office And Study Furniture");
        officeAndStudy.setParent(furniture);
        categoryRepository.save(officeAndStudy);

//-------------SUBCATEGORY:Electronics------------------
        Category mobile = new Category();
        mobile.setName("Mobile");
        mobile.setParent(electronics);
        categoryRepository.save(mobile);

        Category camera = new Category();
        camera.setName("Camera");
        camera.setParent(electronics);
        categoryRepository.save(camera);

        Category watch = new Category();
        watch.setName("Watch");
        watch.setParent(electronics);
        categoryRepository.save(watch);

        Category laptop = new Category();
        laptop.setName("Laptop");
        laptop.setParent(electronics);
        categoryRepository.save(laptop);

//-------------SUBCATEGORY:Health & fitness-------------
        Category men = new Category();
        men.setName("Men");
        men.setParent(fashion);
        categoryRepository.save(men);

        Category women = new Category();
        women.setName("Women");
        women.setParent(fashion);
        categoryRepository.save(women);

        Category kids = new Category();
        kids.setName("Kids");
        kids.setParent(fashion);
        categoryRepository.save(kids);

//------------SUB CATEGORY LEVEL-2 Home & Furniture-----
//-------------SUBCATEGORY:Bed Room Furniture-----------
        Category beds = new Category();
        beds.setName("Bed");
        beds.setParent(bedRoom);
        beds.setLeafNode(true);
        categoryRepository.save(beds);

        Category wardrobe = new Category();
        wardrobe.setName("Wardrobe");
        wardrobe.setParent(bedRoom);
        wardrobe.setLeafNode(true);
        categoryRepository.save(wardrobe);

//-------------SUBCATEGORY:Living Room Furniture--------
        Category sofa = new Category();
        sofa.setName("Sofa");
        sofa.setParent(livingRoom);
        sofa.setLeafNode(true);
        categoryRepository.save(sofa);

        Category diningTable = new Category();
        diningTable.setName("Dining Table");
        diningTable.setParent(livingRoom);
        diningTable.setLeafNode(true);
        categoryRepository.save(diningTable);

        Category diningChair = new Category();
        diningChair.setName("Dining Chair");
        diningChair.setParent(livingRoom);
        diningChair.setLeafNode(true);
        categoryRepository.save(diningChair);

//-------------SUBCATEGORY:Office & Study Furniture-----
        Category studyTable = new Category();
        studyTable.setName("Study Table");
        studyTable.setParent(officeAndStudy);
        studyTable.setLeafNode(true);
        categoryRepository.save(studyTable);

        Category officeChair = new Category();
        officeChair.setName("Office Chair");
        officeChair.setParent(officeAndStudy);
        officeChair.setLeafNode(true);
        categoryRepository.save(officeChair);

        Category officeTable = new Category();
        officeTable.setName("Office Table");
        officeTable.setParent(officeAndStudy);
        officeTable.setLeafNode(true);
        categoryRepository.save(officeTable);

//------------SUB CATEGORY LEVEL-2 Electronics----------
//-------------SUBCATEGORY:MOBILE-----------------------
        Category samsung = new Category();
        samsung.setName("Samsung Mobile");
        samsung.setParent(mobile);
        samsung.setLeafNode(true);
        categoryRepository.save(mobile);

        Category apple = new Category();
        apple.setName("Apple Mobile");
        apple.setParent(mobile);
        apple.setLeafNode(true);
        categoryRepository.save(mobile);

//-------------SUBCATEGORY:CAMERA-----------------------
        Category dslrCamera = new Category();
        dslrCamera.setName("DSLR Camera");
        dslrCamera.setParent(camera);
        dslrCamera.setLeafNode(true);
        categoryRepository.save(dslrCamera);

        Category sportsCamera = new Category();
        sportsCamera.setName("Sports Camera");
        sportsCamera.setParent(camera);
        sportsCamera.setLeafNode(true);
        categoryRepository.save(sportsCamera);

//-------------SUBCATEGORY:WATCH------------------------
        Category digitalWatch = new Category();
        digitalWatch.setName("Digital Watches");
        digitalWatch.setParent(watch);
        digitalWatch.setLeafNode(true);
        categoryRepository.save(digitalWatch);

        Category smartWatch = new Category();
        smartWatch.setName("Smart Watches");
        smartWatch.setParent(watch);
        smartWatch.setLeafNode(true);
        categoryRepository.save(smartWatch);

//-------------SUBCATEGORY:LAPTOP-----------------------
        Category macBookLaptop = new Category();
        macBookLaptop.setName("MAC Book Laptops");
        macBookLaptop.setParent(laptop);
        macBookLaptop.setLeafNode(true);
        categoryRepository.save(macBookLaptop);

        Category gamingLaptop = new Category();
        gamingLaptop.setName("Gaming Laptops");
        gamingLaptop.setParent(laptop);
        gamingLaptop.setLeafNode(true);
        categoryRepository.save(gamingLaptop);

//------------SUB CATEGORY LEVEL-2 FASHION-----
//-------------SUBCATEGORY:MEN SUBCATEGORY----------
        Category menShoes = new Category();
        menShoes.setName("Men Shoes");
        menShoes.setParent(men);
        menShoes.setLeafNode(true);
        categoryRepository.save(menShoes);

        Category menShirts = new Category();
        menShirts.setName("Men Shirts");
        menShirts.setParent(men);
        menShirts.setLeafNode(true);
        categoryRepository.save(menShirts);

//-------------SUBCATEGORY: WOMEN SUBCATEGORY------------
        Category womenShoes = new Category();
        womenShoes.setName("Women Shoes");
        womenShoes.setParent(women);
        womenShoes.setLeafNode(true);
        categoryRepository.save(womenShoes);

        Category womenShirts = new Category();
        womenShirts.setName("Women Shirts");
        womenShirts.setParent(women);
        womenShirts.setLeafNode(true);
        categoryRepository.save(womenShirts);


//---------------ADD DATA TO CATEGORY METADATA FIELD TABLE------------------------

        CategoryMetadataField size = new CategoryMetadataField();
        size.setName("size");
        categoryMetadataFieldRepository.save(size);

        CategoryMetadataField color = new CategoryMetadataField();
        color.setName("color");
        categoryMetadataFieldRepository.save(color);

        CategoryMetadataField brand = new CategoryMetadataField();
        brand.setName("brand");
        categoryMetadataFieldRepository.save(brand);

        CategoryMetadataField length = new CategoryMetadataField();
        size.setName("length");
        categoryMetadataFieldRepository.save(length);

        CategoryMetadataField fabric = new CategoryMetadataField();
        color.setName("fabric");
        categoryMetadataFieldRepository.save(fabric);

        CategoryMetadataField heelSize = new CategoryMetadataField();
        brand.setName("Heel Size");
        categoryMetadataFieldRepository.save(heelSize);

//---------------ADD DATA TO CATEGORY METADATA FIELD VALUES TABLE------------------

        CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues.setCategory(studyTable);
        categoryMetadataFieldValues.setCategoryMetadataField(color);
        categoryMetadataFieldValues.setValue("orange,white,brown");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);

        CategoryMetadataFieldValues categoryMetadataFieldValues2 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues2.setCategory(officeChair);
        categoryMetadataFieldValues2.setCategoryMetadataField(size);
        categoryMetadataFieldValues2.setValue("small,large");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues2);

        CategoryMetadataFieldValues categoryMetadataFieldValues3 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues3.setCategory(officeTable);
        categoryMetadataFieldValues3.setCategoryMetadataField(brand);
        categoryMetadataFieldValues3.setValue("Wooden Street,Urban Ladder");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues3);

        CategoryMetadataFieldValues categoryMetadataFieldValues4 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues4.setCategory(menShoes);
        categoryMetadataFieldValues4.setCategoryMetadataField(color);
        categoryMetadataFieldValues4.setValue("black,brown");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues4);

        CategoryMetadataFieldValues categoryMetadataFieldValues7 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues7.setCategory(menShoes);
        categoryMetadataFieldValues7.setCategoryMetadataField(size);
        categoryMetadataFieldValues7.setValue("8,8.5,9,9.5,10");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues7);

        CategoryMetadataFieldValues categoryMetadataFieldValues5 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues5.setCategory(menShirts);
        categoryMetadataFieldValues5.setCategoryMetadataField(size);
        categoryMetadataFieldValues5.setValue("S,M,L,XL,XXL");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues5);

        CategoryMetadataFieldValues categoryMetadataFieldValues8 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues8.setCategory(menShirts);
        categoryMetadataFieldValues8.setCategoryMetadataField(fabric);
        categoryMetadataFieldValues8.setValue("fine cotton,linen");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues8);

        CategoryMetadataFieldValues categoryMetadataFieldValues6 = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues6.setCategory(womenShoes);
        categoryMetadataFieldValues6.setCategoryMetadataField(brand);
        categoryMetadataFieldValues6.setValue("crocs,sparx,Puma");
        categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues6);

    }
}




























