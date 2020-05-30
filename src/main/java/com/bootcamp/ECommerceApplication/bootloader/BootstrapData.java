package com.bootcamp.ECommerceApplication.bootloader;

import com.bootcamp.ECommerceApplication.entity.*;
import com.bootcamp.ECommerceApplication.repository.*;
import com.bootcamp.ECommerceApplication.service.ImageUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class BootstrapData {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageUploaderService imageUploaderService;
    @Autowired
    private ProductVariationRepository productVariationRepository;

    //-----------ROLES TABLE DATA-------------
    public void addRoles() {
        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setAuthority("ROLE_SELLER");
        Role role3 = new Role();
        role3.setAuthority("ROLE_CUSTOMER");
        roleRepository.save(role);
        roleRepository.save(role2);
        roleRepository.save(role3);
    }

    //-----------ADMIN DATA-----------------------
    public void addAdmin() {
        User user = new User();
        user.setEmail("aayushi.thani@tothenew.com");
        user.setFirstName("Aayushi");
        user.setLastName("Thani");
        user.setPassword(passwordEncoder.encode("Aayushi12#"));
        user.setActive(true);
        user.setDeleted(false);

        ArrayList<Role> tempRole = new ArrayList<>();
        Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN");
        tempRole.add(adminRole);
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
    }

    public void addData(){
//---------SELLER DATA--------------------------------------------------------------------------------------------------
        //SELLER 1
        Seller seller1 = new Seller();
        seller1.setEmail("aayushithani@yahoo.in");
        seller1.setFirstName("Mohan");
        seller1.setLastName("Sharma");
        seller1.setPassword(passwordEncoder.encode("Aayushi12#"));
        seller1.setActive(true);
        seller1.setDeleted(false);
        seller1.setCompanyName("Company Name 1");
        seller1.setCompanyContact("8130170780");
        seller1.setGst("37adapm1724a2Z6");

        ArrayList<Role> tempRole2 = new ArrayList<>();
        Role sellerRole = roleRepository.findByAuthority("ROLE_SELLER");
        tempRole2.add(sellerRole);
        seller1.setRoles(tempRole2);
        seller1.setCreatedBy(seller1.getFirstName());
        seller1.setDateCreated(new Date());
        userRepository.save(seller1);

        Address sellerAddress = new Address();
        sellerAddress.setCity("Mumbai");
        sellerAddress.setState("Maharashtra");
        sellerAddress.setCountry("India");
        sellerAddress.setAddress("AU-Block");
        sellerAddress.setZipCode(110999);
        sellerAddress.setLabel("Office");
        sellerAddress.setUser(seller1);
        addressRepository.save(sellerAddress);

        //SELLER 2
        Seller seller2 = new Seller();
        seller2.setEmail("something.thani@yahoo.in");
        seller2.setFirstName("Suresh");
        seller2.setLastName("Arora");
        seller2.setPassword(passwordEncoder.encode("Aayushi12#"));
        seller2.setActive(true);
        seller2.setDeleted(false);
        seller2.setCompanyName("Company Name 2");
        seller2.setCompanyContact("9930198680");
        seller2.setGst("27adapm1924a2Z2");

        ArrayList<Role> tempRole3 = new ArrayList<>();
        Role sellerRole2 = roleRepository.findByAuthority("ROLE_SELLER");
        tempRole3.add(sellerRole2);
        seller2.setRoles(tempRole3);
        seller2.setCreatedBy(seller2.getFirstName());
        seller2.setDateCreated(new Date());
        userRepository.save(seller2);
        Address sellerAddress2 = new Address();
        sellerAddress2.setCity("Pune");
        sellerAddress2.setState("Maharashtra");
        sellerAddress2.setCountry("India");
        sellerAddress2.setAddress("BU-Block");
        sellerAddress2.setZipCode(110989);
        sellerAddress2.setLabel("Office");
        sellerAddress2.setUser(seller2);
        addressRepository.save(sellerAddress2);


//------------ROOT CATEGORY---------------------------------------------------------------------------------------------
        Category electronics = new Category();
        electronics.setName("Electronics");
        categoryRepository.save(electronics);

//------------SUB CATEGORY LEVEL-1----------------------
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
//------------SUB CATEGORY LEVEL-2 Electronics----------
//-------------SUBCATEGORY:MOBILE-----------------------
        Category touchMobile = new Category();
        touchMobile.setName("Touch Screen Mobile");
        touchMobile.setParent(mobile);
        touchMobile.setLeafNode(true);
        categoryRepository.save(touchMobile);

        Category featurePhones = new Category();
        featurePhones.setName("Feature Phones");
        featurePhones.setParent(mobile);
        featurePhones.setLeafNode(true);
        categoryRepository.save(featurePhones);

//-------------SUBCATEGORY:CAMERA-----------------------
        Category dslrCamera = new Category();
        dslrCamera.setName("DSLR Camera");
        dslrCamera.setParent(camera);
        dslrCamera.setLeafNode(true);
        categoryRepository.save(dslrCamera);

        Category sportsCamera = new Category();
        sportsCamera.setName("360 Camera");
        sportsCamera.setParent(camera);
        sportsCamera.setLeafNode(true);
        categoryRepository.save(sportsCamera);

//-------------SUBCATEGORY:WATCH------------------------
        Category casualWatches = new Category();
        casualWatches.setName("Casual Watches");
        casualWatches.setParent(watch);
        casualWatches.setLeafNode(true);
        categoryRepository.save(casualWatches);

        Category smartWatch = new Category();
        smartWatch.setName("Smart Watches");
        smartWatch.setParent(watch);
        smartWatch.setLeafNode(true);
        categoryRepository.save(smartWatch);

//-------------SUBCATEGORY:LAPTOP-----------------------
        Category businessLaptops = new Category();
        businessLaptops.setName("Business Laptops");
        businessLaptops.setParent(laptop);
        businessLaptops.setLeafNode(true);
        categoryRepository.save(businessLaptops);

        Category gamingLaptop = new Category();
        gamingLaptop.setName("Gaming Laptops");
        gamingLaptop.setParent(laptop);
        gamingLaptop.setLeafNode(true);
        categoryRepository.save(gamingLaptop);


//------------ROOT CATEGORY---------------------------------------------------------------------------------------------
        Category fashion = new Category();
        fashion.setName("Fashion");
        categoryRepository.save(fashion);

//------------SUB CATEGORY LEVEL-1----------------------
//-------------SUBCATEGORY:Fashion----------------------
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
//------------SUB CATEGORY LEVEL-2 FASHION----------
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

//-------------SUBCATEGORY: KID SUBCATEGORY--------------
        Category kidShoes = new Category();
        kidShoes.setName("Kid Shoes");
        kidShoes.setParent(kids);
        kidShoes.setLeafNode(true);
        categoryRepository.save(kidShoes);

        Category kidShirts = new Category();
        kidShirts.setName("Kid Shirts");
        kidShirts.setParent(kids);
        kidShirts.setLeafNode(true);
        categoryRepository.save(kidShirts);


//------------ROOT CATEGORY---------------------------------------------------------------------------------------------
        Category furniture = new Category();
        furniture.setName("Furniture");
        categoryRepository.save(furniture);

//------------SUB CATEGORY LEVEL-2 Home & Furniture-----
//-------------SUBCATEGORY:Furniture--------------------
        Category diningFurniture = new Category();
        diningFurniture.setName("Dining Room Furniture");
        diningFurniture.setParent(furniture);
        diningFurniture.setLeafNode(true);
        categoryRepository.save(diningFurniture);

        Category bedRoomFurniture = new Category();
        bedRoomFurniture.setName("Bed Room Furniture");
        bedRoomFurniture.setParent(furniture);
        bedRoomFurniture.setLeafNode(true);
        categoryRepository.save(bedRoomFurniture);

//--------------------- ADD DATA TO PRODUCT TABLE------------------------------------------------------
        //MEN SHOES PRODUCTS
        Product sportsWear = new Product();
        sportsWear.setName("Men Sports Wear Shoes");
        sportsWear.setDescription("These are men sports wear shoes.");
        sportsWear.setCategory(menShoes);
        sportsWear.setBrand("Woodland");
        sportsWear.setCancellable(true);
        sportsWear.setReturnable(true);
        sportsWear.setActive(true);
        sportsWear.setSeller(seller1);
        productRepository.save(sportsWear);

        Product causalWear = new Product();
        causalWear.setName("Men Casual Wear Shoes");
        causalWear.setDescription("These are men casual wear shoes.");
        causalWear.setCategory(menShoes);
        causalWear.setBrand("Puma");
        causalWear.setCancellable(true);
        causalWear.setReturnable(true);
        causalWear.setActive(true);
        causalWear.setSeller(seller2);
        productRepository.save(causalWear);

        Product formalWear = new Product();
        formalWear.setName("Men Formal Wear Shoes");
        formalWear.setDescription("These are men formal wear shoes.");
        formalWear.setCategory(menShoes);
        formalWear.setBrand("Bata");
        formalWear.setCancellable(true);
        formalWear.setReturnable(true);
        formalWear.setActive(true);
        formalWear.setSeller(seller1);
        productRepository.save(formalWear);

        //MEN SHIRTS PRODUCTS
        Product casualShirts = new Product();
        casualShirts.setName("Men Casual Shirts");
        casualShirts.setDescription("These are men casual wear shirts.");
        casualShirts.setCategory(menShirts);
        casualShirts.setBrand("US Polo");
        casualShirts.setCancellable(true);
        casualShirts.setReturnable(true);
        casualShirts.setActive(true);
        casualShirts.setSeller(seller2);
        productRepository.save(casualShirts);

        Product formalShirts = new Product();
        formalShirts.setName("Men formal Shirts");
        formalShirts.setDescription("These are men formal wear shirts.");
        formalShirts.setCategory(menShirts);
        formalShirts.setBrand("Van Heusen");
        formalShirts.setCancellable(true);
        formalShirts.setReturnable(true);
        formalShirts.setActive(true);
        formalShirts.setSeller(seller1);
        productRepository.save(formalShirts);

        Product poloShirts = new Product();
        poloShirts.setName("Men Polo Shirts");
        poloShirts.setDescription("These are men polo wear shirts.");
        poloShirts.setCategory(menShirts);
        poloShirts.setBrand("US Polo");
        poloShirts.setCancellable(true);
        poloShirts.setReturnable(true);
        poloShirts.setActive(true);
        poloShirts.setSeller(seller2);
        productRepository.save(poloShirts);

        //WOMEN SHOES PRODUCTS
        Product sportsWearWomen = new Product();
        sportsWearWomen.setName("Women Sports Shoes");
        sportsWearWomen.setDescription("These are women sports wear shoes.");
        sportsWearWomen.setCategory(womenShoes);
        sportsWearWomen.setBrand("Woodland");
        sportsWearWomen.setCancellable(true);
        sportsWearWomen.setReturnable(true);
        sportsWearWomen.setActive(true);
        sportsWearWomen.setSeller(seller1);
        productRepository.save(sportsWearWomen);

        Product causalWearWomen = new Product();
        causalWearWomen.setName("Women Casual Shoes");
        causalWearWomen.setDescription("These are women casual wear shoes.");
        causalWearWomen.setCategory(womenShoes);
        causalWearWomen.setBrand("Flite");
        causalWearWomen.setCancellable(true);
        causalWearWomen.setReturnable(true);
        causalWearWomen.setActive(true);
        causalWearWomen.setSeller(seller2);
        productRepository.save(causalWearWomen);

        Product formalWearWomen = new Product();
        formalWearWomen.setName("Women Formal Shoes");
        formalWearWomen.setDescription("These are women formal wear shoes.");
        formalWearWomen.setCategory(womenShoes);
        formalWearWomen.setBrand("Bata");
        formalWearWomen.setCancellable(true);
        formalWearWomen.setReturnable(true);
        formalWearWomen.setActive(true);
        formalWearWomen.setSeller(seller1);
        productRepository.save(formalWearWomen);

        //WOMEN SHIRTS PRODUCTS
        Product casualShirtsWomen = new Product();
        casualShirtsWomen.setName("Women Casual Shirts");
        casualShirtsWomen.setDescription("These are women casual wear shirts.");
        casualShirtsWomen.setCategory(womenShirts);
        casualShirtsWomen.setBrand("VERO MODA");
        casualShirtsWomen.setCancellable(true);
        casualShirtsWomen.setReturnable(true);
        casualShirtsWomen.setActive(true);
        casualShirtsWomen.setSeller(seller2);
        productRepository.save(casualShirtsWomen);

        Product formalShirtsWomen = new Product();
        formalShirtsWomen.setName("Women Formal Shirts");
        formalShirtsWomen.setDescription("These are women formal wear shirts.");
        formalShirtsWomen.setCategory(womenShirts);
        formalShirtsWomen.setBrand("Van Heusen");
        formalShirtsWomen.setCancellable(true);
        formalShirtsWomen.setReturnable(true);
        formalShirtsWomen.setActive(true);
        formalShirtsWomen.setSeller(seller1);
        productRepository.save(formalShirtsWomen);

        Product poloShirtsWomen = new Product();
        poloShirtsWomen.setName("Women Polo Shirts");
        poloShirtsWomen.setDescription("These are women polo wear shirts.");
        poloShirtsWomen.setCategory(womenShirts);
        poloShirtsWomen.setBrand("US Polo");
        poloShirtsWomen.setCancellable(true);
        poloShirtsWomen.setReturnable(true);
        poloShirtsWomen.setActive(true);
        poloShirtsWomen.setSeller(seller1);
        productRepository.save(poloShirtsWomen);

        //KIDS SHOES PRODUCTS
        Product sportsWearKids = new Product();
        sportsWearKids.setName("Kids Sports Shoes");
        sportsWearKids.setDescription("These are kids sports wear shoes.");
        sportsWearKids.setCategory(kidShoes);
        sportsWearKids.setBrand("Woodland");
        sportsWearKids.setCancellable(true);
        sportsWearKids.setReturnable(true);
        sportsWearKids.setActive(true);
        sportsWearKids.setSeller(seller2);
        productRepository.save(sportsWearKids);

        Product causalWearKids = new Product();
        causalWearKids.setName("Kids Casual Shoes");
        causalWearKids.setDescription("These are kids casual wear shoes.");
        causalWearKids.setCategory(kidShoes);
        causalWearKids.setBrand("Puma");
        causalWearKids.setCancellable(true);
        causalWearKids.setReturnable(true);
        causalWearKids.setActive(true);
        causalWearKids.setSeller(seller1);
        productRepository.save(causalWearKids);

        //KIDS SHIRTS PRODUCTS
        Product casualShirtsKids = new Product();
        casualShirtsKids.setName("Kids Casual Shirts");
        casualShirtsKids.setDescription("These are kids casual wear shirts.");
        casualShirtsKids.setCategory(kidShirts);
        casualShirtsKids.setBrand("Woodland");
        casualShirtsKids.setCancellable(true);
        casualShirtsKids.setReturnable(true);
        casualShirtsKids.setActive(true);
        casualShirtsKids.setSeller(seller1);
        productRepository.save(casualShirtsKids);

        Product poloShirtsKids = new Product();
        poloShirtsKids.setName("Kids Polo Shirts");
        poloShirtsKids.setDescription("These are kids polo wear shirts.");
        poloShirtsKids.setCategory(kidShirts);
        poloShirtsKids.setBrand("US Polo");
        poloShirtsKids.setCancellable(true);
        poloShirtsKids.setReturnable(true);
        poloShirtsKids.setActive(true);
        poloShirtsKids.setSeller(seller1);
        productRepository.save(poloShirtsKids);

        //BUSINESS LAPTOPS
        Product businessLaptopDell = new Product();
        businessLaptopDell.setName("Dell Business Laptops");
        businessLaptopDell.setDescription("These are Dell Business Laptops");
        businessLaptopDell.setCategory(businessLaptops);
        businessLaptopDell.setBrand("Dell");
        businessLaptopDell.setCancellable(true);
        businessLaptopDell.setReturnable(true);
        businessLaptopDell.setActive(true);
        businessLaptopDell.setSeller(seller2);
        productRepository.save(businessLaptopDell);

        Product businessLaptopLenovo = new Product();
        businessLaptopLenovo.setName("Lenovo Business Laptops");
        businessLaptopLenovo.setDescription("These are Lenovo Business Laptops");
        businessLaptopLenovo.setCategory(businessLaptops);
        businessLaptopLenovo.setBrand("Lenovo");
        businessLaptopLenovo.setCancellable(true);
        businessLaptopLenovo.setReturnable(true);
        businessLaptopLenovo.setActive(true);
        businessLaptopLenovo.setSeller(seller1);
        productRepository.save(businessLaptopLenovo);

        Product businessLaptopHp = new Product();
        businessLaptopHp.setName("HP Business Laptops");
        businessLaptopHp.setDescription("These are HP Business Laptops");
        businessLaptopHp.setCategory(businessLaptops);
        businessLaptopHp.setBrand("HP");
        businessLaptopHp.setCancellable(true);
        businessLaptopHp.setReturnable(true);
        businessLaptopHp.setActive(true);
        businessLaptopHp.setSeller(seller1);
        productRepository.save(businessLaptopHp);

        //GAMING LAPTOPS
        Product gamingLaptopAsus = new Product();
        gamingLaptopAsus.setName("ASUS Gaming Laptops");
        gamingLaptopAsus.setDescription("These are ASUS Gaming Laptops");
        gamingLaptopAsus.setCategory(gamingLaptop);
        gamingLaptopAsus.setBrand("Asus");
        gamingLaptopAsus.setCancellable(true);
        gamingLaptopAsus.setReturnable(true);
        gamingLaptopAsus.setActive(true);
        gamingLaptopAsus.setSeller(seller1);
        productRepository.save(gamingLaptopAsus);

        Product gamingLaptopHp = new Product();
        gamingLaptopHp.setName("HP Gaming Laptops");
        gamingLaptopHp.setDescription("These are HP Gaming Laptops");
        gamingLaptopHp.setCategory(gamingLaptop);
        gamingLaptopHp.setBrand("HP");
        gamingLaptopHp.setCancellable(true);
        gamingLaptopHp.setReturnable(true);
        gamingLaptopHp.setActive(true);
        gamingLaptopHp.setSeller(seller1);
        productRepository.save(gamingLaptopHp);

        //Casual Watches
        Product casualWatchTitan = new Product();
        casualWatchTitan.setName("Titan Casual Watches");
        casualWatchTitan.setDescription("These are Titan Casual Watches");
        casualWatchTitan.setCategory(casualWatches);
        casualWatchTitan.setBrand("Titan");
        casualWatchTitan.setCancellable(true);
        casualWatchTitan.setReturnable(true);
        casualWatchTitan.setActive(true);
        casualWatchTitan.setSeller(seller2);
        productRepository.save(casualWatchTitan);

        Product casualWatchRado = new Product();
        casualWatchRado.setName("Rado Casual Watches");
        casualWatchRado.setDescription("These are Rado Casual Watches");
        casualWatchRado.setCategory(casualWatches);
        casualWatchRado.setBrand("Rado");
        casualWatchRado.setCancellable(true);
        casualWatchRado.setReturnable(true);
        casualWatchRado.setActive(true);
        casualWatchRado.setSeller(seller1);
        productRepository.save(casualWatchRado);

        //Smart Watches
        Product smartWatchMiBand = new Product();
        smartWatchMiBand.setName("MI Band");
        smartWatchMiBand.setDescription("These are MI Band");
        smartWatchMiBand.setCategory(smartWatch);
        smartWatchMiBand.setBrand("MI Band");
        smartWatchMiBand.setCancellable(true);
        smartWatchMiBand.setReturnable(true);
        smartWatchMiBand.setActive(true);
        smartWatchMiBand.setSeller(seller1);
        productRepository.save(smartWatchMiBand);

        Product smartWatchAmazonFit = new Product();
        smartWatchAmazonFit.setName("Amazon Fit");
        smartWatchAmazonFit.setDescription("These are Amazon Fit");
        smartWatchAmazonFit.setCategory(smartWatch);
        smartWatchAmazonFit.setBrand("Amazon Fit");
        smartWatchAmazonFit.setCancellable(true);
        smartWatchAmazonFit.setReturnable(true);
        smartWatchAmazonFit.setActive(true);
        smartWatchAmazonFit.setSeller(seller1);
        productRepository.save(smartWatchAmazonFit);

        //DSLR Camera
        Product dslrCameraNikon = new Product();
        dslrCameraNikon.setName("Nikon DSLR Camera");
        dslrCameraNikon.setDescription("These are Nikon DSLR Camera");
        dslrCameraNikon.setCategory(dslrCamera);
        dslrCameraNikon.setBrand("Nikon");
        dslrCameraNikon.setCancellable(true);
        dslrCameraNikon.setReturnable(true);
        dslrCameraNikon.setActive(true);
        dslrCameraNikon.setSeller(seller1);
        productRepository.save(dslrCameraNikon);

        Product dslrCameraSony = new Product();
        dslrCameraSony.setName("SONY DSLR Camera");
        dslrCameraSony.setDescription("These are SONY DSLR Camera");
        dslrCameraSony.setCategory(dslrCamera);
        dslrCameraSony.setBrand("Sony");
        dslrCameraSony.setCancellable(true);
        dslrCameraSony.setReturnable(true);
        dslrCameraSony.setActive(true);
        dslrCameraSony.setSeller(seller1);
        productRepository.save(dslrCameraSony);

        //360 Camera
        Product sportsCameraDJI = new Product();
        sportsCameraDJI.setName("DJI 360 Camera");
        sportsCameraDJI.setDescription("These are DJI 360 Camera");
        sportsCameraDJI.setCategory(sportsCamera);
        sportsCameraDJI.setBrand("DJI");
        sportsCameraDJI.setCancellable(true);
        sportsCameraDJI.setReturnable(true);
        sportsCameraDJI.setActive(true);
        sportsCameraDJI.setSeller(seller1);
        productRepository.save(sportsCameraDJI);

        //Touch Screen Phones
        Product touchMobileSamsung = new Product();
        touchMobileSamsung.setName("Samsung Mobile");
        touchMobileSamsung.setDescription("These are Samsung Mobile");
        touchMobileSamsung.setCategory(touchMobile);
        touchMobileSamsung.setBrand("Samsung");
        touchMobileSamsung.setCancellable(true);
        touchMobileSamsung.setReturnable(true);
        touchMobileSamsung.setActive(true);
        touchMobileSamsung.setSeller(seller2);
        productRepository.save(touchMobileSamsung);

        Product touchMobileApple = new Product();
        touchMobileApple.setName("Apple iPhone");
        touchMobileApple.setDescription("These are Apple iPhone");
        touchMobileApple.setCategory(touchMobile);
        touchMobileApple.setBrand("Apple");
        touchMobileApple.setCancellable(true);
        touchMobileApple.setReturnable(true);
        touchMobileApple.setActive(true);
        touchMobileApple.setSeller(seller1);
        productRepository.save(touchMobileApple);

        //Feature Phones
        Product featurePhoneNokia = new Product();
        featurePhoneNokia.setName("Nokia Phones");
        featurePhoneNokia.setDescription("These are Nokia Phones");
        featurePhoneNokia.setCategory(featurePhones);
        featurePhoneNokia.setBrand("Nokia");
        featurePhoneNokia.setCancellable(true);
        featurePhoneNokia.setReturnable(true);
        featurePhoneNokia.setActive(true);
        featurePhoneNokia.setSeller(seller2);
        productRepository.save(featurePhoneNokia);

//-------------------PRODUCT-VARIATION TABLE DATA-----------------------------------------------------------

        ProductVariation sportsWear1 = new ProductVariation();
        sportsWear1.setProduct(sportsWear);
        sportsWear1.setQuantityAvailable(10);
        sportsWear1.setPrice(1000.00);
        sportsWear1.setActive(true);

        ProductVariation sportsWear2 = new ProductVariation();
        sportsWear2.setProduct(sportsWear);
        sportsWear2.setQuantityAvailable(10);
        sportsWear2.setPrice(1000.00);
        sportsWear2.setActive(true);

        ProductVariation sportsWear3 = new ProductVariation();
        sportsWear3.setProduct(sportsWear);
        sportsWear3.setQuantityAvailable(10);
        sportsWear3.setPrice(1000.00);
        sportsWear3.setActive(true);

        ProductVariation sportsWear4 = new ProductVariation();
        sportsWear4.setProduct(sportsWear);
        sportsWear4.setQuantityAvailable(10);
        sportsWear4.setPrice(1000.00);
        sportsWear4.setActive(true);




    }
}