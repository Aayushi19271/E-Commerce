package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {




//    @Query(value = "insert into user_role (user_id, role_id) values (:value1, :value2)",nativeQuery = true)
//    public void insertCustomerRole(@Param("value1") Long user_id,@Param("value2") Long role_id);
}
