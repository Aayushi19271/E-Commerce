package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne
    private Category parent;
    private boolean leafNode = false;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public boolean isLeafNode() {
        return leafNode;
    }

    public void setLeafNode(boolean leafNode) {
        this.leafNode = leafNode;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                ", leafNode=" + leafNode +
                ", products=" + products +
                '}';
    }
}
