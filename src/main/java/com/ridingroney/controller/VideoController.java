package com.ridingroney.controller;

import com.ridingroney.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * Video Controller for Riding Roney Video Generator
 * 
 * Handles the main interface and video generation endpoints
 */
@Controller
public class VideoController {
    
    @Autowired
    private VideoService videoService;
    
    /**
     * Main page with CREATE button
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("channelName", "Riding Roney");
        return "index";
    }
    
    /**
     * CREATE button - generates and downloads video
     * 
     * This is the main functionality:
     * 1. Generate riding video with random story
     * 2. Create MP4 file with title as filename
     * 3. Return file for download
     */
    @PostMapping("/create")
    public ResponseEntity<Resource> createVideo() {
        try {
            System.out.println("🎬 Creating new riding video...");
            
            // Generate video file
            File videoFile = videoService.generateRidingVideo();
            
            System.out.println("✅ Video created: " + videoFile.getName());
            
            // Return file for download
            Resource resource = new FileSystemResource(videoFile);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + videoFile.getName() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            System.err.println("❌ Error creating video: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "🚴‍♂️ Riding Roney Video Generator is running!";
    }
}
