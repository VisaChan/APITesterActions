package com.example.apitest;

import java.util.ArrayList;

public class Question {

    private String question;
    private String correct;
    private ArrayList<String> answers = new ArrayList<String>();

    Question(String q, String c, ArrayList<String> a){
        this.question = q;
        this.correct = c;
        this.answers = a;
        if(q.contains("&#039;s")){
            String change = this.question.replace("&#039;s", "\'");
            this.question = change;
        }
        if(q.contains("&quot;")){
            String change = this.question.replace("&quot;", "\"");
            this.question = change;
        }

    }

    public String getQuestion(){
        return question;
    }

    public String getCorrect(){
        return correct;
    }

    public ArrayList<String> getAnswers(){
        return answers;
    }

}
