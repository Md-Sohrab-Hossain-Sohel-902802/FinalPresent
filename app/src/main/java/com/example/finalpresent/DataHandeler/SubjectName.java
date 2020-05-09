package com.example.finalpresent.DataHandeler;

import com.google.firebase.database.Exclude;

public class SubjectName {
    String subjectname;

    public SubjectName(String subjectname) {
        this.subjectname = subjectname;
    }
    public SubjectName(){

    }
    private  String key;

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }



    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }
}
