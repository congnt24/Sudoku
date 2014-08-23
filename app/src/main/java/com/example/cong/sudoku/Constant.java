package com.example.cong.sudoku;

import android.provider.BaseColumns;

public interface Constant extends BaseColumns{
    public static final String KEY_DIFFICULT="com.example.cong.sudoku.Game";
    public static final int DIFFICULT_CONTINUE=3;
    public static final int DIFFICULT_EASY=0;
    public static final int DIFFICULT_MEDIUM=1;
    public static final int DIFFICULT_HARD=2;
    public static final String TAG="Sudoku";
    public static final String DATABASE_NAME="events.db";
    public static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="events";
    public static final String LEVEL = "level";
    public static final String PUZZLE="puzzle";
    public static final String[] FROM=new String[]{_ID, LEVEL, PUZZLE};

}
