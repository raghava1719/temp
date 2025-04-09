package com.example.savingsaccount.service;

import com.example.savingsaccount.model.SavingsAccount;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SavingsAccountService {
    private List<SavingsAccount> accounts = new ArrayList<>();

    public SavingsAccountService() {
        // Initialize with some accounts
        accounts.add(new SavingsAccount("Customer1", 2000.00));
        accounts.add(new SavingsAccount("Customer2", 3000.00));
    }

    public List<SavingsAccount> getAllAccounts() {
        return accounts;
    }
    
    public void addAccount(String customerName, double balance) {
        if (balance < 0) throw new IllegalArgumentException("Balance cannot be negative");
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        
        SavingsAccount newAccount = new SavingsAccount(customerName.trim(), balance);
        newAccount.addTransaction(String.format("Account opened with initial deposit: Rs.%.2f", balance));
        accounts.add(newAccount);
    }
    
    public void calculateMonthlyInterestForAll() {
        for (SavingsAccount account : accounts) {
            double before = account.getSavingsBalance();
            account.calculateMonthlyInterest();
            double after = account.getSavingsBalance();
            account.addTransaction(String.format(
                "Monthly interest: $%.2f added (%.2f%% annual rate)", 
                after - before, 
                SavingsAccount.getAnnualInterestRate() * 100
            ));
        }
    }

    public void modifyInterestRate(double newRatePercent) {
        double oldRate = SavingsAccount.getAnnualInterestRate() * 100;
        SavingsAccount.modifyInterestRate(newRatePercent);
        
        // Record rate change for all accounts
        accounts.forEach(account -> 
            account.addTransaction(String.format(
                "Interest rate changed from %.2f%% to %.2f%%",
                oldRate,
                newRatePercent*100
            ))
        );
    }
    
 // Add to SavingsAccountService.java
//    @Cacheable("projections")
    public Map<Integer, Double> calculateYearlyProjection(String customerName) {
        SavingsAccount account = accounts.stream()
            .filter(acc -> acc.getCustomerName().equalsIgnoreCase(customerName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerName));
        
        Map<Integer, Double> projection = new LinkedHashMap<>();
        double balance = account.getSavingsBalance();
        double rate = SavingsAccount.getAnnualInterestRate();
        
        for (int month = 1; month <= 12; month++) {
            double interest = (balance * rate) / 12;
            balance += interest;
            projection.put(month, balance);
        }
        
        return projection;
    }
    
 // Add these methods to SavingsAccountService.java

    public List<String> getTransactionHistory(String customerName) {
        return accounts.stream()
                .filter(account -> account.getCustomerName().equalsIgnoreCase(customerName))
                .findFirst()
                .map(SavingsAccount::getTransactionHistory)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }

    public void addTransaction(String customerName, String transactionDescription) {
        accounts.stream()
                .filter(account -> account.getCustomerName().equalsIgnoreCase(customerName))
                .findFirst()
                .ifPresent(account -> account.addTransaction(transactionDescription));
    }
}