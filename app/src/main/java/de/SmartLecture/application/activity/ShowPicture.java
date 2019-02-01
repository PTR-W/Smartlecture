package de.SmartLecture.application.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import de.SmartLecture.R;
import de.SmartLecture.application.helper.FolderNameGenerator;

import static android.os.Environment.DIRECTORY_PICTURES;

public class ShowPicture extends AppCompatActivity {

    private ImageView image;
    public static final String LOG_TAG = "MyLog";

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


        if (extras != null)
        {
            String path = extras.getString("filename");
            image= findViewById(R.id.image);
            image.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
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

            String folder = FolderNameGenerator.generateFolderName();
            File dir = new File(root.getAbsolutePath() + "/SmartLecture/"+ folder);
            dir.mkdirs();
            int trimStart = filePath.lastIndexOf("/");
            int trimEnd = filePath.lastIndexOf(".");
            String imageFileName= filePath.substring(trimStart, trimEnd);
            Log.i(LOG_TAG, imageFileName);
            File file = new File(root.getAbsolutePath() + "/SmartLecture/"+folder +"/"+imageFileName+".jpg");

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
