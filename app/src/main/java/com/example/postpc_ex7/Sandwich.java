package com.example.postpc_ex7;

import java.io.Serializable;
import java.util.UUID;

public class Sandwich implements Serializable {
    private final String id;
    private String customerName;
    private int pickles;
    private boolean hummus;
    private boolean tahini;
    private String comment;
    private String status;


    public Sandwich(String customerName, int pickles, boolean hummus, boolean tahini, String comment) {
        this.id = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
        this.status = "init";
    }

    public Sandwich() {
        this("", 0, false, false, "");
    }

    public Sandwich(Sandwich o) {
        this.id = o.id;
        this.customerName = o.customerName;
        this.comment = o.comment;
        this.pickles = o.pickles;
        this.hummus = o.hummus;
        this.tahini = o.tahini;
        this.status = o.status;
    }

    @Override
    public String toString() {
        return "Sandwich{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", pickles=" + pickles +
                ", hummus=" + hummus +
                ", tahini=" + tahini +
                ", comment='" + comment + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }


    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getPickles() {
        return pickles;
    }

    public void setPickles(int pickles) {
        this.pickles = pickles;
    }

    public boolean isHummus() {
        return hummus;
    }

    public void setHummus(boolean hummus) {
        this.hummus = hummus;
    }

    public boolean isTahini() {
        return tahini;
    }

    public void setTahini(boolean tahini) {
        this.tahini = tahini;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
