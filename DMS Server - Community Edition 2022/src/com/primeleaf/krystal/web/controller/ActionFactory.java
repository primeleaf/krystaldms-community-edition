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

package com.primeleaf.krystal.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.action.Action404;
import com.primeleaf.krystal.web.action.DefaultAction;
import com.primeleaf.krystal.web.action.ForgotPasswordAction;
import com.primeleaf.krystal.web.action.LoginAction;
import com.primeleaf.krystal.web.action.LogoutAction;
import com.primeleaf.krystal.web.action.StatsAction;
import com.primeleaf.krystal.web.action.console.AccessHistoryAction;
import com.primeleaf.krystal.web.action.console.AutoCompleteAction;
import com.primeleaf.krystal.web.action.console.BulkDeleteDocumentAction;
import com.primeleaf.krystal.web.action.console.BulkDownloadAction;
import com.primeleaf.krystal.web.action.console.CancelCheckoutAction;
import com.primeleaf.krystal.web.action.console.ChangePasswordAction;
import com.primeleaf.krystal.web.action.console.CheckInDocumentAction;
import com.primeleaf.krystal.web.action.console.CheckoutDocumentAction;
import com.primeleaf.krystal.web.action.console.DeleteBookmarkAction;
import com.primeleaf.krystal.web.action.console.DeleteDocumentAction;
import com.primeleaf.krystal.web.action.console.DeleteProfilePictureAction;
import com.primeleaf.krystal.web.action.console.DocumentNotesAction;
import com.primeleaf.krystal.web.action.console.DocumentViewerAction;
import com.primeleaf.krystal.web.action.console.DownloadDocumentAction;
import com.primeleaf.krystal.web.action.console.EditDocumentIndexesAction;
import com.primeleaf.krystal.web.action.console.HomeAction;
import com.primeleaf.krystal.web.action.console.MobileDocumentViewerAction;
import com.primeleaf.krystal.web.action.console.MyProfileAction;
import com.primeleaf.krystal.web.action.console.NewBookmarkAction;
import com.primeleaf.krystal.web.action.console.NewDocumentAction;
import com.primeleaf.krystal.web.action.console.OpenDocumentClassAction;
import com.primeleaf.krystal.web.action.console.PreferencesAction;
import com.primeleaf.krystal.web.action.console.ProfilePictureAction;
import com.primeleaf.krystal.web.action.console.RevisionHistoryAction;
import com.primeleaf.krystal.web.action.console.SearchAction;
import com.primeleaf.krystal.web.action.console.SearchDocumentClassAction;
import com.primeleaf.krystal.web.action.console.ShareDocumentAction;
import com.primeleaf.krystal.web.action.console.UpdateProfilePictureAction;
import com.primeleaf.krystal.web.action.console.UserDetailsAction;
import com.primeleaf.krystal.web.action.console.ViewDocumentAction;
import com.primeleaf.krystal.web.action.cpanel.CancelCheckoutAdminAction;
import com.primeleaf.krystal.web.action.cpanel.ChangeUserPasswordAction;
import com.primeleaf.krystal.web.action.cpanel.ClassIndexesAction;
import com.primeleaf.krystal.web.action.cpanel.ControlPanelAction;
import com.primeleaf.krystal.web.action.cpanel.DeleteClassIndexAction;
import com.primeleaf.krystal.web.action.cpanel.DeleteDocumentClassAction;
import com.primeleaf.krystal.web.action.cpanel.DeleteUserAction;
import com.primeleaf.krystal.web.action.cpanel.DocumentClassAccessHistoryReportAction;
import com.primeleaf.krystal.web.action.cpanel.EditClassIndexesAction;
import com.primeleaf.krystal.web.action.cpanel.EditDocumentClassAction;
import com.primeleaf.krystal.web.action.cpanel.EditUserAction;
import com.primeleaf.krystal.web.action.cpanel.ManageCheckoutsAction;
import com.primeleaf.krystal.web.action.cpanel.ManageDocumentClassesAction;
import com.primeleaf.krystal.web.action.cpanel.NewClassIndexAction;
import com.primeleaf.krystal.web.action.cpanel.NewDocumentClassAction;
import com.primeleaf.krystal.web.action.cpanel.NewUserAction;
import com.primeleaf.krystal.web.action.cpanel.PermissionsAction;
import com.primeleaf.krystal.web.action.cpanel.PurgeDocumentAction;
import com.primeleaf.krystal.web.action.cpanel.RecycleBinAction;
import com.primeleaf.krystal.web.action.cpanel.RecycleBinContentAction;
import com.primeleaf.krystal.web.action.cpanel.ReportsAction;
import com.primeleaf.krystal.web.action.cpanel.RestoreDocumentAction;
import com.primeleaf.krystal.web.action.cpanel.SetPermissionsAction;
import com.primeleaf.krystal.web.action.cpanel.SummaryReportAction;
import com.primeleaf.krystal.web.action.cpanel.UserAccessHistoryReportAction;
import com.primeleaf.krystal.web.action.cpanel.UsersAction;


/**
 * Author Rahul Kubadia
 */

public class ActionFactory {
	private static ActionFactory instance;
	private static final Map<String, Action> actions = new HashMap<String,Action>();
	public static final ArrayList<String> AJAXActions = new ArrayList<String>();
	public static synchronized ActionFactory getInstance() { 
		if (instance == null) { 
			instance = new ActionFactory();
			actions.put("/", new DefaultAction());
			actions.put("/login", new LoginAction());
			actions.put("/logout", new LogoutAction());
			actions.put("/forgotpassword", new ForgotPasswordAction());//Added By Saumil Shah
			
			actions.put("/stats", new StatsAction());
			
			actions.put("/console", new HomeAction());
			actions.put("/console/autocomplete", new AutoCompleteAction());

			actions.put("/console/changepassword", new ChangePasswordAction());
			actions.put("/console/myprofile", new MyProfileAction());
			actions.put("/console/preferences", new PreferencesAction());
			
			actions.put("/console/userdetails", new UserDetailsAction());
			actions.put("/console/deleteprofilepicture", new DeleteProfilePictureAction());
			
			actions.put("/console/search", new SearchAction());
			actions.put("/console/newdocument", new NewDocumentAction());


			actions.put("/console/searchdocumentclass", new SearchDocumentClassAction());
			
			
			actions.put("/console/opendocumentclass", new OpenDocumentClassAction());
			actions.put("/console/viewdocument", new ViewDocumentAction());
			actions.put("/console/mobiledocumentviewer", new MobileDocumentViewerAction());
			
			actions.put("/console/documentviewer", new DocumentViewerAction());
			actions.put("/console/editdocumentindexes", new EditDocumentIndexesAction());
			actions.put("/console/documentnotes",new DocumentNotesAction());
			actions.put("/console/accesshistory",new AccessHistoryAction());
			actions.put("/console/revisionhistory",new RevisionHistoryAction());

			actions.put("/console/downloaddocument",new DownloadDocumentAction());
			actions.put("/console/checkoutdocument",new CheckoutDocumentAction());
			actions.put("/console/cancelcheckout",new CancelCheckoutAction());
			actions.put("/console/checkindocument",new CheckInDocumentAction());
			actions.put("/console/sharedocument", new ShareDocumentAction());
			actions.put("/console/deletedocument",new DeleteDocumentAction());
			actions.put("/console/bulkdelete",new BulkDeleteDocumentAction());//Added by Rahul Kubadia on 27-Nov-2014 
			actions.put("/console/bulkdownload",new BulkDownloadAction());//Added by Rahul Kubadia on 28-Nov-2014 
			
			

			actions.put("/console/newbookmark", new NewBookmarkAction());
			actions.put("/console/deletebookmark", new DeleteBookmarkAction());
			actions.put("/console/profilepicture", new ProfilePictureAction());//15-June-2014 -Rahul Kubadia
			actions.put("/console/updateprofilepicture", new UpdateProfilePictureAction());//15-June-2014 -Rahul Kubadia
		
			
			/*Control Panel Actions start here*/
			actions.put("/cpanel", new ControlPanelAction());

			actions.put("/cpanel/users", new UsersAction());
			actions.put("/cpanel/newuser", new NewUserAction());
			actions.put("/cpanel/edituser", new EditUserAction());
			actions.put("/cpanel/deleteuser", new DeleteUserAction());
			actions.put("/cpanel/changeuserpassword", new ChangeUserPasswordAction());


			actions.put("/cpanel/managedocumentclasses", new ManageDocumentClassesAction());
			actions.put("/cpanel/newdocumentclass", new NewDocumentClassAction());
			actions.put("/cpanel/editdocumentclass", new EditDocumentClassAction());
			actions.put("/cpanel/deletedocumentclass", new DeleteDocumentClassAction());
			actions.put("/cpanel/classindexes", new ClassIndexesAction());
			actions.put("/cpanel/newclassindex", new NewClassIndexAction());
			actions.put("/cpanel/editclassindexes", new EditClassIndexesAction());
			actions.put("/cpanel/deleteclassindex", new DeleteClassIndexAction());
			actions.put("/cpanel/permissions", new PermissionsAction());
			actions.put("/cpanel/setpermissions", new SetPermissionsAction());
			
			actions.put("/cpanel/managecheckouts", new ManageCheckoutsAction());
			actions.put("/cpanel/cancelcheckoutadmin",new CancelCheckoutAdminAction());

			actions.put("/cpanel/reports", new ReportsAction());
			actions.put("/cpanel/summary", new SummaryReportAction());
			actions.put("/cpanel/useraccesshistory", new UserAccessHistoryReportAction());
			actions.put("/cpanel/documentclassaccesshistory", new DocumentClassAccessHistoryReportAction());
			
			actions.put("/cpanel/recyclebin", new RecycleBinAction());
			actions.put("/cpanel/recyclebincontent", new RecycleBinContentAction());
			actions.put("/cpanel/restoredocument", new RestoreDocumentAction());
			actions.put("/cpanel/purgedocument", new PurgeDocumentAction());
		} 
		return instance; 
	}

	public Action getAction(HttpServletRequest request){
		String strAction = request.getRequestURI();
		Action action = actions.get(strAction.toLowerCase());
		if(action == null){
			action = new Action404();
		}
		return action ;
	}

	private ActionFactory(){
		AJAXActions.add("/console/editdocumentindexes");
		AJAXActions.add("/console/attachments");
		AJAXActions.add("/console/documentnotes");
		AJAXActions.add("/console/revisionhistory");
		AJAXActions.add("/console/accesshistory");
		AJAXActions.add("/forgotpassword");
		AJAXActions.add("/console/workflowcaseaudittrail");
		AJAXActions.add("/console/newbookmark");
		AJAXActions.add("/console/documenttags");
		AJAXActions.add("/console/newdocumenttag");
		AJAXActions.add("/console/deletedocumenttag");
		AJAXActions.add("/console/addtodocumentset");
		AJAXActions.add("/console/sharedocument");
		AJAXActions.add("/console/viewdiscussion");
		AJAXActions.add("/console/viewannouncement");
		AJAXActions.add("/console/preferences");
		AJAXActions.add("/console/changepassword");
		AJAXActions.add("/cpanel/changeuserpassword");
		AJAXActions.add("/status");
	}
}

