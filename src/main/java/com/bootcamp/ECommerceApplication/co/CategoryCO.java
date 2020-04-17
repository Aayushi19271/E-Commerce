package com.bootcamp.ECommerceApplication.co;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class CategoryCO {
    @NotNull(message = "Please provide Name")
    @NotBlank(message = "Please provide valid Name")
    private String name;
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CategoryCO{" +
                "name='" + name + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
