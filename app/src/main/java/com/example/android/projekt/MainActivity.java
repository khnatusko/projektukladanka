package com.example.android.projekt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.example.android.projekt.BoardView;

public class MainActivity extends AppCompatActivity  {

    private Gyroscope gyroscope;

    private Place place;

    private static final String TAG = "MainActivity";

    private ViewGroup mainView;

    private Board board;

    private BoardView boardView;

    private int boardSize = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = (ViewGroup) findViewById(R.id.mainLayout);
        this.newGame();

        gyroscope = new Gyroscope(this);

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float rx, float ry, float rz) {
                if(ry > 1.0f)
                {

                    //slide = new Slide(this);
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                }
                else if(ry <-1.0f)
                {


                    //getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                }
                else if(rx > 1.0f)
                {


                    //getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                else if(rx <-1.0f)
                {



                   // getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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

    public void changeSize(int newSize) {
        if (newSize != this.boardSize) {
            this.boardSize = newSize;
            this.newGame();
            boardView.invalidate();
        }
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
        public void tileSlid(Place from, Place to, int finish) {

       }

        public void solved(int finish) {

            Toast.makeText(getApplicationContext(), "You won!",
                    Toast.LENGTH_LONG).show();
        }
    };


}