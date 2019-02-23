package de.SmartLecture.application.helper;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import de.SmartLecture.application.DAO.PhotoDAO;
import de.SmartLecture.application.DAO.SubjectDAO;


@Database(entities = {Subject.class, Photo.class}, version = 1)
public abstract class SubjectDatabase extends RoomDatabase {

    private static SubjectDatabase instance;

    public abstract SubjectDAO subjectDAO();
    public abstract PhotoDAO photoDAO();

    public static  synchronized SubjectDatabase getInstance(Context context){
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SubjectDatabase.class, "subject_database")
                    .fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private SubjectDAO subjectDAO;
        private PhotoDAO photoDAO;
        private PopulateDbAsyncTask(SubjectDatabase db)
        {
            subjectDAO = db.subjectDAO();
            photoDAO = db.photoDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subjectDAO.insert(new Subject("IT-Recht", "Mon 08:00", "Mon 10:00"));
            subjectDAO.insert(new Subject("Algorithmen", "Mon 10:00", "Mon 14:00"));
            subjectDAO.insert(new Subject("Java", "Wed 10:00", "Wed 12:00"));
            photoDAO.insert(new Photo(
                    "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)/SmartLecture/Sat/IMG_20190223_143509_8932319127060920295.jpg"
            ,"abc"));
            return null;
        }
    }
}
