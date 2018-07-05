package businesscomponents;

import java.util.Properties;

import org.testng.Assert;

import com.cognizant.framework.Settings;

public class UtilTool {

	private static Properties properties = Settings.getInstance();;
	private static String env;

	public static void assignEnv(String e) {
		Assert.assertTrue(e != null && !e.trim().isEmpty());
		env = e;
	}

	// ###################################################################################//
	// Functionality to get environment details from system environment or
	// through pom.xml
	// ##################################################################################//
	public static String getEnv() {
		if (env != null && !env.trim().isEmpty())
			return env;

		if (properties.getProperty("ALMExecutionFlag").equals("0")) {
			env = System.getenv("CBEcom_ENV");

		} else {
			env = System.getProperty("environment");

		}

		 env = "DS"; //first use assignEnv("QA1") instead
		return env;
	}

	// ###################################################################################//
	// Functionality to get environment details from system environment or
	// through pom.xml
	// ##################################################################################//
	public static String getBrowser() {
		return System.getProperty("Browser") != null && !System.getProperty("Browser").trim().isEmpty()
				? System.getProperty("Browser") : properties.getProperty("DefaultBrowser");
	}

	public static String getTestSetID() {
		String testSetId = "TestSetID";
		try{
			testSetId = testSetId + "_" + getBrowser();
		}catch(Exception e){}finally{
			if(testSetId == null || testSetId.isEmpty()){
				testSetId = "TestSetID";
			}
		}
		
		return System.getProperty("TestSetID") != null && !System.getProperty("TestSetID").trim().isEmpty()
				? System.getProperty("TestSetID") : properties.getProperty(testSetId);
	}
}
