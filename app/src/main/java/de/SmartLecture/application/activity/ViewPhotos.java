package de.SmartLecture.application.activity;

import java.util.List;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;


import de.SmartLecture.R;
import de.SmartLecture.application.helper.Photo;
import de.SmartLecture.application.helper.PhotoAdapter;
import de.SmartLecture.application.helper.PhotoViewModel;

public class ViewPhotos extends AppCompatActivity {
    private PhotoViewModel photoViewModel;
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

        photoViewModel = ViewModelProviders.of(this ).get(PhotoViewModel.class) ;
        photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));
        photoViewModel.getSearchResults().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                if (photos.size() > 0)
                {
                    photoAdapter.setPhotos(photos);
                }
                else {
                    Toast.makeText(ViewPhotos.this, "No Photos for this Subject", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
