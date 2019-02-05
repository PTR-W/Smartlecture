package de.SmartLecture.application.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.SmartLecture.R;

import static java.lang.String.format;

public class AddEditSubject extends AppCompatActivity {
    public static final String EXTRA_ID = "SmartLecture.EXTRA_ID";
    public static final String EXTRA_TITLE = "SmartLecture.EXTRA_TITLE";
    public static final String EXTRA_DATE_START = "SmartLecture.EXTRA_DATE_START";
    public static final String EXTRA_DATE_END = "SmartLecture.EXTRA_DATE_END";

    private EditText editTextTitle;
    private EditText editTextDay;
    private TimePicker timePickerStart;
    private TimePicker timePickerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDay = findViewById(R.id.edit_day);
        timePickerStart = findViewById(R.id.date_start_picker);
        timePickerStart.setIs24HourView(true);
        timePickerEnd = findViewById(R.id.date_end_picker);
        timePickerEnd.setIs24HourView(true);

        timePickerStart.setHour(8); timePickerStart.setMinute(0);
        timePickerEnd.setHour(10); timePickerEnd.setMinute(0);


        getSupportActionBar().setHomeAsUpIndicator(0);
        getSupportActionBar().setHomeActionContentDescription(0);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID))
        {
            toolbarTitle.setText("Edit Subject");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDay.setText((intent.getStringExtra(EXTRA_DATE_START)).substring(0,3));
        }
        else{
            toolbarTitle.setText("Add Subject");
        }

    }

    private void saveSubject()
    {
        String title = editTextTitle.getText().toString();
        String day = editTextDay.getText().toString();
        String dateStart = day + " "+  String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String dateEnd = day + " "+  String.format("%02d:%02d", timePickerEnd.getHour(), timePickerEnd.getMinute());

        if(title.trim().isEmpty())
        {
            Toast.makeText(this, "The title can not be left empty", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (day.trim().isEmpty())
        {
            Toast.makeText(this, "The day can not be left empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DATE_START, dateStart);
        data.putExtra(EXTRA_DATE_END, dateEnd);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1)
        {
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_subject_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_subject:
                saveSubject();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
