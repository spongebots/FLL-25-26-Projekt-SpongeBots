package de.spongebots.e_ink_sign;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText narrowTextLengthEditText;
    private RadioGroup orientationRadioGroup;
    private RadioGroup themeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        narrowTextLengthEditText = findViewById(R.id.narrowTextLengthEditText);
        orientationRadioGroup = findViewById(R.id.orientationRadioGroup);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);

        loadPreferences();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        int orientationId = prefs.getInt(AppPreferences.ORIENTATION_KEY, R.id.portraitRadioButton);
        int themeId = prefs.getInt(AppPreferences.THEME_KEY, R.id.lightThemeRadioButton);
        int narrowLength = prefs.getInt(AppPreferences.NARROW_LENGTH_KEY, 12);

        narrowTextLengthEditText.setText(String.valueOf(narrowLength));
        orientationRadioGroup.check(orientationId);
        themeRadioGroup.check(themeId);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(AppPreferences.ORIENTATION_KEY, orientationRadioGroup.getCheckedRadioButtonId());
        editor.putInt(AppPreferences.THEME_KEY, themeRadioGroup.getCheckedRadioButtonId());
        try {
            editor.putInt(AppPreferences.NARROW_LENGTH_KEY, Integer.parseInt(narrowTextLengthEditText.getText().toString()));
        } catch (NumberFormatException e) {
            // ignore and don't save
        }
        editor.apply();
    }
}
