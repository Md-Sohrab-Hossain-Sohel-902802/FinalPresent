package com.example.finalpresent.DataHandeler;

import com.google.firebase.database.Exclude;

public class ChepterLIst {

    String Lesson;

    private  String key;


    public ChepterLIst(String lesson) {
        Lesson = lesson;
    }
    public ChepterLIst(){

    }


    public String getLesson() {
        return Lesson;
    }

    public void setLesson(String lesson) {
        Lesson = lesson;
    }


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }



}
