package businesscomponents;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class DBComponents extends ReusableLibrary {

	public DBComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public static Connection DBConnection(String DB_Name) throws SQLException
	{
		System.out.println("<<<<<<<<<<<<<<<<<<<<< Oracle JDBC Connection Testing >>>>>>>>>>>>>>>>>>>>>");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return null;
		}

		System.out.println("Oracle JDBC Driver Registered!");
		Connection connection = null;
		try {
			if(DB_Name.equals("COXBIZ"))
				//connection = DriverManager.getConnection("jdbc:oracle:thin:@172.18.93.134:1521/CSDB","A1EQAAUTOMATION", "a1eqaauto123");
				connection = DriverManager.getConnection("jdbc:oracle:thin:@172.18.93.134:1521/CSDB","CBS_QA3", "CBS_QA3");
			else if(DB_Name.equals("MySQL")){
				connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/A1EQAAutomation","a1eqaautomation","a1eqaautomation");}
			else if(DB_Name.equals("COXBIZ2")){
				connection = DriverManager.getConnection("jdbc:oracle:thin:@172.18.93.134:1521/CSDB","CBS_QA2", "CBS_QA2");
			}
			else
				connection = DriverManager.getConnection("jdbc:oracle:thin:@xhastage-duke1.corp.cox.com:1521/QHASTAGE_GENERIC_APP.WORLD","DPSCBSUSER", "DPSCBSUSER");
				
		} catch (SQLException e) 
		{
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.err.println("Failed to make connection!");
		}
		return connection;
	}
	
	public static Connection DBConnection_MySQL() throws SQLException, InstantiationException, IllegalAccessException
	{
		System.out.println("<<<<<<<<<<<<<<<<<<<<< MySQL JDBC Connection Testing >>>>>>>>>>>>>>>>>>>>>");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your mysql JDBC Driver?");
			e.printStackTrace();
			return null;
		}

		System.out.println("mysql JDBC Driver Registered!");
		Connection connection = null;
		try {		
			connection = DriverManager.getConnection("jdbc:mysql://catl0qa1267.corp.cox.com:3306","a1eqaautomation","a1eqaautomation");
			
		} catch (SQLException e) 
		{
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
		}
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.err.println("Failed to make connection!");
		}
		return connection;
	}

	public static Hashtable<String, String> ExecuteQuery(String DB, String sql) throws SQLException 
	{
		Connection connection = null;
		if(DB.equals("COXBIZ"))
			connection = DBConnection("COXBIZ");
		else if(DB.equals("COXBIZ2"))
			connection = DBConnection("COXBIZ2");
		else 
			connection = DBConnection("PHASTAGE");
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Hashtable<String, String> data = new Hashtable<>(25);
		int colsize = rs.getMetaData().getColumnCount();
		if (colsize != 0) 
		{
			while (rs.next()) 
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= colsize; i++) 
				{
					String name = rsmd.getColumnName(i);
					try {
						if (rs.getString(i) != null) 
						{
							System.out.println("Column " + i + " => " + name + " : " + rs.getString(i));
							data.put(name, rs.getString(i));
						} else {
							data.put(name, "null");
							System.out.println("Column " + i + " => " + name + " : null");
						}
					} catch (Exception e) {
						data.put(name, "null");
						System.out.println("Column " + i + " => " + name + " : null");
					}
				}
				System.out.println("::::::::::::::All data captured under Hashtable::::::::::::::::");
			}
		}
		else 
		{
			System.out.println("No records found");
		}
		return data;
	}
	
	public static Hashtable<String, String> ExecuteQuery_MySQL(String sql) throws SQLException, InstantiationException, IllegalAccessException 
	{
		Connection connection = null;
		connection = DBConnection_MySQL();
		Hashtable<String, String> data = new Hashtable<>(25);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
		
		return data;
	}
}
