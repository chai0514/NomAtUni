package com.example.groupassignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create LinearLayout with vertical orientation
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundResource(R.drawable.asplashscreen);
        linearLayout.setGravity(Gravity.CENTER);
        setContentView(linearLayout);

        // Create ImageView
        ImageView logoImageView = new ImageView(this);
        logoImageView.setId(View.generateViewId());
        logoImageView.setImageResource(R.drawable.applogo);
        logoImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        // Add ImageView to LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                604, 400
        );
        logoImageView.setLayoutParams(params);
        linearLayout.addView(logoImageView);

        // Create ProgressBar
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setId(View.generateViewId());
        progressBar.setVisibility(View.INVISIBLE);

        // Add ProgressBar to LinearLayout
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        progressBar.setLayoutParams(params);
        linearLayout.addView(progressBar);

        // Add fade-in animation for ImageView
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(2500); // Adjust the duration as needed
        logoImageView.startAnimation(fadeInAnimation);

        // Add a delay before starting the next activity
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.VISIBLE); // Make ProgressBar visible

            // Start the next activity after loading
            startActivity(new Intent(splash_screen.this, sign_in_page.class));
            finish();
        }, 6500);
    }
}
