package com.oneclickvideo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "one-click-video")
public class OneClickVideoProperties {

    private final Video video = new Video();
    private final Content content = new Content();

    public Video getVideo() {
        return video;
    }

    public Content getContent() {
        return content;
    }

    public static class Video {
        private int duration = 30;
        private int width = 1920;
        private int height = 1080;
        private String outputDir = "./generated-videos/";
        private String ffmpegCommand = "ffmpeg";
        private long ffmpegTimeoutSeconds = 120;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(String outputDir) {
            this.outputDir = outputDir;
        }

        public String getFfmpegCommand() {
            return ffmpegCommand;
        }

        public void setFfmpegCommand(String ffmpegCommand) {
            this.ffmpegCommand = ffmpegCommand;
        }

        public long getFfmpegTimeoutSeconds() {
            return ffmpegTimeoutSeconds;
        }

        public void setFfmpegTimeoutSeconds(long ffmpegTimeoutSeconds) {
            this.ffmpegTimeoutSeconds = ffmpegTimeoutSeconds;
        }
    }

    public static class Content {
        private boolean safetyFocused = true;
        private boolean kidFriendly = true;
        private String channelName = "Riding Roney";

        public boolean isSafetyFocused() {
            return safetyFocused;
        }

        public void setSafetyFocused(boolean safetyFocused) {
            this.safetyFocused = safetyFocused;
        }

        public boolean isKidFriendly() {
            return kidFriendly;
        }

        public void setKidFriendly(boolean kidFriendly) {
            this.kidFriendly = kidFriendly;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }
    }
}
