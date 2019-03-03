package de.SmartLecture.application.helper;

import java.util.List;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.app.Application;
import android.arch.lifecycle.LiveData;

import de.SmartLecture.application.DAO.SubjectDAO;

class SubjectRepository {
    private SubjectDAO subjectDAO;
    private LiveData<List<Subject>> allSubjects;
    private MutableLiveData<List<Subject>> searchResults = new MutableLiveData<>();

    SubjectRepository(Application application) {
        SubjectDatabase database = SubjectDatabase.getInstance(application);
        subjectDAO = database.subjectDAO();
        allSubjects = subjectDAO.getAllSubjects();
    }

    void insert(Subject subject)
    {
        new InsertSubjectAsyncTask(subjectDAO).execute(subject);
    }
    void update(Subject subject)
    {
        new UpdateSubjectAsyncTask(subjectDAO).execute(subject);
    }
    void delete(Subject subject)
    {
        new DeleteSubjectAsyncTask(subjectDAO).execute(subject);
    }
    void deleteAllSubjects()
    {
        new DeleteAllSubjectsAsyncTask(subjectDAO).execute();
    }
    LiveData<List<Subject>> getAllSubjects()
    {
        return allSubjects;
    }
    MutableLiveData<List<Subject>> getSearchResults(){return searchResults;}

    void findSubject(String day, String time){
        QuerySubjectAsyncTask task = new QuerySubjectAsyncTask(subjectDAO);
        task.delegate = this;
        task.execute(day,time);
    }

    private void asyncFinished(List<Subject> result)
    {
        searchResults.setValue(result);
    }

    private static class QuerySubjectAsyncTask extends AsyncTask<String, Void, List<Subject>> {
        private SubjectDAO subjectDAO;
        private SubjectRepository delegate = null;
        private QuerySubjectAsyncTask(SubjectDAO subjectDAO){
            this.subjectDAO = subjectDAO;
        }

        @Override
        protected List<Subject> doInBackground(String... params){
            return subjectDAO.findSubject(params[0], params[1]);
        }

        @Override protected void onPostExecute(List<Subject> result)
        {
            delegate.asyncFinished(result);
        }
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
