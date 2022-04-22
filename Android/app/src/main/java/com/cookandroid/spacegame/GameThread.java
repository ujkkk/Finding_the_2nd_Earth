package com.cookandroid.spacegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Vector;

public class GameThread extends Thread {
    SurfaceHolder holder;
    Context context;
    int screen_width, screen_height;
    int x, y, dw, dh;
    int sx, sy;
    int num;
    int x1, y1;
    Bitmap imgBack;
    boolean getAllItem = false;
    static boolean gameOver;
    Vector<Rect> polyVector = new Vector<Rect>(100);
    ArrayList<Enemy> enemys = new ArrayList <Enemy>(20);
    ArrayList <Item> itemList = new ArrayList <Item>(20);
    Character character;
    Goal goal;
    Map map;
    ArrayList<Tile> tiles = new  ArrayList<Tile>(100);
    ClosedArea closedArea = null;
    boolean flag = true;
    Point initPostion;
    GameView gameView;


    public GameThread(GameView gameView, SurfaceHolder holder, Context context, Vector<Rect> polyVector, ArrayList<Enemy> enemys
    , ArrayList <Item> itemList, Character character, Goal goal, Map map,ArrayList<Tile> tiles , ClosedArea closedArea){
        this.holder = holder;
        this.context = context;
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        screen_width = display.getWidth();
        screen_height = display.getHeight();
        Log.d("Weight", "width : " + screen_width + " height : " + screen_height);

        this.gameView = gameView;
        this.polyVector = polyVector;
        this.enemys = enemys;
        this.itemList = itemList;
        this.character = character;
        this.initPostion = new Point(character.x, character.y);
        this.goal = goal;
        this.map = map;
        this.tiles = tiles;
        this.closedArea = closedArea;
        num=0;
        sx = -2;
        sy = 3;
        x= 100;
        y = 100;
    };



    public int getWeight(){
        int minX = screen_width, maxX = 0;
        int weight;
        for(int i =0; i< closedArea.pointVector.size(); i++){
            int x = closedArea.pointVector.get(i).y;
            if(x < minX) minX = x;
            if(x >= maxX) maxX = x;
        }
        weight = (screen_width - (maxX*closedArea.blockSize - minX*closedArea.blockSize))/2;
        if(weight-  closedArea.startPoint.y>0){
            weight =  weight - closedArea.startPoint.y;
        }
       else{
            weight =   closedArea.startPoint.y - weight ;
        }
        return weight;
    }

    public void enemyProcess() {
        for(int i =0; i < enemys.size(); i++){
            Enemy enemy = enemys.get(i);
            // move
            enemy.process();
        }

    }
    //적과 캐릭터가 부딪히면 게임오버
    public void crushEnemy() {
        Enemy enemy;
        for(int i=0; i< enemys.size(); i++) {
            enemy = enemys.get(i);
            Rect rect = new Rect(enemy.x, enemy.y,
                    enemy.x + enemy.w, enemy.y + enemy.h);
            if(rect.contains(character.x, character.y,character.x + 10,character.y+ 10)) {
               // SpaceGame.music.play("die");
                GameView.deathN++;
                gameOver();
                break;
            }

            else if(rect.contains(character.x+ character.w/2, character.y + character.h/2 ,
                    character.x+ character.w/2 + 10,character.y + character.h/2  + 10)) {
                //SpaceGame.music.play("die");
                GameView.deathN++;
                gameOver();
                break;
            }
            //조정필요
            else if(rect.contains(character.x+ character.w - 5, character.y+ character.h/2,
                    character.x+ character.w ,character.y+ character.h/2 + 5)) {
               // SpaceGame.music.play("die");
                GameView.deathN++;
                gameOver();
                break;
            }

        }
    }
    //아이템을 모두 먹어 스테이지 클리어 조건체크
    public void getItem() {
        if(itemList.size() == 0)
            getAllItem = true;
        for(int i=0; i< itemList.size(); i++) {
            Item item = itemList.get(i);
            Rect rect = new Rect(item.x, item.y, item.x + item.w, item.y + item.h);
            if(rect.contains(character.x + character.w/2, character.y + character.h/2)|| rect.contains(character.x + character.w, character.y)) {
                //SpaceGame.music.play("get");
                Bitmap.createScaledBitmap(item.img, 1,1 , true);
                itemList.remove(item);
            }
        }
    }
    public void nextStage() {
        if(getAllItem) {
            Rect poly = new Rect(goal.x, goal.y,goal.x + goal.w , goal.y + goal.h );
            if(poly.contains(character.x, character.y)|| poly.contains(character.x + character.w, character.y)
            || poly.contains(character.x, character.y + character.h)) {
                GameView.stageN++;
                String xmlFileName = "stage" + GameView.stageN +".xml";
                XMLReader xml = new XMLReader(context, xmlFileName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    gameView.parsing(xml.getActiveMapElement());
                }
                this.flag = false;
            }
        }
    }
    public void gameOver() {
        //캐릭터의 처음 좌표로
        character.x = initPostion.x;
        character.y = initPostion.y;

        // 아이템 new parsing
        gameView.parsingItem(gameView.activeMapNode);

    }

    //-------------------------
    // 캔버스에 그리기
    // ------------------------
    @Override
    public void run(){

        int weight = getWeight();
        Canvas canvas = null;
        Paint paint = new Paint();
        while(flag){
            enemyProcess();
            //골인 여부 체크
            nextStage();
            //아이템 먹었는지 체크
            getItem();
            //적과 캐릭터가 부딪혔는지 체크
            crushEnemy();
            canvas = holder.lockCanvas(); // canvas를 잠그고 버퍼 할당
            try{
                //동기화 유지 - 실제 그래픽이 처리되는 부분
                //synchronized (holder){
                    if(canvas != null){
                        //맵
                        canvas.drawBitmap(map.img,0,0, null);
                        //타일
                        for(int i =0; i< tiles.size(); i++) {
                            Tile tile = tiles.get(i);
                            canvas.drawBitmap(tile.img, tile.x  + weight, tile.y, null);
                        }
                        //선
                        paint.setColor(Color.WHITE);
                        Point p1;
                        Point p2;
                        int xp = 0, yp = 0;
                        int x1, x2 = 0, y1, y2 = 0;
                        for (int i = 0; i < closedArea.pointVector.size(); i++) {
                            p1 = closedArea.pointVector.get(i);
                            x1 = (closedArea.startPoint.y - closedArea.blockSize) + (p1.x + xp) * closedArea.blockSize;
                            y1 = (closedArea.startPoint.x - closedArea.blockSize) + (p1.y + yp) * closedArea.blockSize;

                            if (i + 1 == closedArea.pointVector.size()) {
                                p2 = closedArea.pointVector.get(0);
                            } else {
                                p2 = closedArea.pointVector.get(i + 1);
                            }
                            x2 = (closedArea.startPoint.y - closedArea.blockSize) + (p2.x + xp) * closedArea.blockSize;
                            y2 = (closedArea.startPoint.x - closedArea.blockSize) + (p2.y + yp) * closedArea.blockSize;
                            paint.setStrokeWidth(3);
                            canvas.drawLine(y1 +weight, x1, y2 +weight, x2, paint);
                        }
                        //캐릭터
                        canvas.drawBitmap(character.img,character.x  +weight,character.y, null);
                        Log.d("Weight", "character.x " +(character.x  +weight));
                        //적
                        for(int i =0; i< enemys.size(); i++){
                            Enemy enemy = enemys.get(i);
                            canvas.drawBitmap(enemy.img,enemy.x  + weight,enemy.y, null);
                        }
                        //아이템
                        for(int i =0; i< itemList.size(); i++){
                            Item item = itemList.get(i);
                            canvas.drawBitmap(item.img,item.x  +weight,item.y, null);
                        }
                        //골
                        canvas.drawBitmap(goal.img,goal.x + weight,goal.y, null);
                        //pointBar

                        paint.setColor(Color.BLACK);
                        canvas.drawRect(0,0, screen_width, screen_height/10, paint);
                        paint.setColor(Color.WHITE);
                        paint.setStrokeWidth(1);
                        canvas.drawLine(0,screen_height/10, screen_width,screen_height/10, paint);

                        paint.setTextSize(screen_height/14);
                        canvas.drawText("STAGE : " + GameView.stageN , 100, screen_height/12, paint);
                        canvas.drawText("DEATH : " + GameView.deathN , screen_width/2, screen_height/12, paint);

                        Log.d("View", "enemy");
                    }

              //  }
            }finally {  // 버퍼의 작업이 끝나면
                if(canvas != null)  // 버퍼의 내용을 view에 전달
                    holder.unlockCanvasAndPost(canvas);
            }
        }//while
    } // run
} // GameThread 끝



