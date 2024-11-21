package AgeOfWar.Graphics;

import javax.swing.*;
import java.awt.*;

public class BackGroundScreens extends JPanel {
    private ImageIcon introImageIcon;
    private Image backgroundImage;

    public BackGroundScreens() {
        loadIntroScreenImage();
        loadBackgroundScreenImage();
    }

    private void loadIntroScreenImage() {
        // Load GIF as ImageIcon for animation support
        introImageIcon = new ImageIcon(getClass().getResource("/AgeOfWarIntro.gif"));
    }

    private void loadBackgroundScreenImage() {
        try {
            backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GameBackgroundF.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getIntroImageIcon() {
        return introImageIcon;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }


}
