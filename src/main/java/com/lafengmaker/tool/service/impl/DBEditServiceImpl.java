package com.lafengmaker.tool.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lafengmaker.tool.bean.JdbcBean;
import com.lafengmaker.tool.bean.SqlBean;
import com.lafengmaker.tool.dao.BaseDao;
import com.lafengmaker.tool.service.DBEditService;
import com.lafengmaker.tool.util.DBUtil;
import com.lafengmaker.tool.util.Md5Util;
import com.lafengmaker.tool.util.ProperConfigure;
import com.lafengmaker.tool.util.SoapUtil;
@Service
public class DBEditServiceImpl implements  DBEditService {
	Logger logger=Logger.getLogger(DBEditServiceImpl.class);
	private BaseDao BaseDao;
	private BaseDao remotebaseDao;
	public List<JdbcBean> getallDBConfig() {
		logger.debug("====pare the config in the db========");
		 List<JdbcBean> beanlist=new ArrayList<JdbcBean>();
		ProperConfigure prop=ProperConfigure.getInstance();
		String configlist=prop.getProperty(ProperConfigure.ENVLIST);
		logger.info("configlist:"+configlist);
		String[] envs=configlist.split(",");
		if(envs.length>0){
			for(String env:envs){
				beanlist.add(prop.getJdbcBeanFromConfig(env));
			}
		}
		logger.debug("====pare the config in the db========finish");
		return beanlist;
	}
	
	public Node sendSoap(String soap,JdbcBean jdbcBean) throws Exception {
		logger.info("send soap:\n"+soap);
		Document response= SoapUtil.getInstance().getSoapClient(jdbcBean).sendSOAPMessage(soap);
		logger.info("result soap:\n"+response.asXML());
		return  response.selectSingleNode(".//SOAP:Body");
	}
	public String executeSQList(JdbcBean jdbcBean, String sqlist)throws Exception {
		BaseDao.delete("deletetooOldData");
		String[] sqls=sqlist.split(";");
		int length=sqls.length;
		logger.debug("sql length:"+length);
		String md5=Md5Util.getMD5str(System.currentTimeMillis()+ sqlist);
		for(int i=0;i<length;i++){
			String sql=sqls[i];
			sql=sql.trim();
			logger.info("insert sql"+sql);
			Map<String, Object>map=DBUtil.newInstance();
			map.put("sql", sql);
			map.put("md5", md5);
			map.put("status", 0);
			map.put("seq", (i+1));
			map.put("env", jdbcBean.getName());
			try {
				BaseDao.insert("insertsql", map);
			} catch (Exception e) {
				logger.error("inseret db error", e);
			}
		}
		String returnstr="({ md5:\""+md5+"\",total:\""+length+"\" })";
		logger.info("executeSQList"+returnstr);
		return returnstr;
	}
	
	public void deleteDbConfig(JdbcBean jdbcBean) {
			this.BaseDao.delete("delete",jdbcBean);
	}
	
	private String getStirngByXpath(Node node,String xpath){
		Node n=node.selectSingleNode(xpath);
		if(n!=null){
			return n.getText();
		}
		return null;
	}

	
	public String getOderStatusAndContractId(String orderid, JdbcBean jdbcBean) throws Exception {
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
	@Autowired()
	@Qualifier("baseDao")
	public void setBaseDao(BaseDao baseDao) {
		BaseDao = baseDao;
	}
	
	public String loadResultandUpdateStatus(String md5,long load, long count,Map<String,JdbcBean> jdbcmap,String startquery) throws Exception {
		logger.info("param===================================md5:"+md5+"count:"+count+"load:"+load+"startquery:"+startquery);
		Map<String, String> p=new HashMap<String, String>();
		if("true".equals(startquery)){
			List<SqlBean>dbl=this.BaseDao.getList("selectSql", md5);
			logger.info(dbl.size()+"db sql length");
			if(dbl.size()>0){
				DataStack ds =new DataStack(dbl);
				SqlBean tmp=ds.getOne();
				QueryProcesser qp=new QueryProcesser(ds, jdbcmap.get(tmp.getEnv()));
				int thread=dbl.size()/5+1;
				for(int i=0;i<thread;i++){
					new Thread(qp).start();
					if(i>5){
						break;
					}
				}
			}
		}
		p.put("startquery", "false");
//		if(count==0){
//			count=(Long)this.BaseDao.getOne("selectSqlCount",md5);
//			if(count==0){
//				p.put("flag", "wait");	
//				return JSON.toJSONString(p);
//			}
//		}
		if(load==count){
			p.put("flag", "false");	
		}else{
			p.put("flag", "true");
			p.put("count", count+"");
			List<Map<String, Object>>rl=this.BaseDao.getList("selectdoneSql", md5);
			if(rl.size()>0){
				load+=rl.size();
				p.put("list", JSON.toJSONString(rl));
				p.put("load", load+"");
				for(Map<String, Object> m:rl){
					Integer id=(Integer)m.get("id");
					BaseDao.update("updateLoadSql", id);
				}
			}
	}
	logger.info("loadandUpdateStatus==============return======================="+p.toString());
	return JSON.toJSONString(p);
	}

	public String updateoppid(String orderid, JdbcBean jdbcBean) throws Exception {
		initRemotebaseDao(jdbcBean);
		String action=orderid.split(":")[0];
		String oid=orderid.split(":")[1];
		logger.info("action:"+action+" oppid for:"+oid+":=="+"env"+jdbcBean.getName());
		Map<String, String>p=DBUtil.newInstance();
		p.put("orderid", oid.trim());
		if("oppidupdate".equals(action))
			this.remotebaseDao.update("updateoppid",p);
		if("oppidclear".equals(action))
			this.remotebaseDao.update("clearoppid",p);
		return "update oppid for"+orderid+" success.";
	}

	private void initRemotebaseDao(JdbcBean jdbcBean) throws Exception {
		logger.info("init remote");
		remotebaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
		logger.info("init remote finished");
	}
	@Autowired
	@Qualifier("remotebaseDao")
	public void setRemotebaseDao(BaseDao remotebaseDao) {
		this.remotebaseDao = remotebaseDao;
	}
	
	class DataStack{
		private List<SqlBean> datalist;
		private int index;
		public DataStack(List<SqlBean> datalist) {
			super();
			this.datalist = datalist;
			this.index=datalist.size();
		}
		public synchronized SqlBean popSql(){
			logger.info("index====="+index);
			if(index==0){
				return null;
			}
			index--;
			return datalist.get(index);
		}
		public  SqlBean getOne(){
			return datalist.get(0);
		}
		
	}
	class QueryProcesser implements Runnable{
		private DataStack dataStack;
		private JdbcBean jdbcBean;
		public QueryProcesser(DataStack dataStack,JdbcBean jdbcBean) {
			super();
			this.dataStack = dataStack;
			this.jdbcBean=jdbcBean;
		}
		
		public void run() {
			logger.info( Thread.currentThread().getName()+"start excutesql");
			try {
				Thread.sleep(2000);
				initRemotebaseDao(jdbcBean);
			} catch ( Throwable e1) {
				logger.error("init connection", e1);
			}
			logger.info( Thread.currentThread().getName()+"after init remote");
			SqlBean sb=null;
			while (( sb=dataStack.popSql())!=null) {
				try {
					String sql=sb.getSql();
					sb.setCost(0);
					sql=sql.toLowerCase();
					if(sql.indexOf("select")>-1 ){
						try {
							Long start=System.currentTimeMillis();
							logger.info("start"+sql);
							List<Object>ll = (List<Object>)remotebaseDao.getList("execustomizeSQL", sql);
							if(null==ll||ll.size()==0){
								sb.setResult(" no row return ");
							}
							Long end=System.currentTimeMillis();
							Long x=end-start;
							sb.setCost(x.intValue());
							
							sb.setResult(JSON.toJSONString(ll));
						} catch (Exception e) {
							logger.error("quert error", e);
							sb.setResult(getTrackerror(e.getMessage()));
						}
					}else{
						sb.setResult(" only can execute select");
					}
					BaseDao.update("updateSql",sb);
				} catch (Exception e) {
					logger.error("unknow error", e);
				}
			}
			logger.info( Thread.currentThread().getName()+"finish this thread execute sql");
		}
	}
	private String getTrackerror(String message){
		String arr[]=message.split("###");
		if(arr.length>2){
			for(String temp:arr){
				if(temp!=null&& temp.indexOf("Cause")>-1){
					message=temp.replace("\n", "");
					message=message.replace("\r", "");
					break;
				}
			}
		}
		if(message.length()>80){
			message=message.substring(67, message.length());
			message=message.replace("\r", "");
			message=message.replace("\n", "");
		}
		return message;
	}
	
}
