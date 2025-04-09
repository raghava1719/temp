package com.example.savingsaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//Add @EnableCaching to your main application class
@SpringBootApplication
@EnableCaching
public class SavingsAccountAppApplication {
 public static void main(String[] args) {
     SpringApplication.run(SavingsAccountAppApplication.class, args);
 }
}
