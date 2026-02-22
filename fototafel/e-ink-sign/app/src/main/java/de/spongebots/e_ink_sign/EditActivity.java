package de.spongebots.e_ink_sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "EInkSignPrefs";
    private static final String TEXT_KEY = "text";
    private static final String ORIENTATION_KEY = "orientation";
    private static final String THEME_KEY = "theme";
    private static final String NARROW_LENGTH_KEY = "narrow_length";

    private EditText multiLineEditText;
    private EditText narrowTextLengthEditText;
    private RadioGroup orientationRadioGroup;
    private RadioGroup themeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        multiLineEditText = findViewById(R.id.multiLineEditText);
        narrowTextLengthEditText = findViewById(R.id.narrowTextLengthEditText);
        Button showButton = findViewById(R.id.showButton);
        orientationRadioGroup = findViewById(R.id.orientationRadioGroup);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);

        loadPreferences();

        showButton.setOnClickListener(v -> {
            String text = multiLineEditText.getText().toString();
            Intent intent = new Intent(this, ShowActivity.class);
            intent.putExtra("text", text);

            int selectedOrientationId = orientationRadioGroup.getCheckedRadioButtonId();
            if (selectedOrientationId == R.id.landscapeRadioButton) {
                intent.putExtra("orientation", "landscape");
            } else {
                intent.putExtra("orientation", "portrait");
            }

            int selectedThemeId = themeRadioGroup.getCheckedRadioButtonId();
            if (selectedThemeId == R.id.darkThemeRadioButton) {
                intent.putExtra("theme", "dark");
            } else {
                intent.putExtra("theme", "light");
            }

            int narrowLength = 0;
            try {
                narrowLength = Integer.parseInt(narrowTextLengthEditText.getText().toString());
            } catch (NumberFormatException e) {
                // ignore and use default
            }
            intent.putExtra("narrow_text_length", narrowLength);

            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String text = prefs.getString(TEXT_KEY, "");
        int orientationId = prefs.getInt(ORIENTATION_KEY, R.id.portraitRadioButton);
        int themeId = prefs.getInt(THEME_KEY, R.id.lightThemeRadioButton);
        int narrowLength = prefs.getInt(NARROW_LENGTH_KEY, 12);

        multiLineEditText.setText(text);
        narrowTextLengthEditText.setText(String.valueOf(narrowLength));
        orientationRadioGroup.check(orientationId);
        themeRadioGroup.check(themeId);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(TEXT_KEY, multiLineEditText.getText().toString());
        editor.putInt(ORIENTATION_KEY, orientationRadioGroup.getCheckedRadioButtonId());
        editor.putInt(THEME_KEY, themeRadioGroup.getCheckedRadioButtonId());
        try {
            editor.putInt(NARROW_LENGTH_KEY, Integer.parseInt(narrowTextLengthEditText.getText().toString()));
        } catch (NumberFormatException e) {
            // ignore and don't save
        }
        editor.apply();
    }
}