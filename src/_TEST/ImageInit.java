package _TEST;

import java.awt.*;

public class ImageInit {
    Image image;
    int x,y,w,h;
    int psleeps = 3;
    boolean pup = false;
    int speed = 5;

    public ImageInit(int x, int y, Image image){
        this.x = x;
        this.y = y;
        this.image = image;
        w = image.getWidth(null);
        h = image.getHeight(null);
    }
    public void move(){
        this.y += speed;
    }
}
