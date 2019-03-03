package de.SmartLecture.application.helper;

import java.util.List;
import android.os.AsyncTask;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import de.SmartLecture.application.DAO.PhotoDAO;

    class PhotoRepository {
        private PhotoDAO photoDAO;
        private LiveData<List<Photo>> allPhotos;
        private MutableLiveData<List<Photo>> searchResults = new MutableLiveData<>();

        PhotoRepository(Application application){
            SubjectDatabase database = SubjectDatabase.getInstance(application);
            photoDAO = database.photoDAO();
            allPhotos = photoDAO.getAllPhotos();
    }
        LiveData<List<Photo>> getAllPhotos() { return allPhotos; }
        MutableLiveData<List<Photo>> getSearchResults(){return searchResults;}
        void insert(Photo photo)
    {
        new InsertPhotoAsyncTask(photoDAO).execute(photo);
    }
        void delete(Photo photo) { new DeletePhotoAsyncTask(photoDAO).execute(photo); }
        void findPhoto(String subjectName){
            QueryPhotoAsyncTask task = new QueryPhotoAsyncTask(photoDAO);
            task.delegate = this;
            task.execute(subjectName);
    }
//    public void getPhotoById(String id){
//        GetPhotoAsyncTask task = new GetPhotoAsyncTask(photoDAO);
//        task.delegate = this;
//        task.execute(id);
//    }

    private void asyncFinished(List<Photo> result)
    {
        searchResults.setValue(result);
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

    private static class QueryPhotoAsyncTask extends AsyncTask<String, Void, List<Photo>> {
        private PhotoDAO photoDAO;
        private PhotoRepository delegate = null;
        private QueryPhotoAsyncTask(PhotoDAO photoDAO){
            this.photoDAO = photoDAO;
        }

        @Override
        protected List<Photo> doInBackground(String... subjectName){
            return photoDAO.findPhoto(subjectName[0]);
        }

        @Override protected void onPostExecute(List<Photo> result)
        {
            delegate.asyncFinished(result);
        }
    }

//    private static class GetPhotoAsyncTask extends AsyncTask<String, Void, List<Photo>> {
//        private PhotoDAO photoDAO;
//        private PhotoRepository delegate = null;
//        private GetPhotoAsyncTask(PhotoDAO photoDAO){
//            this.photoDAO = photoDAO;
//        }
//
//        @Override
//        protected List<Photo> doInBackground(String... id){
//            return photoDAO.getPhotoById(id[0]);
//        }
//
//        @Override protected void onPostExecute(List<Photo> result)
//        {
//            delegate.asyncFinished(result);
//        }
//    }
}