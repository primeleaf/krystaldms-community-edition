/**
 * Created On May 6, 2006
 *
 * Copyright 2006 by Primeleaf Consulting (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consulting (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting (P) Ltd.
 **/

package com.primeleaf.krystal.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;  


/**
 * @author Rahul Kubadia
 * @since 2.0
 */
public final class PasswordService {
	private static PasswordService instance;

	private PasswordService(){

	}

	public synchronized String encrypt(String plaintext){
		MessageDigest md = null;
		try	    {
			md = MessageDigest.getInstance("SHA1");
		}
		catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		try{
			md.update(plaintext.getBytes("UTF-8"));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		byte raw[] = md.digest();
		Base64.Encoder encoder = Base64.getEncoder();  
		String hash = encoder.encodeToString(raw);
		return hash;
	}
	public static synchronized PasswordService getInstance(){
		if(instance == null){
			return new PasswordService();
		}else{
			return instance;
		}
	}
	
	public String generatePassword(){//Added by saumil shah on Nov 2021 to generate random valid password
		String passwordChars ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String password="";
		int pos=0;
		for(int i=0;i<8;i++){
			pos =(int) Math.floor(Math.random() * passwordChars.length());
			password+=passwordChars.charAt(pos);
		}
		return password;
	}

	public static void main(String args[]){
		try{
			String encryptedPassword = PasswordService.getInstance().encrypt("admin");
			System.out.print(encryptedPassword);
		}catch(Exception sue){
			System.out.println("No such System");
		}
	}
}
