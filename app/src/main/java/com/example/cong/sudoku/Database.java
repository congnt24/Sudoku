package com.example.cong.sudoku;


import static android.provider.BaseColumns._ID;
import static com.example.cong.sudoku.Constant.TABLE_NAME;
import static com.example.cong.sudoku.Constant.LEVEL;
import static com.example.cong.sudoku.Constant.PUZZLE;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database extends Activity implements View.OnClickListener{
    DataEvents events=new DataEvents(this);
    private Button button, button2;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        textView = (TextView) findViewById(R.id.textView);
        this.button = (Button) findViewById(R.id.button);
        this.button2 = (Button) findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                new Thread(new Runnable() {
                    int r = new Random().nextInt(3) + 1;

                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://view.websudoku.com/?level=" + r);
                            URLConnection conn = url.openConnection();
                            StringBuilder html = new StringBuilder();
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            String line;
                            while ((line = br.readLine()) != null) {
                                html.append(line);
                            }
                            Matcher matCheat = Pattern.compile("<INPUT NAME=cheat ID=\"cheat\" TYPE=hidden VALUE=\"([^\"]*)\">").matcher(html.toString());
                            Matcher matHidden = Pattern.compile("<INPUT ID=\"editmask\" TYPE=hidden VALUE=\"([^\"]*)\">").matcher(html.toString());
                            String cheat = null, hidden = null;
                            if (matCheat.find() & matHidden.find()) {
                                cheat = matCheat.group(1);
                                hidden = matHidden.group(1);
                            }
                            StringBuilder puzz=new StringBuilder();
                            for (int i = 0; i < 81; i++) {
                                if (Integer.parseInt(hidden.substring(i, i+1))==0){
                                    puzz.append(cheat.substring(i,i+1));
                                }else{
                                    puzz.append(0);
                                }
                            }
                            //Addn to sqlite
                            SQLiteDatabase db=events.getWritableDatabase();
                            ContentValues values=new ContentValues();
                            values.put(LEVEL, r-1);
                            values.put(PUZZLE, puzz.toString());
                            db.insertOrThrow(TABLE_NAME, null, values);
                            Log.d("Sudoku", " Success 1");
                            db.close();
                        } catch (MalformedURLException e) {
                            Log.d("Sudoku", "Error 1");
                        } catch (IOException e) {
                            Log.d("Sudoku", "Error 2");
                        }finally {
                            Thread.interrupted();
                        }
                    }
                }).start();
            case R.id.button2:
                SQLiteDatabase db=events.getReadableDatabase();
                Cursor cursor = db.query(TABLE_NAME, new String[]{LEVEL, PUZZLE},null,null,null,null,LEVEL+ " ASC");
                startManagingCursor(cursor);
                StringBuilder sb=new StringBuilder("\n");
                int i=1;
                while (cursor.moveToNext()){
                    sb.append(i+":"+cursor.getInt(0)+":"+cursor.getString(1)+"\n");
                    i++;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textView.setText(sb.toString());
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        events.close();
    }
}
