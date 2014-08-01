package com.lafengmaker.tool.util;

import java.util.Hashtable;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.lafengmaker.tool.bean.JdbcBean;

public class DataSourceManager {
	private Hashtable<String, DataSource> pools;
	private static DataSourceManager manager = new DataSourceManager();
	
	private  DataSourceManager() {
		pools=new Hashtable<String, DataSource>();
	}
	public static DataSourceManager getInstance(){
		return manager;
	}
	public DataSource getDataSource(JdbcBean jdbcBean) throws Exception {
		DataSource ds = (DataSource) pools.get(jdbcBean.getName());
		if (ds == null)
			return setDataSource(jdbcBean);
		else
			return ds;
	}

	private DataSource setDataSource(JdbcBean jdbcBean) throws Exception {
		if (jdbcBean == null ||jdbcBean.getName()==null)
			throw new Exception("DataSource Key Name is missing.");
		DataSource ds = getDBCPPoolingDataSource(jdbcBean);
		if (ds == null) {
			throw new Exception("DataSource is null.");
		} else {
			return ds;
		}
	}
	private DataSource getDBCPPoolingDataSource(JdbcBean jdbcBean) throws Exception {
		String ping_query ="select sysdate from dual";
		BasicDataSource ds = new BasicDataSource();
		ds.setUrl(jdbcBean.generateOracleURL());
		ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		ds.setMaxActive(2);
		ds.setMaxWait(5000);
		ds.setUsername(jdbcBean.getDbUserName());
		ds.setPassword(jdbcBean.getDbPassword());
		if (ping_query != null)
			ds.setValidationQuery(ping_query);
		org.apache.commons.pool.ObjectPool connectionPool = new GenericObjectPool(	null);
		org.apache.commons.dbcp.ConnectionFactory connectionFactory = new DataSourceConnectionFactory(				ds);
		new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		pools.put(jdbcBean.getName(), dataSource);
		return dataSource;
	}
}
