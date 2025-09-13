package com.oneclickvideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * One Click Video Generator Application
 * 
 * One-click riding animation video generator for YouTube
 * Generates 30-second riding videos with safety-focused content
 */
@SpringBootApplication
public class OneClickVideoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OneClickVideoApplication.class, args);
        System.out.println("🚴‍♂️ One Click Video Generator started!");
        System.out.println("Open: http://localhost:8080");
    }
}
