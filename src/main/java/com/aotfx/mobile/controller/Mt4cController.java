package com.aotfx.mobile.controller;

import com.jfx.Broker;
import com.jfx.net.JFXServer;
import com.aotfx.mobile.dao.entity.HistroyOrderBean;
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

        return null;
    }

}
