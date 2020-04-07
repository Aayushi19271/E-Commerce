package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {
    //THE TOKEN EXPIRES IN 30 MINUTES
    private static final int EXPIRATION = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long tokenid;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name="expiry_date")
    private Date expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ConfirmationToken() {
    }

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    private Date calculateExpiry(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,getEXPIRATION());
        return calendar.getTime();
    }

    public static int getEXPIRATION() {
        return EXPIRATION;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = calculateExpiry(expiryDate);
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ConfirmationToken{" +
                "tokenid=" + tokenid +
                ", confirmationToken='" + confirmationToken + '\'' +
                ", createdDate=" + createdDate +
                ", expiryDate=" + expiryDate +
                ", user=" + user +
                '}';
    }
}