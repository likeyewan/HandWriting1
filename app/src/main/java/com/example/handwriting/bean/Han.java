package com.example.handwriting.bean;


import android.graphics.Point;

import java.util.Date;
import java.util.List;

public class Han {
    private long id;
    private String name;
    private Date time;
    private String bitmap;
    private List<List<WordPoint>> lists;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getBitmap() {
        return bitmap;
    }
    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }
    public List<List<WordPoint>> getLists() {
        return lists;
    }
    public void setLists(List<List<WordPoint>> lists) {
        this.lists = lists;
    }
}
