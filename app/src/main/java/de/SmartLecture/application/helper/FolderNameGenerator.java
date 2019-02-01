package de.SmartLecture.application.helper;

import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FolderNameGenerator extends AppCompatActivity {
    public static String generateFolderName()
    {
        String timeStamp = new SimpleDateFormat("E", Locale.getDefault()).format(new Date());
        return timeStamp;
    }
}
