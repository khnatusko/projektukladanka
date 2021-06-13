package com.example.android.projekt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import model.Board;
import model.Place;
import model.Tile;


import com.example.android.projekt.BoardView;

public class MainActivity extends AppCompatActivity {

    public Gyroscope gyroscope;

    public static final String TAG = "MainActivity";

    public ViewGroup mainView;

    public Board board;

    public BoardView boardView;

    public int boardSize = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (ViewGroup) findViewById(R.id.mainLayout);
        this.newGame();
        boardView = new BoardView(this, board);

        gyroscope = new Gyroscope(this);

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz)  {
                //prawo

                if(ry > 1.0f)
                {
                    boardView.SlideRight();
                }
                //lewo

                else if(ry <-1.0f)
                {
                    boardView.SlideLeft();
                }

                //w dół
                else if(rx > 1.0f)
                {
                    boardView.SlideDown();
                }

                //w góre
                else if(rx <-1.0f)
                {
                    boardView.SlideUp();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        boardView.invalidate();
        gyroscope.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gyroscope.unregister();

    }

    private void newGame() {
        this.board = new Board(this.boardSize);
        this.board.addBoardChangeListener(boardChangeListener);
        this.board.rearrange();
        this.mainView.removeView(boardView);
        this.boardView = new BoardView(this, board);
        this.mainView.addView(boardView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_new_game:
                new AlertDialog.Builder(this)
                        .setTitle("New Game")
                        .setMessage("Are you sure you want to begin a new game?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        board.rearrange();
                                        boardView.invalidate();
                                    }
                                })
                        .setNegativeButton(android.R.string.no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.action_help:
                new AlertDialog.Builder(this)
                        .setTitle("Rules")
                        .setMessage(
                                "Set the squares from the lowest value to the highest value.")
                        .setPositiveButton("Understood. Let's play!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Board.BoardChangeListener boardChangeListener = new Board.BoardChangeListener() {

        public void solved(int finish) {
            Toast.makeText(getApplicationContext(), "You won!",
                    Toast.LENGTH_LONG).show();
        }
    };


}