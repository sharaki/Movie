package com.example.sharaki.movie;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sharaki on 9/22/2016.
 */
public class Db  extends RealmObject{
    public String name;
    public String date;
    public String overview;
    public double rate;
    @PrimaryKey
    public int id;
    public String poster;

    public Db(){

    }

    public Db(String name, String date, String overview, double rate, int id, String poster) {
        this.name = name;
        this.date = date;
        this.overview = overview;
        this.rate = rate;
        this.id = id;
        this.poster = poster;
    }
}
