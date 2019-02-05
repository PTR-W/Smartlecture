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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import java.util.List;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Subject;
import de.SmartLecture.application.helper.SubjectAdapter;
import de.SmartLecture.application.helper.SubjectViewModel;

public class Schedule extends AppCompatActivity {
    public static final int ADD_SUBJECT_REQUEST = 3;
    public static final int EDIT_SUBJECT_REQUEST = 4;
    public static final String LOG_TAG = "MyLog";

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
                //subjectAdapter.submitList(subjects);
            }
        });
        registerForContextMenu(recyclerView);
        onSwipe(subjectAdapter, recyclerView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getGroupId();
        Log.i("MyLog", ""+position);
        Subject subject = subjectAdapter.getSubjectAt(position);
        switch (item.getItemId())
        {
            case R.id.option_edit:

                Intent intent = new Intent(Schedule.this, AddEditSubject.class);
                intent.putExtra(AddEditSubject.EXTRA_ID, subject.getId());
                intent.putExtra(AddEditSubject.EXTRA_TITLE, subject.getTitle());
                intent.putExtra(AddEditSubject.EXTRA_DATE_START, subject.getDateStart());
                startActivityForResult(intent, EDIT_SUBJECT_REQUEST);
                return true;
            case R.id.option_delete:
                subjectViewModel.delete(subjectAdapter.getSubjectAt(position));

                return true;
            default:
                    return super.onContextItemSelected(item);
        }
    }

    private void onSwipe(final SubjectAdapter adapter, RecyclerView recyclerView)
    {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                subjectViewModel.delete(adapter.getSubjectAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Schedule.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditSubject.EXTRA_TITLE);
            String dateStart = data.getStringExtra(AddEditSubject.EXTRA_DATE_START);
            String dateEnd = data.getStringExtra(AddEditSubject.EXTRA_DATE_END);

            Subject subject = new Subject(title, dateStart, dateEnd);
            subjectViewModel.insert(subject);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_SUBJECT_REQUEST && resultCode == RESULT_OK)
        {
            int id = data.getIntExtra(AddEditSubject.EXTRA_ID, -1);
            // maybe put a check
            String title = data.getStringExtra(AddEditSubject.EXTRA_TITLE);
            String dateStart = data.getStringExtra(AddEditSubject.EXTRA_DATE_START);
            String dateEnd = data.getStringExtra(AddEditSubject.EXTRA_DATE_END);

            Subject subject = new Subject(title, dateStart, dateEnd);
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
                subjectViewModel.deleteAllSubjects();
                Toast.makeText(Schedule.this, "Exterminated", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
