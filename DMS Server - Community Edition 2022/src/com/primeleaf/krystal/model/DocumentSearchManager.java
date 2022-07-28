/**
 * Created On Feb 19, 2014
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

package com.primeleaf.krystal.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.primeleaf.krystal.model.dao.IndexDefinitionDAO;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.Hit;
import com.primeleaf.krystal.model.vo.HitList;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.model.vo.SearchFilter;
import com.primeleaf.krystal.util.DBStringHelper;

/**
 * Author Rahul Kubadia
 */

public class DocumentSearchManager {
	private static DocumentSearchManager instance;

	public static synchronized DocumentSearchManager getInstance() { 
		if (instance==null) { 
			instance = new DocumentSearchManager(); 
		} 
		return instance; 
	}

	private DocumentSearchManager() {
		super();
	}

	public ArrayList<Hit> searchDocumentClass(DocumentClass documentClass,ArrayList<SearchFilter> searchFilterList, boolean searchAllRevisions,String orderBy, String orderType) throws Exception {
		ArrayList<Hit> results = new ArrayList<Hit>();
		String indexTableName = documentClass.getIndexTableName();
		StringBuffer hitListQuery =  new StringBuffer("SELECT DOCUMENTS.DOCUMENTID, DOCUMENTS.CLASSID, DOCUMENTS.DOCUMENTTYPE,"); 
		hitListQuery.append("DOCUMENTS.STATUS, DOCUMENTS.CREATED,DOCUMENTS.MODIFIED,DOCUMENTS.EXT,");
		hitListQuery.append("DOCUMENTS.LASTACCESS, DOCUMENTS.CREATEDBY, DOCUMENTS.EXPIRY, ");
		hitListQuery.append("DOCUMENTS.MODIFIEDBY , DOCUMENTREVISIONS.LENGTH, "); 
		hitListQuery.append("DOCUMENTS.REVISIONID AS HEADREVISION ," + indexTableName + ".REVISIONID,");

		for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
			hitListQuery.append(indexTableName + ".I" + indexDefinition.getIndexColumnName() + ", ");
		}
		hitListQuery.append("DOCUMENTS.HASNOTE  FROM " + indexTableName);

		if(searchAllRevisions){ 
			hitListQuery.append(" INNER JOIN DOCUMENTS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTS.DOCUMENTID ");
		}else{
			hitListQuery.append(" INNER JOIN DOCUMENTS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTS.DOCUMENTID AND " + indexTableName+ ".REVISIONID = DOCUMENTS.REVISIONID");
		}
		hitListQuery.append(" INNER JOIN DOCUMENTREVISIONS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTREVISIONS.DOCUMENTID AND " + indexTableName+ ".REVISIONID = DOCUMENTREVISIONS.REVISIONID");

		hitListQuery.append(createWhereClause(searchFilterList));

		hitListQuery.append( " AND (DOCUMENTS.STATUS NOT IN ('"+Hit.STATUS_DELETED+"','"+ Hit.STATUS_HIDDEN+"', '"+ Hit.STATUS_EXPIRED+"') ) ");

		hitListQuery.append(createOrderByClause(documentClass,orderBy,orderType));

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rsHits = null; 
		Statement stmt = connection.createStatement();

		rsHits = stmt.executeQuery(hitListQuery.toString());
		while(rsHits.next()){
			Hit hit = new Hit();
			hit.documentId = rsHits.getInt("DOCUMENTID");
			hit.extension = rsHits.getString("EXT").trim();
			hit.created = rsHits.getString("CREATED");
			hit.modified = rsHits.getString("MODIFIED");
			hit.expiryOn = rsHits.getString("EXPIRY");
			hit.status = rsHits.getString("STATUS").trim();
			hit.fileLength = rsHits.getInt("LENGTH");
			hit.createdBy = rsHits.getString("CREATEDBY");
			hit.setProperties(rsHits.getByte("HASNOTE"));
			hit.revisionId = rsHits.getString("REVISIONID");
			hit.modifiedBy  = rsHits.getString("MODIFIEDBY");
			hit.isHeadRevision = false;
			
			String headRevision = rsHits.getString("HEADREVISION");
			if(hit.revisionId.equalsIgnoreCase(headRevision)){
				hit.isHeadRevision = true;
			}

			ArrayList<String> indexValues = new ArrayList<String>();
			String value = "";
			for (IndexDefinition indexDefinition : documentClass.getIndexDefinitions()) {
				value = rsHits.getString("I"+ indexDefinition.getIndexColumnName())!= null?rsHits.getString("I"+ indexDefinition.getIndexColumnName()):"";
				indexValues.add(value);
			}
			hit.indexValues = indexValues;
			results.add(hit);
		}
		rsHits.close();
		stmt.close();
		connection.close();

		rsHits = null;
		stmt= null;
		connection = null;

		return results;
	}


	public ArrayList<Hit> searchDocuments(DocumentClass documentClass,String[] searchWords) throws Exception{
		ArrayList<Hit> results = new ArrayList<Hit>();
		String indexTableName = documentClass.getIndexTableName();
		StringBuffer hitListQuery =  new StringBuffer("SELECT DOCUMENTS.DOCUMENTID, DOCUMENTS.CLASSID, DOCUMENTS.DOCUMENTTYPE,"); 
		hitListQuery.append("DOCUMENTS.STATUS, DOCUMENTS.CREATED,DOCUMENTS.MODIFIED,DOCUMENTS.EXT,");
		hitListQuery.append("DOCUMENTS.LASTACCESS, DOCUMENTS.CREATEDBY, DOCUMENTS.EXPIRY, ");
		hitListQuery.append("DOCUMENTS.MODIFIEDBY , DOCUMENTREVISIONS.LENGTH,"); 
		hitListQuery.append("DOCUMENTS.REVISIONID AS HEADREVISION ," + indexTableName + ".REVISIONID,");
		for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
			hitListQuery.append(indexTableName + ".I" + indexDefinition.getIndexColumnName() + ", ");
		}
		hitListQuery.append("DOCUMENTS.HASNOTE  FROM " + indexTableName);
		hitListQuery.append(" INNER JOIN DOCUMENTS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTS.DOCUMENTID AND " + indexTableName+ ".REVISIONID = DOCUMENTS.REVISIONID");
		hitListQuery.append(" INNER JOIN DOCUMENTREVISIONS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTREVISIONS.DOCUMENTID AND " + indexTableName+ ".REVISIONID = DOCUMENTREVISIONS.REVISIONID");
		hitListQuery.append(" WHERE ( 1=2 ");

		for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
			hitListQuery.append(" OR ( 1=2");
			for(int i=0 ; i<searchWords.length ; i++){
				hitListQuery.append(" OR UPPER("+ indexTableName + ".I" + indexDefinition.getIndexColumnName() + ") LIKE  ? ");
			}
			hitListQuery.append(" ) ");
		}
		hitListQuery.append(" ) ");
		hitListQuery.append( " AND (DOCUMENTS.STATUS NOT IN ('"+Hit.STATUS_DELETED+"','"+ Hit.STATUS_HIDDEN+"', '"+ Hit.STATUS_EXPIRED+"') ) ");
		hitListQuery.append( " ORDER BY 1 DESC  ");

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rsHits = null; 
		PreparedStatement ps = connection.prepareStatement(hitListQuery.toString());
		int i = 1;

		for(IndexDefinition indexDefinition:  documentClass.getIndexDefinitions()){
			indexDefinition.getDefaultValue();
			for(String searchWord : searchWords){
				ps.setString(i++, "%"+searchWord+"%");
			}
		}
		rsHits = ps.executeQuery();
		while(rsHits.next()){
			Hit hit = new Hit();
			hit.documentId = rsHits.getInt("DOCUMENTID");
			hit.extension = rsHits.getString("EXT").trim();
			hit.created = rsHits.getString("CREATED");
			hit.modified = rsHits.getString("MODIFIED");
			hit.expiryOn = rsHits.getString("EXPIRY");
			hit.status = rsHits.getString("STATUS").trim();
			hit.fileLength = rsHits.getInt("LENGTH");
			hit.createdBy = rsHits.getString("CREATEDBY");
			hit.setProperties(rsHits.getByte("HASNOTE"));
			hit.revisionId = rsHits.getString("REVISIONID");
			hit.modifiedBy  = rsHits.getString("MODIFIEDBY");
			hit.isHeadRevision = false;

			String headRevision = rsHits.getString("HEADREVISION");
			if(hit.revisionId.equalsIgnoreCase(headRevision)){
				hit.isHeadRevision = true;
			}

			ArrayList<String> indexValues = new ArrayList<String>();
			String value = "";
			for (IndexDefinition indexDefinition : documentClass.getIndexDefinitions()) {
				value = rsHits.getString("I"+ indexDefinition.getIndexColumnName())!= null?rsHits.getString("I"+ indexDefinition.getIndexColumnName()):"";
				indexValues.add(value);
			}
			hit.indexValues = indexValues;
			results.add(hit);
		}
		rsHits.close();
		ps.close();
		connection.close();

		rsHits = null;
		ps = null;
		connection = null;

		return results;

	}

	public ArrayList<Hit> searchDocumentsByTag(DocumentClass documentClass,String tagText) throws Exception{
		tagText = tagText.toLowerCase();
		ArrayList<Hit> results = new ArrayList<Hit>();
		String indexTableName = documentClass.getIndexTableName();
		StringBuffer hitListQuery =  new StringBuffer("SELECT DOCUMENTS.DOCUMENTID, DOCUMENTS.CLASSID, DOCUMENTS.DOCUMENTTYPE,"); 
		hitListQuery.append("DOCUMENTS.STATUS, DOCUMENTS.CREATED,DOCUMENTS.MODIFIED,DOCUMENTS.EXT,");
		hitListQuery.append("DOCUMENTS.LASTACCESS, DOCUMENTS.CREATEDBY, DOCUMENTS.EXPIRY,");
		hitListQuery.append("DOCUMENTS.MODIFIEDBY , DOCUMENTREVISIONS.LENGTH,"); 
		hitListQuery.append("DOCUMENTS.REVISIONID AS HEADREVISION ," + indexTableName + ".REVISIONID,");
		for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
			hitListQuery.append(indexTableName + ".I" + indexDefinition.getIndexColumnName() + ", ");
		}
		hitListQuery.append("DOCUMENTS.HASNOTE  FROM " + indexTableName);
		hitListQuery.append(" INNER JOIN DOCUMENTS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTS.DOCUMENTID AND " + indexTableName+ ".REVISIONID = DOCUMENTS.REVISIONID");
		hitListQuery.append(" INNER JOIN DOCUMENTREVISIONS ON " + indexTableName+ ".DOCUMENTID = DOCUMENTREVISIONS.DOCUMENTID AND " + indexTableName+ ".REVISIONID = DOCUMENTREVISIONS.REVISIONID");
		hitListQuery.append(" INNER JOIN DOCUMENTTAGS ON DOCUMENTTAGS.DOCUMENTID = DOCUMENTS.DOCUMENTID ");
		hitListQuery.append(" WHERE TAGTEXT = ? ");
		hitListQuery.append(" AND (DOCUMENTS.STATUS NOT IN ('"+Hit.STATUS_DELETED+"','"+ Hit.STATUS_HIDDEN+"', '"+ Hit.STATUS_EXPIRED+"') ) ");
		hitListQuery.append(" ORDER BY 1 DESC  ");

		Connection connection = ConnectionPoolManager.getInstance().getConnection();
		ResultSet rsHits = null; 
		PreparedStatement ps = connection.prepareStatement(hitListQuery.toString());
		ps.setString(1,tagText.toLowerCase());

		rsHits = ps.executeQuery();

		while(rsHits.next()){
			Hit hit = new Hit();
			hit.documentId = rsHits.getInt("DOCUMENTID");
			hit.extension = rsHits.getString("EXT").trim();
			hit.created = rsHits.getString("CREATED");
			hit.modified = rsHits.getString("MODIFIED");
			hit.expiryOn = rsHits.getString("EXPIRY");
			hit.status = rsHits.getString("STATUS").trim();
			hit.fileLength = rsHits.getInt("LENGTH");
			hit.createdBy = rsHits.getString("CREATEDBY");
			hit.setProperties(rsHits.getByte("HASNOTE"));
			hit.revisionId = rsHits.getString("REVISIONID");
			hit.modifiedBy  = rsHits.getString("MODIFIEDBY");
			hit.isHeadRevision = false;

			String headRevision = rsHits.getString("HEADREVISION");
			if(hit.revisionId.equalsIgnoreCase(headRevision)){
				hit.isHeadRevision = true;
			}

			ArrayList<String> indexValues = new ArrayList<String>();
			String value = "";
			for (IndexDefinition indexDefinition : documentClass.getIndexDefinitions()) {
				value = rsHits.getString("I"+ indexDefinition.getIndexColumnName())!= null?rsHits.getString("I"+ indexDefinition.getIndexColumnName()):"";
				indexValues.add(value);
			}
			hit.indexValues = indexValues;
			results.add(hit);
		}
		rsHits.close();
		ps.close();
		connection.close();

		rsHits = null;
		ps= null;
		connection = null;

		return results;

	}

	private String createWhereClause(ArrayList<SearchFilter> searchFilterList){
		StringBuffer whereClause = new StringBuffer(" WHERE 1=1 ");
		for(SearchFilter searchFilter : searchFilterList){
			String filterValue1 = searchFilter.getValue1();
			if(filterValue1 != null){
				filterValue1 = filterValue1.replaceAll("'", "''");
			}
			String filterValue2 = searchFilter.getValue2();
			if(filterValue2 != null){
				filterValue2 = filterValue2.replaceAll("'", "''");
			}
			if(searchFilter.isMetaColumn()){
				if(searchFilter.getValue1().trim().length() > 0){
					whereClause.append( " AND ");
					if("CREATED".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.CREATED BETWEEN '" + new java.sql.Date(DBStringHelper.getSQLDate(filterValue1).getTime()) +" 00:00:00 " +  "' AND '" +  new java.sql.Date(DBStringHelper.getSQLDate(filterValue2).getTime()) +" 23:59:59 " + "'"); 
					}else if ("MODIFIED".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.MODIFIED BETWEEN '" + new java.sql.Date(DBStringHelper.getSQLDate(filterValue1).getTime()) +" 00:00:00 " +  "' AND '" +  new java.sql.Date(DBStringHelper.getSQLDate(filterValue2).getTime()) +" 23:59:59 " + "'"); 
					}else if("EXPIRY".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.EXPIRY BETWEEN '" + new java.sql.Date(DBStringHelper.getSQLDate(filterValue1).getTime()) +" 00:00:00 " +  "' AND '" +  new java.sql.Date(DBStringHelper.getSQLDate(filterValue2).getTime()) +" 23:59:59 " + "'"); 
					}else if("EXT".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.EXT = '"+filterValue1+"'"); 
					}else if("FILENAME".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.FILENAME LIKE '%"+filterValue1+"%'"); 
					}else if("CREATEDBY".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.CREATEDBY LIKE '%"+filterValue1+"%'"); 
					}else if("MODIFIEDBY".equalsIgnoreCase(searchFilter.getColumnName())){
						whereClause.append( "DOCUMENTS.MODIFIEDBY LIKE '%"+filterValue1+"%'"); 
					}else if("DOCUMENTID".equalsIgnoreCase(searchFilter.getColumnName())){
						String documentid = searchFilter.getValue1()!=null?searchFilter.getValue1():"0";
						documentid=documentid.trim().length() == 0?"0":documentid;

						int documentId = Integer.parseInt(documentid);
						if(documentId > 0){
							whereClause.append( " DOCUMENTS.DOCUMENTID = "+documentId+"");
						}else{
							whereClause.append( " DOCUMENTS.DOCUMENTID > "+documentId+"");
						}
					}
				}
			}else{
				if(searchFilter.getValue1().trim().length() > 0 || searchFilter.getOperator() > SearchFilter.OPERATOR_BETWEEN){
					whereClause.append( " AND ");

					String indexName = "I"+searchFilter.getColumnName();
					switch(searchFilter.getOperator()){
					case SearchFilter.OPERATOR_IS:
						if(IndexDefinition.INDEXTYPE_DATE.equals(searchFilter.getColumnType())){
							whereClause.append( "DATE("+indexName + ") = DATE('" +filterValue1+ "')");
						}else{
							whereClause.append( indexName + " = '" +filterValue1+ "'");
						}
						break;
					case SearchFilter.OPERATOR_ISNOT:
						if(IndexDefinition.INDEXTYPE_DATE.equals(searchFilter.getColumnType())){
							whereClause.append(  " ( DATE("+indexName + ") <> DATE('" +filterValue1+ "') OR " + indexName + " IS NULL)");
						}else{
							whereClause.append(  "(" + indexName + " <> '" +filterValue1+ "' OR " + indexName + " IS NULL ) ");
						}
						break;
					case SearchFilter.OPERATOR_LIKE:
						whereClause.append( "UPPER("+indexName + ") LIKE '%" +filterValue1.toUpperCase()+ "%'");
						break;
					case SearchFilter.OPERATOR_NOTLIKE:
						whereClause.append( " (UPPER(" + indexName + ")  NOT LIKE '%" +filterValue1.toUpperCase()+ "%'  OR " + indexName + " IS NULL) ");
						break;
					case SearchFilter.OPERATOR_ISGREATERTHAN:
						if(IndexDefinition.INDEXTYPE_DATE.equals(searchFilter.getColumnType())){

							whereClause.append( "(DATE("+indexName + ") > DATE('" +filterValue1+ "') AND " + indexName + " IS NOT NULL)");
						}else if(IndexDefinition.INDEXTYPE_NUMBER.equals(searchFilter.getColumnType())){
							whereClause.append( "CAST(NULLIF("+indexName + ",'') AS DECIMAL(10,2) ) > CAST(" +filterValue1+ " AS DECIMAL)");
						}else{
							whereClause.append( indexName + "> '" +filterValue1+ "'");
						}
						break;
					case SearchFilter.OPERATOR_ISLESSERTHAN:
						if(IndexDefinition.INDEXTYPE_DATE.equals(searchFilter.getColumnType())){
							whereClause.append( "(DATE("+indexName + ") < DATE('" +filterValue1+ "') AND " + indexName + " IS  NOT NULL AND LENGTH(TRIM("+indexName+")) > 0)");
						}else if(IndexDefinition.INDEXTYPE_NUMBER.equals(searchFilter.getColumnType())){
							whereClause.append( "CAST(NULLIF("+indexName + ",'') AS DECIMAL(10,2) ) < CAST(" +filterValue1+ " AS DECIMAL)");
						}else{
							whereClause.append( indexName + "< '" +filterValue1+ "'");
						}
						break;
					case SearchFilter.OPERATOR_BETWEEN:
						if(IndexDefinition.INDEXTYPE_DATE.equals(searchFilter.getColumnType())){

							whereClause.append("DATE("+ indexName + ") '" + new java.sql.Date(DBStringHelper.getSQLDate(filterValue1).getTime()) +" 00:00:00 " +  "' AND '" +  new java.sql.Date(DBStringHelper.getSQLDate(searchFilter.getValue2()).getTime()) +" 23:59:59 " + "'");
						}else if(IndexDefinition.INDEXTYPE_NUMBER.equals(searchFilter.getColumnType())){
							whereClause.append(" CAST(NULLIF("+ indexName + ",'') AS DECIMAL (10,2)) BETWEEN " +filterValue1+ " AND " + searchFilter.getValue2() + "");
						}else{
							whereClause.append( indexName + " BETWEEN '" +filterValue1+ "' AND '" + searchFilter.getValue2() + "'");
						}
						break;
						//New funtionality added for empty and non empty index values filter by Rahul Kubadia on 23rd October 2012 for Version 6.0	
					case SearchFilter.OPERATOR_ISEMPTY:
						whereClause.append( "(" + indexName + " IS NULL OR LENGTH("+indexName+") = 0 )");
						break;
						//New funtionality added for empty and non empty index values filter by Rahul Kubadia on 23rd October 2012 for Version 6.0
					case SearchFilter.OPERATOR_ISNOTEMPTY:
						whereClause.append( indexName + " IS NOT NULL AND LENGTH("+indexName+") > 0 ");
						break;
					default:
						whereClause.append( indexName + " = '" +filterValue1+ "'");
					}
				}
			}
		}
		return whereClause.toString();
	}

	private String createOrderByClause(DocumentClass documentClass,String orderColumn,String orderType){
		if(orderType.trim().length() == 0 ){
			return " ORDER BY DOCUMENTS.DOCUMENTID DESC";
		}

		StringBuffer orderByClause = new StringBuffer(" ORDER BY ");
		orderType =  orderType.equalsIgnoreCase("D")?" DESC ":" ASC ";

		if(HitList.isMetaDataColumn(orderColumn)){ //sort is on meta data column
			if(orderColumn.equalsIgnoreCase(HitList.META_LENGTH)){
				orderByClause.append("  DOCUMENTREVISIONS." + orderColumn + orderType ) ; 
			}else if(orderColumn.equalsIgnoreCase(HitList.META_REVISIONID)) {
				orderByClause.append("  DOCUMENTREVISIONS." + orderColumn + orderType ); 
			}else{
				orderByClause.append("  DOCUMENTS." + orderColumn + orderType ) ;
			}
		}else{
			ArrayList<IndexDefinition> sortIndexDefinitions = IndexDefinitionDAO.getInstance().readIndexDefinitions("IDXCOLNAME='"+orderColumn+"' AND INDEXID=" + documentClass.getIndexId() );
			orderColumn="I"+orderColumn;
			IndexDefinition indexDefinition = sortIndexDefinitions.get(0);
			String indexTableName = documentClass.getIndexTableName();
			if(indexDefinition.getIndexType().equalsIgnoreCase(IndexDefinition.INDEXTYPE_NUMBER)){
				orderByClause.append(" DOUBLE(NULLIF(" + indexTableName + "." + orderColumn +" , ''))"+ orderType );
			}else if(indexDefinition.getIndexType().equalsIgnoreCase(IndexDefinition.INDEXTYPE_DATE)){
				orderByClause.append(" DATE("+ orderColumn+") " + orderType );
			}else{
				orderByClause.append(" " + indexTableName + "." + orderColumn+ orderType );
			}
		}
		return orderByClause.toString();
	}
}