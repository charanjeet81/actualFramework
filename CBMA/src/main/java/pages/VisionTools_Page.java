package pages;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import businesscomponents.WaitTool;

import com.cognizant.framework.Status;

public class VisionTools_Page extends ReusableLibrary {

	static String timestamp = new SimpleDateFormat("HH_mm_ss").format(Calendar.getInstance().getTime());
	public static String user = "test" + timestamp + "@mailinator.com";

	class Local {};

	public VisionTools_Page(ScriptHelper scriptHelper) {
		super(scriptHelper);
		PageFactory.initElements(driver, this);
	}
		
	@FindBy(xpath = "//select[@id='j_id6:j_id15']//option[3]")
	public WebElement queryBy_DropDown;
	@FindBy(id = "j_id6:j_id24")
	public WebElement accountNum_Input;
	@FindBy(id = "j_id6:j_id25")
	public WebElement sumbit_Button;
	@FindBy(id = "j_username")
	public WebElement username;
	@FindBy(id = "j_password")
	public WebElement password;
	@FindBy(xpath = "//button[text()='Login']")
	public WebElement login;
	@FindBy(linkText = "Network Elements")
	public WebElement NetworkElements;
	@FindBy(linkText = "Voice Workorder")
	public WebElement VoiceWorkorder;
	@FindBy(linkText = "Customer Search")
	public WebElement CustomerSearch;
	@FindBy(linkText = "Query Number (TLQDN)")
	public WebElement QueryNumber;
	@FindBy(linkText = "CBS Voicemanager")
	public WebElement CBSVoicemanager;
	@FindBy(linkText = "BTS / Hybrid Status")
	public WebElement BTSHybridStatus;
	@FindBy(linkText = "Query Subscriber")
	public WebElement QuerySubscriber;
	@FindBy(xpath = "//tr[1]/td[4]/input")
	public WebElement Acct_Number;
	@FindBy(linkText = "Query Voicemail / Reset Voicemail PIN")
	public WebElement QueryVoicemail;
	@FindBy(xpath = ".//input[@value='Reset']")
	public WebElement Reset;
	@FindBy(xpath = ".//input[@value='Submit']")
	public WebElement Submit;

	@FindBy(xpath = ".//input[@value='DN']")
	public WebElement QueryByDNChkBox;

	@FindBy(xpath = ".//*[@id='requestForm']//tr[2]/td[2]/input[1]")
	public WebElement QueryByDNText1;

	@FindBy(xpath = ".//input[@value='LEN']")
	public WebElement QueryByLENChkBox;

	@FindBy(name = "device.id")
	public WebElement SelectSwitch;

	@FindBy(name = "device.id")
	public WebElement SelectEquipment;

	@FindBy(xpath = "//tr[1]/td[2]/select")
	public WebElement Queryby;

	@FindBy(xpath = "//tr[2]/td[2]/input")
	public WebElement AcctNo;

	@FindBy(xpath = ".//*[@id='requestForm']//tr[2]/td[2]/input[1]")
	public WebElement TelephoneNoField1;

	@FindBy(xpath = ".//*[@id='formattedTabContainer']//span[text()='Notifications']")
	public WebElement Notifications;

	@FindBy(xpath = ".//*[@id='Profile and Features (Raw)']/pre")
	public WebElement verifyfield;

	@FindBy(xpath = ".//*[@id='Profile (Raw)']/pre")
	public WebElement verifyfield_MACON;

	@FindBy(xpath = ".//*[contains(text(),'Show Query')]")
	public WebElement showQuery;

	@FindBy(xpath = "//*[text()='MTA']")
	public WebElement MTA;

	@FindBy(xpath = "//table[contains(@id,'cbsVoicemanagerStatusTable')]")
	public WebElement VMStatusTable;

	@FindBy(xpath = "//table[contains(@id,'voipWorkorderStatusTable')]")
	public WebElement LegacyVMStatusTable;

	@FindBy(linkText = "Click here to change")
	public WebElement Clickheretochange;

	@FindBy(xpath = "//span[@class='formLabel'][text()='Status']/..//following-sibling::td//span")
	public WebElement Active;

	@FindBy(linkText = "Logout")
	public WebElement logout;

	@FindBy(xpath = ".//*[contains(text(),'logged out')]")
	public WebElement logoutstatus;

	@FindBy(xpath = "//tr[1]/td[2]/select")
	public WebElement LegacySite;

	@FindBy(xpath = "//tr[2]/td[2]/select")
	public WebElement LegacyQueryBy;

	@FindBy(xpath = "//tr[3]/td[2]/input")
	public WebElement LegacyAccountNum;
		
		public VisionTools_Page invokeApplication_VT() 
		{
			deleteCookies();
			driver.get(properties.getProperty("VisionToolURL_"+env));
			Reporting("Invoke the application under test @ <b>"+ properties.getProperty("VisionToolURL") +"</b>", Status.DONE);
			return new VisionTools_Page(scriptHelper);
		}
		
		public VisionTools_Page VT_Login() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, username, 10);
				username.clear();
				username.sendKeys(properties.getProperty("VisionToolsUserID_"+env));
				Reporting("User Name Entered Successfully: "+properties.getProperty("VisionToolsUserID_"+env), Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		Reporting("Unable to enter User Name", Status.FAIL);
		    	}
			
			try
			{
				WaitTool.waitForElementDisplayed(driver, password, 10);
				password.clear();
				password.sendKeys(properties.getProperty("VisionToolsPassword_"+env));
				Reporting("Password Entered Successfully: "+properties.getProperty("VisionToolsPassword_"+env), Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		Reporting("Unable to enter Password", Status.FAIL);
			}
			
			try
			{
				WaitTool.waitForElementDisplayed(driver, login, 10);
				login.click();
				sync(3);
				Reporting("Clicked on <b>Login</b>", Status.PASS);
				driver.navigate().refresh();
				sync(3);
			}
	    	catch (Exception e)
	    	{
	    		Reporting("Unable to Click on Login", Status.FAIL);
	    	}
			return this;
		}
			
		public VisionTools_Page SelectQueryNumber() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, QueryNumber, 10);
				QueryNumber.click();
				report.updateTestLog("Vision Tools", "Clicked on Query Number", Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Click on Query Number", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page SelectCBSVoicemanager() throws InterruptedException
		{			
			if (VoiceWorkorder.isDisplayed())
				VoiceWorkorder.click();
			sync(5);
			try {
				WaitTool.waitForElementDisplayed(driver, CBSVoicemanager, 10);
				CBSVoicemanager.click();
				Reporting("Clicked on CBS Voicemanager", Status.PASS);
			} catch (Exception e) {
				Reporting("Unable to Click on CBS Voicemanager", Status.FAIL);
			}
			return this;
		}

		public VisionTools_Page SelectBTSHybridStatus() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, BTSHybridStatus, 10);
				BTSHybridStatus.click();
				report.updateTestLog("Vision Tools", "Clicked on BTS / Hybrid Status", Status.PASS);
			}
		  	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Click on  BTS / Hybrid Status", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page SelectQuerySubscriber() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, QuerySubscriber, 10);
				QuerySubscriber.click();
				report.updateTestLog("Vision Tools", "Clicked on Query Subscriber", Status.PASS);
			}
		    catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Click on Query Subscriber", Status.FAIL);
			}
			return this;
		}
		
		
		public VisionTools_Page Reset() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, Reset, 10);
				Reset.click();
				report.updateTestLog("Vision Tools", "Clicked on Reset", Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Click on Reset", Status.FAIL);
			}
			return this;
		}
			
		
		public VisionTools_Page EnterAcct_Number() throws InterruptedException
		{
			try
			{ 
				String siteID = "";
				
				if (properties.getProperty("DefaultDataSheet").equals("QA2")) 
				{
					siteID = "216";
				}
				
				if (properties.getProperty("DefaultDataSheet").equals("MACON_PROD")) 
				{
					siteID = "001";
				}
				
				if (properties.getProperty("DefaultDataSheet").equals("SAN_PROD")) 
				{
					siteID = "541";
				}
						
				WaitTool.waitForElementDisplayed(driver, Acct_Number, 10);
				Acct_Number.clear();
				Acct_Number.sendKeys( siteID + dataTable.getData(env,"AccountNumber"));
				report.updateTestLog("Vision Tools", "Entered the Account Number as <b>"+ siteID + dataTable.getData(env,"AccountNumber") + "</b>", Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Enter the DN", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page Submit() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, Submit, 10);
				Submit.click();
				report.updateTestLog("Vision Tools", "Clicked on Submit", Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Click on Submit", Status.FAIL);
			}
			return this;
		}
			
		public VisionTools_Page ValidateStatus() throws InterruptedException
		{
			String statusmsg = null;
			String mymsg = null;
			int count = 0;
			try
			{
				if(count==0)
				{
					for(int time =0; time <= 300; time++)
					{
						WaitTool.waitForElementDisplayed(driver, showQuery, 10);
						showQuery.click();
						Submit.click();
						sync(2);
						
						if (properties.getProperty("DefaultDataSheet").equals("QA2")) 
						{
							mymsg="UNASSIGNED";
							WaitTool.waitForElementDisplayed(driver, verifyfield, 10);
							statusmsg = verifyfield.getText();
						}
						
						if (properties.getProperty("DefaultDataSheet").equals("MACON_PROD")) 
						{
							mymsg="void of entries";
							WaitTool.waitForElementDisplayed(driver, verifyfield_MACON, 10);
							statusmsg = verifyfield_MACON.getText();
						}
						
						if (properties.getProperty("DefaultDataSheet").equals("SAN_PROD")) 
						{
							mymsg="UNASSIGNED";
							WaitTool.waitForElementDisplayed(driver, verifyfield, 10);
							statusmsg = verifyfield.getText();
						}
						if(statusmsg.contains(mymsg))
						{
							count =1;
							break;
						}
					}
					if(count==1)
					{
						report.updateTestLog("Vision Tools", "TYPE appears as <b>"+mymsg+"</b> successfully", Status.PASS);
					}
					else
					{
						report.updateTestLog("Vision Tools", "TYPE not appeared as expected", Status.FAIL);
					}
				}
				
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to verify the status", Status.FAIL);
			}
			return this;
		}

		
		public VisionTools_Page ValidateNotificationsBlank() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, Notifications, 10);
				Notifications.click();
				sync(3);
				List<WebElement> NotificationList = driver.findElements(By.xpath(".//*[@id='notificationsFormattedTab']//tr/td[1]"));
				for(int i =1;i<=NotificationList.size();i++)
				{
					String text = driver.findElement(By.xpath(".//*[@id='notificationsFormattedTab']//tr["+i+"]/td[1]")).getText();
					if(text.contains("MWI Switch"))
					{
						String switchValue = driver.findElement(By.xpath(".//*[@id='notificationsFormattedTab']//tr["+i+"]/td[2]")).getText();
						if(switchValue.equals("") || switchValue == null)
						{
							report.updateTestLog("Vision Tools", "MWI Switch Value is <b>Blank</b>", Status.PASS);
							break;
						}
						else
						{
							report.updateTestLog("Vision Tools", "MWI Switch Value is not <b>Blank</b>", Status.FAIL);
						}			
					}
				}
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Exception - Unable to verify the Notifications", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page ValidateNotificationsNotBlank() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, Notifications, 10);
				Notifications.click();
				sync(3);
				List<WebElement> NotificationList = driver.findElements(By.xpath(".//*[@id='notificationsFormattedTab']//tr/td[1]"));
				for(int i =1;i<=NotificationList.size();i++)
				{
					String text = driver.findElement(By.xpath(".//*[@id='notificationsFormattedTab']//tr["+i+"]/td[1]")).getText();
		
					if(text.contains("MWI Switch"))
					{
						String switchValue = driver.findElement(By.xpath(".//*[@id='notificationsFormattedTab']//tr["+i+"]/td[2]")).getText();
						if(switchValue != null)
						{
							report.updateTestLog("Vision Tools", "MWI Switch Value is <b>"+switchValue+"</b>", Status.PASS);
							break;
						}
						else
						{
							report.updateTestLog("Vision Tools", "MWI Switch Value is not <b>"+switchValue+"</b>", Status.FAIL);
						}
					}
				}
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Exception - Unable to verify the Notifications", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page VT_Logout() throws InterruptedException
		{
			try
			{
				WaitTool.waitForElementDisplayed(driver, logout, 10);
				logout.click();
				report.updateTestLog("Vision Tools", "Clicked on Log Out", Status.PASS);
			}
		    	catch (Exception e)
		    	{
		    		report.updateTestLog("Vision Tools", "Unable to Click on Log Out", Status.FAIL);
				e.printStackTrace();
			}
			
			try
			{
				WaitTool.waitForElementDisplayed(driver, logoutstatus, 10);
				if(logoutstatus.isDisplayed())
				{
					report.updateTestLog("Vision Tools", "Log Out is Successful", Status.PASS);
				}
				else
				{
					report.updateTestLog("Vision Tools", "Log Out is Unsuccessful", Status.FAIL);
				}
			}
		    	catch (Exception e)
		   	{
		    		report.updateTestLog("Vision Tools", "Unable to Log Out of the Application", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page ValidateAccount() throws InterruptedException
		{
			String statusmsg = null;
			int count=0;
			try
			{
				
				for(int i=0; i<=300; i++)
				{
					Submit.click();
					sync(3);
					WaitTool.waitForElementDisplayed(driver, MTA, 10);
					MTA.click();
					sync(1);
					WaitTool.waitForElementDisplayed(driver, Active, 20);
					statusmsg = Active.getText();
					if(statusmsg.contains("Active"))
					{
						count =1;
						sync(3);
						break;
					}
					sync(10);
				}
				if(count==1)
				{
					report.updateTestLog("Vision Tools", "Status appears as <b>Active</b> successfully", Status.PASS);
				}
				else
				{
					report.updateTestLog("Vision Tools", "Status does not appears as <b>Active</b> after waiting 5 Minutes", Status.FAIL);
				}
			}
		    	catch (Exception e)
		   	{
		    		report.updateTestLog("Vision Tools", "Exception - Unable to verify the status as ACTIVE", Status.FAIL);
			}
			return this;
		}
		
		public VisionTools_Page ValidateVoiceWO() throws InterruptedException
		{
			int count=0;
			int tablesize=0;
			try
			{
				Select Sel1 = new Select(Queryby);
				Sel1.selectByValue("Tasks by Account Number");
				sync(2);
				report.updateTestLog("Vision Tools", "Selects <b>Tasks by Account Number</b> from List successfully", Status.PASS);
				
				String siteIDNumber = dataTable.getData(env, "SiteId");
				String ActNo = siteIDNumber + dataTable.getData(env,"AccountNumber");
				AcctNo.click();
				AcctNo.clear();
				sync(1);
				AcctNo.sendKeys(ActNo);
				report.updateTestLog("Vision Tools", "Entered <b>"+ActNo+"</b> successfully", Status.PASS);
					
				for(int i=0; i<50; i++)
				{
					Submit.click();
					WaitTool.waitForElementDisplayed(driver, VMStatusTable, 20);
					String color = null;
					count = 0;
					List <WebElement> tablelist = driver.findElements(By.xpath("//table[contains(@id,'cbsVoicemanagerStatusTable')]//tr/td[5]/span"));
						tablesize = tablelist.size();
						for(int j=1;j<=tablesize;j++){
							color = driver.findElement(By.xpath("//table[contains(@id,'cbsVoicemanagerStatusTable')]//tr["+j+"]/td/span")).getCssValue("color");
							if(color.trim().equals("rgba(0, 128, 0, 1)")){
								count = count + 1;
							}
						}
					if(count==tablesize){
						break;
					}
					
					sync(10);
					driver.navigate().refresh();
					sync(5);
				}
				sync(9);
				if(count==tablesize){
					report.updateTestLog("Vision Tools", "All Logs appears in <b>Green</b> color successfully", Status.PASS);
					report.updateTestLog("Vision Tools", "All Logs appears in <b>Green</b> color successfully", Status.SCREENSHOT);
				}else{
					report.updateTestLog("Vision Tools", "Logs does not appears as <b>Green</b> even after waiting 5 Minutes", Status.FAIL);
				}
			}
		    catch (Exception e)
		    {
		    	report.updateTestLog("Vision Tools", "Exception - Unable to verify the Logs color as <b>Green</b>"+e.getMessage(), Status.DONE);
				e.printStackTrace();
			}
			return this;
		}

		public VisionTools_Page ValidateLegacyVoiceWO() throws InterruptedException
		{
			int count=0;
			int tablesize=0;
			try{
				String siteIDNumber = "";
				String SiteValue = "";
				if (properties.getProperty("DefaultDataSheet").equals("QA2")) {
					siteIDNumber = "216";
					SiteValue = "216 - Connecticut";
				}
				
				if (properties.getProperty("DefaultDataSheet").equals("MACON_PROD")) {
					siteIDNumber = "0010";
					SiteValue = "001 - Macon";
				}
				
				if (properties.getProperty("DefaultDataSheet").equals("SAN_PROD")) {
					siteIDNumber = "541";
					SiteValue = "541 - San Diego";
				}
				
				Select Sel1 = new Select(LegacySite);
				Sel1.selectByValue(SiteValue);
				sync(2);
				report.updateTestLog("Vision Tools", "Selects <b>SiteID Value</b> from List successfully", Status.PASS);
				
				//Tasks by Account Number
				Select Sel2 = new Select(LegacyQueryBy);
				Sel2.selectByValue("Tasks by Account Number");
				sync(2);
				
				report.updateTestLog("Vision Tools", "Selects <b>Tasks by Account Number</b> from List successfully", Status.PASS);
				String ActNo = siteIDNumber + dataTable.getData(env,"AccountNumber");
				LegacyAccountNum.clear();
				LegacyAccountNum.sendKeys(ActNo);
				report.updateTestLog("Vision Tools", "Entered <b>"+ActNo+"</b> successfully", Status.PASS);
				
				for(int i=0; i<40; i++){
					Submit.click();
					WaitTool.waitForElementDisplayed(driver, LegacyVMStatusTable, 20);
					//String color = null;
					String WOStatus = null; 
					count =0;
					List <WebElement> tablelist = driver.findElements(By.xpath("//table[contains(@id,'voipWorkorderStatusTable')]//tr/td[3]/span"));
						tablesize = tablelist.size();
						for(int j=1;j<=tablesize;j++){
							WOStatus = driver.findElement(By.xpath("//table[contains(@id,'voipWorkorderStatusTable')]//tr["+j+"]/td[3]/span")).getText();
							if(WOStatus.equals("Success")){
								count =count + 1;
							}
						}
					if(count==tablesize){
						break;
					}
					
					sync(20);
				}
				if(count==tablesize){
					report.updateTestLog("Vision Tools", "All Logs status appears <b>Success</b>", Status.PASS);
				}else{
					report.updateTestLog("Vision Tools", "All Logs does not appear <b>Succes</b> even after waiting 5 Minutes", Status.FAIL);
				}
			}
		    catch (Exception e)
		    {
		    	report.updateTestLog("Vision Tools", "Exception - Unable to verify the Logs color as <b>Success</b>", Status.FAIL);
				e.printStackTrace();
			}
			return this;
		}
		
		public VisionTools_Page search_AccountNumber(){
			
			queryBy_DropDown.click();
			sync(2);
			accountNum_Input.sendKeys(dataTable.getData(env, "SiteId")+dataTable.getData(env, "AccountNumber"));
			sync(2);
			sumbit_Button.click();
			sync(5);
			return this;
		}
	}

	
	
	
