package com.aotfx.mobile.controller;

import com.jfx.Broker;
import com.jfx.net.JFXServer;
import com.aotfx.mobile.beans.HistroyOrderBean;
import com.aotfx.mobile.manager.Mt4c;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/api")
public class Mt4cController {
    @ResponseBody
    @RequestMapping("/histroyorders/{mt4}")
    public HistroyOrderBean[] getHistroyOrders(@PathVariable(name="mt4") int mt4){
        Mt4c mt4c = new Mt4c("192.168.1.7", 7788, new Broker("107.154.197.81"), "80012391", "Lxtcfx8793");
        try {
            mt4c.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HistroyOrderBean[] tHistroyOrders =  mt4c.histroyOrders();
        try {
            mt4c.close(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFXServer.stop();
        return tHistroyOrders;
    }

}
