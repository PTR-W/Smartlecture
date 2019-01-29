package de.SmartLecture.application.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.sweetArrayAdapter;
import de.SmartLecture.domain.Lecture.Lecture;
import de.SmartLecture.domain.helper.Weekday;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView list = findViewById(R.id.sweetList);

        //test
        ArrayList<Lecture> exampleList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            Lecture lecture = new Lecture("Vorlesung" + i, Weekday.values()[i], new Date(), new Date());
            exampleList.add(lecture);
        }

        list.setAdapter(new sweetArrayAdapter(this, exampleList));
    }
}
