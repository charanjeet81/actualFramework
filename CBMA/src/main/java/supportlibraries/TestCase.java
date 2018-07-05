package supportlibraries;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import businesscomponents.ALMResultUpdater;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Util;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


/**
 * Abstract base class for all the test cases to be automated
 * @author Cognizant
 */
public abstract class TestCase extends ResultSummaryManager
{
	
	
	/**
	 * The {@link SeleniumTestParameters} object to be used to specify the test parameters
	 */
	protected SeleniumTestParameters testParameters;
	/**
	 * The {@link DriverScript} object to be used to execute the required test case
	 */
	protected DriverScript driverScript;
	
	
	private Date startTime, endTime;
	//ALMResultUpdater almObj=new ALMResultUpdater();
	
	@BeforeSuite(alwaysRun = true)
	public void suiteSetup(ITestContext testContext) throws IOException
	{
		setRelativePath();
		initializeTestBatch();
       ////////////////////////////////////////////////////////////////////
		if(properties.getProperty("RetryFailedTestCase").equals("Yes"))
		{
		//System.out.println(testContext.getAllTestMethods());
		//System.out.println(testContext.getSuite().getAllMethods().size());
         //for (ITestNGMethod method : testContext.getAllTestMethods()) {
		for (ITestNGMethod method : testContext.getSuite().getAllMethods()) {
			testContext.getAttributeNames();
             method.setRetryAnalyzer(new Retry());
       
         }
		}
        //////////////////////////////////////////////////////////////////////
		int nThreads;
		if (testContext.getSuite().getParallel().equalsIgnoreCase("false")) {
			nThreads = 1;
		} else {
			nThreads = testContext.getCurrentXmlTest().getThreadCount();
		}
	
		initializeSummaryReport(testContext.getSuite().getName(), nThreads);
		
		try {
			setupErrorLog();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
		/*Runtime.getRuntime().exec(new String[]{"cmd","/c",frameworkParameters.getRelativePath()
				+ Util.getFileSeparator() + "src" + Util.getFileSeparator()
				+ "main" + Util.getFileSeparator() + "resources"
				+"/kill_exe.bat"});*/
		
		/*Connect to ALM if execution is to be done from Framework*/
//		if(properties.getProperty("ALMExecutionFlag").equals("0"))
//		{
//			almObj.connect();
//		}
		
		if (properties.getProperty("ExecutionMode").equals("Docker")) {
			try {
				SeleniumGridDocker.checkSystemResources();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			// Selenium Grid Hub Creation			
			SeleniumGridDocker.createSeleniumGridHub();
			
			//Creation of Chrome Nodes
			int ChromeNodes = Integer.parseInt(properties.getProperty("ChromeNodes"));
			for (int i = 1 ; i <= ChromeNodes; i++){
				SeleniumGridDocker.createChromeNode();
			}
			
			//Creation of Firefox Nodes
			int FirefoxNodes = Integer.parseInt(properties.getProperty("FirefoxNodes"));
			for (int i = 1 ; i <= FirefoxNodes; i++){
				SeleniumGridDocker.createFirefoxNode();
			}
		}
	}
	
	@BeforeMethod
	@Parameters("browser")
	public void testMethodSetup(@Optional String strBrowser)
	{
		if(frameworkParameters.getStopExecution()) {
			suiteTearDown();

			throw new SkipException("Aborting all subsequent tests!");
		} 
		else if(strBrowser==null)
			{
			startTime = Util.getCurrentTime();
			String package_name= this.getClass().getPackage().getName();
			int index = package_name.lastIndexOf(".");
			String currentScenario =
					capitalizeFirstLetter(package_name.substring(index +1));
			String currentTestcase = this.getClass().getSimpleName();
			testParameters = new SeleniumTestParameters(currentScenario, currentTestcase);
			String appBrowser = properties.get("DefaultBrowser").toString();
			if(appBrowser.contains("Chrome"))
				testParameters.setBrowser(Browser.Chrome);
			else if(appBrowser.contains("Firefox"))
				testParameters.setBrowser(Browser.Firefox);
			else
				testParameters.setBrowser(Browser.InternetExplorer);
				System.out.println(testParameters.getBrowser());
			}
		 else
			{
			startTime = Util.getCurrentTime();
			String package_name=this.getClass().getPackage().getName();
			int index = package_name.lastIndexOf(".");
			String currentScenario = capitalizeFirstLetter(package_name.substring(index +1));
				String currentTestcase = this.getClass().getSimpleName();
				//@For Selenium Grid
				currentTestcase = currentTestcase+"__"+strBrowser;
				testParameters = new SeleniumTestParameters(currentScenario, currentTestcase);
				
				if (testParameters.getBrowser() == null) {
					testParameters.setBrowser(Browser.valueOf(strBrowser));
				}
			}
		}


	
/*	@BeforeMethod
	public void testMethodSetup()
	{
		if(frameworkParameters.getStopExecution()) {
			suiteTearDown();
			
			throw new SkipException("Aborting all subsequent tests!");
		} else {
			startTime = Util.getCurrentTime();
			
			String currentScenario =
					capitalizeFirstLetter(this.getClass().getPackage().getName().substring(12));
			String currentTestcase = this.getClass().getSimpleName();
			testParameters = new SeleniumTestParameters(currentScenario, currentTestcase);
		}
	}*/
	
	private String capitalizeFirstLetter(String myString)
	{
		StringBuilder stringBuilder = new StringBuilder(myString);
		stringBuilder.setCharAt(0, Character.toUpperCase(stringBuilder.charAt(0)));
		return stringBuilder.toString();
	}
	@AfterMethod
	public void testMethodTearDown()
	{
		String testStatus = driverScript.getTestStatus();
		endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		try{
		summaryReport.updateResultSummary(testParameters.getCurrentScenario(),
									testParameters.getCurrentTestcase(),
									testParameters.getCurrentTestDescription(),
									executionTime, testStatus);
		}catch(Exception e){
			
		}
	}
	
	@AfterSuite
	public void suiteTearDown()
	{	
		/*Disconnect from ALM after running all tests*/
//		if(properties.getProperty("ALMExecutionFlag").equals("0"))
//		{
//			almObj.disconnect();			
//		}
		wrapUp();
		
		// - Selenium GRid Hub Creation
		if (properties.getProperty("ExecutionMode").equals("Docker")) 
		{
			SeleniumGridDocker.cleanupNodes();
			SeleniumGridDocker.cleanupGridHubNode();
		}

	}
}