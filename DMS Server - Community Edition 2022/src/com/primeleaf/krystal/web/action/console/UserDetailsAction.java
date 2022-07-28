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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.Error404View;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.UserDetailsView;

/**
 * Author Rahul Kubadia
 */

public class UserDetailsAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
		User user = UserDAO.getInstance().readUserByName(request.getParameter("username"));
		
		request.setAttribute("username",user.getUserName());
		request.setAttribute("realName",user.getRealName());
		request.setAttribute("lastLogin",user.getLastLoginDate());
		request.setAttribute("email",user.getUserEmail());
		request.setAttribute("description",user.getUserDescription());
		request.setAttribute("type",user.getUserType());
		
		return (new UserDetailsView(request, response));
		}catch (Exception e) {
			return (new Error404View(request, response));
		}
	}
}

