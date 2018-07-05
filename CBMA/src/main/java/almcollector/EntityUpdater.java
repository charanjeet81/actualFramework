package almcollector;

import java.util.HashMap;
import java.util.Map;

public class EntityUpdater {
	String domain;
	String project;
	RestConnector con;
	AuthenticateLoginLogout auth;

	public EntityUpdater(String almDomain, String almProject) {
		this.domain = almDomain.trim();
		this.project = almProject.trim();

		this.con = RestConnector.getInstance().init(new HashMap<String, String>(), Constants.HOST, Constants.PORT,
				this.domain, this.project);
		this.auth = new AuthenticateLoginLogout();
	}

	/**
	 * The Method is to Login to ALM via REST calls. Should be invoked to
	 * establish   the connection/session, before calling any other method in the
	 * class to update the data in ALM.
	 * 
	 * @throws Exception
	 */
	public void loginCall() throws Exception {

		boolean isLoggedIn = auth.login(Constants.USERNAME, Constants.PASSWORD);
		if (!isLoggedIn)
			System.out.println("ALM Error: Unable to Login.");
	}

	/**
	 * The Method is to Logout from ALM via REST calls.
	 * 
	 * @throws Exception
	 */
	public void logoutCall() throws Exception {

		auth.logout();
	}

	public synchronized void GetQCSession() {
		String qcsessionurl = con.buildUrl("qcbin/rest/site-session");
		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/xml");
		try {
			Response resp = con.httpPost(qcsessionurl, null, requestHeaders);
			// System.out.println("Response is:"+resp);
			con.updateCookies(resp);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Could not connect to QC:" + e.getMessage());
		}
	}

	public String getAlmProjects(String entity, String filed) {
		String responseProjXml = null;
		try {

			GetQCSession();
			String newGroupingUrl = con.buildEntityCollectionUrl(entity, filed);
			responseProjXml = fetch(newGroupingUrl, null).toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseProjXml;
	}

	public String getEntity(String entity, String iD, String query) {

		String responseXml = null;

		try {
			GetQCSession();
			String newEntityToUpdateUrl = con.buildEntityUrl(entity, iD, query);
			responseXml = fetch(newEntityToUpdateUrl, null).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseXml;

	}

	private Response fetch(String entityUrl, String updatedEntityXml) throws Exception {

		Map<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("Content-Type", "application/xml");
		requestHeaders.put("Accept", "application/xml");

		Response put = con.httpGet(entityUrl, updatedEntityXml, requestHeaders);
		return put;
	}

}
