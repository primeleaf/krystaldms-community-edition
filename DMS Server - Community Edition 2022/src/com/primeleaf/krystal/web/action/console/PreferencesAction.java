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

package com.primeleaf.krystal.web.action.console;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.HitList;
import com.primeleaf.krystal.model.vo.MetaColumnPreferences;
import com.primeleaf.krystal.web.KrystalSession;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.AJAXResponseView;

/**
 * @author Rahul Kubadia
 */

public class PreferencesAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		KrystalSession krystalSession = (KrystalSession)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
		if("POST".equalsIgnoreCase(request.getMethod())){
			try {
				String pageSize=request.getParameter("cmbHitListSize")!=null?request.getParameter("cmbHitListSize"):"10";
				int hitListSize;
				try {
					hitListSize = Integer.parseInt(pageSize);
				}catch (Exception e) {
					hitListSize = 5;
				}
				String checkOutDir=request.getParameter("txtCheckOutDir")!=null?request.getParameter("txtCheckOutDir"):"C:/TEMP";;
				String showThumbNail = request.getParameter("radThumbNail")!=null?request.getParameter("radThumbNail"):"TRUE";
				String createdColumn=request.getParameter(HitList.META_CREATED)!=null?request.getParameter(HitList.META_CREATED):"";
				String createdByColumn=request.getParameter(HitList.META_CREATEDBY)!=null?request.getParameter(HitList.META_CREATEDBY):"";
				String lengthColumn=request.getParameter(HitList.META_LENGTH)!=null?request.getParameter(HitList.META_LENGTH):"";
				String revisionColumn=request.getParameter(HitList.META_REVISIONID)!=null?request.getParameter(HitList.META_REVISIONID):"";
				String documentIdColumn=request.getParameter(HitList.META_DOCUMENTID)!=null?request.getParameter(HitList.META_DOCUMENTID):"";
				String expiryOnColumn=request.getParameter(HitList.META_EXPIRYON)!=null?request.getParameter(HitList.META_EXPIRYON):"";
				String fileNameColumn=request.getParameter(HitList.META_FILENAME)!=null?request.getParameter(HitList.META_FILENAME):"";
				String modifiedColumn=request.getParameter(HitList.META_MODIFIED)!=null?request.getParameter(HitList.META_MODIFIED):"";
				String modifiedByColumn=request.getParameter(HitList.META_MODIFIEDBY)!=null?request.getParameter(HitList.META_MODIFIEDBY):"";
				String Mode=request.getParameter("Mode")!=null?request.getParameter("Mode"):"";
				Cookie modeCookie = new Cookie("darkMode",Mode);
				modeCookie.setMaxAge(60*60*24*365);
				modeCookie.setPath("/");
				response.addCookie(modeCookie);
				
				MetaColumnPreferences metaPreferences = new MetaColumnPreferences();

				metaPreferences.setCreatedByVisible("TRUE".equalsIgnoreCase(createdByColumn));
				metaPreferences.setCreatedVisible("TRUE".equalsIgnoreCase(createdColumn));
				metaPreferences.setFileSizeVisible("TRUE".equalsIgnoreCase(lengthColumn));
				metaPreferences.setRevisionIdVisible("TRUE".equalsIgnoreCase(revisionColumn));
				metaPreferences.setDocumentIdVisible("TRUE".equalsIgnoreCase(documentIdColumn));
				metaPreferences.setExpiryOnVisible("TRUE".equalsIgnoreCase(expiryOnColumn));
				metaPreferences.setFileNameVisible("TRUE".equalsIgnoreCase(fileNameColumn));
				metaPreferences.setModifiedVisible("TRUE".equalsIgnoreCase(modifiedColumn));
				metaPreferences.setModifiedByVisible("TRUE".equalsIgnoreCase(modifiedByColumn));
				
				UserDAO.getInstance().setReadCompleteObject(true);
				krystalSession.getKrystalUser().setCheckOutPath(checkOutDir);
				krystalSession.getKrystalUser().setHitlistSize(hitListSize);
				krystalSession.getKrystalUser().setMetaPreferences(metaPreferences);
				krystalSession.getKrystalUser().setShowThumbNail(showThumbNail);
				
				krystalSession.getKrystalUser().setCheckOutPath(checkOutDir);
				krystalSession.getKrystalUser().setHitlistSize(hitListSize);
				krystalSession.getKrystalUser().setMetaPreferences(metaPreferences);
				krystalSession.getKrystalUser().setShowThumbNail(showThumbNail);
				
				UserDAO.getInstance().updateUser(krystalSession.getKrystalUser(), false);
				AuditLogManager.log(new AuditLogRecord(krystalSession.getKrystalUser().getUserId(),AuditLogRecord.OBJECT_USER,AuditLogRecord.ACTION_PREFERENCESCHANGED,krystalSession.getKrystalUser().getUserName(),request.getRemoteAddr(),AuditLogRecord.LEVEL_FINE));
				request.setAttribute(HTTPConstants.REQUEST_MESSAGE, "Preferences set successfully");
				return (new AJAXResponseView(request, response));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return (new AJAXResponseView(request, response));
	}
}

