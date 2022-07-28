/**
 * Created on Aug 9, 2004
 *
 * Copyright 2005 by Arysys Technologies (P) Ltd.,
 * #3,Shop line,
 * Sasmira Marg,
 * Worli,Mumbai 400 025
 * India
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Arysys Technologies (P) Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Arysys Technologies (P) Ltd.
 *
 */


package com.primeleaf.krystal.util;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * @author Rahul Kubadia
 *
 */


public class StringHelper {

	/**
	 * Pad the beginning of the given String with spaces until
	 * the String is of the given length.
	 *
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String prepad(String s, int length){
		return prepad(s, length, ' ');
	}

	/**
	 * Prepend the given character to the String until
	 * the result is the desired length.
	 * <p>
	 * If a String is longer than the desired length,
	 * it will not be trunkated, however no padding
	 * will be added.
	 *
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @param c padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String prepad(String s, int length, char c){
		int needed = length - s.length();
		if (needed <= 0){
			return s;
		}
		StringBuffer sb = new StringBuffer(length);
		for (int i=0; i<needed; i++){
			sb.append(c);
		}
		sb.append(s);
		return (sb.toString());
	}

	/**
	 * Pad the end of the given String with spaces until
	 * the String is of the given length.
	 *
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String postpad(String s, int length){
		return postpad(s, length, ' ');
	}

	/**
	 * Append the given character to the String until
	 * the result is  the desired length.
	 * <p>
	 * If a String is longer than the desired length,
	 * it will not be trunkated, however no padding
	 * will be added.
	 *
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @param c padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 */
	public static String postpad(String s, int length, char c){
		int needed = length - s.length();
		if (needed <= 0){
			return s;
		}
		StringBuffer sb = new StringBuffer(length);
		sb.append(s);
		for (int i=0; i<needed; i++){
			sb.append(c);
		}
		return (sb.toString());
	}

	/**
	 * Split the given String into tokens.
	 * <P>
	 * This method is meant to be similar to the split
	 * function in other programming languages but it does
	 * not use regular expressions.  Rather the String is
	 * split on a single String literal.
	 * <P>
	 * Unlike java.util.StringTokenizer which accepts
	 * multiple character tokens as delimiters, the delimiter
	 * here is a single String literal.
	 * <P>
	 * Each null token is returned as an empty String.
	 * Delimiters are never returned as tokens.
	 * <P>
	 * If there is no delimiter because it is either empty or
	 * null, the only element in the result is the original String.
	 * <P>
	 * StringHelper.split("1-2-3", "-");<br>
	 * result: {"1", "2", "3"}<br>
	 * StringHelper.split("-1--2-", "-");<br>
	 * result: {"", "1", ,"", "2", ""}<br>
	 * StringHelper.split("123", "");<br>
	 * result: {"123"}<br>
	 * StringHelper.split("1-2---3----4", "--");<br>
	 * result: {"1-2", "-3", "", "4"}<br>
	 *
	 * @param s String to be split.
	 * @param delimiter String literal on which to split.
	 * @return an array of tokens.
	 * @throws NullPointerException if s is null.
	 */
	public static String[] split(String s, String delimiter){
		int delimiterLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0){
			// it is not inherently clear what to do if there is no delimiter
			// On one hand it would make sense to return each character because
			// the null String can be found between each pair of characters in
			// a String.  However, it can be found many times there and we don't
			// want to be returning multiple null tokens.
			// returning the whole String will be defined as the correct behavior
			// in this instance.
			return new String[] {s};
		}

		// a two pass solution is used because a one pass solution would
		// require the possible resizing and copying of memory structures
		// In the wkst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.

		int count;
		int start;
		int end;

		// Scan s and count the tokens.
		count = 0;
		start = 0;
		while((end = s.indexOf(delimiter, start)) != -1) {
			count++;
			start = end + delimiterLength;
		}
		count++;

		// allocate an array to return the tokens,
		// we now know how big it should be
		String[] result = new String[count];

		// Scan s again, but this time pick out the tokens
		count = 0;
		start = 0;
		while((end = s.indexOf(delimiter, start)) != -1) {
			result[count] = (s.substring(start, end));
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}

	/**
	 * Replace occurances of a substring.
	 *
	 * StringHelper.replace("1-2-3", "-", "|");<br>
	 * result: "1|2|3"<br>
	 * StringHelper.replace("-1--2-", "-", "|");<br>
	 * result: "|1||2|"<br>
	 * StringHelper.replace("123", "", "|");<br>
	 * result: "123"<br>
	 * StringHelper.replace("1-2---3----4", "--", "|");<br>
	 * result: "1-2|-3||4"<br>
	 * StringHelper.replace("1-2---3----4", "--", "---");<br>
	 * result: "1-2----3------4"<br>
	 *
	 * @param s String to be modified.
	 * @param find String to find.
	 * @param replace String to replace.
	 * @return a string with all the occurances of the string to find replaced.
	 * @throws NullPointerException if s is null.
	 */
	public static String replace(String s, String find, String replace){
		int findLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		int stringLength = s.length();
		if (find == null || (findLength = find.length()) == 0){
			// If there is nothing to find, we won't try and find it.
			return s;
		}
		if (replace == null){
			// a null string and an empty string are the same
			// for replacement purposes.
			replace = "";
		}
		int replaceLength = replace.length();

		// We need to figure out how long our resulting string will be.
		// This is required because without it, the possible resizing
		// and copying of memory structures could lead to an unacceptable runtime.
		// In the wkst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.
		int length;
		if (findLength == replaceLength){
			// special case in which we don't need to count the replacements
			// because the count falls out of the length formula.
			length = stringLength;
		} else {
			int count;
			int start;
			int end;

			// Scan s and count the number of times we find our target.
			count = 0;
			start = 0;
			while((end = s.indexOf(find, start)) != -1) {
				count++;
				start = end + findLength;
			}
			if (count == 0){
				// special case in which on first pass, we find there is nothing
				// to be replaced.  No need to do a second pass or create a string buffer.
				return s;
			}
			length = stringLength - (count * (replaceLength - findLength));
		}

		int start = 0;
		int end = s.indexOf(find, start);
		if (end == -1){
			// nothing was found in the string to replace.
			// we can get this if the find and replace strings
			// are the same length because we didn't check before.
			// in this case, we will return the original string
			return s;
		}
		// it looks like we actually have something to replace
		// *sigh* allocate memory for it.
		StringBuffer sb = new StringBuffer(length);

		// Scan s and do the replacements
		while (end != -1) {
			sb.append(s.substring(start, end));
			sb.append(replace);
			start = end + findLength;
			end = s.indexOf(find, start);
		}
		end = stringLength;
		sb.append(s.substring(start, end));

		return (sb.toString());
	}

	/**
	 * Replaces characters that may be confused by a HTML
	 * parser with their equivalent character entity references.
	 * <p>
	 * Any data that will appear as text on a web page should
	 * be be escaped.  This is especially important for data
	 * that comes from untrusted sources such as internet users.
	 * A common mistake in CGI programming is to ask a user for
	 * data and then put that data on a web page.  For example:<pre>
	 * Server: What is your name?
	 * User: &lt;b&gt;Joe&lt;b&gt;
	 * Server: Hello <b>Joe</b>, Welcome</pre>
	 * If the name is put on the page without checking that it doesn't
	 * contain HTML code or without sanitizing that HTML code, the user
	 * could reformat the page, insert scripts, and control the the
	 * content on your webserver.
	 * <p>
	 * This method will replace HTML characters such as &gt; with their
	 * HTML entity reference (&amp;gt;) so that the html parser will
	 * be sure to interpret them as plain text rather than HTML or script.
	 * <p>
	 * This method should be used for both data to be displayed in text
	 * in the html document, and data put in form elements. For example:<br>
	 * <code>&lt;html&gt;&lt;body&gt;<i>This in not a &amp;lt;tag&amp;gt;
	 * in HTML</i>&lt;/body&gt;&lt;/html&gt;</code><br>
	 * and<br>
	 * <code>&lt;form&gt;&lt;input type="hidden" name="date" value="<i>This data could
	 * be &amp;quot;malicious&amp;quot;</i>"&gt;&lt;/form&gt;</code><br>
	 * In the second example, the form data would be properly be resubmitted
	 * to your cgi script in the URLEncoded format:<br>
	 * <code><i>This data could be %22malicious%22</i></code>
	 *
	 * @param s String to be escaped
	 * @return escaped String
	 * @throws NullPointerException if s is null.
	 */
	public static String escapeHTML(String s){
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i=0; i<length; i++){
			char c = s.charAt(i);
			switch(c){
			case '\"':
			case '\'':{
				newLength += 5;
			} break;
			case '&':{
				newLength += 4;
			} break;
			case '<':
			case '>':{
				newLength += 3;
			} break;
			}
		}
		if (length == newLength){
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i=0; i<length; i++){
			char c = s.charAt(i);
			switch(c){
			case '\"':{
				sb.append("&quot;");
			} break;
			case '\'':{
				sb.append("&apos;");
			} break;
			case '&':{
				sb.append("&amp;");
			} break;
			case '<':{
				sb.append("&lt;");
			} break;
			case '>':{
				sb.append("&gt;");
			} break;
			default: {
				sb.append(c);
			}
			}
		}
		return sb.toString();
	}

	/**
	 * Replaces characters that may be confused by an SQL
	 * parser with their equivalent escape characters.
	 * <p>
	 * Any data that will be put in an SQL query should
	 * be be escaped.  This is especially important for data
	 * that comes from untrusted sources such as internet users.
	 * <p>
	 * For example if you had the following SQL query:<br>
	 * <code>"SELECT * FROM adresses WHERE name='" + name + "' AND private='N'"</code><br>
	 * Without this function a user could give <code>" OR 1=1 OR ''='"</code>
	 * as their name causing the query to be:<br>
	 * <code>"SELECT * FROM adresses WHERE name='' OR 1=1 OR ''='' AND private='N'"</code><br>
	 * which will give all adresses, including private ones.<br>
	 * Correct usage would be:<br>
	 * <code>"SELECT * FROM adresses WHERE name='" + StringHelper.escapeSQL(name) + "' AND private='N'"</code><br>
	 * <p>
	 * Another way to avoid this problem is to use a PreparedStatement
	 * with appropriate placeholders.
	 *
	 * @param s String to be escaped
	 * @return escaped String
	 * @throws NullPointerException if s is null.
	 */
	public static String escapeSQL(String s){
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i=0; i<length; i++){
			char c = s.charAt(i);
			switch(c){
			case '\\':
			case '\"':
			case '\'':
			case '0':{
				newLength += 1;
			} break;
			}
		}
		if (length == newLength){
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i=0; i<length; i++){
			char c = s.charAt(i);
			switch(c){
			case '\\':{
				sb.append("\\\"");
			} break;
			case '\"':{
				sb.append("\\\"");
			} break;
			case '\'':{
				sb.append("\\\'");
			} break;
			case '0':{
				sb.append("\\0");
			} break;
			default: {
				sb.append(c);
			}
			}
		}
		return sb.toString();
	}

	/**
	 * Replaces characters that are not allowed in a Java style
	 * string literal with their escape characters.  Specifically
	 * quote ("), single quote ('), new line (\n), carriage return (\r),
	 * and backslash (\), and tab (\t) are escaped.
	 *
	 * @param s String to be escaped
	 * @return escaped String
	 * @throws NullPointerException if s is null.
	 */
	public static String escapeJavaLiteral(String s){
		int length = s.length();
		int newLength = length;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i=0; i<length; i++){
			char c = s.charAt(i);
			switch(c){
			case '\"':
			case '\'':
			case '\n':
			case '\r':
			case '\t':
			case '\\':{
				newLength += 1;
			} break;
			}
		}
		if (length == newLength){
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i=0; i<length; i++){
			char c = s.charAt(i);
			switch(c){
			case '\"':{
				sb.append("\\\"");
			} break;
			case '\'':{
				sb.append("\\\'");
			} break;
			case '\n':{
				sb.append("\\n");
			} break;
			case '\r':{
				sb.append("\\r");
			} break;
			case '\t':{
				sb.append("\\t");
			} break;
			case '\\':{
				sb.append("\\\\");
			} break;
			default: {
				sb.append(c);
			}
			}
		}
		return sb.toString();
	}

	public static String formatDate(String dateString ,  String FORMAT){
		try{
			if(dateString.trim().length() < 12){
				dateString += " 00:00:00";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateString);
			sdf.applyPattern(FORMAT);
			return sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	public static String formatDate(String dateString){
		try{
			if(dateString.trim().length() < 12){
				dateString += " 00:00:00";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateString);
			sdf.applyPattern("dd-MMM-yyyy HH:mm");
			return sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * This method returns current date in specific format given
	 * @author Rahul Kubadia
	 * @since 4.1
	 * @date 08-Feb-2012
	 * @param FORMAT
	 * @return current date in given format
	 */
	public static String getCurrentDate(final String FORMAT){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
			Calendar c = Calendar.getInstance();
			return sdf.format(c.getTime());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}


	public static String formatSize(double fileSize){
		DecimalFormat onePlace = new DecimalFormat("0.0");

		String result = "";
		result = "<h1>" + (int)fileSize +  "</h1> bytes";
		double length=0.0;
		if(fileSize  > 1024){
			length = fileSize/1024;
			result = "<h1>" +  onePlace.format(length) + "</h1> kilo bytes";
		}
		if(fileSize > 1048576){
			length = fileSize/1048576;
			result = "<h1>" +  onePlace.format(length) + "</h1> mega bytes";
		}
		if(fileSize > 1073741824){
			length = fileSize / 1073741824;
			result = "<h1>" + onePlace.format(length) + "</h1> giga bytes";
		}

		return result;
	}

	public static String formatSizeText(double fileSize){
		DecimalFormat onePlace = new DecimalFormat("0.0");
		String result = "";
		result = "" + (int)fileSize +  " bytes";
		double length=0.0;
		if(fileSize  > 1024){
			length = fileSize/1024;
			result = "" +  onePlace.format(length) + " KB";
		}
		if(fileSize > 1048576){
			length = fileSize/1048576;
			result = "" +  onePlace.format(length) + " MB";
		}
		if(fileSize > 1073741824){
			length = fileSize / 1073741824;
			result = "" + onePlace.format(length) + " GB";
		}
		return result;
	}
	public static String encodeString(String password){
		return Base64Coder.encode(password);
	}

	public static String decodeString(String password){
		return Base64Coder.decode(password);
	}

	public static String getCurrentDate(){
		String s ="";
		Format formatter;
		Date rightNow = new Date();
		formatter = new SimpleDateFormat("EEEE , dd MMM yyyy");
		s = formatter.format(rightNow);
		return s;
	}
	public static String getCurrentDateTime(){
		String s ="";
		Format formatter;
		Date rightNow = new Date();
		formatter = new SimpleDateFormat("EEEE , dd MMM yyyy HH:mm:ss");
		s = formatter.format(rightNow);
		return s;
	}
	public static boolean isGreaterThanToday(String dateString){
		try{
			if(dateString.trim().length() < 12){
				dateString += " 00:00:00";
			}
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dueDate = sdf.parse(dateString);
			GregorianCalendar calendar = new GregorianCalendar();
			Date today = calendar.getTime();
			if(dueDate.getTime() < today.getTime()){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static Date getDate(String dateString){
		if(dateString == null || dateString.trim().length() <=0) {
			return null;
		}

		Date date = new Date();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateTime(String dateString){
		if(dateString == null || dateString.trim().length() <=0) {
			return null;
		}
		Date date = new Date();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * This function converts the current date string to appropirate database string representation 
	 * @param dateString
	 * @return
	 */
	public static String formatDate(Date dateString){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			return sdf.format(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	public static String formatDate(Date dateString,String FORMAT){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern(FORMAT);
			return sdf.format(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	public static long getTime(String dateTimeString){
		long time = 0;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateTimeString);
			time = date.getTime();
		}catch (Exception e) {
			e.printStackTrace();
			time = 0;
		}
		return time;
	}
	public static String formatDateString(String dateString){
		try{
			if("".equals(dateString) || dateString == null){
				return null;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateString);

			sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	/**
	* This function takes the extension and returns the associated font-awsome icon tag
	 * @param extension extension of the file
	 * @return font-awsome icon tag
	 * @author Rahul Kubadia
	 * @since 14.0 (2020) (3-May-2019)
	 */
	public static String getIconFileTag(String extension) {
		String iconFileTag = "";
		switch(extension.toUpperCase()) {
		case "PDF":
			iconFileTag = "<i class=\"bi bi-file-earmark-pdf text-danger h6\"></i>";
			break;
		case "TIF":
		case "TIFF":
			iconFileTag = "<i class=\"bi bi-file-earmark-richtext text-danger h6\"></i>";
			break;
		case "DWG":
		case "DXF":
		case "DGN" :
		case "STL" :
		case "DWF" :
			iconFileTag = "<i class=\"bi bi-file-earmark-post text-danger h6\"></i>";
			break;
		case "DOC":
		case "DOCX":
			iconFileTag = "<i class=\"bi bi-file-earmark-word text-primary h6\"></i>";
			break;
		case "XLS":
		case "XLSX":
			iconFileTag = "<i class=\"bi bi-file-earmark-excel text-success h6\"></i>";
			break;
		case "PPT":
		case "PPTX":
		case "PPS":
		case "PPSX":
			iconFileTag = "<i class=\"bi bi-file-earmark-ppt text-danger h6\"></i>";
			break;
		case "JPG":
		case "JPEG":
		case "PNG":
		case "BMP":
		case "GIF":
			iconFileTag = "<i class=\"bi bi-file-earmark-image text-primary h6\"></i>";
			break;
		case "MOV":
		case "MP4":
		case "AVI":
		case "OGG":
		case "WEBM":
		case "MPG":
		case "MPEG":
			iconFileTag = "<i class=\"bi bi-file-earmark-play text-primary h6\"></i>";
			break;
		case "MP3":
			iconFileTag = "<i class=\"bi bi-file-earmark-music text-primary h6\"></i>";
			break;
		case "TAR":
		case "ZIP":
			iconFileTag = "<i class=\"bi bi-file-earmark-zip text-primary h6\"></i>";
			break;
		case "TXT":
		case "RTF":
			iconFileTag = "<i class=\"bi bi-file-earmark-richtext text-primary h6\"></i>";
			break;
		default:
			iconFileTag = "<i class=\"bi bi-file-earmark h6\"></i>";
			break;
		}
		return iconFileTag;
	}

	public static long ipToLong(InetAddress ip) {  
		byte[] octets = ip.getAddress();         
		long result = 0;         
		for (byte octet : octets) {     
			result <<= 8;         
			result |= octet & 0xff;    
		}         return result; 
	} 

	public static String getFriendlyDateTime(String dateString){
		return getFriendlyTime(getDateTime(dateString));
	}

	private static String getFriendlyTime(Date dateTime) {
		StringBuffer sb = new StringBuffer();
		Date current = Calendar.getInstance().getTime();
		long diffInSeconds = (current.getTime() - dateTime.getTime()) / 1000;

		long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
		long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
		long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
		long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
		long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
		long years = (diffInSeconds = (diffInSeconds / 12));

		if (years > 0) {
			if (years == 1) {
				sb.append("a year");
			} else {
				sb.append(years + " years");
			}
			if (years <= 6 && months > 0) {
				if (months == 1) {
					sb.append(" and a month");
				} else {
					sb.append(" and " + months + " months");
				}
			}
		} else if (months > 0) {
			if (months == 1) {
				sb.append("a month");
			} else {
				sb.append(months + " months");
			}
			if (months <= 6 && days > 0) {
				if (days == 1) {
					sb.append(" and a day");
				} else {
					sb.append(" and " + days + " days");
				}
			}
		} else if (days > 0) {
			if (days == 1) {
				sb.append("a day");
			} else {
				sb.append(days + " days");
			}
			if (days <= 3 && hrs > 0) {
				if (hrs == 1) {
					sb.append(" and an hour");
				} else {
					sb.append(" and " + hrs + " hours");
				}
			}
		} else if (hrs > 0) {
			if (hrs == 1) {
				sb.append("an hour");
			} else {
				sb.append(hrs + " hours");
			}
			if (min > 1) {
				sb.append(" and " + min + " minutes");
			}
		} else if (min > 0) {
			if (min == 1) {
				sb.append("a minute");
			} else {
				sb.append(min + " minutes");
			}
			if (sec > 1) {
				sb.append(" and " + sec + " seconds");
			}
		} else {
			if (sec <= 1) {
				sb.append("about a second");
			} else {
				sb.append("about " + sec + " seconds");
			}
		}

		sb.append(" ago");
		return sb.toString();
	}

	public static String generateAuthToken(){
		String auhtTokenCharacters ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String authToken="";
		int pos=0;
		for(int i=0;i<128;i++){
			pos = (int) Math.floor(Math.random() * auhtTokenCharacters.length());
			authToken+=auhtTokenCharacters.charAt(pos);
		}
		return authToken;
	}
	public static long calculateDays(Date dateEarly, Date dateLater) {  
		return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);  
	} 
}