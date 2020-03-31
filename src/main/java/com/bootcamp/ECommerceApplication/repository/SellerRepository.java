package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Seller;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SellerRepository extends CrudRepository<Seller,Long> {

    @Query(value = "select u.id,u.first_name,u.middle_name,u.last_name,u.email,u.is_active,s.company_name,s.company_contact " +
            "from user u inner join seller s " +
            "on u.id=s.user_id " +
            "where u.is_active=true",nativeQuery = true)
    List<Object[]> findAllSellers(PageRequest pageable);
}
