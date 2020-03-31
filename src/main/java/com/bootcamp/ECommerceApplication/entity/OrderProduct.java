package com.bootcamp.ECommerceApplication.entity;


import javax.persistence.*;

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    private Double price;
    private String product_variation_metadata;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private CustomerOrder customerOrder;

    @OneToOne
    @JoinColumn(name = "productVariationId")
    private ProductVariation productVariation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public String getProduct_variation_metadata() {
        return product_variation_metadata;
    }

    public void setProduct_variation_metadata(String product_variation_metadata) {
        this.product_variation_metadata = product_variation_metadata;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public ProductVariation getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", product_variation_metadata='" + product_variation_metadata + '\'' +
                ", customerOrder=" + customerOrder +
                ", productVariation=" + productVariation +
                '}';
    }
}
