/** Created on May 8, 2008
 *
 * Copyright 2011 by Primeleaf Consulting [P] Ltd.
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consulting [P] Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting [P] Ltd.
 *
 * Author : Rahul Kubadia
 * 
 */


package com.primeleaf.krystal.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailMessage {

	private String subject="";
	private String to="";
	private String from="";
	private String message="";
	private File attachmentFile;
	private boolean hasAttachment=false;

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the attachmentFile
	 */
	public File getAttachmentFile() {
		return attachmentFile;
	}
	/**
	 * @param attachmentFile the attachmentFile to set
	 */
	public void setAttachmentFile(File attachmentFile) {
		this.attachmentFile = attachmentFile;
		this.hasAttachment = true;
	}

	/**
	 * @return the hasAttachment
	 */
	public boolean isHasAttachment() {
		return hasAttachment;
	}
	/**
	 * @param hasAttachment the hasAttachment to set
	 */
	public void setHasAttachment(boolean hasAttachment) {
		this.hasAttachment = hasAttachment;
	}

	public void send()throws Exception{
		try{
			Authenticator auth = new PasswordAuthenticator();
			Properties props = new Properties();
			props.put("mail.smtp.host", ConfigParser.getSMTPHost());//Modified By Saumil Shah
			props.put("mail.smtp.port", ConfigParser.getSMTPPort());//Modified By Saumil Shah
			props.put("mail.smtp.auth", "TRUE");
			if(ConfigParser.isSMTPTLSEnabled()) {//Added By Saumil Shah
				props.put("mail.smtp.starttls.enable","TRUE");
			}
			Session objSess = Session.getInstance(props,auth);
			MimeMessage objMessage = new MimeMessage(objSess);
			objMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
			InternetAddress objSenAdd = new InternetAddress(ConfigParser.getSMTPUsername(),"KRYSTAL DMS - Community Edition");
			objMessage.setFrom(objSenAdd);
			InternetAddress objReciAdd = new InternetAddress(getTo());
			objMessage.addRecipient(Message.RecipientType.TO, objReciAdd);
			objMessage.setSubject(getSubject());
			objMessage.setFrom(ConfigParser.getSMTPUsername());
			MimeBodyPart mimeBodyPartText = new MimeBodyPart();
			String message = getMessage();

			message+="<p><small>If you are not the intended recipient (or have received this e-mail in error) please notify the sender immediately and destroy this e-mail. Any unauthorized copying, disclosure or distribution of the material in this e-mail is strictly forbidden. Please do not reply to this mail as it is a computer generated mail. </small></p>";
			mimeBodyPartText.setContent(message, "text/html; charset=UTF-8");

			// create the Multipart and add its parts to it
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mimeBodyPartText);

			if(hasAttachment){
				//	create the second message part
				MimeBodyPart mimeBodyPartAttachment = new MimeBodyPart();

				// attach the file to the message
				FileDataSource fds = new FileDataSource(getAttachmentFile());
				mimeBodyPartAttachment.setDataHandler(new DataHandler(fds));
				mimeBodyPartAttachment.setFileName(fds.getName());
				mp.addBodyPart(mimeBodyPartAttachment);

			}
			// add the Multipart to the message
			objMessage.setContent(mp);

			// set the Date: header
			objMessage.setSentDate(new Date());
			Transport.send(objMessage);
		}catch(Exception e){
			System.out.println(e);
		}
	}

	class PasswordAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			//Modified By Saumil Shah
			return new PasswordAuthentication(ConfigParser.getSMTPUsername(),ConfigParser.getSMTPPassword());
		}
	}
}
