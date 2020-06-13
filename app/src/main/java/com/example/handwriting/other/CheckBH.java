package com.example.handwriting.other;


//import android.util.Log;
import com.example.handwriting.bean.WordPoint;
import java.util.ArrayList;
import java.util.List;

public class CheckBH {
    private List<Integer> bh = new ArrayList();
    private List<Integer> bb=new ArrayList<>();
    private int bihua = 1;
    private List<WordPoint> list1 = new ArrayList();
    private List<Integer> lky = new ArrayList();
    //求夹角
    public static double Angle(WordPoint cen, WordPoint first, WordPoint second)
    {
        double dx1, dx2, dy1, dy2;
        double angle;
        dx1 = first.x - cen.x;
        dy1 = first.y - cen.y;
        dx2 = second.x - cen.x;
        dy2 = second.y - cen.y;
        if(Math.sqrt(dx1 * dx1 + dy1 * dy1)>20&&Math.sqrt(dx2 * dx2 + dy2 * dy2)>20) {
            double c = Math.sqrt(dx1 * dx1 + dy1 * dy1) * Math.sqrt(dx2 * dx2 + dy2 * dy2);
            if (c == 0) return -1;
            angle = Math.acos((dx1 * dx2 + dy1 * dy2) / c) * 180 / Math.PI;
            if (angle > 90) {
                angle = 180 - angle;
            }
            return angle;
        }
        return  -1;
    }
    public int cb(List<WordPoint> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            list1.add(list.get(i));
            lky.add(Integer.valueOf(cb1(list1)));
           // Log.d("vvs","lky="+lky.get(i));
            //if(lky.size()>2) {
               // if (cb1(list1) != lky.get(lky.size() - 2)&&cb1(list1)!=6&&lky.get(lky.size() - 2)!=6)
                 //   lky.add(0);
           // }
            if ((lky.get(i) == 0||i==list.size()-1)&&i>1)
            {

                if (((lky.get(i - 1)) != 0) && ((lky.get(i - 1)) != -1)) {
                    bb.add(lky.get(i - 1));
                   // Log.d("vvs","bi="+lky.get(i - 1));
                } else {
                    bihua = 0;
                }
                list1.clear();
                list1.add(list.get(i));
            }
        }
        for(int i=0;i<bb.size();i++){
            if((bb.get(i)==6||bb.get(i)==61||bb.get(i)==62||bb.get(i)==63)&&i!=0&&i!=bb.size()-1){
            }else
                bh.add(bb.get(i));
            if(bh.size()>1&&bh.get(0)==6){
                bh.remove(0);
            }
        }
       // Log.d("vvs","bi="+bb);
       // Log.d("vvs","bb"+bh);
        bihua = 0;
        //一折笔
        if (bh.size() == 1) {
            bihua = bh.get(0);
        }
        //二折笔
        if (bh.size() == 2)
        {
            if (bh.get(0) == 1)
            {
                //横折
                if (bh.get(1) == 2) {
                    bihua = 7;
                }
                //横撇
                if (bh.get(1) == 3) {
                    bihua = 8;
                }
                //横钩
                if (bh.get(1) == 61) {
                    bihua = 9;
                }
            }
            if (bh.get(0) == 2)
            {
                //竖折
                if (bh.get(1) == 1) {
                    bihua = 10;
                }
                //竖提
                if (bh.get(1) == 5) {
                    bihua = 11;
                }
                //竖钩
                if (bh.get(1) == 62||bh.get(1) == 63||bh.get(1)==6) {
                    bihua = 12;
                }
            }
            if (bh.get(0) == 3)
            {
                //撇折
                if (bh.get(1) == 1) {
                    bihua = 14;
                }
                //撇点
                if (bh.get(1) == 4) {
                    bihua = 15;
                }
            }
            //斜钩
            if (bh.get(0) == 4){
                if(bh.get(1)==63)
                    bihua=17;
                if(bh.get(1)==62){
                    bihua=16;
                }
            }
        }
        //三折笔
        if (bh.size() == 3)
        {
            //竖弯
            if (bh.get(1) == 4&&bh.get(2)==1) {
                bihua = 13;
            }
            //弯钩
            if(bh.get(0)==4&&bh.get(1)==3&&bh.get(2)==62){
                bihua=18;
            }
            //横折
            if (bh.get(0) == 1 && bh.get(1) == 2)
            {
                //横折折
                if ((bh.get(2)) == 1) {
                    bihua = 19;
                }
                //横折提
                if ((bh.get(2)) == 5) {
                    bihua = 20;
                }

            }
            //横折钩
            if (bh.get(0) == 1 && (bh.get(1) == 2||bh.get(1)==3)&&bh.get(2) == 62) {
                bihua = 21;
            }
            //竖折
            if (((bh.get(0)) == 2) && ((bh.get(1)) == 1))
            {
                //竖折折
                if ((bh.get(2)) == 2) {
                    bihua = 23;
                }
                //竖折撇
                if ((bh.get(2)) == 3) {
                    bihua = 24;
                }
            }
            //竖弯钩
            if ((bh.get(0)==2||bh.get(0)==4)&&(bh.get(1) == 4||bh.get(1) == 1)&&(bh.get(2)==6||bh.get(2)==62||bh.get(2)==63||bh.get(2)==5)) {
                bihua = 25;
            }
            //横斜钩
            if(bh.get(0)==1&&bh.get(1)==4&&(bh.get(2)==6||bh.get(2)==62||bh.get(2)==63)){
                bihua=26;
            }
        }
        if(bh.size()==2&&bh.get(0)==4&&(bh.get(1)==6||bh.get(1)==62||bh.get(1)==63||bh.get(1)==5))
            bihua=25;
        //四折笔
        if (bh.size() == 4)
        {
            //横斜钩
            if(bh.get(0)==1&&bh.get(1)==2&&bh.get(2)==4&&(bh.get(3)==6||bh.get(3)==62||bh.get(3)==63||bh.get(3)==5)){
                bihua=26;
            }
            //横折折
            if (bh.get(0) == 1 && (bh.get(1) == 3||bh.get(1) == 2) && bh.get(2) == 1)
            {
                //横折折折
                if (bh.get(3) == 2||bh.get(3) == 3) {
                    bihua = 27;
                }
                //横折折撇
                if (bh.get(3) == 3) {
                    bihua = 28;
                }
            }
            //竖折折钩
            if ((bh.get(0) == 3||bh.get(0) == 2) && ((bh.get(1)) == 1) && (bh.get(2) == 3||bh.get(2) == 2) && ((bh.get(3)) == 62)) {
                bihua = 31;
            }
        }
        if(bh.size() == 4&&bh.get(0)==1&&(bh.get(1)==2||bh.get(1)==4)&&(bh.get(2) == 4||bh.get(2) == 1)&&(bh.get(3)==6||bh.get(3)==62||bh.get(3)==63||bh.get(3)==5)){
            bihua=29;
    }
        //横撇弯钩
        if(bh.size() == 4 &&bh.get(0) == 1&&bh.get(1) == 3&&bh.get(2)==4&&(bh.get(3)==6||bh.get(3)==62||bh.get(3)==63)){
            bihua=30;
        }
        //五折笔
        //横折折折钩
        if (bh.size() == 5 && bh.get(0) == 1 && bh.get(1) == 3&& bh.get(2) == 1 && (bh.get(3) == 3||bh.get(3) == 2) && bh.get(4) == 62) {
            bihua = 32;
        }
        list1.clear();
        lky.clear();
        bh.clear();
        bb.clear();
        return bihua;
    }
    //基本笔画的判断
    public int cb1(List<WordPoint> list)
    {
        //点的个数
        int size= list.size();
        int psize = size / 2;
        //xy的递增或递减
        int xasc= 0,xdes=0,yasc=0,ydes=0;
       //起始点xy坐标
        float firstY = list.get(0).y;
        float firstX = list.get(0).x;
        float LastY = list.get(size-1).y;
        float LastX = list.get(size-1).x;
        //xy最值
        float xMax=firstX,xMin=firstX,yMin=firstY,yMax=firstY;
        //xy最值点
        int xMax_dex=0,xMin_dex=0,yMin_dex=0,yMax_dex=0;
        //求xy最值
        for(int i=1;i<list.size();i++)
        {
            if ((list.get(i)).y < yMin)
            {
                yMin = (list.get(i)).y;
                yMin_dex=i;
            }
            if ((list.get(i)).y > yMax)
            {
                yMax= (list.get(i)).y;
                yMax_dex = i;
            }
            if ((list.get(i)).x < xMin)
            {
                xMin= (list.get(i)).x;
                xMin_dex = i;
            }
            if ((list.get(i)).x > xMax)
            {
                xMax = (list.get(i)).x;
                xMax_dex= i;
            }
        }
        //x单调递增
        for(int i=1;i< list.size();i++){
            if(list.get(i).x>=list.get(i-1).x){
                xasc=1;
            }else {
                xasc = 0;
                break;
            }
        }
        //y单调递增
        for(int i=1;i< list.size();i++){
            if(list.get(i).y>=list.get(i-1).y){
                yasc=1;
            }else {
                yasc = 0;
                break;
            }
        }
        //x单调递减
        for(int i=1;i< list.size();i++){
            if(list.get(i).x<=list.get(i-1).x){
                xdes=1;
            }else {
                xdes = 0;
                break;
            }
        }
        //y单调递减
        for(int i=1;i< list.size();i++){
            if(list.get(i).y<=list.get(i-1).y){
                ydes=1;
            }else {
                ydes = 0;
                break;
            }
        }
        //笔画最大的角度
        double maxAngle=0;
        for(int i=1;i<size;i++){
            for(int j=0;j<i;j++){
                for(int k=size-1;k>i;k--){
                    double index=Angle(list.get(i),list.get(j),list.get(k));
                    if(index>maxAngle)
                        maxAngle=index;
                }
            }
        }
        //最大值
        float xMaxX = list.get(xMax_dex).x - list.get(xMin_dex).x;
        float xMaxY = list.get(xMax_dex).y - list.get(xMin_dex).y;
        float yMaxY = list.get(yMax_dex).y - list.get(yMin_dex).y;
        float yMaxX = list.get(yMax_dex).x - list.get(yMin_dex).x;
        float xD=list.get(size-1).x-list.get(0).x;
        float yD=list.get(size-1).y-list.get(0).y;
        //
        double yMaxA = Math.atan2(Math.abs(yMaxY), Math.abs(yMaxX))* 180 /Math.PI;
        double xMaxA = Math.atan2(Math.abs(xMaxY), Math.abs(xMaxX))* 180 /Math.PI;
        double A = Math.atan2(Math.abs(yD), Math.abs(xD))* 180 /Math.PI;
        //Log.d("an","AN="+maxAngle);
        if (Math.abs(LastX - firstX) < 1 && Math.abs(LastY - firstY )< 1) {
            return -1;
        }
        //横钩的钩
        if (Math.abs(LastX - firstX) < 100 && Math.abs(LastY - firstY )< 100&&xdes == 1 && yasc == 1 && A> 15) {
            return 61;
        }
        //竖钩的钩
        if (Math.abs(LastX - firstX) < 100 && Math.abs(LastY - firstY )< 100&&xdes == 1 && ydes == 1 && A> 15) {
            return 62;
        }
        //斜钩的钩
        if (Math.abs(LastX - firstX) < 100 && Math.abs(LastY - firstY )< 100&&xasc == 1 && ydes == 1 && A> 15) {
            return 63;
        }
        //点
        if (Math.abs(LastX - firstX) < 100 && Math.abs(LastY - firstY )< 100) {
            return 6;
        }
        //卧钩的钩

        //横
        if (xasc == 1 && yMaxA < 20&&maxAngle<20) {
            return 1;
        }
        //竖
        if (yasc == 1 && xMaxA > 80&&maxAngle<25||xMax==0) {
            return 2;
        }
        //撇
        if (xdes == 1 && yasc == 1 && A> 15&&maxAngle<40) {
                return 3;
        }
        //捺
        if ( xasc== 1  && yasc == 1&& A > 25&&maxAngle<35) {
            return 4;
        }
        //提
        if (xasc == 1 && ydes == 1 && A > 25) {
            return 5;
        }
        return 0;
    }
}
