package com.aotfx.mobile;/*
 * Metatrader Java (JFX) / .Net (NJ4X) library
 * Copyright (c) 2008-2014 by Gerasimenko Roman.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistribution of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistribution in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The names "JFX" or "NJ4X" must not be used to endorse or
 * promote products derived from this software without prior
 * written permission. For written permission, please contact
 * roman.gerasimenko@nj4x.com
 *
 * 4. Products derived from this software may not be called "JFX" or
 * "NJ4X", nor may "JFX" or "NJ4X" appear in their name,
 * without prior written permission of Gerasimenko Roman.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JFX CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */


import com.jfx.Broker;
import com.jfx.ErrNoOrderSelected;
import com.jfx.SelectionPool;
import com.jfx.SelectionType;
import com.jfx.net.JFXServer;
import com.jfx.strategy.Strategy;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


/**
 * JFX Strategy used as a simple MT4 connection.
 * This demo is to show that coordinate() method can be ignored.
 */
public class MT4ConnectionExample extends Strategy {
    static {
        System.setProperty("jfx_server_port", "7779");
        //如果程序和terminal server不在同一台电脑上，需要执行编写下面这句代码
        System.setProperty("nj4x_server_host", "192.168.1.7");
    }

    public void connect() throws IOException {
//        this.withHistoryPeriod(Strategy.HistoryPeriod.ALL_HISTORY).connect("39.108.14.200", 7788, new Broker("FXCM-USDDemo01"), "3815241", "ur4bzys");
        // this.withHistoryPeriod(Strategy.HistoryPeriod.ALL_HISTORY).connect("192.168.1.4", 7788,  new Broker("162.13.94.164") ,"77011583", "Ava12345");
        //this.withHistoryPeriod(Strategy.HistoryPeriod.ALL_HISTORY).connect("192.168.1.4", 7788,  new Broker("mt4d01.fxcorporate.com") ,"3817104", "Ava12345");
        this.withHistoryPeriod(Strategy.HistoryPeriod.ALL_HISTORY).connect("192.168.1.8", 7788, new Broker("ICMarkets-Demo03"), "30143803", "msxi71");
    }

    public void coordinate() {
        // method has been ignored
    }

    public static void main(String[] args) throws IOException, ErrNoOrderSelected {


        MT4ConnectionExample mt4c = new MT4ConnectionExample();


        System.out.println("======= Connecting ========= " + JFXServer.getInstance().getBindPort());
        mt4c.connect();
        System.out.println("======= Connected ========= ");
        System.out.println("Account info:equity=" + mt4c.accountEquity() + ", balance =" + mt4c.accountBalance());
        System.out.println("Account info: symbols=" + mt4c.getSymbols());

        System.out.println("Account info: histroy orders =" + mt4c.ordersHistoryTotal());
        System.out.println(mt4c.serverTimeGMTOffset() + "timezone");


        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        System.out.println("当前时间" + date);
        cal.setTime(date);
        cal.add(Calendar.HOUR, -15);// 24小时制
        Date mt4Date = cal.getTime();

        System.out.println("调整后的时间" + mt4Date);


        int ordersHistoryCount = mt4c.ordersHistoryTotal();



//
//        for (int i = 0; i < 1; i++) {
//            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_HISTORY)) {
//                try {
////                    int type = mt4c.orderType(); // see TradeOperation
////                    System.out.println(String.format("history order symbol=%s, type=%s, #%s,P/L=%fP/L=%f P/L=%f, comments=%s",
////                            mt4c.orderSymbol(), mt4c.orderType(),TimeTrans.transferLongToDate("yyyy-MM-dd HH:mm:ss",  mt4c.orderOpenTime()), mt4c.orderProfit(), mt4c.orderComment())
////                    );
//
//
////                    String gbk = new String(mt4c.orderComment().getBytes("decode"), "code");
////                    System.out.println(gbk);
//
////                    System.out.printf(mt4c.orderOpenPrice()+"");
////                    System.out.printf(mt4c.orderClosePrice() + "");
//
//
//
//
//                    Map<String, Charset> map = Charset.availableCharsets();
//                    for (Map.Entry<String, Charset> entry1 : map.entrySet()) {
//                        System.out.println("Key = " + entry1.getKey() + ", Value = " + entry1.getValue());
//                        for (Map.Entry<String, Charset> entry2 : map.entrySet()) {
//                            System.out.println("Key = " + entry2.getKey() + ", Value = " + entry2.getValue());
//
//                            try {
//                                String gbk1 = new String(mt4c.orderComment().getBytes(entry1.getKey()), entry2.getKey());
//                                System.out.println("======"+gbk1);
//
//                                String gbk2 = new String(mt4c.orderComment().getBytes(entry2.getKey()), entry1.getKey());
//                                System.out.println("======"+gbk2);
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//
//                } catch (ErrNoOrderSelected errNoOrderSelected) {
//                    errNoOrderSelected.printStackTrace();
//                }
//            }
//        }

        System.out.println("Account info: active orders =" + mt4c.ordersTotal());
        int ordersCount = mt4c.ordersTotal();
//        for (int i = 0; i < ordersCount; i++) {
//            if (mt4c.orderSelect(i, SelectionType.SELECT_BY_POS, SelectionPool.MODE_TRADES)) {
//                int type = orderType(); // see TradeOperation
//                System.out.println(String.format("Active order #%d, P/L=%f, comments=%s",
//                        orderTicket(), orderProfit(), orderComment())
//                );
//            }
//        }
        System.out.println("======= Disconnecting ========= ");
        mt4c.close(true);
        System.out.println("======= Disconnected ========= ");
        JFXServer.stop();
    }
}
