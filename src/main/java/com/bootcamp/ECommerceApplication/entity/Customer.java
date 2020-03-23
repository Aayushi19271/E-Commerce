package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends User{

    private Integer contact;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Address> addresses;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<CustomerOrder> orders;

    public Integer getContact() {
        return contact;
    }

    public void setContact(Integer contact) {
        this.contact = contact;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "contact=" + contact +
                ", addresses=" + addresses +
                '}';
    }
}
