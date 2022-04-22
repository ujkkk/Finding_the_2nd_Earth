package com.cookandroid.spacegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Vector;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    public GameThread gameThread;
    public SurfaceHolder sufaceHolder;
    public static Node gameViewNode = null;
    public Context context;
    int x,y,w,h;
    Vector<Point> lineVector;
    Vector<Rect> polyVector = new Vector<Rect>(100);
    ArrayList<Enemy> enemys = new ArrayList <Enemy>(20);
    ArrayList <Item> itemList = new ArrayList <Item>(20);
    Character character;
    Goal goal;
    Map map;
    ArrayList<Tile> tiles = new  ArrayList<Tile>(100);
    ClosedArea closedArea;
    Node activeMapNode;
    boolean flag = true;
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static boolean isGame = false;
    public static boolean isMain = false;
    public static boolean isGameOver = false;
    public static boolean isNextStage = false;
    public static boolean isHome = false;
    public static int stageN = 1;
    public static int deathN =0;
    public static int allStageN =15;
    //------------------------------------
    //            생성자
    //-------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.O)
    public GameView (Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        // SurfaceView는 SurfaceHolder라는 콜백 함수에 의해 움직인다
        sufaceHolder = getHolder();
        sufaceHolder.addCallback(this); //콜백함수 등록

        setFocusable(true);

        //node parsing..
        //get ActiveMap Node
        this.activeMapNode = XMLReader.getNode(gameViewNode, XMLReader.E_ACTIVEMAP);
        parsing(activeMapNode);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void parsing(Node activeMapNode){

        parsingClosedArea(activeMapNode);
        parsingEnemy(activeMapNode);
        parsingItem(activeMapNode);
        parsingPlayer(activeMapNode);
        parsingBackground(gameViewNode);
        parsingGoal(activeMapNode);
        parsingTile(activeMapNode);

        gameThread = new GameThread(this,sufaceHolder, context, polyVector,enemys,
                itemList, character, goal ,map,tiles,closedArea);

        gameThread.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getBitmapId(Context context, Path path){
        String fileName =  path.getFileName().toString();

        //확장자 제외한 파일명 구하기
        int pos = fileName.lastIndexOf(".");
        String resourceName = fileName.substring(0,pos);
        Log.d("XML", "resourceName : " + resourceName);
        int id = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());

        return id;
    }

    // SurfaceView가 생성될 때 실행되는 부분
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder){
    }

    //SurfaceView가 바뀔 때 실행되는 부분
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    //SurfaceView가 해제될 때 실행되는 부분
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)  {
        boolean done = true;
        while(done){
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            done = false;

        }//while
    }// surfaceDestroyed

    // -----------------------------------------
    //          onTouch Event
    //-----------------------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int x2;
        int y2;
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            gameThread.x1 = (int)event.getX();
            gameThread.y1 = (int)event.getY();
            Log.d("Touch" ,"1.DOWN");
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            x2 = (int)event.getX();
            y2 = (int)event.getY();
            int ax = x2 - gameThread.x1;
            int ay = y2 - gameThread.y1;

            // 처음 터치를 한 지점과의 차이가 많이나면 기준점 변경
            if(Math.abs(ax) > 50 || Math.abs(ay) > 50){
                gameThread.x1 = x2;
                gameThread.y1 = y2;
            }
            //LEFT
            if (ax < 0  && Math.abs(ax)> Math.abs(ay)){
                Character.setDown(false);
                Character.setUp(false);
                Character.setLeft(true);
                Character.setRight(false);
                Log.d("Touch" ,"2.LEFT");
            }
            //Right
            else if(ax > 0  && Math.abs(ax)> Math.abs(ay)){
                Character.setDown(false);
                Character.setUp(false);
                Character.setLeft(false);
                Character.setRight(true);
                Log.d("Touch" ,"2.RIGHT");
            }

            //UP -
            else  if(ay < 0 && Math.abs(ax) < Math.abs(ay)){
                Character.setDown(false);
                Character.setUp(true);
                Character.setLeft(false);
                Character.setRight(false);
                Log.d("Touch" ,"2.UP");
            }
            //DOWM +
            else if (ay > 0  && Math.abs(ax) < Math.abs(ay)){
                Character.setDown(true);
                Character.setUp(false);
                Character.setLeft(false);
                Character.setRight(false);
                Log.d("Touch" ,"2.DOWN");
            }
        }

        else if(event.getAction() == MotionEvent.ACTION_UP){
            x2 = (int)event.getX();
            y2 = (int)event.getY();
            gameThread.x1 = x2;
            gameThread.y1 = y2;

            Character.setDown(false);
            Character.setUp(false);
            Character.setLeft(false);
            Character.setRight(false);
            Log.d("Touch" ,"3.UP");

        }
        character.process();
        return true;
    }

    public void parsingClosedArea(Node activeMapNode){
        polyVector = new Vector<Rect>(100);
        //맵 테두리
        Node closedAreaNode = XMLReader.getNode(activeMapNode, XMLReader.E_CLOSEDAREA);
        int blockSize = Integer.parseInt(XMLReader.getAttr(closedAreaNode, "blockSize"));
        NodeList polyNodeList = closedAreaNode.getChildNodes();
        for(int i=0; i<polyNodeList.getLength(); i++) {
            Node node = polyNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // 하나의 rectangle 태그
            if(node.getNodeName().equals(XMLReader.E_RECTANGLE)) {
                int [] xpoints = new int[4];
                int [] ypoints = new int[4];
                int xIndex = 0;
                int yIndex = 0;
                Node rectangleNode = node;
                NodeList rectangleNodeList = rectangleNode.getChildNodes();
                for(int j=0; j<rectangleNodeList.getLength(); j++) {
                    Node pointNode = rectangleNodeList.item(j);

                    if(pointNode.getNodeType() != Node.ELEMENT_NODE)
                        continue;
                    // found!!, <Obj> tag
                    if(pointNode.getNodeName().equals(XMLReader.E_POINT)) {
                        xpoints[xIndex++] = Integer.parseInt(XMLReader.getAttr(pointNode, "x"));
                        ypoints[yIndex++] = Integer.parseInt(XMLReader.getAttr(pointNode, "y"));
                    }
                    if(xIndex == 4 && yIndex == 4) {
                        polyVector.add(new Rect(xpoints[0], ypoints[0], xpoints[2], ypoints[1]));
                        break;
                    }
                }

            }
        }
        closedArea = new ClosedArea(polyVector,blockSize);
    }

    public void parsingEnemy(Node activeMapNode){
        enemys = new ArrayList <Enemy>(20);
        //적
        Node enemyNode = XMLReader.getNode(activeMapNode, XMLReader.E_ENEMY);
        NodeList enemyNodeList = enemyNode.getChildNodes();
        for(int i=0; i<enemyNodeList.getLength(); i++) {
            Node node = enemyNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {

                //파일명 구하기
                // Path path = Paths.get((XMLReader.getAttr(node, "img")));
                //int id = getBitmapId(context,path);

                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
                String type= XMLReader.getAttr(node, "type");
                //700052
                Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
                Bitmap.createScaledBitmap(img, w,h , true);
                //Bitmap img = BitmapFactory.decodeResource(context.getResources(), id);
                Enemy enemy = new Enemy(x,y,w,h,speed,type,img,polyVector);
                enemys.add(enemy);

            }

        }
        Log.d("Gameview", "enemy 완료");
    }

    public void parsingItem(Node activeMapNode){
        itemList = new ArrayList <Item>(20);
        //아이템
        Node itemNode = XMLReader.getNode(activeMapNode, XMLReader.E_ITEM);
        NodeList itemNodeList = itemNode.getChildNodes();
        for(int i=0; i<itemNodeList.getLength(); i++) {
            Node node = itemNodeList.item(i);
            if(node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            // found!!, <Obj> tag
            if(node.getNodeName().equals(XMLReader.E_OBJ)) {

                //파일명 구하기
                // Path path = Paths.get((XMLReader.getAttr(node, "img")));
                // int id = getBitmapId(context,path);

                int x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                int y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                int w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                int h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.star2);
                img = Bitmap.createScaledBitmap(img, w,h , true);
                Item item = new Item(x,y,w,h,img);
                itemList.add(item);

            }

        }
        Log.d("Gameview", "item 완료");
    }
    public void parsingPlayer(Node activeMapNode){
        //캐릭터
        Node charNode = XMLReader.getNode(activeMapNode, XMLReader.E_CHARACTER);
        x = Integer.parseInt(XMLReader.getAttr(charNode, "x"));
        y = Integer.parseInt(XMLReader.getAttr(charNode, "y"));
        w = Integer.parseInt(XMLReader.getAttr(charNode, "w"));
        h = Integer.parseInt(XMLReader.getAttr(charNode, "h"));
        int speed = Integer.parseInt(XMLReader.getAttr(charNode, "speed"));
        //파일명 구하기
        //Path path = Paths.get((XMLReader.getAttr(charNode, "img")));
        // int id = getBitmapId(context,path);

        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.p);
        img = Bitmap.createScaledBitmap(img, w,h , true);
        character = new Character(x,y,w,h,speed,img, polyVector);
        Log.d("Gameview", "캐릭터 완료");
    }

    public void parsingBackground(Node gameViewNode){
        //배경화면
        Node bgNode = XMLReader.getNode(gameViewNode, XMLReader.E_BG);
        //파일명 구하기
        // path = Paths.get((XMLReader.getAttr(bgNode, "img")));
        // id = getBitmapId(context,path);

        //멥
        int w = Integer.parseInt(XMLReader.getAttr(bgNode, "w"));
        int h = Integer.parseInt(XMLReader.getAttr(bgNode, "h"));
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
        //img = Bitmap.createScaledBitmap(img, w,h , true);
        map = new Map(w,h,img);
        Log.d("Gameview", "배경화면 완료");

    }
    public void parsingGoal(Node activeMapNode){
        // 골인
        Node goalNode = XMLReader.getNode(activeMapNode, XMLReader.E_GOAL);
        //파일명 구하기
        // path = Paths.get((XMLReader.getAttr(goalNode, "img")));
        //id = getBitmapId(context,path);
        x = Integer.parseInt((XMLReader.getAttr(goalNode, "x")));
        y = Integer.parseInt((XMLReader.getAttr(goalNode, "y")));
        w = Integer.parseInt((XMLReader.getAttr(goalNode, "w")));
        h = Integer.parseInt((XMLReader.getAttr(goalNode, "h")));

        Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.goal);
        img = Bitmap.createScaledBitmap(img, w,h , true);
        goal = new Goal(x,y,w,h,img);
        Log.d("Gameview", "골인 완료");
    }
    public void parsingTile(Node activeMapNode){
        //타일
        tiles = new  ArrayList<Tile>(100);
        Node blockNode = XMLReader.getNode(activeMapNode, XMLReader.E_TILE);
        if(blockNode != null) {
            NodeList blokcNodeList = blockNode.getChildNodes();
            for(int i=0; i<blokcNodeList.getLength(); i++) {
                Node node = blokcNodeList.item(i);
                if(node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                // found!!, <Obj> tag
                if(node.getNodeName().equals(XMLReader.E_OBJ)) {

                    x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                    y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                    w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                    h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                    //파일명 구하기
                    //path = Paths.get((XMLReader.getAttr(node, "img")));
                    //id = getBitmapId(context, path);

                    Bitmap img = BitmapFactory.decodeResource(context.getResources(), R.drawable.block);
                    img = Bitmap.createScaledBitmap(img, w,h , true);
                    Tile tile = new Tile(x,y,w,h, img);
                    tiles.add(tile);
                }
            }

        }
        Log.d("Gameview", "tile 완료");
    }
}
class Map{
    Bitmap img;
    int w, h;

    public Map( int w, int h,  Bitmap img) {

        this.w = w;
        this.h = h;
        this.img = img;
    }

}

//color로 할 수 있게
class Tile {

    int x,y,w,h;
    Bitmap img;

    public Tile(int x, int y, int w, int h, Bitmap img) {
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.img = img;
    }
}

class Item {

    int x,y,w,h;
    Bitmap img;
    public Item(int x, int y, int w, int h,  Bitmap img) {
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.img=img;
    }
}
class Goal {

    int x,y,w,h;
    Bitmap img;
    public Goal(int x, int y, int w, int h,  Bitmap img) {
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
        this.img=img;
    }
}