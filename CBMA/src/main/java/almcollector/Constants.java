package almcollector;

import java.io.InputStream;
import java.util.Properties;

public class Constants {

	private static Constants instance = null;
	public static String HOST;
	public static String PORT;
	public static String USERNAME;
	public static String PASSWORD;
	public static String DOMAIN;
	public static String PROJECT;
	public static boolean VERSIONED = true;

	public Constants() {
		try {
			/*InputStream file = getClass().getClassLoader().getResourceAsStream("application.properties");
			Properties props = new Properties();
			props.load(file);*/
			/*this.HOST = props.getProperty("host");
			this.PORT = props.getProperty("port");
			this.USERNAME = props.getProperty("username");
			this.PASSWORD = props.getProperty("password");
			this.DOMAIN = props.getProperty("domain");
			this.PROJECT = props.getProperty("project");*/
			
			this.HOST = "alm.corp.cox.com";
			this.PORT = "80";
			this.USERNAME = "a1eqaautomation";
			this.PASSWORD = "KuT785Kp1";
			this.DOMAIN = "COX";
			this.PROJECT = "EDO_BSS";
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static synchronized Constants getInstance() {
		if (instance == null)
			instance = new Constants();
		return instance;
	}

}
