package com.example.finalpresent.DataHandeler;

import com.google.firebase.database.Exclude;

public class PresentDetails {

    String roll;
    String currenttime;
    String currentDate;
    String attendence;
    private  String key;

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
 public PresentDetails(){

    }

    public PresentDetails(String roll, String currenttime, String currentDate, String attendence) {
        this.roll = roll;
        this.currenttime = currenttime;
        this.currentDate = currentDate;
        this.attendence = attendence;
    }


    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getAttendence() {
        return attendence;
    }

    public void setAttendence(String attendence) {
        this.attendence = attendence;
    }
}
