package edu.westga.jeffrichardsrollcall.model;

/**
 * Attendance consists of name number of classes recorded and attendance percentage
 * Created by Jeff on 4/24/2016.
 */
public class Attendance {
    private String name;
    private int numClasses;
    private int attendencePercent;

    public Attendance(String name, int numClasses, int attendencePercent) {
        this.name = name;
        this.numClasses = numClasses;
        this.attendencePercent = attendencePercent;
    }

    public String getName() {
        return name;
    }

    public int getNumClasses() {
        return numClasses;
    }

    public int getAttendencePercent() {
        return attendencePercent;
    }
}
