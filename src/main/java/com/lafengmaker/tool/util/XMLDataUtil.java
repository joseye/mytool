/*
 * @(#)XMLDataUtil.java        11/25/05
 *
 * Copyright (C) 2002 WebEx Communications Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Webex
 * communications Inc.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Webex
 */
package com.lafengmaker.tool.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class XMLDataUtil
{
	/**
	 * UTF-8
	 * this encode can get from BLiSConfig
	 */
    public final static String ENCODE_TYPE = "UTF-8";
    public static final int DEFAULT_VALUE_ZERO = 0;
    public static final String DEFAULT_VALUE_EMPTYSTRING = "";
    
    public static final String DEFAULT_VALUE_EMPTYSTRING_1 = "-1";
    
    public static long getNodeTextAsLong(Node node) {
    	return getNodeTextAsLong(node,DEFAULT_VALUE_ZERO);
    }
    
    public static long getNodeTextAsLong(Node node,long defaultValue) {
    	if (node !=null){
    		String nodeValue = node.getText();
    		return Long.parseLong(nodeValue);
    	}else{
    		return defaultValue;
    	}
    }
    
    public static long getNodeTextAsLong(Node node,String xPath) {
    	return getNodeTextAsLong(node,xPath,DEFAULT_VALUE_ZERO);
    }
    /**
     * 
     * @param node
     * @param xPath
     * @param defaultValue
     * @return
     */
    public static long getNodeTextAsLong(Node node,String xPath,long defaultValue) {
    	long returnValue=0;
    	if (node !=null){
    		Node tempNode=node.selectSingleNode(xPath);
    		if (tempNode !=null){
    			String nodeValue = tempNode.getText();
    			returnValue=parseLong(nodeValue,defaultValue);
    		}else{
    			returnValue=defaultValue;
    		}
    	}else{
    		returnValue=defaultValue;
    	}
    	return returnValue;
    }
    
    public static int getNodeTextAsInt(Node node){
    	return getNodeTextAsInt(node,DEFAULT_VALUE_ZERO);
    }
    
    public static int getNodeTextAsInt(Node node,int defaultValue){
    	if (node !=null){
    		String nodeValue = node.getText();
    		return parseInt(nodeValue,defaultValue);
    	}else{
    		return defaultValue;
    	}
    }
    
    public static int getNodeTextAsInt(Node node,String xPath) {
    	return getNodeTextAsInt(node,xPath,DEFAULT_VALUE_ZERO);
    }
    
	/**
	 * 
	 * @param node
	 * @param xPath
	 * @param defaultValue
	 * @return
	 */
    public static int getNodeTextAsInt(Node node,String xPath,int defaultValue) {
    	int returnValue=0;
    	if (node !=null){
    		Node tempNode=node.selectSingleNode(xPath);
    		if (tempNode !=null){
    			String nodeValue = tempNode.getText();
    			returnValue=parseInt(nodeValue,defaultValue);
    		}else{
    			returnValue=defaultValue;
    		}
    	}else{
    		returnValue=defaultValue;
    	}
    	return returnValue;
    }
    

    public static String getNodeTextAsString(Node node){
        return getNodeTextAsString(node,DEFAULT_VALUE_EMPTYSTRING);
    }
    
    public static String getNodeTextAsString(Node node,String defaultValue){
    	if (node !=null){
    		String nodeValue = node.getText().trim();
    		return nodeValue;
    	}else{
    		return defaultValue;
    	}
    }
    

    public static String getNodeTextAsStringByPattern(Node node,String xPath) {
    	return getNodeTextAsStringByPattern(node,xPath,DEFAULT_VALUE_EMPTYSTRING);
    }

    public static String getNodeTextAsStringByPattern(Node node,String xPath,String defaultValue) {
    	String returnValue="";
    	if (node !=null){
    		Node tempNode=node.selectSingleNode(xPath);
    		if (tempNode !=null){
    			returnValue=tempNode.getText().trim();
    		}else{
    			returnValue=defaultValue;
    		}
    	}else{
    		returnValue=defaultValue;
    	}
    	return returnValue;
    }
    
    public static boolean setDataByPattern(Node node, String value, String pattern) {
        if (node !=null){
        	Node desNode=node.selectSingleNode(pattern);
        	if (desNode !=null){
        		desNode.setText(value);
        		return true;
        	}else{
        		return false;
        	}
        }else{
        	return false;
        }
    }
    
    /**
     *  
     * @param toNode the Node that u wanna copy to
     * @param fromNode the Node that u use to copy
     * @param toXpath   
     * @param fromXpath
     * @return toNode
     * @author Calvin
     */
    public static Node copyXmlData(Node toNode, Node fromNode,
            String[] toXpath, String[] fromXpath) {
        int length = fromXpath.length;
        if (length != 0) {
            if (length != toXpath.length) {
                return null;
            }

            for (int i = 0; i < length; i++) {
                String fromValue = XMLDataUtil.getNodeTextAsStringByPattern(fromNode,
                        fromXpath[i]);
                XMLDataUtil.setDataByPattern(toNode, fromValue, toXpath[i]);
            }

        } else {
            return null;
        }

        return toNode;
    }
    
    public static double getNodeTextAsDouble(Node node) {
    	return getNodeTextAsDouble(node,DEFAULT_VALUE_ZERO);
    }
    
    public static double getNodeTextAsDouble(Node node,long defaultValue) {
    	if (node !=null){
    		String nodeValue = node.getText();
    		return parseDouble(nodeValue,defaultValue);
    	}else{
    		return defaultValue;
    	}
    }
    
    
    public static double getNodeTextAsDouble(Node node,String xPath) {
    	return getNodeTextAsDouble(node,xPath,DEFAULT_VALUE_ZERO);
    }
    
    public static double getNodeTextAsDouble(Node node,String xPath,long defaultValue) {
    	double returnValue=0;
    	if (node !=null){
    		Node tempNode=node.selectSingleNode(xPath);
    		if (tempNode !=null){
    			String nodeValue = tempNode.getText();
    			returnValue=parseDouble(nodeValue,defaultValue);
    		}else{
    			returnValue=defaultValue;
    		}
    	}else{
    		returnValue=defaultValue;
    	}
    	return returnValue;
    }
    
    
    public static Document CreateDocumentFromFile(String fileName) throws Exception {
		Document doc = null;
			InputStream is=XMLDataUtil.class.getResourceAsStream(fileName);
			SAXReader reader =new SAXReader();
			doc=reader.read(is);
	    	
		return doc;
	}
    
	public static Document loadDocument(String sFileName) throws Exception {
		InputStream is = XMLDataUtil.class.getResourceAsStream("/" + sFileName);
		SAXReader rd = new SAXReader();

		Document document = null;
			document = rd.read(is);
		return document;
	}
	
	 public static Document parseDocument(String sContent)  throws Exception {
		Document doc = null;
		doc = DocumentHelper.parseText(sContent);
		return doc;
	}	 
	 
	 /**
	  * Save to xml file
	  * @param dom
	  * @param sFilePathName
	  * @param encode
	  * @return
	  * @throws CommonException
	  */
    public static boolean saveXML(Document dom, String sFilePathName, String encode) throws Exception {

        File file = new File(sFilePathName);

        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            FileOutputStream out = new FileOutputStream(file);
            // if(!encode.equals(ENCODE_UTF_8)){
            if (encode != null) {
                format.setEncoding(encode);
            }

            // format.setTrimText(true);
            XMLWriter xmlWriter = new XMLWriter(out, format);
            xmlWriter.write(dom);
            xmlWriter.flush();
            xmlWriter.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("XMLDATAUTIL-SAVE_DOCUMENT-001", e);
        }

    }
    public static long parseLong( String value, long defaultValue ) {
    	try
    	{
    		return Long.parseLong(value);
    	}
    	catch (NumberFormatException nfe)
    	{
    		return defaultValue;
    	}
    }
    public static double parseDouble( String value, double defaultValue ) { 
        try
        {
        	return Double.parseDouble(value);
        }
        catch (NumberFormatException nfe)
        {
        	return defaultValue;
        }
}
    public static int parseInt( String value, int defaultValue ) {
    	try
    	{
    		return Integer.parseInt(value);
    	}
    	catch(NumberFormatException nfe)
    	{
    		return defaultValue;
    	}
    }
}