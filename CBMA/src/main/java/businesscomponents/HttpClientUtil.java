package businesscomponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HttpClientUtil {
	
	public static String RestGet(String URL,String Headers) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//client.getParams().setAuthenticationPreemptive(true);
			   
		//Enter Rest Url
		URI uri = new URI(URL, false);
		GetMethod method = new GetMethod(uri.getEscapedURI());
		
		String[] Header = Headers.split(",");
		for(int i=0;i<Header.length;i++){
			String[] HeaderDetails = Header[i].split(":");
			method.addRequestHeader(HeaderDetails[0], HeaderDetails[1]);
		}
		
		//method.setDoAuthentication(true);
			  
		//Get the Status number
		int status = client.executeMethod(method);
		System.out.println("Status:" + status);
			  
		//Get the response and print it in console
		BufferedReader rd = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		String response = "";
		String response1 = "";
		if ((response=rd.readLine())!=null) {
			System.out.println(response);
			response1=response;
		}
		method.releaseConnection();
		return response1;
	}
	
	
	public static String RestPut(String URL,String Headers,String ApiRequest) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//client.getParams().setAuthenticationPreemptive(true);
		//String ApiRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><addTicket><accountAddressLine1>APT 26</accountAddressLine1><accountCity>OKLAHOMA CITY</accountCity><accountAlias>217836701</accountAlias><accountName>test cbot agile</accountName><accountNumber12>131217836701</accountNumber12><accountState>OK</accountState><accountVipCode>K</accountVipCode><accountZip>73107</accountZip><allServicesImpacted>true</allServicesImpacted><alternateContactList><alternateContact><activeContact>true</activeContact><alternatePhoneNumber>1234567890</alternatePhoneNumber><alternatePhoneNumberExtension>1234</alternatePhoneNumberExtension><bestTimeToCallType>MORNING</bestTimeToCallType><displayName>Alternate Contact Name1</displayName><email>acontact1@mailinator.com</email><phoneNumber>1234567890</phoneNumber><phoneNumberExtension>1234</phoneNumberExtension><preferredContactMethodType>BOTH</preferredContactMethodType></alternateContact></alternateContactList><billingMonth>JANUARY</billingMonth><custRefNumber>987654321</custRefNumber><equipmentIds>1234,5678,6543</equipmentIds><mostRecentBillIssue>false</mostRecentBillIssue><preferredContact><activeContact>true</activeContact><alternatePhoneNumber>1234567890</alternatePhoneNumber><alternatePhoneNumberExtension>1234</alternatePhoneNumberExtension><bestTimeToCallType>MORNING</bestTimeToCallType><displayName>Preferred Contact Name</displayName><email>preferred@mailinator.com</email><phoneNumber>1234567890</phoneNumber><phoneNumberExtension>1234</phoneNumberExtension><preferredContactMethodType>BOTH</preferredContactMethodType></preferredContact><questionAndAnswerList><questionAndAnswer><answerList>answer1</answerList><answerList>answer2</answerList><answerList>answer3</answerList><question>testing question and answer?</question></questionAndAnswer></questionAndAnswerList><serviceCategoryList><serviceCategory><name>Phone</name><serviceIssueList><serviceIssue><name>Unable to Receive Calls</name></serviceIssue></serviceIssueList></serviceCategory></serviceCategoryList><ticketType>BILLING_AND_PAYMENT</ticketType></addTicket>";	   
		//Enter Rest Url
		URI uri = new URI(URL, false);
		PutMethod method = new PutMethod(uri.getEscapedURI());
		
		method.addRequestHeader("Accept", "application/xml");
		method.addRequestHeader("Content-Type", "application/xml");
		method.setRequestEntity(new ByteArrayRequestEntity(ApiRequest.getBytes()));
		String[] Header = Headers.split(",");
		for(int i=0;i<Header.length;i++){
			String[] HeaderDetails = Header[i].split(":");
			method.addRequestHeader(HeaderDetails[0], HeaderDetails[1]);
		}
		//method.setDoAuthentication(true);
			  
		//Get the Status number
		int status = client.executeMethod(method);
		System.out.println("Status:" + status);
			  
		//Get the response and print it in console
		BufferedReader rd = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		String response = "";
		String response1 = "";
		if ((response=rd.readLine())!=null) {
			System.out.println(response);
			response1=response;
		}
		method.releaseConnection();
		return response1;
	}
	
	public static String RestPost(String URL,String Headers,String strApirequest) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//client.getParams().setAuthenticationPreemptive(true);
		String ApiRequest = strApirequest;	   
		//Enter Rest Url
		/*URI uri = new URI(URL, false);
		PostMethod method = new PostMethod(uri.getEscapedURI());
		*/
		PostMethod method = new PostMethod(URL);
		method.addRequestHeader("Accept", "application/json");
		method.addRequestHeader("Content-Type", "application/json");
		method.setRequestEntity(new ByteArrayRequestEntity(ApiRequest.getBytes()));
		String[] Header = Headers.split(",");
		/*for(int i=0;i<Header.length;i++){
			String[] HeaderDetails = Header[i].split(":");
			method.addRequestHeader(HeaderDetails[0], HeaderDetails[1]);
		}*/
		//method.setDoAuthentication(true);			  
		//Get the Status number
		int status = client.executeMethod(method);
		System.out.println("Status:" + status);
			  
		//Get the response and print it in console
		BufferedReader rd = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
		String response = "";
		String response1 = "";
		if ((response=rd.readLine())!=null) {
			System.out.println(response);
			response1=response;
		}
		method.releaseConnection();
		return response1;
	}	
	
	public static String getTagValueFromResponse(String response,String tagName)
	{
		String tagValue="";
		tagValue = StringUtils.substringBetween(response, "<"+tagName+">","</"+tagName+">");
		return tagValue;		
	}
	
	public static String getTagValueFromResponse_JSON(String response, String tagName) 
	{
		String[] responseData = response.split(",");
		String tagValue = null;
		for (int i = 0; i < responseData.length; i++) 
		{
			if (responseData[i].contains(tagName)) 
			{
				String tagValues = StringUtils.substringAfter(responseData[i], ":\"");
				tagValue = tagValues.replace("\"", "");
			}
		}
		return tagValue;
	}
	
	public static String getSubTagValuesFromResponse(String strresponse, String strMaintagName, String strSubTagNames, String strExpOccurence)
	{
		String[] arrtagValue;
		String strSubTagValues = null;
		String[] arrSubTagNames = strSubTagNames.split(";");
		String strTempTagValue = "";
		arrtagValue = StringUtils.substringsBetween(strresponse, "<" + strMaintagName + ">","</" + strMaintagName + ">");
		for(int intOuterLoopcounter = 0; intOuterLoopcounter<arrtagValue.length; intOuterLoopcounter++)
		{
			strTempTagValue = StringUtils.substringBetween(arrtagValue[intOuterLoopcounter], "<" + strExpOccurence.split(":")[0] + ">","</" + strExpOccurence.split(":")[0] + ">");
			if(strTempTagValue.equals(strExpOccurence.split(":")[1]))
			{
				for(int intLoopcounter = 0; intLoopcounter<arrSubTagNames.length; intLoopcounter++)
				{
					String strTempValue = StringUtils.substringBetween(arrtagValue[intOuterLoopcounter], "<" + arrSubTagNames[intLoopcounter] + ">","</" + arrSubTagNames[intLoopcounter] + ">");
					if(strSubTagValues == null)
					{
						strSubTagValues = strTempValue;
					}
					else
					{
						strSubTagValues = strSubTagValues + ";" + strTempValue;
					}
				}
				break;
			}		
		}
		return strSubTagValues;		
	}
	
	public static String getSubTagValuesFromResponseString(String strresponse, String strMaintagName, String strSubTagNames) throws ParserConfigurationException, SAXException, IOException
	{
		//String strresponse;
		String strSubTagValues = null;
		String[] arrSubTagNames = strSubTagNames.split(";");
		//String strTempTagValue = "";
		//arrtagValue = StringUtils.substringsBetween(strresponse, "<" + strMaintagName + ">","</" + strMaintagName + ">");
		    strresponse=strresponse.substring(56);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(strresponse));
			Document doc = builder.parse(src);
			for(int intOuterLoopcounter = 0; intOuterLoopcounter<strresponse.length(); intOuterLoopcounter++)
			{
			String age = doc.getElementsByTagName(strMaintagName).item(intOuterLoopcounter).getTextContent();
			if(age.startsWith("true")){
				String name = doc.getElementsByTagName(arrSubTagNames[0]).item(0).getTextContent();
				String em=doc.getElementsByTagName(arrSubTagNames[1]).item(0).getTextContent();
				if(em.contains("true")&&name.contains("4047860317")){
					System.out.println("PASS");
				}
				else
				{
					System.out.println("FAIL");
				}
			}
			else
				System.out.println("emcEnabled");
			}
			
			
		
		return strSubTagValues;		
	}
	
}
