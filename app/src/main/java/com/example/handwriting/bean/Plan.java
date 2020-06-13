package com.example.handwriting.bean;

import java.util.Date;

public class Plan {
    private int id;
    private int level;//hsk等级
    private int learn;//已学习汉字个数
    private int dayPlan;//每日计划学习汉字个数
    private Date startTime;//计划开始时间
    private Date endTime;//计划结束时间
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
