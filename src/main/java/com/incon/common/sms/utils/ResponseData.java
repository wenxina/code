package com.incon.common.sms.utils;

import java.io.Serializable;

/**
 * @author wang
 */
public class ResponseData implements Serializable {
    private static final long serialVersionUID = 4568428634958432029L;

    private int code;   //  请求编码
    private String msg; //  请求结果
    private Object data;    //  请求数据

    public ResponseData(int code, String msg, Object data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
