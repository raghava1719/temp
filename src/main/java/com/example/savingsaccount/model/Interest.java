package com.example.savingsaccount.model;

public class Interest {

    private static double annualInterestRate;

    public static void modifyInterestRate(double newRate) {
    	if(newRate<0) {
    		throw new IllegalArgumentException("Interest rate cannot be less than zero");
    	}
    	else
        annualInterestRate = newRate;
    }
    
    public static double getAnnualInterestRate() {
        return annualInterestRate;
    }
}
