package com.lafengmaker.tool.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lafengmaker.tool.bean.JdbcBean;
import com.lafengmaker.tool.dao.BaseDao;
import com.lafengmaker.tool.service.DBEditService;
import com.lafengmaker.tool.util.DBUtil;
import com.lafengmaker.tool.util.ProperConfigure;
import com.lafengmaker.tool.util.SoapUtil;
@Service
public class DBEditServiceImpl implements  DBEditService {
	Logger logger=Logger.getLogger(DBEditServiceImpl.class);
	private BaseDao BaseDao;
	public List<JdbcBean> getallDBConfig() {
		logger.info("====pare the config in the db========");
		 List<JdbcBean> beanlist=new ArrayList<JdbcBean>();
		//return this.BaseDao.getList("getAllDBConfig", null);
		ProperConfigure prop=ProperConfigure.getInstance();
		String configlist=prop.getProperty(ProperConfigure.ENVLIST);
		logger.info("configlist:"+configlist);
		String[] envs=configlist.split(",");
		if(envs.length>0){
			for(String env:envs){
				beanlist.add(prop.getJdbcBeanFromConfig(env));
			}
		}
		return beanlist;
	}


	
	public void insertDbConfig(JdbcBean jdbcBean) {
		int i=this.BaseDao.insert(jdbcBean);
		System.out.println(i);
	}

	
	public void deleteDbConfig(JdbcBean jdbcBean) {
			this.BaseDao.delete(jdbcBean);
	}
	
	public String endcompleteOrder(String orderid,JdbcBean jdbcBean) throws Exception{
		Map<String, String>p=new HashMap<String, String>();
		p.put("orderid", orderid);
		String soap=	SoapUtil.getInstance().parseFTL("endcomplete.xml", p);
		logger.info("send end-complete soap of order id:"+orderid);
		return  sendSoap(soap, jdbcBean).asXML();
	}
	
	public Document sendSoap(String soap,JdbcBean jdbcBean)throws Exception {
		logger.info("send Soao:\n"+soap);
		 Document ele=SoapUtil.getInstance().getSoapClient(jdbcBean).sendSOAPMessage(soap);
		 return ele;
	}
	
	public int updateDealidforOrder(String orderid,String oppid,JdbcBean jdbcBean) throws Exception {
		HashMap<String, String>hashMap=new HashMap<String, String>();
		hashMap.put("oppid", oppid);
		hashMap.put("orderid", orderid);
		BaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
		logger.info("update oppid for order id:"+orderid+"to oppid:"+oppid);
		return BaseDao.update("updateOppid", hashMap);
	}


	
	public String getOderStatusAndContractId(String orderid, JdbcBean jdbcBean)
			throws Exception {
		BaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
		@SuppressWarnings("unchecked")
		HashMap<String, String> mp=(HashMap<String, String>)BaseDao.getOne("selectContractAndStatusByOrder", orderid);
		if(null==mp){
			return "no order :"+orderid+"in "+jdbcBean.getName();
		}
		StringBuilder sb=new StringBuilder();
		for(String x:mp.keySet()){
			sb.append(x+":"+mp.get(x).toString());
		}
		return sb.toString();
	}
	@Autowired
	public void setBaseDao(BaseDao baseDao) {
		BaseDao = baseDao;
	}
	
	
}
