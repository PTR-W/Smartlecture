package de.SmartLecture.application.activity;

import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import de.SmartLecture.R;
import de.SmartLecture.application.helper.Photo;
import de.SmartLecture.application.helper.PhotoAdapter;
import de.SmartLecture.application.helper.PhotoViewModel;

public class ViewPhotos extends AppCompatActivity implements EditPhoto.FragmentListener {
    public static final String EXTRA_PHOTO_SUBJECT = "SmartLecture.EXTRA_PHOTO_SUBJECT";
    private PhotoAdapter photoAdapter;
    private PhotoViewModel photoViewModel;
    private Intent intent;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);

        RecyclerView recyclerView = findViewById(R.id.photo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
        intent = getIntent();

        photoViewModel = ViewModelProviders.of(this ).get(PhotoViewModel.class) ;
        photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));
        photoViewModel.getSearchResults().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                photoAdapter.setPhotos(photos);
                if (photos.size() == 0)
                {
                    Toast.makeText(ViewPhotos.this, "No Photos for this Subject", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerForContextMenu(recyclerView);
        photoAdapter.setOnItemClickListener(new PhotoAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Photo photo) {

                Intent intent = new Intent(ViewPhotos.this, ShowPhoto.class);
                intent.putExtra(ShowPhoto.EXTRA_IMAGE_PATH, photo.getFileName());
                Log.i("MyLog", photo.getFileName());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getGroupId();
        switch (item.getItemId())
        {
            case R.id.option_delete:
                photoViewModel.delete(photoAdapter.getPhotoAt(position));
                photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));

            case R.id.option_edit:

                fragment = new EditPhoto();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.view_photos_container, fragment).commit();
//                    Intent intent = new Intent(ViewPhotos.this, EditPhoto.class);
//                    Photo photo = photoAdapter.getPhotoAt(position);
//                    intent.putExtra(AddEditSubject.EXTRA_ID, .getId());
//                    intent.putExtra(AddEditSubject.EXTRA_DAY, subject.getDay());
//                    intent.putExtra(AddEditSubject.EXTRA_TITLE, subject.getTitle());
//                    intent.putExtra(AddEditSubject.EXTRA_DATE_START, subject.getDateStart());
//                    startActivity(intent);
                      return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onInputSent(String input) {
        Log.i("MyLog", input);
    }
}