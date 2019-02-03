package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Subject;
import de.SmartLecture.application.helper.SubjectViewModel;
import de.SmartLecture.application.helper.sweetArrayAdapter;
import de.SmartLecture.domain.Lecture.Lecture;
import de.SmartLecture.domain.helper.Weekday;

public class Schedule extends AppCompatActivity {

    private SubjectViewModel subjectViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        subjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        subjectViewModel.getAllSubjects().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                //update RecyclerView
                Toast.makeText(Schedule.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
