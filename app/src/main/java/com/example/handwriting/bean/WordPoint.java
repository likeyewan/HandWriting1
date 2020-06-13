package com.example.handwriting.bean;

public class WordPoint {
    public float x;
    public float y;
    public float width;
    public WordPoint() {
    }
    public WordPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void set(float x, float y, float w) {
        this.x = x;
        this.y = y;
        this.width = w;
    }
    public void set(WordPoint wordPoint) {
        this.x = wordPoint.x;
        this.y = wordPoint.y;
        this.width = wordPoint.width;
    }
    public String toString() {
        String str = "X = " + x + ", Y = " + y + ", W = " + width+";";
        return str;
    }
}
