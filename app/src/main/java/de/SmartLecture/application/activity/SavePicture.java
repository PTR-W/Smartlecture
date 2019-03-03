package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ImageView;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Photo;
import de.SmartLecture.application.helper.Subject;
import de.SmartLecture.application.helper.PhotoViewModel;
import de.SmartLecture.application.helper.SubjectViewModel;

public class SavePicture extends AppCompatActivity {
    private String path;
    private ImageView image;
    private String dbFilePath = "";
    private String subjectName = "Default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_picture);
        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            path = extras.getString("filename");
            image = findViewById(R.id.save_picture_image);
            setBrightness();
            image.setImageURI(Uri.parse(path));
        }
    }

    // Modifies the brightness of the picture
    private void setBrightness(){
        SeekBar brightnessBar = findViewById(R.id.brightness_bar);
        int brightnessMin = 0;
        int brightnessMax = 100;
        int brightnessCurrent = 50;
        brightnessBar.setMax(brightnessMax-brightnessMin);
        brightnessBar.setProgress(brightnessCurrent- brightnessMin);
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ColorMatrix colorMatrix = new ColorMatrix();
                float brightnessFactor = (progress+50)/100f;
                colorMatrix.set(new float[] {
                        brightnessFactor, 0, 0, 0, 0,   // Red
                        0, brightnessFactor, 0, 0, 0,   // Green
                        0, 0, brightnessFactor, 0, 0,   // Blue
                        0, 0, 0, 1, 0 });             // Alpha
                image.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                saveImage(path);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Saves the image file on the internal storage
    private void saveImage(String filePath)
    {
        String state = Environment.getExternalStorageState();
        getSubjectName();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            int trimStart = filePath.lastIndexOf("/");
            int trimEnd = filePath.lastIndexOf(".");
            String imageFileName= filePath.substring(trimStart, trimEnd);
            File file = new File(root.getAbsolutePath() + "/SmartLecture/" +imageFileName+".jpg");

            // Transforms the modified image into Bitmap
            image.setDrawingCacheEnabled(true);
            image.measure(View.MeasureSpec.makeMeasureSpec(image.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(image.getHeight(), View.MeasureSpec.EXACTLY));
            image.layout(0, 0, image.getMeasuredWidth(), image.getMeasuredHeight());
            image.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(image.getDrawingCache());
            image.setDrawingCacheEnabled(false); // clear drawing cache

            try
            {
                FileOutputStream oStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
                oStream.close();
                dbFilePath = file.getPath();
            }
            catch(FileNotFoundException e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"CHECK STORAGE", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(SavePicture.this, MainActivity.class);
        startActivity(intent);
    }

    // Gets the current subject name from the database
    private void getSubjectName(){
        SubjectViewModel subjectViewModel = ViewModelProviders.of(this ).get(SubjectViewModel.class) ;
        Date date = new Date();
        String day = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date);
        String time = new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date);
        subjectViewModel.findSubject(day, time);
        subjectViewModel.getSearchResults().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                if (subjects.size() > 0) {
                    subjectName = subjects.get(0).getTitle();
                    saveToDb(dbFilePath, subjectName);
                }
                else{
                    subjectName = "Default";
                    saveToDb(dbFilePath, subjectName);
                }
            }
        });
    }
    // Inserts the photo in the database
    private void saveToDb(String path, String subjectName)
    {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        PhotoViewModel photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        Photo photo = new Photo(path, subjectName);
        photoViewModel.insert(photo);
    }
}