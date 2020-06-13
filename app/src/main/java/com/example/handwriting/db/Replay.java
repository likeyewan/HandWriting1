package com.example.handwriting.db;

import org.litepal.crud.DataSupport;

public class Replay extends DataSupport {
    private int id;
    private String userPhone;
    private String name;
    private String replay_msg;
    private int score;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getReplay_msg() {
        return replay_msg;
    }
    public void setReplay_msg(String replay_msg) {
        this.replay_msg = replay_msg;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
