package businesscomponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.lang.StringUtils;



import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class RestComponents extends ReusableLibrary{

	public RestComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	
	public String env = UtilTools.getEnv();
	
	public String RestGet(String URL,String Headers) throws HttpException, IOException {
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
	
	
	public String RestPut(String URL,String Headers,String ApiRequest) throws HttpException, IOException {
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
	
	public String RestPost(String URL,String Headers,String strApirequest) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//client.getParams().setAuthenticationPreemptive(true);
		String ApiRequest = strApirequest;	   
		//Enter Rest Url
		/*URI uri = new URI(URL, false);
		PostMethod method = new PostMethod(uri.getEscapedURI());
		*/
		PostMethod method = new PostMethod(URL);
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
	
	
	public String RestPost_Url(String URL,String Headers) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//client.getParams().setAuthenticationPreemptive(true);
		//String ApiRequest = strApirequest;	   
		//Enter Rest Url
		/*URI uri = new URI(URL, false);
		PostMethod method = new PostMethod(uri.getEscapedURI());
		*/
		PostMethod method = new PostMethod(URL);
		method.addRequestHeader("Accept", "application/xml");
		method.addRequestHeader("Content-Type", "application/xml");
//		method.setRequestEntity(new ByteArrayRequestEntity(ApiRequest.getBytes()));
		String[] Header = Headers.split(",");
		for(int i=0;i<Header.length;i++){
			String[] HeaderDetails = Header[i].split(":");
			method.addRequestHeader(HeaderDetails[0], HeaderDetails[1]);
		}
		method.setDoAuthentication(true);
			  
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
	public String RestPost_json(String URL,String Headers,String strApirequest) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		//client.getParams().setAuthenticationPreemptive(true);
		String ApiRequest = strApirequest;	   
		//Enter Rest Url
		/*URI uri = new URI(URL, false);
		PostMethod method = new PostMethod(uri.getEscapedURI());
		*/
		System.out.println("POST Request: "+ApiRequest);
		PostMethod method = new PostMethod(URL);
		method.addRequestHeader("Accept", "application/json");
		method.addRequestHeader("Content-Type", "application/json");
		method.setRequestEntity(new ByteArrayRequestEntity(ApiRequest.getBytes()));
		try {
			String[] Header = Headers.split(",");
			for(int i=0;i<Header.length;i++){
				String[] HeaderDetails = Header[i].split(":");
				method.addRequestHeader(HeaderDetails[0], HeaderDetails[1]);
			}			
		} catch (Exception e) {
			// TODO: handle exception
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
	
	
	public String getTagValueFromResponse(String response,String tagName){
		String tagValue="";
		tagValue = StringUtils.substringBetween(response, "<"+tagName+">","</"+tagName+">");
		return tagValue;		
	}
	
	public void addAccountsToMainAccount() throws Exception{
		
		String TestDataExcel = "C:/Cox_Automation/Test_Data.xls";
		
		String MainAccountEmailID = dataTable.getData(env, "MainAccountEmailID");
		
		String GetUserTokenUrl = "https://cbmaservices.train.cox.com/cbmaservices/services/rest/1.0//user/token/"+MainAccountEmailID;
		String GetUserTokenHeaders = "ma_transaction_id:1234,CoxAccess:Basic ZXFhdXNlcjplcWFwYXNzd29yZA==";
		String GetUserTokenresponse = RestGet(GetUserTokenUrl,GetUserTokenHeaders);
		String UsertokenValue = getTagValueFromResponse(GetUserTokenresponse,"encryptedValue");
		
		String AddAccountUrl = "https://cbmaservices.train.cox.com/cbmaservices/services/rest/1.0//account";
		String strAddAccountHeaders = "ma_transaction_id:1234,CoxAccess:Basic ZXFhdXNlcjplcWFwYXNzd29yZA==,ma_user_token:"+UsertokenValue;
		
		List<List<Cell>> AccountNumbers = new ArrayList<List<Cell>>();		
		AccountNumbers = readExcel_Prerequisite(TestDataExcel,"Test_Data");
		
		int NumberOfAccounts = Integer.parseInt(dataTable.getData(env, "NumberOfAccounts"));
		String SiteID = dataTable.getData(env, "SiteId");
		String CompanyDivId = dataTable.getData(env, "CompanyDivId");
		
		for (int i = 1; i <= NumberOfAccounts; i++){
			List<Cell> list = (List<Cell>) AccountNumbers.get(i);
			String AccountNumber = list.get(1).toString();
			String strrequestAddAccount = "<addAccount><accountNumber16>"+Integer.parseInt(SiteID)+Integer.parseInt(CompanyDivId)+Integer.parseInt(AccountNumber)+"</accountNumber16><alias>"+Integer.parseInt(AccountNumber)+"</alias><pinNumber>1212</pinNumber><contactList><contact><firstName>marry</firstName><lastName>test</lastName><phoneNumber>9972311111</phoneNumber><email>test.tech@cox.com</email><accountContactType>TECHNICAL</accountContactType></contact><contact><firstName>jolie</firstName><lastName>nair</lastName><phoneNumber>9972311111</phoneNumber><email>jolie.sales@cox.com</email><accountContactType>SALES</accountContactType></contact><contact><firstName>alli</firstName><lastName>trim</lastName><phoneNumber>9972311111</phoneNumber><email>allie.marketing@cox.com</email><accountContactType>MARKETING</accountContactType></contact></contactList></addAccount>";
			String AddAccountresponse = RestPut(AddAccountUrl,strAddAccountHeaders,strrequestAddAccount);
			String ResponseErrorMessage = getTagValueFromResponse(AddAccountresponse,"errorMessage");
			System.out.println(ResponseErrorMessage);
		}
	}
	
	public static List<List<Cell>> readExcel_Prerequisite(String strFileNamePath,String strSheetName)
			throws Exception {
				
		List<List<Cell>> sheetData = new ArrayList<List<Cell>>();
		FileInputStream fis = null;
		try {
			
			// Create a FileInputStream that will be use to read the excel file.
			fis = new FileInputStream(strFileNamePath);
			// Create an excel workbook from the file system.
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			// Get the first sheet on the workbook.
			HSSFSheet sheet = workbook.getSheet(strSheetName);
			// Iterating through the sheet's rows and on each row's cells. We
			// store the data read in an ArrayList
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				Iterator<Cell> cells = row.cellIterator();
				List<Cell> data = new ArrayList<Cell>();
				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();
					data.add(cell);
				}
				sheetData.add(data);
				//workbook.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		
		return sheetData;
		
	}

}
