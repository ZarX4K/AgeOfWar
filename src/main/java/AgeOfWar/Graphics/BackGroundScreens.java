package AgeOfWar.Graphics;

import javax.swing.*;
import java.awt.*;

public class BackGroundScreens extends JPanel {
    private ImageIcon introImageIcon;
    private Image backgroundImage;
    private Image endScreenA;
    private Image endScreenB;


    public BackGroundScreens() {
        loadIntroScreenImage();
        loadBackgroundScreenImage();
        loadEndScreenA();
        loadEndScreenB();
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

    private void loadEndScreenA() {
        try {
            endScreenA = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/PlayerAWon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadEndScreenB() {
        try {
            endScreenB = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/PlayerBWon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image getEndScreenA() {
        return endScreenA;
    }

    public Image getEndScreenB() {
        return endScreenB;
    }

    public ImageIcon getIntroImageIcon() {
        return introImageIcon;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }


}
