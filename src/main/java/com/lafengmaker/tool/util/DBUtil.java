package com.lafengmaker.tool.util;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.lafengmaker.tool.bean.JdbcBean;

public class DBUtil {
	static Logger logger =Logger.getLogger(DBUtil.class);
	static{
		 try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			logger.error("init driver error", e);
		}  
	}
	  public static SqlSession getSqlSessionByConInDataSource(JdbcBean jdbcBean)  throws Exception {
	      // DataSource ds = DataSourceManager.getInstance().getDataSource(jdbcBean);
		  DataSource ds = new DriverManagerDataSource("jdbc:oracle:thin:@"
	               + jdbcBean.getTns(), jdbcBean.getDbUserName(), jdbcBean.getDbPassword());
	       SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	       factoryBean.setDataSource(ds);
	       ClassPathResource[] res = { new ClassPathResource("map/jdbcbean.xml")};
	       factoryBean.setMapperLocations(res);
	       SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
	       SqlSession ss = sqlSessionFactory.openSession();
	       return ss;
	   }
		public static <k,v> Map<k, v> newInstance(){
			return new HashMap<k, v>();
		}
}
