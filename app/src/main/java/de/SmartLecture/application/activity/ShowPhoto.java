package de.SmartLecture.application.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.SmartLecture.R;

public class ShowPhoto extends AppCompatActivity {
    public static final String EXTRA_IMAGE_PATH = "SmartLecture.EXTRA_IMAGE_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);
        // Hides the UI
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        ImageView image = findViewById(R.id.show_picture_image);
        image.setImageURI(Uri.parse(intent.getStringExtra(EXTRA_IMAGE_PATH)));
        // Sets the picture in full build
        image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        image.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}