package com.bamafolks.android.games.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.continue_button).setOnClickListener(this);
		findViewById(R.id.new_button).setOnClickListener(this);
		findViewById(R.id.about_button).setOnClickListener(this);
		findViewById(R.id.exit_button).setOnClickListener(this);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Music.play(this, R.raw.intro);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
			// Add more as needed
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.about_button:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.new_button:
			openNewGameDialog();
			break;
		case R.id.exit_button:
			finish();
			break;
		case R.id.continue_button:
			startGame(Game.CONTINUE);
			break;
		}
		
	}

	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.new_game_title)
		.setItems(R.array.first_move, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startGame(which);
			}
		})
		.show();
	}

	protected void startGame(int which) {
		Log.d(this.getClass().getSimpleName(), "clicked on " + which);
		Intent intent = new Intent(this, Game.class);
		intent.putExtra(Game.FIRST_MOVE, which);
		startActivity(intent);
	}

}
