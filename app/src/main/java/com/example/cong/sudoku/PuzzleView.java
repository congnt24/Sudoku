package com.example.cong.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

public class PuzzleView extends View {
    private final Game game;
    private float width, height;  //Size a tile
    private int selX, selY;     //Selection of tile
    private final Rect selRect=new Rect();
    private int selectedTile;

    public PuzzleView(Context context) {
        super(context);
        this.game=(Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width=w/9f;
        height=h/9f;
        getRect(selX, selY, selRect);   //Get Rect posit
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int x, int y, Rect rect) { //selection Rect
        rect.set((int)(x*width+1), (int)(y*height+1), (int)(width*(x+1)-1), (int)(height*(y+1)-1));
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint background=new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        Paint dark = new Paint();
        Paint hilite = new Paint();
        Paint light = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
        light.setColor(getResources().getColor(R.color.puzzle_light));
        //Draw Line
        for (int i = 0; i < 9; i++) {
            canvas.drawLine(0,i*height,getWidth(),i*height,light);
            canvas.drawLine(0,i*height+1,getWidth(),i*height+1,hilite);
            canvas.drawLine(i*width,0,i*width,getHeight(),light);
            canvas.drawLine(i*width+1,0,i*width+1,getHeight(),hilite);
        }
        for (int i = 0; i < 9; i+=3) {
            canvas.drawLine(0, i*height,getWidth(), i*height, dark);
            canvas.drawLine(0,i*height+1,getWidth(),i*height+1,hilite);
            canvas.drawLine(i*width,0,i*width,getHeight(), dark);
            canvas.drawLine(i*width+1,0,i*width+1,getHeight(),hilite);
        }
        Paint def=new Paint();
        def.setColor(getResources().getColor(R.color.puzzle_def));
        Paint foreground=new Paint();
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fm=foreground.getFontMetrics();
        float x=width/2;
        float y=height/2-(fm.ascent+fm.descent)/2;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Integer.parseInt(this.game.getTileSring(i,j))!=0) {
                    canvas.drawRect(j * width + 1, i * height + 1, j * width + width - 1, i * height + height - 1, def);
                    if (Integer.parseInt(game.source.substring(i * 9 + j, i * 9 + j + 1)) != 0)
                        canvas.drawRect(j * width + 1, i * height + 1, j * width + width - 1, i * height + height - 1, def);
                    canvas.drawText(this.game.getTileSring(i, j), x + j * width, y + i * height, foreground);
                }else if(game.puzzleSave!=null){
                    if (Integer.parseInt(game.puzzleSave.substring(i*9+j, i*9+j+1))!=0){
                        canvas.drawText(game.puzzleSave.substring(i*9+j, i*9+j+1), x+j*width, y+i*height, foreground);
                    }

                }
            }
        }
        Paint selected=new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selRect, selected);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()!=MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        game.mp.start();
        select((int)(event.getX()/width), (int)(event.getY()/height));
        if (Integer.parseInt(game.source.substring(selX+9*selY, selX+9*selY+1))==0){
            game.showKeypadOrError(selX, selY);
        }
        return true;

    }

    private void select(int x, int y) {
        invalidate(selRect);
        selX=Math.min(Math.max(x,0),8);
        selY=Math.min(Math.max(y,0),8);
        getRect(selX, selY, selRect);
        invalidate(selRect);
    }

    public void setSelectedTile(int tile) {
        if (game.setTileIfValid(selX, selY, tile)){
            invalidate();
        }
    }

    public Set<Integer> checkHint() {
        return game.checkHint(selX, selY);
    }


}
