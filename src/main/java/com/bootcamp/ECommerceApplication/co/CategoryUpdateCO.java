package com.bootcamp.ECommerceApplication.co;

public class CategoryUpdateCO {

    private Long id;
    private String name;

    public CategoryUpdateCO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public String toString() {
        return "CategoryUpdateCO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
