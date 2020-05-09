package com.example.finalpresent.DataHandeler;

import com.google.firebase.database.Exclude;

public class StudentDetails {
    String name;
    String roll;
    String group;
    String shift;
    String subjectname;
    private  String key;

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public StudentDetails(String name, String roll, String group, String shift,String subjectname) {
        this.name = name;
        this.roll = roll;
        this.group = group;
        this.shift = shift;
        this.subjectname=subjectname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public StudentDetails (){


    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
