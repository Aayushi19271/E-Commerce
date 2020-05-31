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
        mobile.setLeafNode(true);
        categoryRepository.save(mobile);

        Category camera = new Category();
        camera.setName("Camera");
        camera.setParent(electronics);
        camera.setLeafNode(true);
        categoryRepository.save(camera);

        Category watch = new Category();
        watch.setName("Watch");
        watch.setParent(electronics);
        watch.setLeafNode(true);
        categoryRepository.save(watch);

        Category laptop = new Category();
        laptop.setName("Laptop");
        laptop.setParent(electronics);
        laptop.setLeafNode(true);
        categoryRepository.save(laptop);

//------------ROOT CATEGORY---------------------------------------------------------------------------------------------
        Category fashion = new Category();
        fashion.setName("Fashion");
        categoryRepository.save(fashion);

//------------SUB CATEGORY LEVEL-1----------------------
//-------------SUBCATEGORY:Fashion----------------------
//        Category men = new Category();
//        men.setName("Men");
//        men.setParent(fashion);
//        men.setLeafNode(true);
//        categoryRepository.save(men);
//
//        Category women = new Category();
//        women.setName("Women");
//        women.setParent(fashion);
//        women.setLeafNode(true);
//        categoryRepository.save(women);

//        Category kids = new Category();
//        kids.setName("Kids");
//        kids.setParent(fashion);
//        categoryRepository.save(kids);
//------------SUB CATEGORY LEVEL-2 FASHION----------
//-------------SUBCATEGORY:MEN SUBCATEGORY----------
        Category menShoes = new Category();
        menShoes.setName("Men Shoes");
        menShoes.setParent(fashion);
        menShoes.setLeafNode(true);
        categoryRepository.save(menShoes);

        Category menShirts = new Category();
        menShirts.setName("Men Shirts");
        menShirts.setParent(fashion);
        menShirts.setLeafNode(true);
        categoryRepository.save(menShirts);

//-------------SUBCATEGORY: WOMEN SUBCATEGORY------------
        Category womenShoes = new Category();
        womenShoes.setName("Women Shoes");
        womenShoes.setParent(fashion);
        womenShoes.setLeafNode(true);
        categoryRepository.save(womenShoes);

        Category womenShirts = new Category();
        womenShirts.setName("Women Shirts");
        womenShirts.setParent(fashion);
        womenShirts.setLeafNode(true);
        categoryRepository.save(womenShirts);

////-------------SUBCATEGORY: KID SUBCATEGORY--------------
//        Category kidShoes = new Category();
//        kidShoes.setName("Kid Shoes");
//        kidShoes.setParent(kids);
//        kidShoes.setLeafNode(true);
//        categoryRepository.save(kidShoes);
//
//        Category kidShirts = new Category();
//        kidShirts.setName("Kid Shirts");
//        kidShirts.setParent(kids);
//        kidShirts.setLeafNode(true);
//        categoryRepository.save(kidShirts);


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
        sportsWear.setName("WOODLAND Shoes");
        sportsWear.setDescription("Sleek and stylish leather boots which match the royal urban lifestyle and let you demonstrate its durability when you tread on rocky terrains.");
        sportsWear.setCategory(menShoes);
        sportsWear.setBrand("Woodland");
        sportsWear.setCancellable(true);
        sportsWear.setReturnable(true);
        sportsWear.setActive(true);
        sportsWear.setSeller(seller1);
        productRepository.save(sportsWear);

        Product sportsWearPuma = new Product();
        sportsWearPuma.setName("Puma Sports Wear");
        sportsWearPuma.setDescription("Puma is one of the world’s leading sports brands, designing, developing, selling and marketing footwear, apparel and accessories.");
        sportsWearPuma.setCategory(menShoes);
        sportsWearPuma.setBrand("Puma");
        sportsWearPuma.setCancellable(true);
        sportsWearPuma.setReturnable(true);
        sportsWearPuma.setActive(true);
        sportsWearPuma.setSeller(seller2);
        productRepository.save(sportsWearPuma);

        Product formalWear = new Product();
        formalWear.setName("BATA Shoes");
        formalWear.setDescription("Bata India is the largest and leading manufacturer of footwear in India and is a part of the Bata Shoe Organization.");
        formalWear.setCategory(menShoes);
        formalWear.setBrand("Bata");
        formalWear.setCancellable(true);
        formalWear.setReturnable(true);
        formalWear.setActive(true);
        formalWear.setSeller(seller1);
        productRepository.save(formalWear);

        //MEN SHIRTS PRODUCTS
        Product casualShirts = new Product();
        casualShirts.setName("Arrow Shirts");
        casualShirts.setDescription("Arrow urban slim fit shirt pink.");
        casualShirts.setCategory(menShirts);
        casualShirts.setBrand("Arrow");
        casualShirts.setCancellable(true);
        casualShirts.setReturnable(true);
        casualShirts.setActive(true);
        casualShirts.setSeller(seller2);
        productRepository.save(casualShirts);

        Product formalShirts = new Product();
        formalShirts.setName("Van Heusen Shirts");
        formalShirts.setDescription("Van Heusen shirts offers an array of products perfect for any occasions. Upgrade your wardrobe with this white with dark blue print shirt.");
        formalShirts.setCategory(menShirts);
        formalShirts.setBrand("Van Heusen");
        formalShirts.setCancellable(true);
        formalShirts.setReturnable(true);
        formalShirts.setActive(true);
        formalShirts.setSeller(seller1);
        productRepository.save(formalShirts);

        Product casualShirts2 = new Product();
        casualShirts2.setName("US Polo Shirts");
        casualShirts2.setDescription("Uspa core t-shirt of regular fit and colour sunshine made of 100% cotton the US polo assn, brand captures the authenticity of the sport.");
        casualShirts2.setCategory(menShirts);
        casualShirts2.setBrand("US Polo");
        casualShirts2.setCancellable(true);
        casualShirts2.setReturnable(true);
        casualShirts2.setActive(true);
        casualShirts2.setSeller(seller1);
        productRepository.save(casualShirts2);

        //WOMEN SHOES PRODUCTS
        Product sportsWearWomen = new Product();
        sportsWearWomen.setName("PUMA Women Shoes");
        sportsWearWomen.setDescription("The pink dwane slip-on idp is from the running shoes collection from Puma. Comprising of stylish mesh upper and EVA sole this product is lightweight and durable, designed to perfect your performance.");
        sportsWearWomen.setCategory(womenShoes);
        sportsWearWomen.setBrand("PUMA");
        sportsWearWomen.setCancellable(true);
        sportsWearWomen.setReturnable(true);
        sportsWearWomen.setActive(true);
        sportsWearWomen.setSeller(seller1);
        productRepository.save(sportsWearWomen);

        Product causalWearWomen = new Product();
        causalWearWomen.setName("Crocs Women");
        causalWearWomen.setDescription("designed for all-day comfort make a comfy crocs statement with the crocs Baya clog. A twist on our signature classic clog, it features the lightweight.");
        causalWearWomen.setCategory(womenShoes);
        causalWearWomen.setBrand("Crocs");
        causalWearWomen.setCancellable(true);
        causalWearWomen.setReturnable(true);
        causalWearWomen.setActive(true);
        causalWearWomen.setSeller(seller2);
        productRepository.save(causalWearWomen);

        //WOMEN SHIRTS PRODUCTS
        Product casualShirtsWomen = new Product();
        casualShirtsWomen.setName("VERO MODA Women Shirts");
        casualShirtsWomen.setDescription("Vero Moda Women White Solid Ruffled Sleeve Off Shoulder Neck Regular Fit Casual X-Small Top");
        casualShirtsWomen.setCategory(womenShirts);
        casualShirtsWomen.setBrand("VERO MODA");
        casualShirtsWomen.setCancellable(true);
        casualShirtsWomen.setReturnable(true);
        casualShirtsWomen.setActive(true);
        casualShirtsWomen.setSeller(seller2);
        productRepository.save(casualShirtsWomen);

        Product formalShirtsWomen = new Product();
        formalShirtsWomen.setName("Van Heusen Women Shirts");
        formalShirtsWomen.setDescription("This printed Black top from Van Heusen woman is designed exclusively for a classy look. Tailored for a comfort fit, this round-neck design is crafted from fine polyester.");
        formalShirtsWomen.setCategory(womenShirts);
        formalShirtsWomen.setBrand("Van Heusen");
        formalShirtsWomen.setCancellable(true);
        formalShirtsWomen.setReturnable(true);
        formalShirtsWomen.setActive(true);
        formalShirtsWomen.setSeller(seller1);
        productRepository.save(formalShirtsWomen);

        Product poloShirtsWomen = new Product();
        poloShirtsWomen.setName("US Polo Women Shirts");
        poloShirtsWomen.setDescription("U.S. Polo Assn. Women Cotton Blend Floral Print Blue T-Shirt");
        poloShirtsWomen.setCategory(womenShirts);
        poloShirtsWomen.setBrand("US Polo");
        poloShirtsWomen.setCancellable(true);
        poloShirtsWomen.setReturnable(true);
        poloShirtsWomen.setActive(true);
        poloShirtsWomen.setSeller(seller1);
        productRepository.save(poloShirtsWomen);

//        //KIDS SHOES PRODUCTS
//        Product sportsWearKids = new Product();
//        sportsWearKids.setName("Kids Sports Shoes");
//        sportsWearKids.setDescription("These are kids sports wear shoes.");
//        sportsWearKids.setCategory(kidShoes);
//        sportsWearKids.setBrand("Woodland");
//        sportsWearKids.setCancellable(true);
//        sportsWearKids.setReturnable(true);
//        sportsWearKids.setActive(true);
//        sportsWearKids.setSeller(seller2);
//        productRepository.save(sportsWearKids);
//
//        Product causalWearKids = new Product();
//        causalWearKids.setName("Kids Casual Shoes");
//        causalWearKids.setDescription("These are kids casual wear shoes.");
//        causalWearKids.setCategory(kidShoes);
//        causalWearKids.setBrand("Puma");
//        causalWearKids.setCancellable(true);
//        causalWearKids.setReturnable(true);
//        causalWearKids.setActive(true);
//        causalWearKids.setSeller(seller1);
//        productRepository.save(causalWearKids);
//
//        //KIDS SHIRTS PRODUCTS
//        Product casualShirtsKids = new Product();
//        casualShirtsKids.setName("Kids Casual Shirts");
//        casualShirtsKids.setDescription("These are kids casual wear shirts.");
//        casualShirtsKids.setCategory(kidShirts);
//        casualShirtsKids.setBrand("Woodland");
//        casualShirtsKids.setCancellable(true);
//        casualShirtsKids.setReturnable(true);
//        casualShirtsKids.setActive(true);
//        casualShirtsKids.setSeller(seller1);
//        productRepository.save(casualShirtsKids);
//
//        Product poloShirtsKids = new Product();
//        poloShirtsKids.setName("Kids Polo Shirts");
//        poloShirtsKids.setDescription("These are kids polo wear shirts.");
//        poloShirtsKids.setCategory(kidShirts);
//        poloShirtsKids.setBrand("US Polo");
//        poloShirtsKids.setCancellable(true);
//        poloShirtsKids.setReturnable(true);
//        poloShirtsKids.setActive(true);
//        poloShirtsKids.setSeller(seller1);
//        productRepository.save(poloShirtsKids);

        //BUSINESS LAPTOPS

        Product businessLaptopDell = new Product();
        businessLaptopDell.setName("Dell Laptops");
        businessLaptopDell.setDescription("Intel Core i5-8265U,8GB DDR4,1TB SATA,2GB AMD Radeon 520. Dell Inspiron 3583 Laptop (i5-8265U,8GB DDR4,1TB SATA).");
        businessLaptopDell.setCategory(laptop);
        businessLaptopDell.setBrand("Dell");
        businessLaptopDell.setCancellable(true);
        businessLaptopDell.setReturnable(true);
        businessLaptopDell.setActive(true);
        businessLaptopDell.setSeller(seller2);
        productRepository.save(businessLaptopDell);

        Product businessLaptopLenovo = new Product();
        businessLaptopLenovo.setName("Lenovo Laptops");
        businessLaptopLenovo.setDescription("Comfortable confidence. Upto 10th Gen Intel Core processors provide speed & power. Premium Dolby Audio provides a rich audio experience. Added security with physical webcam shutter.");
        businessLaptopLenovo.setCategory(laptop);
        businessLaptopLenovo.setBrand("Lenovo");
        businessLaptopLenovo.setCancellable(true);
        businessLaptopLenovo.setReturnable(true);
        businessLaptopLenovo.setActive(true);
        businessLaptopLenovo.setSeller(seller1);
        productRepository.save(businessLaptopLenovo);

        Product businessLaptopHp = new Product();
        businessLaptopHp.setName("HP Laptops");
        businessLaptopHp.setDescription("Express yourself like never before with this extra slim powerhouse built just for you. Stylish enough to follow you anywhere, and powerful enough to make it through any day.");
        businessLaptopHp.setCategory(laptop);
        businessLaptopHp.setBrand("HP");
        businessLaptopHp.setCancellable(true);
        businessLaptopHp.setReturnable(true);
        businessLaptopHp.setActive(true);
        businessLaptopHp.setSeller(seller1);
        productRepository.save(businessLaptopHp);

//        //GAMING LAPTOPS
//        Product gamingLaptopAsus = new Product();
//        gamingLaptopAsus.setName("ASUS Gaming Laptops");
//        gamingLaptopAsus.setDescription("These are ASUS Gaming Laptops");
//        gamingLaptopAsus.setCategory(laptop);
//        gamingLaptopAsus.setBrand("Asus");
//        gamingLaptopAsus.setCancellable(true);
//        gamingLaptopAsus.setReturnable(true);
//        gamingLaptopAsus.setActive(true);
//        gamingLaptopAsus.setSeller(seller1);
//        productRepository.save(gamingLaptopAsus);
//
//        Product gamingLaptopHp = new Product();
//        gamingLaptopHp.setName("HP Gaming Laptops");
//        gamingLaptopHp.setDescription("These are HP Gaming Laptops");
//        gamingLaptopHp.setCategory(laptop);
//        gamingLaptopHp.setBrand("HP");
//        gamingLaptopHp.setCancellable(true);
//        gamingLaptopHp.setReturnable(true);
//        gamingLaptopHp.setActive(true);
//        gamingLaptopHp.setSeller(seller1);
//        productRepository.save(gamingLaptopHp);

        //Casual Watches
        Product casualWatchTitan = new Product();
        casualWatchTitan.setName("Titan Watches");
        casualWatchTitan.setDescription("The Neo Collection by Titan is an indispensable accessory.'  Titan Neo watches are manufactured with a harmonious input of style, technology and durability and are designed for men and women who value class.");
        casualWatchTitan.setCategory(watch);
        casualWatchTitan.setBrand("Titan");
        casualWatchTitan.setCancellable(true);
        casualWatchTitan.setReturnable(true);
        casualWatchTitan.setActive(true);
        casualWatchTitan.setSeller(seller2);
        productRepository.save(casualWatchTitan);

        Product casualWatchRado = new Product();
        casualWatchRado.setName("Rado Watches");
        casualWatchRado.setDescription("The Quartz movement of this watch is powered by a battery, which ensures precise reading. A display window at the 3 o' clock position indicates the date. You do not need to worry about a sudden downpour.");
        casualWatchRado.setCategory(watch);
        casualWatchRado.setBrand("Rado");
        casualWatchRado.setCancellable(true);
        casualWatchRado.setReturnable(true);
        casualWatchRado.setActive(true);
        casualWatchRado.setSeller(seller1);
        productRepository.save(casualWatchRado);

        //Smart Watches
        Product smartWatchFastrack = new Product();
        smartWatchFastrack.setName("Fastrack Smart Watch");
        smartWatchFastrack.setDescription("The fastrack reflex 2.0 tracks the number of steps you have taken, the total distance you have travelled and the number of calories that you have burned. This helps you keep a track of all the action you're getting!");
        smartWatchFastrack.setCategory(watch);
        smartWatchFastrack.setBrand("Fastrack");
        smartWatchFastrack.setCancellable(true);
        smartWatchFastrack.setReturnable(true);
        smartWatchFastrack.setActive(true);
        smartWatchFastrack.setSeller(seller1);
        productRepository.save(smartWatchFastrack);

        //DSLR Camera
        Product dslrCameraNikon = new Product();
        dslrCameraNikon.setName("Nikon DSLR Camera");
        dslrCameraNikon.setDescription("ILCE-6100 comes with Exmor CMOS image Sensor, implemented with the latest BIONZ X image processor, It offers a lightning fast autofocus time of 0.02 seconds with Real-time Eye AF offers AI based object recognition.");
        dslrCameraNikon.setCategory(camera);
        dslrCameraNikon.setBrand("Nikon");
        dslrCameraNikon.setCancellable(true);
        dslrCameraNikon.setReturnable(true);
        dslrCameraNikon.setActive(true);
        dslrCameraNikon.setSeller(seller1);
        productRepository.save(dslrCameraNikon);

        Product dslrCameraSony = new Product();
        dslrCameraSony.setName("SONY DSLR Camera");
        dslrCameraSony.setDescription("ILCE-6100 comes with Exmor CMOS image Sensor, implemented with the latest BIONZ X image processor, It offers a lightning fast autofocus time of 0.02 seconds with Real-time Eye AF offers AI based object recognition.");
        dslrCameraSony.setCategory(camera);
        dslrCameraSony.setBrand("Sony");
        dslrCameraSony.setCancellable(true);
        dslrCameraSony.setReturnable(true);
        dslrCameraSony.setActive(true);
        dslrCameraSony.setSeller(seller1);
        productRepository.save(dslrCameraSony);

        //Touch Screen Phones
        Product mobileOnePlus = new Product();
        mobileOnePlus.setName("OnePlus");
        mobileOnePlus.setDescription("The all new OnePlus 7T Powered by Qualcomm Snapdragon 855 Plus (Octa-core, 7nm, up to 2.96 GHz) with Qualcomm AI Engine. Equipped with 90hz Fluid display, a Triple Rear camera with Telephoto and Ultra wide angel lens.");
        mobileOnePlus.setCategory(mobile);
        mobileOnePlus.setBrand("OnePlus");
        mobileOnePlus.setCancellable(true);
        mobileOnePlus.setReturnable(true);
        mobileOnePlus.setActive(true);
        mobileOnePlus.setSeller(seller2);
        productRepository.save(mobileOnePlus);

        Product mobileApple = new Product();
        mobileApple.setName("Apple iPhone");
        mobileApple.setDescription("Think Different.");
        mobileApple.setCategory(mobile);
        mobileApple.setBrand("Apple");
        mobileApple.setCancellable(true);
        mobileApple.setReturnable(true);
        mobileApple.setActive(true);
        mobileApple.setSeller(seller1);
        productRepository.save(mobileApple);

        //Feature Phones
        Product featurePhoneNokia = new Product();
        featurePhoneNokia.setName("Nokia Phones");
        featurePhoneNokia.setDescription("Talk to everyone with the latest Nokia 105. Long-lasting battery life will help you stay connected even longer, while the modern, ergonomic design will ensure you remain comfortable however long the conversation lasts.");
        featurePhoneNokia.setCategory(mobile);
        featurePhoneNokia.setBrand("Nokia");
        featurePhoneNokia.setCancellable(true);
        featurePhoneNokia.setReturnable(true);
        featurePhoneNokia.setActive(true);
        featurePhoneNokia.setSeller(seller2);
        productRepository.save(featurePhoneNokia);

//-------------------PRODUCT-VARIATION TABLE DATA-----------------------------------------------------------
        //WOODLAND VARIATIONS SPORTS WEAR
        ProductVariation sportsWear1 = new ProductVariation();
        sportsWear1.setProduct(sportsWear);
        sportsWear1.setQuantityAvailable(10);
        sportsWear1.setPrice(2999.00);
        sportsWear1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855091/ProductVariationImages/4_MEN_SPORTS_SHOES_yve091.jpg");
        sportsWear1.setActive(true);
        productVariationRepository.save(sportsWear1);

        ProductVariation sportsWear2 = new ProductVariation();
        sportsWear2.setProduct(sportsWear);
        sportsWear2.setQuantityAvailable(10);
        sportsWear2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855091/ProductVariationImages/3_MEN_SPORTS_SHOES_cn44um.jpg");
        sportsWear2.setPrice(2999.00);
        sportsWear2.setActive(true);
        productVariationRepository.save(sportsWear2);

        ProductVariation sportsWear3 = new ProductVariation();
        sportsWear3.setProduct(sportsWear);
        sportsWear3.setQuantityAvailable(10);
        sportsWear3.setPrice(4000.00);
        sportsWear3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855091/ProductVariationImages/2_MEN_SPORTS_SHOES_ppmhtp.jpg");
        sportsWear3.setActive(true);
        productVariationRepository.save(sportsWear3);


        ProductVariation sportsWear4 = new ProductVariation();
        sportsWear4.setProduct(sportsWear);
        sportsWear4.setQuantityAvailable(10);
        sportsWear4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/1_MEN_SPORTS_SHOES_vko0i4.jpg");
        sportsWear4.setPrice(6000.00);
        sportsWear4.setActive(true);
        productVariationRepository.save(sportsWear4);


        ProductVariation sportsWear5 = new ProductVariation();
        sportsWear5.setProduct(sportsWear);
        sportsWear5.setQuantityAvailable(10);
        sportsWear5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855093/ProductVariationImages/6_MEN_SPORTS_SHOES_kc9tsq.jpg");
        sportsWear5.setPrice(1200.00);
        sportsWear5.setActive(true);
        productVariationRepository.save(sportsWear5);

        ProductVariation sportsWear6 = new ProductVariation();
        sportsWear6.setProduct(sportsWear);
        sportsWear6.setQuantityAvailable(10);
        sportsWear6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855093/ProductVariationImages/5_MEN_SPORTS_SHOES_igbswy.jpg");
        sportsWear6.setPrice(1300.00);
        sportsWear6.setActive(true);
        productVariationRepository.save(sportsWear6);

        //PUMA VARIATIONS SPORTS WEAR
        ProductVariation sportsWearPuma1 = new ProductVariation();
        sportsWearPuma1.setProduct(sportsWearPuma);
        sportsWearPuma1.setQuantityAvailable(10);
        sportsWearPuma1.setPrice(5000.00);
        sportsWearPuma1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/9_MEN_SPORTS_SHOES_evhq13.jpg");
        sportsWearPuma1.setActive(true);
        productVariationRepository.save(sportsWearPuma1);

        ProductVariation sportsWearPuma2 = new ProductVariation();
        sportsWearPuma2.setProduct(sportsWearPuma);
        sportsWearPuma2.setQuantityAvailable(10);
        sportsWearPuma2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/8_MEN_SPORTS_SHOES_nyo0lz.jpg");
        sportsWearPuma2.setPrice(4000.00);
        sportsWearPuma2.setActive(true);
        productVariationRepository.save(sportsWearPuma2);

        ProductVariation sportsWearPuma3 = new ProductVariation();
        sportsWearPuma3.setProduct(sportsWearPuma);
        sportsWearPuma3.setQuantityAvailable(10);
        sportsWearPuma3.setPrice(3400.00);
        sportsWearPuma3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/7_MEN_SPORTS_SHOES_wisn7x.jpg");
        sportsWearPuma3.setActive(true);
        productVariationRepository.save(sportsWearPuma3);

        ProductVariation sportsWearPuma4 = new ProductVariation();
        sportsWearPuma4.setProduct(sportsWearPuma);
        sportsWearPuma4.setQuantityAvailable(10);
        sportsWearPuma4.setPrice(2300.00);
        sportsWearPuma4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855093/ProductVariationImages/13_MEN_SPORTS_SHOES_mxflxo.jpg");
        sportsWearPuma4.setActive(true);
        productVariationRepository.save(sportsWearPuma4);

        ProductVariation sportsWearPuma5 = new ProductVariation();
        sportsWearPuma5.setProduct(sportsWearPuma);
        sportsWearPuma5.setQuantityAvailable(10);
        sportsWearPuma5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/12_MEN_SPORTS_SHOES_x6bsig.jpg");
        sportsWearPuma5.setPrice(4500.00);
        sportsWearPuma5.setActive(true);
        productVariationRepository.save(sportsWearPuma5);

        ProductVariation sportsWearPuma6 = new ProductVariation();
        sportsWearPuma6.setProduct(sportsWearPuma);
        sportsWearPuma6.setQuantityAvailable(10);
        sportsWearPuma6.setPrice(3500.00);
        sportsWearPuma6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/11_MEN_SPORTS_SHOES_dkl2bm.jpg");
        sportsWearPuma6.setActive(true);
        productVariationRepository.save(sportsWearPuma6);

        ProductVariation sportsWearPuma7 = new ProductVariation();
        sportsWearPuma7.setProduct(sportsWearPuma);
        sportsWearPuma7.setQuantityAvailable(10);
        sportsWearPuma7.setPrice(5500.00);
        sportsWearPuma7.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590855092/ProductVariationImages/10_MEN_SPORTS_SHOES_iej0o8.jpg");
        sportsWearPuma7.setActive(true);
        productVariationRepository.save(sportsWearPuma7);


        //BATA VARIATIONS FORMAL WEAR
        ProductVariation formalWearBata1 = new ProductVariation();
        formalWearBata1.setProduct(formalWear);
        formalWearBata1.setQuantityAvailable(10);
        formalWearBata1.setPrice(1200.00);
        formalWearBata1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590860985/ProductVariationImages/BATA_4_txhznc.jpg");
        formalWearBata1.setActive(true);
        productVariationRepository.save(formalWearBata1);

        ProductVariation formalWearBata2 = new ProductVariation();
        formalWearBata2.setProduct(formalWear);
        formalWearBata2.setQuantityAvailable(10);
        formalWearBata2.setPrice(1300.00);
        formalWearBata2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590860979/ProductVariationImages/BATA_6_t6jypl.jpg");
        formalWearBata2.setActive(true);
        productVariationRepository.save(formalWearBata2);

        ProductVariation formalWearBata3 = new ProductVariation();
        formalWearBata3.setProduct(formalWear);
        formalWearBata3.setQuantityAvailable(10);
        formalWearBata3.setPrice(1400.00);
        formalWearBata3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590860976/ProductVariationImages/BATA_5_vtakoz.jpg");
        formalWearBata3.setActive(true);
        productVariationRepository.save(formalWearBata3);

        ProductVariation formalWearBata4 = new ProductVariation();
        formalWearBata4.setProduct(formalWear);
        formalWearBata4.setQuantityAvailable(10);
        formalWearBata4.setPrice(1500.00);
        formalWearBata4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590860975/ProductVariationImages/BATA_1_gcef1l.jpg");
        formalWearBata4.setActive(true);
        productVariationRepository.save(formalWearBata4);

        ProductVariation formalWearBata5 = new ProductVariation();
        formalWearBata5.setProduct(formalWear);
        formalWearBata5.setQuantityAvailable(10);
        formalWearBata5.setPrice(1600.00);
        formalWearBata5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590860974/ProductVariationImages/BATA_3_h9i25c.jpg");
        formalWearBata5.setActive(true);
        productVariationRepository.save(formalWearBata5);

        ProductVariation formalWearBata6 = new ProductVariation();
        formalWearBata6.setProduct(formalWear);
        formalWearBata6.setQuantityAvailable(10);
        formalWearBata6.setPrice(1700.00);
        formalWearBata6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590860974/ProductVariationImages/BATA_2_gbcpp2.jpg");
        formalWearBata6.setActive(true);
        productVariationRepository.save(formalWearBata6);

        //MEN CASUAL SHIRTS
        //ARROW
        ProductVariation casualShirtsArrow = new ProductVariation();
        casualShirtsArrow.setProduct(casualShirts);
        casualShirtsArrow.setQuantityAvailable(10);
        casualShirtsArrow.setPrice(4000.00);
        casualShirtsArrow.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862699/ProductVariationImages/arrow2_esyehl.jpg");
        casualShirtsArrow.setActive(true);
        productVariationRepository.save(casualShirtsArrow);

        ProductVariation casualShirtsArrow2 = new ProductVariation();
        casualShirtsArrow2.setProduct(casualShirts);
        casualShirtsArrow2.setQuantityAvailable(10);
        casualShirtsArrow2.setPrice(5000.00);
        casualShirtsArrow2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862699/ProductVariationImages/arrow3_fundmw.jpg");
        casualShirtsArrow2.setActive(true);
        productVariationRepository.save(casualShirtsArrow2);

        ProductVariation casualShirtsArrow3 = new ProductVariation();
        casualShirtsArrow3.setProduct(casualShirts);
        casualShirtsArrow3.setQuantityAvailable(10);
        casualShirtsArrow3.setPrice(5500.00);
        casualShirtsArrow3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862699/ProductVariationImages/arrow5_k7lrfx.jpg");
        casualShirtsArrow3.setActive(true);
        productVariationRepository.save(casualShirtsArrow3);

        ProductVariation casualShirtsArrow4 = new ProductVariation();
        casualShirtsArrow4.setProduct(casualShirts);
        casualShirtsArrow4.setQuantityAvailable(10);
        casualShirtsArrow4.setPrice(4500.00);
        casualShirtsArrow4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862699/ProductVariationImages/arrow4_cecyyf.jpg");
        casualShirtsArrow4.setActive(true);
        productVariationRepository.save(casualShirtsArrow4);

        ProductVariation casualShirtsArrow5 = new ProductVariation();
        casualShirtsArrow5.setProduct(casualShirts);
        casualShirtsArrow5.setQuantityAvailable(10);
        casualShirtsArrow5.setPrice(5300.00);
        casualShirtsArrow5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862699/ProductVariationImages/arrow6_riw7db.jpg");
        casualShirtsArrow5.setActive(true);
        productVariationRepository.save(casualShirtsArrow5);

        ProductVariation casualShirtsArrow6 = new ProductVariation();
        casualShirtsArrow6.setProduct(casualShirts);
        casualShirtsArrow6.setQuantityAvailable(10);
        casualShirtsArrow6.setPrice(4500.00);
        casualShirtsArrow6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862699/ProductVariationImages/arrow1_xskbon.jpg");
        casualShirtsArrow6.setActive(true);
        productVariationRepository.save(casualShirtsArrow6);

        //MEN FORMAL SHIRTS
        //Van Heusen
        ProductVariation formalShirtsVanHeusen = new ProductVariation();
        formalShirtsVanHeusen.setProduct(formalShirts);
        formalShirtsVanHeusen.setQuantityAvailable(10);
        formalShirtsVanHeusen.setPrice(2300.00);
        formalShirtsVanHeusen.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590861985/ProductVariationImages/6_MEN_FORMAL_SHIRTS_oe06ey.jpg");
        formalShirtsVanHeusen.setActive(true);
        productVariationRepository.save(formalShirtsVanHeusen);

        ProductVariation formalShirtsVanHeusen2 = new ProductVariation();
        formalShirtsVanHeusen2.setProduct(formalShirts);
        formalShirtsVanHeusen2.setQuantityAvailable(10);
        formalShirtsVanHeusen2.setPrice(3200.00);
        formalShirtsVanHeusen2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590861985/ProductVariationImages/8_MEN_FORMAL_SHIRTS_c6ngqn.jpg");
        formalShirtsVanHeusen2.setActive(true);
        productVariationRepository.save(formalShirtsVanHeusen2);

        ProductVariation formalShirtsVanHeusen3 = new ProductVariation();
        formalShirtsVanHeusen3.setProduct(formalShirts);
        formalShirtsVanHeusen3.setQuantityAvailable(10);
        formalShirtsVanHeusen3.setPrice(4300.00);
        formalShirtsVanHeusen3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590861985/ProductVariationImages/3_MEN_FORMAL_SHIRTS_xmvak2.jpg");
        formalShirtsVanHeusen3.setActive(true);
        productVariationRepository.save(formalShirtsVanHeusen3);

        //US POLO
        ProductVariation casualShirtsPolo1 = new ProductVariation();
        casualShirtsPolo1.setProduct(casualShirts2);
        casualShirtsPolo1.setQuantityAvailable(10);
        casualShirtsPolo1.setPrice(3200.00);
        casualShirtsPolo1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862868/ProductVariationImages/polo6_hipyoq.jpg");
        casualShirtsPolo1.setActive(true);
        productVariationRepository.save(casualShirtsPolo1);

        ProductVariation casualShirtsPolo2 = new ProductVariation();
        casualShirtsPolo2.setProduct(casualShirts2);
        casualShirtsPolo2.setQuantityAvailable(10);
        casualShirtsPolo2.setPrice(4300.00);
        casualShirtsPolo2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862868/ProductVariationImages/polo2_g6xir2.jpg");
        casualShirtsPolo2.setActive(true);
        productVariationRepository.save(casualShirtsPolo2);

        ProductVariation casualShirtsPolo3 = new ProductVariation();
        casualShirtsPolo3.setProduct(casualShirts2);
        casualShirtsPolo3.setQuantityAvailable(10);
        casualShirtsPolo3.setPrice(5600.00);
        casualShirtsPolo3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862868/ProductVariationImages/polo5_n2rxcn.jpg");
        casualShirtsPolo3.setActive(true);
        productVariationRepository.save(casualShirtsPolo3);

        ProductVariation casualShirtsPolo4 = new ProductVariation();
        casualShirtsPolo4.setProduct(casualShirts2);
        casualShirtsPolo4.setQuantityAvailable(10);
        casualShirtsPolo4.setPrice(3300.00);
        casualShirtsPolo4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862868/ProductVariationImages/polo3_zh0eoo.jpg");
        casualShirtsPolo4.setActive(true);
        productVariationRepository.save(casualShirtsPolo4);

        ProductVariation casualShirtsPolo5 = new ProductVariation();
        casualShirtsPolo5.setProduct(casualShirts2);
        casualShirtsPolo5.setQuantityAvailable(10);
        casualShirtsPolo5.setPrice(2500.00);
        casualShirtsPolo5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862868/ProductVariationImages/polo_zgvejl.jpg");
        casualShirtsPolo5.setActive(true);
        productVariationRepository.save(casualShirtsPolo5);

        ProductVariation casualShirtsPolo6 = new ProductVariation();
        casualShirtsPolo6.setProduct(casualShirts2);
        casualShirtsPolo6.setQuantityAvailable(10);
        casualShirtsPolo6.setPrice(6500.00);
        casualShirtsPolo6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590862868/ProductVariationImages/polo4_iwtfqy.jpg");
        casualShirtsPolo6.setActive(true);
        productVariationRepository.save(casualShirtsPolo6);

        //WOMEN SPORTS WEAR
        //CROCS
        ProductVariation casualShoesWomenCrocs = new ProductVariation();
        casualShoesWomenCrocs.setProduct(causalWearWomen);
        casualShoesWomenCrocs.setQuantityAvailable(10);
        casualShoesWomenCrocs.setPrice(3300.00);
        casualShoesWomenCrocs.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590863307/ProductVariationImages/2_WOMEN_SHOES_rn1ufu.jpg");
        casualShoesWomenCrocs.setActive(true);
        productVariationRepository.save(casualShoesWomenCrocs);

        ProductVariation casualShoesWomenCrocs2 = new ProductVariation();
        casualShoesWomenCrocs2.setProduct(causalWearWomen);
        casualShoesWomenCrocs2.setQuantityAvailable(10);
        casualShoesWomenCrocs2.setPrice(2300.00);
        casualShoesWomenCrocs2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590863307/ProductVariationImages/1_WOMEN_SHOES_ku7cai.jpg");
        casualShoesWomenCrocs2.setActive(true);
        productVariationRepository.save(casualShoesWomenCrocs2);

        ProductVariation casualShoesWomenCrocs3 = new ProductVariation();
        casualShoesWomenCrocs3.setProduct(causalWearWomen);
        casualShoesWomenCrocs3.setQuantityAvailable(10);
        casualShoesWomenCrocs3.setPrice(3300.00);
        casualShoesWomenCrocs3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590863307/ProductVariationImages/4_WOMEN_SHOES_tpiojj.jpg");
        casualShoesWomenCrocs3.setActive(true);
        productVariationRepository.save(casualShoesWomenCrocs3);

        //WOMEN SHIRTS
        //CASUAL
        ProductVariation casualShirtsWomen1 = new ProductVariation();
        casualShirtsWomen1.setProduct(casualShirtsWomen);
        casualShirtsWomen1.setQuantityAvailable(10);
        casualShirtsWomen1.setPrice(5500.00);
        casualShirtsWomen1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590864667/ProductVariationImages/shirtg12_q13csh.jpg");
        casualShirtsWomen1.setActive(true);
        productVariationRepository.save(casualShirtsWomen1);

        ProductVariation casualShirtsWomen2 = new ProductVariation();
        casualShirtsWomen2.setProduct(casualShirtsWomen);
        casualShirtsWomen2.setQuantityAvailable(10);
        casualShirtsWomen2.setPrice(4500.00);
        casualShirtsWomen2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590864668/ProductVariationImages/shirtg13_ongrvv.jpg");
        casualShirtsWomen2.setActive(true);
        productVariationRepository.save(casualShirtsWomen2);

        ProductVariation casualShirtsWomen3 = new ProductVariation();
        casualShirtsWomen3.setProduct(casualShirtsWomen);
        casualShirtsWomen3.setQuantityAvailable(10);
        casualShirtsWomen3.setPrice(4300.00);
        casualShirtsWomen3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590864669/ProductVariationImages/shirtg1_hiddmo.jpg");
        casualShirtsWomen3.setActive(true);
        productVariationRepository.save(casualShirtsWomen3);

        //FORMAL
        ProductVariation formalShirtsWomen1 = new ProductVariation();
        formalShirtsWomen1.setProduct(formalShirtsWomen);
        formalShirtsWomen1.setQuantityAvailable(10);
        formalShirtsWomen1.setPrice(3500.00);
        formalShirtsWomen1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590864785/ProductVariationImages/girlformal_gxlbaj.jpg");
        formalShirtsWomen1.setActive(true);
        productVariationRepository.save(formalShirtsWomen1);

        ProductVariation formalShirtsWomen2 = new ProductVariation();
        formalShirtsWomen2.setProduct(formalShirtsWomen);
        formalShirtsWomen2.setQuantityAvailable(10);
        formalShirtsWomen2.setPrice(2300.00);
        formalShirtsWomen2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590864784/ProductVariationImages/girlformal4_naqjtm.jpg");
        formalShirtsWomen2.setActive(true);
        productVariationRepository.save(formalShirtsWomen2);

        ProductVariation formalShirtsWomen3 = new ProductVariation();
        formalShirtsWomen3.setProduct(formalShirtsWomen);
        formalShirtsWomen3.setQuantityAvailable(10);
        formalShirtsWomen3.setPrice(4500.00);
        formalShirtsWomen3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590864783/ProductVariationImages/girlformal2_tbxob4.jpg");
        formalShirtsWomen3.setActive(true);
        productVariationRepository.save(formalShirtsWomen3);

        //BUSINESS LAPTOP DELL
        ProductVariation businessLaptopDell1 = new ProductVariation();
        businessLaptopDell1.setProduct(businessLaptopDell);
        businessLaptopDell1.setQuantityAvailable(10);
        businessLaptopDell1.setPrice(203000.00);
        businessLaptopDell1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865344/ProductVariationImages/61GpzFRWiOL._SL1000__kd2kte.jpg");
        businessLaptopDell1.setActive(true);
        productVariationRepository.save(businessLaptopDell1);

        ProductVariation businessLaptopDell2 = new ProductVariation();
        businessLaptopDell2.setProduct(businessLaptopDell);
        businessLaptopDell2.setQuantityAvailable(10);
        businessLaptopDell2.setPrice(143000.00);
        businessLaptopDell2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865343/ProductVariationImages/buisness143_iltook.jpg");
        businessLaptopDell2.setActive(true);
        productVariationRepository.save(formalShirtsWomen3);

        ProductVariation businessLaptopDell3 = new ProductVariation();
        businessLaptopDell3.setProduct(businessLaptopDell);
        businessLaptopDell3.setQuantityAvailable(10);
        businessLaptopDell3.setPrice(53000.00);
        businessLaptopDell3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865337/ProductVariationImages/buisness1_r408pn.jpg");
        businessLaptopDell3.setActive(true);
        productVariationRepository.save(businessLaptopDell3);

        //BUSINESS LAPTOP LENOVO
        ProductVariation businessLaptopLenovo1 = new ProductVariation();
        businessLaptopLenovo1.setProduct(businessLaptopLenovo);
        businessLaptopLenovo1.setQuantityAvailable(10);
        businessLaptopLenovo1.setPrice(55000.00);
        businessLaptopLenovo1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865474/ProductVariationImages/buisness143121111_eixug3.jpg");
        businessLaptopLenovo1.setActive(true);
        productVariationRepository.save(businessLaptopLenovo1);

        ProductVariation businessLaptopLenovo2 = new ProductVariation();
        businessLaptopLenovo2.setProduct(businessLaptopLenovo);
        businessLaptopLenovo2.setQuantityAvailable(10);
        businessLaptopLenovo2.setPrice(45000.00);
        businessLaptopLenovo2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865471/ProductVariationImages/buisness14312_nn41nn.jpg");
        businessLaptopLenovo2.setActive(true);
        productVariationRepository.save(businessLaptopLenovo2);

        ProductVariation businessLaptopLenovo3 = new ProductVariation();
        businessLaptopLenovo3.setProduct(businessLaptopLenovo);
        businessLaptopLenovo3.setQuantityAvailable(10);
        businessLaptopLenovo3.setPrice(100000.00);
        businessLaptopLenovo3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865471/ProductVariationImages/buisness1431211_chlgow.jpg");
        businessLaptopLenovo3.setActive(true);
        productVariationRepository.save(businessLaptopLenovo3);

        //BUSINESS LAPTOP HP
        ProductVariation businessLaptopHp1 = new ProductVariation();
        businessLaptopHp1.setProduct(businessLaptopHp);
        businessLaptopHp1.setQuantityAvailable(10);
        businessLaptopHp1.setPrice(102300.00);
        businessLaptopHp1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865474/ProductVariationImages/buisness143121111_eixug3.jpg");
        businessLaptopHp1.setActive(true);
        productVariationRepository.save(businessLaptopHp1);

        ProductVariation businessLaptopHp2 = new ProductVariation();
        businessLaptopHp2.setProduct(businessLaptopHp);
        businessLaptopHp2.setQuantityAvailable(10);
        businessLaptopHp2.setPrice(65000.00);
        businessLaptopHp2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865471/ProductVariationImages/buisness14312_nn41nn.jpg");
        businessLaptopHp2.setActive(true);
        productVariationRepository.save(businessLaptopHp2);

        ProductVariation businessLaptopHp3 = new ProductVariation();
        businessLaptopHp3.setProduct(businessLaptopHp);
        businessLaptopHp3.setQuantityAvailable(10);
        businessLaptopHp3.setPrice(76000.00);
        businessLaptopHp3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865343/ProductVariationImages/buisness143_iltook.jpg");
        businessLaptopHp3.setActive(true);
        productVariationRepository.save(businessLaptopHp3);

        //CASUAL WATCHES TITAN
        ProductVariation casualWatchTitan1 = new ProductVariation();
        casualWatchTitan1.setProduct(casualWatchTitan);
        casualWatchTitan1.setQuantityAvailable(10);
        casualWatchTitan1.setPrice(95800.00);
        casualWatchTitan1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865753/ProductVariationImages/tt3_erbx0m.jpg");
        casualWatchTitan1.setActive(true);
        productVariationRepository.save(casualWatchTitan1);

        ProductVariation casualWatchTitan2 = new ProductVariation();
        casualWatchTitan2.setProduct(casualWatchTitan);
        casualWatchTitan2.setQuantityAvailable(10);
        casualWatchTitan2.setPrice(45900.00);
        casualWatchTitan2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865753/ProductVariationImages/tt2_ludeo3.jpg");
        casualWatchTitan2.setActive(true);
        productVariationRepository.save(casualWatchTitan2);

        ProductVariation casualWatchTitan3 = new ProductVariation();
        casualWatchTitan3.setProduct(casualWatchTitan);
        casualWatchTitan3.setQuantityAvailable(10);
        casualWatchTitan3.setPrice(66000.00);
        casualWatchTitan3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865753/ProductVariationImages/tt_kgkr4m.jpg");
        casualWatchTitan3.setActive(true);
        productVariationRepository.save(casualWatchTitan3);

        //CASUAL WATCH RADO
        ProductVariation casualWatchRado1 = new ProductVariation();
        casualWatchRado1.setProduct(casualWatchRado);
        casualWatchRado1.setQuantityAvailable(10);
        casualWatchRado1.setPrice(77000.00);
        casualWatchRado1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865831/ProductVariationImages/r2_gct950.jpg");
        casualWatchRado1.setActive(true);
        productVariationRepository.save(casualWatchRado1);

        ProductVariation casualWatchRado2 = new ProductVariation();
        casualWatchRado2.setProduct(casualWatchRado);
        casualWatchRado2.setQuantityAvailable(10);
        casualWatchRado2.setPrice(43000.00);
        casualWatchRado2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865831/ProductVariationImages/r1_telyag.jpg");
        casualWatchRado2.setActive(true);
        productVariationRepository.save(casualWatchRado2);

        ProductVariation casualWatchRado3 = new ProductVariation();
        casualWatchRado3.setProduct(casualWatchRado);
        casualWatchRado3.setQuantityAvailable(10);
        casualWatchRado3.setPrice(65000.00);
        casualWatchRado3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590865831/ProductVariationImages/r3_ckeo5p.jpg");
        casualWatchRado3.setActive(true);
        productVariationRepository.save(casualWatchRado3);

        //SMART WATCH
        ProductVariation smartWatchFastrack1 = new ProductVariation();
        smartWatchFastrack1.setProduct(smartWatchFastrack);
        smartWatchFastrack1.setQuantityAvailable(10);
        smartWatchFastrack1.setPrice(34000.00);
        smartWatchFastrack1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590866612/ProductVariationImages/fast3_sov5ed.jpg");
        smartWatchFastrack1.setActive(true);
        productVariationRepository.save(smartWatchFastrack1);

        ProductVariation smartWatchFastrack2 = new ProductVariation();
        smartWatchFastrack2.setProduct(smartWatchFastrack);
        smartWatchFastrack2.setQuantityAvailable(10);
        smartWatchFastrack2.setPrice(10000.00);
        smartWatchFastrack2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590866612/ProductVariationImages/fadt2_rkxtpo.jpg");
        smartWatchFastrack2.setActive(true);
        productVariationRepository.save(smartWatchFastrack2);

        ProductVariation smartWatchFastrack3 = new ProductVariation();
        smartWatchFastrack3.setProduct(smartWatchFastrack);
        smartWatchFastrack3.setQuantityAvailable(10);
        smartWatchFastrack3.setPrice(12000.00);
        smartWatchFastrack3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590866612/ProductVariationImages/fastrack1_iwjp5c.jpg");
        smartWatchFastrack3.setActive(true);
        productVariationRepository.save(smartWatchFastrack3);

        //DSLR CAMERA NIKON
        ProductVariation dslrCameraNikon1 = new ProductVariation();
        dslrCameraNikon1.setProduct(dslrCameraNikon);
        dslrCameraNikon1.setQuantityAvailable(10);
        dslrCameraNikon1.setPrice(55000.00);
        dslrCameraNikon1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869333/ProductVariationImages/n1_tufzhi.jpg");
        dslrCameraNikon1.setActive(true);
        productVariationRepository.save(dslrCameraNikon1);

        ProductVariation dslrCameraNikon2 = new ProductVariation();
        dslrCameraNikon2.setProduct(dslrCameraNikon);
        dslrCameraNikon2.setQuantityAvailable(10);
        dslrCameraNikon2.setPrice(65000.00);
        dslrCameraNikon2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869332/ProductVariationImages/n2_sovjbz.jpg");
        dslrCameraNikon2.setActive(true);
        productVariationRepository.save(dslrCameraNikon2);

        ProductVariation dslrCameraNikon3 = new ProductVariation();
        dslrCameraNikon3.setProduct(dslrCameraNikon);
        dslrCameraNikon3.setQuantityAvailable(10);
        dslrCameraNikon3.setPrice(55000.00);
        dslrCameraNikon3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869332/ProductVariationImages/n3_s1czsh.jpg");
        dslrCameraNikon3.setActive(true);
        productVariationRepository.save(dslrCameraNikon3);

        //DSLR CAMERA SONY
        ProductVariation dslrCameraSony1 = new ProductVariation();
        dslrCameraSony1.setProduct(dslrCameraSony);
        dslrCameraSony1.setQuantityAvailable(10);
        dslrCameraSony1.setPrice(65000.00);
        dslrCameraSony1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590867110/ProductVariationImages/SONY1212121_ppf4m5.jpg");
        dslrCameraSony1.setActive(true);
        productVariationRepository.save(dslrCameraSony1);

        ProductVariation dslrCameraSony2 = new ProductVariation();
        dslrCameraSony2.setProduct(dslrCameraSony);
        dslrCameraSony2.setQuantityAvailable(10);
        dslrCameraSony2.setPrice(34000.00);
        dslrCameraSony2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590867110/ProductVariationImages/SONY2_muqc8k.jpg");
        dslrCameraSony2.setActive(true);
        productVariationRepository.save(dslrCameraSony2);

        ProductVariation dslrCameraSony3 = new ProductVariation();
        dslrCameraSony3.setProduct(dslrCameraSony);
        dslrCameraSony3.setQuantityAvailable(10);
        dslrCameraSony3.setPrice(109700.00);
        dslrCameraSony3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590867122/ProductVariationImages/ONSDASDSAD_dhxy3y.jpg");
        dslrCameraSony3.setActive(true);
        productVariationRepository.save(dslrCameraSony3);

        //MOBILE ONE PLUS
        ProductVariation mobileOnePlus1 = new ProductVariation();
        mobileOnePlus1.setProduct(mobileOnePlus);
        mobileOnePlus1.setQuantityAvailable(10);
        mobileOnePlus1.setPrice(55000.00);
        mobileOnePlus1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869801/ProductVariationImages/onep3_jnun2h.jpg");
        mobileOnePlus1.setActive(true);
        productVariationRepository.save(mobileOnePlus1);

        ProductVariation mobileOnePlus2 = new ProductVariation();
        mobileOnePlus2.setProduct(mobileOnePlus);
        mobileOnePlus2.setQuantityAvailable(10);
        mobileOnePlus2.setPrice(45000.00);
        mobileOnePlus2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869801/ProductVariationImages/onep_pdgl5d.jpg");
        mobileOnePlus2.setActive(true);
        productVariationRepository.save(mobileOnePlus2);

        ProductVariation mobileOnePlus3 = new ProductVariation();
        mobileOnePlus3.setProduct(mobileOnePlus);
        mobileOnePlus3.setQuantityAvailable(10);
        mobileOnePlus3.setPrice(46000.00);
        mobileOnePlus3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869801/ProductVariationImages/onep6_tyahnn.jpg");
        mobileOnePlus3.setActive(true);
        productVariationRepository.save(mobileOnePlus3);

        ProductVariation mobileOnePlus4 = new ProductVariation();
        mobileOnePlus4.setProduct(mobileOnePlus);
        mobileOnePlus4.setQuantityAvailable(10);
        mobileOnePlus4.setPrice(55000.00);
        mobileOnePlus4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869801/ProductVariationImages/onep2_akq6op.jpg");
        mobileOnePlus4.setActive(true);
        productVariationRepository.save(mobileOnePlus4);

        ProductVariation mobileOnePlus5 = new ProductVariation();
        mobileOnePlus5.setProduct(mobileOnePlus);
        mobileOnePlus5.setQuantityAvailable(10);
        mobileOnePlus5.setPrice(65000.00);
        mobileOnePlus5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869801/ProductVariationImages/onep5_ugurq8.jpg");
        mobileOnePlus5.setActive(true);
        productVariationRepository.save(mobileOnePlus5);

        ProductVariation mobileOnePlus6 = new ProductVariation();
        mobileOnePlus6.setProduct(mobileOnePlus);
        mobileOnePlus6.setQuantityAvailable(10);
        mobileOnePlus6.setPrice(55000.00);
        mobileOnePlus6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590869801/ProductVariationImages/onep4_lpgkdv.jpg");
        mobileOnePlus6.setActive(true);
        productVariationRepository.save(mobileOnePlus6);

        //MOBILE APPLE
        ProductVariation mobileApple1 = new ProductVariation();
        mobileApple1.setProduct(mobileApple);
        mobileApple1.setQuantityAvailable(10);
        mobileApple1.setPrice(100000.00);
        mobileApple1.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590870042/ProductVariationImages/apple1_oixi95.jpg");
        mobileApple1.setActive(true);
        productVariationRepository.save(mobileApple1);

        ProductVariation mobileApple2 = new ProductVariation();
        mobileApple2.setProduct(mobileApple);
        mobileApple2.setQuantityAvailable(10);
        mobileApple2.setPrice(134000.00);
        mobileApple2.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590870042/ProductVariationImages/apple6_qvlm4d.jpg");
        mobileApple2.setActive(true);
        productVariationRepository.save(mobileApple2);

        ProductVariation mobileApple3 = new ProductVariation();
        mobileApple3.setProduct(mobileApple);
        mobileApple3.setQuantityAvailable(10);
        mobileApple3.setPrice(107000.00);
        mobileApple3.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590870042/ProductVariationImages/apple5_v9bqpt.jpg");
        mobileApple3.setActive(true);
        productVariationRepository.save(mobileApple3);

        ProductVariation mobileApple4 = new ProductVariation();
        mobileApple4.setProduct(mobileApple);
        mobileApple4.setQuantityAvailable(10);
        mobileApple4.setPrice(100860.00);
        mobileApple4.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590870042/ProductVariationImages/apple2_f0l20z.jpg");
        mobileApple4.setActive(true);
        productVariationRepository.save(mobileApple4);

        ProductVariation mobileApple5 = new ProductVariation();
        mobileApple5.setProduct(mobileApple);
        mobileApple5.setQuantityAvailable(10);
        mobileApple5.setPrice(105600.00);
        mobileApple5.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590870042/ProductVariationImages/apple4_i5llrz.jpg");
        mobileApple5.setActive(true);
        productVariationRepository.save(mobileApple5);

        ProductVariation mobileApple6 = new ProductVariation();
        mobileApple6.setProduct(mobileApple);
        mobileApple6.setQuantityAvailable(10);
        mobileApple6.setPrice(100470.00);
        mobileApple6.setPrimaryImageName("https://res.cloudinary.com/da8palx8c/image/upload/v1590870042/ProductVariationImages/apple3_rzu8ox.jpg");
        mobileApple6.setActive(true);
        productVariationRepository.save(mobileApple6);
    }

}






























































































