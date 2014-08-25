package com.hamzah.holothemer.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	Spinner colour_spinner;

	SharedPreferences pref;

	EditText input;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		colour_spinner = (Spinner) findViewById(R.id.spinner_colours);
		input = (EditText) findViewById(R.id.hex_input);

		pref = getSharedPreferences(Keys.PREF_NAME, Context.MODE_WORLD_READABLE);
	}

	public void apply(View v) {
		int id = colour_spinner.getSelectedItemPosition();
		int colour = colourByID(id);

		String hex = input.getText().toString();

		if (!hex.isEmpty() && hex != null)
			colour = Color.parseColor(hex);

		Editor editor = pref.edit();
		editor.putInt(Keys.COLOUR, colour);
		editor.apply();

		Toast.makeText(this, "Changes applied!", Toast.LENGTH_SHORT).show();
	}

	public static int colourByID(int id) {
		int colour = Color.BLACK;
		if (id == 0)
			colour = Color.RED;
		else if (id == 1)
			colour = Color.YELLOW;
		else if (id == 2)
			colour = Color.parseColor("#FF8000");
		else if (id == 3)
			colour = Color.GREEN;
		else if (id == 4)
			colour = Color.BLUE;
		else if (id == 5)
			colour = Color.parseColor("#8904B1");
		else if (id == 6)
			colour = Color.BLACK;
		else if (id == 7)
			colour = Color.parseColor("#585858");
		else if (id == 8)
			colour = Color.WHITE;
		else if (id == 9)
			colour = Color.parseColor("#009587");
		else if (id == 10)
			colour = Color.TRANSPARENT;
		return colour;
	}
}
