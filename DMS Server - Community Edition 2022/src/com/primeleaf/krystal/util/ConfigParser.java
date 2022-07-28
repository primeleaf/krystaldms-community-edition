package com.primeleaf.krystal.util;

import java.io.FileReader;

import org.apache.commons.validator.GenericValidator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.security.PasswordService;

public class ConfigParser {

	static JSONObject configSettings = null;

	public static void init() {
		try {
			configSettings = (JSONObject) new JSONParser().parse(new FileReader(ServerConstants.KRYSTAL_HOME+"/settings/config.json"));
		}catch(Exception e) {
			System.out.println(e);
		}
		ServerConstants.SYSTEM_MASTER_PASSWORD = PasswordService.getInstance().encrypt((String)configSettings.get(ServerConstants.CONFIG_MASTER_PASSWORD));
	}

	public static int getHTTPPort() {
		int result = 8080;
		String httpPort = (String) configSettings.get(ServerConstants.CONFIG_HTTP_PORT);
		
		if(! GenericValidator.isBlankOrNull(httpPort)){
			int httpPortInt = Integer.valueOf(httpPort);
			if(httpPortInt >= 0 && httpPortInt <= 65535) {
				result = httpPortInt;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Boolean isMultipleLoginAllowed() {
		return  (Boolean) configSettings.getOrDefault(ServerConstants.CONFIG_ALLOW_MULTIPLE_LOGIN,false);
	}
	@SuppressWarnings("unchecked")
	public static Boolean isPasswordResetAllowed() {
		return  (Boolean) configSettings.getOrDefault(ServerConstants.CONFIG_ALLOW_PASSWORD_RESET,false);
	}
	@SuppressWarnings("unchecked")
	public static Boolean isOpenOfficeIntegerated() {
		return (Boolean)configSettings.getOrDefault(ServerConstants.CONFIG_INTEGERATE_OPENOFFICE,false);
	}
	@SuppressWarnings("unchecked")
	public static Boolean isMasterPasswordAllowed() {
		return  (Boolean) configSettings.getOrDefault(ServerConstants.CONFIG_ALLOW_MASTER_PASSWORD,false);
	}
	
	public static String getOpenOfficePath() {
		String result = "C:\\Program Files (x86)\\OpenOffice 4\\";
		String openOfficePath = (String)configSettings.get(ServerConstants.CONFIG_OPENOFFICE_PATH);
		
		if(! GenericValidator.isBlankOrNull(openOfficePath)){
			result = openOfficePath;
		}
		return result;
	}
	

	public static int getSessionTimeout() {
		int result = 5;
		String sessionTimeout = (String) configSettings.get(ServerConstants.CONFIG_SESSION_TIMEOUT);
		
		if(! GenericValidator.isBlankOrNull(sessionTimeout)){
			int sessionTimeoutInt = Integer.valueOf(sessionTimeout);
			if(sessionTimeoutInt>=5) {
				result = sessionTimeoutInt;
			}
		}
		return result*60;
		
	}

	public static String getSMTPPort() {
		String result = "C:\\Program Files (x86)\\OpenOffice 4\\";
		String openOfficePath = (String)configSettings.get(ServerConstants.CONFIG_OPENOFFICE_PATH);
		
		if(! GenericValidator.isBlankOrNull(openOfficePath)){
			result = openOfficePath;
		}
		return result;
	}

	public static Boolean isSMTPTLSEnabled() {
		Boolean result = true;
		String allowMasterPassword = (String)configSettings.get(ServerConstants.CONFIG_SMTP_TLS);
		if("true".equalsIgnoreCase(allowMasterPassword)) {
			result =  true;
		}else {
			result =  false;
		}

		return result;
	}

	public static String getSMTPHost() {
		String result = "smtp.yourserver.com";
		String openOfficePath = (String)configSettings.get(ServerConstants.CONFIG_OPENOFFICE_PATH);
		
		if(! GenericValidator.isBlankOrNull(openOfficePath)){
			result = openOfficePath;
		}
		return result;
	}

	public static String getSMTPUsername() {
		String result = "example@gmail.com";
		String openOfficePath = (String)configSettings.get(ServerConstants.CONFIG_OPENOFFICE_PATH);
		
		if(! GenericValidator.isBlankOrNull(openOfficePath)){
			result = openOfficePath;
		}
		return result;
	}

	public static String getSMTPPassword() {
		String result = "your password";
		String openOfficePath = (String)configSettings.get(ServerConstants.CONFIG_OPENOFFICE_PATH);
		
		if(! GenericValidator.isBlankOrNull(openOfficePath)){
			result = openOfficePath;
		}
		return result;
	}
}

