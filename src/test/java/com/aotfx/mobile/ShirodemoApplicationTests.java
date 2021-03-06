package com.aotfx.mobile;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.service.nj4x.IMT4AccountService;
import com.aotfx.mobile.service.quartz.IJobAndTriggerService;
import com.aotfx.mobile.service.quartz.UserRoleVoService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Vector;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShirodemoApplicationTests {
    @Autowired
    SqlSessionFactory mybatisPlusAutoConfiguration;

    @Autowired
    private IJobAndTriggerService iJobAndTriggerService;

    @Autowired
    private UserRoleVoService userRoleVoService;

    @Autowired
    private IMT4AccountService imt4UserService;

    @Autowired
    Nj4xConfig nj4xConfig;

    @Test
    public void contextLoads() {
//		// 当前页，总条数 构造 page 对象
//		Page<JobAndTriggerVo> page = new Page<>(1, 10);
//		page.setRecords(service.getJobAndTriggerDetails(page));
//		System.out.println(page.getRecords().get(0).getJobName());

        // 当前页，总条数 构造 page 对象
//		Page<UserRoleVo> page1 = new Page<>(1, 10);
//		page1.setRecords(service1.selectUserListPage(page1));
//		System.out.println(page1.getRecords().get(0));

//        boolean saveResult = imt4UserService.saveOrUpdate(new Mt4Account("3825688", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        System.out.println(saveResult);
        Vector<Mt4Account> v = new Vector<>();
//        v.add(new Mt4Account("3825680", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        v.add(new Mt4Account("3825689", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        v.add(new Mt4Account("3825699", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        System.out.println(imt4UserService.saveOrUpdateBatch(v));
//                            imt4UserService.saveOrUpdate(new Mt4Account("80012391", "real5.ava-mt.com", "Lxtcfx8793", 1, "15708470013"));

//        System.out.println(saveResult);


    }

}
