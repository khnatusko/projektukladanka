package com.example.android.projekt;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import model.Board;
import model.Place;

public class Slide extends View {

    private Board board;


    private float width;


    private float height;

    public Slide(Context context) {
        super(context);
    }

    private Place locatePlace(float x, float y) {
        int ix = (int) (x / width);
        int iy = (int) (y / height);

        return board.at(ix + 1, iy + 1);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        Place p = locatePlace(event.getX(), event.getY());
        if (p != null && p.slidable() && !board.solved()) {

            p.slide();
            invalidate();
        }
        return true;
    }

}
