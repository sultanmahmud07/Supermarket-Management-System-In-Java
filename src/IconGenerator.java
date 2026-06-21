import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class IconGenerator {
    public static void main(String[] args) {
        String[] icons = {
            "logo.png", "userName.png", "password.png", "login.png", "reset.png",
            "base.png", "bookTypeManager.png", "add.png", "edit.png",
            "bookManager.png", "exit.png", "about.png", "sdtbu.png", "search.png"
        };
        
        File dir = new File("src/images");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        for (String iconName : icons) {
            generateIcon(iconName);
        }
        System.out.println("All missing icons generated in src/images/");
    }
    
    private static void generateIcon(String name) {
        int width = 16;
        int height = 16;
        if (name.equals("logo.png") || name.equals("sdtbu.png")) {
            width = 48;
            height = 48;
        }
        
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        // Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Randomish color based on name length
        Color bgColor = new Color(Math.abs(name.hashCode()) % 200, 
                                  100 + Math.abs(name.hashCode() * 3) % 150, 
                                  200);
        
        g2d.setColor(bgColor);
        g2d.fillRoundRect(2, 2, width - 4, height - 4, 10, 10);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, width / 2));
        
        // Draw the first letter of the name
        String text = name.substring(0, 1).toUpperCase();
        int stringWidth = g2d.getFontMetrics().stringWidth(text);
        g2d.drawString(text, (width - stringWidth) / 2, height / 2 + (width / 6));
        
        g2d.dispose();
        
        try {
            File file = new File("src/images/" + name);
            ImageIO.write(img, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
