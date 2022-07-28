/**
 * Created on Sep 22, 2005 
 *
 * Copyright 2005 by Primeleaf Consutling (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consutling (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consutling (P) Ltd. 
 */
package com.primeleaf.krystal.constants;

/**
 * @author Rahul Kubadia
 * @since 2.0
 * @see com.primeleaf.krystal.web.WebServerManager
 * @comments This class keeps all constants required for server 
 */
public class ServerConstants {
	public static final String KRYSTAL_HOME = System.getProperty("user.dir");
	
	public static final String SERVER_VERSION = "2022";
	public static final String SERVER_EDITION = "Community Edition";
	
	public static final String KRYSTAL_DATABASE="KRYSTALCE";
	public static final String KRYSTAL_DATABASEOWNER="KRYSTALDBO";
	public static final String KRYSTAL_DATABASEPASSWORD="Krystal2018";
	
	public static final String KRYSTAL_WEB_HOME = KRYSTAL_HOME + "/webapps";
	
	public static final String SYSTEM_USER="SYSTEM";
	public static final String SYSTEM_ADMIN_USER="ADMINISTRATOR";
	public static String SYSTEM_MASTER_PASSWORD = "";
	
	public static final String DEFAULT_EXPIRY_DATE="12/31/2099";
	public static final String STORAGE_DIRECTORY_FORMAT="yyyy-MM";
	
	public static final String FORMAT_SHORT_DATE="dd-MMM-yyyy";
	
	public static final String ENCRYPTION_ALGORITHM="HMACMD5";
	
	public static final String CONFIG_HTTP_PORT="httpPort";
	public static final String CONFIG_ALLOW_MULTIPLE_LOGIN="allowMultipleLogin";
	public static final String CONFIG_ALLOW_PASSWORD_RESET="allowPasswordReset";
	public static final String CONFIG_INTEGERATE_OPENOFFICE="integrateOpenOffice";
	public static final String CONFIG_OPENOFFICE_PATH="openOfficePath";
	public static final String CONFIG_ALLOW_MASTER_PASSWORD="allowMasterPassword";
	public static final String CONFIG_MASTER_PASSWORD="masterPassword";
	public static final String CONFIG_SESSION_TIMEOUT="sessionTimeout";
	public static final String CONFIG_SMTP_PORT="smtpPort";
	public static final String CONFIG_SMTP_TLS="smtpTLS";
	public static final String CONFIG_SMTP_HOST="smtpHost";
	public static final String CONFIG_SMTP_USERNAME="smtpUsername";
	public static final String CONFIG_SMTP_PASSWORD="smtpPassword";
	

	/**
	 * 
	 */
	public ServerConstants() {
		super();
	}

}
