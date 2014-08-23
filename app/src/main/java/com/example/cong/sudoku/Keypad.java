package com.example.cong.sudoku;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.Set;

public class Keypad extends Dialog {
    private final PuzzleView puzzleV;
    private View keypad;
    private View[] keys=new View[9];

    public Keypad(Context context, PuzzleView puzzleView) {
        super(context);
        this.puzzleV = puzzleView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keypad);
        findViews();
        setOnClick();
    }

    private void setOnClick() {
        Set<Integer> used=puzzleV.checkHint();
        for (Integer use : used) {
            keys[use-1].setVisibility(View.INVISIBLE);
//            Toast.makeText(getContext(), Integer.toString(use), Toast.LENGTH_SHORT).show();
        }
        keypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puzzleV.setSelectedTile(0);
                dismiss();
            }
        });
        for (int i = 0; i < keys.length; i++) {
            final int t=i+1;
            keys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    puzzleV.setSelectedTile(t);
                    dismiss();
                }
            });
        }
    }

    private void findViews() {
        keypad=findViewById(R.id.keypad);
        keys[0] = findViewById(R.id.keypad_1);
        keys[1] = findViewById(R.id.keypad_2);
        keys[2] = findViewById(R.id.keypad_3);
        keys[3] = findViewById(R.id.keypad_4);
        keys[4] = findViewById(R.id.keypad_5);
        keys[5] = findViewById(R.id.keypad_6);
        keys[6] = findViewById(R.id.keypad_7);
        keys[7] = findViewById(R.id.keypad_8);
        keys[8] = findViewById(R.id.keypad_9);
    }
}
