package businesscomponents;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Status;




public class SoapUIComponents extends ReusableLibrary {
	
	private final FrameworkParameters frameworkParameters =	FrameworkParameters.getInstance();
	private final String workspace = frameworkParameters.getRelativePath();	
	
	private String filename = workspace + "/src/main/resources/DataTables/SoapUI.xls";
	
	private String TestDataExcel = "C:/Cox_Automation/Test_Data.xls";
	
	public String env = UtilTools.getEnv();
		
	public SoapUIComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public SOAPMessage soapUI(String Api) throws Exception 
	{		
		// Create SOAP Connection
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();

		// Get API Url
		HashMap<String, String> hmap = readExcel(Api);
		String url = hmap.get("url").toString();
		
		// Get API request from the text file
		String request = GetAPIRequest(Api);

		// Create Soap Request and get the response
		SOAPMessage soapReq = createSOAPRequest(request, Api);
		SOAPMessage soapResponse = soapConnection.call(soapReq, url);
		if (soapResponse.getSOAPBody().getFault() != null) 
		{
			Reporting("Response: <font color = 'red'>" + getSOAPResponseString(soapResponse)+"</font>", Status.DONE);
			//driver.close();			
		}

		// Print the SOAP Response
		System.out.print("Response SOAP Message = ");
		soapResponse.writeTo(System.out);
		System.out.println();
		soapConnection.close();

		System.out.println(soapResponse.toString());

		// return response in the SOAPMessage format
		return soapResponse;
	}

	// Create Soap request
	private SOAPMessage createSOAPRequest(String request, String Api) throws Exception
	{ 	// Get Api Mime Headers from the sheet
		HashMap<String, String> hmap = readExcel(Api);
		String MimeHeaders = hmap.get("MimeHeaders").toString();
		String HeaderUrl = MimeHeaders;		
		
		// Create soapMessage
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		InputStream is = new ByteArrayInputStream(request.getBytes());
		soapMessage = MessageFactory.newInstance().createMessage(null, is);

		// Add MimeHeader to the Soap Action
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("SOAPAction", HeaderUrl);
		
		// Print the request Soap Message
		System.out.println("Request SOAP Message = ");
		soapMessage.writeTo(System.out);
		System.out.println();

		// return soapMessage
		return soapMessage;
	}

	public String GetAPIRequest(String Api) throws FileNotFoundException 
	{
		String SiteID = dataTable.getData(env, "SiteId");
		//String WorkSpace = frameworkParameters.getRelativePath();
		String Apifilename = workspace + "/src/main/resources/ApiRequests/" + SiteID +"/"+Api + ".txt";
				
		String strrequest = null;
		StringBuffer request = new StringBuffer();
		
		BufferedReader reader = new BufferedReader(new FileReader(Apifilename));

		try {
			// as long as there are lines in the file, print them
			while (true) {
				String line = reader.readLine();
				if (line != null) {
					request.append(line);
				} else {
					break;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		String TodaysDate = dateFormat.format(cal.getTime());
		String Timeslot = TodaysDate + "T00:00:00+05:30";
		//String ICOMS_TimeSlot = dataTable.getData(env,"ICOMS_TimeSlot");

		switch (Api) {
		case "CreateHouseRecordInput":
			int aptNo = (int )(Math.random() * 100000 + 1);
			String createHouseRecord = StringUtils.replace(request.toString(), "$aptNum$", String.valueOf(aptNo));
			strrequest = StringUtils.replace(createHouseRecord, "$SiteID$", SiteID);
			strrequest = StringUtils.replace(strrequest, "$node$", env);
			break;
			
		case "OpenSession":
			String OpenSession1 = StringUtils.replace(request.toString(), "$StreetID$", dataTable.getData(env, "StreetID"));
			strrequest = StringUtils.replace(OpenSession1, "$ZipCode$", dataTable.getData(env, "ZIP"));
			
			break;
			
		case "AddCustomer":
			String HouseNumber = dataTable.getData(env, "HouseNumber");
			//String HouseNumber = properties.getProperty("HouseNumber");
			String AddCustomerrequest = StringUtils.replace(request.toString(), "$HouseNumber$", HouseNumber);
			strrequest = StringUtils.replace(AddCustomerrequest, "$SiteID$", SiteID);
			break;
		
		case "CreateInstallServiceWorkOrders":
			String ServiceInfo = "";
			String ServiceInfo_DATA = "";
			String ServiceInfo_CABLE = "";
			String ServiceInfo_VOICE = "";
			if(!dataTable.getData(env, "ServiceCodes_Data").trim().equals("")){
				if(dataTable.getData(env, "ServiceCodes_Data").contains(",")){
					String[] ServiceCodes = dataTable.getData(env, "ServiceCodes_Data").split(",");
					for(int i=0; i<ServiceCodes.length; i++){
						ServiceInfo_DATA = ServiceInfo_DATA+"<ServiceInfo><ServiceCode>"+ServiceCodes[i]+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
					}
				}else{
					ServiceInfo_DATA = "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "ServiceCodes_Data")+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
				}
			}
			
			if(!dataTable.getData(env, "ServiceCodes_Cable").trim().equals("")){
				if(dataTable.getData(env, "ServiceCodes_Cable").contains(",")){
					String[] ServiceCodes = dataTable.getData(env, "ServiceCodes_Cable").split(",");
					for(int i=0; i<ServiceCodes.length; i++){
						ServiceInfo_CABLE = ServiceInfo_CABLE+"<ServiceInfo><ServiceCode>"+ServiceCodes[i]+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
					}
				}else{
					ServiceInfo_CABLE = "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "ServiceCodes_Cable")+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
				}
			}
			
			if(!dataTable.getData(env, "ServiceCodes_Voice").trim().equals("")){
				if(dataTable.getData(env, "ServiceCodes_Voice").contains(",")){
					String[] ServiceCodes_Voice = dataTable.getData(env, "ServiceCodes_Voice").split(",");
					for(int i=0;i<ServiceCodes_Voice.length;i++){
						ServiceInfo_VOICE = ServiceInfo_VOICE+"<ServiceInfo><ServiceCode>"+ServiceCodes_Voice[i]+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
					}
				}else{
					ServiceInfo_VOICE = "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "ServiceCodes_Voice")+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
				}
			}
			
			String AccountNumber1 = strAccountNumber;
			String InstallServiceWO = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			String ServiceDetails = InstallServiceWO;
			
			if(!dataTable.getData(env, "ServiceCodes_Data").trim().equals("")){
				ServiceDetails = StringUtils.replace(ServiceDetails.toString(), "$<ServiceInfo>$", ServiceInfo_DATA);
			}else{
				ServiceDetails = StringUtils.replace(ServiceDetails.toString(), "<Data>$<ServiceInfo>$<ExternalKey>Y</ExternalKey></Data>", "");
			}
			
			String DirectoryInfo = "";
			if(SiteID.contains("001")) //for MACINT
				DirectoryInfo = "<ExternalKey>TDM20160510122210</ExternalKey><NewPic>05269</NewPic><PICChangeReason>01</PICChangeReason><FreezePIC>N</FreezePIC><NewLPIC>05269</NewLPIC><LPICChangeReason>01</LPICChangeReason><FreezeLPIC>N</FreezeLPIC><LECFreeze>N</LECFreeze><PortedNonPorted>N</PortedNonPorted><TelephoneNumber>4782547001</TelephoneNumber><DirectoryInfo><DirectoryType>PRIMARY</DirectoryType><DirectoryExtKey>01</DirectoryExtKey><ListingTypeCode>S</ListingTypeCode><ListType>MNL</ListType><ListingInstructions>NP</ListingInstructions><ListingIDCode>A1</ListingIDCode><FirstName>IPC</FirstName><LastName>Account</LastName><SpecialFilingName p1:nil=\"true\"/><ServiceBundle>NP CB</ServiceBundle></DirectoryInfo><CustomerQuestionnaire><QuestionnaireCode>PORT1</QuestionnaireCode><CustomerQualified>Y</CustomerQualified></CustomerQuestionnaire>";
			else//for CONINT
				DirectoryInfo = "<ExternalKey>TDM20160510122210</ExternalKey><NewPic>05269</NewPic><PICChangeReason>01</PICChangeReason><FreezePIC>N</FreezePIC><NewLPIC>05269</NewLPIC><LPICChangeReason>01</LPICChangeReason><FreezeLPIC>N</FreezeLPIC><LECFreeze>N</LECFreeze><PortedNonPorted>N</PortedNonPorted><TelephoneNumber>2516161296</TelephoneNumber><DirectoryInfo><DirectoryType>PRIMARY</DirectoryType><DirectoryExtKey>01</DirectoryExtKey><ListingTypeCode>S</ListingTypeCode><ListType>MNL</ListType><ListingInstructions>NP</ListingInstructions><ListingIDCode>A1</ListingIDCode><FirstName p1:nil=\"true\"/><LastName p1:nil=\"true\"/><SpecialFilingName p1:nil=\"true\"/><ServiceBundle>NP CB</ServiceBundle></DirectoryInfo><CustomerQuestionnaire><QuestionnaireCode>PORT1</QuestionnaireCode><CustomerQualified>Y</CustomerQualified></CustomerQuestionnaire>";
			
			if(!dataTable.getData(env, "ServiceCodes_Voice").trim().equals("")){
				ServiceDetails = StringUtils.replace(ServiceDetails.toString(), "$<TelephoneServiceInfo>$", ServiceInfo_VOICE+DirectoryInfo);
			}else{
				ServiceDetails = StringUtils.replace(ServiceDetails.toString(), "<Telephone>$<TelephoneServiceInfo>$</Telephone>", "");
			}
			
			if(!dataTable.getData(env, "ServiceCodes_Cable").trim().equals("")){
				ServiceDetails = StringUtils.replace(ServiceDetails.toString(), "$<CableServiceInfo>$", ServiceInfo_CABLE);
			}else{
				ServiceDetails = StringUtils.replace(ServiceDetails.toString(), "<Cable>$<CableServiceInfo>$<ExternalKey>Y</ExternalKey></Cable>", "");
			}			
			strrequest = StringUtils.replace(ServiceDetails, "$AccountNumber$", AccountNumber1);
			break;	
		
		case "DissconnectAccountWO":
			String AccountNumber_Disconnect = dataTable.getData(env, "AccountNumber");
			String DisconnectAccountWO_SiteID = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			strrequest = StringUtils.replace(DisconnectAccountWO_SiteID.toString(), "$AccountNumber$", AccountNumber_Disconnect);			
			break;	
			
		case "UpdateWOStatusToComplete":
			String WO_UpdateWOStatusCmplt = dataTable.getData(env, "WorkOrder");
			String UpdateWOStatusCmplt_SiteID = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			strrequest = StringUtils.replace(UpdateWOStatusCmplt_SiteID.toString(), "$WorkOrder$", WO_UpdateWOStatusCmplt);		
			break;				
		
		case "RescheduleWorkOrder":
			String AccountNumber2 = dataTable.getData(env, "AccountNumber");
			String WorkOrder = dataTable.getData(env, "WorkOrder");
			String RescheduleWO = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			String RescheduleWO1 = StringUtils.replace(RescheduleWO, "$AccountNumber$", AccountNumber2);
			String RescheduleWO2 = StringUtils.replace(RescheduleWO1, "$WorkOrder$", WorkOrder);
			strrequest = StringUtils.replace(RescheduleWO2, "$ScheduleDate$", Timeslot);			
			break;

		case "CheckInWorkOrder":
			String WorkOrder1 = dataTable.getData(env, "WorkOrder");
			//String WorkOrder1 = properties.getProperty("WorkOrder");
			String CheckinWorkOrder = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			strrequest = StringUtils.replace(CheckinWorkOrder, "$WorkOrder$", WorkOrder1);
			break;
			
		case "RetrieveWorkOrder":
			String WorkOrder2 = dataTable.getData(env, "WorkOrder");
			String RetrieveWorkOrder = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			strrequest = StringUtils.replace(RetrieveWorkOrder, "$WorkOrder$", WorkOrder2);
			break;
			
		case "ServiceChange_AddOccurrence":
			String ServiceOcc = "";
			
			if(dataTable.getData(env, "AddServices").contains(",")){
				String[] ServiceAddOcc = dataTable.getData(env, "AddServices").split(",");
				
				for(int i=0;i<ServiceAddOcc.length;i++){
					ServiceOcc = ServiceOcc + "<ServiceInfo><ServiceCode>"+ServiceAddOcc[i]+"</ServiceCode><ServiceOccurrence>$OccNo$</ServiceOccurrence><Action>ADD</Action></ServiceInfo>";
				}
			}else{
				ServiceOcc = "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "AddServices")+"</ServiceCode><ServiceOccurrence>$OccNo$</ServiceOccurrence><Action>ADD</Action></ServiceInfo>";
			}
			String AccountNumber_SV = dataTable.getData(env, "AccountNumber");
			String WONumber_SV = dataTable.getData(env, "WorkOrder");
			String OccNo_SV = dataTable.getData(env, "OccNo");
			
			String ServiceChange_res = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			String ServiceChange_res1 = StringUtils.replace(ServiceChange_res.toString(), "$AccountNumber$", AccountNumber_SV);
			String ServiceChange_res2 = StringUtils.replace(ServiceChange_res1.toString(), "$WorkOrderNumber$", WONumber_SV);
			String ServiceChange_res3 = StringUtils.replace(ServiceChange_res2.toString(), "$<ServiceInfo>$", ServiceOcc);
			strrequest = StringUtils.replace(ServiceChange_res3, "$OccNo$", OccNo_SV);
			break;
			
		case "ServiceChange_RemoveOccurrence":
			String OccNo_SV1 = dataTable.getData(env,"OccNo");
			String ServiceOcc_Remove = "";
			if(dataTable.getData(env, "RemoveServices").contains(",")){
				String[] ServiceRemoveOcc = dataTable.getData(env, "RemoveServices").split(",");
				for(int i=0;i<ServiceRemoveOcc.length;i++){
					ServiceOcc_Remove = ServiceOcc_Remove + "<ServiceInfo><ServiceCode>"+ServiceRemoveOcc[i]+"</ServiceCode><ServiceOccurrence>$OccNo$</ServiceOccurrence><Action>REMOVE</Action></ServiceInfo>";
				}
			}else{
				ServiceOcc_Remove = "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "RemoveServices")+"</ServiceCode><ServiceOccurrence>$OccNo$</ServiceOccurrence><Action>REMOVE</Action></ServiceInfo>";
			}
			String AccountNumber_RSV = strAccountNumber;
			String WONumber_RSV = dataTable.getData(env,"WorkOrder");						
			String ServiceChange_Remove = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			String ServiceChange_Remove1 = StringUtils.replace(ServiceChange_Remove.toString(), "$AccountNumber$", AccountNumber_RSV);
			String ServiceChange_Remove2 = StringUtils.replace(ServiceChange_Remove1.toString(), "$WorkOrderNumber$", WONumber_RSV);
			String ServiceChange_Remove3 = StringUtils.replace(ServiceChange_Remove2, "$<ServiceInfo>$",ServiceOcc_Remove);
			strrequest = StringUtils.replace(ServiceChange_Remove3, "$OccNo$",OccNo_SV1);
			break;	
		case "CreateAddOccurrenceWorkOrder":
			String ServiceInfo_OCC1 = "";
			String ServiceInfo_DATA_OCC1 = "";
			String ServiceInfo_CABLE_OCC1 = "";
			String ServiceInfo_VOICE_OCC1  = "";
			if(!dataTable.getData(env, "ServiceCodes_Data_Occ1").trim().equals("")){
				if(dataTable.getData(env, "ServiceCodes_Data_Occ1").contains(",")){
					String[] ServiceCodes = dataTable.getData(env, "ServiceCodes_Data_Occ1").split(",");
					for(int i=0; i<ServiceCodes.length; i++){
						ServiceInfo_DATA_OCC1= ServiceInfo_DATA_OCC1+"<ServiceInfo><ServiceCode>"+ServiceCodes[i]+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
					}
				}else{
					ServiceInfo_DATA_OCC1= "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "ServiceCodes_Data_Occ1")+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
				}
			}
			
			if(!dataTable.getData(env, "ServiceCodes_Cable_Occ1").trim().equals("")){
				if(dataTable.getData(env, "ServiceCodes_Cable_Occ1").contains(",")){
					String[] ServiceCodes = dataTable.getData(env, "ServiceCodes_Cable_Occ1").split(",");
					for(int i=0; i<ServiceCodes.length; i++){
						ServiceInfo_CABLE_OCC1= ServiceInfo_CABLE_OCC1+"<ServiceInfo><ServiceCode>"+ServiceCodes[i]+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
					}
				}else{
					ServiceInfo_CABLE_OCC1= "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "ServiceCodes_Cable_Occ1")+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
				}
			}
			
			if(!dataTable.getData(env, "ServiceCodes_Voice_Occ1").trim().equals("")){
				if(dataTable.getData(env, "ServiceCodes_Voice_Occ1").contains(",")){
					String[] ServiceCodes_Voice = dataTable.getData(env, "ServiceCodes_Voice_Occ1").split(",");
					for(int i=0;i<ServiceCodes_Voice.length;i++){
						ServiceInfo_VOICE_OCC1= ServiceInfo_VOICE_OCC1+"<ServiceInfo><ServiceCode>"+ServiceCodes_Voice[i]+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
					}
				}else{
					ServiceInfo_VOICE_OCC1= "<ServiceInfo><ServiceCode>"+dataTable.getData(env, "ServiceCodes_Voice_Occ1")+"</ServiceCode><ServiceRate p1:nil=\"true\" /></ServiceInfo>";
				}
			}
			
			String AccountNumberOCC = strAccountNumber;
			String InstallServiceWO1 = StringUtils.replace(request.toString(), "$SiteID$", SiteID);
			String ServiceDetails1 = InstallServiceWO1;
			
			if(!dataTable.getData(env, "ServiceCodes_Data_Occ1").trim().equals("")){
				ServiceDetails1 = StringUtils.replace(ServiceDetails1.toString(), "$<ServiceInfo>$", ServiceInfo_DATA_OCC1);
			}else{
				ServiceDetails1 = StringUtils.replace(ServiceDetails1.toString(), "<Data>$<ServiceInfo>$<ExternalKey>Y</ExternalKey></Data>", "");
			}
			
			String DirectoryInfo1 = "";
			if(SiteID.contains("001")) //for MACINT
				DirectoryInfo1 = "<ExternalKey>TDM20160510122210</ExternalKey><NewPic>05269</NewPic><PICChangeReason>01</PICChangeReason><FreezePIC>N</FreezePIC><NewLPIC>05269</NewLPIC><LPICChangeReason>01</LPICChangeReason><FreezeLPIC>N</FreezeLPIC><LECFreeze>N</LECFreeze><PortedNonPorted>N</PortedNonPorted><TelephoneNumber>4782547001</TelephoneNumber><DirectoryInfo><DirectoryType>PRIMARY</DirectoryType><DirectoryExtKey>01</DirectoryExtKey><ListingTypeCode>S</ListingTypeCode><ListType>MNL</ListType><ListingInstructions>NP</ListingInstructions><ListingIDCode>A1</ListingIDCode><FirstName>IPC</FirstName><LastName>Account</LastName><SpecialFilingName p1:nil=\"true\"/><ServiceBundle>NP CB</ServiceBundle></DirectoryInfo><CustomerQuestionnaire><QuestionnaireCode>PORT1</QuestionnaireCode><CustomerQualified>Y</CustomerQualified></CustomerQuestionnaire>";
			else//for CONINT
				DirectoryInfo1 = "<ExternalKey>TDM20160510122210</ExternalKey><NewPic>05269</NewPic><PICChangeReason>01</PICChangeReason><FreezePIC>N</FreezePIC><NewLPIC>05269</NewLPIC><LPICChangeReason>01</LPICChangeReason><FreezeLPIC>N</FreezeLPIC><LECFreeze>N</LECFreeze><PortedNonPorted>N</PortedNonPorted><TelephoneNumber>2516161296</TelephoneNumber><DirectoryInfo><DirectoryType>PRIMARY</DirectoryType><DirectoryExtKey>01</DirectoryExtKey><ListingTypeCode>S</ListingTypeCode><ListType>MNL</ListType><ListingInstructions>NP</ListingInstructions><ListingIDCode>A1</ListingIDCode><FirstName p1:nil=\"true\"/><LastName p1:nil=\"true\"/><SpecialFilingName p1:nil=\"true\"/><ServiceBundle>NP CB</ServiceBundle></DirectoryInfo><CustomerQuestionnaire><QuestionnaireCode>PORT1</QuestionnaireCode><CustomerQualified>Y</CustomerQualified></CustomerQuestionnaire>";
			
			if(!dataTable.getData(env, "ServiceCodes_Voice_Occ1").trim().equals("")){
				ServiceDetails1 = StringUtils.replace(ServiceDetails1.toString(), "$<TelephoneServiceInfo>$", ServiceInfo_VOICE_OCC1+DirectoryInfo1);
			}else{
				ServiceDetails1 = StringUtils.replace(ServiceDetails1.toString(), "<Telephone>$<TelephoneServiceInfo>$<ExternalKey>Y</ExternalKey></Telephone>", "");
			}
			
			if(!dataTable.getData(env, "ServiceCodes_Cable_Occ1").trim().equals("")){
				ServiceDetails1 = StringUtils.replace(ServiceDetails1.toString(), "$<CableServiceInfo>$", ServiceInfo_CABLE_OCC1);
			}else{
				ServiceDetails1 = StringUtils.replace(ServiceDetails1.toString(), "<Cable>$<CableServiceInfo>$<ExternalKey>Y</ExternalKey></Cable>", "");
			}			
			strrequest = StringUtils.replace(ServiceDetails1, "$AccountNumber$", AccountNumberOCC);
			break;	
	
		default:
			throw new FrameworkException("Unhandled API Name!");
		}
		return strrequest;
	}

	// Method to read the excel
	public HashMap<String, String> readExcel(String apiname)
			throws Exception {
		
		
		List<List<Cell>> sheetData = new ArrayList<List<Cell>>();
		FileInputStream fis = null;
		try {
			// Create a FileInputStream that will be use to read the excel file.
			fis = new FileInputStream(filename);
			// Create an excel workbook from the file system.
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			// Get the first sheet on the workbook.
			HSSFSheet sheet = workbook.getSheet("SoapUI");
			// Iterating through the sheet's rows and on each row's cells. We store the data read in an ArrayList
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
				// workbook.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		HashMap<String, String> hMap = showExelData(sheetData, apiname);
		return hMap;
	}

	// function to return the Hashmap containing the URL/WSDL/APINAME
	private HashMap<String, String> showExelData(
			List<List<Cell>> sheetData, String apiname) {
		// Iterates the data and print it out to the console.
		HashMap<String, String> hMap = new HashMap<String, String>();
		// int len2 = sheetData.size();
		for (int i = 0; i < sheetData.size(); i++) {
			List<Cell> list = (List<Cell>) sheetData.get(i);
			// int len1 = list.size();
			for (int j = 0; j < list.size(); j++) {
				HSSFCell cell = (HSSFCell) list.get(j);
				String apiname2 = cell.getRichStringCellValue().getString();
				if (apiname2.equals(apiname)) {
					HSSFCell cell2 = (HSSFCell) list.get(j + 1);
					String url = cell2.getRichStringCellValue().getString();
					hMap.put("url", url);
					hMap.put("api", apiname);
					HSSFCell cell3 = (HSSFCell) list.get(j + 2);
					String MimeHeader = cell3.getRichStringCellValue()
							.getString();
					hMap.put("MimeHeaders", MimeHeader);
				
				}
			}
			System.out.println("");
		}
		return hMap;
	}

	public String GetTagValueFromResponse(SOAPMessage response, String TagName)
			throws SOAPException {

		// Get the Tag Value Name
		SOAPBody soapBody = response.getSOAPBody();
		String TagValue = soapBody.getElementsByTagName(TagName).item(0)
				.getTextContent();
		
		// System.out.println("TagValue: " + TagValue);
		return TagValue;

	}

		
	public String getSOAPResponseString(SOAPMessage soapResponse)
			throws Exception {
		StringWriter sw = new StringWriter();
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		StreamResult result = new StreamResult(sw);
		transformer.transform(sourceContent, result);
		
		return sw.toString();
	}
	
		
	public List<List<Cell>> readExcel_Prerequisite(String strFileNamePath,String strSheetName)
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
			// Iterating through the sheet's rows and on each row's cells. We store the data read in an ArrayList
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
				// workbook.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		//HashMap<String, String> hMap = showExelData(sheetData, apiname);
		return sheetData;
		
	}
	
	public void SetExcel_Prerequisite(String strfilename,String strSheetName,String strTCName, String strColumnName, String strValue)
			throws Exception {
				
		
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
                
                if(jxlsheet.getCell(0,j).getContents().equals(strTCName)) {
                      for(int x=1; x< jxlsheet.getColumns(); x++) {
                             if(jxlsheet.getCell(x,0).getContents().equals(strColumnName)) {
                            	 
                            	HSSFRow row = sheet.getRow(j);
                 				HSSFCell c = row.createCell(x);
                 				c.setCellValue(strValue);
                 			    fis.close();
                 				FileOutputStream fos =new FileOutputStream(strfilename);
                 			    workbook.write(fos);
                 			    fos.close();                                                                           
                                    
                             }
                      }
                }
                

                
         }          	
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		
		
	}

	public void DisconnectAccountWO_ExistingAccount() {
		try {

			SOAPMessage DisconnectWO = soapUI("DissconnectAccountWO");

			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber = GetTagValueFromResponse(DisconnectWO,
					"WorkOrderNumber");
			report.updateTestLog("Create scheduled TC work order",
					"Work Order Number: " + WONumber, Status.DONE);

			dataTable.putData(env, "WorkOrder", WONumber);
			Thread.sleep(5000);
			
			soapUI("RescheduleWorkOrder");
			Thread.sleep(15000);
			
			soapUI("UpdateWOStatusToComplete");
			Thread.sleep(5000);
			
			soapUI("CheckInWorkOrder");
			System.out.println("Done");	
					
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void ReconnectAccount_ExistingAccount() {
		try {

			SOAPMessage CreateInstallWO = soapUI("CreateInstallServiceWorkOrders");

			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber = GetTagValueFromResponse(CreateInstallWO,
					"WorkOrderNumber");
			report.updateTestLog("Create install TC work order",
					"Work Order Number: " + WONumber, Status.DONE);

			dataTable.putData(env, "WorkOrder", WONumber);
			Thread.sleep(5000);
						
			soapUI("CheckInWorkOrder");
			System.out.println("Done");	
					
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void ReconnectAccount_ExistingAccount_CBSS() {
		try {

			SOAPMessage CreateInstallWO_CBSS = soapUI("CreateInstallServiceWorkOrders");

			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber = GetTagValueFromResponse(CreateInstallWO_CBSS,
					"WorkOrderNumber");
			report.updateTestLog("Create install TC work order", "Work Order Number: " + WONumber, Status.DONE);

			dataTable.putData(env, "WorkOrder", WONumber);
			Thread.sleep(5000);
						
			soapUI("CheckInWorkOrder");
			System.out.println("Done");				

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
		
	public String GetTagsValueFromResponse(SOAPMessage response, String TagName, int i)
				throws SOAPException {

			// Get the Tag Value Name
			SOAPBody soapBody = response.getSOAPBody();
			String TagValue = soapBody.getElementsByTagName(TagName).item(i-1)
					.getTextContent();
			//System.out.println(TagValue);

			// System.out.println("TagValue: " + TagValue);

			return TagValue;

		}
	
	public void getARBalances(String collectionStatus) {
		try {

			// Update TimeSlot
			SOAPMessage responsegetARBal = soapUI("getPastDueWriteOff");

			float AR1To30 = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"AR1To30"));
			System.out.println("AR1To30 value : "+AR1To30);
			report.updateTestLog("AR Balance Values","AR1To30: "+AR1To30, Status.DONE);
			
			float AR31To60 = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"AR31To60"));
			report.updateTestLog("AR Balance Values","AR31To60: "+AR31To60, Status.DONE);
			System.out.println("AR31To60 value : "+AR31To60);
			
			float AR61To90 = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"AR61To90"));
			report.updateTestLog("AR Balance Values","AR61To90: "+AR61To90, Status.DONE);
			System.out.println("AR61To90 value : "+AR61To90);
			
			float AR91To120 = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"AR91To120"));
			report.updateTestLog("AR Balance Values","AR91To120: "+AR91To120, Status.DONE);
			System.out.println("AR91To120 value : "+AR91To120);
			
			float AR121To150 = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"AR121To150"));
			report.updateTestLog("AR Balance Values","AR31To60: "+AR31To60, Status.DONE);
			System.out.println("AR121To150 value : "+AR121To150);
			
			float AROver150Days = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"AROver150Days"));
			report.updateTestLog("AR Balance Values","AROver150Days: "+AROver150Days, Status.DONE);
			System.out.println("AR121To150 value : "+AROver150Days);
			
			float WriteOffAmount = Float.parseFloat(GetTagValueFromResponse(responsegetARBal,
					"WriteOffAmount"));
			report.updateTestLog("AR Balance Values","WriteOffAmount: "+WriteOffAmount, Status.DONE);
			System.out.println("WriteOffAmount value : "+WriteOffAmount);
			
			if((AR31To60 == 0.00)||(AR61To90 == 0.00)||(AR91To120 == 0.00)||(AR121To150 == 0.00)||(AROver150Days == 0.00)){
				if(collectionStatus.equals("N")){
					report.updateTestLog("Verify Collection Status","Collection Status N displayed", Status.PASS);
				}else{
					report.updateTestLog("Verify Collection Status","Collection Status N not displayed.Actual Collection status Value :"+collectionStatus, Status.FAIL);
				}
			}
			if(!(AR31To60 == 0.00)||!(AR61To90 == 0.00)||!(AR91To120 == 0.00)||!(AR121To150 == 0.00)||!(AROver150Days == 0.00)){
				if(collectionStatus.equals("Y")){
					report.updateTestLog("Verify Collection Status","Collection Status Y displayed", Status.PASS);
				}else{
					report.updateTestLog("Verify Collection Status","Collection Status Y not displayed.Actual Collection status Value :"+collectionStatus, Status.FAIL);
				}
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static String strAccountNumber = "";
	
	public void createNewAccount_CBMA(String accountNumber) 
	{
		try {
			RestComponents RestComponents=new RestComponents(scriptHelper);	
			String RestResponse = "";
			String cableServiceCode = dataTable.getData(env, "ServiceCodes_Cable");
			String dataServiceCode = dataTable.getData(env, "ServiceCodes_Data");
			String voiceServiceCode = dataTable.getData(env, "ServiceCodes_Voice");
			
			Reporting("Data Service Codes: " + dataServiceCode, Status.DONE);
			Reporting("Cable Service Codes: " + cableServiceCode, Status.DONE);
			Reporting("Telephony Service Codes: " + voiceServiceCode, Status.DONE);
			
			// Create House Record
			SOAPMessage response = soapUI("CreateHouseRecordInput");

			// Get the House Number from the response and put the same in excel
			String HouseNumber = GetTagValueFromResponse(response, "HouseNumber");
			report.updateTestLog("Create House Record Input", "House Number: " + HouseNumber, Status.DONE);
			dataTable.putData(env, "HouseNumber", HouseNumber);
			//properties.setProperty("HouseNumber", HouseNumber);
		
			//Running POST Call
			if(dataTable.getData(env, "SiteId").contains("001"))
				RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\":{\"centralOfficeFacilityId\": \"VOIP\",\"switchId\": \"MACNGAHRGT0\"}}");
			else
				RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\": {\"centralOfficeFacilityId\": \"MACMTC\",\"networkElementName\": \"CIRCUIT\"}}");

			// Add Customer and create account
			SOAPMessage responseAddCustomer = soapUI("AddCustomer");
			
			// Get the Account Number from the response and put the same in excel
			strAccountNumber = GetTagValueFromResponse(responseAddCustomer, "AccountNumber");
			Reporting("Account Number: "+ strAccountNumber, Status.DONE);
			if(strAccountNumber.length() == 9 ){
				dataTable.putData(env, accountNumber, strAccountNumber);
			}else{
				strAccountNumber = "0" + strAccountNumber;
				dataTable.putData(env, accountNumber, strAccountNumber);
			}				
			Thread.sleep(15000);	
			
			//Dumping Account Number
			accountDumpToTextFile(dataTable.getData(env, "SiteId")+"-"+strAccountNumber);
			
			// Create Install Service Work Order
			SOAPMessage responseCreateInstallServiceWO = soapUI("CreateInstallServiceWorkOrders");			
						
			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber1 = GetTagValueFromResponse(responseCreateInstallServiceWO,"WorkOrderNumber");
			Reporting("Work Order Number: "+WONumber1, Status.DONE);
			dataTable.putData(env, "WorkOrder", WONumber1);
			//properties.setProperty("WorkOrder", WONumber1);
			Thread.sleep(5000);
			
			System.out.println("<<<<< Account Created >>>>>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createNewAccount(String accountNumber) 
	{
		try {
			RestComponents RestComponents=new RestComponents(scriptHelper);	
			String RestResponse = "";
			String cableServiceCode = dataTable.getData(env, "ServiceCodes_Cable");
			String dataServiceCode = dataTable.getData(env, "ServiceCodes_Data");
			String voiceServiceCode = dataTable.getData(env, "ServiceCodes_Voice");
			
			Reporting("Data Service Codes: " + dataServiceCode, Status.DONE);
			Reporting("Cable Service Codes: " + cableServiceCode, Status.DONE);
			Reporting("Telephony Service Codes: " + voiceServiceCode, Status.DONE);
			
			// Create House Record
			SOAPMessage response = soapUI("CreateHouseRecordInput");

			// Get the House Number from the response and put the same in excel
			String HouseNumber = GetTagValueFromResponse(response, "HouseNumber");
			report.updateTestLog("Create House Record Input", "House Number: " + HouseNumber, Status.DONE);
			dataTable.putData(env, "HouseNumber", HouseNumber);
		
			//Running POST Call
			if(dataTable.getData(env, "SiteId").contains("001"))
				RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\":{\"centralOfficeFacilityId\": \"VOIP\",\"switchId\": \"MACNGAHRGT0\"}}");
			else
				RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\": {\"centralOfficeFacilityId\": \"MACMTC\",\"networkElementName\": \"CIRCUIT\"}}");

			// Add Customer and create account
			SOAPMessage responseAddCustomer = soapUI("AddCustomer");
			
			// Get the Account Number from the response and put the same in excel
			strAccountNumber = GetTagValueFromResponse(responseAddCustomer, "AccountNumber");
			Reporting("Account Number: "+ strAccountNumber, Status.DONE);
			if(strAccountNumber.length() == 9 ){
				dataTable.putData(env, accountNumber, strAccountNumber);
			}else{
				strAccountNumber = "0" + strAccountNumber;
				dataTable.putData(env, accountNumber, strAccountNumber);
			}				
			System.out.println("Account Number >>>>>>>>>>>>>>>>> "+strAccountNumber);
			Thread.sleep(15000);
			
			//Dumping Account Number
			accountDumpToTextFile(dataTable.getData(env, "SiteId")+"-"+strAccountNumber);
			
			// Create Install Service Work Order
			SOAPMessage responseCreateInstallServiceWO = soapUI("CreateInstallServiceWorkOrders");			
						
			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber1 = GetTagValueFromResponse(responseCreateInstallServiceWO,"WorkOrderNumber");
			report.updateTestLog("Create Install Service work order","Work Order Number: "+WONumber1, Status.DONE);
			dataTable.putData(env, "WorkOrder", WONumber1);
			Thread.sleep(5000);
			
			if(voiceServiceCode.isEmpty())
			{
				//Check in Work Order
				soapUI("CheckInWorkOrder");				
			}
			System.out.println("<<<<< Account Created >>>>>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createTestData(String fieldName, boolean profileCreateSetPassword, String userType, int subAccountTags, boolean DTWPush) 
	{
		try {
			RestComponents RestComponents=new RestComponents(scriptHelper);	
			String RestResponse = "";
			String cableServiceCode = dataTable.getData(env, "CableServiceCodes");
			String dataServiceCode = dataTable.getData(env, "ServiceCodes");
			String voiceServiceCode = dataTable.getData(env, "ServiceCodes_Voice");
			
			Reporting("Data Service Codes: " + dataServiceCode, Status.DONE);
			Reporting("Cable Service Codes: " + cableServiceCode, Status.DONE);
			Reporting("Telephony Service Codes: " + voiceServiceCode, Status.DONE);
			
			// Create House Record
			SOAPMessage response = soapUI("CreateHouseRecordInput");

			// Get the House Number from the response and put the same in excel
			String HouseNumber = GetTagValueFromResponse(response, "HouseNumber");
			report.updateTestLog("Create House Record Input", "House Number: " + HouseNumber, Status.DONE);
			dataTable.putData(env, "HouseNumber", HouseNumber);
			if(!voiceServiceCode.isEmpty())
			{
				if(dataTable.getData(env, "SiteId").contains("001"))
					RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\":{\"centralOfficeFacilityId\": \"VOIP\",\"switchId\": \"MACNGAHRGT0\"}}");
				else
					RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\": {\"centralOfficeFacilityId\": \"MACMTC\",\"networkElementName\": \"CIRCUIT\"}}");
			}			

			// Add Customer and create account
			SOAPMessage responseAddCustomer = soapUI("AddCustomer");
			
			// Get the Account Number from the response and put the same in excel
			String strAccountNumber = GetTagValueFromResponse(responseAddCustomer, "AccountNumber");
			Reporting("Account Number: "+ strAccountNumber, Status.DONE);
			if(strAccountNumber.length() == 9 ){
				dataTable.putData(env, fieldName, strAccountNumber);
			}else{
				strAccountNumber = "0" + strAccountNumber;
				dataTable.putData(env, fieldName, strAccountNumber);
			}				
			Thread.sleep(20000);			
			
			// Create Install Service Work Order
			SOAPMessage responseCreateInstallServiceWO = soapUI("CreateInstallServiceWorkOrders");			
						
			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber1 = GetTagValueFromResponse(responseCreateInstallServiceWO,"WorkOrderNumber");
			report.updateTestLog("Create Install Service work order","Work Order Number: "+WONumber1, Status.DONE);
			dataTable.putData(env, "WorkOrder", WONumber1);
			Thread.sleep(5000);
			
			if(voiceServiceCode.isEmpty())
			{
				//Check in Work Order
				soapUI("CheckInWorkOrder");				
			}
			System.out.println("<<<<< Account Created >>>>>");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
	
	public void createNewVoiceAccount() 
	{
		try {
			String ServiceCodes = dataTable.getData(env, "ServiceCodes_Voice");
			
			report.updateTestLog("createNewAccount_CBMA", "Telephony Service Codes: " + dataTable.getData(env, "ServiceCodes_Voice"), Status.DONE);
			
			// Create House Record
			SOAPMessage response = soapUI("CreateHouseRecordInput");

			// Get the House Number from the response and put the same in excel
			String HouseNumber = GetTagValueFromResponse(response, "HouseNumber");
			report.updateTestLog("Create House Record Input", "House Number: " + HouseNumber, Status.DONE);
			dataTable.putData(env, "HouseNumber", HouseNumber);
			
			RestComponents RestComponents=new RestComponents(scriptHelper);	
			String RestResponse = "";
			if(dataTable.getData(env, "SiteId").contains("001"))
				RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\":{\"centralOfficeFacilityId\": \"VOIP\",\"switchId\": \"MACNGAHRGT0\"}}");
			else
				RestResponse = RestComponents.RestPost_json("https://bis.qa.cox.com/ws/house/v1/networkelements/create?clientId=CBVMUSER", null, "{\"siteId\": \""+dataTable.getData(env, "SiteId")+"\",\"houseNumber\": \""+HouseNumber+"\",\"networkElement\": {\"centralOfficeFacilityId\": \"MACMTC\",\"networkElementName\": \"CIRCUIT\"}}");
			System.out.println(RestResponse);	
			
			// Add Customer and create account
			SOAPMessage responseAddCustomer = soapUI("AddCustomer");
			
			// Get the Account Number from the response and put the same in excel
			String strAccountNumber = GetTagValueFromResponse(responseAddCustomer, "AccountNumber");
			report.updateTestLog("Create Account", "Account Number: "+ strAccountNumber, Status.DONE);
			if(strAccountNumber.length() == 9 ){
				dataTable.putData(env, "AccountNumber", strAccountNumber);
			}else{
				strAccountNumber = "0" + strAccountNumber;
				dataTable.putData(env, "AccountNumber", strAccountNumber);
			}			
			Thread.sleep(30000);
						
			// Create Install Service Work Order
			SOAPMessage responseCreateInstallServiceWO = soapUI("CreateInstallServiceWorkOrders");
									
			// Get the WorkOrder Number from the response and put the same in excel
			String WONumber1 = GetTagValueFromResponse(responseCreateInstallServiceWO,"WorkOrderNumber");
			report.updateTestLog("Create Install Service work order","Work Order Number: "+WONumber1, Status.DONE);
			dataTable.putData(env, "WorkOrder", WONumber1);

			Thread.sleep(10000);
			//Check in Work Order
			/*soapUI("CheckInWorkOrder");
			System.out.println("Done");*/			

		} catch (Exception e)
		{ e.printStackTrace(); }
	}
	
	public void openSession(){
		try {
			SOAPMessage opensessionResponse = soapUI("OpenSession");
			String sessionID = GetTagValueFromResponse(opensessionResponse, "id");
			System.out.println(sessionID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void create_DTWPush() throws HttpException, IOException
	{
		RestComponents RestComponents=new RestComponents(scriptHelper);
		String AccountID = dataTable.getData(env, "AccountNumber");
		String MainAccountEmailID = AccountID+"@mailinator.com";
		AccountID = dataTable.getData(env, "SiteId")+dataTable.getData(env, "AccountNumber");
		String GetUserTokenUrl = "https://cbmaservices.train.cox.com/cbmaservices/services/rest/1.0//user/token/"+MainAccountEmailID;
		String GetUserTokenHeaders = "ma_transaction_id:1234,CoxAccess:Basic ZXFhdXNlcjplcWFwYXNzd29yZA==";
		String GetUserTokenresponse = RestComponents.RestGet(GetUserTokenUrl, GetUserTokenHeaders);
		String UsertokenValue = RestComponents.getTagValueFromResponse(GetUserTokenresponse,"encryptedValue");
		String post_VMEnable_Url= "https://cbmaservices.train.cox.com/cbmaservices/services/rest/1.0//voice/account/"+AccountID+"/voicemanager/enable";
		String post_VMEnableHeaders = "ma_transaction_id:1234,CoxAccess:Basic ZXFhdXNlcjplcWFwYXNzd29yZA==,ma_user_token:"+UsertokenValue+"";
		String postVMEnableresponse = RestComponents.RestPost_Url(post_VMEnable_Url, post_VMEnableHeaders);
		String postVMEnableSuccess = RestComponents.getTagValueFromResponse(postVMEnableresponse,"errorMessage");
		if(postVMEnableSuccess.equals("Success"))
			Reporting("DTW Push Successfull", Status.PASS);
		else
			Reporting("DTW Push not Successfull", Status.FAIL);
	}
}
