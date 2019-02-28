package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
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

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Photo;
import de.SmartLecture.application.helper.Subject;
import de.SmartLecture.application.helper.PhotoViewModel;
import de.SmartLecture.application.helper.SubjectViewModel;

public class ShowPicture extends AppCompatActivity {

    public static final String LOG_TAG = "MyLog";
    private String subjectName = "Default";
    private String dbFilePath = "";
    private String path;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            path = extras.getString("filename");
            image = findViewById(R.id.image);
            setContrast();
            image.setImageURI(Uri.parse(path));
        }
    }

    private void setContrast(){
        SeekBar contrastBar = findViewById(R.id.contrast_bar);
        int contrastMin = 0;
        int contrastMax = 100;
        int contrastCurrent = 50;
        contrastBar.setMax(contrastMax-contrastMin);
        contrastBar.setProgress(contrastCurrent- contrastMin);

        contrastBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ColorMatrix colorMatrix = new ColorMatrix();
                float contrastFactor = (progress+50)/100f;
                colorMatrix.set(new float[] {
                        contrastFactor, 0, 0, 0, 0,   // Red
                        0, contrastFactor, 0, 0, 0,   // Green
                        0, 0, contrastFactor, 0, 0,   // Blue
                        0, 0, 0, 1, 0 });             // Alpha
                image.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                Log.i(LOG_TAG, ""+contrastFactor);
                //contrastFactor = contrastProgress;
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
    private void saveImage(String filePath)
    {
        String state = Environment.getExternalStorageState();
        getSubjectName();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File dir = new File(root.getAbsolutePath() + "/SmartLecture/");
            dir.mkdirs();
            int trimStart = filePath.lastIndexOf("/");
            int trimEnd = filePath.lastIndexOf(".");
            String imageFileName= filePath.substring(trimStart, trimEnd);
            File file = new File(root.getAbsolutePath() + "/SmartLecture/" +imageFileName+".jpg");

            ///
            image.setDrawingCacheEnabled(true);

            image.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            image.layout(0, 0, image.getMeasuredWidth(), image.getMeasuredHeight());

            image.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(image.getDrawingCache());
            image.setDrawingCacheEnabled(false); // clear drawing cache

            ///

//            image.setDrawingCacheEnabled(true);
//            image.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
//            image.layout(0,0,image.getMeasuredWidth(), image.getMeasuredHeight());
//            image.buildDrawingCache(true);
//            Bitmap bitmap = Bitmap.createBitmap(image.getDrawingCache());
//            image.setDrawingCacheEnabled(false);

            try
            {
                FileOutputStream oStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
                oStream.close();
                dbFilePath = file.getPath();
            }
            catch(FileNotFoundException e)
            {
                Log.i(LOG_TAG, e.toString());
            }
            catch (IOException e)
            {
                Log.i(LOG_TAG, e.toString());
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"CHECK STORAGE", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(ShowPicture.this, OpenCamera.class);
        startActivity(intent);
    }

    private void getSubjectName(){
        SubjectViewModel subjectViewModel = ViewModelProviders.of(this ).get(SubjectViewModel.class) ;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String day = dayFormat.format(date);
        String time = timeFormat.format(date);
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

    private void saveToDb(String path, String subjectName)
    {
        PhotoViewModel photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        Photo photo = new Photo(path, subjectName);
        photoViewModel.insert(photo);
    }
}
