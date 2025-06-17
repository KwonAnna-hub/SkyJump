package _TEST;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GameSkyJump {
    public static void main(String[] args){
        Gamescreen win = new Gamescreen();

        win.setTitle("SkyJump");
        win.mainpanel = new Mainpanel(win);

        win.add(win.mainpanel);
        win.setBounds(0,0, 720,1310);
        win.setResizable(false);//창 크기 변경 못하도록
        win.setLocationRelativeTo(null);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//JFrame이 정상적으로 종료되도록

        win.setVisible(true);

        Music("src/sounds/Raon-Rau_bg.wav");
    }
    public static void Music(String file){
        Clip clip;
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            clip.loop(-1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class Mainpanel extends JPanel {
    private ImagePanel span;
    private ImagePanel epan;
    private ImagePanel backgroundpan;
    private ImagePanel title;
    private ImagePanel ground;
    private static Gamescreen win;

    public Mainpanel(Gamescreen win){
        this.win = win;
        setLayout(null);

        span = new ImagePanel(new ImageIcon("src/images/start.png").getImage(),(720-350)/2,800);
        epan = new ImagePanel(new ImageIcon("src/images/exit.png").getImage(), 610, 5);
        title = new ImagePanel(new ImageIcon("src/images/title.png").getImage(),(720-467)/2,250);
        backgroundpan = new ImagePanel(new ImageIcon("src/images/background.png").getImage(),0,0);
        ground = new ImagePanel(new ImageIcon("src/images/ground.png").getImage(),0,1143);

        add(span);
        add(epan);
        add(title);
        add(ground);
        add(backgroundpan);

        span.addMouseListener(new spanMouse());
        epan.addMouseListener(new epanMouse());

    }

    static class spanMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            win.change("gamepanel");
            win.gamepanel.Sound("src/sounds/start.wav");
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    static class epanMouse implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            win.dispatchEvent(new WindowEvent(win, WindowEvent.WINDOW_CLOSING));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

class Gamepanel extends JPanel{
    private Gamescreen win;
    private ImageInit ground = new ImageInit(0,1143, new ImageIcon("src/images/ground.png").getImage() );
    private ImagePanel backgroundpan = new ImagePanel(new ImageIcon("src/images/background.png").getImage(),0,0);
    private ImageInit player = new ImageInit((backgroundpan.getWidth() - 132)/2,backgroundpan.getHeight() - ground.h - 135, new ImageIcon("src/images/player.png").getImage());

    private ArrayList<ImageInit> cloudsList = new ArrayList<ImageInit>();
    private ArrayList<ImageInit> clouds2List = new ArrayList<ImageInit>();
    private Image cloudsimg = new ImageIcon("src/images/cloud1.png").getImage();
    private Image clouds2img = new ImageIcon("src/images/cloud2.png").getImage();
    private Image wormimg = new ImageIcon("src/images/worm.png").getImage();
    private Image wingsimg = new ImageIcon("src/images/wings.png").getImage();
    private ImageInit clouds, clouds2, worm, wings;
    private boolean isplayerwings = false;
    private boolean isGameOver = false;
    private boolean isWorm = false;
    private ArrayList<Footboard> footboardsList = new ArrayList<Footboard>();
    private Footboard footboards, footboardscrash, footboardsmoveout, footboardsjump, minimum;
    private int footDownCount;
    private int before = 0;
    int score = 0;

    public void cloudsAppear(){
        if(clouds2List.isEmpty()){
            for(int i = 0; i < 3; i++){
                clouds2 = new ImageInit((int)(Math.random()*600),(int)(Math.random()*330)+(330*i), clouds2img);
                clouds2List.add(clouds2);
                clouds2.speed = 1;
            }
        }
        if(clouds2List.size() < 3){
            clouds2 = new ImageInit(((int)(Math.random() * 600)), -100, clouds2img);
            clouds2List.add(clouds2);
            clouds2.speed = 1;
        }

        if(cloudsList.isEmpty()){
            clouds = new ImageInit((int)(Math.random () * 265),200, cloudsimg);
            cloudsList.add(clouds);
            clouds.speed = 2;
            clouds = new ImageInit((int)(Math.random () * 265 * 2),800, cloudsimg);
            cloudsList.add(clouds);
            clouds.speed = 2;
        }
        if(cloudsList.size() < 2){
            clouds = new ImageInit(((int)(Math.random() * 500)), -200, cloudsimg);
            cloudsList.add(clouds);
            clouds.speed = 2;
        }
    }

    public void footboardsAppear(){
        if(footboardsList.isEmpty()){
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 4; j++){
                    footboards = new Footboard((int) (Math.random() * 260) + (i * 260), (int) (Math.random() * 260) + (j * 260));
                    footboardsList.add(footboards);
                }
            }
            footboards = new Footboard((int) (Math.random() * 530), -45);
            footboardsList.add(footboards);
            for(int i = 0; i < footboardsList.size(); i++){
                footboards = footboardsList.get(i);
                for(int j = 0; j < footboardsList.size(); j++){
                    if(i != j){
                        footboardscrash = footboardsList.get(j);
                        if(footboards.isCrash(footboardscrash)) {
                            footboardsList.remove(footboardscrash);
                            repaint();
                            break;
                        }
                    }
                }
            }
        }

        if(score != 0){
            int prob = (int)(Math.random() * 100);
            if(prob > 20){
                footboards = new Footboard((int) (Math.random() * 531), -100);
                if(score < 1000 && (Footboard.movecount) % 8 == 0){
                    footboardsList.add(footboards);
                }else if(score >= 1000 && score < 2000 && (Footboard.movecount) % 12 == 0){
                    footboardsList.add(footboards);
                }else if(score >= 2000 && score < 3500 && (Footboard.movecount) % 20 == 0){
                    footboardsList.add(footboards);
                }else if(score >= 3500 && score < 5000 && (Footboard.movecount) % 30 == 0) {
                    footboardsList.add(footboards);
                }else if(score >= 5000 && (Footboard.movecount) % 40 == 0) {
                    footboardsList.add(footboards);
                }
            }
        }
    }

    public void wormAppear(){
        int random = (int)(Math.random() * footboardsList.size());
        int prob = (int)(Math.random() * 100);
        if(worm == null && footboardsList.get(random).y < 0 && footboardsList.get(random).y > - 50){
            worm = new ImageInit(footboardsList.get(random).x + 10, footboardsList.get(random).y - 67, wormimg);
            if(score < 2000 && prob < 3){
            }else if(score >= 2000 && score < 5000 && prob < 20){
            } else if (score >= 5000 && score < 10000 && prob < 30) {
            } else if(score >= 10000 && prob < 40){
            }else{worm = null;}
        }
    }

    public void wingsAppear(){
        int random = (int)(Math.random() * footboardsList.size());
        int prob = (int)(Math.random() * 100);
        if(worm != null && wings == null && footboardsList.get(random).y < 0 && footboardsList.get(random).y > - 50){
            wings = new ImageInit(footboardsList.get(random).x + 10, footboardsList.get(random).y - 67, wingsimg);
            if(!(wings.x + wings.w > worm.x && wings.x < worm.x + worm.w && wings.y < worm.y + worm.h && wings.y + wings.h> worm.y)){
                if(score > 500 && score < 1500 && prob < 40){
                }else if(score >= 1500 && score < 3000 && prob < 35){
                }else if (score >= 3000 && score < 5000 && prob < 30) {
                }else if (score >= 5000 && score < 10000 && prob < 20) {
                }else if(score >= 10000 && prob < 10){
                }else{wings = null;}
            }else{wings = null;}
        }
    }

    public void paint(Graphics g){
        super.paint(g);//캔버스 비우기
        cloudsAppear();
        for(int i = 0; i < clouds2List.size(); i++){
            clouds2 = clouds2List.get(i);
            g.drawImage(clouds2.image, clouds2.x, clouds2.y, null);
        }
        for(int i = 0; i < cloudsList.size(); i++){
            clouds = cloudsList.get(i);
            g.drawImage(clouds.image, clouds.x, clouds.y, null);
        }
        g.drawImage(ground.image,ground.x,ground.y,null);
        for(int i = 0; i < footboardsList.size(); i++){
            footboards = footboardsList.get(i);
            for(int j = 0; j < footboardsList.size(); j++){
                if(i != j && footboards.y <= - 50){
                    footboardscrash = footboardsList.get(j);
                    if(footboards.isCrash(footboardscrash)) {
                        footboardsList.remove(footboardscrash);
                        repaint();
                        break;
                    }
                }
                g.drawImage(footboards.image, footboards.x, footboards.y, null);
            }
        }
        wormAppear();
        wingsAppear();
        if(worm !=null){g.drawImage(worm.image, worm.x, worm.y, null);}
        if(wings !=null){
            g.drawImage(wings.image, wings.x, wings.y, null);
        }
        g.drawImage(player.image,player.x,player.y,null);//이미지그리기
        scoreDraw(g);
    }
    public void scoreDraw(Graphics g){
        g.setColor(Color.PINK);
        g.setFont(new Font("맑은 고딕", Font.BOLD, 65));
        g.drawString("SCORE : " + score, 30, 70);
    }

    public Gamepanel(Gamescreen win){
        this.win = win;
        setLayout(null);
        setFocusable(true);

        Thread nt = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isGameOver){
                    footboardsAppear();
                    repaint();
                    try {
                        Thread.sleep(7);
                        player.y -= player.speed;
                        player.pup = true;
                        crashCheck();
                        if(player.y < backgroundpan.getHeight()/2 - 100){
                            if(Footboard.moving){ player.y -= player.speed; for(int i = 0; i < footDownCount; i++){Thread.sleep(player.psleeps);}}
                            player.pup = false;
                            while (!isGameOver && (player.y < ground.y - player.h) && !player.pup){
                                footboardsAppear();
                                repaint();
                                Thread.sleep(9);
                                player.y += player.speed;
                                crashCheck();
                                for(int i = 0; i < footboardsList.size(); i++){
                                    footboardsjump = footboardsList.get(i);
                                    if(((player.x + player.w - 50 > footboardsjump.x && player.x + 50 < footboardsjump.x + footboardsjump.w) && (footboardsjump.y <= player.y + player.h) && (footboardsjump.y + player.speed >= player.y + player.h) && !isWorm)){
                                        player.pup = true;
                                        footDownCount = ((1100 - footboardsjump.y + footboardsjump.h) / 5);//플레이어가 뛰기 시작하는 곳으로부터 발판의 높이를 5로 나눔(발판이 내려갈 횟수 결정)
                                        for(int j = 0; j < cloudsList.size(); j++){
                                            clouds = cloudsList.get(j);
                                            clouds.x += 3;
                                        }
                                        for(int j = 0; j < clouds2List.size(); j++){
                                            clouds2 = clouds2List.get(j);
                                            clouds2.x += 1;
                                        }
                                        break;
                                    }else{
                                        player.pup = false;
                                        footDownCount = 0;
                                    }
                                }
                            }
                            if((player.pup && !Footboard.moving)){
                                Thread footcrash = getFootcrash(footDownCount);
                                footcrash.start();
                            }
                        }
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
                GameOver();
                //충돌 감지(, 플레이어가 벌레와 충돌시 Gameover, !isplayerwings 일때만
                //충돌 감지, 플레이어가 wings와 충돌시 플레이어 이미지 바꾸기, sleep 줄여서 속도 높이기, playerY<backgroundpan.getHeight()/2일때 배경(발판, 구름) 원래보다 빨리 움직이기
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                player.x = e.getX() - player.w/2;
            }
        });

        add(backgroundpan);
        nt.setDaemon(true);//메인 종료할 떄 같이 종료
        nt.start();//스레드 실행
    }

    int c = 0;
    private Thread getFootcrash(int Count) {
        Thread footcrash = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isGameOver){ Sound("src/sounds/jump.wav"); }
                    for(int j = 0; j < Count; j++){
                        Thread.sleep(8 - player.psleeps);
                        Footboard.moving = true;
                        upup();
                    }
                    while (isplayerwings){
                        Thread.sleep(8 - player.psleeps);
                        upup();
                        c++;
                    }
                    Footboard.moving = false;
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });
        footcrash.setDaemon(true);
        return footcrash;
    }

    public  void upup(){
        ground.move();
        if(worm != null) {
            worm.move();
            if (worm.y > backgroundpan.getHeight()) {
                worm = null;
            }
        }
        if(wings != null){
            wings.move();
            if(wings.y > backgroundpan.getHeight()){
                wings = null;
            }
        }
        if(!footboardsList.isEmpty()){
            for(int i = 0; i < footboardsList.size(); i++){
                footboardsmoveout = footboardsList.get(i);
                footboardsmoveout.move();
                if(footboardsmoveout.y > backgroundpan.getHeight()){
                    footboardsList.remove(footboardsmoveout);
                }
                repaint();
            }
        }
        for(int i = 0; i < cloudsList.size(); i++){
            clouds = cloudsList.get(i);
            clouds.move();
            if(clouds.y > backgroundpan.getHeight() || clouds.x > backgroundpan.getWidth()){
                cloudsList.remove(clouds);
            }
        }
        for(int i = 0; i < clouds2List.size(); i++){
            clouds2 = clouds2List.get(i);
            clouds2.move();
            if(clouds2.y > backgroundpan.getHeight() || clouds2.x > backgroundpan.getWidth()){
                clouds2List.remove(clouds2);
            }
        }
        score++;
        crashCheck();
        c++;
        if(c > 70){//밟을 수 있는 발판이 없는 상황 방지
            minimum = new Footboard((int) (Math.random() * 531), -50);
            footboardsList.add(minimum);
            c = 0;
        }
    }

    public void crashCheck(){//충돌감지
        if(worm != null && !isplayerwings) {
            if (player.x + player.w > worm.x + 50 && player.x < worm.x + worm.w - 50 && player.y < worm.y + worm.h - 50 && player.y + player.h> worm.y + 50) {
                worm = null;
                Sound("src/sounds/wormcrash.wav");
                player.image = new ImageIcon("src/images/wormcrash.png").getImage();
                player.speed = 15;
                isWorm = true;
            }
        }
        if( wings != null) {
            if (wings.x + wings.w > player.x + 50 && wings.x < player.x + player.w - 50 && wings.y < player.y + player.h - 50 && wings.y + wings.h > player.y + 75) {
                before = score;
                player.psleeps = 7;
                player.image = new ImageIcon("src/images/player_wings.png").getImage();
                wings = null;
                isplayerwings = true;
                Sound("src/sounds/wings.wav");
            }
        }
        if(score != 200 && score - before == 200){
            player.psleeps = 3;
            player.image = new ImageIcon("src/images/player.png").getImage();
            before = 0;
            isplayerwings = false;
        }
        if(player.y > 1280){
            isGameOver = true;
        }

        //플레이어 이미지 변경, 플레이어 위치 상승폭 증가
        //점수 상승 전 점수와 일정 점수 이상 차이나면 플레이어 원래이미지로 돌아오고 끝내기
    }

    public void Sound(String file) {
        Clip clip;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void GameOver(){
        Overpanel.score = score;
        Sound("src/sounds/worm.wav");
        player.y = backgroundpan.getHeight() - ground.h - 135;
        score = 0;
        win.change("overpanel");
    }
}

class Overpanel extends JPanel{
    static int score = 0;
    private ImagePanel overground = new ImagePanel(new ImageIcon("src/images/overground.png").getImage(),0,0);
    private ImagePanel backgroundpan = new ImagePanel(new ImageIcon("src/images/background.png").getImage(),0,0);
    private ImagePanel gameover = new ImagePanel(new ImageIcon("src/images/gameover.png").getImage(),(backgroundpan.getWidth() - 461) / 2,250);
    private ImagePanel replay = new ImagePanel(new ImageIcon("src/images/replay.png").getImage(),(backgroundpan.getWidth() - 350) / 2,750);
    private ImagePanel epan = new ImagePanel(new ImageIcon("src/images/exit.png").getImage(),610,5);

    public Overpanel(){
        setLayout(null);
        setFocusable(true);

        add(replay);
        add(epan);
        add(gameover);
        add(overground);
        add(backgroundpan);

        replay.addMouseListener(new Mainpanel.spanMouse());
        epan.addMouseListener(new Mainpanel.epanMouse());
    }

    public void paint(Graphics g){
        super.paint(g);
        scoreDraw(g);
    }
    public void scoreDraw(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("맑은 고딕", Font.BOLD, 50));
        g.drawString("LAST SCORE : " + score, 140, 1100);
    }
}

class Gamescreen extends JFrame{
    public Mainpanel mainpanel = null;
    public Gamepanel gamepanel = null;
    public Overpanel overpanel = null;

    public void change(String panelName){
        if(panelName.equals("gamepanel")){
            getContentPane().removeAll();
            gamepanel = new Gamepanel(this);
            getContentPane().add(gamepanel);
            revalidate();
            repaint();
        }else if(panelName.equals("mainpanel")){
            getContentPane().removeAll();
            mainpanel = new Mainpanel(this);
            getContentPane().add(mainpanel);
            revalidate();
            repaint();
        }else if(panelName.equals("overpanel")){
            getContentPane().removeAll();
            overpanel = new Overpanel();
            getContentPane().add(overpanel);
            revalidate();
            repaint();
        }
    }
}