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
        void update(Photo photo)
        {
            new UpdatePhotoAsyncTask(photoDAO).execute(photo);
        }
        void delete(Photo photo) { new DeletePhotoAsyncTask(photoDAO).execute(photo); }
        void deleteAllPhotos()
        {
            new DeleteAllPhotosAsyncTask(photoDAO).execute();
        }
        void findPhoto(String subjectName){
            FindPhotoAsyncTask task = new FindPhotoAsyncTask(photoDAO);
            task.delegate = this;
            task.execute(subjectName);
    }
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

        private static class UpdatePhotoAsyncTask extends AsyncTask<Photo, Void, Void> {
            private PhotoDAO photoDAO;
            private UpdatePhotoAsyncTask(PhotoDAO photoDAO){
                this.photoDAO = photoDAO;
            }

            @Override
            protected Void doInBackground(Photo... photos){
                photoDAO.update(photos[0]);
                return null;
            }
        }
        private static class DeleteAllPhotosAsyncTask extends AsyncTask<Void, Void, Void> {
            private PhotoDAO photoDAO;
            private DeleteAllPhotosAsyncTask(PhotoDAO photoDAO){
                this.photoDAO = photoDAO;
            }

            @Override
            protected Void doInBackground(Void... voids){
                photoDAO.deleteAllPhotos();
                return null;
            }
        }

    private static class FindPhotoAsyncTask extends AsyncTask<String, Void, List<Photo>> {
        private PhotoDAO photoDAO;
        private PhotoRepository delegate = null;
        private FindPhotoAsyncTask(PhotoDAO photoDAO){
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
}