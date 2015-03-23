package com.bamafolks.android.games.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public class Board extends View {
    private final Game game;
    private float width;
    private float height;
    private int selX;
    private int selY;
    int offset = 200;
    private final Rect selRect = new Rect();
    List<Line> Lines;
    public static int countX = 0, count0 = 0;

    public Board(Context context) {
        super(context);
        this.game = (Game) context;
        setFocusable(true);
        Lines = new ArrayList<Line>();
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        width = w / 6f;
        height = (h - offset) / 6f;
        getRect(selX, selY, selRect);
        Log.d(getClass().getSimpleName(), "onSizeChanged: width " + width
                + ",height " + height);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int x, int y, Rect rect) {
        rect.set((int) (x * width), (int) (y * height + offset),
                (int) (x * width + width), (int) (y * height + height + offset));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.board_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);


        // Draw the board
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.board_dark));

        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.board_hilite));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.board_light));

        for (int i = 0; i < 6; i++) {
            canvas.drawLine(0, i * height + 1 + offset, getWidth(), i * height + 1 + offset, hilite);
            canvas.drawLine(i * width, 0 + offset, i * width, getHeight() + offset, dark);
            canvas.drawLine(i * width + 1, 0 + offset, i * width + 1, getHeight() + offset, hilite);
        }

        // Draw the X's and 0's...
        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.board_foreground));
        foreground.setStyle(Style.FILL);
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);

        FontMetrics fm = foreground.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fm.ascent + fm.descent) / 2;
        canvas.drawText("X : " + countX, width + x, y, foreground);
        canvas.drawText("0 : " + count0, 4 * width + x, y, foreground);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                canvas.drawText(this.game.getCellString(i, j), i * width + x, j * height + y + offset, foreground);
            }
        }

        // Draw the selected (active) cell
        Log.d(getClass().getSimpleName(), "selRect=" + selRect);
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.board_selected));
        Paint LineWin = new Paint();
        LineWin.setColor(getResources().getColor(R.color.black));
        LineWin.setStrokeWidth(9);
        canvas.drawRect(selRect, selected);
        for (int i = 0; i < Lines.size(); i++) {
            Line line = Lines.get(i);
            if ((line.getStartIndex() / 6) == (line.getEndIndex() / 6)) {
                canvas.drawLine((line.getStartIndex() % 6) * width
                        , ((line.getStartIndex() / 6) - 1) * height + height + height / 2 + offset,
                        (line.getEndIndex() % 6) * width + width,
                        ((line.getStartIndex() / 6) - 1) * height + height + height / 2 + offset,
                        LineWin);
            } else if ((line.getStartIndex() % 6) == (line.getEndIndex() % 6)) {
                canvas.drawLine(((line.getStartIndex() % 6) - 1) * width + width + width / 2
                        , (line.getStartIndex() / 6) * height + offset,
                        ((line.getEndIndex() % 6) - 1) * width + width / 2 + width,
                        (line.getEndIndex() / 6) * height + height + offset,
                        LineWin);
            } else if ((line.getStartIndex() % 6) < (line.getEndIndex() % 6)) {
                canvas.drawLine((line.getStartIndex() % 6) * width
                        , (line.getStartIndex() / 6) * height + offset,
                        (line.getEndIndex() % 6) * width + width,
                        (line.getEndIndex() / 6) * height + height + offset,
                        LineWin);
            } else {
                canvas.drawLine((line.getStartIndex() % 6) * width + width
                        , (line.getStartIndex() / 6) * height + offset,
                        (line.getEndIndex() % 6) * width,
                        (line.getEndIndex() / 6) * height + height + offset,
                        LineWin);
            }
        }

    }

    private void select(int x, int y) {
        invalidate(selRect);
        selX = Math.min(Math.max(x, 0), 5);
        selY = Math.min(Math.max(y, 0), 5);
        getRect(selX, selY, selRect);
        invalidate(selRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);
        if (event.getY() <= offset)
            return true;


        select((int) (event.getX() / width), (int) ((event.getY() - offset) / height));
        setSelectedCell(this.game.getPlayerSymbol());
        Log.d(getClass().getSimpleName(), "onTouchEvent: x " + selX + ", y " + selY);
        return true;
    }

    public void setSelectedCell(String symbol) {
        if (game.setCellIfValid(selX, selY, symbol)) {
            invalidate();
            if (!game.isGameOver())
                game.doComputerMove();
        } else {
            Log.d(getClass().getSimpleName(), "setSelectedCell: invalid selection");
            startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
        }
    }


}
