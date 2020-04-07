package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByEmailIgnoreCase(String emailId);

//    @Query(value = "select first_name from user u inner join user_role r on u.id=r.user_id where r.role_id=1",nativeQuery = true)
//    User findUserByAdminAuthority();
}
