package com.example.savingsaccount.model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SavingsAccount extends Interest{
    private double savingsBalance;
    private String customerName;
    
    private List<String> transactionHistory = new ArrayList<>();

    public void addTransaction(String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        String timestamp = LocalDateTime.now().format(formatter);
        transactionHistory.add(timestamp + " - " + description);
    }
    public List<String> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }


    public SavingsAccount(String customerName, double savingsBalance) {
        this.customerName = customerName;
        this.savingsBalance = savingsBalance;
    }

 // Modify calculateMonthlyInterest
 public void calculateMonthlyInterest() {
     double monthlyInterest = (savingsBalance * getAnnualInterestRate()) / 12;
     savingsBalance += monthlyInterest;
     addTransaction(String.format("Interest added: Rs.%.2f (New balance: Rs.%.2f)", 
    	     monthlyInterest, savingsBalance));

 }
 


    // Getters and Setters
    public double getSavingsBalance() {
        return savingsBalance;
    }

    public String getCustomerName() {
        return customerName;
    }


}