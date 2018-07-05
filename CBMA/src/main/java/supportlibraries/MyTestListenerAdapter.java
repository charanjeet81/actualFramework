package supportlibraries;

import java.util.Iterator;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class MyTestListenerAdapter extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult result) {
        if (result.getMethod().getRetryAnalyzer() != null) {
            MyRetryAnalyzer retryAnalyzer = (MyRetryAnalyzer)result.getMethod().getRetryAnalyzer();

            if(retryAnalyzer.isRetryAvailable()) {
                result.setStatus(ITestResult.SKIP);
            } else {
                result.setStatus(ITestResult.FAILURE);
            }
            Reporter.setCurrentTestResult(result);
        }
    }

   @Override
   public void onFinish(ITestContext context) {
     Iterator<ITestResult> failedTestCases =context.getFailedTests().getAllResults().iterator();
     Iterator<ITestResult> skippedTestCases =context.getSkippedTests().getAllResults().iterator();
    while (failedTestCases.hasNext()) {
        System.out.println("failedTestCases");
        ITestResult failedTestCase = failedTestCases.next();
        ITestNGMethod method = failedTestCase.getMethod();
        if (context.getFailedTests().getResults(method).size() > 1) {
            System.out.println("failed test case remove as dup:" + failedTestCase.getTestClass().toString());
            failedTestCases.remove();
        } else{

            if (context.getPassedTests().getResults(method).size() > 0) {
                System.out.println("failed test case remove as pass retry:" + failedTestCase.getTestClass().toString());
                failedTestCases.remove();
            }
        }
        	if (context.getSkippedTests().getResults(method).size() > 1) {
                System.out.println("skipped test case remove as dup:" + failedTestCase.getTestClass().toString());
                skippedTestCases.remove();
           }
    }
   }

}
