package com.example.savingsaccount.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//Create new class: src/main/java/com/example/savingsaccount/exception/GlobalExceptionHandler.java

@ControllerAdvice
public class GlobalExceptionHandler {

 @ExceptionHandler(Exception.class)
 public String handleAllExceptions(Exception ex, RedirectAttributes redirectAttributes) {
     redirectAttributes.addAttribute("message", "Error: " + ex.getMessage());
     return "redirect:/";
 }
 
 @ExceptionHandler(IllegalArgumentException.class)
 public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
     redirectAttributes.addAttribute("message", "Validation Error: " + ex.getMessage());
     return "redirect:/";
 }
}