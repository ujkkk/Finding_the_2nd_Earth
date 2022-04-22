package com.cookandroid.spacegame;


import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.Vector;

public class Character {

    int x,y, w,h;
    boolean flag = true;
    boolean stop;
    int speed;
    static boolean crush = false;
    Bitmap img;
    //static Polygon polygon = new Polygon();

    Vector<Rect> polyVector;
    Rect myRect = new Rect();


    public static boolean up, down, right, left;

    public Character(int x, int y, int w, int h, int speed,Bitmap img, Vector<Rect> polyVector) {

        this.x=x; this.y=y;
        this.w=w; this.h=h;
        this.speed = speed;
        this.polyVector = polyVector;
        this.img= img;


        findMyArea(x,y ,w ,h);
        //point = new Point(x, y);
       // Thread th = new Thread(this);
       // th.start();
        init();
        //img = icon.getImage();

    }

    public void init() {
        up = false;
        down = false;
        left = false;
        right = false;
    }
    // 자기 영역의 polygon 찾기
    public boolean findMyArea(int x,int y, int w, int h) {
        for (int i = 0; i < polyVector.size(); i++) {
            Rect rect = polyVector.get(i);
            if (rect.contains(x, y,x+ w, y+h)) {
                if(myRect != rect) {
                    this.myRect = rect;
                    return true;
                }

            }
        }
        return false;
    }

    public void process() {

        if (!myRect.contains(x, y, x+ w, y+h)) {
            if (up && findMyArea(x+w/2, y-speed, 1, 1));
            else if (down && findMyArea(x+w/2, y+h+h/2, 1, 1));
            else if (right && findMyArea(x+w+w/2, y+h/2, 1, 1));
            else if (left && findMyArea(x-speed, y+h/2, 1, 1));
            else {
                crush = true;
                crushCheck();
            }
        }
        else {
            crush = false;
        }
        keyProcess();
    }

    //벽과의 충돌 체크
    public void crushCheck() {
        if (crush) {
            if (this.x - w/5< myRect.left  && this.x- w/5< myRect.right){
                setLeft(false);
                Log.d("Touch", "left 충돌");

            }
            if (this.x + w +5 > myRect.left && this.x + w+5 > myRect.left && this.x + w+5 > myRect.right
                    && this.x + w+5 >= myRect.right){
                setRight(false);
                Log.d("Touch", "right 충돌");

            }
            if (this.y < myRect.top && this.y < myRect.bottom && this.y < myRect.bottom
                    && this.y <= myRect.top){
                setUp(false);
                Log.d("Touch", "up 충돌");

            }
            if (this.y + h > myRect.top && this.y + h > myRect.bottom && this.y + h > myRect.bottom
                    && this.y + h >= myRect.top){
                setDown(false);
                Log.d("Touch", "down 충돌");

            }
        }
    }


    public void keyProcess() {

        if (up && y - speed > 0) {
            y -= speed;
        }
        if (down && x - speed > 0) {
            y += speed;
        }
        if (left && x - speed > 0) {
            x -= speed;
        }
        if (right && x - speed > 0) {
            x += speed;
        }
    }

    public static void setUp(boolean up) {
        Character.up = up;
    }

    public static void setDown(boolean down) {
        Character.down = down;
    }

    public static void setLeft(boolean left) {
        Character.left = left;
    }

    public static void setRight(boolean right) {
        Character.right = right;
    }

}
//
//class KeyListener extends KeyAdapter {
//    @Override
//    public void keyPressed(KeyEvent e) { // 키를 눌렀을 때
//
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_S:
//                Character.setDown(true);
//                break;
//            case KeyEvent.VK_W:
//                Character.setUp(true);
//                break;
//            case KeyEvent.VK_A:
//                Character.setLeft(true);
//                break;
//            case KeyEvent.VK_D:
//                Character.setRight(true);
//                break;
//            case KeyEvent.VK_ESCAPE:
//                System.exit(0);
//                break;
//        }
//
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) { // 키를 뗴었을 때
//
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_S:
//                Character.setDown(false);
//                break;
//            case KeyEvent.VK_W:
//                Character.setUp(false);
//                break;
//            case KeyEvent.VK_A:
//                Character.setLeft(false);
//                break;
//            case KeyEvent.VK_D:
//                Character.setRight(false);
//                break;
//
//        }
//    }
//}
