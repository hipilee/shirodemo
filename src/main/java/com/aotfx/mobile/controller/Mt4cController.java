package com.aotfx.mobile.controller;

import com.aotfx.mobile.dao.entity.HistoryOrderBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class Mt4cController {
    @ResponseBody
    @RequestMapping("/historyorders/{mt4}")
    public HistoryOrderBean[] getHistroyOrders(@PathVariable(name="mt4") int mt4){

        return null;
    }

}
