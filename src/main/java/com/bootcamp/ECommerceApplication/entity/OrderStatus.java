package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "order_product_id")
public class OrderStatus extends OrderProduct{
    private String from_status;
    private String to_status;
    private String transition_notes_comments;

}
