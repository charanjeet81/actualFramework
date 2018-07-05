package supportlibraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Report;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;


/**
 * Class to extend the reporting features of the framework
 * @author Cognizant
 */
public class SeleniumReport extends Report
{
	private WebDriver driver;
	/**
	 * Function to set the {@link WebDriver} object
	 * @param driver The {@link WebDriver} object
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	
	/**
	 * Constructor to initialize the Report
	 * @param reportSettings The {@link ReportSettings} object
	 * @param reportTheme The {@link ReportTheme} object
	 */
	public SeleniumReport(ReportSettings reportSettings, ReportTheme reportTheme)
	{
		super(reportSettings, reportTheme);
	}
	
	@Override
	protected void takeScreenshot(String screenshotPath)
	{
		if (driver == null) {
			throw new FrameworkException("Report.driver is not initialized!");
		}
		
		if (driver.getClass().getSimpleName().equals("HtmlUnitDriver") || 
			driver.getClass().getGenericSuperclass().toString().equals("class org.openqa.selenium.htmlunit.HtmlUnitDriver")) {
			return;	// Screenshots not supported in headless mode
		}
		
		File scrFile;
		if (driver.getClass().getSimpleName().equals("RemoteWebDriver")) {
			Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
			if (capabilities.getBrowserName().equals("htmlunit")) {
				return;	// Screenshots not supported in headless mode
			}
			WebDriver augmentedDriver = new Augmenter().augment(driver);
	        scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		} else {
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}
		
		try {
			FileUtils.copyFile(scrFile, new File(screenshotPath), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while writing screenshot to file");
		}
	}
	
	
	//######################## V A R I A B L E S ############################//
	 String path = "C:\\Users\\465839\\Desktop\\POI.xlsx";
	 String sheetName = "Result";
	 static int rowNumber = 0;
	 
	 public  String getData(String path, String sheetName, int rowNumber) 
		{
			int cellNumber = 0;
			String data = null;
			try
			{
				FileInputStream fis = new FileInputStream(new File(path));	
				data = WorkbookFactory.create(fis).getSheet(sheetName).getRow(rowNumber).getCell(cellNumber).getStringCellValue();
				if(data.isEmpty())
				{
					data = "Data not Found.";
				}
			} 
			catch (Exception e) 
			{
				System.err.println("Error while getting Data.");
			}
			
			return data;		
		}
		
		public  void setData(String path, String sheetName, String dataToSet) 
		{
			int cellNumber = 0;
	    	try 
			{
				FileInputStream fis = new FileInputStream(new File(path));
				Workbook wb =  WorkbookFactory.create(fis);
				
				Sheet sh = wb.getSheet(sheetName);
				Row r = sh.createRow(rowNumber);
				Cell c = r.createCell(cellNumber);
				if(StringUtils.isNotBlank(dataToSet)) 
				{ 
					c.setCellValue(dataToSet);
				}
				
				FileOutputStream fos = new FileOutputStream(new File(path));
				wb.write(fos);
				fos.flush();
				fos.close();
				rowNumber++;
				//System.out.println("Data written in Excel Sheet."+rowNumber++);
			} catch (Exception e) 
	    	{
				e.printStackTrace();
			}
		}
	//#######################################################################//
	 
	 /*	public  void generate_HTML()
	{
		
		while (getData(path, sheetName, rowNumber)!=null) 
		{
			String data = getData(path, sheetName, rowNumber);
			String[] arr = data.split(",");
			
			System.out.println(arr[0]+" "+arr[1]+" "+arr[2]);
			rowNumber++;		
		}
		
	}
	
	public  int allRows(String path, String sheetName)  //no use here.
	{
		int rows = 0 ;
		try 
		{
			FileInputStream fis = new FileInputStream(new File(path));
			Workbook wb =  WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(sheetName);
			rows  = sh.getPhysicalNumberOfRows();     // Acctual
			System.out.println(sh.getLastRowNum());
		
		} 
		catch (Exception e)
		{
			System.err.println("Correct Rows are not returned.");
		}
		return rows;
	}*/
	
	
	

	//----------------------------
	
	
	
	
}