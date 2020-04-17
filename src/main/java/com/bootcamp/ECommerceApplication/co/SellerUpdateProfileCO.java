package com.bootcamp.ECommerceApplication.co;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.bootcamp.ECommerceApplication.constant.Constants.CONTACT;
import static com.bootcamp.ECommerceApplication.constant.Constants.GST;

public class SellerUpdateProfileCO extends UserUpdateProfileCO {
    @NotNull(message = "Please provide GST number")
    @Pattern(regexp = GST,
            message = "GST should be valid as per Govt. norms")
    private String gst;

    @Pattern(regexp = CONTACT
            , message = "The Contact No. is not valid")
    @NotNull(message = "Please provide Company Contact")
    private String companyContact;


    @NotNull(message = "Please provide Company Name")
    @NotBlank(message = "Please provide valid Company Name")
    private String companyName;


    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "SellerUpdateProfileCO{" +
                "gst='" + gst + '\'' +
                ", companyContact='" + companyContact + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
