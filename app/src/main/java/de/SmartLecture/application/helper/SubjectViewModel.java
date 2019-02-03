package de.SmartLecture.application.helper;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {

    private SubjectRepository repository;
    private LiveData<List<Subject>> allSubjects;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        repository = new SubjectRepository(application);
        allSubjects = repository.getAllSubjects();
    }

    public void insert(Subject subject)
    {
        repository.insert(subject);
    }

    public void update(Subject subject)
    {
        repository.update(subject);
    }
    public void delete(Subject subject)
    {
        repository.delete(subject);
    }
    public void deleteAllSubjects()
    {
        repository.deleteAllSubjects();
    }
    public LiveData<List<Subject>> getAllSubjects()
    {
        return allSubjects;
    }


}
