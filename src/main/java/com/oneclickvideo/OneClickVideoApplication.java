package com.oneclickvideo;

import com.oneclickvideo.config.OneClickVideoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * One Click Video Generator Application
 * 
 * One-click riding animation video generator for YouTube
 * Generates 30-second riding videos with safety-focused content
 */
@SpringBootApplication
@EnableConfigurationProperties(OneClickVideoProperties.class)
public class OneClickVideoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OneClickVideoApplication.class, args);
        System.out.println("🚴‍♂️ One Click Video Generator started!");
        System.out.println("Open: http://localhost:8080");
    }
}
