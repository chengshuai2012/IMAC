package com.link.cloud.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.link.cloud.R;
import com.moxun.tagcloudlib.view.TagsAdapter;

/**
 * Created by 49488 on 2018/7/25.
 */

public class EntranceAdapter extends TagsAdapter {

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        if(position%4==0){
            return View.inflate(context, R.layout.function_item,null);

        }else if(position%4==1){
            View view =  View.inflate(context,R.layout.function_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.go_to_class);
            imageView.setImageResource(R.drawable.sign);
            return view;
        }else if(position%4==2){
            View view =  View.inflate(context,R.layout.function_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.go_to_class);
            imageView.setImageResource(R.drawable.finish_class);
            return view;
        }else {
            View view =  View.inflate(context,R.layout.function_item,null);
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
