package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User{

    //VALID GST NUMBER - 37adapm1724a2Z6
    @NotEmpty(message = "gst is a mandatory field")
    @Column(unique = true)
    @Pattern(regexp = "^([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$",
            message = "GST should be valid as per Govt. norms")
    private String  gst;

    //This Pattern is to Validate Mobile Number with 10 digit Number and Countrycode as Optional, And check for Landline Numbers as well.
    @Pattern(regexp = "^(?:\\s+|)((0|(?:(\\+|)91))(?:\\s|-)*(?:(?:\\d(?:\\s|-)*\\d{9})|(?:\\d{2}(?:\\s|-)*\\d{8})|(?:\\d{3}(?:\\s|-)*\\d{7}))|\\d{10})(?:\\s+|)$"
            ,message = "The Contact No. is not valid")
    @NotEmpty(message = "Company Contact No. is a mandatory field")
    private String company_contact;


    @NotEmpty(message = "Company Name is a mandatory field")
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
}
