package edu.hitsz.dao;


import java.util.Date;

public class Record {
    private String name;
    private int score;
    private Date date;

    public Record(int score){
        this.name="testUser";
        this.score=score;
        this.date=getCurDate();
    }

    public Record(int score,String name){
        this.name = name;
        this.date = getCurDate();
        this.score = score;
    }

    private static Date getCurDate(){
        Date out=new Date();
        return out;
    }



}
