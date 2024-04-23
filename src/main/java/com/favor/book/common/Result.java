package com.favor.book.common;

import lombok.Getter;

@Getter
public class Result {
    // 响应码，1 代表成功; 0 代表失败
    private Integer code;
    // 响应码 描述字符串
    private String msg;
    // 返回的数据
    private Object data;

    public Result() {
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 增删改 成功响应(不需要给前端返回数据)
    public static Result success() {
        return new Result(1, "success", null);
    }

    // 查询 成功响应(把查询结果做为返回数据响应给前端)
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    // 失败响应
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
