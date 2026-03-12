package de.spongebots.e_ink_sign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText multiLineEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        multiLineEditText = findViewById(R.id.multiLineEditText);
        Button showButton = findViewById(R.id.showButton);

        loadPreferences();

        showButton.setOnClickListener(v -> {
            savePreferences();
            Intent intent = new Intent(this, ShowActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    private void loadPreferences() {
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);
        String text = prefs.getString(AppPreferences.TEXT_KEY, "");
        multiLineEditText.setText(text);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(AppPreferences.TEXT_KEY, multiLineEditText.getText().toString());
        editor.apply();
    }
}
