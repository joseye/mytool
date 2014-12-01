package com.lafengmaker.tool.service;

import java.util.List;
import java.util.Map;

import org.dom4j.Node;

import com.lafengmaker.tool.bean.JdbcBean;

public interface DBEditService {
	public List<JdbcBean> getallDBConfig();
	public Node  sendSoap(String soap,JdbcBean jdbcBean) throws Exception;
	public String updateoppid(String orderid,JdbcBean jdbcBean) throws Exception;
	public String executeSQList(JdbcBean jdbcBean, String sqlist) throws Exception;
	public String loadResultandUpdateStatus(String md5,long load, long count,Map<String,JdbcBean> jdbcmap,String startquery) throws Exception;
}
