package com.aotfx.mobile.manager;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.jfx.Broker;
import com.jfx.strategy.Strategy;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Mt4c extends Strategy {

    static {
        System.setProperty("jfx_server_port", "7779");

        //如果程序和terminal server不在同一台电脑上，需要执行编写下面这句代码,值是本机IP地址
        InetAddress inetAddress = null;

        System.setProperty("nj4x_server_host", "192.168.1.7");
    }

    private static Lock connectLock = new ReentrantLock();

    private String termServerHost;
    private int termServerPort;
    private Broker mt4Server;
    private String mt4User;
    private String mt4Password;

    public Mt4c(Nj4xConfig nj4xConfig, Broker mt4Server, String mt4User, String mt4Password) {
        this.termServerHost = nj4xConfig.getTerminalServerHost();
        this.termServerPort = nj4xConfig.getTerminalServerPort();
        this.mt4Server = mt4Server;
        this.mt4User = mt4User;
        this.mt4Password = mt4Password;

        System.setProperty("jfx_server_port", Integer.toString(nj4xConfig.getJfxServerPort()));
        System.setProperty("nj4x_server_host", nj4xConfig.getLocalhost());
    }

    public void connect(Strategy.HistoryPeriod historyPeriod) throws Exception {
        connectLock.lock();
        System.out.println(mt4User+Thread.currentThread().getId() + "号线程 开始连接====");
        Long startTime = System.currentTimeMillis();
        Long endTime;
        try {
            try {
                this.withHistoryPeriod(historyPeriod).connect(termServerHost, termServerPort, mt4Server, mt4User, mt4Password);
                endTime = System.currentTimeMillis();
                System.out.println(mt4User+Thread.currentThread().getId() + "号线程 连接成功，耗时：" + (endTime - startTime) / 1000 + "sec");
            } finally {
                connectLock.unlock();

            }
        } catch (IOException e) {
            endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getId() + "号线程 连接失败，耗时：" + (endTime - startTime) / 1000 + "sec");
            e.printStackTrace();
            throw e;
        }
    }
}
