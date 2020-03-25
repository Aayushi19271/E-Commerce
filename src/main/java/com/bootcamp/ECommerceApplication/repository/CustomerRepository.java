package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Customer findByEmailIgnoreCase(String emailId);
}
