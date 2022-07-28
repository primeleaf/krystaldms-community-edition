/**
 * Created On Sep 11, 2012
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

package com.primeleaf.krystal.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.dao.DocumentDAO;
import com.primeleaf.krystal.model.vo.Document;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;

/**
 * This class processes all the documents which are expiring today and will mark them as expired.
 * @author Rahul Kubadia
 * @since 5.1
 */
public class ExpiryProcessor  extends TimerTask {

	/** instance of the Logger */
	public final Logger kLogger = Logger.getLogger(this.getClass().getPackage().getName());

	public ExpiryProcessor(){

	}

	public void run(){
		process();
	}

	private void process(){
		try{
			kLogger.info("Document expiry processor started.");
			ArrayList<DocumentClass> documentClasses = DocumentClassDAO.getInstance().readDocumentClasses("");
			ArrayList<Document> expiringDocuments = null;
			int expiryCount = 0;
			for(DocumentClass documentClass : documentClasses){
				
				Calendar cal=Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND,0);
				java.sql.Date today = new java.sql.Date(cal.getTimeInMillis());

				String sqlSelectDocuments =  "SELECT * FROM DOCUMENTS WHERE CLASSID = "+ documentClass.getClassId() +" AND STATUS IN('" +Hit.STATUS_AVAILABLE + "' ,' " + Hit.STATUS_LOCKED+ "')  AND  EXPIRY  <='" + today + "'";  
				expiringDocuments = DocumentDAO.getInstance().readDocuments(sqlSelectDocuments);
				kLogger.info(expiringDocuments.size() + " documents expiring in document class " + documentClass.getClassDescription());
				expiryCount = 0;
				for(Document document : expiringDocuments){
					try{
						document = DocumentDAO.getInstance().readDocumentById(document.getDocumentId());
						document.setStatus(Hit.STATUS_EXPIRED);
						DocumentDAO.getInstance().updateDocument(document);
						DocumentClassDAO.getInstance().decreaseActiveDocumentCount(documentClass);
						expiryCount++;
					}catch(Exception ex){
						kLogger.severe(ex.getMessage());
						ex.printStackTrace(System.out);
					}
				}
				kLogger.info(expiryCount + " documents marked as expired in document class " + documentClass.getClassDescription());
			}
			kLogger.info("Document expiry processor finished.");
		} catch (Exception e) {
			kLogger.severe(e.getMessage());
			e.printStackTrace(System.out);
		}
	}
}

