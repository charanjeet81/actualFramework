package supportlibraries;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.WebClient;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.internal.ElementScrollBehavior;

//import com.opera.core.systems.OperaDriver;

import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.*;

import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;

/**
 * Factory class for creating the {@link WebDriver} object as required
 * 
 * @author Cognizant
 */
public class WebDriverFactory {
	private static Properties properties;

	private WebDriverFactory() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the appropriate {@link WebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @return The corresponding {@link WebDriver} object
	 * @throws MalformedURLException 
	 */
	public static WebDriver getDriver(Browser browser) {
		WebDriver driver = null;
		properties = Settings.getInstance();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));
		DesiredCapabilities capabilities = new DesiredCapabilities();
		 
		switch (browser) {
		case Android:
			capabilities.setCapability("app", "Chrome");
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
			// capabilities.setCapability("automationName","Selendroid");
			capabilities.setCapability("deviceName", "SM-G920F");
			capabilities.setCapability("automationName", "Appium");
			capabilities.setCapability("platformVersion", "5.1.1");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("app-wait-activity", "activity-to-wait-for");
			// capabilities.setCapability("autoWebview", true);
			// capabilities.setCapability("app-Package", "com.cox.CXConnect");
			// capabilities.setCapability("app-Activity", ".CXConnect");
			// capabilities.setCapability("noReset","True");
			// capabilities.setCapability("udid","989fb005");
			// capabilities.setCapability("automationName",
			// "C:\\Users\\450247\\Downloads\\selenium-server-standalone-2.42.2");
			try {
				driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;		

		case Chrome:
			// Takes the system proxy settings automatically
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\" + properties.getProperty("ChromeDriverPath"));
			String[] switches = { "--ignore-certificate-errors","disable-popup-blocking",};
			ChromeOptions options = new ChromeOptions();
			options.addArguments("no-sandbox");
			options.addArguments("test-type");
			options.addArguments("disable-popup-blocking");
			options.addArguments("chrome.switches","--disable-extensions");
			options.addArguments("disable-infobars");
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", prefs);
			DesiredCapabilities dc = DesiredCapabilities.chrome();
			dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			dc.setCapability("chrome.switches", Arrays.asList(switches));
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(dc);
			//driver.manage().timeouts().pageLoadTimeout(59, TimeUnit.SECONDS);
			break;
			
		case MobileChrome:
			// Takes the system proxy settings automatically
			
			System.setProperty("webdriver.chrome.driver",
			System.getProperty("user.dir") + "\\" + properties.getProperty("ChromeDriverPath"));
			String[] switches1 = { "--ignore-certificate-errors" };
			ChromeOptions optionsmob = new ChromeOptions();
			optionsmob.addArguments("no-sandbox");
			optionsmob.addArguments("test-type");
			optionsmob.addArguments("disable-popup-blocking");
			optionsmob.addArguments("chrome.switches","--disable-extensions");
			optionsmob.addArguments("disable-infobars");
			DesiredCapabilities dc1 = DesiredCapabilities.chrome();
			dc1.setCapability("chrome.switches", Arrays.asList(switches1));
			Map<String, String> mobileEmulation = new HashMap<String, String>();
			mobileEmulation.put("deviceName", properties.getProperty("DeviceName"));
			Map<String, Object> chromeOptions = new HashMap<String, Object>();
			chromeOptions.put("mobileEmulation", mobileEmulation);
			dc1.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			driver = new ChromeDriver(dc1);
			break;

		case Firefox:
			// Takes the system proxy settings automatically
			try {
				ProfilesIni profile = new ProfilesIni();
				FirefoxProfile ffprofile = profile.getProfile("selenium");
				//ffprofile.setPreference("webdriver.load.strategy", "fast");
				//ffprofile.setPreference("webdriver.load.strategy", "unstable");
				driver = new FirefoxDriver(ffprofile);
				//driver.manage().timeouts().pageLoadTimeout(59, TimeUnit.SECONDS);
				//driver = new FirefoxDriver();
			} catch (WebDriverException wdEx) {
				if (wdEx.getMessage().contains("Cannot find firefox binary in PATH"))
				{
					FirefoxBinary ffBin = new FirefoxBinary(new File(System.getProperty("user.dir") + "\\" + properties.getProperty("FirefoxBinary")));
					FirefoxProfile ffProf = new FirefoxProfile();
					driver = new FirefoxDriver(ffBin, ffProf);
					driver = new FirefoxDriver();
					//driver.manage().timeouts().pageLoadTimeout(59, TimeUnit.SECONDS);
				} else
					throw new FrameworkException("Unable to open firefox driver", wdEx.getLocalizedMessage());
			}
			break;

		case HtmlUnit:
			// Does not take the system proxy settings automatically!

			driver = new HtmlUnitDriver();
			if (proxyRequired) {
				boolean proxyAuthenticationRequired = Boolean
						.parseBoolean(properties.getProperty("ProxyAuthenticationRequired"));
				if (proxyAuthenticationRequired) {
					// NTLM authentication for proxy supported

					driver = new HtmlUnitDriver() {
						@Override
						protected WebClient modifyWebClient(WebClient client) {
							DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
							credentialsProvider.addNTLMCredentials(properties.getProperty("Username"),
									properties.getProperty("Password"), properties.getProperty("ProxyHost"),
									Integer.parseInt(properties.getProperty("ProxyPort")), "",
									properties.getProperty("Domain"));
							client.setCredentialsProvider(credentialsProvider);
							return client;
						}
					};
				}
				((HtmlUnitDriver) driver).setProxy(properties.getProperty("ProxyHost"),
						Integer.parseInt(properties.getProperty("ProxyPort")));
			}

			break;

		case InternetExplorer:
			// Takes the system proxy settings automatically

			properties = Settings.getInstance();
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\" + properties.getProperty("InternetExplorerDriverPath"));
			//System.setProperty("webdriver.ie.driver","D:\\Selenium32\\IEDriverServer.exe");
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("nativeEvents", false);
			capabilities.setCapability("unexpectedAlertBehaviour", "accept");
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("disable-popup-blocking", true);
			capabilities.setCapability("enablePersistentHover", true);
			capabilities.setCapability("RequireWindowFocus", true);
			DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
			driver = new InternetExplorerDriver(capabilities);
			break;			

		/*
		 * case Opera: // Does not take the system proxy settings automatically!
		 * // NTLM authentication for proxy NOT supported
		 * 
		 * if (proxyRequired) { DesiredCapabilities desiredCapabilities =
		 * getProxyCapabilities(); driver = new
		 * OperaDriver(desiredCapabilities); } else { driver = new
		 * OperaDriver(); }
		 * 
		 * break;
		 */

		case Safari:
			// Takes the system proxy settings automatically
			driver = new SafariDriver();
			break;

		case MobileSafari:
			// Takes the system proxy settings automatically
			DesiredCapabilities MScapabilities = new DesiredCapabilities();
			MScapabilities.setCapability("platformName", "iOS");
			MScapabilities.setCapability("platformVersion", "8.4");
			MScapabilities.setCapability("deviceName", "5f5cb5959a8c6c143533fd2348ae9bc2182ee6c3");
			MScapabilities.setCapability("browserName", "safari");
			//MScapabilities.setCapability("automationName", "Appium");
			try {
				driver = new IOSDriver(new URL("http://10.141.22.51:4723/wd/hub"), MScapabilities);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			/*System.setProperty("webdriver.chrome.driver",
			System.getProperty("user.dir") + "\\" + properties.getProperty("ChromeDriverPath"));
			String[] switches1 = { "--ignore-certificate-errors" };
			DesiredCapabilities dc1 = DesiredCapabilities.chrome();
			dc1.setCapability("chrome.switches", Arrays.asList(switches1));
			Map<String, String> mobileEmulation = new HashMap<String, String>();
			mobileEmulation.put("deviceName", properties.getProperty("DeviceName"));
			Map<String, Object> chromeOptions = new HashMap<String, Object>();
			chromeOptions.put("mobileEmulation", mobileEmulation);
			// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			dc1.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			driver = new ChromeDriver(dc1);*/

			// driver = new ChromeDriver(dc);
			break;
		default:
			throw new FrameworkException("Unhandled browser!");
		}
		if(browser.getValue().contains("android") || browser.getValue().contains("MobileChrome") || browser.getValue().contains("MobileSafari"))
		{
			properties.setProperty("MobileExecution", "Y");
		}
		else
		{
			properties.setProperty("MobileExecution", "N");
			//driver.manage().deleteAllCookies();
			//driver.manage().window().setSize(new Dimension(1366, 768)); 
			//driver.manage().window().setPosition(new Point(0, 0));
			//driver.manage().window().setSize(new Dimension(1920, 1080));
			//driver.manage().window().maximize();			
		}
		return driver;
	}

	private static DesiredCapabilities getProxyCapabilities() {
		Proxy proxy = new Proxy();
		proxy.setProxyType(ProxyType.MANUAL);

		properties = Settings.getInstance();
		String proxyUrl = properties.getProperty("ProxyHost") + ":" + properties.getProperty("ProxyPort");

		proxy.setHttpProxy(proxyUrl);
		proxy.setFtpProxy(proxyUrl);
		proxy.setSslProxy(proxyUrl);
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
		desiredCapabilities.setCapability(CapabilityType.PROXY, proxy);

		return desiredCapabilities;
	}

	/**
	 * Function to return the appropriate {@link WebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link WebDriver} object
	 */
	public static WebDriver getDriver(Browser browser, String remoteUrl) {
		return getDriver(browser, null, null, remoteUrl);
	}

	/**
	 * Function to return the appropriate {@link WebDriver} object based on the
	 * parameters passed
	 * 
	 * @param browser
	 *            The {@link Browser} to be used for the test execution
	 * @param browserVersion
	 *            The browser version to be used for the test execution
	 * @param platform
	 *            The {@link Platform} to be used for the test execution
	 * @param remoteUrl
	 *            The URL of the remote machine to be used for the test
	 *            execution
	 * @return The corresponding {@link WebDriver} object
	 */
	/*public static WebDriver getDriver(Browser browser, String browserVersion, Platform platform, String remoteUrl) {
		// For running RemoteWebDriver tests in Chrome and IE:
		// The ChromeDriver and IEDriver executables needs to be in the PATH of
		// the remote machine
		// To set the executable path manually, use:
		// java -Dwebdriver.chrome.driver=/path/to/driver -jar
		// selenium-server-standalone.jar
		// java -Dwebdriver.ie.driver=/path/to/driver -jar
		// selenium-server-standalone.jar

		properties = Settings.getInstance();
		boolean proxyRequired = Boolean.parseBoolean(properties.getProperty("ProxyRequired"));

		DesiredCapabilities desiredCapabilities = null;
		if ((browser.equals(Browser.HtmlUnit) || browser.equals(Browser.Opera)) && proxyRequired) {
			desiredCapabilities = getProxyCapabilities();
		} else {
			desiredCapabilities = new DesiredCapabilities();
		}

		desiredCapabilities.setBrowserName(browser.getValue());

		if (browserVersion != null) {
			desiredCapabilities.setVersion(browserVersion);
		}
		if (platform != null) {
			desiredCapabilities.setPlatform(platform);
		}

		desiredCapabilities.setJavascriptEnabled(true); // Pre-requisite for
														// remote execution

		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is malformed");
		}

		return new RemoteWebDriver(url, desiredCapabilities);
	}*/
	
	public static WebDriver getDriver(Browser browser, String browserVersion,
			Platform platform, String remoteUrl)
	{
	   WebDriver driver = null;
		 DesiredCapabilities desireCapabilities;
		System.out.println(browser+"===="+browserVersion+"===="+remoteUrl);
		
		switch (browser) {

		case Chrome:
			String[] switches = { "--ignore-certificate-errors","disable-popup-blocking",};
			desireCapabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions(); 
			options.addArguments("--start-maximized"); 
			options.addArguments("no-sandbox");
			options.addArguments("test-type");
			options.addArguments("disable-popup-blocking");
			options.addArguments("chrome.switches","--disable-extensions");
			options.addArguments("disable-infobars");
			desireCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
			Map<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", prefs);
			desireCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			desireCapabilities.setCapability("chrome.switches", Arrays.asList(switches));
			desireCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
			
			break;
			
		case InternetExplorer:
			desireCapabilities =  DesiredCapabilities.internetExplorer();
			desireCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true); 
			desireCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			desireCapabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true); 
			desireCapabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			desireCapabilities.setJavascriptEnabled(true); 
			desireCapabilities.setCapability("requireWindowFocus", true);
			desireCapabilities.setCapability("ignoreProtectedModeSettings", true);
			desireCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			desireCapabilities.setCapability("nativeEvents", false);
			desireCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
			desireCapabilities.setCapability("disable-popup-blocking", true);
			desireCapabilities.setCapability("enablePersistentHover", true);
			break;
			
		case Firefox:
			desireCapabilities =  DesiredCapabilities.firefox();
			//desireCapabilities.setCapability("marionette", false);
			break;

		default:
			desireCapabilities = DesiredCapabilities.firefox();
		}
		URL url;
		try {
			url = new URL(remoteUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new FrameworkException("The specified remote URL is malformed");
		}
		driver=new RemoteWebDriver(url, desireCapabilities);
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		driver.manage().deleteAllCookies();
//		driver.manage().window().setPosition(new Point(0, 0));
//		driver.manage().window().setSize(new Dimension(1920, 1080));
//		driver.manage().window().maximize();	
		return driver;
	}

	

}