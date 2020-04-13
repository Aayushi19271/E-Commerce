package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer,Long> {
    Customer findByEmailIgnoreCase(String emailId);

    @Query(value = "select u.id,u.first_name,u.middle_name,u.last_name,u.email,u.is_active " +
            "from user u inner join customer c " +
            "on u.id=c.user_id ",nativeQuery = true)
    Page<Customer> findAll(Pageable paging);

}
