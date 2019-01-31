package de.SmartLecture.application.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.SmartLecture.R;

import static android.os.Environment.DIRECTORY_PICTURES;

public class ShowPicture extends AppCompatActivity {

    private ImageView image;
    public static final String LOG_TAG = "MyLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null)
        {
            String path = extras.getString("filename");
            image= findViewById(R.id.image);
            image.setImageURI(Uri.parse(path));
            saveimage(path);
        }
    }

    private void saveimage(String filePath)
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File root = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);

            File dir = new File(root.getAbsolutePath() + "/SmartLecture");
            dir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "IMG_" + timeStamp + "_";
            File file = new File(root.getAbsolutePath() + "/SmartLecture/"+imageFileName+".jpg");

            try
            {
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();
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
}
