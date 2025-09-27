package com.application.BankStatement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
public class Statement {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    //@JsonFormat(shape =JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String postdate;
   //@JsonFormat(shape =JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String valuedate;
    private String details;
    private String accountName;
    private double amount;
    private String Type;
    private double Balance;

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getValuedate() {
        return valuedate;
    }

    public void setValuedate(String valuedate) {
        this.valuedate = valuedate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "postdate=" + postdate +
                ", valuedate=" + valuedate +
                ", details='" + details + '\'' +
                ", accountName='" + accountName + '\'' +
                ", amount=" + amount +
                ", Type='" + Type + '\'' +
                ", Balance=" + Balance +
                '}';
    }
}
