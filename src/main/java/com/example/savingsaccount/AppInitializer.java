package com.example.savingsaccount;

import com.example.savingsaccount.model.SavingsAccount;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // Set initial interest rate to 4%
        SavingsAccount.modifyInterestRate(0.04);
    }
}