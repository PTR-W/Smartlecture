package de.SmartLecture.application.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "subject_table")
public class Subject {

    @PrimaryKey(autoGenerate =  true)
    private int id;
    private String title;
    private String startDate;
    private String endDate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Subject(String title, String startDate, String endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setId(int id) {
        this.id = id;
    }
}
