package com.example.sketcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    static String currentPhotoPath;
    private Context context;


    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(context);
        currentPhotoPath = "image.png";
        this.context = context;
        this.setBackgroundColor(0xFFFFFFFF);
    }

    //setup drawing
    private void setupDrawing(Context context){

        //prepare for drawing and setup paint stroke properties
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(ContextCompat.getColor(context, R.color.red));
        drawPaint.setStrokeWidth(15.0f);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void changeColor(Context context, Character ch) {
        if (ch == 'r') {
            drawPaint.setColor(ContextCompat.getColor(context, R.color.red));
        } else if (ch == 'g') {
            drawPaint.setColor(ContextCompat.getColor(context, R.color.green));
        } else if (ch == 'b') {
            drawPaint.setColor(ContextCompat.getColor(context, R.color.blue));
        } else {
            drawPaint.setColor(ContextCompat.getColor(context, R.color.purple));
        }
    }

    public void changeStroke(Character ch) {
        if (ch == 's') {
            drawPaint.setStrokeWidth(15.0f);
        } else if (ch == 'm') {
            drawPaint.setStrokeWidth(35.0f);
        } else {
            drawPaint.setStrokeWidth(55.0f);
        }
    }

    //size assigned to view
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    //draw the view - will be called after touch event
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, drawPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //register user touches as drawing action
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //respond to down, move and up events
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(x,y);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(x,y);
                break;
            default:
                break;
        }
        //redraw
        invalidate();
        return true;

    }

    //start new drawing
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    //Uri curUri = FileProvider.getUriForFile(context,
    //                "com.example.android.elester32_PA8", file);
    public void imageFile() {
        Bitmap bm = getBitmap();
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    currentPhotoPath);
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap() {
        return canvasBitmap.copy(canvasBitmap.getConfig(), true);

    }
}

