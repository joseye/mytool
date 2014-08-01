package com.lafengmaker.tool.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.lafengmaker.tool.bean.JdbcBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback =false)
@Transactional
public class DBEditServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	private  DBEditService dbEditService;
	@Test
	public void  testGetAll(){
		JdbcBean jdbcBean=new JdbcBean();
		jdbcBean.setTitle("DEVDB");
		jdbcBean.setName("DEVDB");
		jdbcBean.setTns("(DESCRIPTION=    (ADDRESS=      (PROTOCOL=TCP)    (HOST=10.224.167.211)"+
      "(PORT=1521)    )    (CONNECT_DATA=      (SERVICE_NAME=dbls3)    )  )");
//		jdbcBean.("blis");
//		jdbcBean.setPasswd("blis");
		//dbEditService.insertDbConfig(jdbcBean);
		List<JdbcBean> bls=dbEditService.getallDBConfig();
		System.out.println("blssssssss======================size======"+bls.size());
		for(JdbcBean b:bls){
			System.out.println(b.toString());
		}
		jdbcBean.setId("3");
		dbEditService.deleteDbConfig(jdbcBean);
	}
	@Autowired
	public void setDbEditService(DBEditService dbEditService) {
		this.dbEditService = dbEditService;
	}
	
	
}
