package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id",  referencedColumnName = "id")
public class Seller extends User{

    @Column(unique = true)
    private String  gst;
    private String company_contact;
    @Column(unique = true)
    private String company_name;


    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> products;

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompany_contact() {
        return company_contact;
    }

    public void setCompany_contact(String company_contact) {
        this.company_contact = company_contact;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "gst='" + gst + '\'' +
                ", company_contact='" + company_contact + '\'' +
                ", company_name='" + company_name + '\'' +
                ", products=" + products +
                '}';
    }
}
