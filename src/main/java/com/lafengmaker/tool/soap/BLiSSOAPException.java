package com.lafengmaker.tool.soap;


/**
 * @package com.webex.sos.common.soap; - BLiSSOAPException.java
 * @create at - 2005-10-28 14:40:40
 * @user - Administrator
 * @description - 
 * @version 1.0
 */

public class BLiSSOAPException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BLiSSOAPException(String code,String message) {
		super(message);
	}
	
	public BLiSSOAPException(String code,Throwable thx) {
		super( code,thx);
	}
	 
	public BLiSSOAPException(String code,String message,Throwable thx) {
	}
}


