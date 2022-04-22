package com.cookandroid.spacegame;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.Vector;


public class Enemy {
    int x,y, w,h;
    int speed;
    String type;
    boolean crush = false;
    boolean direction = true;
    Bitmap img;
    Vector<Rect> polyVector;
    Rect myRect = new Rect();

    public Enemy (int x, int y, int w, int h, int speed, String type, Bitmap img, Vector<Rect> polyVector) {

        this.x=x; this.y=y;
        this.w=w; this.h=h;
        this.speed = speed;
        this.type = type;
        this.polyVector = polyVector;
        this.img= img;
        findMyArea();

    }
    public void findMyArea() {
        for(int i = 0; i< polyVector.size(); i++) {
            Rect rect = polyVector.get(i);
            if(rect.contains(x , y , x+w, y+h))
                myRect = rect;

        }
        Log.d("Enemy", "1.enemy : " + this.x + "," + this.y);
        Log.d("Enemy", "2MyRect : " + myRect.left + "," + myRect.top +
                ",(" + myRect.right + "," + myRect.top +")");
    }

    public void process() {

        switch (type) {
            //좌우만 움직이는 적
            case "LeftRight":
                if(crush) {
                    //벽과 부딪히면 방향 바꿔주기
                    crush = false;
                    if(direction)
                        direction = false;
                    else
                        direction = true;
                }
                if(direction)
                    this.x+= speed;
                else
                    this.x -= speed;
                break;
            //상하 움직이는 적
            case "UpDown":
                if(crush) {
                    //벽과 부딪히면 방향 바꿔주기
                    crush = false;
                    if(direction)
                        direction = false;
                    else
                        direction = true;
                }
                if(direction)
                    this.y+= speed;
                else
                    this.y-= speed;
                break;
        }
        crushCheck();
    }

    public void crushCheck() {
        if(!myRect.contains(this.x, this.y, this.x + this.w, this.y + this.h)){
            crush = true;
        }
    }
}
