package de.SmartLecture.application.listener;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class BtoNewAct implements View.OnClickListener {

    private Class dest;
    private AppCompatActivity sourceAct;

    public BtoNewAct(AppCompatActivity sourceAct, Class dest) {
        this.dest = dest;
        this.sourceAct = sourceAct;
    }

    @Override
    public void onClick(View v) {
        Intent newAct = new Intent(sourceAct, dest);
        sourceAct.startActivity(newAct);
    }
}
