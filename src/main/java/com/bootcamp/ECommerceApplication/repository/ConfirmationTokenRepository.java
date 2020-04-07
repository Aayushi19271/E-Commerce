package com.bootcamp.ECommerceApplication.repository;

import com.bootcamp.ECommerceApplication.entity.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

    ConfirmationToken findByConfirmationToken(String confirmationToken);

    void deleteByConfirmationToken(String confirmationToken);
}
