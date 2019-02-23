package de.SmartLecture.application.helper;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;
    private LiveData<List<Photo>> photos;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
        photos = photoRepository.getAllPhotos();
    }

    public void delete(Photo photo)
    {
        photoRepository.delete(photo);
    }
    public void insert(Photo photo)
    {
        photoRepository.insert(photo);
    }
    public LiveData<List<Photo>> getPhotos(){
        return photos;
    }

}
