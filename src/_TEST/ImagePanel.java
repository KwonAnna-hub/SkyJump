package _TEST;

import javax.swing.*;
import java.awt.*;

class ImagePanel extends JPanel {
    Image img;

    public ImagePanel(Image img, int x, int y) {
        this.img = img;
        setBounds(x, y, img.getWidth(null), img.getHeight(null));
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
