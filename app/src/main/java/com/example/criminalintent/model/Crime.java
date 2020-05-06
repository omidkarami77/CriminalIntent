package com.example.criminalintent.model;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mtitle;
    private Date mDate;
    private boolean mSolved;

    public Crime(){
       // mId=UUID.randomUUID();
       // mDate=new Date();
       // mDate=randomDate();
   this(UUID.randomUUID());
    }
    public Crime(UUID uuid){
        mId=uuid;
        mDate=new Date();
    }
    public UUID getId() {
        return mId;
    }


    public String getTitle() {
        return mtitle;
    }

    public void setTitle(String mtitle) {
        this.mtitle = mtitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

   private Date randomDate() {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(2000, 2022);
        gc.set(gc.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
        gc.set(gc.DAY_OF_YEAR, dayOfYear);

        return gc.getTime();
    }

    private int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}
