package com.example.handwriting.bean;

import java.util.List;

public class WordZ {
    private int id;
    private String name;
    private String py;
    private List<Integer> bishun;
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

    public List<Integer> getBishun() {
        return bishun;
    }

    public void setBishun(List<Integer> bishun) {
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
}
