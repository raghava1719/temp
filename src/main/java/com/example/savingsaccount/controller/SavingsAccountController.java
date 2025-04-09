package com.example.savingsaccount.controller;

import com.example.savingsaccount.model.SavingsAccount;
import com.example.savingsaccount.service.SavingsAccountService;

import jakarta.validation.constraints.Min;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SavingsAccountController {
    private final SavingsAccountService accountService;

    public SavingsAccountController(SavingsAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String showAccounts(Model model) {
    	
    	// Initialize rate if not set
        if (SavingsAccount.getAnnualInterestRate() == 0) {
            SavingsAccount.modifyInterestRate(0.04); // Default to 4%
        }
        model.addAttribute("accounts", accountService.getAllAccounts());
        model.addAttribute("currentRate", SavingsAccount.getAnnualInterestRate());
        return "accounts";
    }

    @PostMapping("/calculate")
    public String calculateInterest() {
        accountService.calculateMonthlyInterestForAll();
        return "redirect:/";
    }

    @PostMapping("/modify-rate")
    public String modifyInterestRate(@RequestParam double newRate, RedirectAttributes redirectAttributes) {
        // No division here since we're now handling percentages consistently
        accountService.modifyInterestRate(newRate / 100);
        redirectAttributes.addAttribute("message", "Interest rate changed to " + newRate + "%");
        return "redirect:/";
    }

    @PostMapping("/add-account")
    public String addAccount(
        @RequestParam String customerName,
        @RequestParam double balance,
        RedirectAttributes redirectAttributes) {
        
        try {
            accountService.addAccount(customerName.trim(), balance);
            redirectAttributes.addAttribute("message", "Account added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        }
        return "redirect:/";
    }
    
 // Add to SavingsAccountController.java
    @GetMapping("/projection/{customerName}")
    public String showProjection(@PathVariable String customerName, Model model) {
        try {
            Map<Integer, Double> projection = accountService.calculateYearlyProjection(customerName);
            model.addAttribute("projection", projection);
            model.addAttribute("customerName", customerName);
            
            // Add current balance for reference
            double currentBalance = accountService.getAllAccounts().stream()
                .filter(acc -> acc.getCustomerName().equalsIgnoreCase(customerName))
                .findFirst()
                .get()
                .getSavingsBalance();
            model.addAttribute("currentBalance", currentBalance);
            
            return "projection";
        } catch (IllegalArgumentException e) {
            return "redirect:/?message=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }
 // Add to SavingsAccountController.java

    @GetMapping("/history/{customerName}")
    public String showHistory(@PathVariable String customerName, Model model) {
        try {
            model.addAttribute("history", accountService.getTransactionHistory(customerName));
            model.addAttribute("customerName", customerName);
            return "history";
        } catch (IllegalArgumentException e) {
            return "redirect:/?message=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        }
    }
}