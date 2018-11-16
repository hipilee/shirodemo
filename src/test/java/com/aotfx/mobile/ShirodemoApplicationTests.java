package com.aotfx.mobile;

import com.aotfx.mobile.dao.entity.JobAndTrigger;
import com.aotfx.mobile.dao.entity.UserRoleVo;
import com.aotfx.mobile.service.quartz.IJobAndTriggerService;
import com.aotfx.mobile.service.quartz.UserRoleVoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShirodemoApplicationTests {
//
	@Autowired
	IJobAndTriggerService service;

	@Autowired
	private UserRoleVoService service1;

	@Test
	public void contextLoads() {
		// 当前页，总条数 构造 page 对象
		Page<JobAndTrigger> page = new Page<>(1, 10);
		page.setRecords(service.getJobAndTriggerDetails(page));
		System.out.println(page.getRecords().get(0).getJobName());

		// 当前页，总条数 构造 page 对象
//		Page<UserRoleVo> page1 = new Page<>(1, 10);
//		page1.setRecords(service1.selectUserListPage(page1));
//		System.out.println(page1.getRecords().get(0));
	}

}
