package de.SmartLecture.application.activity;

import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
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
import de.SmartLecture.application.helper.*;

public class ViewPhotos extends AppCompatActivity implements EditPhoto.FragmentListener {
    public static final String EXTRA_PHOTO_SUBJECT = "SmartLecture.EXTRA_PHOTO_SUBJECT";
    private Intent intent;
    private Photo selectedPhoto;
    private PhotoAdapter photoAdapter;
    private RelativeLayout dimBackground;
    private PhotoViewModel photoViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the layout
        setContentView(R.layout.activity_view_photos);
        // declare the background
        dimBackground = findViewById(R.id.view_photos_bg_dim);
        // set the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.photo_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // set the PhotoAdapter
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);
        // set the intent
        intent = getIntent();
        // look for photos related to the subjectName passed through Intent
        photoViewModel = ViewModelProviders.of(this ).get(PhotoViewModel.class) ;
        photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));
        // observe search results
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

        // listen to long click
        registerForContextMenu(recyclerView);
        // listen to and set action of click on an item.
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


    // set action of LongClick on an item.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getGroupId();
        switch (item.getItemId())
        {
            case R.id.option_delete:
                photoViewModel.delete(photoAdapter.getPhotoAt(position));
                photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));

            case R.id.option_edit:
                selectedPhoto = photoAdapter.getPhotoAt(position);
                Fragment fragment = new EditPhoto();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.view_photos_container, fragment).addToBackStack("fragment").commit();
                setBgVisibility(true);
                      return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    /*
    Implements the interface on the fragment.
    Handles the data sent back from the fragment.
     */
    @Override
    public void onInputSent(String input) {
        selectedPhoto.setSubject(input);
        SubjectViewModel subjectViewModel = ViewModelProviders.of(this ).get(SubjectViewModel.class) ;
        subjectViewModel.subjectExists(input);
        subjectViewModel.getSearchResults().observe(this, new Observer<List<Subject>>() {
            @Override
            public void onChanged(@Nullable List<Subject> subjects) {
                if (subjects.size() > 0) {
                    photoViewModel.update(selectedPhoto);
                    photoViewModel.findPhoto(intent.getStringExtra(EXTRA_PHOTO_SUBJECT));
                }
                else{
                    Toast.makeText(ViewPhotos.this, "Subject not in Database", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Log.i("MyLog", ""+selectedPhoto.getId() + " " + selectedPhoto.getSubject());
    }

    /*
    Called from the fragment.
    This method dims the background when the fragment pops up,
    and und clears it when the fragment is destroyed.
    the clearing call is called from inside the fragment
    */
    public void setBgVisibility(boolean visibility)
    {
        if (visibility)
        {
            dimBackground.setVisibility(View.VISIBLE);
        }
        else dimBackground.setVisibility(View.INVISIBLE);
    }
    /*
    Called from the fragment.
    Sets the EditText on the fragment.
    */
    public String getSubject()
    {
        return this.selectedPhoto.getSubject();
    }

    /*
    clears the background in case the fragment
    is destroyed through back navigation
     */
    @Override
    public void onBackPressed() {
        dimBackground.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }
}