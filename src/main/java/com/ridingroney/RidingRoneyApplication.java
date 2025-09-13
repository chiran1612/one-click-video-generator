package com.ridingroney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Riding Roney Video Generator Application
 * 
 * One-click riding animation video generator for YouTube
 * Generates 30-second riding videos with safety-focused content
 */
@SpringBootApplication
public class RidingRoneyApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(RidingRoneyApplication.class, args);
        System.out.println("🚴‍♂️ Riding Roney Video Generator started!");
        System.out.println("Open: http://localhost:8080");
    }
}
