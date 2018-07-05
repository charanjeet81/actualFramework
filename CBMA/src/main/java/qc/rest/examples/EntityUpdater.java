package qc.rest.examples;

/**
*
*/
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.support.PageFactory;

import businesscomponents.ALMResultUpdater;
import businesscomponents.Constants;
import supportlibraries.ReusableLibrary;
import supportlibraries.ScriptHelper;

public class EntityUpdater{
	
	String domain;
	String project;
	RestConnector con;
	AuthenticateLoginLogout auth;
	
	
	public EntityUpdater(String almDomain, String almProject){
		this.domain = almDomain.trim();
		this.project = almProject.trim();
		System.out.println("EntityUpdater=="+this.domain);	
		this.con =  RestConnector.getInstance().init(new HashMap<String, String>(),Constants.HOST, Constants.PORT, this.domain, this.project);
		this.auth = new AuthenticateLoginLogout();
		
	}
	ALMResultUpdater almObj=new ALMResultUpdater();
	/**The Method is to Login to ALM via REST calls.
	 * Should be invoked to establish the connection/session, before calling any other method in the class to update the data in ALM.
	 * @throws Exception
	 */
	public synchronized void loginCall() throws Exception
	{
		Constants constobj=new Constants();		
		boolean isLoggedIn = auth.login(constobj.USERNAME, constobj.PASSWORD);
		if(!isLoggedIn)System.out.println("ALM Error: Unable to Login.");
	}

	/**The Method is to Logout from ALM via REST calls.
	 * @throws Exception
	 */
	public synchronized  void logoutCall() throws Exception{	
		auth.logout();
	}
	
	public synchronized void GetQCSession(){
		almObj.connect();
		
	    String qcsessionurl = con.buildUrl("qcbin/rest/site-session");
	    Map<String, String> requestHeaders = new HashMap<String, String>();
	    requestHeaders.put("Content-Type", "application/xml");
	    requestHeaders.put("Accept", "application/xml");
	    try {
	        Response resp = con.httpPost(qcsessionurl, null, requestHeaders);
	        con.updateCookies(resp);
	    } catch (Exception e) {
	        //e.printStackTrace();
	    	System.out.println("Could not connect to QC:"+e.getMessage());
	    }   
	}
	
	
	private void doUpdateEntity (String entity, String iD,String field1,String value1,String field2,String value2) throws Exception{
		GetQCSession();
		String newEntityToUpdateUrl = con.buildEntityUrl(entity,iD);
		if (Constants.VERSIONED) {
			String checkout = checkout(newEntityToUpdateUrl, "REST Test Checkout", -1);
		}
	    else {
	    	String lock = lock(newEntityToUpdateUrl);
	    }
    
	    //Create update string
	    String entityUpdateXml = generateUpdateEntityXml(entity, field1, value1,field2,value2);
    
	    //Create entity. (We could have instantiated the entity and used methods to set the new values.)
	    Entity e = EntityMarshallingUtils.marshal(Entity.class, entityUpdateXml);
	
	    //Do update operation
	    String updateResponseEntityXml = update(newEntityToUpdateUrl,EntityMarshallingUtils.unmarshal(Entity.class, e)).toString();
	            
	    //checkin
	    if (Constants.VERSIONED) {
	        boolean checkin = checkin(newEntityToUpdateUrl);
	    }
	    else {
	            boolean unlock = unlock(newEntityToUpdateUrl);
	    }    
    }
	
	private void doCreateEntity (String entity, String field,String value) throws Exception {
		GetQCSession();
		String newEntityToUpdateUrl = con.buildEntityUrl(entity,null);   
	    
	    //Create update string
	    String entityUpdateXml = generateCreateEntityXml(entity, field, value);
	    
	    //Create entity. (We could have instantiated the entity and used methods to set the new values.)
	    Entity e = EntityMarshallingUtils.marshal(Entity.class, entityUpdateXml);
	
	    //Do update operation
	    String updateResponseEntityXml = create(newEntityToUpdateUrl,EntityMarshallingUtils.unmarshal(Entity.class, e)).toString();
    }
	
	
	private synchronized void doUpdateRun (String entity,String runId,String field1,String value1,String field2 ,String value2) throws Exception{
		GetQCSession();
	    String newEntityToUpdateUrl = con.buildEntityUrl(entity,runId);	   
		if (Constants.VERSIONED) {
			String checkout = checkout(newEntityToUpdateUrl, "REST Test Checkout", -1);
		}
		else {
	        String lock = lock(newEntityToUpdateUrl);
		}
	
		//Create update string
	    String entityUpdateXml = generateUpdateEntityXml(entity, field1, value1,field2, value2);
	
		//Create entity. (We could have instantiated the entity and used methods to set the new values.)
		Entity e = EntityMarshallingUtils.marshal(Entity.class, entityUpdateXml);
	
		//Do update operation
	    String updateResponseEntityXml = update(newEntityToUpdateUrl,EntityMarshallingUtils.unmarshal(Entity.class, e)).toString();           
	    System.out.println(updateResponseEntityXml);
	    
	    //checkin
		if (Constants.VERSIONED) {
			boolean checkin = checkin(newEntityToUpdateUrl);
		}
		else {
			boolean unlock = unlock(newEntityToUpdateUrl);
		}
		auth.logout();
    }
	
	/**The method sends REST calls to update the test case corresponding to the given ID. 
	 * First, it sends a Lock request to lock the test case to avoid parallel update from other channel.
	 * Then the actual updated call is sent with the data (field and value) represented as XML String.
	 * At last, it sends a request to unlock the test case(Delete the lock)
	 * @param testCaseId
	 * @param field - the field/column of the test case in ALM which has to be updated.
	 * @param value - the actual value.
	 * @throws Exception
	 */
	private void doUpdate(String testCaseId,String field,String value) throws Exception{
		GetQCSession();
		String newEntityToUpdateUrl = con.buildEntityUrl("test",testCaseId);
		if (Constants.VERSIONED) {
			String checkout = checkout(newEntityToUpdateUrl, "REST Test Checkout", -1);
		}
		else {
			String lock = lock(newEntityToUpdateUrl);
		}
		
		//Create update string
		String entityUpdateXml = generateUpdateXml(field, value);
		
		//Create entity. (We could have instantiated the entity and used methods to set the new values.)
		Entity e = EntityMarshallingUtils.marshal(Entity.class, entityUpdateXml);
		
		//Do update operation
		String updateResponseEntityXml = update(newEntityToUpdateUrl,EntityMarshallingUtils.unmarshal(Entity.class, e)).toString();
		        
		//checkin
		if (Constants.VERSIONED) {
			boolean checkin = checkin(newEntityToUpdateUrl);
		}
		else {
			boolean unlock = unlock(newEntityToUpdateUrl);
		}    
    }
    
   
    /**
    * @param entityUrl
    * of the entity to checkout
    * @param comment
    * to keep on the server side of why you checked this entity out
    * @param version
    * to checkout or -1 if you want the latest
    * @return a string description of the checked out entity
    * @throws Exception
    */
    public synchronized String checkout(String entityUrl, String comment, int version) throws Exception {
	    String commentXmlBit =
	    ((comment != null) && !comment.isEmpty()
	    ? "<Comment>" + comment + "</Comment>"
	    : "");
	    String versionXmlBit = (version >= 0 ? "<Version>" + version + "</Version>" : "");
	    String xmlData = commentXmlBit + versionXmlBit;
	    String xml =
	    xmlData.isEmpty() ? "" : "<CheckOutParameters>" + xmlData + "</CheckOutParameters>";
	    Map<String, String> requestHeaders = new HashMap<String, String>();
	    requestHeaders.put("Content-Type", "application/xml");
	    requestHeaders.put("Accept", "application/xml");
	    Response response =
	    con.httpPost(entityUrl + "/versions/check-out", xml.getBytes(), requestHeaders);
	    return response.toString();
	}
	
	/**The method sends REST calls to update the test case corresponding to the given ID. 
	 * If the update request fails/encounters any exception, the methods attempts to send a REST call to unlock the test case.
	 * @param testCaseId
	 * @param field - the field/column of the test case in ALM which has to be updated.
	 * @param value - the actual value.
	*/
    public void updateTestCase(String testCaseId,String field,String value){ 
    	try{
        	doUpdate(testCaseId.trim(),field.trim(),value.trim());
        }catch(Exception e){
        	System.out.println("updateTestCase--Exception in Update - Unlock Enitty.....");
        	System.out.println("update Testcase method"+e.getMessage());
        	String newEntityToUpdateUrl = con.buildEntityUrl("test",testCaseId);
			try {
				boolean unlock = unlock(newEntityToUpdateUrl);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
        }        	   
	}

	public synchronized String getEntity (String entity, String iD, String query){	
		String responseXml = null;	
	    try{
	    	 responseXml = doGetEntity(entity, iD, query);
	    }
	    catch(Exception e){
	    	System.out.println("getEntity--Exception in Update - Unlock Enitty.....");
			e.printStackTrace();
	    }
	    return responseXml;    	   
	}

	private synchronized String doGetEntity (String entity, String iD, String query) throws Exception{
		GetQCSession();
		String newEntityToUpdateUrl = con.buildEntityUrl(entity,iD, query);
	
		//Do update operation
		String updateResponseEntityXml = fetch(newEntityToUpdateUrl,null).toString();
		auth.logout();
		return updateResponseEntityXml;	
	}

	public void updateEntityStatus(String entity, String iD,String field1,String value1,String field2,String value2){
		try{
			doUpdateEntity(entity, iD.trim(),field1.trim(),value1.trim(),field2.trim(),value2.trim());
		}
		catch(Exception e){
			System.out.println("updateEntityStatus--Exception in Update - Unlock Enitty.....");
			String newEntityToUpdateUrl = con.buildEntityUrl(entity,iD);
			try {
				boolean unlock = unlock(newEntityToUpdateUrl);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
	    }	    	   
	}

	public void createEntity(String entity, String field,String value){
		try{
			doCreateEntity(entity,field.trim(),value.trim());
		}
		catch(Exception e){
			System.out.println("createEntity--Exception in Update - Unlock Enitty.....");
			String newEntityToUpdateUrl = con.buildEntityUrl(entity,null);
			try {
				boolean unlock = unlock(newEntityToUpdateUrl);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
	    }	    	   
	}

	public synchronized void updateRunStatus(String entity,String runID,String field1,String value1,String field2,String value2,String testcaseName){
		try{
			
			doUpdateRun(entity.trim(),runID.trim(),field1.trim(),value1.trim(),field2.trim(),value2.trim());
		}
		catch(Exception e){
			System.out.println("updateRunStatus--Exception in Update - Unlock Enitty....."+"TestCaseName=="+testcaseName+"Entity="+entity+"RunID="+runID+"Field1="+field1+"Value1="+value1+"Field2="+field2+"Value2="+value2);
			String newEntityToUpdateUrl = con.buildEntityUrl("test",runID);
			try {
				boolean unlock = unlock(newEntityToUpdateUrl);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		e.printStackTrace();
		}	    	   
	}
	
	/**
	* @param entityUrl
	* to checkin
	* @return true if operation is successful
	* @throws Exception
	*/
	public synchronized boolean checkin(String entityUrl) throws Exception {
	
		//Execute a post operation on the checkin resource of your entity.
		Response response = con.httpPost(entityUrl + "/versions/check-in", null, null);
	    boolean ret = response.getStatusCode() == HttpURLConnection.HTTP_OK;
	    return ret;
	}
	
	/**
	* @param entityUrl
	* to lock
	* @return the locked entity xml
	* @throws Exception
	*/
	public synchronized String lock(String entityUrl) throws Exception {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Accept", "application/xml");
		return con.httpPost(entityUrl + "/lock", null, requestHeaders).toString();
	}
	
	/**
	* @param entityUrl
	* to unlock
	* @return
	* @throws Exception
	*/
	public synchronized boolean unlock(String entityUrl) throws Exception {
		return con.httpDelete(entityUrl + "/lock", null).getStatusCode() == HttpURLConnection.HTTP_OK;
	}
	
	/**
	* @param field
	* the field name to update
	* @param value
	* the new value to use
	* @return an XML that can be used to update an entity's single given field to given value
	*/
	private String generateUpdateXml(String field, String value) {
		return "<Entity Type=\"test\"><Fields>"
		+ "<Field Name=\""
		+ field
		+ "\"><Value>"
		+ value
		+ "</Value></Field>"
		+ "</Fields></Entity>";
	}
	
	/**
	* @param field
	* the field name to update
	* @param value
	* the new value to use
	* @return an XML that can be used to update an entity's single given field to given value
	*/
	private synchronized String generateUpdateEntityXml(String entity,String field1, String value1,String field2, String value2) {
		return "<Entity Type=\""+entity+"\"><Fields>"
		+ "<Field Name=\""
		+ field1
		+ "\"><Value>"
		+ value1
		+ "</Value></Field>"
		 + "<Field Name=\""
		+ field2
		+ "\"><Value>"
		+ value2
		+ "</Value></Field>"
		+ "</Fields></Entity>";
	}
	
	private String generateCreateEntityXml(String entity,String field, String value) {
		return "<Entity Type=\""+entity+"\"><Fields>"
		+ "<Field Name=\""
		+ field
		+ "\"><Value>"
		+ value
		+ "</Value></Field>"
		+ "<Field Name=\""
		+ "parent-id"
		+ "\"><Value>"
		+ "20"
		+ "</Value></Field>"
		+ "<Field Name=\""
		+ "subtype-id"
		+ "\"><Value>"
		+ "hp.qc.test-set.default"
		+ "</Value></Field>"
		+ "</Fields></Entity>";	
	}
	
	
	private synchronized Response fetch(String entityUrl, String updatedEntityXml) throws Exception {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/xml");
		Response put = con.httpGet(entityUrl, updatedEntityXml, requestHeaders);
		return put;
	}
	
	/**
	* @param entityUrl
	* to update
	* @param updatedEntityXml
	* new entity descripion. only lists updated fields. unmentioned fields will not
	* change.
	* @return xml description of the entity on the serverside, after update.
	* @throws Exception
	*/
	private synchronized Response update(String entityUrl, String updatedEntityXml) throws Exception {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/xml");
	    Response put = con.httpPut(entityUrl, updatedEntityXml.getBytes(), requestHeaders);
	    return put;
	}
	
	/**
	* @param entityUrl
	* to update
	* @param updatedEntityXml
	* new entity descripion. only lists updated fields. unmentioned fields will not
	* change.
	* @return xml description of the entity on the serverside, after update.
	* @throws Exception
	*/
	private synchronized Response create(String entityUrl, String updatedEntityXml) throws Exception {
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/xml");
	    Response post = con.httpPost(entityUrl, updatedEntityXml.getBytes(), requestHeaders);
	    return post;
	}
	
	public synchronized String createEntity(String entity, String field,String value, String testInstanceId, String testId, String owner, String status,String testsetID)
	{
		String responseXMLToGetRunIN=null;	
	    try{
	    	responseXMLToGetRunIN=doCreateEntity(entity,field.trim(),value.trim(), testInstanceId, testId, owner,  status,testsetID);
	    }
	    catch(Exception e)
	    {
	    	System.out.println("createEntity--Exception in Update - Unlock Enitty.....");
	    	System.out.println("create Entity method"+e.getMessage());
	    	String newEntityToUpdateUrl = con.buildEntityUrl(entity,null);
	    	try {
	    		boolean unlock = unlock(newEntityToUpdateUrl);
	    	} 
	    	catch (Exception e1) {
	    		// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
	    }
		return responseXMLToGetRunIN;
	}
	
	 private synchronized String doCreateEntity (String entity, String field,String value, String testInstanceId, String testId, String owner, String status,String testsetID) throws Exception
	 {
		 GetQCSession();
		 String newEntityToUpdateUrl = con.buildEntityUrl(entity,null);
		 
		 //Create update string
	    String entityUpdateXml = generateCreateEntityXml(entity, field, value, testInstanceId, testId, owner,  status, testsetID);

		//Create entity. (We could have instantiated the entity and used methods to set the new values.)
		Entity e = EntityMarshallingUtils.marshal(Entity.class, entityUpdateXml);
	
		//Do update operation
		String updateResponseEntityXml = create(newEntityToUpdateUrl,EntityMarshallingUtils.unmarshal(Entity.class, e)).toString();
	    auth.logout();	    
	    return updateResponseEntityXml;
	 }
	 
	 
	 private synchronized String generateCreateEntityXml(String entity,String field, String value, String testInstanceId, String testId, String owner,  String status,String testsetID) {
		 return "<Entity Type=\""+entity+"\"><Fields>"
		+ "<Field Name=\""+ field+ "\"><Value>" + value + "</Value></Field>"	           
		+ "<Field Name=\""+ "test-id"+ "\"><Value>"+ testId + "</Value></Field>"
		+ "<Field Name=\""+ "owner" + "\"><Value>" + owner + "</Value></Field>"
		+ "<Field Name=\""+ "testcycl-id"+ "\"><Value>"+ testInstanceId + "</Value></Field>"
		+ "<Field Name=\""+ "status" + "\"><Value>" + status + "</Value></Field>"
		+ "<Field Name=\""+ "subtype-id" + "\"><Value>" + "hp.qc.run.VAPI-XP-TEST" + "</Value></Field>"
		+ "<Field Name=\""+ "cycle-id" + "\"><Value>" + testsetID + "</Value></Field>"
		+ "<Field Name=\"" + "test-instance"  + "\"><Value>"+ testInstanceId + "</Value></Field>"
		+ "</Fields></Entity>";
	}	
	    	   
	 public synchronized void CreateTCAttachment(String instanceId,byte[] filedata,String filename)
	 { 
		 try{
			 doAttachment(instanceId.trim(),filedata,filename.trim());
		 }
		 catch(Exception e)
		 {
			 System.out.println("CreateTCAttachment--Exception in Update - Unlock Enitty....."+"InstanceID="+instanceId+"FileData="+filedata+"FileName="+filename);
			 System.out.println("create TC Attachment"+e.getMessage());
			 String newEntityToUpdateUrl = con.buildEntityUrl("run",instanceId);
			 try {
				 boolean unlock = unlock(newEntityToUpdateUrl);
			 }
			 catch (Exception e1) 
			 {
				 // TODO Auto-generated catch block
				 e1.printStackTrace();
			 }
			 e.printStackTrace();
		 }
	 }
	 
	 private synchronized void doAttachment(String instanceId,byte[] filedata,String filename) throws Exception 
	 {
		 GetQCSession();
		
		 //changed
		 String newEntityToUpdateUrl = con.buildEntityUrl("run",instanceId);
		 Map<String, String> requestHeaders = new HashMap<String, String>();
		 requestHeaders.put("Slug", filename);
		 requestHeaders.put("Content-Type", "application/octet-stream");
		 Response response=null;
		 try {
			 response = con.httpPost(newEntityToUpdateUrl + "/attachments", filedata, requestHeaders);
		 } 
		 catch (Exception e) {
			 // TODO Auto-generated catch block
			 System.out.println("Attachment upload fialed:"+e.getMessage());
		 }
	
		 //checkin
		 if (Constants.VERSIONED) {
			 try {
				 boolean checkin = checkin(newEntityToUpdateUrl);
			 } 
			 catch (Exception e) 
			 {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }
		 else {
			 try {
				 boolean unlock = unlock(newEntityToUpdateUrl);
			 } 
			 catch (Exception e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }
		 auth.logout();
	 }
}

