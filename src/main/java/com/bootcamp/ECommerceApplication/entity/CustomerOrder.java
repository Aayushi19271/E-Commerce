package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
public class CustomerOrder{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="customerUserId")
    @NotNull
    private Customer customer;
    private Float amountPaid;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressAddressLine;
    private Integer customerZipCode;
    private String customerAddressLabel;

    @OneToMany(mappedBy = "customerOrder")
    private List<OrderProduct> orderProducts;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerAddressCity() {
        return customerAddressCity;
    }

    public void setCustomerAddressCity(String customerAddressCity) {
        this.customerAddressCity = customerAddressCity;
    }

    public String getCustomerAddressState() {
        return customerAddressState;
    }

    public void setCustomerAddressState(String customerAddressState) {
        this.customerAddressState = customerAddressState;
    }

    public String getCustomerAddressCountry() {
        return customerAddressCountry;
    }

    public void setCustomerAddressCountry(String customerAddressCountry) {
        this.customerAddressCountry = customerAddressCountry;
    }

    public String getCustomerAddressAddressLine() {
        return customerAddressAddressLine;
    }

    public void setCustomerAddressAddressLine(String customerAddressAddressLine) {
        this.customerAddressAddressLine = customerAddressAddressLine;
    }

    public Integer getCustomerZipCode() {
        return customerZipCode;
    }

    public void setCustomerZipCode(Integer customerZipCode) {
        this.customerZipCode = customerZipCode;
    }

    public String getCustomerAddressLabel() {
        return customerAddressLabel;
    }

    public void setCustomerAddressLabel(String customerAddressLabel) {
        this.customerAddressLabel = customerAddressLabel;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
                "id=" + id +
                ", customer=" + customer +
                ", amountPaid=" + amountPaid +
                ", dateCreated=" + dateCreated +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", customerAddressCity='" + customerAddressCity + '\'' +
                ", customerAddressState='" + customerAddressState + '\'' +
                ", customerAddressCountry='" + customerAddressCountry + '\'' +
                ", customerAddressAddressLine='" + customerAddressAddressLine + '\'' +
                ", customerZipCode=" + customerZipCode +
                ", customerAddressLabel='" + customerAddressLabel + '\'' +
                ", orderProducts=" + orderProducts +
                '}';
    }
}
