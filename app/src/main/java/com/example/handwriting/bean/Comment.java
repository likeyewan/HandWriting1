package com.example.handwriting.bean;

import java.util.ArrayList;
import java.util.List;

public class Comment {
    private String id;					//评论记录ID
    private int tieID;
    private String commentNickname;	//评论人昵称
    private String commentTime;		//评论时间
    private String commentContent;	//评论内容
    private String paisenum;
    public String getPaisenum() {
        return paisenum;
    }
    public void setPaisenum(String paisenum) {
        this.paisenum = paisenum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTieID() {
        return tieID;
    }

    public void setTieID(int tieID) {
        this.tieID = tieID;
    }

    public String getCommentNickname() {
        return commentNickname;
    }
    public void setCommentNickname(String commentNickname) {
        this.commentNickname = commentNickname;
    }
    public String getCommentTime() {
        return commentTime;
    }
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
    public String getCommentContent() {
        return commentContent;
    }
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }


}
