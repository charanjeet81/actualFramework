package businesscomponents;

import java.util.Properties;

import org.openqa.selenium.support.PageFactory;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

import com.cognizant.framework.Settings;

public class UtilTools extends ReusableLibrary 
{
	public UtilTools(ScriptHelper scriptHelper) 
	{
		super(scriptHelper);
		PageFactory.initElements(driver, this);
	}
	private static Properties properties = Settings.getInstance();
	static String env;	
//###########################################################//
//Functionality to get environment details from system environment or through pom.xml
//###########################################################//	
		public static String getEnv() 
		{		
			
			if(properties.getProperty("ALMExecutionFlag").equals("0"))
			{
			   env = System.getenv("CB_MyAccount_ENV");			  
			}
			else
			{ 
				env = System.getProperty("environment");						
			}	
			//env = "QA03";
			/*try 
			{
				if(dataTable.getData(env, "Enviroment").isEmpty())
				{
					env = "QA03";
				}
				else
					env = dataTable.getData(env, "Enviroment");
			} catch (Exception e) { }*/
			return env;
		}
	}
 