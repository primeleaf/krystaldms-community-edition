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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.model.IndexRecordManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class AutoCompleteAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		response.setHeader("Cache-Control", "no-cache"); 
		
		StringBuffer sb = new StringBuffer();
		String value= (request.getParameter("term")!=null?request.getParameter("term"):"").trim();
		String classid = (request.getParameter("classid")!=null?request.getParameter("classid"):"").trim();  
		String indexColumnName=(request.getParameter("indexname")!=null?request.getParameter("indexname"):"").trim();
		
		int classId = 0;
		try{
			classId = Integer.parseInt(classid);
			DocumentClass documentClass = DocumentClassDAO.getInstance().readDocumentClassById(classId);
			ArrayList<String> indexValues =IndexRecordManager.getInstance().getDistinctIndexValues(documentClass.getClassName(), indexColumnName); 
			value=value.toLowerCase();
			if(indexValues.size()>0){
				sb.append("[");
				for(String indexValue : indexValues){
					if ((value != null) && indexValue.toLowerCase ().startsWith(value)  && value.trim().length() > 0) {
						sb.append("\"");
						sb.append(indexValue);
						sb.append("\"");
						sb.append(",");
					}
				}
				if(sb.length()>1){
					sb.replace(sb.length()-1, sb.length(), "");//replace the last ","
				}
				sb.append("]");
				response.setContentType("text/plain"); 
				response.setHeader("Cache-Control", "no-cache"); 
				response.getWriter().write(sb.toString()); 
				response.getWriter().flush();
				response.getWriter().close();
			}else{
				response.setStatus(HttpServletResponse.SC_NO_CONTENT); 
			}
		}catch(Exception ex){
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}
		return null;
	}
}

