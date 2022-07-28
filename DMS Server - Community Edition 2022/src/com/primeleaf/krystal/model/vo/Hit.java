/**
 * Copyright 2005 by Arysys Technologies (P) Ltd.,
 * #3,Shop line,
 * Sasmira Marg,
 * Worli,Mumbai 400 025
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Arysys Technologies (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Arysys Technologies (P) Ltd.
 * 
 */

package com.primeleaf.krystal.model.vo;

import java.util.ArrayList;

import com.primeleaf.krystal.constants.ServerConstants;

/**
 * The value object for document Hit
 * @author Rahul Kubadia
 * @since 1.0
 * 
 * @see com.primeleaf.krystal.model.vo.HitList
 * @see com.primeleaf.krystal.model.dao.HitListDAO
 */

public class Hit implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int documentId = 0;

	public String revisionId = "";
	
	public boolean hasNote = false;

	public boolean hasAnnotation = false;

	public boolean hasAttachment = false;

	public String created = "";

	public String modified = "";

	public String parentView = "";

	public ArrayList<String> indexValues = null;

	public String status = "";

	public static String STATUS_LOCKED = "L";

	public static String STATUS_AVAILABLE = "I";

	public static String STATUS_DELETED = "D";
	
	public static String STATUS_HIDDEN = "H";
	
	public static String STATUS_EXPIRED = "E";

	public String extension = "";

	public String expiryOn = "";

	public int userFieldSize = 0; 
	
	public int fileLength=0;
	
	public boolean isHeadRevision = true;

	public String createdBy = ServerConstants.SYSTEM_USER;
	
	public String modifiedBy = "";
	
	public String userName = "";
	
	public static final byte FLAG_HASNOTE = 1;

	public static final byte FLAG_HASANNOTATION = 2;

	public static final byte FLAG_HASATTACHMENTS = 4;

	public void setProperties(byte properties) {
		if ((properties & 1) == 1)
			hasNote = true;
		properties >>= 1;
		if ((properties & 1) == 1)
			hasAnnotation = true;
		properties >>= 1;
		if ((properties & 1) == 1)
			hasAttachment = true;
	}

	public byte getProperties() {
		byte properties = 0;
		if (hasNote)
			properties |= FLAG_HASNOTE;
		if (hasAnnotation)
			properties |= FLAG_HASANNOTATION;
		if (hasAttachment)
			properties |= FLAG_HASATTACHMENTS;
		return properties;
	}
}