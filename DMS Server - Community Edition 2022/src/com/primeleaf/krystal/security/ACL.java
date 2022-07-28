/*
 * Created on Apr 15, 2005
 *
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
package com.primeleaf.krystal.security;

/**
 * @author Rahul Kubadia
 *
 */
public class ACL {

	private int acl = 0;
	
	private boolean create = false;
	private boolean read =false;
	private boolean write =false;
	private boolean delete =false;
	private boolean print = false;
	private boolean email =false;
	private boolean checkin =false;
	private boolean checkout =false;
	private boolean download = false;
	
	public static final int MAXVALID15DIGIT = 16639;
	public static final int MINVALID15DIGIT = 16384;
	
	/**
	 * 
	 */
	public ACL() {
		super();
	}
	
	public ACL(int acl) {
		this.acl = acl;
		
		if((acl & 1) ==1){
			create = true;
		}
		acl = acl >> 1;
		if((acl & 1) ==1){
			read = true;
		}
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			write = true;
		}
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			delete = true;
		}
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			print = true;
		}
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			email = true;
		}
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			checkin = true;
		}
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			checkout = true;
		}	
		
		acl = acl >> 1;
		if((acl & 1) ==1){
			download = true;
		}		
	}
	
	public boolean canCheckin() {
		return checkin;
	}

	/**
	 * @return
	 */
	public boolean canCheckout() {
		return checkout;
	}

	/**
	 * @return
	 */
	public boolean canCreate() {
		return create;
	}

	/**
	 * @return
	 */
	public boolean canDelete() {
		return delete;
	}

	/**
	 * @return
	 */
	public boolean canEmail() {
		return email;
	}

	/**
	 * @return
	 */
	public boolean canPrint() {
		return print;
	}

	/**
	 * @return
	 */
	public boolean canRead() {
		return read;
	}

	/**
	 * @return
	 */
	public boolean canWrite() {
		return write;
	}
	/**
	 * @return
	 */
	public int getAcl() {
		return acl;
	}

	/**
	 * @param i
	 */
	public void setAcl(int i) {
		acl = i;
	}

	/**
	 * @param b
	 */
	public void setCheckin(boolean b) {
		checkin = b;
	}

	/**
	 * @param b
	 */
	public void setCheckout(boolean b) {
		checkout = b;
	}

	/**
	 * @param b
	 */
	public void setCreate(boolean b) {
		create = b;
	}

	/**
	 * @param b
	 */
	public void setDelete(boolean b) {
		delete = b;
	}

	/**
	 * @param b
	 */
	public void setEmail(boolean b) {
		email = b;
	}

	/**
	 * @param b
	 */
	public void setPrint(boolean b) {
		print = b;
	}

	/**
	 * @param b
	 */
	public void setRead(boolean b) {
		read = b;
	}

	/**
	 * @param b
	 */
	public void setWrite(boolean b) {
		write = b;
	}

	public boolean canDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}
	

	/**
	 * returns the acl value 
	 * 
	 * @return
	 */
	public int getACLValue(){
		String aclS = "";
		
		if(download){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(checkout){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(checkin){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(email){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(print){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(delete){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(write){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(read){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}
		
		if(create){
			aclS = aclS+"1";
		}else{
			aclS = aclS+"0";
		}	
		
		int aclI = Integer.valueOf(aclS,2).intValue();
		
		return aclI;
	}
	public void displayPermissions(){
		System.out.println("canCreate() " +canCreate());
		System.out.println("canReadonly() " +canRead());
		System.out.println("canWrite() " +canWrite());
		System.out.println("canDelete() " +canDelete());
		System.out.println("canPrint() " +canPrint());
		System.out.println("canEmail() " +canEmail());
		System.out.println("canCheckin() " +canCheckin());
		System.out.println("canCheckout() " +canCheckout());
		System.out.println("canDownload() " +canDownload());
		System.out.println("IntValue() " + getACLValue());
	}
}
