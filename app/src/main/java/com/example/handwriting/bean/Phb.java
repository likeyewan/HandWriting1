package com.example.handwriting.bean;

public class Phb {
    private String name;
    private int score;
    private int day;
    private int zi;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getZi() {
        return zi;
    }

    public void setZi(int zi) {
        this.zi = zi;
    }

    @Override
    public String toString() {
        return "Phb{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", day=" + day +
                ", zi=" + zi +
                '}';
    }
}
