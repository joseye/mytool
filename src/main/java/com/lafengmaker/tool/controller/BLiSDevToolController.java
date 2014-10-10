package com.lafengmaker.tool.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.DOMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.lafengmaker.tool.bean.JdbcBean;
import com.lafengmaker.tool.service.DBEditService;
import com.lafengmaker.tool.util.SessionUtil;

@Controller
public class BLiSDevToolController {
	Logger logger=Logger.getLogger(BLiSDevToolController.class);
	private DBEditService dbEditService;
	@RequestMapping(value="welcome.do")
	public String welcomeHandler(HttpServletRequest request){
		StringBuilder sb =new StringBuilder();
		sb.append("ip:"+getIpAddr(request));
		sb.append("host"+request.getRemoteHost());
		sb.append("user"+request.getRemoteUser());
		System.out.println(sb.toString());
		logger.info("logininfo"+sb.toString());
		request.setAttribute("message", sb.toString());
		return "index";
	}
	
	@RequestMapping(value = "sendSoap.do")
	@ResponseBody
	public String sendSoap(HttpServletRequest request, ModelMap modelMap){
		String env=getParameter(request,"env");
		String soap=getParameter(request,"soap");
		try {
			JdbcBean jdbcBean=getEnvInfo(request, env);
			if(soap.indexOf("SOAP:Envelope xmlns:SOAP")>-1){
				Document  result= this.dbEditService.sendSoap(soap, jdbcBean);
				TransformerFactory tf = TransformerFactory.newInstance();  
				Transformer transformer = tf.newTransformer();  
				StringWriter sw= new StringWriter();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				StreamResult xmlResult = new StreamResult(sw);  
				org.w3c.dom.Document w3cDoc = new DOMWriter().write(result);
				transformer.transform(new DOMSource(w3cDoc), xmlResult);  
				System.out.println("xxxx\n"+sw.toString());
				return sw.toString();
			}else{
				return this.dbEditService.updateoppid(soap, jdbcBean);
			}
		} catch (Exception e) {
			logger.error("send error", e);
			return e.getMessage();
		}
	}
	
	@RequestMapping(value = "getDBInfos.do")
	@ResponseBody
	public String getDblists(HttpServletRequest request, ModelMap modelMap){
		Map<String,JdbcBean> jdbcmap= getMapFromSession(request);
		return JSON.toJSONString(jdbcmap.keySet());
	}
	
	@RequestMapping(value = "executeQuery.do")
	@ResponseBody
	public String executeQuery(HttpServletRequest request, ModelMap modelMap){
		String env=getParameter(request,"env");
		String sqls=getParameter(request,"sqls");
		try {
			JdbcBean jdbcBean=getEnvInfo(request, env);
			//return dbEditService.executeSQList(jdbcBean, sqls);
			return this.dbEditService.executeSQListAsync(jdbcBean, sqls);
		} catch (Exception e) {
			logger.error("executeQuery", e);
			return e.getMessage();
		}
	}
	@RequestMapping(value = "loadData.do")
	@ResponseBody
	public String loadData(HttpServletRequest request, ModelMap modelMap){
		String md5=getParameter(request,"md5");
		String load=getParameter(request,"load");
		String count=getParameter(request,"count");
		try {
			return dbEditService.loadandUpdateStatus(md5, getLong(load), getLong(count));
		} catch (Exception e) {
			logger.error("executeQuery", e);
			return e.getMessage();
		}
	}
	@RequestMapping(value = "uploadFile.do")
	@ResponseBody
	public String uploadFile(HttpServletRequest request, ModelMap modelMap,@RequestParam MultipartFile file){
		InputStream in= null;
		try {
			in=file.getInputStream();
			BufferedReader br =new BufferedReader(new InputStreamReader(in));
			String s=null;
			StringBuilder sb =new StringBuilder();
			int total=0;
			while ((s=br.readLine())!=null) {
				if(null!=s &&!("".equals(s.trim()))){
					total+=1;
					sb.append(s);
					sb.append("\\r\\n");
				}
			}
			System.out.println("({content:\""+sb.toString()+"\",total:\""+total+"\"})");
			return "({content:\""+sb.toString()+"\",total:\""+total+"\"})";
		} catch (Exception e) {
			logger.error("executeQuery", e);
			return e.getMessage();
		}
	}
	
	private Long getLong(String s){
		if(null==s)
			return 0l;
		try {
			return Long.parseLong(s);
		} catch (Exception e) {
			return 0l;
		}
		
	}
//	@RequestMapping(value = "updateOpp.do")
//	@ResponseBody
//	public String updateOppid(HttpServletRequest request, ModelMap modelMap){
//		String env=getParameter(request,"env");
//		String orderid=getParameter(request,"orderid");
//		String oppid=getParameter(request,"oppid");
//		try {
//			JdbcBean jdbcBean=getEnvInfo(request, env);
//			String result=this.dbEditService.acceptOrder(orderid, oppid, jdbcBean);
//			return result;
//		} catch (Exception e) {
//			logger.error("accept order  error", e);
//			return e.getMessage();
//		}
//	}

//	@RequestMapping(value = "endcomplete.do")
//	@ResponseBody
//	public String endcompleteOrder(HttpServletRequest request, ModelMap modelMap){
//		String env=getParameter(request,"env");
//		String orderid=getParameter(request,"orderid");
//		String finalString=null;
//		try {
//			JdbcBean jdbcBean=getEnvInfo(request, env);
//			finalString= 	this.dbEditService.endcompleteOrder(orderid, jdbcBean);
//		} catch (Exception e) {
//			logger.error("send soap error", e);
//			finalString= e.getMessage();
//		}
//		logger.info("endcompletefinalString"+finalString);
//		return finalString;
//		
//	}
	
//	@RequestMapping(value = "getOrdersatus.do")
//	@ResponseBody
//	public String getOrderstatus(HttpServletRequest request, ModelMap modelMap){
//		String env=request.getParameter("env");
//		String orderid=request.getParameter("orderid");
//		try {
//			JdbcBean jdbcBean=getEnvInfo(request, env);
//			return dbEditService.getOderStatusAndContractId(orderid, jdbcBean);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return e.getMessage();
//		}
//	}
	private String getParameter(HttpServletRequest request,String pname){
		String pp=request.getParameter(pname);
		if(null!=pp){
			return pp.trim();
		}
		return pp;
	}
	private void initSessioninfo(HttpServletRequest request,Map<String,JdbcBean> jdbcmap){
		List<JdbcBean> al=new ArrayList<JdbcBean>();
		try {
			al = this.dbEditService.getallDBConfig();
		} catch (Exception e) {
			logger.error("parese from config error", e);
		}
		
		if(al.size()>0){
			for(JdbcBean bean:al){
				jdbcmap.put(bean.getName(), bean);
			}
		}
		SessionUtil.addObjtoSeesion(request, SessionUtil.SESSIONKEY_DBLIST, jdbcmap);
	}
	private JdbcBean getEnvInfo(HttpServletRequest request,String env) throws Exception{
		Map<String,JdbcBean> jdbcmap=getMapFromSession(request);
		JdbcBean  jdbcBean=jdbcmap.get(env);
		if(null==jdbcBean){
			throw new Exception( "env info not found"+env);
		}
		return jdbcBean;
	}
	
	private   Map<String,JdbcBean> getMapFromSession(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		Map<String,JdbcBean> jdbcmap=(Map<String,JdbcBean>)request.getSession().getAttribute(SessionUtil.SESSIONKEY_DBLIST);
		if(null==jdbcmap){
			 jdbcmap=new HashMap<String, JdbcBean>();
			initSessioninfo(request,jdbcmap);
		}
		return jdbcmap;
	}
	@Autowired
	public void setDbEditService(DBEditService dbEditService) {
		this.dbEditService = dbEditService;
	}

	private  String getIpAddr(HttpServletRequest request) {
       String ip = request.getHeader("x-forwarded-for");
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
       }
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
       }
       return ip;
   } 

	
}
