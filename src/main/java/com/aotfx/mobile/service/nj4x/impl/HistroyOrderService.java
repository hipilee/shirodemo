package com.aotfx.mobile.service.nj4x.impl;

import com.aotfx.mobile.dao.entity.HistroyOrderBean;
import com.aotfx.mobile.dao.mapper.HistroyOrderMapper;
import com.aotfx.mobile.service.nj4x.IHistroyOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HistroyOrderService extends ServiceImpl<HistroyOrderMapper,HistroyOrderBean> implements IHistroyOrderService {
}
