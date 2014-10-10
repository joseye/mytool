/*
 * Created on 2006-6-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package com.lafengmaker.tool.constants;

public interface OrderConstants {
	public static final int NEW_SERVICE = 1;

	public static final int ADD_NEW_SERVICE = 2;

	public static final int MODIFY_SERVICE = 3;

	public static final int CANCEL_SERVICE = 4;

	public static final int REPLACE_SERVICE = 5;

	public static final int INT_MOVEORDERTYPE = 9;

	public static final int INT_TERMINATEORDERTYPE = 10;

	public static final int INT_EXPIREORDERTYPE = 11;

	public static final int INT_CANCELLATION = 4;

	public static final String SELL_TYPE_NOCHANGE = "No change";

	public static final String SELL_TYPE_UPSELL = "Up sell";

	public static final String SELL_TYPE_DOWNSELL = "Down sell";

	public static final String CANCELTION_CHANGESERVCIE = "downsell";

	public static final String CANCELTION_CANCELSERVCIE = "cancel";

	public static final String ORDER_INSTANCETYPE = "serviceportfolio";

	public static final String ACCOUNT_SUBTYPE_VAR = "Partner VAR";

	public static final String ACCOUNT_SUBTYPE_REFERRAL = "Partner Referral";

	public static final String TO_CURRENCY = "USD";

	public static final String USD_CURRENCY_EXCHANGERATE = "1.000";

	public static final String OPPNAMECHECK_ACCOUNTTYPE_WEBEX = "WebEx Direct";

	public static final String OPPNAMECHECK_ACCOUNTTYPE_AGENCY = "Agency Direct";

	public static final String ENTRYTYPE_VIEW = "orderview";

	public static final String ENTRYTYPE_CHANGE = "orderchange";

	public static final String SERVICEPROVIDER_PREFIX = "WebEx ";

	/**
	 * Active
	 */
	public static final String STATUS_ACTIVE = "Active";

	public static final String STATUS_END_APPROVED = "End-approved";

	public static final String STATUS_END_COMPLETED = "End-completed";

	public static final String STATUS_END_FREEZED = "End-freezed";

	public static final String STATUS_NONE = "None";

	/**
	 * insert
	 */
	public static String ACTION_TYPE_INSERT = "insert";

	public static String ACTION_TYPE_UPDATE = "update";

	public static String ACTION_TYPE_DELETE = "delete";

	public static final String ORDER_LOGIC_TYPE_SR = "orderSR";

	public static final String COMPONENT_SEARCH_ADDITIONAL_SITE = "%Additional Site%";

	public static final String APPROVE_TYPE_CORRECT = "CORRECT";

	public static final String APPROVE_TYPE_REJECT = "REJECT";

	/**
	 * %Additional Language%
	 */
	public static final String COMPONENT_SEARCH_ADDITIOANL_LANGUAGE = "%Additional Language%";

	public static final String XPATH_ORDER_PRIMARY_LANGUAGE = ".//offer/chargeCondition[@blischecked='true'][@blisActivationType!='cancel']/ParamSetInstance//parameter[@name='Primary Language']/@value";

	public static final String XPATH_SELECT_REBRANDSITE_SKU =".//chargeCondition[@blischecked='true' and @blisActivationType='new']/TrailRunResultItem/transac/AdditionSKUInfo";
	/**
	 * DefaultSite
	 */
	public static final String PROVISION_TICKETTYPE_DEFAULTSITE = "DefaultSite";

	/**
	 * PackageSite
	 */
	public static final String PROVISION_TICKETTYPE_PACKAGESITE = "PackageSite";

	/**
	 * AdditionalSite
	 */
	public static final String PROVISION_TICKETTYPE_ADDITIONALSITE = "AdditionalSite";

	/**
	 * Primary
	 */
	public static final String PROVISION_SETTINGTYPE_PRIMARY = "Primary";

	/**
	 * Default
	 */
	public static final String PROVISION_SETTINGTYPE_DEFAULT = "Default";

	/**
	 * key_initial_term
	 */
	public static final String KEY_INITIAL_TERM = "key_initial_term";

	/**
	 * Inactive
	 */
	public static final String STATUS_INACTIVE = "Inactive";

	public static final String ENT_ORDERBEAN = "orderbean";

	public static final String ENT_ACCOUNT = "account";

	public static final String ENT_CUSTOMERFORM = "customerForm";

	public static final String ENT_CREDITCARDFORM = "creditCardForm";

	public static final String ENT_ORDAGNTASGNMENT = "orderagentassignment";

	public static final String ENT_REASONCODE = "reasonCodeBean";

	public static final String ENT_CSMSR = "csmSR";

	public static final String ENT_THIRDSR = "thirdSR";

	public static final String ENT_SECONDSR = "secondSR";

	public static final String ENT_PRIMARYSR = "primarySR";

	public static final String ENT_CHANNELPARTNERINFO = "channelPartnerInfo";

	public static final String PROVISION_TIMEZONE_DEFAULTVALUE = "(GMT -08:00) PACIFIC TIME,USA & CANADA";

	/**
	 * PROVISIONTICKET
	 */
	public static final String SEQ_NAME_PROVISIONTICKET = "PROVISIONTICKET";

	/**
	 * PROVISIONSETTING
	 */
	public static final String SEQ_NAME_PROVISIONSETTING = "PROVISIONSETTING";

	/**
	 * PROVISIONSITE
	 */
	public static final String SEQ_NAME_PROVISIONSITE = "SITE";

	/**
	 * CONTACT
	 */
	public static final String SEQ_NAME_CONTACT = "CONTACT";

	/**
	 * LOSS_REQUEST
	 */
	public static final String SEQ_NAME_APPROVALREQUEST = "APPROVALREQUEST";

	/*
	 * REASON
	 */
	public static final String SEQ_NAME_REASON = "REASON";

	/*
	 * AGENTASSIGNMENT
	 */
	public static final String SEQ_NAME_AGENTASSIGNMENT = "AGENTASSIGNMENT";

	/*
	 * ORDERSERVCIE
	 */
	public static final String SEQ_NAME_ORDERSERVICE = "ORDERSERVICE";

	/*
	 * CONTRACTSETTING
	 */
	public static final String SEQ_NAME_CONTRACTSETTING = "CONTRACTSETTING";

	public static final String ORDER_STATUS_NEW = "New";

	public static final String ORDER_STATUS_PENDINGAPPROVAL = "Pending-approval";

	public static final String ORDER_STATUS_PENDINGCORRECTION = "Pending-correction";

	public static final String ORDER_STATUS_NEWRESUBMIT = "New-resubmit";

	public static final String ORDER_STATUS_ACCEPTED = "Accepted";

	public static final String ORDER_STATUS_PENDINGEFFECTIVE = "Pending-effective";

	public static final String ORDER_ORDERENTITYID = "5";

	public static final String SERVICESNAPSHOT_ENTITYID = "15";

	public static final String SERVICE_ENTITYID = "24";

	public static final String ORDER_YES_FLAG = "Yes";

	public static final String ORDER_NO_FLAG = "No";

	public static final String ORDER_TYPE_NEW = "1";

	public static final String AGENTASSIGNMENTTYPEID_PRIMARYSR = "6";

	public static final String AGENTASSIGNMENTTYPEID_SECONDSR = "7";

	public static final String AGENTASSIGNMENTTYPEID_THIRDSR = "8";

	public static final String AGENTASSIGNMENTTYPEID_CSMSR = "9";

	public static final String FLAG_TRUE = "true";

	public static final String FLAG_FALSE = "false";

	public static final String FLAG_YES = "Yes";

	public static final String FLAG_NO = "No";

	public static final String FLAG_NONE = "none";

	public static final String FLAG_NA = "N/A";

	public static final String LOSSREQUEST_EXTEND_TYPE1 = "Termination";

	public static final String LOSSREQUEST_EXTEND_TYPE2 = "Expiration";

	public static final String USD_SIGN = "$";

	public static final String SKUINFO_SKUID = "computed.SKUID";

	public static final String SKUINFO_QTY = "computed.QTY";

	public static final String SKUINFO_EXT_PRICE = "computed.EXT_PRICE";

	public static final String SKUINFO_LIST_PRICE = "computed.LIST_PRICE";

	public static final String SKUINFO_UNIT_PRICE = "computed.UNIT_PRICE";

	public static final String SKUINFO_QTY_TYPE = "computed.QTY_TYPE";

	public static final String SKUINFO_LIST_PRICE_TYPE = "computed.LIST_PRICE_TYPE";

	public static final String SKUINFO_SKU_TYPE = "computed.SKU_TYPE";

	/* for modify service IDTracking */
	public static final String XPATH_DELETED_ORDERITEM = ".//tuple[.//OPERATIONCODE='Deleted']";

	public static final String XPATH_ADDED_ORDERITEM = ".//tuple[.//OPERATIONCODE='Added']";

	public static final String XPATH_MODIFY_ORDERITEM = ".//tuple[.//OPERATIONCODE='Modified']";

	public static final String XPATH_MODIFY_NOCHANGE = ".//tuple[.//OPERATIONCODE='']";

	public static final String AGENTASSIGNT_TEMPLATE_INSERT = "<AgentAssignment><ActionType>insert</ActionType><AgentAssignmentID/>"
			+ "<AssignmentTypeID/><AgentID/><EntityID/><KeyID/>" + "<AssignmentValue1/><AssignmentValue2/><Status/><CreatedBy/>"
			+ "<CreationDate>sysdate</CreationDate><LastModifiedBy/><LastModifiedDate>sysdate</LastModifiedDate></AgentAssignment>";

	public static final String AGENTASSIGNT_TEMPLATE_UPDATE = "AgentAssignment><ActionType>update</ActionType><AgentAssignmentID/>"
			+ "<AgentID/><AssignmentValue1/><AssignmentValue2/><LastModifiedBy/></AgentAssignment>";

	public static final String AGENTASSIGNT_TEMPLATE_DELETE = "<AgentAssignment><ActionType>delete</ActionType><AgentAssignmentID/></AgentAssignment>";

	/**
	 * ".//offer/ParamSetInstance//parameter[@name='Initial_Term']/@value"
	 */
	public static final String XPATH_ORDER_INTIALTERM = ".//offer/ParamSetInstance//parameter[@name='Initial_Term']/@value";

	/**
	 * ".//offer/ParamSetInstance//parameter[@name='Renewal_Term']/@value"
	 */
	public static final String XPATH_ORDER_RENEWTERM = ".//offer/ParamSetInstance//parameter[@name='Renewal_Term']/@value";
	
	public static final String XPATH_ORDER_PREPAYMENT = ".//offer/ParamSetInstance//parameter[@name='Prepay_Term']/@value";

	// for control the button on contract file upload page, added by Stony Shi
	// 20061116
	public static final String contractControlType_EDIT = "edit";

	public static final String contractControlType_VIEW = "view";

	public static final String APPROVAL_DOWNSELL_TYPE = "1";

	public static final String APPROVAL_CANCELLATION_TYPE = "2";

	// add by jarod for the opp id validate code
	public static final String VALIDATE_OPPID_NOTEXISTINPIVOTAL = "notExistInPivotal";

	public static final String VALIDATE_OPPID_USEDINBLIS = "usedInBLiS";

	public static final String VALIDATE_OPPID_UCANUSE = "canUse";

	public static final String VALIDATE_OPPID_ERROR = "error";

	public static final String VALIDATE_OPPID_NOTNEEDORDERID = "999999";

	// Add by String for package offer
	public static final String SERVICE_TAG_ALL = "All";

	// public static final String XPATH_ORDER_PRIMARY_LANGUAGE_SERVICE_TAG =
	// ".//TrailRunResultItem/transac[contains(@chargeConditionCode,'Primary
	// Language Charge')]/detail[@name='computed.Service_Tag']/@value";
	public static final String XPATH_ORDER_SERVICE_TAG = ".//transac/@serviceTag";

	public static final String XPATH_MODIFY_ORDER_SERVICE_TAG = ".//SERVICETAG";

	public static final String DEFAULT_VALUE_EMPTYSERVICETAG = "";

	public static final String PROVISIONINGBY_BPS = "BPS";
	
	public static final String BPS_PROVISION_STATUS = "New";

	public static final String COMPLETEDFLAG_YES = "Yes";

	public static final String OFFER_NAME_CUWL="MC Ports for CUWL";
	
	public static final String OFFER_NAME_MCWEBEX_NODE ="MC WebEx Node Ports";
	public static final String OFFER_NAME_CUWP="MC Ports and Cisco WebEx Connect for CUWP";
	
	public static final String XPATH_CANCELED_TELECONFERENCE_CHARGE =".//offerCondition[@blisActivationType='cancel' and starts-with(@blisoffercode,'Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	public static final String XPATH_CANCELED_GPL_TELECONFERENCE_CHARGE =".//offerCondition[@blisActivationType='cancel' and starts-with(@blisoffercode,'GPL Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	
	public static final String XPATH_NEW_TELECONFERENCE_CHARGE = ".//offerCondition[@blisActivationType='new' and starts-with(@offerCode,'Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	public static final String XPATH_NEW_GPL_TELECONFERENCE_CHARGE = ".//offerCondition[@blisActivationType='new' and starts-with(@offerCode,'GPL Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	
	public static final String XPATH_ACTIVE_TELECONFERENCE_CHARGE = ".//offerCondition[@blisActivationType='active' and starts-with(@blisoffercode,'Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	public static final String XPATH_ACTIVE_GPL_TELECONFERENCE_CHARGE = ".//offerCondition[@blisActivationType='active' and starts-with(@blisoffercode,'GPL Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	
	public static final String XPATH_ACTIVE_TELECONFERENCE_CHARGE_NEW = ".//offerCondition[@blisActivationType='new' and starts-with(@offerCode,'Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	//For GPL Audio teleconference sub offer
	public static final String XPATH_ACTIVE_GPL_TELECONFERENCE_CHARGE_NEW = ".//offerCondition[@blisActivationType='new' and starts-with(@offerCode,'GPL Teleconference')]/chargeCondition[@blischecked='true']/@parentCode";
	
}