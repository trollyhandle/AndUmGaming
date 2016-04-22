package com.example.andumgaming.g370.views;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.andumgaming.g370.R;

import Game.Board;
import Game.BoardView;

public class GameTest extends AppCompatActivity {

    private Board board;
    private BoardView boardView;

    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);
        findSize();

        System.out.println("TEST creating Board");
        board = new Board();
        board.setCenter(width / 2, height / 2);
        System.out.printf("TEST center at (%1$2d,%2$2d)\n", width/2, height/2);
        board.setHexSize(20);
        board.update();

        System.out.println("TEST creating BoardView");
        boardView = new BoardView(this);
        boardView.setDrawable(board.getPath());

        setContentView(boardView);
    }

    private void findSize()
    {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        width = size.x;
        height = size.y;
        System.out.println("TEST Window size: " + width + " by " + height);
    }
}
