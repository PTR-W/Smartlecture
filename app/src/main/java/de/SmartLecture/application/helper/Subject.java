package de.SmartLecture.application.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "subject_table")
public class Subject {

    @PrimaryKey(autoGenerate =  true)
    private int id;
    private String title;
    private String day;
    private String dateStart;
    private String dateEnd;

    public Subject(String title, String day, String dateStart, String dateEnd) {
        this.title = title;
        this.day = day;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDay() {
        return day;
    }
    public String getDateStart() {
        return dateStart;
    }
    public String getDateEnd() {
        return dateEnd;
    }
    public void setId(int id) {
        this.id = id;
    }
}
