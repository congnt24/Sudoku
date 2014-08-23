package com.example.cong.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener, Constant {
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.button5 = (Button) findViewById(R.id.button5);
        this.button4 = (Button) findViewById(R.id.button4);
        this.button3 = (Button) findViewById(R.id.button3);
        this.button2 = (Button) findViewById(R.id.button2);
        this.button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startGame(3);
                break;
            case R.id.button2:
                opendDifficultChooser();
                break;
            case R.id.button3:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.button4:
                finish();
                break;
            case R.id.button5:
                startActivity(new Intent(this, Database.class));
                break;
        }
    }

    private void opendDifficultChooser() {
        new AlertDialog.Builder(this).setTitle(R.string.new_game_title).setItems(R.array.difficult, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame(which);
            }
        }).show();
    }

    private void startGame(int which) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(KEY_DIFFICULT, which);
        startActivity(intent);
        Log.d(TAG, "Click on " + which);
    }
}
