package com.bootcamp.ECommerceApplication.co;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductUpdateCO {
    private String name;
    private String description;
    @JsonProperty
    private boolean isCancellable=false;
    @JsonProperty
    private boolean isReturnable=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public void setCancellable(boolean cancellable) {
        isCancellable = cancellable;
    }

    public boolean isReturnable() {
        return isReturnable;
    }

    public void setReturnable(boolean returnable) {
        isReturnable = returnable;
    }

    @Override
    public String toString() {
        return "ProductUpdateCO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isCancellable=" + isCancellable +
                ", isReturnable=" + isReturnable +
                '}';
    }
}
