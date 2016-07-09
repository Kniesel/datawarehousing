package at.fhj.itm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

/**
 * ConnectionFactory: returns always new connections.
 * When starting VM, DB Initscript is automatically called and executed.
 * 
 * @author gue (Gerhard Hutter)
 * 
 */
public class ConnectionFactory 
{
	private static final String DB_PROPFILE = "db.properties";
	
	private static String db_username 		= "";
	private static String db_password 		= "";
	private static String jdbc_driver 		= "";
	private static String jdbc_url			= "";
	private static String app_initscript	= "";
	
	// is only called once
	static
	{
		// read all
		Properties props = new Properties();
		try(InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream(DB_PROPFILE))
		{
			props.load(in);
			in.close();
			
			// and read
			db_username = props.getProperty("db.user");
			db_password = props.getProperty("db.password");
			jdbc_driver = props.getProperty("jdbc.driver");
			jdbc_url = props.getProperty("jdbc.url");
			app_initscript = props.getProperty("application.initscript");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println(DB_PROPFILE + " nicht gefunden");
			System.exit(1);
		}
		
		// check if database driver exists
		try 
		{
			Class.forName(jdbc_driver);
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			System.err.println("jdbc.driver could not be loaded.");
			System.exit(3);
		}
		
		// after this block is only executed once
		// database gets initialized
		if(app_initscript != null)
		{
			try(InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream(app_initscript))
			{				
				ScriptRunner s = new ScriptRunner(ConnectionFactory.getConnection(), true, true);
				s.runScript(new InputStreamReader(in));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				System.err.println("application.initscript not found.");
				System.exit(3);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				System.err.println("application.initscript threw an error.");
				System.exit(4);
			}
		}
	}
	
	public static Connection getConnection()
	{
		Connection theConnection = null;

		try 
		{
			theConnection = DriverManager.getConnection(jdbc_url, db_username, db_password);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return theConnection;
	}
}
