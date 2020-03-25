package com.bootcamp.ECommerceApplication;

import com.bootcamp.ECommerceApplication.entity.Address;
import com.bootcamp.ECommerceApplication.entity.Role;
import com.bootcamp.ECommerceApplication.entity.User;
import com.bootcamp.ECommerceApplication.repository.RoleRepository;
import com.bootcamp.ECommerceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bootstrap implements ApplicationRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Role role = new Role();
        role.setAuthority("ROLE_ADMIN");
        Role role2 = new Role();
        role2.setAuthority("ROLE_SELLER");
        Role role3 = new Role();
        role3.setAuthority("ROLE_CUSTOMER");
        roleRepository.save(role);
        roleRepository.save(role2);
        roleRepository.save(role3);

        User user = new User();
        user.setEmail("aayushithani@yahoo.in");
        user.setFirst_name("Aayushi");
        user.setLast_name("Thani");
        user.setPassword("Aayushi12#");
        user.setIs_active(true);
        user.setIs_deleted(false);

        List<Address> list = new ArrayList<>();
        Address address = new Address();
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        address.setAddress("B7- Pitmapura");
        address.setZip_code(110085);
        address.setLabel("Home");
        list.add(address);
        user.setAddresses(list);

        ArrayList<Role> tempRole = new ArrayList<>();
        Role role1 = roleRepository.findById((long) 1).get();
        tempRole.add(role1);
        user.setRoles(tempRole);

        userRepository.save(user);
    }
}
