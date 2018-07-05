package businesscomponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.support.PageFactory;

import com.cognizant.framework.Settings;

import businesscomponents.Constants;
import qc.rest.examples.EntityUpdater;
import supportlibraries.DriverScript;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import supportlibraries.TestCase;

public class ALMResultUpdater{

	private Properties properties;
	
	private String testSetID;
	
    static EntityUpdater restObj= new EntityUpdater(Constants.DOMAIN, Constants.PROJECT);
	
	private String run_ID;	
	
	//ScriptHelper scriptHelper;
//	public ALMResultUpdater(ScriptHelper scriptHelper) {
//		super(scriptHelper);
//		PageFactory.initElements(driver, this);
//	}
			
	public synchronized void connect()
	{
		try 
		{
			restObj.loginCall();
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ALM connect");
	}
	
/*	public synchronized void createRun(String testCaeName,String testCaseStatus,Date duration,String executionTime)
	{	
		properties = Settings.getInstance();	
		
		testSetID=properties.getProperty("TestSetID");
				
		//get test ID					
		String testId =getTestID(testCaeName);
			
		//get test instance id
		String testInstance=getTestInstance(testId,testSetID);
		
		String responseXMLToGetRunID=restObj.createEntity("run", "name", "Run:"+duration ,testInstance, testId, properties.getProperty("almid"), "Not Completed" ,testSetID);
							
		run_ID= StringUtils.substringBetween(responseXMLToGetRunID, "<Field Name=\"id\"><Value>", "</Value></Field>");
		
		restObj.updateRunStatus("run", run_ID, "status", testCaseStatus,"duration",executionTime.split("minute")[0]);
		
	}
	*/
	
	
	public synchronized void createRun(String testCaeName, String testCaseStatus, Date duration, String executionTime,String pack,String zipReportPath) 
	{
       String testId = null;
        properties = Settings.getInstance();
        System.out.println("testCaeName:"+testCaeName);
        System.out.println("testCaseStatus:"+testCaseStatus);
       // String pack=testCaeName.getClass().getPackage().getName();
        //Package packagename=scriptHelper.getTestCaseName().getClass().getPackage();
       // String pack=packagename.getName();
        //System.out.println("Package name in create Run"+pack);
        // @For selinium grid
        if (properties.get("ExecutionMode").toString().trim().equalsIgnoreCase("Grid")||properties.get("ExecutionMode").toString().trim().equalsIgnoreCase("Docker")) // --Shravan
        {
              String testcaseNameQC[] = testCaeName.split("__");
              testSetID = properties.getProperty("TestSetID_"+pack+"_" + testcaseNameQC[1]).trim();
              
              //testSetID = properties.getProperty("TestSetID_"+ testcaseNameQC[1]).trim();
//              System.out.println("CreateRun_TestSetID: "+testSetID+" for  "+pack+"  test case aname ---"+testcaseNameQC[0]);
//              System.out.println("CreateRun_functionality name: "+pack);
//              System.out.println("CreateRun_Testcase Name: "+testcaseNameQC[0]);
              // get test ID
              //testId = getTestID(testcaseNameQC[0]); 
              System.out.println("CreateRun_TestSetID: "+testSetID+" for  "+pack+"_"+testcaseNameQC[1]+"  test case aname ---"+testcaseNameQC[0]);
              updateEverything(testcaseNameQC[0],testCaseStatus,duration,executionTime,testSetID,zipReportPath);
              
              //System.out.println("TestID: "+testId);
              // get test instance id
        } else {
              testId = getTestID(testCaeName);
              testSetID = properties.getProperty("TestSetID");
              System.out.println("CreateRun_TestSetID: "+testSetID+" for  "+pack+"  test case aname ---"+testCaeName+" testcase ID=="+testId);
        }
        
//        String testInstance = getTestInstance(testId, testSetID);
//        String responseXMLToGetRunID = restObj.createEntity("run", "name", "Run:" + duration, testInstance, testId, properties.getProperty("almid"), "Not Completed", testSetID);
//        run_ID = StringUtils.substringBetween(responseXMLToGetRunID, "<Field Name=\"id\"><Value>", "</Value></Field>");
//        restObj.updateRunStatus("run", run_ID, "status", testCaseStatus, "duration", executionTime.split("minute")[0]);

  }

	
	public synchronized void updateAttachment(String attachmentPath, String testCaseName)
	{
		byte[] filedata;	
		filedata=convertTobytes(attachmentPath);		
		restObj.CreateTCAttachment(run_ID, filedata, testCaseName+".zip");
	}
	
	public byte[] convertTobytes(String attachPath)
	{
		  File file = new File(attachPath);		  
	      byte[] b = new byte[(int) file.length()];
	      try 
	         {	      
	        	  FileInputStream fileInputStream = new FileInputStream(file);
	               fileInputStream.read(b);
	               fileInputStream.close();
	          } 
	          catch (FileNotFoundException e) 
	          {
	                      System.out.println("File Not Found.");
	                      e.printStackTrace();
	          }
	          catch (IOException e1) 
	          {
	                    System.out.println("Error Reading The File.");
	                    e1.printStackTrace();
	          }
	        
	         return b;

	}
	
	public synchronized void disconnect()
	{
		try {
			restObj.logoutCall();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*Function to get test instances and test id */
	public static HashMap<String,String> getTestInstances(EntityUpdater rest, String testSetID) 
		 {
			String responseXml = rest.getEntity("test-instance", null, "contains-test-set.id["+testSetID+"]");
			
			String[] testInstanceIDs = StringUtils.substringsBetween(responseXml, "<Field Name=\"id\"><Value>", "</Value></Field>");
			
			String[] testIDs = StringUtils.substringsBetween(responseXml, "<Field Name=\"test-id\"><Value>", "</Value></Field>");
			
			HashMap<String, String> testInstanceID=new HashMap<String, String>(); 
			 
			 int testcount=testInstanceIDs.length;
			 
			 for( int i=0;i<testcount;i++ )
			 { 
				 testInstanceID.put(testInstanceIDs[i], testIDs[i]);	
			 }		
			
			 return testInstanceID;
		
		}
	
	
	
	/*Function to get test instance based on test Id and test Set*/
	public synchronized String getTestInstance(String testId,String testSetID)
	{
		String responseXML=restObj.getEntity("test-instance", null , "cycle-id["+testSetID+"];test-id["+testId+"]");
		
		String testInstanceId=StringUtils.substringBetween(responseXML, "<Field Name=\"id\"><Value>", "</Value></Field>");
		
		return testInstanceId;
	}
		 

	/*Function to get test id from test Name*/
	public synchronized String getTestID(String testName)
	{
		String responseXML=restObj.getEntity("test", null , "name["+testName+"]");
		
		String testID=StringUtils.substringBetween(responseXML, "<Field Name=\"id\"><Value>", "</Value></Field>");
		
		return testID;
	}
	
	public synchronized String getTestID(String testName,String testSetID)
	{
		String responseXML=restObj.getEntity("test", null , "cycle-id["+testSetID+"];name["+testName+"]");
		
		String testID=StringUtils.substringBetween(responseXML, "<Field Name=\"id\"><Value>", "</Value></Field>");
		
		return testID;
	}
	
	public synchronized void updateEverything(String testCaeName, String testCaseStatus, Date duration, String executionTime,String testSetID,String zipReportPath){
		String testid=getTestID(testCaeName);
		String testInstance = getTestInstance(testid, testSetID);
		System.out.println("updateEverything_TestSetID: "+testSetID+" for  test case aname"+testCaeName);
        String responseXMLToGetRunID = restObj.createEntity("run", "name", "Run:" + duration, testInstance, testid, properties.getProperty("almid"), "Not Completed", testSetID);
        run_ID = StringUtils.substringBetween(responseXMLToGetRunID, "<Field Name=\"id\"><Value>", "</Value></Field>");
        restObj.updateRunStatus("run", run_ID, "status", testCaseStatus, "duration", executionTime.split("minute")[0],testCaeName);
        updateAttachment(zipReportPath, testCaeName);
	}
	
	
}
