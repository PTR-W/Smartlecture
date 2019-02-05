package de.SmartLecture.application.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import de.SmartLecture.R;
import de.SmartLecture.application.listener.BtoNewAct;

public class OpenCamera extends AppCompatActivity {

    public static final String LOG_TAG = "MyLog";
    private static final int CAMERA_REQUEST = 1;
    private static final int PERMISSION_REQUEST = 2;
    private String imageFilePath = "";
    private Button CameraBtn;
    private Button ScheduleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void CamBtnListener() {
        CameraBtn = findViewById(R.id.btnCamera);
        CameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, "de.SmartLecture.provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getPath();

        return image;
    }

    protected void onResume () {
        super.onResume();
        setContentView(R.layout.open_camera);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        }
        setContentView(R.layout.open_camera);
        CamBtnListener();
        ScheduleBtn = findViewById(R.id.btnSchedule);
        ScheduleBtn.setOnClickListener(new BtoNewAct(this, Schedule.class));
    }

    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Intent showPictureIntent = new Intent(this, ShowPicture.class);
            showPictureIntent.putExtra("filename", imageFilePath);
            startActivity(showPictureIntent);
        }
    }
}