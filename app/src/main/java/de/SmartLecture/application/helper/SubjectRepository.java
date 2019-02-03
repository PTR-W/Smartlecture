package de.SmartLecture.application.helper;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.List;

import de.SmartLecture.application.DAO.SubjectDAO;

public class SubjectRepository {
    private SubjectDAO subjectDAO;
    private LiveData<List<Subject>> allSubjects;

    public SubjectRepository(Application application)
    {
        SubjectDatabase database = SubjectDatabase.getInstance(application);
        subjectDAO = database.subjectDAO();
        allSubjects = subjectDAO.getAllSubjects();
    }

    public void insert(Subject subject)
    {
        new InsertSubjectAsyncTask(subjectDAO).execute(subject);
    }
    public void update(Subject subject)
    {
        new UpdateSubjectAsyncTask(subjectDAO).execute(subject);
    }
    public void delete(Subject subject)
    {
        new DeleteSubjectAsyncTask(subjectDAO).execute(subject);
    }
    public void deleteAllSubjects()
    {
        new DeleteAllSubjectsAsyncTask(subjectDAO).execute();
    }
    public LiveData<List<Subject>> getAllSubjects()
    {
        return allSubjects;
    }

    private static class InsertSubjectAsyncTask extends AsyncTask<Subject, Void , Void>
    {
        private SubjectDAO subjectDAO;
        private InsertSubjectAsyncTask(SubjectDAO subjectDAO)
        {
            this.subjectDAO = subjectDAO;
        }

        @Override
        protected Void doInBackground(Subject... subjects)
        {
            subjectDAO.insert(subjects[0]);
            return null;
        }
    }

    private static class UpdateSubjectAsyncTask extends AsyncTask<Subject, Void , Void>
    {
        private SubjectDAO subjectDAO;
        private UpdateSubjectAsyncTask(SubjectDAO subjectDAO)
        {
            this.subjectDAO = subjectDAO;
        }

        @Override
        protected Void doInBackground(Subject... subjects)
        {
            subjectDAO.update(subjects[0]);
            return null;
        }
    }

    private static class DeleteSubjectAsyncTask extends AsyncTask<Subject, Void , Void>
    {
        private SubjectDAO subjectDAO;
        private DeleteSubjectAsyncTask(SubjectDAO subjectDAO)
        {
            this.subjectDAO = subjectDAO;
        }

        @Override
        protected Void doInBackground(Subject... subjects)
        {
            subjectDAO.delete(subjects[0]);
            return null;
        }
    }

    private static class DeleteAllSubjectsAsyncTask extends AsyncTask<Void, Void , Void>
    {
        private SubjectDAO subjectDAO;
        private DeleteAllSubjectsAsyncTask(SubjectDAO subjectDAO)
        {
            this.subjectDAO = subjectDAO;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            subjectDAO.deleteAllSubjects();
            return null;
        }
    }
}
