package com.aotfx.mobile;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.jfx.net.TerminalClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @Description 在启动完成后执行特定代码,如连接上nj4x-terminal-server，关闭先前未关闭的mt4客户端
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
    public void run(ApplicationArguments args) {

        try {
            boolean res =new TerminalClient(nj4xConfig.getTerminalServerHost(), nj4xConfig.getTerminalServerPort()).killTerminals();
            log.info("在" + nj4xConfig.getTerminalServerHost() + " ：" + nj4xConfig.getTerminalServerPort() + " 上关闭ts所有连接成功！");
        } catch (IOException e) {
            log.info("在" + nj4xConfig.getTerminalServerHost() + "：" + nj4xConfig.getTerminalServerPort() +" 上关闭ts所有连接失败，请检查nj4x_terminal_sever是否正常启动！");
        }



    }
}
