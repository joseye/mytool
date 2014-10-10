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
import com.lafengmaker.tool.util.ProperConfigure;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback =false)
@Transactional
public class DBEditServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	private  DBEditService dbEditService;
	//@Test
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
	//@Test
	public void testexecuteSQListAsync(){
		ProperConfigure prop=ProperConfigure.getInstance();
		JdbcBean jdbcBean=prop.getJdbcBeanFromConfig("dev");
		System.out.println(jdbcBean.getTns());
		String sqllist="select count( OBJECT_OID) from HDADMIN.ACCESS_BR_CHRONOLOGY where OBJECT_OID<0;"+
"select count( OWNER_ID) from HDADMIN.ACCESS_BR_CHRONOLOGY where OWNER_ID<0;"+
"select count( OID) from HDADMIN.ACCESS_TABLE where OID<0;"+
"select count( ACCOUNT_ID) from HDADMIN.ACCOUNT where ACCOUNT_ID<0;"+
"select count( OPERATION_ID) from HDADMIN.ACCOUNT_OPERATION where OPERATION_ID<0;"+
"select count( ACCOUNT_ID) from HDADMIN.ADDITIONAL_ACCOUNT_INFO where ACCOUNT_ID<0;"+
"select count( NUMBER_VALUE) from HDADMIN.ADDITIONAL_ACCOUNT_INFO where NUMBER_VALUE<0;"+
		"select count( *) from blis_order";
		try {
			String s=this.dbEditService.executeSQListAsync(jdbcBean, sqllist);
			logger.info(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//@Test
	public void testLoandupdatestatus(){
		try {
			String md5="dd438deae3301bf4cded004f321e1aba";
			String s=dbEditService.loadandUpdateStatus(md5, 0, 0);
			logger.info(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testupdateoppid(){
		ProperConfigure prop=ProperConfigure.getInstance();
		JdbcBean jdbcBean=prop.getJdbcBeanFromConfig("DEV");
		try {
			this.dbEditService.updateoppid("115519", jdbcBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Autowired
	public void setDbEditService(DBEditService dbEditService) {
		this.dbEditService = dbEditService;
	}
	
	
}
