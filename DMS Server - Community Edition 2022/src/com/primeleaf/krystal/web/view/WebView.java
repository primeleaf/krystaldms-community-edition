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

package com.primeleaf.krystal.web.view;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.HitList;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.web.KrystalSession;

/**
 * @author Rahul Kubadia
 *
 */
public abstract class WebView {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected PrintWriter out;
	protected KrystalSession krystalSession = null;
	public abstract void render() throws Exception;

	public void init(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception{
		httpRequest.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);
		httpResponse.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);
		request = httpRequest;
		response = httpResponse;
		request.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);
		response.setCharacterEncoding(HTTPConstants.CHARACTER_ENCODING);

		response.setContentType("text/html; charset=utf-8"); 
		response.setHeader("Cache-Control", "no-cache"); 
		out = httpResponse.getWriter();
		session = (HttpSession) httpRequest.getSession();
		out = httpResponse.getWriter();
		
		krystalSession = (KrystalSession) session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		
	}

	public void printError(String message) throws Exception{
		out.println("<div class=\"alert d-flex align-items-center alert-danger\"><i class=\"me-2 bi bi-x-lg\"></i>"+ message + "</div>");
	}
	public void printSuccess(String message) throws Exception{
		out.println("<div class=\"alert d-flex align-items-center alert-success\"><i class=\"me-2 bi bi-check-lg\"></i>"+StringEscapeUtils.escapeHtml4(message) + "</div>");
	}
	public void printInfo(String message) throws Exception{
		out.println("<div class=\"alert d-flex align-items-center alert-info\"><i class=\"me-2 bi bi-info-circle h6\"></i>"+StringEscapeUtils.escapeHtml4(message) + "</div>");
	}
	public void printWarning(String message) throws Exception{
		out.println("<div class=\"alert d-flex align-items-center alert-warning\"><i class=\"me-2 bi bi-exclamation-triangle h6\"></i>"+StringEscapeUtils.escapeHtml4(message) + "</div>");
	}
	public void printErrorDismissable(String message) throws Exception{
		out.println("<div class=\"alert d-flex justify-content-between align-items-center alert-danger alert-dismissable p-2\"><span><i class=\"me-2 bi bi-x-lg\"></i>"+StringEscapeUtils.escapeHtml4(message)+"</span><button type=\"button\" class=\"btn\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi-x-circle text-white\"></i></button></div>");
	}
	public void printSuccessDismissable(String message) throws Exception{
		out.println("<div class=\"alert d-flex justify-content-between align-items-center alert-success alert-dismissable p-2\"><span> <i class=\"me-2 bi bi-check-lg\"></i>"+StringEscapeUtils.escapeHtml4(message)+"</span><button type=\"button\" class=\"btn\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi-x-circle text-white\"></i></button></div>");
	}
	public void printInfoDismissable(String message) throws Exception{
		out.println("<div class=\"alert d-flex justify-content-between align-items-center alert-info alert-dismissable p-2\"><span><i class=\"me-2 bi bi-info-circle h6\"></i>"+StringEscapeUtils.escapeHtml4(message)+"</span><button type=\"button\" class=\"btn\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi-x-circle text-white\"></i></button></div>");
	}
	public void printWarningDismissable(String message) throws Exception{
		out.println("<div class=\"alert d-flex justify-content-between align-items-center alert-warning alert-dismissable p-2\"><span><i class=\"me-2 bi bi-exclamation-triangle h6></i>\""+StringEscapeUtils.escapeHtml4(message)+"</span><button type=\"button\" class=\"btn\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi-x-circle text-white\"></i></button></div>");
	}

	public void showCriteriaDropdown(String indexName,byte selectedOperator)throws Exception{
		out.println("<select id=\"operator_"+indexName+"\" name=\"operator_"+indexName+"\" class=\"operator form-select form-control\"> ");
		if(selectedOperator == IndexDefinition.OPERATOR_IS){
			out.println("<option value=\""+IndexDefinition.OPERATOR_IS+"\" selected>Is</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_IS+"\">Is</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_ISNOT){
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISNOT+"\" selected>Is Not</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISNOT+"\">Is Not</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_LIKE){
			out.println("<option value=\""+IndexDefinition.OPERATOR_LIKE+"\" selected>Like</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_LIKE+"\">Like</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_NOTLIKE){
			out.println("<option value=\""+IndexDefinition.OPERATOR_NOTLIKE+"\" selected>Not Like</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_NOTLIKE+"\">Not Like</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_ISGREATERTHAN){
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISGREATERTHAN+"\" selected>Is Greater Than</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISGREATERTHAN+"\">Is Greater Than</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_ISLESSERTHAN){
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISLESSERTHAN+"\" selected>Is Lesser Than</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISLESSERTHAN+"\">Is Lesser Than</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_BETWEEN){
			out.println("<option value=\""+IndexDefinition.OPERATOR_BETWEEN+"\" selected>Between</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_BETWEEN+"\">Between</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_ISEMPTY){
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISEMPTY+"\" selected>Is Empty</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISEMPTY+"\">Is Empty</option>");
		}
		if(selectedOperator == IndexDefinition.OPERATOR_ISNOTEMPTY){
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISNOTEMPTY+"\" selected>Is Not Empty</option>");
		}else{
			out.println("<option value=\""+IndexDefinition.OPERATOR_ISNOTEMPTY+"\">Is Not Empty</option>");
		}
		out.println("</select>");
	}
	public void showMetaCriteriaDropdown(String indexName)throws Exception{
		out.println("<select id=\"operator_"+indexName+"\" name=\"operator_"+indexName+"\" class=\"operator form-select form-control\"> ");
		out.println("<option value=\""+IndexDefinition.OPERATOR_BETWEEN+"\">Between</option>");
		out.println("</select>");
	}
	public void printModal(String id){
		if(id=="userDetailsModal") {
			out.println("<div class=\"modal fade bs-example-modal-lg\" data-bs-backdrop=\"static\" data-bs-keyboard=\"false\" tabindex=\"-1\" role=\"dialog\" id=\""+id+"\" aria-labelledby=\"myLargeModalLabel\" aria-hidden=\"true\">");
			out.println("<div class=\"modal-dialog modal-dialog-centered modal-sm \">");
			out.println("<div class=\"modal-content\">");
			out.println("<div class=\"modal-body\"></div>");
			out.println("<div class=\"modal-footer\">\r\n"
					+ "<button type=\"button\" class=\"btn btn-sm btn-dark\" data-bs-dismiss=\"modal\">Close</button>\r\n"
					+ "</div>");
			out.println("</div>");
			out.println("</div>");
			out.println("</div>");
			return;
		}
		out.println("<div class=\"modal fade bs-example-modal-lg\" tabindex=\"-1\" role=\"dialog\" id=\""+id+"\" aria-labelledby=\"myLargeModalLabel\" aria-hidden=\"true\">");
		out.println("<div class=\"modal-dialog modal-lg\">");
		out.println("<div class=\"modal-content\">");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
	}

	public void generatePagination(String url){
		int totalSize = (Integer)request.getAttribute("TOTALHITS");
		int currentPage = (Integer)request.getAttribute("PAGE");
		int pageSize = krystalSession.getKrystalUser().getHitlistSize();
		int totalPages = (int)Math.ceil((double)totalSize / (double) pageSize); 

		Enumeration<String> enumRequest = request.getParameterNames(); // get all the requested parameters
		StringBuffer queryString = new StringBuffer(url);
		queryString.append("?");
		while(enumRequest.hasMoreElements()){
			String parameterName = (String) enumRequest.nextElement();
			if(parameterName.equalsIgnoreCase("page")){
				continue;
			}
			String parameterValue = request.getParameter(parameterName);
			queryString.append("&");
			queryString.append(parameterName);
			queryString.append("=");
			queryString.append(parameterValue);
		}

		if(totalPages > 1){
			out.println("<div class=\"text-center\">");
			out.println("<ul class=\"pagination mt-2 mb-3\">");

			int startPage = 1;
			int endPage = 0; 
			
			if(currentPage > 10){
				startPage = currentPage - 10;
			}
			endPage = currentPage + 9; //end page will be current page plus 10
			
			if(endPage>totalPages) { //if end page becomes more than total pages then end page should be total pages
				endPage = totalPages;
			}
			
			
			if(currentPage == 1){
				out.println("<li class=\"page-item disabled\"><a class=\"page-link\" href=\"javascript:void(0);\">&laquo;</a></li>");
			}else{
				out.println("<li page=\"page-item\"><a class=\"page-link\" href=\""+StringEscapeUtils.escapeHtml4(queryString.toString()) +"&page="+(currentPage-1)+"\">&laquo;</a></li>");
			}
			for(int i = startPage; i <= endPage ; i++){
				if(i != currentPage){
					out.println("<li class=\"page-item\">");
				}else{
					out.println("<li class=\"page-item active\">");
				}
				out.println("<a class=\"page-link\" href=\""+StringEscapeUtils.escapeHtml4(queryString.toString()) +"&page="+i+"\">");
				out.println(i);
				out.println("</a>");
				out.println("</li>");
			}
			if(currentPage == totalPages){
				out.println("<li class=\"page-item disabled\"><a class=\"page-link\" href=\"javascript:void(0);\">&raquo;</a></li>");
			}else{
				out.println("<li class=\"page-item\"><a class=\"page-link\" href=\""+StringEscapeUtils.escapeHtml4(queryString.toString()) +"&page="+(currentPage+1)+"\">&raquo;</a></li>");
			}
			out.println("</ul>");
			out.println("</div>");
		}
	}


	public void printHitListColumnHeader(String columnName,String displayName){
		Enumeration<String> enumRequest = request.getParameterNames(); // get all the requested parameters
		String orderType = request.getParameter("orderType")!=null?request.getParameter("orderType"):"D";
		String orderBy = request.getParameter("orderBy")!=null?request.getParameter("orderBy"):"DOCUMENTID";
		String orderIcon = "";
		if(orderType.equalsIgnoreCase("A")){
			if(orderBy.equalsIgnoreCase(columnName)){
				orderIcon = " <i class=\"bi bi-caret-up-fill\"></i>";
			}
			orderType = "D";
		}else{
			if(orderBy.equalsIgnoreCase(columnName)){
				orderIcon = " <i class=\"bi bi-caret-down-fill\"></i>";
			}
			orderType = "A";
		}
		StringBuffer queryString = new StringBuffer(request.getRequestURL());
		queryString.append("?");
		while(enumRequest.hasMoreElements()){
			String parameterName = (String) enumRequest.nextElement();
			if(parameterName.equalsIgnoreCase("orderby") || parameterName.equalsIgnoreCase("ordertype")){
				continue;
			}
			String parameterValue = request.getParameter(parameterName);
			queryString.append("&");
			queryString.append(parameterName);
			queryString.append("=");
			queryString.append(parameterValue);
		}
		queryString.append("&orderBy=");
		queryString.append(columnName);
		queryString.append("&orderType=");
		queryString.append(orderType);
		String cssClass = "text-left";
		if(HitList.isMetaDataColumn(columnName)){
			cssClass="text-center";
		}
		out.println("<th class=\""+cssClass+"\"><a href=\""+StringEscapeUtils.escapeHtml4(queryString.toString())+"\">"+displayName+" " + orderIcon + "</a>");
	}

	public void printErrorDismissableMain(String message) throws Exception{
		out.println("<div class=\"alert d-flex justify-content-between align-items-center alert-danger alert-dismissable\"><span>"+message + "</span><button type=\"button\" class=\"btn\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi-x-circle text-white\"></i></button></div>");
	}
	public void printInfoDismissableMain(String message) throws Exception{
		out.println("<div class=\"alert d-flex justify-content-between align-items-center alert-info alert-dismissable\"><span>"+message + "</span><button type=\"button\" class=\"btn\" data-bs-dismiss=\"alert\" aria-hidden=\"true\"><i class=\"bi-x-circle text-white\"></i></button></div>");
	}
}