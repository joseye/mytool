package com.lafengmaker.tool.service;

import java.util.List;

import org.dom4j.Document;

import com.lafengmaker.tool.bean.JdbcBean;

public interface DBEditService {
	public List<JdbcBean> getallDBConfig();
	public void insertDbConfig(JdbcBean jdbcBean);
	public void deleteDbConfig(JdbcBean jdbcBean);
	public String endcompleteOrder(String orderid,JdbcBean jdbcBean) throws Exception;
	public Document sendSoap(String soap,JdbcBean jdbcBean) throws Exception;
	public String executeSQList(JdbcBean jdbcBean,String sqlist) throws Exception;
	
	public String acceptOrder(String orderid,String oppid,JdbcBean jdbcBean) throws Exception;
	
	public String getOderStatusAndContractId(String orderid,JdbcBean jdbcBean) throws Exception;
	
	public String executeSQListAsync(JdbcBean jdbcBean,String sqlist) throws Exception;
	
	public String loadandUpdateStatus(String md5,long load, long count) throws Exception;
	
	public String updateoppid(String orderid,JdbcBean jdbcBean) throws Exception;
}
