package com.orderGestion.orderGestion.domain.model;

public class Costumer {
    private int costumerId;
    private String name;
    private String email;

    public Costumer(int costumerId, String name, String email) {
        this.costumerId = costumerId;
        this.name = name;
        this.email = email;
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
