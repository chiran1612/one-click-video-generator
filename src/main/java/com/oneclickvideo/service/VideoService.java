package com.oneclickvideo.service;

import com.oneclickvideo.config.OneClickVideoProperties;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Video Service for One Click Video Generator
 *
 * Generates riding-themed videos with safety-focused content.
 */
@Service
public class VideoService {

    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final List<String> ridingStories = Arrays.asList(
        "Alex gears up for mountain trail adventure! Helmet on, knee pads secure. Safety first!",
        "BMX tricks time! Watch our rider perform safe stunts with proper protective gear.",
        "Forest trail challenge! Navigating rocks and roots while staying safe and having fun.",
        "Pump track mastery! Using body weight to gain speed and maintain perfect balance.",
        "Dirt jumping adventure! Controlled stunts with full safety equipment and smooth landings.",
        "Urban bike skills! City riding with ramps, stairs, and safe trick performances.",
        "Desert trail exploration! Sand dunes, cacti, and sunset riding with proper gear.",
        "Coastal bike adventure! Cliffside trails with ocean views and safety first approach.",
        "Night riding safety! Headlights, reflective gear, and proper trail navigation.",
        "Mountain summit challenge! Uphill climb with determination and downhill thrill ride."
    );

    private final List<String> videoTitles = Arrays.asList(
        "Epic Mountain Trail Adventure - Kids Bike Safety",
        "BMX Tricks and Stunts - Safe Riding for Kids",
        "Forest Trail Challenge - Mountain Biking Fun",
        "Pump Track Mastery - Kids Bike Skills",
        "Dirt Jumping Adventure - Safe Stunts for Kids",
        "Urban Bike Tricks - City Riding Fun",
        "Desert Trail Adventure - Kids Mountain Biking",
        "Coastal Bike Adventure - Ocean Trail Fun",
        "Night Riding Adventure - Kids Bike Safety",
        "Mountain Summit Challenge - Kids Adventure"
    );

    private final OneClickVideoProperties properties;
    private final Random random;

    public VideoService(OneClickVideoProperties properties) {
        this(properties, new Random());
    }

    VideoService(OneClickVideoProperties properties, Random random) {
        this.properties = properties;
        this.random = random;
    }

    public File generateRidingVideo() throws IOException {
        OneClickVideoProperties.Video videoProps = properties.getVideo();
        Path outputDir = Path.of(videoProps.getOutputDir()).normalize();
        Files.createDirectories(outputDir);

        String title = videoTitles.get(random.nextInt(videoTitles.size()));
        String story = ridingStories.get(random.nextInt(ridingStories.size()));
        String filename = buildFilename(title);
        Path outputPath = outputDir.resolve(filename);

        System.out.println("Generating video: " + title);
        System.out.println("Story: " + story);

        createVideoFile(outputPath, title, story);
        return outputPath.toFile();
    }

    private String buildFilename(String title) {
        String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9\\s-]", "").trim().replaceAll("\\s+", "-");
        String timestamp = LocalDateTime.now().format(FILE_TIMESTAMP);
        return sanitizedTitle + "-" + timestamp + ".mp4";
    }

    private void createVideoFile(Path filePath, String title, String story) throws IOException {
        List<BufferedImage> frames = new ArrayList<>();
        int frameCount = properties.getVideo().getDuration();

        for (int i = 0; i < frameCount; i++) {
            frames.add(createFrame(title, story, i, frameCount));
        }

        saveFramesAndEncodeVideo(frames, filePath);
    }

    private BufferedImage createFrame(String title, String story, int frameNumber, int frameCount) {
        OneClickVideoProperties.Video videoProps = properties.getVideo();
        int width = videoProps.getWidth();
        int height = videoProps.getHeight();

        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = frame.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(135, 206, 235),
            width, height, new Color(70, 130, 180)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString(properties.getContent().getChannelName(), 50, 100);

        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, Math.max(40, (width - titleWidth) / 2), 200);

        g2d.setFont(new Font("Arial", Font.PLAIN, 32));
        String[] words = story.split(" ");
        int startWord = Math.max(0, frameNumber - 5);
        int endWord = Math.min(words.length, startWord + 8);

        int y = Math.max(280, height / 3);
        for (int i = startWord; i < endWord; i++) {
            g2d.drawString(words[i], 100, y);
            y += 40;
        }

        if (properties.getContent().isSafetyFocused()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 28));
            g2d.setColor(new Color(255, 255, 0));
            g2d.drawString("Safety First! Always wear protective gear!", 100, height - 280);
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.setColor(Color.WHITE);
        g2d.drawString("Frame " + (frameNumber + 1) + "/" + frameCount, 50, height - 30);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        g2d.drawString("Generated: " + timestamp, Math.max(50, width - 420), height - 30);

        g2d.dispose();
        return frame;
    }

    private void saveFramesAndEncodeVideo(List<BufferedImage> frames, Path filePath) throws IOException {
        String baseName = filePath.getFileName().toString().replaceFirst("\\.mp4$", "");
        Path framesDir = filePath.getParent().resolve(baseName + "_frames");
        Files.createDirectories(framesDir);

        for (int i = 0; i < frames.size(); i++) {
            Path framePath = framesDir.resolve("frame_" + String.format("%03d", i) + ".png");
            ImageIO.write(frames.get(i), "PNG", framePath.toFile());
        }

        createVideoWithFFmpeg(framesDir, filePath);

        System.out.println("Video file created: " + filePath);
        System.out.println("Frame images saved to: " + framesDir);
        System.out.println("Frames generated: " + frames.size());
    }

    private void createVideoWithFFmpeg(Path framesDir, Path outputPath) throws IOException {
        OneClickVideoProperties.Video videoProps = properties.getVideo();
        ProcessBuilder pb = new ProcessBuilder(
            videoProps.getFfmpegCommand(),
            "-y",
            "-framerate", "1",
            "-i", framesDir.resolve("frame_%03d.png").toString(),
            "-c:v", "libx264",
            "-pix_fmt", "yuv420p",
            "-crf", "23",
            "-preset", "medium",
            outputPath.toString()
        );
        pb.redirectErrorStream(true);

        System.out.println("Creating video with FFmpeg...");
        System.out.println("Input frames: " + framesDir);
        System.out.println("Output video: " + outputPath);

        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new IOException(
                "FFmpeg is required to generate MP4 output. Configure one-click-video.video.ffmpeg-command " +
                    "or install ffmpeg on the server PATH.",
                e
            );
        }

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
                System.out.println("FFmpeg: " + line);
            }
        }

        boolean finished;
        try {
            finished = process.waitFor(videoProps.getFfmpegTimeoutSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            throw new IOException("Interrupted while waiting for FFmpeg to finish.", e);
        }

        if (!finished) {
            process.destroyForcibly();
            throw new IOException("FFmpeg timed out after " + videoProps.getFfmpegTimeoutSeconds() + " seconds.");
        }

        if (process.exitValue() != 0) {
            throw new IOException("FFmpeg failed with exit code " + process.exitValue() + "." +
                System.lineSeparator() + output);
        }

        if (!Files.exists(outputPath) || Files.size(outputPath) == 0) {
            throw new IOException("FFmpeg reported success but no video file was created.");
        }
    }
}
