package com.example.handwriting.db;

import org.litepal.crud.DataSupport;

import java.util.List;

public class HanZi extends DataSupport {
    private int id;
    private String name;
    private String py;
    private String bishun;
    private int bhs;
    private int bushou;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getBishun() {
        return bishun;
    }

    public void setBishun(String bishun) {
        this.bishun = bishun;
    }

    public int getBhs() {
        return bhs;
    }

    public void setBhs(int bhs) {
        this.bhs = bhs;
    }

    public int getBushou() {
        return bushou;
    }

    public void setBushou(int bushou) {
        this.bushou = bushou;
    }

    @Override
    public String toString() {
        return "HanZi{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", py='" + py + '\'' +
                ", bishun='" + bishun + '\'' +
                ", bhs=" + bhs +
                ", bushou=" + bushou +
                '}';
    }
}
