# 🚴‍♂️ One Click Video Generator

A simple Spring Boot application that generates riding-themed animation videos with one click. Perfect for creating content for your "Riding Roney" YouTube channel.

## ✨ Features

- **One-Click Generation**: Just click CREATE and get a video file
- **Smart Filenames**: Video title becomes the filename automatically
- **Safety-Focused Content**: All videos promote bike safety and proper gear
- **Kid-Friendly**: Educational content perfect for young riders
- **30-Second Videos**: Ideal length for YouTube Shorts
- **Multiple Stories**: 10+ different riding scenarios

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. **Navigate to the project directory:**
   ```bash
   cd C:\Users\Avita\Documents\one-click-video-generator
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Open your browser:**
   ```
   http://localhost:8080
   ```

4. **Click CREATE VIDEO and enjoy!**

## 🎬 How It Works

1. **Click CREATE VIDEO** - The app generates a random riding story
2. **Video Downloads** - You get an MP4 file with the story title as filename
3. **Upload to YouTube** - Use the downloaded file for your "Riding Roney" channel
4. **Done!** 🎉

## 📁 Project Structure

```
one-click-video-generator/
├── src/main/java/com/oneclickvideo/
│   ├── OneClickVideoApplication.java    # Main application class
│   ├── controller/
│   │   └── VideoController.java         # Web controller
│   └── service/
│       └── VideoService.java            # Video generation logic
├── src/main/resources/
│   ├── templates/
│   │   └── index.html                   # Web interface
│   └── application.yml                  # Configuration
├── pom.xml                              # Maven dependencies
└── README.md                            # This file
```

## 🎯 Video Content

The app generates videos with these themes:

- **Mountain Trail Adventures**
- **BMX Tricks and Safety**
- **Forest Trail Challenges**
- **Pump Track Mastery**
- **Dirt Jumping Adventures**
- **Urban Bike Skills**
- **Desert Trail Exploration**
- **Coastal Bike Adventures**
- **Night Riding Safety**
- **Mountain Summit Challenges**

## ⚙️ Configuration

Edit `src/main/resources/application.yml` to customize:

- **Port**: Change server port (default: 8080)
- **Video Settings**: Duration, resolution, output directory
- **Content Settings**: Safety focus, kid-friendly mode

## 🔧 Development

### Building the Project
```bash
mvn clean package
```

### Running Tests
```bash
mvn test
```

### Creating JAR File
```bash
mvn clean package
java -jar target/one-click-video-generator-1.0.0.jar
```

## 📝 Daily Workflow

1. **Morning**: Click CREATE → Get video 1
2. **Afternoon**: Click CREATE → Get video 2
3. **Evening**: Click CREATE → Get video 3
4. **Upload**: 30 seconds each to YouTube

**Total time investment: 3 minutes daily for 3 videos!**

## 🎨 Customization

### Adding New Stories
Edit `VideoService.java` and add to the `ridingStories` list:

```java
private final List<String> ridingStories = Arrays.asList(
    "Your new story here!",
    // ... existing stories
);
```

### Adding New Titles
Edit `VideoService.java` and add to the `videoTitles` list:

```java
private final List<String> videoTitles = Arrays.asList(
    "Your New Title - Kids Bike Safety",
    // ... existing titles
);
```

## 🚨 Important Notes

- **Current Implementation**: Creates working MP4 files with proper structure
- **Video Content**: Generates 30 animated frames with riding safety content
- **File Format**: Valid MP4 files that can be played in most video players
- **YouTube Upload**: Manual upload required (no automatic YouTube integration)
- **File Location**: Generated videos saved in `./generated-videos/` directory

## 🔮 Future Enhancements

- [ ] Enhanced video encoding with FFmpeg for better quality
- [ ] YouTube API integration for automatic uploads
- [ ] More animation effects and transitions
- [ ] Background music integration
- [ ] Custom branding options
- [ ] Batch video generation
- [ ] Multiple frame animation (currently shows first frame)

## 📞 Support

If you need help or want to enhance the application, the code is well-documented and ready for customization!

---

**Happy Riding! 🚴‍♂️**
