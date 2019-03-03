package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;
import java.util.List;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.*;

public class Schedule extends AppCompatActivity {
    public static final int ADD_SUBJECT_REQUEST = 3;
    public static final int EDIT_SUBJECT_REQUEST = 4;

    private SubjectViewModel subjectViewModel;
    private SubjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        FloatingActionButton btnAddSubject = findViewById(R.id.button_add_subject);
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Schedule.this, AddEditSubject.class);
                startActivityForResult(intent, ADD_SUBJECT_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        subjectAdapter = new SubjectAdapter();
        recyclerView.setAdapter(subjectAdapter);

        subjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel.class);
        subjectViewModel.getAllSubjects().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                subjectAdapter.setSubjects(subjects);
            }
        });
        registerForContextMenu(recyclerView);
        onSwipe(subjectAdapter, recyclerView);

        subjectAdapter.setOnItemClickListener(new SubjectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Subject subject) {

                Intent intent = new Intent(Schedule.this, ViewPhotos.class);
                intent.putExtra(ViewPhotos.EXTRA_PHOTO_SUBJECT, subject.getTitle());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getGroupId();
        Subject subject = subjectAdapter.getSubjectAt(position);
        switch (item.getItemId())
        {
            case R.id.option_edit:
                if (subject.getTitle().equals("Default"))
                {
                    Toast.makeText(Schedule.this, "Can not edit Default folder", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(Schedule.this, AddEditSubject.class);
                    intent.putExtra(AddEditSubject.EXTRA_ID, subject.getId());
                    intent.putExtra(AddEditSubject.EXTRA_DAY, subject.getDay());
                    intent.putExtra(AddEditSubject.EXTRA_TITLE, subject.getTitle());
                    intent.putExtra(AddEditSubject.EXTRA_DATE_START, subject.getDateStart());
                    startActivityForResult(intent, EDIT_SUBJECT_REQUEST);
                }
                return true;
            case R.id.option_delete:
                if (subject.getTitle().equals("Default"))
                {
                    Toast.makeText(Schedule.this, "Can not delete Default folder", Toast.LENGTH_SHORT).show();
                }
                else {
                    subjectViewModel.delete(subjectAdapter.getSubjectAt(position));
                }

                return true;
            default:
                    return super.onContextItemSelected(item);
        }
    }

    // Swiping any element to the left
    private void onSwipe(final SubjectAdapter adapter, RecyclerView recyclerView)
    {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Subject subject = adapter.getSubjectAt(viewHolder.getAdapterPosition());
                if(subject.getTitle().equals("Default"))
                {
                    Toast.makeText(Schedule.this, "Can not delete Default folder", Toast.LENGTH_SHORT).show();
                    subjectViewModel.getAllSubjects().observe(Schedule.this, new Observer<List<Subject>>() {
                        @Override
                        public void onChanged(@Nullable List<Subject> subjects) {
                            subjectAdapter.setSubjects(subjects);
                        }
                    });
                }
                else {
                    subjectViewModel.delete(subject);
                    Toast.makeText(Schedule.this, subject.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    //After the AddEditSubject activity is destroyed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            String title = intent.getStringExtra(AddEditSubject.EXTRA_TITLE);
            String day = intent.getStringExtra(AddEditSubject.EXTRA_DAY);
            String dateStart = intent.getStringExtra(AddEditSubject.EXTRA_DATE_START);
            String dateEnd = intent.getStringExtra(AddEditSubject.EXTRA_DATE_END);

            Subject subject = new Subject(title, day, dateStart, dateEnd);
            subjectViewModel.insert(subject);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_SUBJECT_REQUEST && resultCode == RESULT_OK)
        {
            int id = intent.getIntExtra(AddEditSubject.EXTRA_ID, -1);
            // maybe put a check if the value is -1
            String title = intent.getStringExtra(AddEditSubject.EXTRA_TITLE);
            String day = intent.getStringExtra(AddEditSubject.EXTRA_DAY);
            String dateStart = intent.getStringExtra(AddEditSubject.EXTRA_DATE_START);
            String dateEnd = intent.getStringExtra(AddEditSubject.EXTRA_DATE_END);

            Subject subject = new Subject(title, day, dateStart, dateEnd);
            subject.setId(id);
            subjectViewModel.update(subject);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Discarded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_btn:
                PhotoViewModel photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
                photoViewModel.deleteAllPhotos();
                subjectViewModel.deleteAllSubjects();
                Toast.makeText(Schedule.this, "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
