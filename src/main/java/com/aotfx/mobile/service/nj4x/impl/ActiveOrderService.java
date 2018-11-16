package com.aotfx.mobile.service.nj4x.impl;

import com.aotfx.mobile.dao.entity.ActiveOrderBean;
import com.aotfx.mobile.dao.mapper.ActiveOrderMapper;
import com.aotfx.mobile.service.nj4x.IActiveOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ActiveOrderService  extends ServiceImpl<ActiveOrderMapper, ActiveOrderBean> implements IActiveOrderService {
}
