package com.example.apitest;

import java.util.ArrayList;

public class QuestionList {

    private ArrayList<Question> qList;

    public QuestionList(){

    }

    public void add(Question q){
        qList.add(q);
    }

    public void clear(){
        qList.clear();
    }

    public ArrayList<Question> getqList(){
        return qList;
    }

}
