package com.selenium.framework.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.selenium.framework.utils.PropertiesUtil;
import com.selenium.framework.utils.XlHelper;

public class TestDriver {

	public static Logger APP_LOGS;
	public XlHelper suiteXLS;
	public int currentSuiteID;
	public String currentTestSuite;

	public static XlHelper currentTestSuiteXLS;
	public static int currentTestCaseID;
	public static String currentTestCaseName;
	public static int currentTestStepID;
	public static String currentKeyword;
	public static String stepDescription;
	public static int currentTestDataSetID = 2;
	public static Method method[];
	public static Method capturescreenShot_method;

	public static Keywords keywords;
	public static String keyword_execution_result;
	public static ArrayList<String> resultSet;
	public static String data;
	public static String object;

	public static Properties CONFIG;
	public static Properties UIMap;

	public static String dataBaseFlag = null;

	/**
	 * Default constructor
	 */
	public TestDriver() {
	}

	/**
	 * Parameterized constructor with keywords
	 * 
	 * @param sk
	 *            Keywords
	 */
	public TestDriver(Keywords sk) throws NoSuchMethodException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, IOException {
		keywords = sk;
		method = sk.getClass().getMethods();
		capturescreenShot_method = sk.getClass().getMethod("captureScreenshot",
				String.class, String.class);
	}

	/**
	 * Parameterized constructor with keywords and config
	 * 
	 * @param sk
	 *            Keywords
	 * @param configFile
	 *            Config file
	 */
	public TestDriver(Keywords sk, String configFile)
			throws NoSuchMethodException, SecurityException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, IOException {
		keywords = sk;
		method = sk.getClass().getMethods();
		FileInputStream fs = new FileInputStream(configFile);
		CONFIG = new Properties();
		CONFIG.load(fs);
	}

	/**
	 * Setup
	 */
	public void setUp() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException,
			NoSuchMethodException, SecurityException {
		APP_LOGS = Logger.getLogger("devpinoyLogger");
		APP_LOGS.debug("Hello");
		APP_LOGS.debug("Properties loaded. Starting testing");

		FileInputStream fs;

		if (CONFIG == null) {
			fs = new FileInputStream(System.getProperty("user.dir")
					+ "/config/selenium.config.properties");
			CONFIG = new Properties();
			CONFIG.load(fs);
		}
		String UIMapPath = "";
		String ResultsPath = "";

		if (CONFIG.getProperty("UIMapPath") == null
				|| CONFIG.getProperty("TestSuitePath") == null
				|| CONFIG.getProperty("ResultsPath") == null) {
			APP_LOGS.debug("config properties like UIMapPath, TestSuitePath, ResultsPath are not defined .. please check your config.properties");
		System.exit(1);
		} else {
			UIMapPath = CONFIG.getProperty("UIMapPath").trim();
			ResultsPath = CONFIG.getProperty("ResultsPath").trim();
		}

		if (!UIMapPath.endsWith("/") || !UIMapPath.endsWith("//")) {
			UIMapPath = UIMapPath + "/";
		}
		fs = new FileInputStream(UIMapPath + "UIMap.properties");
		UIMap = new Properties();
		UIMap.load(fs);

	
		fs.close();
	}

	/**
	 * Start
	 */
	public void start() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {
		setUp();
		String testStatus = "";
		String startTime = "";
		String automation = "";
		APP_LOGS.debug("Intialize Suite xlsx");
		SuiteHelper suiteHelper = new SuiteHelper();
		String suitePath = CONFIG.getProperty("TestSuitePath").trim();
		if (!suitePath.endsWith("/") || !suitePath.endsWith("//")) {
			suitePath = suitePath + "/";
		}
		suiteXLS = new XlHelper(suitePath + "Suite.xlsx");
		if (!suiteXLS.verifyMainSuiteFormat()) {
			APP_LOGS.debug("Failed to parse Suite.xlsx... please check the column headings or sheet names");
		return;
		}

		HashMap<String, String> map = suiteHelper.getSuite(suiteXLS);
		for (String key : map.keySet()) {
			currentTestSuite = key;
			if (map.get(key).equalsIgnoreCase("Y")) {
				currentTestSuiteXLS = new XlHelper(suitePath + currentTestSuite
						+ ".xlsx");
				if (!currentTestSuiteXLS.verifyTestSuiteFormat()) {
					APP_LOGS.debug("Failed to parse "
							+ currentTestSuite
							+ ".xlsx... please check the column headings or sheet names");
						return;
				}
				List<TestCase> tcList = suiteHelper.getTC(currentTestSuiteXLS);
				for (TestCase tc : tcList) {
					currentTestCaseName = tc.getTCName();
					if (tc.getRunMode().equalsIgnoreCase("Y")) {
						suiteHelper.readTestSteps(currentTestSuiteXLS);
						automation = "Y";
						if (!testStatus.equals(Constants.KEYWORD_PASS)
								&& tc.getDependency().equalsIgnoreCase("y")) {
							
							continue;
						}
						APP_LOGS.debug("Executing the test case -> "
								+ currentTestCaseName);
						TestSteps ts = suiteHelper
								.getTCSteps(currentTestCaseName);
						if (ts == null) {
							
							continue;
						}

						if (currentTestSuiteXLS
								.isSheetExist(currentTestCaseName)) {
							List<String> ds = suiteHelper.getDataSet(
									currentTestSuiteXLS, currentTestCaseName);
							currentTestDataSetID = 2;
							String tcName = "";
							for (String s : ds) {
								
								resultSet = new ArrayList<String>();
								APP_LOGS.debug("Iteration number "
										+ (currentTestDataSetID - 1));
								tcName = currentTestCaseName + "_"
										+ (currentTestDataSetID - 1);
								if (s.equals("Y")) {
									testStatus = executeKeywords(ts);
								
								} else {
									
								}
								currentTestDataSetID = currentTestDataSetID + 1;
							}
						} else {
							
							currentTestDataSetID = 2;
							resultSet = new ArrayList<String>();
							testStatus = executeKeywords(ts);// no data with the
																// test
							
						}
					} else {
						APP_LOGS.debug("Skipping the test "
								+ currentTestCaseName);
						testStatus = tc.getStatus();
						if (testStatus.equals("") || testStatus.equals(null)) {
							testStatus = "Skip";
							automation = "Y";
						} else {
							automation = "N";
						}
						// report skipped
						APP_LOGS.debug("***********************************"
								+ currentTestCaseName + " --- " + testStatus);
					
					}
				}
			}
		}
	}

	/**
	 * Execute keywords
	 * 
	 * @param ts
	 *            Test steps
	 * @return Test result
	 */
	private String executeKeywords(TestSteps ts) {
		int rc = 0;
		String exeOnFailureFlag = "";
		String results = "Fail";
		String fileName;
		for (int j = 0; j < ts.getKeywords().size(); j++) {
			keyword_execution_result = "Fail --could be some exceptions please check logs";
			data = ts.getData().get(j);
			dataBaseFlag = ts.getDbFlag().get(j);
			exeOnFailureFlag = ts.getExecFlag().get(j);
			if (data.startsWith(Constants.DATA_START_COL)) {
				// read actual data value from the corresponding column
				data = currentTestSuiteXLS.getCellData(currentTestCaseName,
						data.split(Constants.DATA_SPLIT)[1],
						currentTestDataSetID);
			} else if (data.startsWith(Constants.CONFIG)) {
				// read actual data value from config.properties
				if (data.split(Constants.DATA_SPLIT)[1].equals("testSiteURL")) {
					
					data = CONFIG.getProperty(
							data.split(Constants.DATA_SPLIT)[1]).trim();
				}
			} else if (data.startsWith(Constants.UIMAP)) {
				data = UIMap.getProperty(data.split(Constants.DATA_SPLIT)[1])
						.trim();
				// }else if(data.contains("select") && data.contains("from")){
						}
			object = ts.getObjects().get(j);
			currentKeyword = ts.getKeywords().get(j);
			stepDescription = ts.getTCDesc().get(j);
			APP_LOGS.debug(currentKeyword);
			System.out.println("Executing Step");
			System.out.println(ts.getTCName() + "--" + ts.getKeywords().get(j)
					+ "--" + ts.getObjects().get(j) + "--"
					+ ts.getData().get(j));
			try {
				keyword_execution_result = (String) keywords.getClass()
						.getMethod(currentKeyword, String.class, String.class)
						.invoke(keywords, object, data);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				keyword_execution_result = "Fail -- Failed to execute keyword";
				APP_LOGS.error("Fail -- Failed to execute keyword " + e);
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				keyword_execution_result = "Fail -- keyword not found, please check typos";
				APP_LOGS.error("Fail -- keyword not found, please check typos \n"
						+ e);
				// e.printStackTrace();
			}
			APP_LOGS.debug(keyword_execution_result);
			resultSet.add(keyword_execution_result);
			results = keyword_execution_result;
			System.out.println(results);
			String tcName = currentTestCaseName;
			if (tcName.indexOf("|") != -1) {
				tcName = currentTestCaseName.split("\\|")[1];
			}
			fileName = currentTestSuite + "_" + tcName + "_TS" + j + "_"
					+ (currentTestDataSetID - 1);
			if (CONFIG.getProperty("screenshot_everystep").trim()
					.equalsIgnoreCase("y")
					|| !keyword_execution_result.equals(Constants.KEYWORD_PASS)) {
				if (!ts.getKeywords().get(j).equals("openBrowser")
						&& !ts.getKeywords().get(j).equals("closeBrowser")
						&& !ts.getKeywords().get(j).equals("dataSetup")
						&& !ts.getKeywords().get(j).equals("runJMeter")) {
					try {
						capturescreenShot_method.invoke(keywords, fileName,
								keyword_execution_result);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if (!keyword_execution_result.equals(Constants.KEYWORD_PASS)) {
				results = "Fail";
				rc = 1;
				if (!exeOnFailureFlag.equalsIgnoreCase("y")) {
					return results;
				}
			}
		}

		// return keyword_execution_result;
		if (rc != 0) {
			results = "Fail";
		}
		return results;
	}
}
