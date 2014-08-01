/*
 * Created on 2005-1-31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lafengmaker.tool.util;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.lafengmaker.tool.bean.JdbcBean;




/**
 * @author peihe
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProperConfigure {
	public static final String GATEWAYURL_PREFIX="gatewayurl";
	public static final String AUTHURL_PREFIX="authurl";
	public static final String USERNAME_PREFIX="username";
	public static final String PASSWORD_PREFIX="password";
	public static final String DB_USERNAME_PREFIX="db_username";
	public static final String DB_PASSWORD_PREFIX="db_password";
	public static final String DB_TNS_PREFIX="db_tns";
	public static final String ENVLIST="envlist";
	public static final String TIMEOUT="timeout";
	public static final String DEFAULTENCODING="defaultEncoding";
    private Properties prop = new Properties();
    private static ProperConfigure instance = new ProperConfigure();
    
    private ProperConfigure() {
		init();
	}

	public static ProperConfigure getInstance() {
		return instance;
	}
	private String appendEnv(String prefix,String env){
		return prefix+"_"+env.toLowerCase();
	}
	public JdbcBean getJdbcBeanFromConfig(String env){
		JdbcBean jdbcBean =new JdbcBean();
		jdbcBean.setAuthUrl(prop.getProperty(appendEnv(ProperConfigure.AUTHURL_PREFIX,env)));
		jdbcBean.setDbPassword(prop.getProperty(appendEnv(ProperConfigure.DB_PASSWORD_PREFIX,env)));
		jdbcBean.setDbUserName(prop.getProperty(appendEnv(ProperConfigure.DB_USERNAME_PREFIX,env)));
		jdbcBean.setName(env);
		jdbcBean.setSoapPasswd(prop.getProperty(appendEnv(ProperConfigure.PASSWORD_PREFIX,env)));
		jdbcBean.setSoapUsername(prop.getProperty(appendEnv(ProperConfigure.USERNAME_PREFIX,env)));
		jdbcBean.setTns(prop.getProperty(appendEnv(ProperConfigure.DB_TNS_PREFIX,env)));
		jdbcBean.setWcpUrl(prop.getProperty(appendEnv(ProperConfigure.GATEWAYURL_PREFIX,env)));
		return jdbcBean;
	}
    private void init() {
		try {
			prop.load(getClass().getResourceAsStream("/env_db_cordys.properties"));
			PropertyConfigurator.configure(getClass().getResource("/env_db_cordys.properties"));
		} catch (Throwable e) {
			System.err.println("Error loading properties:"+e.toString());
		}
	}
    
    public String getProperty(String key){
		return prop.getProperty(key);
	}

}
