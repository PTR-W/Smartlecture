package de.SmartLecture.application.helper;

import java.util.List;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.app.Application;
import android.arch.lifecycle.LiveData;

import de.SmartLecture.application.DAO.PhotoDAO;

public class PhotoRepository{
    private PhotoDAO photoDAO;
    private LiveData<List<Photo>> allPhotos;
    private LiveData<List<Photo>> searchResults;

    public PhotoRepository(Application application){
        SubjectDatabase database = SubjectDatabase.getInstance(application);
        photoDAO = database.photoDAO();
        allPhotos = photoDAO.getAllPhotos();
    }

    public LiveData<List<Photo>> getAllPhotos() { return allPhotos; }
    public LiveData<List<Photo>> getSearchResults(){return searchResults;}
    public void insert(Photo photo)
    {
        new InsertPhotoAsyncTask(photoDAO).execute(photo);
    }
    public void delete(Photo photo) { new DeletePhotoAsyncTask(photoDAO).execute(photo); }
    public void findPhoto(String subjectName){
        QueryPhotoAsyncTask task = new QueryPhotoAsyncTask(photoDAO);
        task.delegate = this;
        task.execute(subjectName);
    }

//    public LiveData<List<Photo>> getPhotos(String subjectName)
//    {
//        new QueryPhotoAsyncTask(photoDAO).execute(subjectName);
//        return photos;
//    }

    private void asyncFinished(LiveData<List<Photo>> result)
    {
        searchResults = result;
    }

    private static class InsertPhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
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

    private static class DeletePhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
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

    private static class QueryPhotoAsyncTask extends AsyncTask<String, Void, LiveData<List<Photo>>> {
        private PhotoDAO photoDAO;
        private PhotoRepository delegate = null;
        private QueryPhotoAsyncTask(PhotoDAO photoDAO){
            this.photoDAO = photoDAO;
        }

        @Override
        protected LiveData<List<Photo>> doInBackground(String... subjectName){
            return photoDAO.findPhoto(subjectName[0]);
        }

        @Override protected void onPostExecute(LiveData<List<Photo>> result)
        {
            delegate.asyncFinished(result);
        }
    }
}