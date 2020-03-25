package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {

    //THE TOKEN EXPIRES IN 30 MINUTES
    private static final int EXPIRATION = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long tokenid;

    @Column(name = "confirmation_token")
    private String confirmationToken;

//    @Column(name = "expired_token")
//    private String expiredToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name="expiry_date")
    private Date expiryDate;

    @OneToOne
    @JoinColumn(name = "customer_user_id")
    private Customer customer;

    public ConfirmationToken() {
    }

    public ConfirmationToken(Customer customer) {
        this.customer = customer;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    private Date calculateExpiry(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,getEXPIRATION());
        Date expirydate = calendar.getTime();
        return expirydate;
    }

    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        Date time = calculateExpiry(expiryDate);
        this.expiryDate = time;
    }
//
//    public String getExpiredToken() {
//        return expiredToken;
//    }
//
//    public void setExpiredToken(String expiredToken) {
//        this.expiredToken = expiredToken;
//    }

    public long getTokenid() {
        return tokenid;
    }

    public void setTokenid(long tokenid) {
        this.tokenid = tokenid;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}