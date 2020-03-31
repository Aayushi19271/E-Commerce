package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Customer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Customer findByEmailIgnoreCase(String emailId);

    @Query(value = "select u.id,u.first_name,u.middle_name,u.last_name,u.email,u.is_active " +
            "from user u inner join customer c " +
            "on u.id=c.user_id " +
            "where u.is_active=true",nativeQuery = true)
    List<Object[]> findAllCustomers(PageRequest pageable);
}
