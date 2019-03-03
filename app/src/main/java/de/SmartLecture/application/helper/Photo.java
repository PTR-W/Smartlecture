package de.SmartLecture.application.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "photo_table")
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fileName;
    private String subject;

    public Photo(String fileName, String subject) {
        this.fileName = fileName;
        this.subject = subject;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSubject() {
        return subject;
    }
}
