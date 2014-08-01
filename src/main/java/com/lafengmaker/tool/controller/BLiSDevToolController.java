package com.lafengmaker.tool.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.lafengmaker.tool.bean.JdbcBean;
import com.lafengmaker.tool.service.DBEditService;
import com.lafengmaker.tool.util.SessionUtil;

@Controller
public class BLiSDevToolController {
	Logger logger=Logger.getLogger(BLiSDevToolController.class);
	private DBEditService dbEditService;
	@RequestMapping(value="welcome.do")
	public String welcomeHandler(){
		return "index";
	}
	@RequestMapping(value = "getDBInfos.do")
	@ResponseBody
	public String getDblists(HttpServletRequest request, ModelMap modelMap){
		Map<String,JdbcBean> jdbcmap= getMapFromSession(request);
		return JSON.toJSONString(jdbcmap.keySet());
	}
	
	@RequestMapping(value = "updateOpp.do")
	@ResponseBody
	public String updateOppid(HttpServletRequest request, ModelMap modelMap){
		String env=request.getParameter("env");
		String orderid=request.getParameter("orderid");
		String oppid=request.getParameter("oppid");
		try {
			JdbcBean jdbcBean=getEnvInfo(request, env);
			this.dbEditService.updateDealidforOrder(orderid, oppid, jdbcBean);
			return "success";
		} catch (Exception e) {
			logger.error("update error", e);
			return e.getMessage();
		}
	}
	
	@RequestMapping(value = "endcomplete.do")
	@ResponseBody
	public String endcompleteOrder(HttpServletRequest request, ModelMap modelMap){
		String env=request.getParameter("env");
		String orderid=request.getParameter("orderid");
		String finalString=null;
		try {
			JdbcBean jdbcBean=getEnvInfo(request, env);
			finalString= 	this.dbEditService.endcompleteOrder(orderid, jdbcBean);
		} catch (Exception e) {
			logger.error("send soap error", e);
			finalString= e.getMessage();
		}
		logger.info("endcompletefinalString"+finalString);
		return finalString;
		
	}
	@RequestMapping(value = "sendSoap.do")
	@ResponseBody
	public String sendSoap(HttpServletRequest request, ModelMap modelMap){
		String env=request.getParameter("env");
		String soap=request.getParameter("soap");
		try {
			JdbcBean jdbcBean=getEnvInfo(request, env);
			return this.dbEditService.sendSoap(soap, jdbcBean).asXML();
		} catch (Exception e) {
			return e.getMessage();
		}
		
	}
	
	@RequestMapping(value = "getOrdersatus.do")
	@ResponseBody
	public String getOrderstatus(HttpServletRequest request, ModelMap modelMap){
		String env=request.getParameter("env");
		String orderid=request.getParameter("orderid");
		try {
			JdbcBean jdbcBean=getEnvInfo(request, env);
			return dbEditService.getOderStatusAndContractId(orderid, jdbcBean);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return e.getMessage();
		}
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
	
	public  Map<String,JdbcBean> getMapFromSession(HttpServletRequest request){
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
	
	
}
