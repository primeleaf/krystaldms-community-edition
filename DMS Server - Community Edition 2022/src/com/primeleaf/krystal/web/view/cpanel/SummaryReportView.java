/**
 * Created On 09-Jan-2014
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

package com.primeleaf.krystal.web.view.cpanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class SummaryReportView extends WebView {

	public SummaryReportView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printSummaryReport();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel/reports\">System Reports</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Repository Content Summary</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printSummaryReport() throws Exception{
		printBreadCrumbs();
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}

		try{
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\"><i class=\"bi text-primary bi-bar-chart\"></i> Repository Content Summary</div>");
			out.println("<div class=\"card-body\">");

			out.println("<div class=\"row\">");

			out.println("<div class=\"col-lg-4\">");
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\">");
			out.println("<div class=\"d-flex justify-content-between\">");
			out.println("<div class=\"col-xs-4\">");
			out.println("<i class=\"bi text-primary bi-folder2-open h1\"></i>");
			out.println("</div>");
			out.println("<div class=\"col-xs-8 text-end\">");
			out.println("<h3>"+request.getAttribute("DOCUMENT_CLASSES")+"</h3>");
			out.println("<p >Document Classes</p>");
			out.println("</div>");
			out.println("</div>");//row
			out.println("</div>");//card-header
			out.println("</div>");//panel
			out.println("</div>");//col-lg-4


			out.println("<div class=\"col-lg-4\">");
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\">");
			out.println("<div class=\"d-flex justify-content-between\">");
			out.println("<div class=\"col-xs-6\">");
			out.println("<i class=\"bi h1 bi-file-earmark text-primary\"></i>");
			out.println("</div>");
			out.println("<div class=\"col-xs-6 text-end\">");
			out.println("<h3>"+request.getAttribute("DOCUMENTS")+"</h3>");
			out.println("<p >Documents</p>");
			out.println("</div>");
			out.println("</div>");//row
			out.println("</div>");//card-header
			out.println("</div>");//panel
			out.println("</div>");//col-lg-4

			out.println("<div class=\"col-lg-4\">");
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\">");
			out.println("<div class=\"d-flex justify-content-between\">");
			out.println("<div class=\"col-xs-6\">");
			out.println("<i class=\"bi bi-person text-primary h1\"></i>");
			out.println("</div>");
			out.println("<div class=\"col-xs-6 text-end\">");
			out.println("<h3>"+request.getAttribute("USERS")+"</h3>");
			out.println("<p >Users</p>");
			out.println("</div>");
			out.println("</div>");//row
			out.println("</div>");//card-header
			out.println("</div>");//panel
			out.println("</div>");//col-lg-4

			out.println("</div>");//row

			ArrayList<DocumentClass> documentClasses = 	(ArrayList<DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
			if(documentClasses.size() > 0){
				//charts rendering starts here
				out.println("<div class=\"card\">");
				out.println("<div class=\"card-header\">");
				out.println("<i class=\"bi text-primary bi-pie-chart \"></i> Charts");
				out.println("</div>");
				out.println("<div class=\"card-body\">");
				out.println("<div class=\"row\">");
				out.println("<div class=\"col-sm-6 text-center\">");
				out.println("<h3>Documents : "  + request.getAttribute("DOCUMENTS") +"</h3>");
				out.println("<canvas id=\"classchart\" height=\"100\"></canvas>");
				out.println("<div>");
				out.println("<script>");
				
				out.println("const ctx = document.getElementById(\'classchart\').getContext(\'2d\');");
				out.println("var dataLabels = []");
				
				for(DocumentClass documentClass : documentClasses){
					out.println("dataLabels.push(\""+StringEscapeUtils.escapeHtml4(documentClass.getClassName())+"\");");
				}
				
				out.println("var dataValues = []");
				
				for(DocumentClass documentClass : documentClasses){
					out.println("dataValues.push("+documentClass.getActiveDocuments()+");");
				}
				out.println("$(document).ready(function(){const myChart = new Chart(ctx, {");
				out.println("    type: \'pie\',");
				out.println("    data: {");
				out.println("        labels:dataLabels,");
				out.println("        datasets: [{");
				out.println("            data: dataValues,");
				out.println("            backgroundColor: [");
				out.println("                \'#0B62A4\', \'#3980B5\', \'#679DC6\', \'#95BBD7\', \'#B0CCE1\', \'#095791\', \'#095085\', \'#083E67\', \'#052C48\', \'#042135\'");
				out.println("            ],");
				out.println("            borderColor: [");
				out.println("               \'#fff\'");
				out.println("            ],");
				out.println("            borderWidth: 1");
				out.println("        }]");
				out.println("    },");
				out.println("    options: {");
				out.println("        legend: {");
				out.println("         display: false //This will do the task");
				out.println("      },"
						+ "aspectRatio:2.5");
				out.println("    }");
				out.println("});});");
				
				out.println("</script>");
				out.println("</div>");
				out.println("</div>");//col-sm-6


				double totalSize = (Double)request.getAttribute("TOTALSIZE");
				out.println("<div class=\"col-sm-6 text-center\">");
				out.println("<h3>Total Size : " + StringHelper.formatSizeText(totalSize)+ "</h3>");
				out.println("<canvas id=\"sizechart\" height=\"100\"></canvas>");
				out.println("<div>");
				out.println("<script>");
				
				out.println("const ctx1 = document.getElementById(\'sizechart\').getContext(\'2d\');");
				out.println("var dataLabels1 = []");
				
				for(DocumentClass documentClass : documentClasses){
					out.println("dataLabels1.push(\""+StringEscapeUtils.escapeHtml4(documentClass.getClassName())+"\");");
				}
				
				out.println("var dataValues1 = []");
				
				for(DocumentClass documentClass : documentClasses){
					out.println("dataValues1.push("+(Double) request.getAttribute(documentClass.getClassName()+"_SIZE")+");");
				}
				out.println("$(document).ready(function(){const myChart1 = new Chart(ctx1, {");
				out.println("    type: \'pie\',");
				out.println("    data: {");
				out.println("        labels:dataLabels1,");
				out.println("        datasets: [{");
				out.println("            data: dataValues1,");
				out.println("            backgroundColor: [");
				out.println("                \'#0B62A4\', \'#3980B5\', \'#679DC6\', \'#95BBD7\', \'#B0CCE1\', \'#095791\', \'#095085\', \'#083E67\', \'#052C48\', \'#042135\'");
				out.println("            ],");
				out.println("            borderColor: [");
				out.println("               \'#fff\'");
				out.println("            ],");
				out.println("            borderWidth: 1");
				out.println("        }]");
				out.println("    },");
				out.println("    options: {");
				out.println("        legend: {");
				out.println("         display: false //This will do the task");
				out.println("      },");
				out.println("tooltips: {");
				out.println("callbacks: {");
				out.println("	label: function(tooltipItem, value) {"
						+ "			var allData = value.datasets[tooltipItem.datasetIndex].data; "
						+"			var tooltipLabel = value.labels[tooltipItem.index];"
						+ "			var size = allData[tooltipItem.index];"		
						+ " 		var result = size + ' bytes';"
						+ " 		if(size > 1024) { result = parseFloat(size/1024).toFixed(1)+ ' KB'} "
						+ " 		if(size > 1048576) { result = parseFloat(size/1048576).toFixed(1)+' MB'} "
						+ " 		if(size > 1073741824) { result = parseFloat(size/1073741824).toFixed(1)+' GB'} "
						+ " 		return  tooltipLabel + ' : '   + result; "
						+ "		},");
				out.println("}");
				out.println("},");
				out.println("aspectRatio:2.5");
				out.println("    }");
				out.println("});});");
				
				out.println("</script>");
				out.println("</div>");
				out.println("</div>");//col-sm-6
				out.println("</div>");//row

				if(documentClasses.size() > 0){
					out.println("<div class=\"text-center\">");
					out.println("<div>");
					
					LinkedHashMap<String,Integer> chartValues =(LinkedHashMap<String,Integer>) request.getAttribute(documentClasses.get(0).getClassName() + "_CHARTVALUES");
					
					out.println("<canvas id=\"uploadchart\" height=\"300\"></canvas>");
					out.println("<script>");
					out.println("$(document).ready(function(){var ctx4 = document.getElementById(\"uploadchart\");");
					out.println("var myChart3 = new Chart(ctx4, {");
					out.println("type: 'line',");
					out.println("data: {");
					out.println("labels: [");
					for(String month : chartValues.keySet()){
						out.print("'"+month+"',");
					}
					out.println("],");
					out.println("datasets: [");
					int i = 0;
					for(DocumentClass documentClass : documentClasses){
						chartValues = (LinkedHashMap<String,Integer>) request.getAttribute(documentClass.getClassName() + "_CHARTVALUES");
						out.println("{label: '"+StringEscapeUtils.escapeHtml4(documentClass.getClassName())+"',");
						out.println("data: [");
						for(String key : chartValues.keySet()){
							int value = chartValues.get(key)!=null?chartValues.get(key):0;
							out.print(value + ",");
						}
						out.println("],"
								+ "fill:false,");
						out.println("backgroundColor : lineBackgroundColors["+i  % 10 +"],");
						out.println("borderColor :lineBorderColors["+i % 10 +"],");
						out.println("borderWidth: 1");
						out.println("},");
						i++;
					}
					out.println("]");
					out.println("},");
					out.println("options: {");
					out.println("legend: {");
					out.println("display: false,");
					out.println("},scales:{"
							+ "y:{"
							+ "min:0,"
							+ "},"
							+ "yAxes:[{"
							+ "ticks:{"
							+ "stepSize:1"
							+ "}"
							+ "}]"
							+ "},");
					out.println("responsive:true,");
					out.println("maintainAspectRatio: false,"
							+ "tooltips: {\r\n"
							+ "      mode: 'index',\r\n"
							+ "      intersect: false\r\n"
							+ "   },\r\n"
							+ "   hover: {\r\n"
							+ "      mode: 'index',\r\n"
							+ "      intersect: false\r\n"
							+ "   }");
					out.println("}");
					out.println("});});");
					out.println("</script>");
					
					out.println("</div>");//line-chart
					out.println("</div>");//
				}

				out.println("</div>");//card-body
				out.println("</div>");//panel
			}
			//charts rendering ends here

			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header\">");
			out.println("<i class=\"bi text-primary bi-folder2-open \"></i> Document Classes");
			out.println("</div>");
			ArrayList<DocumentClass> documentClassList = (ArrayList<DocumentClass>) request.getAttribute("DOCUMENTCLASSLIST");
			if(documentClassList.size() > 0 ){
				out.println("<div class=\"table-responsive\">");
				out.println("<table class=\"table table-bordered table-stripped\">");
				out.println("<thead><tr>");
				out.println("<th>Document Class</th>");
				out.println("<th class=\"text-center\">Documents</th>");
				out.println("<th class=\"text-end\">Total Size</th></tr></thead>");
				out.println("<tbody>");
				for (DocumentClass documentClass : documentClassList) {
					int documentCount =  documentClass.getActiveDocuments();
					double documentSize = (Double) request.getAttribute(documentClass.getClassName()+"_SIZE");
					String ownerName = (String) request.getAttribute(documentClass.getClassName()+"_OWNER" );
					out.println("<tr>");
					out.println("<td style=\"width:80%;\">");
					out.println("<h4 class=\"text-primary\">" + StringEscapeUtils.escapeHtml4(documentClass.getClassName()) + "</h4>");
					out.println("<h5>" + StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</h5>");
					out.println("<p>");
					out.println("<i>Created By " + ownerName);
					out.println(" , " +StringHelper.getFriendlyDateTime(documentClass.getCreated()) +"</i>");
					out.println("</p>");
					out.println("</td>");

					out.println("<td style=\"width:10%;\" class=\"text-center\">");
					out.println("<h4>" + documentCount + "</h4>");
					out.println("</td>");

					out.println("<td class=\"text-end\">");
					out.println("<h4>" + StringHelper.formatSizeText(documentSize) + "</h4>");
					out.println("</td>");
					out.println("</tr>");//row

				}// for
				out.println("</tbody>");
				out.println("</table>");
				out.println("</div>");
			}else{
				out.println("<div class=\"card-body\">"); //panel
				out.println("No document class found");
				out.println("</div>"); //card-body
			}
			out.println("</div>"); //panel

			out.println("</div>");
			out.println("</div>");
			out.println("</div>");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}