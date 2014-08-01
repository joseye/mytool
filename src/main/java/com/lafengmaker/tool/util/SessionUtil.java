package com.lafengmaker.tool.util;

import javax.servlet.http.HttpServletRequest;

public class SessionUtil {
	public static final String  SESSIONKEY_DBLIST="session_dblist";
	public static void addObjtoSeesion(HttpServletRequest request, String key,Object o){
		request.getSession().setAttribute(key, o);
	}

}
