/** Connect to a MariaDB/MySQL database to retrieve event information
 * 
 * Currently connects to a test/dummy DB located at gruesomevisage.net
 * 
 * Use the getEventList() method to get an array of EventListings
 * 
 */

package com.example.gt_coc_studentevents;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseConnector {
	
	private static String userName = "coc_events_user";
	private static String password = "justdoit";
	private static String dbms = "mysql";
	private static String serverName = "gruesomevisage.net";
	private static int portNumber = 3306;
	private static String listQuery = "SELECT * from cocevents.events";
	
	
	
	
	private static Connection connect() throws SQLException {
		
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
	    conn = DriverManager.getConnection(
	    		"jdbc:" + dbms + "://" +
				serverName +
				":" + portNumber + "/",
				 connectionProps);
			
	    System.out.println("Connected to database");
	    return conn;
	}
	
	private static ResultSet query(Connection connector) throws SQLException{
		
		Statement stmt = connector.createStatement();
		ResultSet rs = stmt.executeQuery(listQuery);
		return rs;
		
	}
	
	public static ArrayList<EventListing> getEventList() throws SQLException {
		
		Connection conn = connect();
		ResultSet rs = query(conn);
				ArrayList<EventListing> eventList = new ArrayList<EventListing>();
		
		
		while ( rs.next() ) {
			eventList.add( new EventListing(
						rs.getString("eventName"),
						rs.getString("location"),
						rs.getTimestamp("time"),
						rs.getString("description")
						));
		}
		
		return eventList;
	}
	
	
	/*
	 * Test the database connection
	 */
	public static void main(String[] args){
		
		ArrayList<EventListing> eventList = null;
		
		try {
			 eventList = getEventList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("You done fucked up.");
			e.printStackTrace();
		}
		if (eventList == null){ System.out.println("You have fucked up now."); return;}
		for (int i=0; i < eventList.size(); i++){
			System.out.println(eventList.get(i).getEventName() );
			System.out.println(eventList.get(i).getLocation() );
			System.out.println(eventList.get(i).getTime().toString() );
			System.out.println(eventList.get(i).getDescription() );
			
		}
	}

}
