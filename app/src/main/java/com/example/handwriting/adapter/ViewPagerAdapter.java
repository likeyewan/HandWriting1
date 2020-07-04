package com.example.handwriting.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.handwriting.bean.FragmentInfo;
import com.example.handwriting.fragment.DayFragment;
import com.example.handwriting.fragment.Fragment3;
import com.example.handwriting.fragment.ZiFragment;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/7/4 11:38.
 **/
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<FragmentInfo> mFragments=new ArrayList<>(3);
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        initFragments();
    }

    private void initFragments(){
        mFragments.add(new FragmentInfo("分数榜", Fragment3.class));
        mFragments.add(new FragmentInfo("天数榜", DayFragment.class));
        mFragments.add(new FragmentInfo("字数榜", ZiFragment.class));
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        try {
            return (Fragment)mFragments.get(position).getFragment().newInstance();
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }
}
