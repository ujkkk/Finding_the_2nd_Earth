package com.cookandroid.spacegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainView extends View {

    int width, height;
    Context context;
    int cx, cy;
    Bitmap imgBack, imgButton;
    Rect rectBtn;
    //-------------------------------
    //    생성자
    //-------------------------------
    public MainView(Context context) {
        super(context);
        this.context = context;
        Display display = ((WindowManager) context.getSystemService
                (Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

//        //중앙좌표
//        cx = width/2;
//        cy = height/2;
//
//        imgBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.space);
//        imgButton = BitmapFactory.decodeResource(context.getResources(), R.drawable.play);
//        //버튼의 사이즈 조절
//        imgButton = Bitmap.createScaledBitmap(imgButton, height/4, height/4, true);
//        //버튼 영역의 rect
//        rectBtn  = new Rect(cx - imgButton.getWidth()/2 , cy - imgButton.getHeight()/2,
//                cx + imgButton.getWidth()/2 , cy + imgButton.getHeight()/2);

        
    }//생성자

    //------------------------------
    //         onDraw
    //-----------------------------
    public void onDraw(Canvas canvas){
        Paint paint = new Paint();

        //배경
       // canvas.drawBitmap(imgBack,0,0,null);
        //버튼
        canvas.drawBitmap(imgButton,cx - imgButton.getWidth()/2,cy - imgButton.getHeight()/2,null);
    }//onDraw

    View.OnClickListener btn1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

}
