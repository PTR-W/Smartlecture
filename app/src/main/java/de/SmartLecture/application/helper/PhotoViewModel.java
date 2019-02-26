package de.SmartLecture.application.helper;

import java.util.List;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.AndroidViewModel;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;
    private LiveData<List<Photo>> allPhotos;
    private MutableLiveData<List<Photo>> searchResults;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
        allPhotos = photoRepository.getAllPhotos();
        searchResults = photoRepository.getSearchResults();
    }

    public void delete(Photo photo)
    {
        photoRepository.delete(photo);
    }
    public void insert(Photo photo)
    {
        photoRepository.insert(photo);
    }
    public MutableLiveData<List<Photo>> getSearchResults(){ return searchResults; }
    public LiveData<List<Photo>> getAllPhotos(){return allPhotos; }
    public void findPhoto(String subjectName){photoRepository.findPhoto(subjectName);}

}
