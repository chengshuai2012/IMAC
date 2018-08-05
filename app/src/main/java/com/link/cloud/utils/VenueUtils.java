package com.link.cloud.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import md.com.sdk.MicroFingerVein;

/**
 * Created by 49488 on 2018/7/27.
 */

public class VenueUtils {

    public interface VenueCallBack{
        void modelMsg(String msg);
        void identifyMsg(String msg,String uid);
    };
    private  String TAG ="VENUEUTILS";
    private  boolean bOpen;//设备是否打开
    private  volatile boolean bRun=true;//是否运行工作线程
    private  volatile boolean bWorkIdentify;//是否是认证
    private volatile boolean bWorkModel;//是否是建模
    private  Thread mdWorkThread;//进行建模或认证的全局工作线程
    MicroFingerVein microFingerVein;
    Context context;
    VenueCallBack callBack;
    public  void initVenue( MicroFingerVein microFingerVein,  Context context,VenueCallBack callBack,Boolean bWorkIdentify,Boolean bWorkModel,Boolean bOpen){
        this.microFingerVein=microFingerVein;
        this.context=context;
        this.callBack=callBack;
        this.bOpen=bOpen;
        this.bWorkModel=bWorkModel;
        this.bWorkIdentify=bWorkIdentify;
        startIdenty();
    }
    public  void startIdenty(){
        bRun=true;
        if(mdWorkThread == null){
            mdWorkThread=new Thread(runnable);
            mdWorkThread.start();
        }else {
            mdWorkThread.interrupt();
            mdWorkThread.start();
        }
    }
    public  void StopIdenty(){
        bRun=false;
        if(mdWorkThread == null){

        }else {
            try {
                mdWorkThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int state = 0;
            int[] pos = new int[1];
            float[] score = new float[1];
            boolean ret;
            Log.e(TAG, "run: "+bRun);
            ModelImgMng modelImgMng = new ModelImgMng();
            int[] tipTimes = {0, 0};//后两次次建模时用了不同手指，重复提醒限制3次
            int modOkProgress = 0;
            Log.e(TAG, "run: "+bRun);
            while (bRun) {
                if (!bOpen) {
                    if (MicroFingerVein.fvdev_get_count() == 0) {//无设备连接
                        try {
                            Thread.sleep(300L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    modOkProgress = 0;
                    modelImgMng.reset();
                    bOpen = microFingerVein.fvdev_open(0);//开启指定索引的设备
                    if (bOpen) {
                        Log.e(TAG, "fvdevOpen success");
                        // handler.obtainMessage(MSG_SHOW_LOG,"fvdevOpen success").sendToTarget();
                    } else {
                        Log.e(TAG, "fvdevOpen failed,modeling stop,identifying stop.");
                        // handler.obtainMessage(MSG_SHOW_LOG,"fvdevOpen failed,modeling and identifying stop.\nplease check the connect to device.\n").sendToTarget();
                    }
                    continue;
                }
                //设备连接正常则获取设备状态，进入正常建模或认证流程
                state = microFingerVein.fvdev_get_state(0);
                callBack.modelMsg("请放置手指");
                if (state != 0) {//返回值state=3表检测到了触摸（148设备只有一个touch，返回值没有1,2）
                    //deviceTouchState=0;
                    if (!bWorkIdentify && !bWorkModel) {
                        Log.e(TAG, "no identify or model task,try sleep a moment.");
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    //-----------------------------------------------------------------------------------------------------//抓图与质量评估
                    Log.e(TAG, "try grab image.");//3中示例抓图方式，按需选择；
                    //byte[] img=MdFvHelper.tryGetFirstBestImg(microFingerVein,mdDevice.getDeviceIndex(),5);//optional way 1
                    byte[] img = MdFvHelper.tryGetFirstBestImgNew(microFingerVein, 0, 5);//optional way 2
                    //byte[] img=microFingerVein.fvdev_grab(mdDevice.getDeviceIndex());//optional way 3
                    //--------------------------
                    if (img == null) {
                        Log.e(TAG, "get img failed,please try again");
                        // handler.obtainMessage(MSG_SWITCH_POP_CONTENT,false).sendToTarget();
                        continue;
                    }
                    //---------------------------//show visible decoded img(optional function)
                    byte[] imgVisible = MicroFingerVein.fvGetImage(img);
                    final Bitmap bmpVisible = CommonUtils.getBitmapByBytes(imgVisible, 1);
                    //---------------------------show visible decoded img(optional function)
                    //handler.obtainMessage(MSG_SWITCH_POP_CONTENT).sendToTarget();
                    //----------------------------------------------------------//质评有新旧两个接口，新接口能返回更多状态值，按需选择；
                    //int quality=MicroFingerVein.fv_quality(img);//质评接口①：仅通过函数返回值返回int型质评结果0,2001,2002,2003；
                    float[] quaScore = {0f, 0f, 0f, 0f};//idx[0]：质评结果，idx[1]：质评得分，idx[2]：漏光值，idx[3]:按压值
                    int quaRtn = MicroFingerVein.fv_QualityEx(img, quaScore);//质评接口②：通过传入的引用数组返回多个质评参数；
                    String oneResult = new StringBuilder().append("quality return=" + quaRtn).append(",result=").append(quaScore[0]).append(",score=").append(quaScore[1]).append(",fLeakRatio=").append(quaScore[2]).append(",fPress=").append(quaScore[3]).toString();
                    Log.e(TAG, oneResult);
                    int quality = (int) quaScore[0];
                    //----------------------------------------------------------
                    if (quality != 0) {
                        //   handler.obtainMessage(MSG_SHOW_LOG,"img quality not good,pleas retry.").sendToTarget();
                        // handler.obtainMessage(MSG_SWITCH_POP_CONTENT,false).sendToTarget();
                        //-----------------------------------
                        callBack.modelMsg("取图失败请抬高手指,重试");
                        //------------------------------------
                        continue;
                    }
                    //handler.obtainMessage(MSG_SWITCH_POP_CONTENT).sendToTarget();
                    //-----------------------------------------------------------------------------------------------------//抓图与质量评估
                    byte[] feature = MicroFingerVein.fv_extract_model(img, null, null);
                    if (feature == null) {
                        Log.e(TAG, "fvExtraceFeature get feature from img fail,retry soon");
                        callBack.modelMsg("取图失败请抬高手指,重试");
                        //  handler.obtainMessage(MSG_SHOW_LOG,"fvExtraceFeature get feature from img fail,retrying...").sendToTarget();
                    } else {
                        if (bWorkIdentify) {//认证
                            tipTimes[0] = 0;
                            tipTimes[1] = 0;
                            modelImgMng.reset();
                            modOkProgress = 0;
                            if (identifyNewImg(img, pos, score)) {//比对及判断得分放到identifyNewImg()内实现
                                Log.e("\nIdentify success,", "pos=" + pos[0]);
                                //handler.obtainMessage(MSG_SHOW_LOG,"Identify success,pos="+ pos[0]).sendToTarget();
                            } else {
                                Log.e("Identify fail,", "pos=" + pos[0]);
                                // handler.obtainMessage(MSG_SHOW_LOG,"Identify fail,pos="+ pos[0]).sendToTarget();
                                //----------------------------------------------------------
                                //if(isMdDebugOpen&&!featureSpUtils.isFeatureDbEmpty()){//数据库非空+debug模式开启+认证失败时才打印
                                //    String tips="DEBUG:本次认证图片及状态日志已保存到目录：\n"+MdDebugger.debugIdentifySrcByTimeMillis(false,pos[0],score[0],img);
                                //    Log.e(TAG,tips);
                                //    handler.obtainMessage(MSG_SHOW_LOG,tips).sendToTarget();
                                //}
                                //----------------------------------------------------------
                            }
                            while (state == 3 && bRun) {
                                //deviceTouchState=0;
                                state = microFingerVein.fvdev_get_state(0);
                                if (!bOpen) {//等待手指拿开的中途设备断开了
                                    Log.e(TAG, "device disconnected when identifying is waiting for finger moving away");
                                    //handler.obtainMessage(MSG_SHOW_LOG,"device disconnected when identifying is waiting for finger moving away").sendToTarget();
                                }
                            }
                            continue;
                        } else if (bWorkModel) {//建模
                            modOkProgress++;
                            if (modOkProgress == 1) {//first model
                                tipTimes[0] = 0;
                                tipTimes[1] = 0;
                                modelImgMng.setImg1(img);
                                modelImgMng.setFeature1(feature);
                                Log.e(TAG, "first model ok");
                                callBack.modelMsg("请抬起手指，再次放置");
                                //handler.obtainMessage(MSG_SHOW_LOG,"first model Ok").sendToTarget();
                                // handler.obtainMessage(MSG_SHOW_LOG,"please reput(重放) your finger on device for second modeling").sendToTarget();
                            } else if (modOkProgress == 2) {//second model
                                ret = MicroFingerVein.fv_index(modelImgMng.getFeature1(), 1, img, pos, score);
                                if (ret && score[0] > MODEL_SCORE_THRESHOLD) {
                                    feature = MicroFingerVein.fv_extract_model(img, null, null);//无须传入第一张图片，第三次混合特征值时才同时传入3张图；
                                    if (feature != null) {
                                        tipTimes[0] = 0;
                                        tipTimes[1] = 0;
                                        modelImgMng.setImg2(img);
                                        modelImgMng.setFeature2(feature);
                                        Log.e(TAG, "second model ok");
                                        callBack.modelMsg("请抬起手指，再次放置");
                                        // handler.obtainMessage(MSG_SHOW_LOG,"second model Ok").sendToTarget();
                                        //handler.obtainMessage(MSG_SHOW_LOG,"please reput(重放) your finger on device for third modeling").sendToTarget();
                                    } else {//第二次建模从图片中取特征值无效
                                        modOkProgress = 1;
                                        if (++tipTimes[0] <= 3) {
                                            callBack.modelMsg("请放置同一根手指");
                                            Log.e(TAG, "get feature from img failed when try second modeling");
                                            //  handler.obtainMessage(MSG_SHOW_LOG,"please move away your finger and put the same one for second modeling!!!").sendToTarget();
                                        } else {//连续超过3次放了不同手指则忽略此次建模重来
                                            modOkProgress = 0;
                                            modelImgMng.reset();
                                            callBack.modelMsg("请放置同一根手指");
                                            Log.e(TAG, "put different finger more than 3 times,this modeling is ignored,a new modeling start.");
                                            // handler.obtainMessage(MSG_SHOW_LOG,"\nput different finger more than 3 times,this modeling is IGNORED,a new modeling start.\n").sendToTarget();
                                        }
                                    }
                                } else {
                                    modOkProgress = 1;
                                    if (++tipTimes[0] <= 3) {
                                        Log.e(TAG, "get feature from img failed when try second modeling");
                                        callBack.modelMsg("请放置同一根手指");
                                        //handler.obtainMessage(MSG_SHOW_LOG,"please move away your finger and put the same one for second modeling!!!").sendToTarget();
                                    } else {//连续超过3次放了不同手指则忽略此次建模重来
                                        modOkProgress = 0;
                                        modelImgMng.reset();
                                        Log.e(TAG, "put different finger more than 3 times,this modeling is ignored,a new modeling start.");
                                        /// handler.obtainMessage(MSG_SHOW_LOG,"\nput different finger more than 3 times,this modeling is IGNORED,a new modeling start.\n").sendToTarget();
                                        callBack.modelMsg("三次数据不同，重新建模");
                                    }
                                }
                            } else if (modOkProgress == 3) {//third model
                                ret = MicroFingerVein.fv_index(modelImgMng.getFeature2(), 1, img, pos, score);
                                if (ret && score[0] > MODEL_SCORE_THRESHOLD) {
                                    feature = MicroFingerVein.fv_extract_model(modelImgMng.getImg1(), modelImgMng.getImg2(), img);
                                    if (feature != null) {//成功生成一个3次建模并融合的融合特征数组
                                        tipTimes[0] = 0;
                                        tipTimes[1] = 0;
                                        modelImgMng.setImg3(img);
                                        modelImgMng.setFeature3(feature);
                                        //保存3次建模成功的混合模版
                                        //  handler.obtainMessage(MSG_SHOW_LOG,"thirdly model Ok, this model have been saved to database...\n").sendToTarget();
                                        // handler.obtainMessage(MSG_SHOW_LOG,"now you can reput(重放) other finger for modeling.\n").sendToTarget();
                                        //----------------------------------------------------------
                                        //if(isMdDebugOpen) {//保存3次建模后的3张图片用于分析异常情况;
                                        //    String tips="DEBUG:本次建模图片及日志已经保存到:\n"+MdDebugger.debugModelSrcByTimeMillis(modelImgMng.getImg1(),modelImgMng.getImg2(),modelImgMng.getImg3());
                                        //    Log.e(TAG,tips);
                                        //    handler.obtainMessage(MSG_SHOW_LOG,tips).sendToTarget();
                                        //}
                                        //----------------------------------------------------------
                                        modelImgMng.reset();
                                        bRun = false;
                                    } else {//第三次建模从图片中取特征值无效
                                        modOkProgress = 2;
                                        if (++tipTimes[1] <= 3) {
                                            Log.e(TAG, "fvExtraceFeature get third feature fail(isAllImgDataOk=" + modelImgMng.isAllImgDataOk() + ")");
                                            //  handler.obtainMessage(MSG_SHOW_LOG,"please move away your finger and put the same one for third modeling!!!").sendToTarget();
                                            callBack.modelMsg("请放置同一根手指");
                                        }
                                    }
                                } else {
                                    modOkProgress = 2;
                                    if (++tipTimes[1] <= 3) {
                                        // handler.obtainMessage(MSG_SHOW_LOG,"please move away your finger and put the same one for third modeling!!!").sendToTarget();
                                        callBack.modelMsg("三次数据不同，重新建模");
                                    }
                                    continue;
                                }
                            } else if (modOkProgress > 3 || modOkProgress <= 0) {
                                modOkProgress = 0;
                                modelImgMng.reset();
                            }
                        }
                    }
                    //-----------------------------------------------------------
                    while (state == 3 && bRun) {
                        //deviceTouchState=0;
                        state = microFingerVein.fvdev_get_state(0);
                        if (!bOpen) {//等待手指拿开的中途设备断开了
                            Log.e(TAG, "device disconnected when identifying is waiting for finger moving away");
                            //handler.obtainMessage(MSG_SHOW_LOG,"device disconnected when identifying is waiting for finger moving away").sendToTarget();
                        }
                    }
                    //-----------------------------------------------------------
                    continue;
                } else {//触摸state==0时，表无触摸状态
                    // handler.obtainMessage(MSG_SWITCH_POP_CONTENT).sendToTarget();
                    if (bOpen) {
                        //deviceTouchState=1;
                    }
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (bOpen) {
                microFingerVein.close(0);
                bOpen = false;
                bRun=false;
            }

    }
    };



    private boolean identifyNewImg(final byte[] img,int[] pos,float[] score) {
        final HashMap allFeatures=null;
        if(allFeatures==null) {
            Log.e(TAG,"can't identify,because features database is empty!");
            //handler.obtainMessage(MSG_SHOW_LOG,"can't identify,features database is empty!").sendToTarget();
            return false;
        }
        Log.e(TAG,"try identify new img,total db features counts="+allFeatures.size());
       // handler.obtainMessage(MSG_SHOW_LOG,"try identify new img,total db features counts="+allFeatures.size()).sendToTarget();
        byte[] allFeaturesBytes=new byte[0];
        List<byte[]> allFeatureList=new ArrayList<byte[]>(allFeatures.values());
        for(byte[] feature:allFeatureList){
            allFeaturesBytes= CommonUtils.byteMerger(allFeaturesBytes,feature);
        }
        boolean identifyResult= MicroFingerVein.fv_index(allFeaturesBytes,allFeatures.size(),img,pos,score);//比对是否通过
        identifyResult=identifyResult&&score[0]>IDENTIFY_SCORE_THRESHOLD;//得分是否达标
        if(identifyResult){//比对通过且得分达标时打印此手指绑定的用户名
            Set<String> allFeatureKey=allFeatures.keySet();
            String featureName=(String)allFeatureKey.toArray()[pos[0]];
            Log.e(TAG,"identified finger user name："+featureName);
           // handler.obtainMessage(MSG_SHOW_LOG,"identified finger user name："+featureName).sendToTarget();
        }
        return identifyResult;
    }
    private final static int MSG_SWITCH_POP_CONTENT=6;
    private final static float IDENTIFY_SCORE_THRESHOLD=0.63f;//认证通过的得分阈值，超过此得分才认为认证通过；
    private final static float MODEL_SCORE_THRESHOLD=0.4f;//
    private  boolean checkIsModelRepeatRegister(final byte[] img){
        final HashMap allFeatures=null;
        if(allFeatures!=null){
            int[] pos=new int[1];
            float[] score=new float[1];
            byte[] allFeaturesBytes=new byte[0];
            List<byte[]> allFeatureList=new ArrayList<byte[]>(allFeatures.values());
            for(byte[] feature:allFeatureList){
                allFeaturesBytes=CommonUtils.byteMerger(allFeaturesBytes,feature);
            }
            boolean identifyResult= MicroFingerVein.fv_index(allFeaturesBytes,allFeatures.size(),img,pos,score);
            identifyResult=identifyResult&&score[0]>IDENTIFY_SCORE_THRESHOLD;
            return identifyResult;
        }
        return false;
    }
}
