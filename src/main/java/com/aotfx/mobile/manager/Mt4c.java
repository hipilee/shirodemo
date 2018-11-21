package com.aotfx.mobile.manager;

import com.jfx.Broker;
import com.jfx.strategy.Strategy;

import java.net.InetAddress;

public class Mt4c extends Strategy {
    static {
        System.setProperty("jfx_server_port", "7779");

        //如果程序和terminal server不在同一台电脑上，需要执行编写下面这句代码,值是本机IP地址
        InetAddress inetAddress = null;

        System.setProperty("nj4x_server_host", "192.168.1.4");
    }

    private String termServerHost;
    private int termServerPort;
    private Broker mt4Server;
    private String mt4User;
    private String mt4Password;

    public Mt4c(String termServerHost, int termServerPort, Broker mt4Server, String mt4User, String mt4Password) {
        this.termServerHost = termServerHost;
        this.termServerPort = termServerPort;
        this.mt4Server = mt4Server;
        this.mt4User = mt4User;
        this.mt4Password = mt4Password;
    }

    public void connect(Strategy.HistoryPeriod historyPeriod) throws Exception {
        this.withHistoryPeriod(historyPeriod).connect(termServerHost, termServerPort, mt4Server, mt4User, mt4Password);
    }
}
