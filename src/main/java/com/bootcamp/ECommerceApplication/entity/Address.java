package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;

@Entity
@Embeddable
//@PrimaryKeyJoinColumn(name = "id")
public class Address extends Customer{
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    private String city;
    private String state;
    private String country;
    private String address;
    private Integer zip_code;
    private String label;

    @ManyToOne
    @JoinColumn(name = "customer_user_id",insertable = false,updatable = false)
    private Customer customer;


}
