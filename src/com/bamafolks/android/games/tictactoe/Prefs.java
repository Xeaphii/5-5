package com.bamafolks.android.games.tictactoe;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	private static final String OPT_COLOR = "color";
	private static final boolean OPT_COLOR_DEFAULT = true;
	private static final String OPT_MUSIC = "music";
	private static final boolean OPT_MUSIC_DEFAULT = true;

	public static boolean getColor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_COLOR, OPT_COLOR_DEFAULT);
	}

	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_MUSIC, OPT_MUSIC_DEFAULT);
	}
}
