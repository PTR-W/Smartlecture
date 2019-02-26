package de.SmartLecture.application.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import de.SmartLecture.application.helper.Photo;

@Dao
public interface PhotoDAO {
    @Insert
    void insert(Photo photo);
    @Delete
    void delete(Photo photo);

    @Query("SELECT * FROM photo_table")
    LiveData<List<Photo>> getAllPhotos();

    @Query("SELECT * FROM photo_table WHERE subject = :subjectName")
    LiveData<List<Photo>> findPhoto(String subjectName);
}
