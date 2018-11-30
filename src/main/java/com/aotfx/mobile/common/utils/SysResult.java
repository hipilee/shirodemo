package com.aotfx.mobile.common.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Description 封装访问结果
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-17 13:04
 */

@Data
public class SysResult<T> {
    private int code;
    private String message;
    private T data;

    public SysResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> SysResult<T> build(int code, String message, T data) {

        return new SysResult<>(code, message, data);
    }
}
