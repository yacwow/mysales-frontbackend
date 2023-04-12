package com.duyi.readingweb.bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class ResultMsg {
    private boolean result;
    private int code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    public ResultMsg() {}

    //成功静态方法
    public static ResultMsg ok(){
        ResultMsg result = new ResultMsg();
        result.setResult(true);
        result.setCode(ResultCode.SUCCESS);//设置状态码
        result.setMessage("成功");//设置消息
        return result;
    }

    //失败静态方法
    public static ResultMsg error(){
        ResultMsg result = new ResultMsg();
        result.setResult(false);
        result.setCode(ResultCode.ERROR);
        result.setMessage("失败");
        return result;
    }

    public ResultMsg result(Boolean result){
        this.setResult(result);
        return this;
    }

    public ResultMsg message(String message){
        this.setMessage(message);
        return this;
    }

    public ResultMsg code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResultMsg data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public ResultMsg data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

}
