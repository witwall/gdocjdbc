package org.gdocjdbc.db;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;


public class DBCreator {

	  private static SpreadsheetService spreadsheetsService= null;
	  
	  public static final String SPREADSHEETS_SERVICE_NAME = "wise";
	  private Connection con = null;
	  private static final String DEFAULT_COLUMN_TYPE="VARCHAR(100)";
	  private static Logger log = Logger.getLogger(DBCreator.class.getName());
	  String username = "";
	  String password = "";
	  String userDBName = "";
	  boolean created = false;
	  
	  
	  public DBCreator(String username, String password) {
		  this.username = username;
		  this.password = password;
		  this.userDBName = scrubUserName(username);
		  try {
			setUpConnection();
			
			if(tablesExists()) {
				log.info("Table(s) have been setup previously");
			} else {
				
				createInfoTable();
				
				createDatabase();
			}
			
		  } catch (SQLException e) {
			  log.log(Level.SEVERE, "Problem with connection", e);
		  }
		
		
		
	  }


	 private void setUpConnection() throws SQLException {
		 try {
				Class.forName("org.hsqldb.jdbcDriver" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Statement stmt = null;
			con = DriverManager.getConnection("jdbc:hsqldb:mem:"+userDBName, "sa", "");
	 }

	public void createDatabase() throws SQLException {
			  log.log(Level.INFO, "Local Database of Google Doc data doesn't exist, creating it...");
			  setupGoogleDocConnection(username,password);
			  List <String> urls = new ArrayList<String>();
			  
			
			  
			    SpreadsheetFeed feed = null;
				try {
					feed = spreadsheetsService.getFeed(FeedURLFactory.getDefault().getSpreadsheetsFeedUrl(),
					        SpreadsheetFeed.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
  			  log.info("found " + feed.getEntries().size() + " spreadsheet(s) in your google doc account");
			  
  			  
  			  for(SpreadsheetEntry sse:feed.getEntries()) {
  				populateDBFromSpeadsheet(sse);
  			  }
  			  
  			  Statement stmt = con.createStatement();
  			  stmt.executeUpdate("UPDATE PICKLES_TABLE_INFO SET MODIFIED_DATE_END=now()");
  			  stmt.close();
  			  
	  }
	  
	  
	private boolean tablesExists() throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		String select = "SELECT COUNT(*) as MYCOUNT FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_NAME=\'PICKLES_TABLE_INFO\'";
		boolean tableExists = false;
		ResultSet rs = stmt.executeQuery(select);
		
		while(rs.next()) {
			Integer count =rs.getInt("MYCOUNT");
			tableExists = count > 0;
		}
		
		return tableExists;
	}
	

	
	private void createInfoTable() throws SQLException {
		StringBuffer createString = new StringBuffer();
		createString.append("create table PICKLES_TABLE_INFO (");
		createString.append("CREATE_DATE DATE,");
		createString.append("MODIFIED_DATE_START DATE,");			
		createString.append("MODIFIED_DATE_END DATE");		
		createString.append(");");
				


		Statement stmt = null;
		stmt = con.createStatement();
		stmt.executeUpdate(createString+"");
		stmt.close();
		
		stmt = con.createStatement();
		stmt.executeUpdate("INSERT INTO PICKLES_TABLE_INFO (CREATE_DATE, MODIFIED_DATE_START) VALUES(SYSDATE, SYSDATE)");
		stmt.close();
	}

	  
	  
	public void setupGoogleDocConnection(String username, String password) {
		 spreadsheetsService = new SpreadsheetService("NikeProductFinder");
		    

		    try {

			    spreadsheetsService.setUserCredentials(username, password);
			
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
	}
	

	
private Map<String, String> createTable(String tablename, ListEntry entry) throws SQLException {
	StringBuffer createString = new StringBuffer();
	createString.append("create table "+tablename+" (");
	String colType = "";
	
	Map<String, String> columnNames = new HashMap<String, String>();
	
	try {
		Class.forName("org.hsqldb.jdbcDriver" );
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Statement stmt = null;


		for (String tag : entry.getCustomElements().getTags()) {
			if(tag != null && tag.length() > 0) {
				//to support overriding later on
				colType = "null";
				columnNames.put(tag.toUpperCase(), null);
				createString.append(tag.toUpperCase() + " "+((colType.equals("null"))?DEFAULT_COLUMN_TYPE:colType)+", ");
			}
	     }
    createString.delete(createString.length()-2, createString.length()-1);

	createString.append(");");
	
	stmt = con.createStatement();
	stmt.executeUpdate(createString+"");
	stmt.close();
	
	return columnNames;
	
}

	
private void populateDBFromSpeadsheet(SpreadsheetEntry sse) throws SQLException {
	
		try {
			List<WorksheetEntry> worksheetList = sse.getWorksheets();
			
			String documentName = scrubTableName(sse.getTitle().getPlainText());
			
			URL columnListFeedUrl = worksheetList.get(0).getListFeedUrl();
		    ListFeed columnFeed = spreadsheetsService.getFeed(columnListFeedUrl, ListFeed.class);
			
		    
			for(WorksheetEntry worksheet : worksheetList) {
			   
				String workSheetTitle = scrubTableName(worksheet.getTitle().getPlainText());
				
				
				
			    URL listFeedUrl = worksheet.getListFeedUrl();
			    ListFeed feed = spreadsheetsService.getFeed(listFeedUrl, ListFeed.class);
			    if(feed.getEntries().size() < 1) {
			    	log.info("Worksheet: " + workSheetTitle + " from document: "+documentName + " is empty so skipping it.");
			    	break;
			    }
			    Map<String, String> columnNames = createTable(documentName+"_"+workSheetTitle,feed.getEntries().get(0));
			    log.info("populating table : "+documentName+"_"+workSheetTitle);
			    for (ListEntry entry : feed.getEntries()) {
			    	insertEntry(documentName, workSheetTitle, entry, columnNames);
			    }
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}




	private void insertEntry(String tablePrefix, String worksheetName, ListEntry entry, Map<String, String> columnNames) throws SQLException {
		
		String colNames = "";
		String values = "";
		
		String tempStr = "";
		
		for (String tag : entry.getCustomElements().getTags()) {
			//making sure no columns are set within the body of the spreadsheet.
			if(columnNames.containsKey(tag.toUpperCase())) {
				tempStr = entry.getCustomElements().getValue(tag);
				tempStr = (tempStr==null)?"":tempStr;
				colNames += tag.toUpperCase()+", ";
				values += "\'"+tempStr.replaceAll("\'", "\'\'")+"\', ";
			}
	      }
		colNames = colNames.substring(0, colNames.length()-2);
		values = values.substring(0, values.length()-2);	
		

		
		String insertString = "INSERT INTO "+tablePrefix+"_"+worksheetName+" ("+colNames+") VALUES ("+ values+");";
		
		log.fine(insertString);
	
		Statement stmt = con.createStatement();
   		stmt = con.createStatement();
   		stmt.executeUpdate(insertString);
   		stmt.close();
	}

	
	private String scrubTableName(String origName) {
		return origName.replaceAll("[^a-zA-Z0-9]", "");

	}

	public static String scrubUserName(String origName) {
		return origName.replaceAll("[^a-zA-Z0-9]", "");
	}
}
