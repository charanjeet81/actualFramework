package supportlibraries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


import businesscomponents.UtilTools;

public class SeleniumGridDocker extends ResultSummaryManager {

	public static ArrayList<String> ContainersCreated = new ArrayList<String>();
	public static ArrayList<Process> VncViewsCreated = new ArrayList<Process>();
	public static String host;
	public static String user;
	public static String password;
	public static String port;
	public static String vncpwd;
	public static String debug;

	public static void getVncViewer(String Port) {

		try {
			String vnccmd = "java -jar " + System.getProperty("user.dir") + "/src/main/resources/VncViewer.jar HOST "
					+ host + " port " + Port + " password " + vncpwd + " scaling factor 10";
			Process proc = Runtime.getRuntime().exec(vnccmd);
			VncViewsCreated.add(proc);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static void cleanupVNCViewers() {
		// System.out.println("CleanupVNCViewers entered");
		try {
			for (int i = 0; i <= VncViewsCreated.size(); i++) {
				System.out.println();
				VncViewsCreated.get(i).destroy();
			}
		} catch (Exception e) {
			System.out.println("Exception in VNC Views Clean up" + e.getStackTrace());
		}
		// System.out.println("CleanupVNCViewers exit");
	}

	public static String createDynamicPort() {
		String port = null;
		try {
			for (int i = 0; i <= 9999; i++) {
				long min = (long) Math.pow(10, 4 - 1);
				port = Long.toString(ThreadLocalRandom.current().nextLong(min, min * 10));

				String PortsData = connectAndExecute(user, host, password, "netstat -vatn");
				if (!PortsData.contains(port)) {
					break;
				}
			}
			System.out.println("Port: " + port);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return port;
	}

	public static String connectAndExecute(String user, String host, String password, String command) {
		String CommandOutput = null;
		try {

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();

			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			// System.out.println("Connected");

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();
			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					// System.out.print(new String(tmp, 0, i));
					CommandOutput = new String(tmp, 0, i);
				}

				if (channel.isClosed()) {
					// System.out.println("exit-status: " +
					// channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			channel.disconnect();
			session.disconnect();
			// System.out.println("DONE");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return CommandOutput;

	}

	public static void createSeleniumGridHub() {
		try {
			host = properties.getProperty("host");
			user = properties.getProperty("user");
			password = properties.getProperty("password");
			port = properties.getProperty("port");
			debug = properties.getProperty("debug");
			vncpwd = properties.getProperty("vncpwd");

			// Create Selenium Grid Hub
			String hubport = createDynamicPort();
			String hubcreationcmd = "docker run -d -p " + hubport + ":4444 selenium/hub:2.53.1";
			String hubContainerID = connectAndExecute(user, host, password, hubcreationcmd);
			properties.setProperty("HubContainerID", hubContainerID);

			// Check Selenium Grid Hub is created
			if (connectAndExecute(user, host, password, "docker ps").contains("0.0.0.0:" + hubport + "->4444/tcp")) {
				System.out.println("Selenium Grid HUB is created successfully");
			}

			String GridURL = "http://" + host + ":" + hubport + "/wd/hub";
			String GridConsole = "http://" + host + ":" + hubport + "/grid/console";
			properties.setProperty("RemoteUrl", GridURL);
			properties.setProperty("GridHubUrl", GridURL);
			properties.setProperty("GridConsole", GridConsole);
			System.out.println("Grid Hub URL : " + properties.getProperty("RemoteUrl"));
			System.out.println("Grid Hub Console : " + properties.getProperty("GridConsole"));
			System.out.println("Grid Hub Container : " + properties.setProperty("HubContainerID", hubContainerID));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in Creation of Selenium Grid HUB");
		}
	}

	public static void cleanupGridHubNode() {
		try {

			String stopcommand = "docker stop " + properties.getProperty("HubContainerID");
			connectAndExecute(user, host, password, stopcommand);

			String removecommand = "docker rm " + properties.getProperty("HubContainerID");
			connectAndExecute(user, host, password, removecommand);

			// Validate Deletion
			String pscommand = "docker ps";
			connectAndExecute(user, host, password, pscommand);

			if (!connectAndExecute(user, host, password, pscommand)
					.contains(properties.getProperty("HubContainerID"))) {
				System.out.println("Hub Container is closed successfully");
			} else {
				System.out.println("Hub Container is not closed successfully");
			}

		} catch (Exception e) {
			System.out.println("Exception in Containers Clean");
		}
	}

	public static String createChromeNode() {
		String gc = "";
		try {
			// Finalize the Chrome Browser Version
			String chromeversion = properties.getProperty("chromeversion").trim();
			if (chromeversion.equals(""))
			{
				chromeversion = "58";
			}
			// Create and Link Selenium Grid Chrome node to Hub created above
			String gcDynamicPort = "";
			String gccommand = "";
			if (debug.equals("true")) {
				gcDynamicPort = createDynamicPort();
				gccommand = " docker run -d -P -p " + gcDynamicPort + ":5900 --link "
						+ properties.getProperty("HubContainerID").trim() + ":hub nodechrome-debug-v" + chromeversion;
				/*gccommand = " docker run -d -P -p " + gcDynamicPort + ":5900 --link "
						+ properties.getProperty("HubContainerID").trim() + ":hub nodechrome-debug-proxy";*/
			} else {
				gccommand = " docker run -d -P --link " + properties.getProperty("HubContainerID").trim()
						+ ":hub nodechrome-v" + chromeversion;
			}

			System.out.println("gccommand" + gccommand);
			gc = connectAndExecute(user, host, password, gccommand);
			properties.setProperty("GCContainerID", gc);
			System.out.println(gc);
			ContainersCreated.add(gc);

			// Open VNC Viewer if debug mode is ON
			if (debug.equals("true")) {
				getVncViewer(gcDynamicPort);
				// Check Selenium Chrome Node with VNC is created
				if (connectAndExecute(user, host, password, "docker ps")
						.contains("0.0.0.0:" + gcDynamicPort + "->5900/tcp")) {
					System.out.println("Selenium Chrome Node is created successfully");
				}
			}

			// Add host file if it is a Dark Silo Environment
			String env = UtilTools.getEnv().trim();
			if (env.equals("QA03")||env.equals("QA02")||env.equals("QA01")) {
				updateHostFiles(env, "gc");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in Creation of Chrome Node");
		}
		return gc;
	}

	public static String createFirefoxNode() {
		String ff = null;
		try {
			// Finalize the Firefox Browser Version
			String firefoxversion = properties.getProperty("firefoxversion").trim();
			if(firefoxversion.equals(""))
			{
				firefoxversion = "47";
			}

			// Create and Link Selenium Grid Firefox node to Hub created above
			String ffDynamicPort = "";
			String ffcommand = "";
			if (debug.equals("true")) {
				ffDynamicPort = createDynamicPort();
				ffcommand = "docker run -d -P -p " + ffDynamicPort + ":5900 --link "
						+ properties.getProperty("HubContainerID").trim() + ":hub nodefirefox-debug-v" + firefoxversion;
			} else {
				ffcommand = " docker run -d -P --link " + properties.getProperty("HubContainerID").trim()
						+ ":hub nodefirefox-v" + firefoxversion;
			}

			System.out.println("ffcommand" + ffcommand);
			ff = connectAndExecute(user, host, password, ffcommand);
			properties.setProperty("FFContainerID", ff);
			System.out.println(ff);
			ContainersCreated.add(ff);

			// Open VNC Viewer if debug mode is ON
			if (debug.equals("true")) {
				getVncViewer(ffDynamicPort);
				// Check Selenium Firefox Node with VNC is created
				if (connectAndExecute(user, host, password, "docker ps")
						.contains("0.0.0.0:" + ffDynamicPort + "->5900/tcp")) {
					System.out.println("Selenium Chrome Node is created successfully");
				}
			}

			// Add host file if it is a Dark Silo Environment
			String env = UtilTools.getEnv().trim();
			if (env.equals("QA03")||env.equals("QA02")||env.equals("QA01")) {
				updateHostFiles(env, "ff");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in Creation of Firefox Node=="+e.getMessage());
		}
		return ff;
	}

	public static void cleanupNodes() {
		try {

			for (int i = 0; i < ContainersCreated.size(); i++) {

				String stopcommand = "docker stop " + ContainersCreated.get(i);
				connectAndExecute(user, host, password, stopcommand);
				System.out.println("Stopped Nodes : " + ContainersCreated.get(i));

				String removecommand = "docker rm " + ContainersCreated.get(i);
				connectAndExecute(user, host, password, removecommand);
				System.out.println("Removed Nodes : " + ContainersCreated.get(i));
			}

		} catch (Exception e) {
			System.out.println("Exception in Nodes Clean");
		}
		// System.out.println("Cleanup Nodes exit");
	}

	public static void updateHostFiles(String env, String browser) {
		String fileName = System.getProperty("user.dir") + "\\" + env + "_Hosts.txt";
		System.out.println("Host File Name"+fileName);
		System.out.println("Host file update started");
        List<String> list = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
              fr = new FileReader(fileName);
              br = new BufferedReader(fr);

              String sCurrentLine;
              while ((sCurrentLine = br.readLine()) != null) {
                    list.add(sCurrentLine);
                    System.out.println("Host file current line"+ sCurrentLine);
              }
              String[] mainstring = null;
              String main = "";
              for (int i = 0; i < list.size(); i++) {
                    String[] value = list.get(i).split("#");
                    mainstring = value[0].split(" ");
                    for (int j = 1; j < mainstring.length; j++) {
                          main = main + "sudo sh modifyhostfile.sh add " + mainstring[j] + " " + mainstring[0];
                          if (j != mainstring.length - 1) {
                                main = main + ";";
                          }
                    }
                    main = main + ";";
              }
              main = main.replaceFirst(".$", "");
              System.out.println("Host file main content"+ main.trim());
              String cid = "";
              if (browser.equals("ff")) {
                    cid = properties.getProperty("FFContainerID").trim();
              } else if (browser.equals("gc")) {
                    cid = properties.getProperty("GCContainerID").trim();
              }
              String hfu = "docker exec " + cid + " /bin/sh -c " + "'cd /opt/selenium;" + main.trim() + "'";
              System.out.println(hfu);
              connectAndExecute(user, host, password, hfu);
        } catch (IOException e) {
              e.printStackTrace();
        }
        finally{
              try {
                    fr.close();
                    br.close();
              } catch (IOException e) {
                    e.printStackTrace();
              }
              
        }

	}

	public static void getBrowser_OS_Version(WebDriver driver) {
		// Get Browser name and version.
		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = caps.getBrowserName();
		String browserVersion = caps.getVersion();

		// Get OS name.
		String os = System.getProperty("os.name").toLowerCase();

		System.out.println(os + " " + browserName + " " + browserVersion);
	}

	public static void checkSystemResources() throws Exception {
		int count = 0;
		String hostsytems = properties.getProperty("host");
		String[]hosts = hostsytems.split(",");
		System.out.println(hosts.length +" VMs identified for Test Execution.Checking for the resource availability");
		
		for(int j = 1; j<= 10 ; j++){
			
				for (int i = 1; i <= hosts.length; i++) {
					String nodestest = SeleniumGridDocker.connectAndExecute(properties.getProperty("user"),
							hosts[i-1], properties.getProperty("password"), "docker ps -a | wc -l");
					nodestest = StringUtils.removeEnd(nodestest, "\n");
					int result = Integer.parseInt(nodestest) - 1;
					int chromenodes = Integer.parseInt(properties.getProperty("ChromeNodes"));
					int firefoxnodes = Integer.parseInt(properties.getProperty("FirefoxNodes"));
					result = result + chromenodes + firefoxnodes + 1;
					if (result <=65) {
						System.out.println("System "+ hosts[i-1] +" is been assigned for your Test Execution");
						properties.setProperty("host", hosts[i-1]);
						count = 1;
						break;
					}else{
						System.out.println("System "+ hosts[i-1] +" is not free. Looking for Other Systems.. Kindly Hold On");
		 			}
				}
		
		if (count == 1) {
			break;
		}
		System.out.println("All Systems Resources are full. Please wait for 5 minutes or terminate your execution and try again later");
		Thread.sleep(300 *1000);
	}
		if (count == 0) {
			System.out.println("System is waiting for more than an hour. Please retry again some other time..Good Bye");
			System.exit(0);
		}
	}
}
