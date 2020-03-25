package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class CustomerOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer amount_paid;
    private Date date;
    private String payment_method;

    @ManyToOne
    @JoinColumn(name="customer_user_id")
    private Customer customer;

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "city", column = @Column(name = "customer_address_city")),
//            @AttributeOverride(name = "state", column = @Column(name = "customer_address_state")),
//            @AttributeOverride(name = "country", column = @Column(name = "customer_address_country")),
//            @AttributeOverride(name = "address", column = @Column(name = "customer_address_address")),
//            @AttributeOverride(name = "zip_code", column = @Column(name = "customer_address_zip_code")),
//            @AttributeOverride(name = "label", column = @Column(name = "customer_address_label")),
//    })
//    private Address address;

    @OneToMany(mappedBy = "customerOrder")
    private List<OrderProduct> orderProducts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(Integer amount_paid) {
        this.amount_paid = amount_paid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", amount_paid=" + amount_paid +
                ", date=" + date +
                ", payment_method='" + payment_method + '\'' +
                ", customer=" + customer +
                '}';
    }
}
