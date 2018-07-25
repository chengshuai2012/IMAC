package com.link.cloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.link.cloud.R;

/**
 * Created by ls on 2017/11/25.
 */

public class SignCardAdapter extends RecyclerView.Adapter<SignCardAdapter.UniversalViewHolder> {
    //public ReservationList mData;ReservationList mData,
    public Context context;

    public SignCardAdapter(Context context) {
     //   this.mData = mData;
        this.context = context;
    }
    PositionChangedLister listener;
    public interface PositionChangedLister{
        void postionChanged(String postion, String id, String LessonStart, String EmName, String LesName, String State, String RestCount, String ReservationNumber);
    }
    public void setPostionListener(PositionChangedLister listener){
        this.listener=listener;
    }
    @Override
    public UniversalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.muti_card_item, null);
        UniversalViewHolder holder = new UniversalViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UniversalViewHolder holder, int position) {
//        UniversalViewHolder holder1=holder;
//        holder1.lessonTime.setText("上课时间:"+mData.getData().get(position).getLessonStartTime());
//        holder1.coachName.setText("教练姓名:"+mData.getData().get(position).getEmployeeName());
//        holder.lesname.setText("课程名称:"+mData.getData().get(position).getLessonName());
//        holder.lessonStates.setText("状态:"+mData.getData().get(position).getReservationStatus());
//        holder.lessonNum.setText("预约课时:"+mData.getData().get(position).getReservationNumber());
//        if(listener!=null){
//            listener.postionChanged(mData.getData().get(position).getReservationID(),mData.getData().get(position).getEmployeeID()
//            ,mData.getData().get(position).getLessonStartTime()
//            ,mData.getData().get(position).getEmployeeName()
//            ,mData.getData().get(position).getLessonName()
//            ,mData.getData().get(position).getReservationStatus()
//            ,mData.getData().get(position).getStillNumber()+""
//                    ,mData.getData().get(position).getReservationNumber()
//            );
//        }
    }

    @Override
    public int getItemCount() {
//        return mData == null ? 0 : mData.getData().size();
        return 3;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class UniversalViewHolder extends RecyclerView.ViewHolder {
        public TextView lesname;
        public TextView coachName;
        public TextView lessonTime;
        public TextView lessonStates;
        public TextView lessonNum;


        public UniversalViewHolder(View itemView) {
            super(itemView);
//            lesname= (TextView) itemView.findViewById(R.id.lesson_name);
//            coachName= (TextView) itemView.findViewById(R.id.coch_name);
//            lessonTime= (TextView) itemView.findViewById(R.id.lesson_time);
//            lessonStates= (TextView) itemView.findViewById(R.id.lesson_statue);
//            lessonNum= (TextView) itemView.findViewById(R.id.lesson_num);

        }
    }
}