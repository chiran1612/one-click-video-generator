package com.oneclickvideo.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OneClickVideoPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(TestConfig.class)
        .withConfiguration(org.springframework.boot.autoconfigure.AutoConfigurations.of(
            ConfigurationPropertiesAutoConfiguration.class
        ));

    @Test
    void bindsConfiguredValues() {
        contextRunner
            .withPropertyValues(
                "one-click-video.video.duration=12",
                "one-click-video.video.width=640",
                "one-click-video.video.height=360",
                "one-click-video.video.output-dir=build/out",
                "one-click-video.video.ffmpeg-command=custom-ffmpeg",
                "one-click-video.video.ffmpeg-timeout-seconds=9",
                "one-click-video.content.channel-name=Test Channel"
            )
            .run(context -> {
                OneClickVideoProperties properties = context.getBean(OneClickVideoProperties.class);
                assertEquals(12, properties.getVideo().getDuration());
                assertEquals(640, properties.getVideo().getWidth());
                assertEquals(360, properties.getVideo().getHeight());
                assertEquals("build/out", properties.getVideo().getOutputDir());
                assertEquals("custom-ffmpeg", properties.getVideo().getFfmpegCommand());
                assertEquals(9, properties.getVideo().getFfmpegTimeoutSeconds());
                assertEquals("Test Channel", properties.getContent().getChannelName());
            });
    }

    @Configuration
    @EnableConfigurationProperties(OneClickVideoProperties.class)
    static class TestConfig {
    }
}
