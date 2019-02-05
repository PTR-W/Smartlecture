package de.SmartLecture.application.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import de.SmartLecture.R;
import de.SmartLecture.application.listener.BtoNewAct;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        //setTitle("SmartLecture");

        Button execute = findViewById(R.id.button);

        execute.setOnClickListener(new BtoNewAct(this, OpenCamera.class));
    }
}
