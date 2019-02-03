package de.SmartLecture.application.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "subject_table")
public class Subject {

    @PrimaryKey(autoGenerate =  true)
    private int id;
    private String title;
    private String dateStart;
    private String dateEnd;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public Subject(String title, String dateStart, String dateEnd) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public void setId(int id) {
        this.id = id;
    }
}
