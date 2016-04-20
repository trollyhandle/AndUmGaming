package com.example.andumgaming.g370;

import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import Game.Board;
import Game.BoardView;

public class GameTest extends AppCompatActivity {

    private Board board;
    private BoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

        board = new Board();
        // board.setCenter(x, y);
        board.setHexSize(20);
        board.updateDrawable();
        boardView = new BoardView(this);
        boardView.setDrawable(board.getDrawable());

        setContentView(boardView);
    }
}
