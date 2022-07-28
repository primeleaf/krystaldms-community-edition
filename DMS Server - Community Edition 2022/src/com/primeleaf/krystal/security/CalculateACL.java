/**
 * Copyright 2004 by Arysys Technologies Private Limited LLC.,
 * 3972 Barranca Pkwy, J#135
 * Irvine, CA 92606
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Arysys Technologies Private Limited LLC. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Arysys Technologies Private Limited LLC.
 */

/**
 * This class calculates the effective permission on an object.
 * Create an instance of this class and add the public ACL.
 * Calculate the groups and roles that the user is assigned to. 
 * Add all the role acls and group acls. Finally add the user ACL on the
 * doc class if it was saved.
 * 
 * Get the final acl int value and find out if the object has the necessary 
 * permissions using the ACL object.
 * 
 *  @author hhakim
 *
 */
package com.primeleaf.krystal.security;
public class CalculateACL {

	int finalACL = 0; /** holds the final ACL value */
	int roleACL = 0; /** holds the role ACL value */ 
	int groupACL = 0; /** holds the group ACL value */
	int userACL = 0; /** holds the user ACL value */
	
	boolean hasPublicACL = false; /** does it have Public ACL */
	boolean hasGroupACL = false; /** does it have group ACL */
	boolean hasRoleACL = false; /** does it have Role ACL */
	boolean hasUserACL = false; /** does it have Role ACL */
	
	/**
	 * Default constructor
	 */
	public CalculateACL(){
		
	}
	/**
	 * Add the public acl that is stored for that object. Note every object will have a public acl stored in the database. The value of it may be 0, but it should have it.
	 * 
	 * @param acl
	 */
	public void addPublicAcl(int acl){
		hasPublicACL = true;
	    finalACL = acl;  
	}
	/**
	 * Add the ACL int values of all the roles that the user is assigned to. Remember to add only those ACL values for roles whose permissions are set on the Object.
	 * @param acl
	 */
	public void addRoleAcl(int acl){
		hasRoleACL = true;
        roleACL  = roleACL | acl; 
	}

	/**
	 * Add the ACL int values of all the groups that the user is assigned to. Remember to add only those ACL values for groups whose permissions are set on the Object.
	 * @param acl
	 */
	public void addGroupAcl(int acl){
		hasGroupACL = true;
        groupACL = groupACL | acl;  
	}
	/**
	 * If the user permissions are set on that object, then add the acl integer value.
	 * @param acl
	 */
	public void addUserAcl(int acl){
		hasUserACL = true;
		userACL = acl;
	}
	
	/**
	 * Returns the final ACL integer value on the object for the particular user.
	 * @return
	 */
	public int getFinalACL(){
		
		if(hasGroupACL){
			finalACL = groupACL;
		}
		
		if(hasRoleACL){
			finalACL = roleACL;
		}
		
		if(hasUserACL){
			finalACL = userACL;
		}

		return finalACL;
	}
	
	public static void main(String args[]){
		CalculateACL cacl = new CalculateACL();
		/** read the public ACL for that class and set it here */
		cacl.addPublicAcl(255);
		
		/** for each role assigned to the class add its acl */
		cacl.addGroupAcl(110);
		
		
		/** for each role assigned to the class add its acl */
		/*cacl.addRoleAcl(1);
		cacl.addRoleAcl(18);
		cacl.addRoleAcl(1);
		cacl.addRoleAcl(18);
		cacl.addRoleAcl(1);
		cacl.addRoleAcl(18);
		*/
		/** if the user permissions are defined add the acl */
		//cacl.addUserAcl(1);
		
		/** get the final acl int value and this will determine whether the user has specific permissions on the doc class */
		int finals = cacl.getFinalACL();
		ACL acl = new ACL(finals);
		acl.displayPermissions();
	}
	
}
