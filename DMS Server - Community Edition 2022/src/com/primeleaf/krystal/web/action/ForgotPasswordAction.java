package com.primeleaf.krystal.web.action;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.PasswordService;
import com.primeleaf.krystal.util.EmailMessage;
import com.primeleaf.krystal.web.view.ForgotPasswordView;
import com.primeleaf.krystal.web.view.WebView;

public class ForgotPasswordAction implements Action {
	
	private Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if("POST".equalsIgnoreCase(request.getMethod())){
			try {
				String userEmail = request.getParameter("txtUserEmail")==null?"":request.getParameter("txtUserEmail");
				String userName=request.getParameter("txtUserName")==null?"":request.getParameter("txtUserName");
				if(userName.trim().length()>0){
					if(UserDAO.getInstance().validateUser(userName)){
						UserDAO.getInstance().setReadCompleteObject(true);
						User user=UserDAO.getInstance().readUserByName(userName);
						if(userEmail.equalsIgnoreCase(user.getUserEmail())){
							String password=PasswordService.getInstance().generatePassword();
							user.setPassword(password);
							UserDAO.getInstance().updateUserPassword(user);
							EmailMessage emailMessage = new EmailMessage();
							emailMessage.setSubject("KRYSTAL DMS - Community Edition : Password");
							emailMessage.setFrom(user.getUserEmail());

							StringBuffer messageBody = new StringBuffer();
							messageBody.append("Hello "+ user.getRealName()+",");
							messageBody.append("<br/>Login to KRYSTAL DMS - Community Edition using your new password.");
							messageBody.append("<br/>Your new password is : " + password);

							messageBody.append("<br/><br/>Regards,<br/>DMS Administrator");
							
							emailMessage.setTo(user.getUserEmail());
							emailMessage.setMessage(messageBody.toString());
							emailMessage.send();
							
							request.setAttribute(HTTPConstants.REQUEST_MESSAGE,"Password successfully sent to your email id");
						}else{
							request.setAttribute(HTTPConstants.REQUEST_ERROR,"Username "+userName+" does not exist");
						}
					}else{
						request.setAttribute(HTTPConstants.REQUEST_ERROR,"Username "+userName+" does not exist");
					}
				}
			} catch (Exception e) {
				kLogger.severe("Unable to complete the forgot password request.");
			}	
		}
		return (new ForgotPasswordView(request, response));
	}

}
