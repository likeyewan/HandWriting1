package com.example.handwriting.bean;

import java.util.List;

public class Tie {
    private int id;
    private String name;
    private String userName;
    private String pp;
    private String tieDate;
    private int zanNum;
    private String zanName;
    private String pic;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getZanNum() {
        return zanNum;
    }

    public void setZanNum(int zanNum) {
        this.zanNum = zanNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getTieDate() {
        return tieDate;
    }

    public void setTieDate(String tieDate) {
        this.tieDate = tieDate;
    }

    public String getZanName() {
        return zanName;
    }

    public void setZanName(String zanName) {
        this.zanName = zanName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Tie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", pp='" + pp + '\'' +
                ", tieDate='" + tieDate + '\'' +
                ", zanNum=" + zanNum +
                ", zanName='" + zanName + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
