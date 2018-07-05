package businesscomponents;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.VisionTools_Page;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

import com.cognizant.framework.Status;

/* This class has all the TCs related to Smoke TCs
 */
public class SMOKE_SUITE extends ReusableLibrary {
	
	public SMOKE_SUITE(ScriptHelper scriptHelper) 
	{
		super(scriptHelper);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//span[contains(text(),'Dial Tone Worksheet')]")
	public WebElement DTW_LNK;	
	@FindBy(id = "submitbutton")
	public WebElement DTW_Sub_BTN;	
	
	public void getBuildVersion()
	{
		String url = String.valueOf(dataTable.getData(env, "URL_Type"));
		url = url + "_"+env;
		driver.get(properties.getProperty(url).trim( )+"/release.cox");
		String applicationVersion = driver.findElement(By.xpath("(//span[contains(text(),'Version : ')])[1]/..")).getText();
		String applicationVersionNumber = applicationVersion.split(":")[1].trim();
		Reporting("Application version displayed as: "+applicationVersionNumber, Status.DONE);
		String s =driver.findElement(By.xpath("//tbody/tr/td[3]")).getText();
		
		String[] environment=s.split("\\.");
		Reporting("Environment pointed as: "+s, Status.PASS);
	}
	
	public void invokeApplication_CBMyAccount() 
	{
		String url = String.valueOf(dataTable.getData(env, "URL_Type"));
		url = url+"_"+env;
		System.out.println("Printing URL: "+url);
		Reporting("Environment URL is: "+url, Status.PASS);
		
		//*******This is a workaround for iteration***********//	
		
	}
	
	public void sMOKE_CBMA_Profile_Creation_Password_Reset_1519878()      
	{
			SoapUIComponents SoapUIComponents=new SoapUIComponents(scriptHelper);
			
			SoapUIComponents.createNewAccount("AccountNumber");
			SoapUIComponents.createNewAccount("SubAccount");
			
			//AdminLogin_Page adminLogin = new AdminLogin_Page(scriptHelper);	
			//adminLogin.fn_application_login();
	}
	
}	


