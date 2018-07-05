package testscripts.SMOKE;

import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;

import supportlibraries.DriverScript;
import supportlibraries.TestCase;

public class SMOKE_CBMA_BillPay_Flow_1544515 extends TestCase
{
	@Test
	public void runSMOKE_TC057_E2E_Flow_For_Single_Acct_Single_Stmt_1544515()
	{
		testParameters.setCurrentTestDescription("Bill_Pay_End_To_End_Flow_For_Single_Acct_With_Single_Statement.");
		testParameters.setIterationMode(IterationOptions.RunOneIterationOnly);
		//testParameters.setBrowser(Browser.Firefox);		
		driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
	}	
}