package com.bootcamp.ECommerceApplication.co;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CustomerCO extends UserCO {
    @Pattern(regexp = "^(?:\\s+|)((0|(?:(\\+|)91))(?:\\s|-)*(?:(?:\\d(?:\\s|-)*\\d{9})|(?:\\d{2}(?:\\s|-)*\\d{8})|(?:\\d{3}(?:\\s|-)*\\d{7}))|\\d{10})(?:\\s+|)$"
            ,message = "Please provide valid Contact No.")
    @NotNull(message = "Please provide Contact No.")
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}