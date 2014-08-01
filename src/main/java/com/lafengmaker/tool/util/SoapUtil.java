package com.lafengmaker.tool.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.dom4j.Document;

import com.lafengmaker.tool.bean.JdbcBean;
import com.lafengmaker.tool.soap.BLiSAuthenticator;
import com.lafengmaker.tool.soap.BLiSSOAPClient;
import com.lafengmaker.tool.soap.BLiSSOAPException;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SoapUtil {
	private static SoapUtil soapUtil=null;
	private static Template template=null;
	private  SoapUtil() {
	}
	public static SoapUtil getInstance(){
		if(soapUtil==null){
			soapUtil=new SoapUtil();
		}
		return soapUtil;
	}
	public  BLiSSOAPClient getSoapClient(JdbcBean jdbcBean) throws Exception{
		ProperConfigure prop=ProperConfigure.getInstance();
		BLiSAuthenticator authenticator=new BLiSAuthenticator();
		authenticator.setAuthurl(jdbcBean.getAuthUrl());
		authenticator.setDefaultEncoding(prop.getProperty(ProperConfigure.DEFAULTENCODING));
		authenticator.setPassword(jdbcBean.getSoapPasswd());
		authenticator.setUsername(jdbcBean.getSoapUsername());
		BLiSSOAPClient client=new BLiSSOAPClient();
		client.setAuthenticator(authenticator);
		client.setDefaultEncoding(prop.getProperty(ProperConfigure.DEFAULTENCODING));
		client.setGatewayurl(jdbcBean.getWcpUrl());
		client.setTimeout(Long.parseLong(prop.getProperty(ProperConfigure.TIMEOUT)));
		return client;
	}
	public String parseFTL(String filename,Map< String, String >p) throws Exception{
		ProperConfigure prop=ProperConfigure.getInstance();
		StringWriter writer = new StringWriter();
		 getTemplate(filename, Locale.getDefault(), prop.getProperty(ProperConfigure.DEFAULTENCODING)).process(p, writer);
		 return writer.toString();
	}
	private Template getTemplate(final String filename, final Locale locale, final String encode)	throws IOException, TemplateException{
			if(null!=template){
				return template;
			}
		     Configuration config=new Configuration();
		     TemplateLoader loader=new ClassTemplateLoader(this.getClass(),"/soap");		
		     config.setTemplateLoader(loader);
//		     config.setDirectoryForTemplateLoading(new File("soap"));
			if (locale == null && encode == null)
			{
				template = config.getTemplate(filename);
			}
			else if (locale == null && encode != null)
			{
				template = config.getTemplate(filename, encode);
			}
			else if (locale != null && encode == null)
			{
				template = config.getTemplate(filename, locale);
			}
			else
			{
				template = config.getTemplate(filename, locale, encode);
			}
			return template;
		}
	public String getClasspath(){
		 return this.getClass().getResource("/soap").toString();
	}
	public static void main(String[] args) {
		try {
			JdbcBean jdbcBean=ProperConfigure.getInstance().getJdbcBeanFromConfig("DEV");
			System.out.println(ProperConfigure.getInstance().getProperty(ProperConfigure.DEFAULTENCODING));
			Map<String, String>p=new HashMap<String, String>();
			p.put("orderid", "111111");
//			File f=new File("classpath:xxxx");
//			System.out.println(f.getAbsolutePath());
//			System.out.println(SoapUtil.getInstance().getClasspath());
		String soap=	SoapUtil.getInstance().parseFTL("endcomplete.xml", p);
		 Document ele=SoapUtil.getInstance().getSoapClient(jdbcBean).sendSOAPMessage(soap);
		
		System.out.println(soap);
		System.out.println(ele.asXML());
	//	System.out.println(soap);
		}catch(BLiSSOAPException exception){
			exception.printStackTrace();
			System.out.println(exception.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
