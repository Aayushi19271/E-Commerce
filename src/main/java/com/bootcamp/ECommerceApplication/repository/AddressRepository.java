package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface AddressRepository extends CrudRepository<Address,Long> {

    @Modifying
    @Query(value = "delete from address where id=:id",nativeQuery = true)
    void deleteByAddressID(Long id);
}
