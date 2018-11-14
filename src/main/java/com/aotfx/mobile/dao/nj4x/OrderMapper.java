package com.aotfx.mobile.dao.nj4x;

import com.aotfx.mobile.beans.ActiveOrderBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int insertActiveOrdersBatch(List<ActiveOrderBean> ActiveOrdersList);

    int deleteActiveOrdersByUser(@Param("user") String user);

    int deleteOrders(@Param(("user"))String user,@Param("list") List<ActiveOrderBean> ActiveOrdersList);
}
