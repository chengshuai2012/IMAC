package com.link.cloud.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.util.HashMap;

public class FeatureSpUtils {
    private final static String TAG=FeatureSpUtils.class.getSimpleName()+"_DEBUG";
    private final static String DB_NAME="MdRecFeatureSpDb";
    private final static String FEATURE_LIST="FeatureList";
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public FeatureSpUtils(Context context){
        this.context=context;
        sp=context.getSharedPreferences(this.DB_NAME, Context.MODE_PRIVATE);
        editor=sp.edit();
    }

    private boolean saveFeature(final byte[] feature,final String saveName) {
        if(feature==null||saveName==null||saveName.equals("")) {
            Log.e(TAG,"saveFeature():parameters error");
            return false;
        }
        final String featureStr=Base64.encodeToString(feature,Base64.NO_WRAP);
        editor.putString(saveName,featureStr);
        String featureList=sp.getString(FEATURE_LIST,"");
        editor.putString(FEATURE_LIST,featureList+(saveName+"_"));
        editor.commit();
        Log.e(TAG,"save a new feature.(featureName="+saveName+")");
        return true;
    }

    public boolean saveFeatureByTimeMillis(byte[] feature){
        String featureName="mdFeature"+System.currentTimeMillis();
        saveFeature(feature,featureName);
        return true;
    }

    public boolean isFeatureDbEmpty() {
        String featureList=sp.getString(FEATURE_LIST,"");
        if(featureList.equals("")) return true;
        return false;
    }

    public HashMap<String,byte[]> getAllFeatures() {
        String featureList=sp.getString(FEATURE_LIST,"");
        if(featureList.equals("")) return null;
        String[] featureListStr=featureList.split("_");
        HashMap allFeatures=new HashMap<String,byte[]>();
        for(int i=0;i<featureListStr.length;i++) {
            if(!featureListStr[i].equals("")) {//注意分隔符"_"分出的最后一项是空的
                Log.i(TAG,"featureDb"+i+":"+featureListStr[i]);
                allFeatures.put(featureListStr[i], geFeatureFromSp(featureListStr[i]));
            }
        }
        return allFeatures;
    }

    public byte[] geFeatureFromSp(final String saveName) {
        String featureStr=sp.getString(saveName,"");
        if(featureStr.equals("")) {
            Log.e(TAG,"get feature from sp failed,feature saveName is empty");
            return null;
        }
        byte[] feature=Base64.decode(featureStr,Base64.NO_WRAP);
        return feature;
    }

    public boolean isFeatureDbExist(final String saveName) {
        return sp.contains(saveName);
    }

    public boolean clearAllFeatures(){
        return editor.clear().commit();
    }
}
