package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Subject;
import de.SmartLecture.application.helper.SubjectAdapter;
import de.SmartLecture.application.helper.SubjectViewModel;
import de.SmartLecture.application.helper.sweetArrayAdapter;
import de.SmartLecture.domain.Lecture.Lecture;
import de.SmartLecture.domain.helper.Weekday;

public class Schedule extends AppCompatActivity {
    public static final int ADD_SUBJECT_REQUEST = 2;

    private SubjectViewModel subjectViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        FloatingActionButton btnAddSubject = findViewById(R.id.button_add_subject);
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Schedule.this, AddSubject.class);
                startActivityForResult(intent, ADD_SUBJECT_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final SubjectAdapter adapter = new SubjectAdapter();
        recyclerView.setAdapter(adapter);


        subjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        subjectViewModel.getAllSubjects().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                adapter.setSubjects(subjects);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK)
        {
            String title        = data.getStringExtra(AddSubject.EXTRA_TITLE);
            String dateStart    = data.getStringExtra(AddSubject.EXTRA_DATE_START);
            String dateEnd      = data.getStringExtra(AddSubject.EXTRA_DATE_END);

            Subject subject = new Subject(title, dateStart, dateEnd);
            subjectViewModel.insert(subject);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(this, "Discarded", Toast.LENGTH_SHORT).show();
        }
    }
}
