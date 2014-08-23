package com.example.cong.sudoku;

import static com.example.cong.sudoku.Constant._ID;
import static com.example.cong.sudoku.Constant.DATABASE_NAME;
import static com.example.cong.sudoku.Constant.DATABASE_VERSION;
import static com.example.cong.sudoku.Constant.TABLE_NAME;
import static com.example.cong.sudoku.Constant.LEVEL;
import static com.example.cong.sudoku.Constant.PUZZLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataEvents extends SQLiteOpenHelper {
    public DataEvents(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+LEVEL+" INTEGER, " +PUZZLE+" TEXT NOT NULL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
        onCreate(db);
    }
}
