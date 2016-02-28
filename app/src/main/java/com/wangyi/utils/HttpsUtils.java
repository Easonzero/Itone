package com.wangyi.utils;

import java.io.File;
import java.util.Map;

import org.xutils.x;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

public class HttpsUtils {
    public static <T>Cancelable Get(String url,Map<String,String> map,CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        if(null!=map){
            for(Map.Entry<String, String> entry : map.entrySet()){
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    public static <T>Cancelable Post(String url,Map<String,Object> map,CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        if(null!=map){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    public static <T>Cancelable UpLoadFile(String url,Map<String,Object> map,CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        if(null!=map){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                if(entry.getKey().equals("file"))
                    params.addBodyParameter(entry.getKey(), (File)entry.getValue());
                else{
                    params.addBodyParameter(entry.getKey(), (String)entry.getValue());
                }
            }
        }
        params.setMultipart(true);
        Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    public static <T>Cancelable DownLoadFile(String url,String filepath,CommonCallback<T> callback){
        RequestParams params=new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }
}
