package supportlibraries;

/**
 * Enumeration to represent the browser to be used for execution
 * @author Cognizant
 */
public enum Browser
{
	Android("android"),
	Chrome("chrome"),
	Firefox("firefox"),
	HtmlUnit("htmlunit"),
	InternetExplorer("internet explorer"),
	//iPhone("iPhone"),
	//iPad("iPad"),
	Opera("opera"),
	Safari("safari"), 
	MobileChrome("MobileChrome"), 
	MobileSafari("MobileSafari");
	
	private String value;
	
	Browser(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
}