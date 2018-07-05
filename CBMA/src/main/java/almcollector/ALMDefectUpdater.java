package almcollector;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import org.testng.annotations.Test;

public class ALMDefectUpdater {


	static Constants cons = Constants.getInstance();
	static EntityUpdater restObj = new EntityUpdater(cons.DOMAIN, cons.PROJECT);
	private static final int PRETTY_PRINT_INDENT_FACTOR = 4;
	public synchronized void connect() {
		try {
			restObj.loginCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void disconnect() {
		try {
			restObj.logoutCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public synchronized JSONArray getProjects() {
		String responseXml = restObj.getAlmProjects("defect", "user-template-01");
		JSONObject JsonObj = XMLtoJSON(responseXml);
		JSONObject groupHeaders = JsonObj.getJSONObject("GroupByHeaders");
		JSONArray arr = groupHeaders.getJSONArray("GroupByHeader");
		return arr;
	}

	@Test
	public void getDefectID() {
		String TCName = "TC001_MyAdmin_Create_Profile_OKC_Reset_Pssword_1519878";
		String TestSuiteID = "146942";
		int totalResults;
				
		connect();
		String responseXml = restObj.getEntity("defect-links", null,
				"second-endpoint-id["+TestSuiteID+"]" + ";" + "second-endpoint-type[test-set]");

		JSONObject xmlJSONObj = XML.toJSONObject(responseXml);
		String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		JSONObject prettyJson = new JSONObject(jsonPrettyPrintString);
		
		for (int i = 0; i < prettyJson.length(); i++) {
			JSONObject child1 = (JSONObject) prettyJson.get("Entities");
			if (child1.length() != 0) {
				totalResults = (int) child1.get("TotalResults");
				if (totalResults > 1) {
					JSONArray child2 = child1.getJSONArray("Entity");
					if (child2.length() != 0) {
						for (int j = 0; j < child2.length(); j++) {
							JSONObject fieldprocess = (JSONObject) child2.getJSONObject(j).get("Fields");
							JSONArray element = fieldprocess.getJSONArray("Field");
							if (element.length() != 0) {
								if(element.get(3).toString().contains(TCName)){
									String defectID = element.get(2).toString();
									String[] defectID1 = defectID.split(",");
									String[] defectID2 = defectID1[0].split(":");
									defectID = defectID2[1];
									System.out.println(defectID);
									break;
								}
							}							
						}
					}
				} else if (totalResults == 1) {

					// Code to get the total test count
					JSONObject testEntities = (JSONObject) prettyJson.get("Entities");
					JSONObject testEntity = (JSONObject) testEntities.getJSONObject("Entity");
					JSONObject testFields = (JSONObject) testEntity.getJSONObject("Fields");

					JSONArray testField = testFields.getJSONArray("Field");
					if (testField.length() != 0) {
						if(testField.get(3).toString().contains(TCName)){
							String defectID = testField.get(2).toString();
							String[] defectID1 = defectID.split(",");
							String[] defectID2 = defectID1[0].split(":");
							defectID = defectID2[1];
							System.out.println(defectID);
							break;
						}
					}
					
				} else {
					
				}
			}
		}
		
		disconnect();
		
		
	}
	
	// Converting XML data to JSON
	public synchronized JSONObject XMLtoJSON(String xml) {
		JSONObject xmlJSONObj = null;
		try {
			xmlJSONObj = XML.toJSONObject(xml);
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return xmlJSONObj;
	}

}
