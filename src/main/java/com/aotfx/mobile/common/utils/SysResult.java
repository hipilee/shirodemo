package com.aotfx.mobile.common.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 封装访问结果
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-17 13:04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysResult<T> {
    private int code;
    private String message;
    private T data;
}
