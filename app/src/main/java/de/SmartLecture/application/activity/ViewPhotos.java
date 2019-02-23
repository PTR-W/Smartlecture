package de.SmartLecture.application.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Photo;
import de.SmartLecture.application.helper.PhotoAdapter;
import de.SmartLecture.application.helper.PhotoViewModel;

public class ViewPhotos extends AppCompatActivity {
    private PhotoViewModel photoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);

        RecyclerView recyclerView = findViewById(R.id.photo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PhotoAdapter photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel.class);
        photoViewModel.getPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                photoAdapter.setPhotos(photos);
            }
        });
    }
}
