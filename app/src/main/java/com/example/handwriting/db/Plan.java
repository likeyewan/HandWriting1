package com.example.handwriting.db;

import org.litepal.crud.DataSupport;

public class Plan extends DataSupport {
    private int id;
    private String name;
    private int level;//hsk等级
    private int learn;//已学习汉字个数
    private int dayPlan;//每日计划学习汉字个数
    private int cc;//是否选中
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
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getLearn() {
        return learn;
    }
    public void setLearn(int learn) {
        this.learn = learn;
    }
    public int getDayPlan() {
        return dayPlan;
    }
    public void setDayPlan(int dayPlan) {
        this.dayPlan = dayPlan;
    }
    public int getCc() {
        return cc;
    }
    public void setCc(int cc) {
        this.cc = cc;
    }
    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", learn=" + learn +
                ", dayPlan=" + dayPlan +
                ", cc=" + cc +
                '}';
    }
}
