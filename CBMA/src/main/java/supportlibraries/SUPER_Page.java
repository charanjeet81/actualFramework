package supportlibraries;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


import businesscomponents.UtilTools;
import businesscomponents.WaitTool;

import com.cognizant.framework.CraftDataTable;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
//import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Abstract base class for reusable libraries created by the user
 * 
 * @author Cognizant
 */
public abstract class SUPER_Page {
	public String env = UtilTools.getEnv();
	static String sheetName = "Result";
	static int rowNumber = 0;

	/**
	 * The {@link CraftDataTable} object (passed from the test script)
	 */
	protected CraftDataTable dataTable;
	/**
	 * The {@link SeleniumReport} object (passed from the test script)
	 */
	protected SeleniumReport report;
	/**
	 * The {@link WebDriver} object
	 */
	protected WebDriver driver;
	/**
	 * The {@link ScriptHelper} object (required for calling one reusable
	 * library from another)
	 */
	protected ScriptHelper scriptHelper;

	/**
	 * The {@link Properties} object with settings loaded from the framework
	 * properties file
	 */
	protected Properties properties;
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters;

	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the
	 * objects wrapped by it
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object
	 */

	public SUPER_Page(ScriptHelper scriptHelper) {
		this.scriptHelper = scriptHelper;
		this.dataTable = scriptHelper.getDataTable();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getDriver();
		properties = Settings.getInstance();
		frameworkParameters = FrameworkParameters.getInstance();
		PageFactory.initElements(driver, this);
	}

	// ########################### C O M M O N O B J E C T S
	// ############################//
	@FindBy(xpath = "//a[@title='Select Location']/..")
	public WebElement selectLocation_POPUP;
	@FindBy(css = "div#multi-col-1>div:nth-child(1)")
	public WebElement firstSearchCard;
	@FindBy(css = "p.pageHeader.boldtext")
	public WebElement pageHeader;
	@FindBy(css = "input#USER")
	public WebElement txt_userid;
	@FindBy(css = "input#PASSWORD")
	public WebElement txt_password;
	@FindBy(id = "sign-in-btn")
	public WebElement btn_signIn;
	@FindBy(css = "input[id = 'changePasswordForm:newPassword']")
	public WebElement newPassword_EDT;
	@FindBy(css = "input[id ='cpwd']") // for onboarding flow
	public WebElement confirmPassword_Onboarding_EDT;
	@FindBy(css = "input[id ='changePasswordForm:confirmPassword']")
	public WebElement confirmPassword_EDT;
	@FindBy(xpath = "//span[contains(text(), 'Your changes were saved successfully.')]")
	public WebElement success_msg;
	@FindBy(xpath = "//ul[@id='messages']//span")
	public WebElement successMsg;
	@FindBy(css = "span.pf-close-btn")
	public WebElement locationClose_BTN;
	@FindBy(xpath = "//label[contains(text(),'Account Alias')]")
	WebElement account_alias_stepFive;
	@FindBy(xpath = "//ul[@id='messages']/li[1]/span")
	public WebElement errorMsg;
	@FindBy(css = "img[id='administration-img']")
	public WebElement adminImage;
	@FindBy(xpath = "(//a[contains(text(),'Product Administration')])[2]")
	public WebElement newProductAdmin;
	@FindBy(xpath = "(//a[contains(text(),'Product Administration')])[3]")
	public WebElement newProductAdmin1;
	@FindBy(css = "ul#messages>li")
	public WebElement msg;
	@FindBy(xpath = "(//a[contains(text(),'X')])[2]")
	public WebElement bottomSlider_AND;
	@FindBy(xpath = "//h1[contains(text(),'Create a Ticket')]")
	WebElement createTicket_LBL;
	@FindBy(xpath = "(//div[@class='pf-sub-nav-close']/a)[4]")
	public WebElement leftSlider_AND;
	@FindBy(xpath = "(//div[@class='pf-sub-nav-close'])[5]")
	public WebElement sliderClose_BTN;
	@FindBy(css = "li.msg-error")
	public WebElement errorMSG;
	@FindBy(css = "li.msg-info")
	public WebElement infoMSG;
	@FindBy(xpath = "//span[@class='ui-paginator-current']")
	public List<WebElement> pagination_number;
	@FindBy(css = "a.pf-close-btn")
	public WebElement locationClosed_AND;
	@FindBy(css = "img#administration-img")
	public WebElement adminLink_AND;
	@FindBy(xpath = "(//a[contains(text(),'Add Account')])[2]")
	public WebElement addAccount_LP;
	@FindBy(xpath = "(//a[contains(text(),'Profile Admin')])[2]")
	public WebElement profAdmin_LP;
	@FindBy(xpath = "//span[@class='pf-close-btn']")
	public WebElement sliderClose_BTN_AND;
	@FindBy(xpath = "//span[contains(text(),'Caller ID')]")
	public WebElement callerIdLink;
	@FindBy(xpath = "//div[@id='nav:breadcrumbs']//a")
	List<WebElement> navBreadcrumbs;
	@FindBy(css = "div.pf-main-header")
	public WebElement aem_HDR;
	@FindBy(css = "div.pf-footer-toaster-inner")
	public WebElement aem_FTR;
	@FindBy(xpath = "//span[text()='MyAccount']/..")
	public WebElement signIN_AEM_LNK;
	@FindBy(xpath = "//a[contains(text(),'Logout')]")
	public WebElement logOUT_AEMADMIN_LNK;
	@FindBy(id="pf-signin-trigger")
	public WebElement myAccountDropdown;
	/*
	 * @FindBy(xpath=
	 * "//div[@class='pf-authenticated-links']//a[contains(text(),'Sign Out')]")
	 * public WebElement signOUT_AEM_LNK;
	 */
	@FindBy(xpath = "//div[@class='pf-mobile-only']//a[contains(text(),'X')]")
	public WebElement closeXLNK_AND;
	@FindBy(xpath = "//li[@class='msg-error last-child first-child']/span")
	public WebElement err_MSG;
	@FindBy(css = "li.msg-alert span")
	public WebElement alertMSG;
	@FindBy(css = "div[id='voice']")
	public WebElement voiceImage;
	@FindBy(css = "img[id='businessemail-img']")
	public WebElement businessImage;
	@FindBy(xpath = "(//li[@class='msg-error'])[2]")
	public WebElement err_MSG2;
	@FindBy(xpath = "//h1[contains(text(),'Welcome')]")
	public WebElement welcometext;
	@FindBy(xpath = "//div[@id='admin']//a[contains(text(),'Administration')]")
	public WebElement administrationlink;
	@FindBy(xpath = "(//a[contains(text(),'Account Authorization Settings')])[3]")
	public WebElement accountauthorizationstt;
	@FindBy(xpath = "(//h1[contains(text(),'Account Authorization Settings')]")
	public WebElement accountauthorizationtext;
	@FindBy(id = "step1-continue")
	public WebElement continuebutt;

	public JavascriptExecutor jse;

	// ########################### C O M M O N M E T H O D S ############################//

	public boolean deviceExecution() {
		return properties.getProperty("MobileExecution").contains("Y");
	}

	/**
	 * This method is to validate breadcrumbs.
	 * 
	 * @author Charanjeet
	 * @param array of breadcrumbs
	 * @param array of href
	 **/
	public void validate_BreadCrumbs(String[] breadcrumbs, String[] related_hrefs)
	{
		int count = 0;
		for (final String breadcrumb : breadcrumbs) {
			if (driver.findElement(By.xpath("//a[contains(text(),'" + breadcrumb.trim() + "')]")).getAttribute("href")
					.contains(related_hrefs[count]))
				Reporting(breadcrumb + ", breadcrumb is coming as expected and navigating to: " + related_hrefs[count],
						Status.PASS);
			else
				Reporting(breadcrumb + ", breadcrumb is not coming as expected and navigating to: "
						+ related_hrefs[count], Status.FAIL);
			count++;
		}
	}

	/**
	 * This method is to validate breadcrumbs.
	 * 
	 * @author Charanjeet
	 * @param array
	 *            of breadcrumbs
	 * @param array
	 *            of href
	 **/
	public void validate_BreadCrumbs_Blueprint_Page(String[] breadcrumbs, String[] related_hrefs)
	{
		sync(4);
		int count = 0;
		for (final String breadcrumb : breadcrumbs) {
			WebElement breadCrumb_WE = driver
					.findElement(By.xpath("//span[contains(text(),'" + breadcrumb.trim() + "')]"));
			try {
				if (breadCrumb_WE.isDisplayed()) {
					Reporting(breadcrumb + " breadcrumb is displayed as expected.", Status.DONE);
					if (!(count == 2)) {
						try {
							if (breadCrumb_WE.findElement(By.xpath("./..")).getAttribute("href")
									.contains(related_hrefs[count]))
								Reporting(breadcrumb + ", breadcrumb is coming as expected and navigating to: "
										+ related_hrefs[count], Status.PASS);
							else
								Reporting(breadcrumb + ", breadcrumb is not coming as expected and navigating to: "
										+ related_hrefs[count], Status.FAIL);
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				Reporting(breadcrumb + " breadcrumb is not displayed as expected.", Status.FAIL);
			}
			count++;
		}
	}

	/**
	 * This method is to open URL at any instance
	 * 
	 * @author Charanjeet
	 * @param URL
	 *            to open
	 **/
	public void open_URL(String url)
	{
		driver.get(url);
		driver.manage().timeouts().pageLoadTimeout(21, TimeUnit.SECONDS);
		Reporting(url + " is opened.", Status.DONE);
	}

	/**
	 * This method is to validate URL part, passed as a parameter
	 * 
	 * @author Charanjeet
	 * @param Text
	 *            to validate
	 **/
	public void validate_URL(String textToValidate) 
	{
		waitForPageToBeReady();
		driver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.SECONDS);
		if (driver.getCurrentUrl().contains(textToValidate.trim()))
			Reporting(textToValidate + " is coming as a part of URL <br/> " + driver.getCurrentUrl(), Status.DONE);
		else
			Reporting(textToValidate + " is not coming as a part of URL " + driver.getCurrentUrl(), Status.FAIL);
	}

	/**
	 * This method is to click on the WebElement if it is displayed.
	 * 
	 * @author Charanjeet
	 * @param WebElement
	 *            to Click
	 **/
	public void clickElementIfDisplayed(WebElement element) 
	{
		WaitTool.waitForElementisClickable(driver, element, 9);
		try {
			if (element.isDisplayed()) {
				Reporting("Element is displyed to click.", Status.DONE);
				element.click();
				Reporting("Element is clicked.", Status.DONE);
			}
		} catch (Exception e) {
			Reporting("Element is not displayed to click.", Status.DONE);
		}
	}

	/**
	 * This method is to check placeholder
	 * 
	 * @author Charanjeet
	 * @param Placeholder
	 *            to validate
	 * @param Input
	 *            field for which you want to check placeholder
	 **/
	public void validate_Placeholder(String placeHolder, WebElement inputField) {
		WaitTool.waitForElementDisplayed(driver, inputField, 18);
		if (inputField.getAttribute("placeholder").contains(placeHolder))
			Reporting(placeHolder + ", is coming as placeholder as expected.", Status.PASS);
		else
			Reporting(placeHolder + ", is coming as placeholder as expected.", Status.FAIL);
	}

	/**
	 * This method is to check title of the page
	 * 
	 * @author Charanjeet
	 * @param Title
	 *            to validate
	 **/
	public void validate_PageTitle(String titleToValidate) {
		driver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.SECONDS);
		if (driver.getTitle().contains(titleToValidate.trim()))
			Reporting(titleToValidate + ", title is coming as page title.", Status.DONE);
		else
			Reporting(titleToValidate + ", title is not coming as page title.", Status.FAIL);
	}

	/**
	 * This method is to check heading displayed.
	 * 
	 * @author Charanjeet
	 * @param Heading
	 *            to validate
	 * @param Underlying
	 *            html tag
	 **/
	public void validate_HeadingDisplayed(String heading, String htmlTag) {
		try {
			WebElement headinghOBJ = driver.findElement(By.xpath("(//" + htmlTag + "[contains(text(),'" + heading.trim() + "')])[1]"));
			WaitTool.waitForElementDisplayed(driver, headinghOBJ, 18);
			if (headinghOBJ.isDisplayed())
				Reporting(heading + ", heading is displayed as expected.", Status.PASS);
		} catch (Exception e) {
			Reporting(heading + ", heading is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check page content, basically text.
	 * 
	 * @author Charanjeet
	 * @param Text to validate
	 * @param Underlying html tag
	 **/
	public void validate_PageContent(String textToValidate, String htmlTag) {
		try {
			WebElement textOBJ = driver.findElement(By.xpath("(//" + htmlTag + "[contains(text(),'" + textToValidate.trim() + "')][1])"));
			WaitTool.waitForElementDisplayed(driver, textOBJ, 18);
			scrollToElement(textOBJ);
			if (textOBJ.isDisplayed())
				Reporting(textToValidate + ", text is displayed as expected.", Status.PASS);
		} catch (Exception e) {
			Reporting(textToValidate + ", text is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check if any page element is displayed or not
	 * 
	 * @author Charanjeet
	 * @param Heading
	 *            to validate
	 * @param Description
	 *            about element
	 **/
	public void validate_ElementDisplayed(WebElement webElement, String elementDescription) {
		WaitTool.waitForElementDisplayed(driver, webElement, 21);
		try {
			if (webElement.isDisplayed())
				Reporting(elementDescription + " is displayed as expected.", Status.PASS);
		} catch (Exception e) {
			Reporting(elementDescription + " is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check if any page element is displayed or not
	 * 
	 * @author Charanjeet
	 * @param Value
	 *            to check
	 * @param Underlying
	 *            html tag
	 **/
	public void validate_ElementDisplayed(String value, String htmlTag) {
		WebElement linkWE = null;
		try {
			linkWE = driver.findElement(By.xpath("(//" + htmlTag + "[contains(text(),'" + value + "')])[1]"));
			WaitTool.waitForElementDisplayed(driver, linkWE, 18);
			scrollToElement(linkWE);
			if (linkWE.isDisplayed()) {
				Reporting("Element '" + linkWE.getText() + "' is displayed as expected.", Status.PASS);
			}
		} catch (Exception e) {
			Reporting("Element '" + linkWE.getText() + "' is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check dropdown content/options
	 * 
	 * @author Charanjeet
	 * @param Array
	 *            of options
	 * @param Webelement
	 *            for select dropdown/select
	 **/
	public void validate_DropdownContent(String[] contentList, WebElement selectElement) {
		int count = 0;
		WaitTool.waitForElementDisplayed(driver, selectElement, 21);
		Select dropdown = new Select(selectElement);
		for (WebElement option : dropdown.getOptions()) {
			if (option.getText().contains(contentList[count]))
				Reporting("<u>" + contentList[count] + "</u>, option is coming as a part of state dropdown list.",
						Status.PASS);
			else
				Reporting(contentList[count] + ", option is not coming as a part of state dropdown list.", Status.FAIL);
			count++;
		}
	}

	/**
	 * This method is to check if list items are displayed or not
	 * 
	 * @author Charanjeet
	 * @param List
	 *            to check
	 * @param Underlying
	 *            html tag
	 **/
	public void validate_List(String[] list, String htmlTag) {
		for (String listItem : list) {
			validate_ElementDisplayed(listItem, htmlTag);
		}
	}

	/**
	 * This method is navigate back.
	 * 
	 * @author Charanjeet
	 **/
	public void navigate_Back() {
		driver.navigate().back();
		driver.manage().timeouts().pageLoadTimeout(-1, TimeUnit.SECONDS);
		Reporting("Application is navigated back.", Status.DONE);
	}

	/**
	 * This method is check element should not displayed.(In case of defect)
	 * 
	 * @author Charanjeet
	 **/
	public void elementShouldNotDisplayed(WebElement webElement, String descriptions) {
		WaitTool.waitForElementDisplayed(driver, webElement, 18);
		try {
			if (webElement.isDisplayed())
				Reporting(descriptions + ", element displayed, should not be present.", Status.FAIL);
		} catch (Exception e) {
			Reporting(descriptions + ", element not displayed, should not be present also.", Status.PASS);
		}
	}

	public void sync() // implicit wait
	{
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	public void sync(int sec) // hard wait
	{
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCookies()
	{
		driver.manage().deleteAllCookies();
		sync(2);
		Reporting("Deleted browser cookies.", Status.DONE);
	}

	public void validate_ElementDisabled(WebElement element, String description) {
		if (element.getAttribute("disabled").contains("true"))
			Reporting("<u>" + description + "</u> field is disabled before making any search.", Status.DONE);
		else
			Reporting("<u>" + description + "</u> field is enabled before making any search.", Status.FAIL);
	}

	public void validateAttribute(WebElement element, String attribute, String value, String msg) {
		if (element.getAttribute(attribute).contains(value))
			Reporting(msg, Status.DONE);
		else
			Reporting(msg, Status.FAIL);
	}

	public void validateFeedBack(boolean validateFeedback) {
		try {
			if (driver.findElement(By.xpath("//h1[contains(text(),'We'd welcome your feedback!')]")).isDisplayed()) {
				if (validateFeedback) {
					validate_ElementDisplayed("We'd welcome your feedback!", "h1");
				} else {
					clickTo("No, thanks", "a");
				}
			}
		} catch (Exception e) {
		}
	}

	public void Reporting(String message, Status s) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		String callerMethod = stack[2].getMethodName();
		if (callerMethod.length() > 35)
			callerMethod = callerMethod.substring(0, 35);
		callerMethod = callerMethod.substring(0, 1).toUpperCase() + callerMethod.substring(1);
		if (s == Status.FAIL) {
			report.updateTestLog("<i>" + callerMethod + ":</i> @" + stack[2].getLineNumber(), message, s);
			System.out.println("<i>" + callerMethod + ":</i> @" + stack[2].getLineNumber());
		} else
			report.updateTestLog("<i>" + callerMethod + ":</i>", message, s);
	}

	public void clickElementUsing_JSE(WebElement webelement, String description) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", webelement);
		Reporting("<u>" + description + "</u> element is clicked using JSE.", Status.DONE);
	}

	public void clickTo(String link, String linkTag) {
		WebElement linkWE = null;
		try {
			linkWE = driver.findElement(By.xpath("//" + linkTag + "[contains(text(),'" + link + "')]"));
			WaitTool.waitForElementDisplayed(driver, linkWE, 9);
			if (linkWE.isDisplayed()) {
				Reporting("Link <u>" + link + "</u> is present on the page.", Status.DONE);
				scrollToElement(linkWE);
				linkWE.click();
				Reporting("Clicked to the link <u>" + link + "</u>.", Status.DONE);
			}
		} catch (Exception e) {
			if (link.equals("close X") || link.equals("MyAccount Home"))
				Reporting("<u>" + link + "</u> link is not available on the application.", Status.DONE);
			else
				Reporting("<u>" + link + "</u> link is not available on the application.", Status.FAIL);
		}
	}

	public void clickTo(String link, String value, int index) 
	{
		WebElement linkWE = null;
		try
		{
			linkWE = driver.findElement(By.xpath("(//" + link + "[contains(text(),'" + value + "')])[" + index + "]"));
			sync(1);
			WaitTool.waitForElementDisplayed(driver, linkWE, 9);
			if (linkWE.isDisplayed()) {
				Reporting("Link <u>" + link + "</u> is present on the page.", Status.DONE);
				scrollToElement(linkWE);
				linkWE.click();
				Reporting("Clicked to the link <u>" + link + "</u>.", Status.DONE);
			}
		} catch (Exception e) {
			Reporting(link + " link is not available on the application.", Status.FAIL);
		}
		sync(1);
	}

	public void clickTo(String tag, String parameter, String value) {

	}

	public void checkElementExistenance(WebElement element) {
		try {
			WaitTool.waitForElementDisplayed(driver, element, 13);
			if (element.isDisplayed()) {
				Reporting("Element '" + element.getText() + "' is displayed as expected.", Status.DONE);
			}
		} catch (Exception e) {
			Reporting("Element '" + element.getText() + "' is not displayed as expected.", Status.FAIL);
		}
		sync(1);
	}

	public void checkAllSelectOptions(WebElement select_WE, String... valueArray) {
		Select select = new Select(select_WE);
		List<WebElement> allOptions = select.getOptions();
		for (int count = 0; count < valueArray.length; count++) {
			if (allOptions.get(count).getText().contains(valueArray[count]))
				report.updateTestLog("CheckAllOptions", valueArray[count] + " is coming as expected.", Status.DONE);
			else
				report.updateTestLog("CheckAllOptions", valueArray[count] + " is not coming as expected.", Status.FAIL);
		}
	}

	public void selectByText(WebElement select_WE, String text) {
		WaitTool.waitForElementDisplayed(driver, select_WE, 18);
		scrollToElement(select_WE);
		Select select = new Select(select_WE);
		select.selectByVisibleText(text);
		sync(1);
		Reporting("<u>" + text + "</u> is selected from dropdown.", Status.DONE);
	}

	/**
	 * @author 587382 : Adarsha T This method is to select the value based on
	 *         the value
	 * @param select_WE
	 *            :Web Element
	 * @param text
	 *            : Value to be selected
	 */
	public void selectByValue(WebElement select_WE, String text) {
		WaitTool.waitForElementDisplayed(driver, select_WE, 18);
		Select select = new Select(select_WE);
		select.selectByValue(text);
		sync(1);
		Reporting("<u>" + text + "</u> is selected from dropdown based on the value.", Status.DONE);
	}

	//Method to select by Index - Aishwarya Jagadeesh
	public void selectByIndex(WebElement select_WE, int index) {
		WaitTool.waitForElementDisplayed(driver, select_WE, 18);
		scrollToElement(select_WE);
		Select select = new Select(select_WE);
		select.selectByIndex(index);
		sync(1);
		Reporting("<u>Index value of " +index  + "</u> is selected from dropdown.", Status.DONE);
	}
	
	public static  ArrayList<String> tabs;

	public void openNewTab() throws AWTException {
		Robot robot;
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_T);
		String tabURL = dataTable.getData(env, "TabURL");
		/*
		 * String selectAll = Keys.chord(Keys.CONTROL,"t");
		 * driver.findElement(By.id("USER")).sendKeys(selectAll);
		 */

		report.updateTestLog("OpenNewTab", "New tab is opened.", Status.DONE);
		tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window((String) tabs.get(1));
		driver.get(tabURL);
		report.updateTestLog("OpenNewTab", tabURL + " URL is opened.", Status.DONE);
	}

	public void SCROLL_PAGE(String value) {
		// for Scroll down the current page
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0, " + value + ")", "");
		Reporting("Page is scrolled down by " + value + " units.", Status.DONE);
		sync(2);
	}
	
	public void closeFeedback()
	{
		try {
			driver.findElement(By.xpath("(//a[@title='Click to close.'])[1]")).click();
			Reporting("Clicked to 'close feedback' button.", Status.DONE);
		} catch (Exception e) { }
	}

	public void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		sync(2);
	}

	public int CALCULATE_PAGE_SIZE() {
		String number = pagination_number.get(1).getText();
		String[] splited = StringUtils.split(number);
		int size = splited.length;
		String reqString = splited[size - 1].trim();
		int reqNumber = Integer.parseInt(reqString);
		return reqNumber;
	}

	public void focusOnElement(WebElement element) 
	{
		driver.findElement(By.tagName("body")).sendKeys(Keys.HOME);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		for (int second = 0;; second++) {
			if (second >= 60)
				break;
			jse.executeScript("window.scrollBy(0,50)", ""); // y value '800' can be altered
			try {
				Thread.sleep(1000);
				if (element.isDisplayed())
					break;
			} catch (Exception e) {
				continue;
			}
		}
	}

	/*
	 * public void scrollingView(WebElement element){ ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView(true);", element); }
	 */

	public void SCROLL_DOWN_COMPLETE() {
		SCROLL_PAGE("300");
		sync(1);
		SCROLL_PAGE("300");
		sync(1);
		SCROLL_PAGE("300");
		sync(1);
		SCROLL_PAGE("300");
		sync(1);
		SCROLL_PAGE("300");
		sync(1);
		SCROLL_PAGE("300");
		/*
		 * sync(2); Robot robot; try { robot = new Robot();
		 * robot.keyPress(KeyEvent.VK_CONTROL); robot.keyPress(KeyEvent.VK_END);
		 * robot.keyRelease(KeyEvent.VK_END);
		 * robot.keyRelease(KeyEvent.VK_CONTROL);
		 * report.updateTestLog("SCROLL_DOWN_COMPLETE",
		 * "Scrolling complete page.", Status.DONE); } catch (AWTException e) {
		 * report.updateTestLog("SCROLL_DOWN_COMPLETE",
		 * "Unable to scroll complete page.", Status.DONE); e.printStackTrace();
		 * }
		 */
	}

	public void SCROLL_UP_COMPLETE() {

		SCROLL_PAGE("-300");
		sync(1);
		SCROLL_PAGE("-300");
		sync(1);
		SCROLL_PAGE("-300");
		sync(1);
		SCROLL_PAGE("-300");
		sync(1);
		SCROLL_PAGE("-300");
		/*
		 * sync(2); Robot robot; try { robot = new Robot();
		 * robot.keyPress(KeyEvent.VK_CONTROL);
		 * robot.keyPress(KeyEvent.VK_HOME); robot.keyRelease(KeyEvent.VK_HOME);
		 * robot.keyRelease(KeyEvent.VK_CONTROL);
		 * report.updateTestLog("SCROLL_UP_COMPLETE", "Scrolling complete page."
		 * , Status.DONE); } catch (AWTException e) {
		 * report.updateTestLog("SCROLL_UP_COMPLETE",
		 * "Unable to scroll complete page.", Status.DONE); e.printStackTrace();
		 * }
		 */
	}

	public void HighlightElement(WebElement element) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int iCnt = 0; iCnt < 3; iCnt++) {
			js.executeScript("arguments[0].style.border='3px dotted black'", element);
			Thread.sleep(1000 / 7);
			js.executeScript("arguments[0].style.border='3px dotted white'", element);
			Thread.sleep(1000 / 7);
			js.executeScript("arguments[0].style.border='3px dotted black'", element);
			js.executeScript("arguments[0].style.border=''", element);
		}
	}

	public void selectDate(String date, boolean currentDate_BLN) {
		String day = date.split("-")[0];
		String month = date.split("-")[1];
		String year = date.split("-")[2];
		if (currentDate_BLN == true) {
			// get current date time with Date()
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date d = new Date();
			String currentDate = dateFormat.format(d);
			switch (currentDate.substring(3, 5)) {
			case "01":
				currentDate = currentDate.replace("-01-", "-January-");
				break;
			case "02":
				currentDate = currentDate.replace("-02-", "-February-");
				break;
			case "03":
				currentDate = currentDate.replace("-03-", "-March-");
				break;
			case "04":
				currentDate = currentDate.replace("-04-", "-April-");
				break;
			case "05":
				currentDate = currentDate.replace("-05-", "-May-");
				break;
			case "06":
				currentDate = currentDate.replace("-06-", "-June-");
				break;
			case "07":
				currentDate = currentDate.replace("-07-", "-July-");
				break;
			case "08":
				currentDate = currentDate.replace("-08-", "-August-");
				break;
			case "09":
				currentDate = currentDate.replace("-09-", "-September-");
				break;
			case "10":
				currentDate = currentDate.replace("-10-", "-October-");
				break;
			case "11":
				currentDate = currentDate.replace("-11-", "-November-");
				break;
			case "12":
				currentDate = currentDate.replace("-12-", "-December-");
				break;
			}
			dataTable.putData(env, "EndDate", currentDate);
		}
		boolean strFlag = false;
		for (int i = 0; i < 12; i++) {
			WebElement monthWE = driver.findElement(By.xpath("//span[@class='ui-datepicker-month']"));
			WebElement yearWE = driver.findElement(By.xpath("//span[@class='ui-datepicker-year']"));
			if (monthWE.getText().equals(month) && yearWE.getText().equals(year)) {
				strFlag = true;
				break;
			}
			WebElement PrevArrow = driver.findElement(By.xpath("//span[contains(text(),'Prev')]/.."));
			PrevArrow.click();
			sync(3);
		}
		WebElement Date = driver.findElement(By.xpath("(//td[@data-handler='selectDay']/a[contains(text(),'" + day + "')])[1]"));
		Date.click();
		sync(3);
	}

	public static String getData(String path, String sheetName, int rowNumber, int cellNumber) // Charanjeet
	{
		String data = null;
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			data = WorkbookFactory.create(fis).getSheet(sheetName).getRow(rowNumber).getCell(cellNumber)
					.getStringCellValue();

			if (data.isEmpty()) {
				data = "Data not Found okay.";
			}
		} catch (Exception e) {
			System.err.println("Error while getting Data.");
		}
		return data;
	}

	public void setData(String path, String sheetName, String dataToSet)
	{
		int cellNumber = 0;
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			Workbook wb = WorkbookFactory.create(fis);

			Sheet sh = wb.getSheet(sheetName);
			Row r = sh.createRow(rowNumber);
			Cell c = r.createCell(cellNumber);
			if (StringUtils.isNotBlank(dataToSet)) {
				c.setCellValue(dataToSet);
			}

			FileOutputStream fos = new FileOutputStream(new File(path));
			wb.write(fos);
			fos.flush();
			fos.close();
			rowNumber++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void putdata_Excel(String strfilename, String strSheetName, String SerialNO, String strColumnName,
			String strValue, String color, int rowNo) throws Exception {

		FileInputStream fis = null;
		try {

			// Create a FileInputStream that will be use to read the excel file.
			fis = new FileInputStream(strfilename);
			// Create an excel workbook from the file system.
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			// Get the first sheet on the workbook.
			HSSFSheet sheet = workbook.getSheet(strSheetName);

			jxl.Workbook jxlworkbook = jxl.Workbook.getWorkbook(new File(strfilename));
			jxl.Sheet jxlsheet = jxlworkbook.getSheet(strSheetName);

			for (int j = 1; j < jxlsheet.getRows(); j++) {
				String StrSiNO = jxlsheet.getCell(0, j).getContents();
				String intSiNo = StrSiNO.toString();
				if (intSiNo.equalsIgnoreCase(SerialNO)) {
					for (int x = 1; x < jxlsheet.getColumns(); x++) {
						if (jxlsheet.getCell(x, rowNo).getContents().equals(strColumnName)) {

							HSSFRow row = sheet.getRow(j);
							HSSFCell c = row.createCell(x);
							// CellStyle cellStyle = workbook.createCellStyle();
							CellStyle style = workbook.createCellStyle();
							c.setCellValue(strValue);

							// cellStyle.setFillBackgroundColor(bg);
							if (color.equals("RED")) {

								style.setFillForegroundColor(IndexedColors.RED.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								Font font = workbook.createFont();
								font.setColor(IndexedColors.BLACK.getIndex());
								style.setFont(font);
								c.setCellStyle(style);
							} else if (color.equals("GREEN")) {
								style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
								style.setFillPattern(CellStyle.SOLID_FOREGROUND);
								Font font = workbook.createFont();
								font.setColor(IndexedColors.BLACK.getIndex());
								style.setFont(font);
								c.setCellStyle(style);
							} else {

							}
							fis.close();
							FileOutputStream fos = new FileOutputStream(strfilename);
							workbook.write(fos);
							fos.close();
							break;
						}
					}
					break;
				}

				// testNames.add(sheet.getCell(arg0, arg1))

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			/*
			 * if (fis != null) { fis.close(); }
			 */
		}
		// HashMap<String, String> hMap = showExelData(sheetData, apiname);
		// return sheetData;
	}

	public static HSSFCell c = null;

	public static String getdata_Excel(String strfilename, String strSheetName, String SerialNO, String strColumnName,
			int rowNo) throws Exception {

		FileInputStream fis = null;
		try {

			// Create a FileInputStream that will be use to read the excel file.
			fis = new FileInputStream(strfilename);
			// Create an excel workbook from the file system.
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			// Get the first sheet on the workbook.
			HSSFSheet sheet = workbook.getSheet(strSheetName);

			jxl.Workbook jxlworkbook = jxl.Workbook.getWorkbook(new File(strfilename));
			jxl.Sheet jxlsheet = jxlworkbook.getSheet(strSheetName);

			for (int j = 1; j < jxlsheet.getRows(); j++) {
				String StrSiNO = jxlsheet.getCell(0, j).getContents();

				String intSiNo = StrSiNO.toString();
				if (intSiNo.equalsIgnoreCase(SerialNO)) {
					for (int x = 1; x < jxlsheet.getColumns(); x++) {
						if (jxlsheet.getCell(x, rowNo).getContents().equals(strColumnName)) {

							HSSFRow row = sheet.getRow(j);
							// HSSFCell c = row.createCell(x);
							c = row.getCell(x);
							// c.setCellValue(strValue);

							break;
						}
					}
					break;
					// }

					// testNames.add(sheet.getCell(arg0, arg1))

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return c.getStringCellValue();
	}

	private String pdfContent;
	private String filePath;
	private File file;
	private PDFParser parser;
	private PDFTextStripper pdfStripper;
	private PDDocument pdDoc;
	private COSDocument cosDoc;

	/**
	 * Author: 587382 Pre-Requiste: pom.xml should have the dependancy for the
	 * org.apache.pdfbox v2.0.X and above This function is used to get the PDF
	 * content into s String having the PDF content
	 * 
	 * @param filePath
	 *            : This denotes the entire filepath where PDF file is stored
	 * 
	 */
	public String getStringPDFContent(String filePath) {
		driver.get(filePath);
		sync(5);
		try {
			file = new File(filePath);
			parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			cosDoc = parser.getDocument();
			pdfStripper = new PDFTextStripper();
			pdDoc = PDDocument.load(new File(filePath));
			pdfStripper = new PDFTextStripper();
			pdfContent = pdfStripper.getText(pdDoc);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // update for PDFBox V 2.0
		driver.navigate().back();
		sync(2);
		return pdfContent;
	}

	/**
	 * @author :587382 This method will switch to the opened tab
	 * @param parentHandle
	 *            : Pass the parent Window handle from the application to switch
	 *            to other open tab
	 */
	public void switchToNewTab(String parentHandle) {

		for (String winHandle : driver.getWindowHandles()) {
			if (!winHandle.equals(parentHandle)) {
				driver.switchTo().window(winHandle);
				Reporting("Navigated to the Open Tab", Status.PASS);
			}
		}
	}

	/**
	 * @author: 587382 This method is to verify that the WebElement is displayed
	 *          or not based on the attribute content
	 * @param value
	 *            : pattern value used to identify element
	 * @param htmlTag
	 *            :Html Tag of element to be checked
	 * @param attribute
	 *            : Attribute of the html tag to be verfied
	 */
	public void validate_ElementDisplayed_ByAttribute(String value, String htmlTag, String attribute) {
		WebElement WE = null;
		try {
			WE = driver.findElement(By.xpath("//" + htmlTag + "[contains(" + attribute + "," + value + "')]"));
			WaitTool.waitForElementDisplayed(driver, WE, 18);
			if (WE.isDisplayed()) {
				Reporting("Element '" + value + "' is displayed as expected.", Status.DONE);
			}
		} catch (Exception e) {
			Reporting("Element '" + value + "' is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * @author: 587382 This method is to verify that BreadScrumb from any place
	 *          of the application
	 * @param String
	 *            nav[] : Array of the Link Texts to be verified in the bread
	 *            scrumbs
	 */
	public void checkBreadCrumbs(String nav[]) {
		for (int navcount = 0; navcount < nav.length; navcount++) {
			nav[navcount] = nav[navcount].trim().replaceAll("\\s", "");
		}

		for (int navBrdcmbCount = 0; navBrdcmbCount < navBreadcrumbs.size(); navBrdcmbCount++) {
			for (int navcount = 0; navcount < nav.length; navcount++) {
				String breadCrumbs = navBreadcrumbs.get(navBrdcmbCount).getText().trim().replaceAll("\\s", "");
				if (nav[navcount].equalsIgnoreCase(breadCrumbs)) {
					Reporting(breadCrumbs + " Link is present in the BreadCrumbs navigation links " + breadCrumbs,
							Status.PASS);
					break;
				} else if (navcount < nav.length - 1) {
				}
			}
		}
	}

	/**
	 * This method is to check if any page element Text is displayed or not
	 * 
	 * @author Siva
	 * @param webelement
	 * @param element
	 *            Text which needs to be validated
	 **/
	public void validate_ElementText(WebElement webElement, String elementDescription) {
		WaitTool.waitForElementDisplayed(driver, webElement, 21);
		try {
			if (webElement.getText().replace("\n", " ").contains(elementDescription))
				Reporting(elementDescription + " Text is displayed as expected.", Status.PASS);
		} catch (Exception e) {
			Reporting(elementDescription + " Text is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check if any page element Placeholder is displayed or
	 * not
	 * 
	 * @author Siva
	 * @param webelement
	 * @param Placeholder
	 *            Text which needs to be validated
	 **/
	public void validate_ElementPlaceHolder(WebElement webElement, String elementDescription) {
		WaitTool.waitForElementDisplayed(driver, webElement, 21);
		try {
			if (webElement.getAttribute("placeholder").contains(elementDescription))
				Reporting(elementDescription + " Placeholder is displayed as expected.", Status.PASS);
		} catch (Exception e) {
			Reporting(elementDescription + " Placeholder is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to get the integer value from a webElement
	 * 
	 * @author RoopKishore
	 * @param element
	 *            WebElement of the number
	 * @return Integer value of webElement
	 */
	public Integer convertToInteger(WebElement element) {
		Integer number = Integer.parseInt(element.getText());
		return number;
	}

	/**
	 * This method is to verify if a button is disabled based on style attribute
	 * 
	 * @author RoopKishore
	 * @param btn
	 *            WebElement of the button
	 * @return true if button is disabled
	 */
	public boolean validateButtonDisabled(WebElement btn) {
		String styleClass = btn.getAttribute("class");
		if (styleClass.contains("btn-disabled")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method is to validate if the list is sorted or not. Used in checking
	 * sorting property of data tables.
	 * 
	 * @param users
	 *            List of WebElements with details
	 * @return True if elements are sorted
	 */
	public boolean validateIfSorted(List<WebElement> users) {
		boolean sorted = true;
		int maxsize = 0;
		Iterator<WebElement> it = users.iterator();
		List<String> userNames = new ArrayList<String>();
		while (it.hasNext()) {
			if (maxsize < 7) {
				WebElement tempElement = it.next();
				userNames.add(tempElement.getText());
				maxsize++;
			} else {
				break;
			}

		}
		for (int i = 1; i < userNames.size(); i++) {
			if (userNames.get(i - 1).compareTo(userNames.get(i)) > 0) {
				sorted = false;
			}
		}

		return sorted;
	}

	/**
	 * Overloaded method to update report with description
	 * 
	 * @param element
	 *            Webelement without text in it
	 * @param description
	 *            Description of the type of element
	 */
	public void checkElementExistenance(WebElement element, String description) {
		try {
			WaitTool.waitForElementDisplayed(driver, element, 13);
			if (element.isDisplayed()) {
				Reporting("Element '" + description + "' is displayed as expected.", Status.DONE);
			}
		} catch (Exception e) {
			Reporting("Element '" + description + "' is not displayed as expected.", Status.FAIL);
		}
		sync(1);
	}

	public void uploadFile(String fileLocation) {
		try {
			// Setting clipboard with file location
			// setClipboardData(fileLocation);
			StringSelection stringSelection = new StringSelection(fileLocation);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

			// native key strokes for CTRL, V and ENTER keys
			Robot robot = new Robot();

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Reporting("Attachment is added succesfully", Status.DONE);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public void setFieldValue(WebElement element, String value, String description) {
		try {
			element.clear();
			sync(1);
			element.click();
			sync(1);
			element.sendKeys(value);
			sync(1);
			Reporting("Entered " + value + " for " + description + " Field", Status.DONE);
		} catch (Exception e) {
		}

	}

	public void accountDumpToTextFile(String content) {
		String datatablePath = frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src"
				+ Util.getFileSeparator() + "main" + Util.getFileSeparator() + "resources" + Util.getFileSeparator()
				+ "ApiRequests";
		String filePath = datatablePath + Util.getFileSeparator() + "AccountCreationDump.txt";
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			// String content = "xyz";
			fw = new FileWriter(filePath, true);
			bw = new BufferedWriter(fw);
			bw.write("\n");
			bw.write(content + Util.getCurrentTime() + ",");
			System.out.println("Account Dumped...");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	/**
	 * This method is to validate field presence and it's label based on id
	 * 
	 */
	public void validateFieldLabelsById(String[] elementId) {
		for (String listItem : elementId) {
			WebElement fieldLabel = driver.findElement(By.cssSelector("label[for='" + listItem + "']"));
			checkElementExistenance(fieldLabel);
		}
	}

	public String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = new Date();
		return (dateFormat.format(date));
	}

	public String getPreviousDate(int numberOfDays) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -numberOfDays);
		Date todate1 = cal.getTime();
		String previousDate = dateFormat.format(todate1);

		return previousDate;
	}

	public String getCurrentFileName(String downloadedPath, String currentFileName) {
		final File folder = new File(downloadedPath);
		//String strCurrentPath = downloadedPath + "\\" + currentFileName;
		List<File> list = new ArrayList<File>();
		for (final File fileSearch : folder.listFiles()) {
			if (fileSearch.getName().contains(currentFileName)) {
				list.add(fileSearch);
			}
		}

		final Map<File, Long> staticLastModifiedTimes = new HashMap<File, Long>();
		for (final File f : list) {
			staticLastModifiedTimes.put(f, f.lastModified());
		}

		Collections.sort(list, new Comparator<File>() {
			@Override
			public int compare(final File f1, final File f2) {
				return staticLastModifiedTimes.get(f2).compareTo(staticLastModifiedTimes.get(f1));
			}
		});

		String fileName = list.get(0).getName();

		return fileName;
	}

	public JsonObject getJsonFromString(String jsonstring) {
		try {
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(jsonstring).getAsJsonObject();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * Page scroll down to the WebElement functionality
	 * 
	 * @author Rahul
	 * 
	 * @param WebElements List
	 */
	public void slider(WebElement we) {
		sync();
		Point location = we.getLocation();
		int x = location.getX();
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0," + (x + 100) + ");");
		sync(3);

	}

	private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STRING_LENGTH = 10;
	/*
	 * @author: 587382 This method is used to generate the Random String It uses
	 * the getRandom()
	 */

	public String generateRandomString() {

		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}

	private int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	/**
	 * @author 587382 ( Adarsha T)
	 * @param alertText
	 *            : Pass the Alert Text to validate
	 * @return
	 */
	public SUPER_Page switchToAlert_AndVerifyContent(String alertText, boolean textVerify) {
		Alert alt = driver.switchTo().alert();
		WaitTool.isAlertPresent(driver, 10);
		if (textVerify) {
			if (alt.getText().contains(alertText)) {
				Reporting("Alert message displayed is " + alt.getText(), Status.PASS);
				alt.accept();
				Reporting("Closing the Alert Popup", Status.PASS);
			} else {
				Reporting("Alert message displayed is " + alt.getText(), Status.FAIL);
			}
		} else {
			Reporting("Alert message displayed is " + alt.getText(), Status.PASS);
			alt.accept();
			Reporting("Closing the Alert Popup", Status.PASS);
		}
		return this;
	}

	/**
	 * @author 587382 ( Adarsha T)
	 * @param alertText
	 *            : This method wait till the Page is loaded and elements of the
	 *            page are active to operate.
	 */
	public void waitForPageToBeReady() {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		// This loop will rotate for 100 times to check If page Is ready after
		// every 1 second.
		// You can replace your if you wants to Increase or decrease wait time.
		for (int i = 0; i < 400; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// To check page ready state.

			if (executor.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
		}
	}

	/**
	 * @author 587382 ( Adarsha T)
	 * @param alertText
	 *            : This method is used when only one Tab is opened
	 * @return
	 */
	public SUPER_Page switchToOpenedTabandVerifyURL(boolean urlVerify, boolean close, String navURL,
			boolean switchToPrevious) {
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
		if (urlVerify) {
			waitForPageToBeReady();
			if (driver.getCurrentUrl().contains(navURL))
				Reporting("Navigated to correct URL" + driver.getCurrentUrl(), Status.PASS);
			else
				Reporting("Navigated to wrong URL" + driver.getCurrentUrl(), Status.PASS);
		}
		if (close) {
			driver.close();
			Reporting("Closed the opened Tab", Status.PASS);
		}
		if (switchToPrevious) {
			driver.switchTo().window(tabs2.get(0));
			Reporting("User switched to previous the opened Tab", Status.PASS);
		}
		return this;
	}

	/**
	 * @author 587382 (Adarsha T)
	 * @category : Re-Usable method to verify whether the element is enabled or
	 *           Not.
	 * @param element
	 *            << Pass the Element which needs to verify
	 * @param description
	 *            << Describe Element
	 */
	public void validate_ElementEnabled(WebElement element, String description) {
		if (element.isEnabled())
			Reporting(description + " field is enabled ", Status.DONE);
		else
			Reporting(description + "field is disabled", Status.FAIL);
	}

	/**
	 * @author 587382 (Adarsha T)
	 * @category : Re-Usable method to Move Hover to the desired element where
	 *           Tool Tip Needs to be verified.
	 * @param element
	 *            << Pass the Element on hovering which Tool tip is visible
	 * @param description
	 *            << Describtion of the ELement /Tool Tip Name
	 */
	public void moveToHover(WebElement element, String description) {
		Actions act = new Actions(driver);
		act.moveToElement(element).build().perform();
		Reporting(description + " is displayed on Mouse Hover ", Status.DONE);
	}

	public void verifyLinkActive(String linkUrl) {
		try {
			URL url = new URL(linkUrl);

			HttpURLConnection httpURLConnect = (HttpURLConnection) url.openConnection();

			httpURLConnect.setConnectTimeout(5000);

			httpURLConnect.connect();

			if (httpURLConnect.getResponseCode() == 200) {
				Reporting(linkUrl + " - " + httpURLConnect.getResponseMessage(), Status.PASS);
			}
			if (httpURLConnect.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				Reporting(linkUrl + " - " + httpURLConnect.getResponseMessage() + " - "
						+ HttpURLConnection.HTTP_NOT_FOUND, Status.FAIL);
			}
		} catch (Exception e) {

		}
	}

	public void verifyBrokenLinks(String pageName) {
		// List<WebElement> allPageLNKS = driver.findElements(By.tagName("a"));
		List<WebElement> allPageLNKS = driver.findElements(By.tagName("img"));
		Reporting("Total Links in the" + pageName + "are >> " + allPageLNKS.size(), Status.DONE);
		for (WebElement link : allPageLNKS) {
			String url = link.getAttribute("href");
			verifyLinkActive(url);
		}
	}

	/**
	 * This method is to check page icon, basically class for image.
	 * 
	 * @author Raghavendra Venkat
	 * @param icon
	 *            Name to be validated
	 * @param Class
	 *            Name
	 * @param Underlying
	 *            html tag
	 **/
	public void validate_Icon(String iconName, String className, String htmlTag) {
		try {
			WebElement iconOBJ = driver
					.findElement(By.xpath("(//" + htmlTag + "[contains(@class,'" + className.trim() + "')])[1]"));
			WaitTool.waitForElementDisplayed(driver, iconOBJ, 18);
			scrollToElement(iconOBJ);
			if (iconOBJ.isDisplayed())
				Reporting(iconName + ", is displayed as expected.", Status.PASS);
		} catch (Exception e) {
			Reporting(iconName + ", is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check page icon, basically class for image.
	 * 
	 * @author Raghavendra Venkat
	 * @param icon
	 *            Name to be validated
	 * @param Class
	 *            Name
	 * @param Underlying
	 *            html tag
	 **/
	public void validate_stepNum(String stepText, String className, String htmlTag) {
		try {
			WebElement stepOBJ = driver
					.findElement(By.xpath("(//" + htmlTag + "[contains(@class,'" + className.trim() + "')])[1]"));
			WaitTool.waitForElementDisplayed(driver, stepOBJ, 18);
			scrollToElement(stepOBJ);
			if (stepOBJ.isDisplayed())
				if (stepOBJ.getText().equals(stepText)) {
					Reporting(stepText + ", is displayed as expected.", Status.PASS);
				} else {
					Reporting(stepText + ", is not displayed as expected.", Status.FAIL);
				}
		} catch (Exception e) {
			Reporting(stepText + ", is not displayed as expected.", Status.FAIL);
		}
	}

	/**
	 * This method is to check search of users with few characters in Account
	 * Contacts page
	 * 
	 * @author Raghavendra Venkat
	 * @param dropDownName
	 * @param textToFind
	 **/

	@FindBy(xpath = "//input[@placeholder='Search for User']")
	WebElement searchForUserTextbox;

	@FindBy(xpath = "//input[@placeholder='Select Contact Type...']")
	WebElement SelectContactTypeTextbox;
	
	@FindBy(id = "select2-userEmails-container")
	WebElement paperlessDropdown;
	
	@FindBy(id="contact-type")
	WebElement otherDropdown;
	
	@FindBy(id ="select2-authuser-2-container")
	WebElement authoriseUserDropdown;
		
	@FindBy(id ="select2-other-contact-container")
	WebElement otherUserDropdown;

	public void validate_letterSearch(String dropDownName, String value) {
		WebElement dropDownOBJ;
		String textToFind =value.substring(0, 3);
		try {
			if (dropDownName.equalsIgnoreCase("other")) {
				dropDownOBJ = otherDropdown;				
			}else if(dropDownName.equalsIgnoreCase("authuser")){
				dropDownOBJ = authoriseUserDropdown;
			}else if(dropDownName.equalsIgnoreCase("paperless")){
				dropDownOBJ = paperlessDropdown;
			}else if(dropDownName.equalsIgnoreCase("otherUserDropdown")){
				dropDownOBJ = otherUserDropdown;
			}
			else {
				dropDownOBJ = driver.findElement(By.id("select2-" + dropDownName.toLowerCase() + "-contact-container"));
			}
			WaitTool.waitForElementDisplayed(driver, dropDownOBJ, 18);
			scrollToElement(dropDownOBJ);
			if (dropDownOBJ.isDisplayed()){
				dropDownOBJ.click();
				Reporting(dropDownName + " is Clicked", Status.DONE);
			}
			if (!(dropDownName.equalsIgnoreCase("other"))) {
				searchForUserTextbox.sendKeys(textToFind);
				searchForUserTextbox.sendKeys(Keys.ENTER);
			} else {
				dropDownOBJ.click();
				sync(1);
				Select cntType = new Select(dropDownOBJ);
				cntType.selectByVisibleText(value);				
				}
					
			if(dropDownName.equalsIgnoreCase("Billing")){
			sync(15);
			WaitTool.waitForElementDisplayed(driver, dropDownOBJ, 25);
			scrollToElement(dropDownOBJ);
			}
					
				if (dropDownOBJ.getText().contains(textToFind)) {
					Reporting(dropDownName + ", dropdown searched "+ textToFind + " and was able to search with few letters.", Status.PASS);
				}else{
					Reporting(dropDownName + ", dropdown searched "+ textToFind + " and was not able to search with few letters", Status.FAIL);
				}
			
			
		} catch (Exception e) {
			
			Reporting(dropDownName + ", dropdown searched "+ textToFind + " and was not able to search with few letters.", Status.FAIL);
		}
	}
	
	@FindBy(id="select2-authuser-2-container")
	WebElement authorizationSettingsDropdown;

	public void validateUsersInDropdown(String dropDownName, String textToFind, String[] allUsers) {
		sync(8);
		WebElement dropDownOBJ;
		String page = "";
		if (dropDownName.equalsIgnoreCase("other")) {
			dropDownOBJ = otherDropdown;
			page = "Account Contacts";
		} else if (dropDownName.equalsIgnoreCase("authuser")) {
			dropDownOBJ = authoriseUserDropdown;
			page = "Account Authorization Settings";
		} else if (dropDownName.equalsIgnoreCase("otherUserDropdown")) {
			dropDownOBJ = otherUserDropdown;
		} else if (dropDownName.equalsIgnoreCase("authorizationSettings")) {
			dropDownOBJ = authorizationSettingsDropdown;
			page = "Account Authorization Settings";
		} else {
			dropDownOBJ = driver.findElement(By.id("select2-" + dropDownName.toLowerCase() + "-contact-container"));
			page = "Account Contacts";
		}

		WaitTool.waitForElementDisplayed(driver, dropDownOBJ, 18);
		scrollToElement(dropDownOBJ);
		dropDownOBJ.click();
		sync(1);
		Reporting(dropDownName + " is Clicked", Status.DONE);

		if (dropDownName.equalsIgnoreCase("Other")) {
			selectByText(otherDropdown, textToFind);
			sync(1);
			otherUserDropdown.click();
			sync(1);
			Reporting("Other User Dropdown is Clicked", Status.DONE);
		}

		try {
			for (int i = 0; i < allUsers.length; i++) {
				if (dropDownName.equalsIgnoreCase("authorizationSettings")) {
					driver.findElement(
							By.xpath("//ul[@id='select2-authuser-2-results']/li[text()='" + allUsers[i] + "']"));
				} else {
					if (dropDownName.equalsIgnoreCase("other")) {
						driver.findElement(By.xpath("//ul[contains(@id,'" + dropDownName.toLowerCase()
								+ "')]/li[text()='" + allUsers[i] + "']"));
					} else {
						driver.findElement(By.xpath("//ul[contains(@id,'" + dropDownName.toLowerCase()
								+ "')]/li[text()='" + allUsers[i] + "']"));
					}
				}
			}
			Reporting("All Users are present in " + page + " page dropdown.", Status.PASS);
		} catch (Exception e) {
			Reporting("All Users are not present in " + page + " page dropdown.", Status.FAIL);
		}
		if (dropDownName.equalsIgnoreCase("Other")) {
			otherUserDropdown.click();
			sync(1);
		} else {
			scrollToElement(dropDownOBJ);
			dropDownOBJ.click();
			sync(1);
		}
	}

	/**
	 * This method is to check search of users with few characters in Account
	 * Contacts page
	 * 
	 * @author Raghavendra Venkat
	 * @param dropDownName
	 * @param textToFind
	 **/

	@FindBy(id = "submitsearch")
	WebElement Submit_ViewAllUsers;
	@FindBy(xpath = "//table[@id='view-users-table']//td")
	WebElement noRecordsMsg;

	public void validate_UserSearch(String endUser,boolean singlesearch) {
		try {
			String textToFind = endUser.substring(0, 3);
			WaitTool.waitForElementDisplayed(driver, searchForUserTextbox, 18);
			validate_ElementDisplayed(searchForUserTextbox, "Search box in View all Users");
			searchForUserTextbox.clear();
			searchForUserTextbox.sendKeys(textToFind);
			Submit_ViewAllUsers.click();
			Reporting("Submit button in View all Users page is Clicked", Status.DONE);
			sync(9);
			
			List<WebElement> name = driver.findElements(By.xpath("//table[@id='view-users-table']//tr"));
			if(singlesearch && name.size()!=2){
				Reporting("Multiple Users Found with same data",Status.FAIL);
				return;
			}
			
			if(noRecordsMsg.getText().equals("No results found")){
				Reporting("No Results Found ",Status.DONE);
				return;
			}
			
			try{
				driver.findElement(By.xpath("//span[contains(text(),'"+endUser+"')]"));
				Reporting("View all Users text box for "+ endUser +" is able to search users with few letters.", Status.PASS);
				} catch(Exception e) {
					Reporting("View all Users text box for" +endUser+ " is not able to search users with few letters.", Status.FAIL);
				}	
		
		} catch (Exception e) {
			Reporting("View all Users text box is not able to search users with few letters.", Status.FAIL);
		}
		
		
	}
	
	/**
	 * This method is to Check pixel Spacing between header and error/success message
	 * 
	 * @author Raghavendra Venkat
	 * @param headerDiv
	 * @param messageDiv
	 **/

	public void validate_pixelSpace(WebElement headerDiv,WebElement messageDiv) {
		try {
			int header = headerDiv.getLocation().getY();
			int message = messageDiv.getLocation().getY();
			int diff = message - header;
			if(diff==20){
				Reporting("Spacing between header and error/success message is 20px", Status.PASS);
			}
			else{
				Reporting("Spacing between header and error/success message is "+diff, Status.FAIL);
			}
		}catch (Exception e){
			Reporting("Spacing between header and error/success message is not 20px", Status.FAIL);
		}
	}
	
	@FindBy(id = "header-logo")
	WebElement coxLogo;
	@FindBy(xpath = "//div[@id='onboardingHeader']//h3")
	WebElement myAccSetUp;
	@FindBy(xpath = "//div[@id='onboardingHeader']//p")
	WebElement welcomeName;
	
	public void validate_logo(){
		if(coxLogo.getLocation().toString().equals("(72, 16)")){
			Reporting("Cox Logo is alligned left", Status.PASS);
		}else{
			Reporting("Cox Logo is not alligned left", Status.FAIL);
		}
			
		if(myAccSetUp.getLocation().toString().equals("(211, 24)")){
			Reporting("My Account SetUp is alligned center", Status.PASS);
		}else{
			Reporting("My Account SetUp is not alligned center", Status.FAIL);
		}
		
		validate_ElementText(welcomeName, "Welcome, DATA!");
		
		if(welcomeName.getLocation().toString().equals("(1106, 28)")){
			Reporting("Welcome is alligned right", Status.PASS);
		}else{
			Reporting("Welcome is not alligned right", Status.FAIL);
		}
	}
	
	public void validate_header(){
		WaitTool.waitForElementDisplayed(driver, myAccountDropdown, 18);
		myAccountDropdown.click();
		validate_ElementDisplayed("Logout", "a");
		myAccountDropdown.click();
	}
	
	@FindBy(id="bhome-form:billpay-widget-content")
	WebElement billPayWidget;
	@FindBy(id="bhome-form:onlineticketing-widget-content")
	WebElement onlineTicketingWidget;
	@FindBy(id="bhome-form:mc-content")
	WebElement msgCenterWidget;
	@FindBy(id="bhome-form:services-products-content")
	WebElement servicesWidget;
	@FindBy(id="internet-tools-div")
	WebElement internetToolsWidget;
	@FindBy(id="internet-subscribed-services")
	WebElement internetSubscribedServices;
	@FindBy(id="bhome-form:support-product")
	WebElement supportWidget;
	@FindBy(id="my-services-h3")
	WebElement myServicesHeading;
		
	public void validate_widgets(){
		validate_ElementDisplayed(billPayWidget,"BillPay Widget");
		validate_ElementDisplayed(onlineTicketingWidget,"Online Ticketing Widget");
		validate_ElementDisplayed(msgCenterWidget,"Message Center Widget");
		scrollToElement(myServicesHeading);
		validate_ElementDisplayed(myServicesHeading, "My Services heading");
		validate_ElementDisplayed(servicesWidget,"Services Widget");
		String siteId = String.valueOf(dataTable.getData(env, "SiteId"));
		String accountNumber = dataTable.getData(env, "AccountNumber");
		validate_ElementDisplayed(siteId+"-"+accountNumber, "td");
		scrollToElement(internetToolsWidget);
		validate_ElementDisplayed(internetToolsWidget,"Internet Tools");
		validate_ElementDisplayed(internetSubscribedServices, "Internet Subscribed Services");
		scrollToElement(supportWidget);
		validate_ElementDisplayed(supportWidget,"Support Widget");
	}
	
	public boolean listIteration(List<WebElement> name){
		Iterator<WebElement> nameIt = name.iterator();
		while(nameIt.hasNext()){
			String userName = nameIt.next().getText();
			try{
			driver.findElement(By.xpath("(//table[@id='view-users-table-md']//span[text()='"+userName+"']//following::a[text()='Edit'])[1]"));
			}catch(Exception e){
				Reporting("Edit link is not present for" +userName,Status.FAIL);
				return false;
			}
		}
		return true;
	}
	
	public boolean clickable(WebDriver driver,WebElement element){
		try{
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		return true;
		}catch(Exception e){
			return false;
		}
	}
	/**
	 * This method is to get the hexcode
	 * 
	 * @author Chetan
	 * @param  WebElemet-needs to be converted to hex code
	 *            
	 **/
		
	public String convertColorToHEX(WebElement wb){
		String color = wb.getCssValue("color");
		String hexCode = Color.fromString(color).asHex();
		return hexCode;
	}
	
	/**
	 * This method is to validate URL 
	 * 
	 * @author Chetan
	 * @param  String-url to compare with current url
	 *            
	 **/
	
	public void compare_URL(String urlToCompare) {
		waitForPageToBeReady();
		String url = driver.getCurrentUrl();
		if (driver.getCurrentUrl().equals(urlToCompare.trim()))
			Reporting("URL is displaying as expected. ie <br/>"+url, Status.DONE);
		else
			Reporting("URL is not displaying as expected. ie <br/>"+url, Status.FAIL);
	}
}