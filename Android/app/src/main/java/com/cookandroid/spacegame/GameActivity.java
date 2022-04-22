package com.cookandroid.spacegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Node;

import java.util.logging.Handler;

import static com.cookandroid.spacegame.GameView.stageN;

public class GameActivity extends AppCompatActivity {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static TextView stage, death;
    GameView gameView;
    Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("GameActivity", "onCreate");
        super.onCreate(savedInstanceState);

        //full Screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //가로 모드로 지정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();

        //xml 파일 읽고 노드 생성
        String xmlFileName = "stage1.xml";
        XMLReader xml = new XMLReader(this, xmlFileName);

        Node spaceGameNode = xml.getSpaceGameElement();
        Node sizeNode = XMLReader.getNode(spaceGameNode, XMLReader.E_SIZE);
        String w = XMLReader.getAttr(sizeNode, "w");
        String h = XMLReader.getAttr(sizeNode, "h");
        SCREEN_WIDTH = Integer.parseInt(w);
        SCREEN_HEIGHT = Integer.parseInt(h);

        GameView.gameViewNode = xml.getGamePanelElement();
        gameView = (GameView)findViewById(R.id.gameView);
        setContentView(R.layout.activity_game);
        //GameView가 참고할 GamePanel node 넘겨주기

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}