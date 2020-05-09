package com.example.finalpresent.DataHandeler;

import com.google.firebase.database.Exclude;

public class ChepterValue {
    String  partnumber;
    String question;
    String answer;
    String image;
    private  String key;

    public ChepterValue(String partnumber, String question, String answer, String image) {
        this.partnumber = partnumber;
        this.question = question;
        this.answer = answer;
        this.image = image;
    }

    public  ChepterValue(){

    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPartnumber() {
        return partnumber;
    }

    public void setPartnumber(String partnumber) {
        this.partnumber = partnumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
