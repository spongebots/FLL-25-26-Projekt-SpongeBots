package de.spongebots.e_ink_sign;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences(AppPreferences.PREFS_NAME, MODE_PRIVATE);

        int selectedThemeId = prefs.getInt(AppPreferences.THEME_KEY, R.id.lightThemeRadioButton);
        if (selectedThemeId == R.id.darkThemeRadioButton) {
            setTheme(R.style.Theme_EinkSign_Dark);
        } else {
            setTheme(R.style.Theme_EinkSign_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(v -> {
            finish();
        });

        TextView textView = findViewById(R.id.fullScreenTextView);
        Typeface normalTypeface = ResourcesCompat.getFont(this, R.font.ptsans_bold);
        Typeface narrowTypeface = ResourcesCompat.getFont(this, R.font.ptsans_narrow_bold);
        textView.setTypeface(normalTypeface);

        String text = prefs.getString(AppPreferences.TEXT_KEY, "");
        int narrowTextLength = prefs.getInt(AppPreferences.NARROW_LENGTH_KEY, 12);

        if (!text.isEmpty()) {
            SpannableString spannableString = new SpannableString(text);
            if (narrowTextLength > 0) {
                int start = 0;
                while (start < text.length()) {
                    int end = text.indexOf('\n', start);
                    if (end == -1) {
                        end = text.length();
                    }
                    if (end - start >= narrowTextLength + 1) {
                        spannableString.setSpan(new CustomTypefaceSpan(narrowTypeface), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    start = end + 1;
                }
            }
            textView.setText(spannableString);
        }

        int selectedOrientationId = prefs.getInt(AppPreferences.ORIENTATION_KEY, R.id.portraitRadioButton);
        if (selectedOrientationId == R.id.landscapeRadioButton) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }

    private void hideNavigationBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private static class CustomTypefaceSpan extends MetricAffectingSpan {
        private final Typeface typeface;

        CustomTypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint);
        }

        private void applyCustomTypeFace(Paint paint) {
            paint.setTypeface(typeface);
        }
    }
}
