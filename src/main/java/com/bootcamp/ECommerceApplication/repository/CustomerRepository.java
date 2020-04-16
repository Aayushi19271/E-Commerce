package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer,Long> {
    Customer findByEmailIgnoreCase(String emailId);

    @Query(value = "select u.id,u.first_name,u.middle_name,u.last_name,u.email,u.is_active,c.contact " +
            "from user u inner join customer c " +
            "on u.id=c.user_id ",nativeQuery = true)
    List<Map<Object,Object>> findAllCustomers(Pageable paging);

}
