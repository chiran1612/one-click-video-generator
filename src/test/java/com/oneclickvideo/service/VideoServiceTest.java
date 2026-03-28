package com.oneclickvideo.service;

import com.oneclickvideo.config.OneClickVideoProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VideoServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void generateRidingVideoUsesConfiguredSettingsBeforeEncoding() throws Exception {
        OneClickVideoProperties properties = new OneClickVideoProperties();
        properties.getVideo().setOutputDir(tempDir.toString());
        properties.getVideo().setDuration(3);
        properties.getVideo().setWidth(320);
        properties.getVideo().setHeight(240);
        properties.getVideo().setFfmpegCommand("missing-ffmpeg-command");
        properties.getContent().setChannelName("Config Driven Channel");

        VideoService service = new VideoService(properties, new Random(0));

        IOException error = assertThrows(IOException.class, service::generateRidingVideo);
        assertTrue(error.getMessage().contains("FFmpeg is required"));

        Path framesDir = Files.list(tempDir)
            .filter(Files::isDirectory)
            .findFirst()
            .orElseThrow();

        long frameCount = Files.list(framesDir)
            .filter(path -> path.getFileName().toString().endsWith(".png"))
            .count();
        assertEquals(3, frameCount);

        Path firstFrame = framesDir.resolve("frame_000.png");
        BufferedImage image = ImageIO.read(firstFrame.toFile());
        assertEquals(320, image.getWidth());
        assertEquals(240, image.getHeight());

        boolean hasMp4 = Files.list(tempDir)
            .anyMatch(path -> path.getFileName().toString().endsWith(".mp4"));
        assertFalse(hasMp4);
    }

    @Test
    void generateRidingVideoIncludesTimestampInFilename() throws Exception {
        OneClickVideoProperties properties = new OneClickVideoProperties();
        properties.getVideo().setOutputDir(tempDir.toString());
        properties.getVideo().setDuration(1);
        properties.getVideo().setWidth(160);
        properties.getVideo().setHeight(90);
        properties.getVideo().setFfmpegCommand("missing-ffmpeg-command");

        VideoService service = new VideoService(properties, new Random(0));

        assertThrows(IOException.class, service::generateRidingVideo);

        Path framesDir = Files.list(tempDir)
            .filter(Files::isDirectory)
            .min(Comparator.comparing(path -> path.getFileName().toString()))
            .orElseThrow();

        String name = framesDir.getFileName().toString();
        assertTrue(name.matches("[A-Za-z0-9-]+-\\d{8}-\\d{6}_frames"));
    }
}
