/**
 * Created on Oct 26, 2005 
 *
 * Copyright 2005 by Arysys Technologies (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Arysys Technologies (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Arysys Technologies (P) Ltd. 
 */
package com.primeleaf.krystal.model.vo;

import com.primeleaf.krystal.constants.ServerConstants;

/**
 * This class is a value object for AuditLogs tables in database
 * @author Rahul Kubadia
 * @since 2.0
 * 
 */
public class AuditLogRecord {

	public static final String ACTION_CREATED="Created";
	public static final String ACTION_CHECKIN="Checked in";
	public static final String ACTION_CHECKOUT="Checked out";
	public static final String ACTION_DELETED="Deleted";
	public static final String ACTION_DOWNLOADED="Downloaded";
	public static final String ACTION_RESTORED="Restored";
	public static final String ACTION_EDITED="Edited";
	public static final String ACTION_ACCESS="Accessed";
	public static final String ACTION_DESTROYED="Deleted permanently";
	public static final String ACTION_SHARED="Document Shared";
	public static final String ACTION_CHECKOUTCANCEL="Checkout cancelled";
	public static final String ACTION_MOVED="Moved";

	public static final String ACTION_LOGIN="Logged in";
	public static final String ACTION_LOGOUT="Logged out";
	public static final String ACTION_PASSWORDCHANGED="Password Changed";
	public static final String ACTION_PERMISSIONCHANGED="Permissions Changed";
	public static final String ACTION_PREFERENCESCHANGED="Preferences Changed";
	public static final String ACTION_FORGOT_PASSWORD_REQUEST="New Password Requested";

	public static final int LEVEL_WARNING =0;
	public static final int LEVEL_INFO =1;
	public static final int LEVEL_FINE =2;
	public static final int LEVEL_FINER =3;
	public static final int LEVEL_FINEST =4;

	public static final String OBJECT_ANNOUNCEMENT="A";
	public static final String OBJECT_BOOKMARK ="B";
	public static final String OBJECT_DOCUMENTCLASS="C";


	public static final String OBJECT_DOCUMENT="D";
	public static final String OBJECT_TAG="DT";//Version 6.0
	
	public static final String OBJECT_EVENT="E";
	public static final String OBJECT_GROUP="G";

	public static final String OBJECT_HOTLINK="H";
	public static final String OBJECT_INDEX="I";
	public static final String OBJECT_JOB="J";
	
	public static final String OBJECT_TASK="K";
	
	public static final String OBJECT_ACLTEMPLATE="L";
	
	public static final String OBJECT_DOMAIN="M";
	
	public static final String OBJECT_JOURNAL_NOTE="N";

	public static final String OBJECT_WORKQUEUE="Q";

	public static final String OBJECT_RETENTIONPOLICY="P";
	
	public static final String OBJECT_ROLE="R";
	public static final String OBJECT_RESTRICTED_IP="RI"; //Version 5.0
	
	public static final String OBJECT_GLOBALSETTINGS="S";
	
	public static final String OBJECT_WORKFLOWTEMPLATE="T";
	
	public static final String OBJECT_USER="U";

	public static final String OBJECT_DOCVIEW="V";
	public static final String OBJECT_DOCSET="DS";
	public static final String OBJECT_WORKFLOW_CASE="W";
	public static final String OBJECT_WORKFLOW_VIEW="WV";

	public static final String OBJECT_DISCUSSION="X";
	
	public static final String OBJECT_LIST="LT";
	public static final String OBJECT_LIST_ITEM="LI";

	private int objectId=0;
	private String objectType=OBJECT_DOCUMENT;
	private String action=ACTION_CREATED;
	private String userName=ServerConstants.SYSTEM_USER;
	private String ipAddress="LOCALHOST";
	private String actionDate="";
	private String parameters="";
	private String comments="";
	private int level=1;


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	public String getActionDate() {
		return actionDate;
	}


	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public String getIpAddress() {
		return ipAddress;
	}


	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public int getObjectId() {
		return objectId;
	}


	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}


	public String getObjectType() {
		return objectType;
	}
	public String getObjectDescription(){
		String objectDescription = "";
		if(OBJECT_ANNOUNCEMENT.equalsIgnoreCase(objectType)){
			objectDescription = "Announcement";
		}else if(OBJECT_BOOKMARK.equalsIgnoreCase(objectType)){
			objectDescription = "Bookmark";
		}else if(OBJECT_DOCUMENTCLASS.equalsIgnoreCase(objectType)){
			objectDescription = "Document Class";
		}else if(OBJECT_DOCVIEW.equalsIgnoreCase(objectType)){
			objectDescription = "Document View";
		}else if(OBJECT_DOCUMENT.equalsIgnoreCase(objectType)){
			objectDescription = "Document";
		}else if(OBJECT_DOMAIN.equalsIgnoreCase(objectType)){
			objectDescription = "Domain";
		}else if(OBJECT_EVENT.equalsIgnoreCase(objectType)){
			objectDescription = "Event";
		}else if(OBJECT_GROUP.equalsIgnoreCase(objectType)){
			objectDescription = "Group";
		}else if(OBJECT_INDEX.equalsIgnoreCase(objectType)){
			objectDescription = "Index";
		}else if(OBJECT_JOB.equalsIgnoreCase(objectType)){
			objectDescription = "Job";
		}else if(OBJECT_JOURNAL_NOTE.equalsIgnoreCase(objectType)){
			objectDescription = "Journal Note";
		}else if(OBJECT_HOTLINK.equalsIgnoreCase(objectType)){
			objectDescription = "Hotlink";
		}else if(OBJECT_WORKQUEUE.equalsIgnoreCase(objectType)){
			objectDescription = "Workqueue";
		}else if(OBJECT_WORKFLOWTEMPLATE.equalsIgnoreCase(objectType)){
			objectDescription = "Workflow Template";
		}else if(OBJECT_WORKFLOW_CASE.equalsIgnoreCase(objectType)){
			objectDescription = "Workflow Case";
		}else if(OBJECT_DISCUSSION.equalsIgnoreCase(objectType)){
			objectDescription = "Discussion";
		}else if(OBJECT_ROLE.equalsIgnoreCase(objectType)){
			objectDescription = "Role";
		}else if(OBJECT_RESTRICTED_IP.equalsIgnoreCase(objectType)){
				objectDescription = "Restricted IP";
		}else if(OBJECT_TASK.equalsIgnoreCase(objectType)){
			objectDescription = "Task";
		}else if(OBJECT_USER.equalsIgnoreCase(objectType)){
			objectDescription = "User";
		}else if(OBJECT_GLOBALSETTINGS.equalsIgnoreCase(objectType)){
			objectDescription = "Global Settings";
		}else if(OBJECT_ACLTEMPLATE.equalsIgnoreCase(objectType)){
			objectDescription = "ACL Template";
		}else if(OBJECT_RETENTIONPOLICY.equalsIgnoreCase(objectType)){
			objectDescription = "Retention Policy";
		}else if(OBJECT_WORKFLOW_VIEW.equalsIgnoreCase(objectType)){
			objectDescription = "Workflow View";
		}else if(OBJECT_DOCSET.equalsIgnoreCase(objectType)){
				objectDescription = "Document Set";
		}else if(OBJECT_TAG.equalsIgnoreCase(objectType)){
			objectDescription = "Document Tag";
		}else{
			objectDescription = "Unknown";
		}
		return objectDescription;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}


	public String getParameters() {
		return parameters;
	}


	public void setParameters(String parameters) {
		this.parameters = parameters;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * Default constructor
	 */
	public AuditLogRecord() {
		super();
	}

	public AuditLogRecord(int objectId,String objectType){
		this.objectId = objectId;
		this.objectType=objectType;
	}

	public AuditLogRecord(int objectId,String objectType,String action){
		this(objectId,objectType);
		this.action=action;
	}

	public AuditLogRecord(int objectId,String objectType,String action,String userName){
		this(objectId,objectType,action);
		this.userName=userName;
	}

	public AuditLogRecord(int objectId,String objectType,String action,String userName,String ipAddress){
		this(objectId,objectType,action,userName);
		this.ipAddress = ipAddress;
	}

	public AuditLogRecord(int objectId,String objectType,String action,String userName,String ipAddress,int level){
		this(objectId,objectType,action,userName,ipAddress);
		this.level=level;
	}
	public AuditLogRecord(int objectId,String objectType,String action,String userName,String ipAddress,int level,String parameters){
		this(objectId,objectType,action,userName,ipAddress,level);
		this.parameters=parameters;
	}
	public AuditLogRecord(int objectId,String objectType,String action,String userName,String ipAddress,int level,String parameters,String comments){
		this(objectId,objectType,action,userName,ipAddress,level,parameters);
		this.comments=comments;
	}
}
