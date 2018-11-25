package com.aotfx.mobile.service.quartz.job;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-25 20:36
 */
public interface SynTimeZoneOffset {
    // 昨日收益
    //计算出mt4服务器的时间，由于周六周天服务器不更新服务器时间，所以在这种情况下需要自己手动计算服务器时间。
//    int offset=mt4Account.getTimeZoneOffset();;
//                try {
//                    offset = mt4c.serverTimeGMTOffset();
//
//                    //当变和GMT视察发生变化是需要更新数据库
//                    if(offset!=mt4Account.getTimeZoneOffset()){
//                        mt4Account.setTimeZoneOffset(offset);
//                        UpdateWrapper updateWrapper = new UpdateWrapper<Mt4Account>().eq("telephone",mt4Account.getTelephone()).eq("user",mt4Account.getUser());
//                        imt4AccountService.update(mt4Account,updateWrapper);
//                    }
//                } catch (MT4RuntimeException e) {
//                    offset = mt4Account.getTimeZoneOffset();
//                }
}
