package de.SmartLecture.application.activity;

import android.os.Bundle;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

import de.SmartLecture.R;
import de.SmartLecture.application.listener.BtoNewAct;

public class MainActivity extends AppCompatActivity {
    //public static final String LOG_TAG = "MyLog";
    //Log.i("MyLog", "Hello");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button execute = findViewById(R.id.button);

        execute.setOnClickListener(new BtoNewAct(this, OpenCamera.class));
    }
}
