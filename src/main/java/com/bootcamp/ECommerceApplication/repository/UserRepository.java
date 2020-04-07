package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByEmailIgnoreCase(String emailId);
}
