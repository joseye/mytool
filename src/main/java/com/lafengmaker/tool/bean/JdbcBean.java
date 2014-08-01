package com.lafengmaker.tool.bean;

import java.io.Serializable;

public class JdbcBean implements Serializable {
	private static final long serialVersionUID = -8195528515722094269L;
	private String id;
	private String title;
	private String name;
	private String tns;
	private  String soapUsername;
	private  String SoapPasswd;
	private String dbUserName;
	private String dbPassword;
	private String wcpUrl;
	private String authUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTns() {
		return tns;
	}
	public void setTns(String tns) {
		this.tns = tns;
	}
	
	public String getSoapUsername() {
		return soapUsername;
	}
	public void setSoapUsername(String soapUsername) {
		this.soapUsername = soapUsername;
	}
	public String getSoapPasswd() {
		return SoapPasswd;
	}
	public void setSoapPasswd(String soapPasswd) {
		SoapPasswd = soapPasswd;
	}
	public String getDbUserName() {
		return dbUserName;
	}
	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getWcpUrl() {
		return wcpUrl;
	}
	public void setWcpUrl(String wcpUrl) {
		this.wcpUrl = wcpUrl;
	}
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	public String generateOracleURL() throws Exception{
		if(null==tns){ throw new  RuntimeException("tns is null");}
		if(null==dbUserName){ throw new  RuntimeException("username is null");}
		if(null==dbPassword){ throw new  RuntimeException("password is null");}
		if(null==name){ throw new  RuntimeException("name is null");}
		return "jdbc:oracle:thin:@"+tns;
		
	}
	public String toString() {
		return "JdbcBean [id=" + id + ", title=" + title + ", name=" + name
				+ ", tns=" + tns + ", soapUsername=" + soapUsername
				+ ", SoapPasswd=" + SoapPasswd + ", dbUserName=" + dbUserName
				+ ", dbPassword=" + dbPassword + ", wcpUrl=" + wcpUrl
				+ ", authUrl=" + authUrl + "]";
	}
	
	
}
