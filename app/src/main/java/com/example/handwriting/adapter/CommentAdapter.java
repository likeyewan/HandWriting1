package com.example.handwriting.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.handwriting.R;
import com.example.handwriting.bean.Comment;

import java.util.List;


public class CommentAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private Handler handler;
    private List<Comment> list;
    private LayoutInflater inflater;
    private ViewHolder mholder = null;
    public CommentAdapter(Context context, List<Comment> list
            , int resourceId, Handler handler) {
        this.list = list;
        this.context = context;
        this.handler = handler;
        this.resourceId = resourceId;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Comment bean = list.get(position);
//		ViewHolder mholder = null;
        if (convertView == null) {
            mholder = new ViewHolder();
            convertView = inflater.inflate(resourceId, null);
            mholder.commentNickname = (TextView)
                    convertView.findViewById(R.id.comment_name);
            mholder.commentItemTime = (TextView)
                    convertView.findViewById(R.id.comment_time);
            mholder.commentItemContent = (TextView)
                    convertView.findViewById(R.id.comment_content);
            convertView.setTag(mholder);
        } else {
            mholder = (ViewHolder) convertView.getTag();
        }
        mholder.commentNickname.setText(bean.getCommentNickname());
        mholder.commentItemTime.setText(bean.getCommentTime());
        mholder.commentItemContent.setText(bean.getCommentContent());
        return convertView;
    }

    private final class ViewHolder {
        public TextView commentNickname;            //评论人昵称
        public TextView commentItemTime;            //评论时间
        public TextView commentItemContent;         //评论内容
    }

}
