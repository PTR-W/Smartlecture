package de.SmartLecture.application.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Photo;
import de.SmartLecture.application.helper.PhotoAdapter;
import de.SmartLecture.application.helper.PhotoViewModel;

public class ViewPhotos extends AppCompatActivity {
    private PhotoViewModel photoViewModel;
    public static final String LOG_TAG = "MyLog";
    public static final String EXTRA_PHOTO_SUBJECT = "SmartLecture.EXTRA_PHOTO_SUBJECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);

        RecyclerView recyclerView = findViewById(R.id.photo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PhotoAdapter photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
        Intent intent = getIntent();

        Log.i("MyLog",intent.getStringExtra(EXTRA_PHOTO_SUBJECT));
        photoViewModel = ViewModelProviders.of(this ).get(PhotoViewModel.class) ;

        //
//        private void observerSetup(){
//            photoViewModel.getAllphotos().observe(this, new Observer<List<Photo>>(){
//                @Override
//                public void onChanged(@Nullable final List<Photo> photos){
//                    photoAdapter.setPhotos(photos);
//                }
//            });
//        }
        ///
        photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));
        photoViewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                photoAdapter.setPhotos(photos);
            }
        });
//        photoViewModel.getSearchResults().observe(this, new Observer<List<Photo>>() {
//            @Override
//            public void onChanged(@Nullable List<Photo> photos) {
//                if (photos.size() > 0)
//                {
//                    photoAdapter.setPhotos(photos);
//                }
//            }
//        });
    }
}
