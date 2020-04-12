package com.bootcamp.ECommerceApplication.bootloader;

import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Category;
import com.bootcamp.ECommerceApplication.entity.Role;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.repository.AddressRepository;
import com.bootcamp.ECommerceApplication.repository.CategoryRepository;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AddressRepository addressRepository;

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
        user.setCreatedBy("admin@"+user.getFirstName());
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

        Category health = new Category();
        health.setName("Health & fitness");
        categoryRepository.save(health);

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
        Category healthCareDevices = new Category();
        healthCareDevices.setName("Health Care Devices");
        healthCareDevices.setParent(health);
        categoryRepository.save(healthCareDevices);

        Category personalFitness = new Category();
        personalFitness.setName("Personal Fitness Devices");
        personalFitness.setParent(health);
        categoryRepository.save(personalFitness);

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

//------------SUB CATEGORY LEVEL-2 Health & fitness-----
//-------------SUBCATEGORY:Health Care Devices----------
        Category bloodPressureMonitor = new Category();
        bloodPressureMonitor.setName("Blood Pressure Monitor");
        bloodPressureMonitor.setParent(healthCareDevices);
        bloodPressureMonitor.setLeafNode(true);
        categoryRepository.save(bloodPressureMonitor);

        Category glucoseMonitor = new Category();
        glucoseMonitor.setName("Glucose Monitor");
        glucoseMonitor.setParent(healthCareDevices);
        glucoseMonitor.setLeafNode(true);
        categoryRepository.save(glucoseMonitor);

//-------------SUBCATEGORY: Family Nutrition------------
        Category cardioEquipment = new Category();
        cardioEquipment.setName("Cardio Equipments");
        cardioEquipment.setParent(personalFitness);
        cardioEquipment.setLeafNode(true);
        categoryRepository.save(cardioEquipment);

        Category strengthEquipment = new Category();
        strengthEquipment.setName("Strength Equipments");
        strengthEquipment.setParent(personalFitness);
        strengthEquipment.setLeafNode(true);
        categoryRepository.save(strengthEquipment);
    }
}
