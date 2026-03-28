package com.oneclickvideo.controller;

import com.oneclickvideo.config.OneClickVideoProperties;
import com.oneclickvideo.service.VideoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

@Controller
public class VideoController {

    private final VideoService videoService;
    private final OneClickVideoProperties properties;

    public VideoController(VideoService videoService, OneClickVideoProperties properties) {
        this.videoService = videoService;
        this.properties = properties;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("channelName", properties.getContent().getChannelName());
        return "index";
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVideo() {
        try {
            System.out.println("Creating new riding video...");

            File videoFile = videoService.generateRidingVideo();
            Resource resource = new FileSystemResource(videoFile);

            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + videoFile.getName() + "\"")
                .body(resource);
        } catch (Exception e) {
            System.err.println("Error creating video: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Video generation failed: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "One Click Video Generator is running!";
    }
}
