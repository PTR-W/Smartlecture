package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
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
import de.SmartLecture.application.helper.PhotoViewModel;
import de.SmartLecture.application.helper.FolderNameGenerator;
import de.SmartLecture.application.helper.Subject;
import de.SmartLecture.application.helper.SubjectViewModel;

public class ShowPicture extends AppCompatActivity {

    public static final String LOG_TAG = "MyLog";
    private String subjectName = "Default";
    private String mfilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
        Bundle extras = this.getIntent().getExtras();

        // Contrast
        ColorMatrix colorMatrix = new ColorMatrix();
        float ContrastFactor = 1.2f;
        colorMatrix.set(new float[] {
                ContrastFactor, 0, 0, 0, 0,   // Red
                0, ContrastFactor, 0, 0, 0,   // Green
                0, 0, ContrastFactor, 0, 0,   // Blue
                0, 0, 0, 1, 0 });             // Alpha

        ImageView image;
        if (extras != null)
        {
            String path = extras.getString("filename");
            image= findViewById(R.id.image);
            image.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            image.setImageURI(Uri.parse(path));
            saveImage(path);
        }
    }

    private void saveImage(String filePath)
    {
        String state = Environment.getExternalStorageState();
        getSubjectName();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            String folder = FolderNameGenerator.generateFolderName();
            File dir = new File(root.getAbsolutePath() + "/SmartLecture/"+ folder);
            dir.mkdirs();
            int trimStart = filePath.lastIndexOf("/");
            int trimEnd = filePath.lastIndexOf(".");
            String imageFileName= filePath.substring(trimStart, trimEnd);
            File file = new File(root.getAbsolutePath() + "/SmartLecture/"+folder +imageFileName+".jpg");

            try
            {
                file.createNewFile();
                FileOutputStream oStream = new FileOutputStream(file);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
                oStream.close();
                mfilePath = file.getPath();
                //saveToDb(file.getPath(),subjectName);
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
    }

    private void getSubjectName(){
        SubjectViewModel subjectViewModel = ViewModelProviders.of(this ).get(SubjectViewModel.class) ;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String day = dayFormat.format(date);
        String time = timeFormat.format(date);
        Log.i(LOG_TAG,  day);
        subjectViewModel.findSubject(day, time);
        subjectViewModel.getSearchResults().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                if (subjects.size() > 0) {
                    subjectName = subjects.get(0).getTitle();
                    saveToDb(mfilePath, subjectName);
                }
                else{
                    subjectName = "Default";
                    saveToDb(mfilePath, subjectName);
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
