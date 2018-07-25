package com.link.cloud.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.link.cloud.R;
import com.link.cloud.view.CardConfig;
import com.link.cloud.view.SignCardCallBack;
import com.link.cloud.view.SwipeCardLayoutManager;

/**
 * Created by 49488 on 2018/7/23.
 */

public class SignPagerViewAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return 3;
    }
    Context context;
    public SignPagerViewAdapter(Context context){
        this.context=context;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=null;
        if(position==0){
             view = View.inflate(context, R.layout.sign_introduce_item,null);
        }
        if(position==1){
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
        if(position==2){
            view = View.inflate(context, R.layout.sign_success,null);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

}
