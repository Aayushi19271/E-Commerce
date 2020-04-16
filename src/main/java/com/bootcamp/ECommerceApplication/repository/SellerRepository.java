package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;


public interface SellerRepository extends PagingAndSortingRepository<Seller,Long> {

    @Query(value = "select u.id,u.first_name,u.middle_name,u.last_name,u.email,u.is_active,s.company_name,s.company_contact " +
            "from user u inner join seller s " +
            "on u.id=s.user_id ",nativeQuery = true)
    List<Map<Object,Object>> findAllSellers(Pageable paging);

    Seller findByEmailIgnoreCase(String emailId);
}
