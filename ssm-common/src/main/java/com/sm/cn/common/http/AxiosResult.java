package com.sm.cn.common.http;

import java.util.HashMap;
import java.util.Hashtable;

public class AxiosResult extends HashMap<String,Object> {

    private final static String STATUS="status";
    private final static String MSG="msg";
    private final static String DATA="data";

    public AxiosResult(AxiosStatus axiosStatus) {
        this.put(MSG,axiosStatus.getMsg());
        this.put(STATUS,axiosStatus.getStatus());
    }

    //返回自定义状态码
    public static AxiosResult custom(AxiosStatus axiosStatus){
        return  new AxiosResult(axiosStatus);
    }

    //返回成功没有携带数据
    public static AxiosResult success(){
        return  new AxiosResult(AxiosStatus.OK);
    }

    //返回成功，携带数据
    public static AxiosResult success(Object obj){
        AxiosResult success = success();
        success.put(DATA,obj);
        return success;
    }

    //返回失败没有携带数据
    public static AxiosResult error(){
        return  new AxiosResult(AxiosStatus.ERROR);
    }

    //返回失败，携带数据
    public static AxiosResult error(Object obj){
        AxiosResult error = error();
        error.put(DATA,obj);
        return error;
    }
}
