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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;

/**
 * Author Rahul Kubadia
 */

public class ProfilePictureAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int imageWidth=96;
		int imageHeight=96;
		try{
			String username = request.getParameter("username")!=null?request.getParameter("username"):"ADMINISTRATOR";
			String imageSize = request.getParameter("size")!=null?request.getParameter("size"):"";
			if("large".equalsIgnoreCase(imageSize)){
				imageWidth=64;
				imageHeight=64;
			}
			if("medium".equalsIgnoreCase(imageSize)){
				imageWidth=48;
				imageHeight=48;
			}
			if("small".equalsIgnoreCase(imageSize)){
				imageWidth=24;
				imageHeight=24;
			}
			if("xs".equalsIgnoreCase(imageSize)){
				imageWidth=20;
				imageHeight=20;
			}
			
			BufferedImage originalImage = UserDAO.getInstance().getProfilePicture(username);
			BufferedImage mdImage = resizeImage(originalImage,BufferedImage.TYPE_INT_ARGB,imageWidth, imageHeight);
			response.setContentType("image/png");
			ImageIO.write(mdImage, "png",response.getOutputStream());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int IMG_WIDTH,int IMG_HEIGHT){
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}
}

