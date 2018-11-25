package com.aotfx.mobile.config.nj4x;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-25 21:42
 */

@Data
@Component
@ConfigurationProperties(prefix = "nj4x")
@PropertySource("classpath:/nj4x.properties")//@PropertySource来指定自定义的资源目录
public class Nj4xConfig {
    private String terminalServerHost;
    private int terminalServerPort;
    private String localhost;
    private int jfxServerPort;
}
