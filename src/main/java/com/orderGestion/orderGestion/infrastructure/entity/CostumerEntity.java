package com.orderGestion.orderGestion.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "costumers")
public class CostumerEntity {
    @Id
    @Column(name = "costumerid")
    private int costumerId;
    private String name;
    private String email;

    public CostumerEntity(int costumerId, String name, String email) {
        this.costumerId = costumerId;
        this.name = name;
        this.email = email;
    }

    public CostumerEntity() {
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
