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

public class Board extends View {
	private final Game game;
	private float width;
	private float height;
	private int selX;
	private int selY;
	private final Rect selRect = new Rect();

	public Board(Context context) {
		super(context);
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / 3f;
		height = h / 3f;
		getRect(selX, selY, selRect);
		Log.d(getClass().getSimpleName(), "onSizeChanged: width " + width
				+ ",height " + height);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.board_background));
		canvas.drawRect(0,  0, getWidth(), getHeight(), background);
		
		// Draw the board
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.board_dark));
		
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.board_hilite));
		
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.board_light));
		
		for (int i = 0; i < 3; i++) {
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
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
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				canvas.drawText(this.game.getCellString(i,j), i * width + x, j * height + y, foreground);
			}
		}
		
		// Draw the selected (active) cell
		Log.d(getClass().getSimpleName(), "selRect=" + selRect);
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.board_selected));
		canvas.drawRect(selRect, selected);
	}

	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), 2);
		selY = Math.min(Math.max(y, 0), 2);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ( event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		
		select((int) (event.getX() / width), (int) (event.getY() / height));
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
