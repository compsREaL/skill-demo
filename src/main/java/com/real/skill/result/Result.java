package com.real.skill.result;

/**
 * @author: mabin
 * @create: 2019/5/15 21:00
 */
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    private Result(T data){
        this.code=0;
        this.msg="success";
        this.data=data;
    }

    private Result(CodeMsg msg){
        if (msg==null){
            return;
        }
        this.code=msg.getCode();
        this.msg=msg.getMsg();
    }

    /**
     * 成功时候调用
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    public static <T> Result<T> error(CodeMsg msg){
        return new Result<>(msg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
