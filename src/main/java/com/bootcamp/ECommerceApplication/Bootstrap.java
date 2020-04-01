package com.bootcamp.ECommerceApplication;

import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Category;
import com.bootcamp.ECommerceApplication.entity.Role;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.repository.CategoryRepository;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

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


//---------------------CREATE ADMIN ACCOUNT-----------------------------------------------------------------------------
//        User user = new User();
//        user.setEmail("aayushithani@yahoo.in");
//        user.setFirstName("Aayushi");
//        user.setLastName("Thani");
//        user.setPassword("Aayushi12#");
//        user.setActive(true);
//        user.setDeleted(false);
//
//        List<Address> list = new ArrayList<>();
//        Address address = new Address();
//        address.setCity("Delhi");
//        address.setState("Delhi");
//        address.setCountry("India");
//        address.setAddress("B7- Pitmapura");
//        address.setZipCode(110085);
//        address.setLabel("Home");
//        list.add(address);
//        user.setAddresses(list);
//
//        ArrayList<Role> tempRole = new ArrayList<>();
//        Role role1 = roleRepository.findById((long) 1).get();
//        tempRole.add(role1);
//        user.setRoles(tempRole);
//
//        userRepository.save(user);



//-----------INSERTING THE DATA INTO CATEGORY TABLE---------------------------------------------------------------------

//------------ROOT CATEGORY------------------
            Category fashion = new Category();
            fashion.setName("FASHION");
            categoryRepository.save(fashion);

            Category electronics = new Category();
            electronics.setName("ELECTRONICS");
            categoryRepository.save(electronics);

            Category books = new Category();
            books.setName("BOOKS");
            categoryRepository.save(books);

//------------SUB CATEGORY LEVEL-1------------------
//-------------SUBCATEGORY:FASHION------------------
            Category men = new Category();
            men.setName("MEN");
            men.setParent(fashion);
            categoryRepository.save(men);

            Category women = new Category();
            women.setName("WOMEN");
            women.setParent(fashion);
            categoryRepository.save(women);

            Category kids = new Category();
            kids.setName("KIDS");
            kids.setParent(fashion);
            categoryRepository.save(kids);

//-------------SUBCATEGORY:ELECTRONICS------------------
            Category mobileAndAccessory = new Category();
            mobileAndAccessory.setName("MOBILE AND ACCESSORY");
            mobileAndAccessory.setParent(electronics);
            categoryRepository.save(mobileAndAccessory);

            Category cameraAndAccessory = new Category();
            cameraAndAccessory.setName("CAMERA AND ACCESSORY");
            cameraAndAccessory.setParent(electronics);
            categoryRepository.save(cameraAndAccessory);

//-------------SUBCATEGORY:BOOKS------------------
            Category actionAndAdventure = new Category();
            actionAndAdventure.setName("ACTION AND ADVENTURE");
            actionAndAdventure.setParent(books);
            categoryRepository.save(actionAndAdventure);

            Category biography = new Category();
            biography.setName("BIOGRAPHY");
            biography.setParent(books);
            biography.setLeafNode(true);
            categoryRepository.save(biography);

//------------SUB CATEGORY LEVEL-2------------------
//-------------SUBCATEGORY:MEN------------------
            Category menCloth = new Category();
            menCloth.setName("MEN CLOTH");
            menCloth.setParent(men);
            menCloth.setLeafNode(true);
            categoryRepository.save(menCloth);

            Category menShoe = new Category();
            menShoe.setName("MEN SHOE");
            menShoe.setParent(men);
            menShoe.setLeafNode(true);
            categoryRepository.save(menShoe);

            Category menWatch = new Category();
            menWatch.setName("MEN WATCH");
            menWatch.setParent(men);
            menWatch.setLeafNode(true);
            categoryRepository.save(menWatch);

//-------------SUBCATEGORY:WOMEN------------------
            Category womenCloth = new Category();
            womenCloth.setName("WOMEN CLOTH");
            womenCloth.setParent(women);
            womenCloth.setLeafNode(true);
            categoryRepository.save(womenCloth);

            Category womenShoe = new Category();
            womenShoe.setName("WOMEN SHOE");
            womenShoe.setParent(women);
            womenShoe.setLeafNode(true);
            categoryRepository.save(womenShoe);

            Category womenWatch = new Category();
            womenWatch.setName("WOMEN WATCH");
            womenWatch.setParent(women);
            womenWatch.setLeafNode(true);
            categoryRepository.save(womenWatch);

//-------------SUBCATEGORY:KIDS------------------
            Category kidsCloth = new Category();
            kidsCloth.setName("KIDS CLOTH");
            kidsCloth.setParent(kids);
            kidsCloth.setLeafNode(true);
            categoryRepository.save(kidsCloth);

            Category kidsShoe = new Category();
            kidsShoe.setName("KIDS SHOE");
            kidsShoe.setParent(kids);
            kidsShoe.setLeafNode(true);
            categoryRepository.save(kidsShoe);

            Category kidsWatch = new Category();
            kidsWatch.setName("KIDS WATCH");
            kidsWatch.setParent(kids);
            kidsWatch.setLeafNode(true);
            categoryRepository.save(kidsWatch);

//-------------SUBCATEGORY:MOBILE AND ACCESSORY------------------
            Category mobile = new Category();
            mobile.setName("MOBILE");
            mobile.setParent(mobileAndAccessory);
            mobile.setLeafNode(true);
            categoryRepository.save(mobile);

            Category mobileAccessory = new Category();
            mobile.setName("MOBILE ACCESSORY");
            mobile.setParent(mobileAndAccessory);
            mobile.setLeafNode(true);
            categoryRepository.save(mobile);

//-------------SUBCATEGORY:CAMERA AND ACCESSORY------------------
            Category camera = new Category();
            camera.setName("CAMERA");
            camera.setParent(cameraAndAccessory);
            camera.setLeafNode(true);
            categoryRepository.save(camera);

            Category cameraAccessory = new Category();
            camera.setName("CAMERA ACCESSORY");
            camera.setParent(mobileAndAccessory);
            camera.setLeafNode(true);
            categoryRepository.save(camera);

//-------------SUBCATEGORY:ACTION AND ADVENTURE------------------
            Category actionBook = new Category();
            actionBook.setName("ACTION BOOK");
            actionBook.setParent(actionAndAdventure);
            actionBook.setLeafNode(true);
            categoryRepository.save(actionBook);

            Category adventureBook = new Category();
            adventureBook.setName("ADVENTURE BOOK");
            adventureBook.setParent(actionAndAdventure);
            adventureBook.setLeafNode(true);
            categoryRepository.save(adventureBook);
        }
}
