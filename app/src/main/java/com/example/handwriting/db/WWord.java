package com.example.handwriting.db;

import org.litepal.crud.DataSupport;

public class WWord extends DataSupport {
    private int id;
    private String userPhone;
    private String wrong_word;

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

    public String getWrong_word() {
        return wrong_word;
    }

    public void setWrong_word(String wrong_word) {
        this.wrong_word = wrong_word;
    }
}
