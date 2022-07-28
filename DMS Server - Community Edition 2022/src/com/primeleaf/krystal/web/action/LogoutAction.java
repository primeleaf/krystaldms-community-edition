/**
 * Created On 05-Jan-2014
 * Copyright 2010 by Primeleaf Consulting (P) Ltd.,
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
 */

package com.primeleaf.krystal.web.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.view.LogoutView;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class LogoutAction implements Action {

	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
    	if(krystalSession != null)
			AuditLogManager.log(new AuditLogRecord(
					krystalSession.getKrystalUser().getUserId(),
					AuditLogRecord.OBJECT_USER,
					AuditLogRecord.ACTION_LOGOUT,
					krystalSession.getKrystalUser().getUserName(),
					request.getRemoteAddr(),
					AuditLogRecord.LEVEL_INFO,
					"",""));
		session.invalidate();
		Cookie[] cookies = request.getCookies();
		int cookieLength = cookies.length;
		for (int i = 0; i < cookieLength; i++) {
			if(cookies[i].getName().compareTo("darkMode")!=0) {
			Cookie cookie = cookies[i];
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			}
		}
		return (new LogoutView(request, response));
	}
}

