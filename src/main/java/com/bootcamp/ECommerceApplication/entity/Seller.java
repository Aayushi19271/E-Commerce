package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User{

    private Integer gst;
    private String company_address_id;
    private Integer company_contact;
    private String company_name;

    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> products;


    public Integer getGst() {
        return gst;
    }

    public void setGst(Integer gst) {
        this.gst = gst;
    }

    public String getCompany_address_id() {
        return company_address_id;
    }

    public void setCompany_address_id(String company_address_id) {
        this.company_address_id = company_address_id;
    }

    public Integer getCompany_contact() {
        return company_contact;
    }

    public void setCompany_contact(Integer company_contact) {
        this.company_contact = company_contact;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
