package com.link.cloud.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.link.cloud.view.CardConfig;
import com.link.cloud.view.SignCardCallBack;
import com.link.cloud.view.SwipeCardLayoutManager;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by 49488 on 2018/7/23.
 */

public class BindPagerViewAdapter extends PagerAdapter implements View.OnClickListener{
    String [] keys = new String []{"1","2","3","4","5","6","7","8","9","清空","0","回删"};
    ArrayList<TextView> textViews = new ArrayList<>();
    private final StringBuilder builder;
    BindViewListener listener;
    private TextView info_model;

    @Override
    public int getCount() {
        return 4;
    }
    Activity context;
    public BindPagerViewAdapter(Activity context,BindViewListener listener){
        this.context=context;
        for(int x=0;x<keys.length;x++){
            TextView view = new TextView(context);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(context.getResources().getColor(R.color.white));
            view.setTextSize(40);
            view.setText(keys[x]);
            textViews.add(view);
        }
        this.listener =listener;
        builder = new StringBuilder();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=null;
        if(position==0){
             view = View.inflate(context, R.layout.keybord,null);
             GridView gridView  = (GridView) view.findViewById(R.id.key_bord);
             final EditText editPhoneNum  = (EditText) view.findViewById(R.id.edit_phone_num);
            TextView back  = (TextView) view.findViewById(R.id.bind_back);
            TextView next  = (TextView) view.findViewById(R.id.bind_next);
             gridView.setAdapter(new GridViewAdapter());
             back.setOnClickListener(this);
             next.setOnClickListener(this);
             editPhoneNum.setInputType(InputType.TYPE_NULL);
             gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                     if(position==9){
                         editPhoneNum.setText("");
                         if(builder.length()>0){
                             builder.delete(0,builder.length());
                         }
                     }else if(position==11){
                         if(builder.length()>0){
                             builder.deleteCharAt(builder.length()-1);
                         }
                         editPhoneNum.setText(builder.toString());
                     }else{
                         if(builder.length()==11){
                             return;
                         }
                         builder.append(keys[position]);
                         editPhoneNum.setText(builder.toString());
                     }
                     textViews.get(position).setTextColor(context.getResources().getColor(R.color.weather));
                     handler.sendEmptyMessageDelayed(position,300);
                 }
             });

        }
        if(position==1){
            view=View.inflate(context,R.layout.sign_introduce_item,null);
            info_model = (TextView) view.findViewById(R.id.info_model);
        }
        if(position==2){
            view = View.inflate(context, R.layout.card_container,null);
            SwipeCardLayoutManager swmanamger = new SwipeCardLayoutManager(context);
            RecyclerView viewById = (RecyclerView) view.findViewById(R.id.bind_review);
            viewById.setLayoutManager(swmanamger);
            SignCardAdapter mAdatper = new SignCardAdapter(context);
            mAdatper.setPostionListener(new SignCardAdapter.PositionChangedLister() {

                @Override
                public void postionChanged(String postion, String id, String LessonStart, String EmName, String LesName, String State, String RestCount,String reservationNumber) {
//                ReservationID=postion;
//                chooseCoachID=id;
//                LessonTime=LessonStart;CoachName=EmName;
//                LessonName=LesName;
//                status=State;
//                Resttime=RestCount;
                }
            });
            viewById.setAdapter(mAdatper);
            CardConfig.initConfig(context);
            ItemTouchHelper.Callback callback=new SignCardCallBack(viewById,mAdatper);
            ItemTouchHelper helper=new ItemTouchHelper(callback);
            helper.attachToRecyclerView(viewById);
        }
        if(position==3){
            view = View.inflate(context, R.layout.sign_success,null);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bind_back:
                context.finish();
                break;
                case R.id.bind_next:
                listener.next(builder.toString());
                if(builder.length()>0){
                    builder.delete(0,builder.length());
                }

                break;
        }
    }
    public interface BindViewListener {
        void next(String phone);
    }
    private class GridViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return keys.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return textViews.get(position);
        }
    }
    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int messageId =msg.what;
            handler.removeMessages(messageId);
            textViews.get(messageId).setTextColor(context.getResources().getColor(R.color.white));
        }
    };
    public void setModelMsg(String msg){
        info_model.setText(msg);
    }
}
