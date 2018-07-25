package com.link.cloud.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.link.cloud.R;
import com.link.cloud.base.BaseActivity;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.moxun.tagcloudlib.view.TagsAdapter;

import butterknife.BindView;

/**
 * Created by 49488 on 2018/7/23.
 */

public class ActivityEn extends BaseActivity {
    @BindView(R.id.global)
    TagCloudView global;

    @Override
    protected void setTime(String date, String time, String timeWithSecond) {

    }

    @Override
    protected int getLayoutID() {
        return R.layout.acitivity_intrance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAdapter mmyAdapter = new myAdapter();
        global.setAdapter(mmyAdapter);
        global.setScrollSpeed(0.8f);
        global.setRadiusPercent(0.4f);

    }
    class myAdapter extends TagsAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public View getView(Context context, int position, ViewGroup parent) {
            if(position%4==0){
                return View.inflate(ActivityEn.this,R.layout.item,null);

            }else if(position%4==1){
                View view =  View.inflate(ActivityEn.this,R.layout.item,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.go_to_class);
                imageView.setImageResource(R.drawable.sign);
                return view;
            }else if(position%4==2){
                View view =  View.inflate(ActivityEn.this,R.layout.item,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.go_to_class);
                imageView.setImageResource(R.drawable.finish_class);
                return view;
            }else {
                View view =  View.inflate(ActivityEn.this,R.layout.item,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.go_to_class);
                imageView.setImageResource(R.drawable.pay_count);
                return view;
            }

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getPopularity(int position) {
            return 0;
        }

        @Override
        public void onThemeColorChanged(View view, int themeColor) {

        }
    }
}
