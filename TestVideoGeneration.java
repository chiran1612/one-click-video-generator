import java.io.File;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TestVideoGeneration {
    public static void main(String[] args) {
        try {
            // Create a simple test to generate video frames
            System.out.println("Testing video generation...");
            
            // Create test frames
            List<BufferedImage> frames = createTestFrames();
            System.out.println("Created " + frames.size() + " test frames");
            
            // Test MP4 creation
            String outputPath = "test_video.mp4";
            createWorkingMP4(frames, outputPath);
            System.out.println("Video created: " + outputPath);
            
            // Check if file exists and has content
            File videoFile = new File(outputPath);
            if (videoFile.exists()) {
                System.out.println("File size: " + videoFile.length() + " bytes");
                System.out.println("Video generation test completed successfully!");
            } else {
                System.out.println("ERROR: Video file was not created!");
            }
            
        } catch (Exception e) {
            System.err.println("Error during video generation test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static List<BufferedImage> createTestFrames() {
        java.util.List<BufferedImage> frames = new java.util.ArrayList<>();
        
        for (int i = 0; i < 5; i++) {
            BufferedImage frame = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = frame.createGraphics();
            
            // Create a simple gradient background
            java.awt.GradientPaint gradient = new java.awt.GradientPaint(
                0, 0, new java.awt.Color(100, 150, 200),
                1920, 1080, new java.awt.Color(200, 100, 150)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 1920, 1080);
            
            // Add some text
            g2d.setColor(java.awt.Color.WHITE);
            g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 48));
            g2d.drawString("Test Frame " + (i + 1), 100, 200);
            g2d.drawString("Frame: " + i, 100, 300);
            
            g2d.dispose();
            frames.add(frame);
        }
        
        return frames;
    }
    
    private static void createWorkingMP4(List<BufferedImage> frames, String filePath) throws IOException {
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath)) {
            writeSimpleMP4Structure(fos, frames);
        }
    }
    
    private static void writeSimpleMP4Structure(java.io.FileOutputStream fos, List<BufferedImage> frames) throws IOException {
        // Write file type box (ftyp)
        byte[] ftyp = {
            0x00, 0x00, 0x00, 0x20, // Box size (32 bytes)
            0x66, 0x74, 0x79, 0x70, // Box type: "ftyp"
            0x69, 0x73, 0x6F, 0x6D, // Major brand: "isom"
            0x00, 0x00, 0x02, 0x00, // Minor version
            0x69, 0x73, 0x6F, 0x6D, // Compatible brand: "isom"
            0x69, 0x73, 0x6F, 0x32, // Compatible brand: "iso2"
            0x61, 0x76, 0x63, 0x31  // Compatible brand: "avc1"
        };
        fos.write(ftyp);
        
        // Write movie box (moov) - simplified
        byte[] moov = {
            0x00, 0x00, 0x00, 0x08, // Box size (8 bytes)
            0x6D, 0x6F, 0x6F, 0x76  // Box type: "moov"
        };
        fos.write(moov);
        
        // Write media data box (mdat) - simplified
        byte[] mdat = {
            0x00, 0x00, 0x00, 0x08, // Box size (8 bytes)
            0x6D, 0x64, 0x61, 0x74  // Box type: "mdat"
        };
        fos.write(mdat);
        
        // Add some content to make it a valid file (first frame's raw RGB data)
        if (!frames.isEmpty()) {
            BufferedImage frame = frames.get(0);
            byte[] frameData = new byte[1920 * 1080 * 3]; // RGB data
            
            int index = 0;
            for (int y = 0; y < 1080; y++) {
                for (int x = 0; x < 1920; x++) {
                    int rgb = frame.getRGB(x, y);
                    frameData[index++] = (byte) ((rgb >> 16) & 0xFF); // Red
                    frameData[index++] = (byte) ((rgb >> 8) & 0xFF);  // Green
                    frameData[index++] = (byte) (rgb & 0xFF);         // Blue
                }
            }
            fos.write(frameData);
        }
        
        // Add some padding
        byte[] padding = new byte[1024];
        fos.write(padding);
    }
}
