package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByEmailIgnoreCase(String emailId);

    @Query(value = "select * from address where user_id=:id",nativeQuery = true)
    List<Map<Object,Object>> findAllAddress(Long id);

    @Query(value = "select c.authority AS authority from user a " +
            "inner join user_role b on a.id=b.user_id " +
            "inner join role c on b.role_id = c.id " +
            "where Email=:emailId ",nativeQuery = true)
    String getUserRole(String emailId);
}
