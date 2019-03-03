package de.SmartLecture.application.helper;

import android.os.AsyncTask;
import android.content.Context;
import android.support.annotation.NonNull;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.db.SupportSQLiteDatabase;

import de.SmartLecture.application.DAO.PhotoDAO;
import de.SmartLecture.application.DAO.SubjectDAO;

@Database(entities = {Subject.class, Photo.class}, version = 1)
public abstract class SubjectDatabase extends RoomDatabase {

    private static SubjectDatabase instance;
    public abstract SubjectDAO subjectDAO();
    public abstract PhotoDAO photoDAO();

    static  synchronized SubjectDatabase getInstance(Context context){
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
        private PopulateDbAsyncTask(SubjectDatabase db)
        {
            subjectDAO = db.subjectDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            subjectDAO.insert(new Subject("Default", "", "", ""));
            return null;
        }
    }
}
