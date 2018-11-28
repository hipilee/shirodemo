package com.aotfx.mobile;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.jfx.net.TerminalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * @Description 在启动完成后执行特定代码
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-27 15:55
 */

@Component
public class AfterServiceStarted implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(AfterServiceStarted.class);

    @Autowired
    private Nj4xConfig nj4xConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("关闭ts所有连接！"+nj4xConfig.getTerminalServerHost()+ "  " +nj4xConfig.getTerminalServerPort());

        boolean res = new TerminalClient(nj4xConfig.getTerminalServerHost(), nj4xConfig.getTerminalServerPort()).killTerminals();

        if(!res){
            throw new Exception("Fail to kill all terminals.");
        }else{
            log.info("关闭ts所有连接成功！");
        }
    }
}
