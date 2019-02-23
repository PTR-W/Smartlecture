package de.SmartLecture.application.helper;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import de.SmartLecture.application.DAO.PhotoDAO;

public class PhotoRepository {
    private PhotoDAO photoDAO;
    private LiveData<List<Photo>> allPhotos;

    public PhotoRepository(Application application){
        SubjectDatabase database = SubjectDatabase.getInstance(application);
        photoDAO = database.photoDAO();
        allPhotos = photoDAO.getPhotos("abc");
    }
    public void insert(Photo photo)
    {
        new InsertPhotoAsyncTask(photoDAO).execute(photo);
    }
    public void delete(Photo photo)
    {
        new DeletePhotoAsyncTask(photoDAO).execute(photo);
    }
    public LiveData<List<Photo>> getAllPhotos()
    {
        return allPhotos;
    }

    public static class InsertPhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
        private PhotoDAO photoDAO;
        private InsertPhotoAsyncTask(PhotoDAO photoDAO){
            this.photoDAO = photoDAO;
        }

        @Override
        protected Void doInBackground(Photo... photos){
            photoDAO.insert(photos[0]);
            return null;
        }
    }

    public static class DeletePhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
        private PhotoDAO photoDAO;
        private DeletePhotoAsyncTask(PhotoDAO photoDAO){
            this.photoDAO = photoDAO;
        }

        @Override
        protected Void doInBackground(Photo... photos){
            photoDAO.delete(photos[0]);
            return null;
        }
    }
}