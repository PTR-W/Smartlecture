package de.SmartLecture.domain.Lecture;

import java.util.Date;

import de.SmartLecture.domain.helper.Weekday;

public class Lecture {
    private String name;
    private Weekday weekday;
    private Date timefrom;
    private Date timeto;

    public Lecture(String name, Weekday weekday, Date timefrom, Date timeto) {
        this.name = name;
        this.weekday = weekday;
        this.timefrom = timefrom;
        this.timeto = timeto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }


    public Date getTimefrom() {
        return timefrom;
    }

    public void setTimefrom(Date timefrom) {
        this.timefrom = timefrom;
    }

    public Date getTimeto() {
        return timeto;
    }

    public void setTimeto(Date timeto) {
        this.timeto = timeto;
    }


}
