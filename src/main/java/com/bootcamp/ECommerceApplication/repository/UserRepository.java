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
}
