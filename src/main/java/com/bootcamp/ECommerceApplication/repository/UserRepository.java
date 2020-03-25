package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Customer;
import com.bootcamp.ECommerceApplication.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    User findByEmailIgnoreCase(String emailId);
}
