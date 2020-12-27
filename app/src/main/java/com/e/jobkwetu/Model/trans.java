package com.e.jobkwetu.Model;

public class trans {
    private String date, transaction, status, amount;
    private String id;

    public trans() {
    }

    public trans(String date, String transaction, String status, String amount, String id) {
        this.date = date;
        this.transaction = transaction;
        this.status = status;
        this.amount = amount;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}