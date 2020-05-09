package com.example.finalpresent.DataHandeler;

import com.google.firebase.database.Exclude;

public class ClassName {
    private  String classname;
    private  String subjectName;
    private  String key;

    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
    public ClassName(String classname,String subjectName) {
        this.classname = classname;
        this.subjectName=subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public ClassName(){


    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }
}
