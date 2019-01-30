package de.SmartLecture.application.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import de.SmartLecture.R;
public class ShowPicture extends AppCompatActivity {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null)
        {
            String path = extras.getString("filename");
            image= findViewById(R.id.image);
            image.setImageURI(Uri.parse(path));
        }
    }
}
