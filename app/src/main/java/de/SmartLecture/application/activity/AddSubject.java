package de.SmartLecture.application.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import de.SmartLecture.R;

import static java.lang.String.format;

public class AddSubject extends AppCompatActivity {
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

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDay = findViewById(R.id.edit_day);
        timePickerStart = findViewById(R.id.date_start_picker);
        timePickerStart.setIs24HourView(true);
        timePickerEnd = findViewById(R.id.date_end_picker);



        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Subject");
    }

    private void saveSubject()
    {
        String title = editTextTitle.getText().toString();
        String day = editTextDay.getText().toString();
        //String dateStart = day+" 08:00";
        //String dateEnd = day + " 10:00";
        String dateStart = String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String dateEnd = String.format("%02d:%02d", timePickerStart.getHour()+2, timePickerStart.getMinute());

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
