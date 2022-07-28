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

/**
 * Created on 05-Jan-2014
 *
 * Copyright 2003-09 by Primeleaf Consulting (P) Ltd.,
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

package com.primeleaf.krystal.web.view.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class DownloadDocumentView extends WebView {

	public DownloadDocumentView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		WebPageTemplate edmcTemplate = new WebPageTemplate(request, response);
		edmcTemplate.generateHeader();
		downloadDocumentResponse();
		edmcTemplate.generateFooter();
	}

	private void downloadDocumentResponse() throws Exception{
		try {
			if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
				printError((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
			}
			if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
				printSuccess((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}