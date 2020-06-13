package com.example.handwriting.db;

import org.litepal.crud.DataSupport;
public class Learn extends DataSupport {
    private int id;
    private int level;//hsk等级
    private int xue;//一天学的
    private int stu;//计划学的
    private int s_p;//学了多少次
    private String name;//用户标识
    private String day;//日期

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getXue() {
        return xue;
    }
    public void setXue(int xue) {
        this.xue = xue;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getStu() {
        return stu;
    }
    public void setStu(int stu) {
        this.stu = stu;
    }
    public int getS_p() {
        return s_p;
    }
    public void setS_p(int s_p) {
        this.s_p = s_p;
    }
    @Override
    public String toString() {
        return "Learn{" +
                "id=" + id +
                ", xue=" + xue +
                ", stu=" + stu +
                ", s_p=" + s_p +
                ", name='" + name + '\'' +
                ", day='" + day + '\'' +
                '}';
    }
}
