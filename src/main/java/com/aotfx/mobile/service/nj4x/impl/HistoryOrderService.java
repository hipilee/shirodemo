package com.aotfx.mobile.service.nj4x.impl;

import com.aotfx.mobile.dao.entity.HistoryOrderBean;
import com.aotfx.mobile.dao.mapper.HistoryOrderMapper;

import com.aotfx.mobile.service.nj4x.IHistoryOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HistoryOrderService extends ServiceImpl<HistoryOrderMapper, HistoryOrderBean> implements IHistoryOrderService {
}
