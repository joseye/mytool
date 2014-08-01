package com.lafengmaker.tool.soap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import com.lafengmaker.tool.constants.SOAPConstants;

/**
 * @package com.webex.sos.common.soap; - BLiSAuthenticator.java
 * @create at - 2005-10-27 16:55:57
 * @user - Administrator
 * @description - 
 * @version 1.0
 */

public class BLiSAuthenticator
{
	
	
	/**
	 * authenticate
	 */
	public static final String AUTHENTICATE = "authenticate";
	
	private String authurl;
	private String username;
	private String password;
	private String defaultEncoding;
	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	/**
	 * getter for authurl
	 * @return
	 */
	public String getAuthurl()
	{
		return authurl;
	}

	/**
	 * setter for authurl
	 * @param authurl
	 */
	public void setAuthurl(String authurl)
	{
		this.authurl = authurl;
	}
	
	public Document authenticate(String strAuthRequest)
		throws BLiSSOAPException
	{
		org.dom4j.Document doc = null;
		DataOutputStream out = null;
		BufferedReader in = null;
		try
		{
			URL authenticateURL;
            authenticateURL = new URL(this.getAuthurl());
            URLConnection urlconn = authenticateURL.openConnection();
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
            out = new DataOutputStream(urlconn.getOutputStream());
            out.writeBytes(strAuthRequest);
//            out.close();

            StringBuffer reply = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            char[] buffer = new char[SOAPConstants.BUFFER_SIZE];
            int read = -1;
            while ((read = in.read(buffer)) > -1) {
                reply.append(new String(buffer, 0, read));
            }
//            String strReply = new String(reply);           
            doc = DocumentHelper.parseText(reply.toString());
        } catch (Exception e) {
            throw new BLiSSOAPException("parse xml error", e);
        }finally
        {
        	IOUtils.closeQuietly(out);
        	IOUtils.closeQuietly(in);
        }
        
        return doc;
	}
	
	public synchronized String authenticate()
		throws BLiSSOAPException
	{
		String _sessionID = null;
		DataOutputStream out = null;
		BufferedReader in = null;
        try {
            //make a url connection to the gateway
            URL authenticateURL;
            authenticateURL = new URL(this.getAuthurl());
            URLConnection urlconn = authenticateURL.openConnection();
            urlconn.setDoInput(true);
            urlconn.setDoOutput(true);
            out = new DataOutputStream(urlconn.getOutputStream());
            String strAuthRequest = 
            	new StringBuffer("<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\">")
            		.append("<SOAP:Header/>")
            		.append("<SOAP:Body>")
            		.append("<Authenticate xmlns=\"http://schemas.cordys.com/1.0/webgateway\">")
            		.append("<username>")
            		.append(this.getUsername())
            		.append("</username>")
            		.append("<password>")
            		.append(this.getPassword())
            		.append("</password>")
            		.append("</Authenticate>")
            		.append("</SOAP:Body>")
            		.append("</SOAP:Envelope>")
            		.toString();           
            
            out.writeBytes(strAuthRequest);
//            out.close();

            StringBuffer reply = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            char[] buffer = new char[SOAPConstants.BUFFER_SIZE];
            int read = -1;
            while ((read = in.read(buffer)) > -1) {
                reply.append(new String(buffer, 0, read));
            }
//            String strReply = new String(reply);           
            org.dom4j.Document doc = DocumentHelper.parseText(reply.toString());       
            Node node = doc.selectSingleNode("//*[name()='wcp-session']");
            if (null != node) {
                _sessionID = node.getText();

            }
        } catch (Exception e) {
        	e.printStackTrace();
        }finally
        {
        	IOUtils.closeQuietly(out);
        	IOUtils.closeQuietly(in);
        }
        
        return _sessionID;
        
	}
}


