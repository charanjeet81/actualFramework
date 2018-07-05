package businesscomponents;

import java.util.Properties;

import com.cognizant.framework.Settings;

public class Constants  {

	 private Properties properties = Settings.getInstance();
		
	 public static final String HOST = "alm.corp.cox.com";
	 public static final String PORT = "80";
	 public String USERNAME = properties.getProperty("almid");
	 public String PASSWORD = properties.getProperty("almpwd");
	 public static String DOMAIN = "COX";
//	 public static String PROJECT = "cox";
//	 public static boolean VERSIONED = false;
	 public static String PROJECT = "EDO_BSS";
	 public static boolean VERSIONED = true;
}
