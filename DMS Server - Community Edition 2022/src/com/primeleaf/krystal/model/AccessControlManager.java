/**
 * Created on Oct 8, 2005 
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
package com.primeleaf.krystal.model;

import java.util.ArrayList;

import com.primeleaf.krystal.model.dao.PermissionDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Permission;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.ACL;

/**
 * Access control values are managed / read via this module
 * @author Rahul Kubadia
 * @since 2.0
 * 
 */
public class AccessControlManager {
	
	/**
	 * Default constructor
	 */
	public AccessControlManager() {
		super();
	}
	
	public ACL getACL(DocumentClass documentClass, User krystalUser)throws Exception{
		ACL finalACL ;
		int aclValue  = 0 ;
		ArrayList<Permission> permissions = PermissionDAO.getInstance().readPermissions("CLASSID = " + documentClass.getClassId() + " AND USERID = " + krystalUser.getUserId());
		if(permissions.size() > 0 ){
			aclValue = permissions.get(0).getAclValue();
		}
		finalACL = new ACL(aclValue);
		return finalACL;
	}

}
