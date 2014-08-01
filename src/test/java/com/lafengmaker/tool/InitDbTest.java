package com.lafengmaker.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
////@ActiveProfiles("test")
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//@Transactional
//
public class InitDbTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private SqlSession sqlSession;
	@Test
	public void testInitDb(){
		//sqlSession.select(statement, handler);
	try {
		Connection conn=sqlSession.getConnection();
		//pst.execute();
//		String sql="CREATE TABLE jdbcbean(   ID INT PRIMARY KEY      NOT NULL,   title    TEXT NOT NULL,   name     TEXT NOT NULL,     tns      TEXT NOT NULL,   username TEXT NOT NULL,   passwd   TEXT NOT NULL"+
//				 ");";
//		Statement stat=conn.createStatement();
//		stat.executeUpdate(sql);
		PreparedStatement pst=conn.prepareStatement("select * from jdbcbean");
		pst.execute();
		conn.commit();
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
}
