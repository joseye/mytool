package com.lafengmaker.tool.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lafengmaker.tool.bean.JdbcBean;
import com.lafengmaker.tool.bean.OrderBean;
import com.lafengmaker.tool.constants.OrderConstants;
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
		logger.info("====pare the config in the db========");
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
		logger.info("====pare the config in the db========finish");
		return beanlist;
	}
	
	public String executeSQList(JdbcBean jdbcBean, String sqlist)
			throws Exception {
		logger.info("start excutesql");
		String[] sqls=sqlist.split(";");
		int length=sqls.length;
		logger.debug("sql length:"+length);
		StringBuilder sb=new StringBuilder("{list:[");
		BaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
		for(int i=0;i<length;i++){
			if(i>0){
				sb.append(",");
			}
			sb.append("{index:"+(i+1));
			String sql=sqls[i];
			logger.info("query:"+sql);
			long start=System.currentTimeMillis();
			if(sql!=null && !"".equals(sql.trim())){
				sql=sql.trim().replace("\n", "");
				String tmp=sql;
				if(sql.length()>60){
					tmp=sql.substring(0, 60);
				}
				sb.append(",sql:\""+tmp+"\",result:\"");
				
				try {
					if(!("select".equals(sql.trim().substring(0,6)))){
						throw new RuntimeException("only can run query sql here");
					}
					HashMap<String, String> mp=(HashMap<String, String>)BaseDao.getOne("execustomizeSQL", sql);
					if(null==mp){
						throw new RuntimeException("No Data Return");
					}
					String mm=mp.toString();
					mm=mm.replace("{", "");
					mm=mm.replace("}", "");
					if(mp.keySet().size()==1){
						mm=mm.split("=")[1];
						if(Integer.parseInt(mm)>0){
							mm=mm+"\",color:\"red";
						}
					}
					sb.append(mm+"\"");
				} catch (Exception e) {
					e.printStackTrace();
					String msg=e.getMessage();
					String arr[]=msg.split("###");
					if(arr.length>2){
						for(String temp:arr){
							if(temp!=null&& temp.indexOf("Cause")>-1){
								msg=temp.replace("\n", "");
								msg=msg.replace("\r", "");
								break;
							}
						}
					}
					if(msg.length()>80){
						msg=msg.substring(66, msg.length());
					}
					sb.append(msg+"\"");
				}
			}
			long end=System.currentTimeMillis();
			sb.append(",cost:\""+(end-start)+" ms\"");
			sb.append("}");
		}
		sb.append("]}");
		logger.info("result"+sb.toString());
		return sb.toString();
	}



	public void insertDbConfig(JdbcBean jdbcBean) {
		int i=this.BaseDao.insert(jdbcBean);
		System.out.println(i);
	}

	
	public void deleteDbConfig(JdbcBean jdbcBean) {
			this.BaseDao.delete("delete",jdbcBean);
	}
	
	public String endcompleteOrder(String orderid,JdbcBean jdbcBean) throws Exception{
		Map<String, String>p=new HashMap<String, String>();
		p.put("orderid", orderid);
		String soap=	SoapUtil.getInstance().parseFTL("endcomplete.xml", p);
		logger.info("send end-complete soap of order id:"+orderid);
		return  sendSoap(soap, jdbcBean).asXML();
	}
	private void  envcheck(JdbcBean jdbcBean) throws Exception{
		if(!("QA".equals(jdbcBean.getName())||"DEV".equals(jdbcBean.getName()))){
			throw new RuntimeException("This tool can only be userd in QA and DEV.");
		}
	}
	public Document sendSoap(String soap,JdbcBean jdbcBean)throws Exception {
		envcheck(jdbcBean);
		logger.info("send soap:\n"+soap);
		Document response= SoapUtil.getInstance().getSoapClient(jdbcBean).sendSOAPMessage(soap);
		logger.info("result soap:\n"+response.asXML());
		if(soap.indexOf("InsertPortfolio")>-1){
			String failstr=getFailStr(response);
			Pattern p=Pattern.compile("<OrderID>\\d+</OrderID>"); 
			Matcher m=p.matcher(soap);
			String orderid=null;
			while(m.find()){
				Pattern p1=Pattern.compile("\\d+"); 
				Matcher m1=p1.matcher(m.group());
				if(m1.find())
					orderid=m1.group();
			}
			endcompleteHelper(orderid,failstr,jdbcBean);
			System.out.println("=================="+failstr);
		}
		return response;
	}
	
	private Document endcompleteHelper(String orderid,String failstr,JdbcBean jdbcBean) throws Exception{
		if(null==failstr)
			return null;
		if(orderid==null)
			throw new Exception("ca n't got orderid from soap.");
		BaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
		HashMap<String, String> mp=(HashMap<String, String>)BaseDao.getOne("selectContractAndStatusByOrder", orderid);
		if(null==mp){
			logger.error("No orderid:    "+orderid+" found  in  "+jdbcBean.getName()+"   DB,"+jdbcBean.getTns());
			throw new RuntimeException("No orderid:    "+orderid+" found  in  "+jdbcBean.getName()+"   DB,");
		}
		String status=mp.get("STATUS");
		if(!OrderConstants.ORDER_STATUS_ACCEPTED.equals(status)){
			throw new Exception("Order status is "+status+" not  accepted.");
		}
		return null;
	}
	private String getFailStr(Document response){
		Node node=response.selectSingleNode(".//SOAP:Fault");
		String result=null;
		if(node!=null){
			Node n=node.selectSingleNode("./SOAP:faultstring");
			result=n.getText();
		}
		return result;
	}
	private  String loandciscoorderid(String orderid,JdbcBean jdbcBean) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orderId", orderid);
		String loadorderid=	SoapUtil.getInstance().parseFTL("getCiscoOrderIdByOrderId.ftl", map);
		Document responseorder=sendSoap(loadorderid, jdbcBean);
		Node temp=responseorder.selectSingleNode(".//CISCOORDERID");
		String code=null;
		if(temp!=null){
			code=temp.getText();
		}
		return code;
		
	}
	private OrderBean getOrderBean(String orderid,JdbcBean jdbcBean) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orderID", orderid);
		String getOrderInfoByID=	SoapUtil.getInstance().parseFTL("getOrderInfoByID.ftl", map);
		Document responseorder=sendSoap(getOrderInfoByID, jdbcBean);
		OrderBean orderBean =new OrderBean();
		 Node node=responseorder.getRootElement();
		orderBean.setOrdertype(getStirngByXpath(node, ".//ORDERTYPEID"));
		orderBean.setStatus(getStirngByXpath(node, ".//STATUS"));
		return orderBean;
	}
	private String getStirngByXpath(Node node,String xpath){
		Node n=node.selectSingleNode(xpath);
		if(n!=null){
			return n.getText();
		}
		return null;
	}
	private boolean loadCiscoOrderNumber(String orderid,JdbcBean jdbcBean) throws Exception {
		boolean result= false;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("entityId", "5");
		map.put("entityKeyId", orderid);
		String showcisconumber=	SoapUtil.getInstance().parseFTL("showCiscoOrderNumber.ftl", map);
		Document response=sendSoap(showcisconumber, jdbcBean);
		Node tempNode=response.selectSingleNode(".//showCiscoOrderNumber");
		if(tempNode!=null){
			result=Boolean.valueOf(tempNode.getText()).booleanValue();
		}
		return  result;
	}
	private String  acceptOrder(String orderid,JdbcBean jdbcBean) throws Exception{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("currentUser", "1001");
		map.put("orderid", orderid);
		String showcisconumber=	SoapUtil.getInstance().parseFTL("acceptOrder.ftl", map);
		Document response=sendSoap(showcisconumber, jdbcBean);
		return response.asXML();
		
	}
	public String acceptOrder(String orderid,String oppid,JdbcBean jdbcBean) throws Exception {
		BaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
		OrderBean orderBean=getOrderBean(orderid, jdbcBean);
		boolean isshow=loadCiscoOrderNumber(orderid, jdbcBean);
		if(!"New".equalsIgnoreCase(orderBean.getStatus())&&!"New-resubmit".equals(orderBean.getStatus())){
			throw new Exception("Order status not new or new-resubmit");
		}
		// TODO
		//orderBean.getFinApprove()
		
		//checkDuplicateOrgName
		
		String ordertype=orderBean.getOrdertype();
		logger.info("show cisco order number"+isshow+"order type"+orderBean.getOrdertype());
		//1==orderTypeId||2==orderTypeId||3==orderTypeId||5==orderTypeId
		//check cisco number
		if(isshow &&("1".equals(ordertype)||"2".equals(ordertype)||"3".equals(ordertype)||"5".equals(ordertype))){//show cisconumber
			String code=loandciscoorderid(orderid, jdbcBean);
			if(null==code){
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("orderId", orderid);
				logger.info("update ciscoorderid for order id:"+orderid);
				BaseDao.update("updateciscobyorderid", hashMap);
				code=loandciscoorderid(orderid, jdbcBean);
				if(null==code){
						throw new Exception("update cisco error");
				}
			}
		}
		HashMap<String, String>hashMap=new HashMap<String, String>();
		hashMap.put("oppid", oppid);
		hashMap.put("orderid", orderid);
		BaseDao.update("updateNamescheckbyorderid", hashMap);
		BaseDao.update("updateContractFlagkbyorderid", hashMap);
		logger.info("update oppid for order id:"+orderid+"to oppid:"+oppid);
		 BaseDao.update("updateoppid", orderid);
		 
		return acceptOrder(orderid, jdbcBean);
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
	@Autowired()
	@Qualifier("baseDao")
	public void setBaseDao(BaseDao baseDao) {
		BaseDao = baseDao;
	}
	
	public String executeSQListAsync(JdbcBean jdbcBean, String sqlist)
			throws Exception {
		logger.info("start excutesql");
		String md5ss=System.currentTimeMillis()+ sqlist;
		String md5=Md5Util.getMD5str(md5ss);
		String[] sqls=sqlist.split(";");
		int length=sqls.length;
		logger.info("sql list length:"+length);
		if(length>0){
			Async t=new Async();
			t.sqls=sqls;
			t.md5=md5;
			t.jdbcBean=jdbcBean;
			t.start();
		}
		String returnstr="({ md5:\""+md5+"\",total:\""+length+"\" })";
		logger.info("executeSQListAsync"+returnstr);
		return returnstr;
	}
	
	public String loadandUpdateStatus(String md5,long load, long count) throws Exception {
		logger.info("md5:"+md5+"count:"+count+"load:"+load);
		StringBuilder sb =new StringBuilder("({flag:");
		if(count==0){
				count=(Long)this.BaseDao.getOne("selectSqlCount",md5);
				if(count==0){
					Thread.sleep(5000l);
					return sb.append("\"wait\"})").toString();
				 }
		}
		if(load==count){
				sb.append("\"false\"})");
		}else{
				sb.append("\"true\",");
				sb.append("count:"+count);
				List<Map<String, Object>>rl=this.BaseDao.getList("selectdoneSql", md5);
				if(rl.size()>0){
						load+=rl.size();
						sb.append(",list:[");
						int i=0;
						for(Map<String, Object> m:rl){
								if(i!=0){
										sb.append(",");
								}
								i++;
								sb.append("{ sql:\"");
								sb.append((String)m.get("sql"));
								sb.append("\"");
								
								sb.append(",cost:"+(Integer)m.get("cost"));
								sb.append(",result:\""+(String)m.get("result")+"\"");
								sb.append(",color:\""+(String)m.get("color")+"\"");
								sb.append(",index:\""+(Integer)m.get("seq")+"\"}");
								Integer id=(Integer)m.get("id");
								BaseDao.update("updateLoadSql", id);
						}
						sb.append("]");
				}
				sb.append(",load:"+load);
				sb.append("})");
	}
		logger.info("loadandUpdateStatus"+sb.toString());
	return sb.toString();
	}

	public String updateoppid(String orderid, JdbcBean jdbcBean) throws Exception {
		envcheck(jdbcBean);
		initRemotebaseDao(jdbcBean);
		String oid=orderid.split(":")[1];
		logger.info("update oppid for:"+oid+":==");
		Map<String, String>p=DBUtil.newInstance();
		p.put("orderid", oid.trim());
		this.remotebaseDao.update("updateoppid",p);
		return "update oppid for"+orderid+" success.";
	}

	private void initRemotebaseDao(JdbcBean jdbcBean) throws Exception {
		remotebaseDao.setSqLSession(DBUtil.getSqlSessionByConInDataSource(jdbcBean));
	}
	@Autowired
	@Qualifier("remotebaseDao")
	public void setRemotebaseDao(BaseDao remotebaseDao) {
		this.remotebaseDao = remotebaseDao;
	}

	class Async extends  Thread{
		public String[] sqls;
		public String md5;
		public JdbcBean jdbcBean;
		public void run() {
			try {
				logger.info("start to Run in thread");
				insertSQlToDB();
				//md5="60d2f4fe0275d790764f40abc6734499";
				executeSQLQuery();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("deal with db error", e);
			}
		}
		private void insertSQlToDB() throws Exception{
			BaseDao.delete("deletetooOldData");
			logger.info("after delete");
			for(int i=0;i<sqls.length;i++){
				String sql=sqls[i];
				if(null==sql||"".equals(sql.trim())){
					logger.warn("The "+i+"sql is empty,");
				}
				sql=sql.trim().replace("\n", "");
				logger.info("insert sql"+sql);
				Map<String, Object>map=DBUtil.newInstance();
				map.put("sql", sql);
				map.put("md5", md5);
				map.put("status", 0);
				map.put("seq", (i+1));
				try {
					BaseDao.insert("insertsql", map);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("inseret db error", e);
					sleep(1000l);
				}
			}
			
		}
		private void executeSQLQuery() throws Exception{
			initRemotebaseDao(jdbcBean);
			Map<String,Object> m=DBUtil.newInstance();
			m.put("md5", md5);
			List<Map<String, Object>> result=BaseDao.getList("selectSql", m);
			if(null==result||Collections.EMPTY_LIST.equals(result)){
				logger.info("finish execute all sql.");
				return ;
			}
			for(Map<String, Object>row:result){
				long start=System.currentTimeMillis();
				logger.info("row"+row);
				Integer id=(Integer) row.get("id");
				String sql=(String)row.get("sql");
				String resultstr=null;
				String color=null;
				Map<String, Object> resremote=null;
				try {
					resremote = (Map<String, Object>)remotebaseDao.getOne("execustomizeSQL", sql);
					resultstr=resremote.toString();
					resultstr=resultstr.replace("{", "");
					resultstr=resultstr.replace("}", "");
				} catch (Exception e) {
					String msg=e.getMessage();
					String arr[]=msg.split("###");
					if(arr.length>2){
						for(String temp:arr){
							if(temp!=null&& temp.indexOf("Cause")>-1){
								msg=temp.replace("\n", "");
								resultstr=msg.replace("\r", "");
								color="red";
								break;
							}
						}
					}
					if(msg.length()>80){
						resultstr=msg.substring(67, msg.length());
						resultstr=resultstr.replace("\r", "");
						resultstr=resultstr.replace("\n", "");
					}
				}
				if(null!=resremote&&resremote.keySet().size()==1){
					resultstr=resultstr.split("=")[1];
					if(Integer.parseInt(resultstr)>0){
						color="red";
					}
				}
				long end=System.currentTimeMillis();
				long cost=end-start;
				Map<String, Object>p=DBUtil.newInstance();
				p.put("color", color);
				p.put("id", id);
				p.put("cost", cost);
				p.put("result", resultstr);
				BaseDao.update("updateSql", p);
			}
			
			
	}
}

	
}
