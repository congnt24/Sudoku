package com.example.cong.sudoku;

import static com.example.cong.sudoku.Constant.FROM;
import static com.example.cong.sudoku.Constant.*;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Game extends Activity implements Constant{

    public int[] puzzle=new int[9*9];
    public PuzzleView puzzleView = null;
    DataEvents events=new DataEvents(this);
    public String source;
    public int[] cheat=new int[81];
    public String puzzleSave;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int diff=getIntent().getIntExtra(KEY_DIFFICULT, DIFFICULT_EASY);
//        Log.d(TAG, "KEY_DIFFICULT= "+diff);
            puzzle=getPuzzle(diff);
            if (diff!=3){
                cheat=Arrays.copyOf(puzzle, 81);
                solvePuzzle(0,0);
            }
        puzzleView = new PuzzleView(this);
        mp=MediaPlayer.create(this, R.raw.amfmbeep);
        setContentView(puzzleView);
        setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
        puzzleView.requestFocus();
    }

    private boolean solvePuzzle(int i,int j) {
        if (i==9){
            i=0;
            if (++j==9)return true;
        }
        //Skip fill
        if (cheat[i*9+j]!=0)
            return solvePuzzle(i+1,j);
        //Fill
        for (int k = 1; k <= 9; k++) {
            if (legal(i,j,k, cheat)){
                cheat[i*9+j]=k;
                if (solvePuzzle(i+1,j))
                    return true;
            }

        }
        cheat[i*9+j]=0;
        return false;
    }

    private boolean legal(int i, int j, int k, int[] puzzle1) {
        //row
        for (int l = 0; l < 9; l++) {
            if (k==puzzle1[i*9+l])return false;
        }
        //col
        for (int l = 0; l < 9; l++) {
            if (k==puzzle1[l*9+j])return false;
        }
        //box
        int boxrow=(i/3)*3,boxcol=(j/3)*3;
        for (int l = 0; l < 3; l++) {
            for (int m = 0; m < 3; m++) {
                if (k==puzzle1[(boxrow+l)*9+(boxcol+m)]){
                    return false;
                }
            }
        }
        return true;
    }

    private int[] getPuzzle(int diff) {
        if (diff==DIFFICULT_CONTINUE){
            puzzleSave=getPreferences(MODE_PRIVATE).getString("puzzle", source);
            source=getPreferences(MODE_PRIVATE).getString("source", source);
            Log.d("Tag", ""+source);
            return fromPuzzleString(source);
        }
        try {
            SQLiteDatabase db=events.getReadableDatabase();
//        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHARE "+LEVEL+"="+diff,null);
            Cursor cursor=db.query(TABLE_NAME,FROM, LEVEL+"=?", new String[]{Integer.toString(diff)},null,null,null);
            startManagingCursor(cursor);
            cursor.moveToPosition(new Random().nextInt(cursor.getCount()));
            source=cursor.getString(cursor.getColumnIndex(PUZZLE));
            db.close();
        }catch (Exception e){

        }
        return fromPuzzleString(source);
    }

    private int[] fromPuzzleString(String source1) {
        int[] a=new int[9*9];
        for (int i = 0; i < 81; i++) {
            if(Integer.parseInt(source1.substring(i,i+1))!=0){
                a[i]=Integer.parseInt(source1.substring(i,i+1));
            }
        }
        return a;
    }

    public String getTileSring(int i, int j) {
        return Integer.toString(puzzle[i*9+j]);
    }

    public void showKeypadOrError(int x, int y) {
        Dialog keypad=new Keypad(this, puzzleView);
        keypad.show();
    }

    public boolean setTileIfValid(int selX, int selY, int tile) {
        puzzle[selX+9*selY]=tile;
        if (tile==cheat[selX+9*selY]){
            Toast.makeText(getBaseContext(), ""+tile,Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < 81; i++) {
            if (puzzle[i]==0)return true;
        }
        int g=0;
        for (int i = 0; i < 81; i++) {
            if(puzzle[i]!=cheat[i]){
                Toast.makeText(getBaseContext(), "Sai Roi!!",Toast.LENGTH_SHORT).show();
                g=1;
                break;
            }
        }
        if(g==0){
            Toast.makeText(getBaseContext(), "Win",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public Set<Integer> checkHint(int x,int y) {
        Set<Integer> used=new HashSet<Integer>();
        int yy=(x/3)*3;
        int xx=(y/3)*3;
        for (int i = 0; i < 9; i++) {
            if (puzzle[y*9+i]!=0 & i!=x){
                used.add(puzzle[y*9+i]);
            }
            if(puzzle[i*9+x]!=0 & i!=y){
                used.add(puzzle[i*9+x]);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (puzzle[xx*9+yy+i*9+j]!=0 & i!=y & j!=x){
                    used.add(puzzle[xx*9+yy+i*9+j]);
                }
            }
        }
        return used;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
        getPreferences(MODE_PRIVATE).edit().putString("puzzle", toPuzzleString(puzzle)).commit();
        getPreferences(MODE_PRIVATE).edit().putString("source", source).commit();
    }

    private String toPuzzleString(int[] puzzle) {
        StringBuilder sb=new StringBuilder();
        for (int i : puzzle) {
            sb.append(i);
        }
        return sb.toString();
    }
}
