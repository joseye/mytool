package com.lafengmaker.tool.soap;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.lafengmaker.tool.constants.SOAPConstants;

/**
 * @package com.webex.sos.common.soap; - BLiSSOAPClient.java
 * @create at - 2005-10-28 13:48:27
 * @user - Administrator
 * @description - 
 * @version 1.0
 */

public class BLiSSOAPClient
{
	private String gatewayurl;
	private long timeout;
	private BLiSAuthenticator authenticator=null;
	private String sessionId;
	private String defaultEncoding;

	
	public Document sendSOAPMessage(final String request)throws BLiSSOAPException {
		Document d= this.sendSOAPMessage(request,timeout);
		return d;
	}
	
	public Document sendSOAPMessage(final String request,final long interval)throws BLiSSOAPException{
		if (sessionId == null){
			sessionId = authenticator.authenticate();
		}
		Document response = this.sendAndReceive(request,interval);
		if (null!=response.selectSingleNode(".//SOAP:Fault/SOAP:faultcode")&&"Client.NotAuthenticated".equalsIgnoreCase(response.selectSingleNode(".//SOAP:Fault/SOAP:faultcode").getText())) {
			sessionId = getAuthenticator().authenticate();
            response = this.sendAndReceive(request,interval);
        }
        return response;
	}
	
	
	private Document sendAndReceive(final String request,final long interval)throws BLiSSOAPException{
		Document document = null;
		DataOutputStream out = null;
		long timeOutInterval=0;
		InputStream is =null;
		try {
			if (interval<=0)
				timeOutInterval=timeout;
			else
				timeOutInterval=interval;
			
			final StringBuffer sessionURL = new StringBuffer(getGatewayurl());
			sessionURL.append("?");
			sessionURL.append("wcp-session=").append(sessionId);
			sessionURL.append("&");
			sessionURL.append("timeout=").append(timeOutInterval);
			final URL serviceURL = new URL(sessionURL.toString());
			
			final long start = System.currentTimeMillis();
	        URLConnection urlconn = serviceURL.openConnection();
	        HttpURLConnection hcon = (HttpURLConnection) urlconn;
	
	        urlconn.setDoInput(true);
	        urlconn.setDoOutput(true);
	        hcon.setRequestMethod("POST");
	        hcon.setRequestProperty("Content-Type", "text/xml");
	
	        out = new DataOutputStream(hcon.getOutputStream());

	        //out.writeBytes(request);
	        out.write(request.getBytes(getDefaultEncoding()));
	        out.flush();

	        is = urlconn.getInputStream();

	        //timeout checking here 
	        final long costtime = System.currentTimeMillis() - start;
	        System.out.println("this soap send cost"+costtime/1000+" seconds.");
	        is = new BufferedInputStream(is);
	        byte[] byteArray = new byte[SOAPConstants.BUFFER_SIZE];
	        int iLength = -1;
	        final StringBuffer sBuffer = new StringBuffer();
	        while ((iLength = is.read(byteArray)) > -1)
	        {
	        	sBuffer.append(new String(byteArray, 0, iLength,getDefaultEncoding()));
	        }
            document = replaceNameSpace(sBuffer.toString());	       
		}catch (Exception de)
		{
			throw new BLiSSOAPException("build document  error", de);
		}finally
		{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(is);
		}
        return document;
	}
	
	/**
	 * tuning for replaceNameSpace(Document)
	 * @param sResponseXML
	 * @return Document
	 * @throws DocumentException
	 */
	public Document replaceNameSpace(String sResponseXML)
		throws DocumentException
	{
		final String sResponseString =sResponseXML.replaceAll("xmlns=\"http://www.webex.com/blis/","xmlTemp" + "=\"");
		final Document document = DocumentHelper.parseText(sResponseString);
		return document;
	}
	
	/**
	 * 
	 *
	 * @param document
	 * @return
	 * @throws DocumentException
	 * @author witty
	 *
	 */
	public Document replaceNameSpace(Document paramDocument) throws DocumentException{
		Document document=paramDocument;
		
		String documentString = document.asXML();
		
		documentString=documentString.replaceAll("xmlns=\"http://www.webex.com/blis/","xmlTemp"+"=\"");
		//OMSLogger.info("documentString:"+documentString);
		document = DocumentHelper.parseText(documentString);
		return document;
	}
	
	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
	
	protected BLiSAuthenticator getAuthenticator()
	{
		return authenticator;
	}

	public void setAuthenticator(BLiSAuthenticator authenticator)
		throws BLiSSOAPException
	{
		this.authenticator = authenticator;
//		sessionId = authenticator.authenticate();
	}

	public String getGatewayurl()
	{
		return gatewayurl;
	}

	public void setGatewayurl(String gatewayurl)
	{
		this.gatewayurl = gatewayurl;
	}
	
	public Document authenticate(final String strAuthRequest)
		throws BLiSSOAPException
	{
		return this.getAuthenticator().authenticate(strAuthRequest);
	}
	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}
}


