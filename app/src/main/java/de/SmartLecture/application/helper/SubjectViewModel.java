package de.SmartLecture.application.helper;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {

    private SubjectRepository subjectRepository;
    private LiveData<List<Subject>> allSubjects;
    private MutableLiveData<List<Subject>> searchResults;

    public SubjectViewModel(@NonNull Application application) {
        super(application);
        subjectRepository = new SubjectRepository(application);
        allSubjects = subjectRepository.getAllSubjects();
        searchResults = subjectRepository.getSearchResults();
    }

    public void insert(Subject subject)
    {
        subjectRepository.insert(subject);
    }
    public void update(Subject subject)
    {
        subjectRepository.update(subject);
    }
    public void delete(Subject subject)
    {
        subjectRepository.delete(subject);
    }
    public void deleteAllSubjects()
    {
        subjectRepository.deleteAllSubjects();
    }
    public LiveData<List<Subject>> getAllSubjects()
    {
        return allSubjects;
    }
    public MutableLiveData<List<Subject>> getSearchResults(){ return searchResults; }
    public void findSubject(String day, String time){subjectRepository.findSubject(day, time);}


}
