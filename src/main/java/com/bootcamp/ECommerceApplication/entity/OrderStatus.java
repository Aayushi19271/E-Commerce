package com.bootcamp.ECommerceApplication.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class OrderStatus{

    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "orderProductId")
    private OrderProduct orderProductId;

    private enum fromStatus{
        ORDER_PLACED, CANCELLED, ORDER_REJECTED, ORDER_CONFIRMED, ORDER_SHIPPED,
        DELIVERED, RETURN_REQUESTED, RETURN_REJECTED, RETURN_APPROVED, PICK_UP_INITIATED,
        PICK_UP_COMPLETED, REFUND_INITIATED, REFUND_COMPLETED;
    }
    private enum ToStatus {
        CANCELLED, ORDER_CONFIRMED, ORDER_REJECTED, REFUND_INITIATED,
        CLOSED, ORDER_SHIPPED, DELIVERED, RETURN_REQUESTED, RETURN_REJECTED,
        RETURN_APPROVED, PICK_UP_INITIATED, PICK_UP_COMPLETED, REFUND_COMPLETED;
    }

    private String transitionNotesComments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderProduct getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(OrderProduct orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getTransitionNotesComments() {
        return transitionNotesComments;
    }

    public void setTransitionNotesComments(String transitionNotesComments) {
        this.transitionNotesComments = transitionNotesComments;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "id=" + id +
                ", orderProductId=" + orderProductId +
                ", transitionNotesComments='" + transitionNotesComments + '\'' +
                '}';
    }
}

