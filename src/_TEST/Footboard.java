package _TEST;

import javax.swing.*;
import java.awt.*;

public class Footboard{
    Image image = new ImageIcon("src/images/footboard.png").getImage();
    int x,y;
    int w = image.getWidth(null);
    int h = image.getHeight(null);
    static int speed = 5;
    static int movecount;
    static boolean moving;

    public Footboard(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void move(){
        this.y += speed;
        this.movecount++;
    }

    public boolean isCrash(Footboard this, Footboard crash){
        if(this.x + this.w > crash.x && this.x < crash.x + crash.w && this.y < crash.y + crash.h && this.y + this.h > crash.y){
            return true;
        }else{ return false; }
    }
}
