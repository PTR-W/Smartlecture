package de.SmartLecture.application.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import de.SmartLecture.R;

public class AddEditSubject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String EXTRA_ID = "SmartLecture.EXTRA_ID";
    public static final String EXTRA_TITLE = "SmartLecture.EXTRA_TITLE";
    public static final String EXTRA_DAY = "SmartLecture.EXTRA_DAY";
    public static final String EXTRA_DATE_START = "SmartLecture.EXTRA_DATE_START";
    public static final String EXTRA_DATE_END = "SmartLecture.EXTRA_DATE_END";

    private String day;
    private EditText editTextTitle;
    private TimePicker timePickerStart;
    private TimePicker timePickerEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout
        setContentView(R.layout.activity_add_subject);
        // Initialise layout elements
        editTextTitle = findViewById(R.id.edit_text_title);
        Spinner spinner = findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.days_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        timePickerStart = findViewById(R.id.date_start_picker);
        timePickerStart.setIs24HourView(true);
        timePickerEnd = findViewById(R.id.date_end_picker);
        timePickerEnd.setIs24HourView(true);

        timePickerStart.setHour(8); timePickerStart.setMinute(0);
        timePickerEnd.setHour(10); timePickerEnd.setMinute(0);

        // Set navigation button
        getSupportActionBar().setHomeAsUpIndicator(0);
        getSupportActionBar().setHomeActionContentDescription(0);

        // Check if the intention is to Add or Edit
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID))
        {
            setTitle("Edit Subject");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
        }
        else{
            setTitle("Add Subject");
        }
    }


    //Sends the input data back to the parent activity
    private void saveSubject()
    {
        String title = editTextTitle.getText().toString();
        String dateStart = String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String dateEnd = String.format("%02d:%02d", timePickerEnd.getHour(), timePickerEnd.getMinute());

        if(title.trim().isEmpty())
        {
            Toast.makeText(this, "The title can not be left empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DAY, day);
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
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    // if the save button is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                saveSubject();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Set the day to selected item in the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        day = parent.getItemAtPosition(position).toString();
    }

    // Set day to the first item if nothing is selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        day = parent.getItemAtPosition(0).toString();
    }
}
