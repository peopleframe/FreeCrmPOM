package com.selenium.framework.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.selenium.framework.utils.XlHelper;


public class SuiteHelper {

	public List<TestSteps> tsList;

	/**
	 * Default constructor
	 */
	public SuiteHelper() {
	}

	/**
	 * Parameterized constructor with test suite
	 * 
	 * @param suiteXLS
	 */
	public SuiteHelper(XlHelper suiteXLS) {
		this.tsList = getSteps(suiteXLS);
	}

	/**
	 * Get test suite
	 * 
	 * @param suiteXLS
	 *            Suite
	 * @return Test suite
	 */
	public LinkedHashMap<String, String> getSuite(XlHelper suiteXLS) {
		String currentTestSuite;
		String runMode;
		LinkedHashMap<String, String> suiteMap = new LinkedHashMap<String, String>();
		for (int currentSuiteID = 2; currentSuiteID <= suiteXLS
				.getRowCount(Constants.TEST_SUITE_SHEET); currentSuiteID++) {
			currentTestSuite = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.Test_Suite_ID,
					currentSuiteID);
			runMode = suiteXLS.getCellData(Constants.TEST_SUITE_SHEET, Constants.RUNMODE, currentSuiteID);
			suiteMap.put(currentTestSuite, runMode);
		}
		return suiteMap;
	}

	/**
	 * Get test case from the suite
	 * 
	 * @param currentTestSuiteXLS
	 *            Current test suite
	 * @return Test case
	 */
	public List<TestCase> getTC(XlHelper currentTestSuiteXLS) {
		String currentTestCaseName;
		String currentUS;
		String runMode;
		String currentTestCaseDesc;
		String dependency;
		String status;
		String priority;
		List<TestCase> tcList = new ArrayList<TestCase>();

		for (int currentTestCaseID = 2; currentTestCaseID <= currentTestSuiteXLS
				.getRowCount("Test Cases"); currentTestCaseID++) {
			currentTestCaseName = currentTestSuiteXLS
					.getCellData(Constants.TEST_CASES_SHEET, Constants.TCID, currentTestCaseID).trim();
			currentUS = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "USID", currentTestCaseID).trim();
			currentTestCaseDesc = currentTestSuiteXLS
					.getCellData(Constants.TEST_CASES_SHEET, "Description", currentTestCaseID).trim();
			runMode = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, Constants.RUNMODE, currentTestCaseID)
					.trim();
			dependency = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Dependency", currentTestCaseID)
					.trim();
			status = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Status", currentTestCaseID).trim();
			priority = currentTestSuiteXLS.getCellData(Constants.TEST_CASES_SHEET, "Priority", currentTestCaseID)
					.trim();
			if (priority == "" || priority == null) {
				priority = "P1";
			}
			TestCase tc = new TestCase(currentTestCaseName, currentUS, currentTestCaseDesc, runMode, dependency, status,
					priority);
			tcList.add(tc);
		}
		return tcList;
	}

	/**
	 * Get data set
	 * 
	 * @param currentTestSuiteXLS
	 *            Current test suite
	 * @param currentTestCaseName
	 *            Current test case name
	 * @return Data set
	 */
	public List<String> getDataSet(XlHelper currentTestSuiteXLS, String currentTestCaseName) {
		String runMode;
		List<String> dataSet = new ArrayList<String>();
		for (int currentTestDataSetID = 2; currentTestDataSetID <= currentTestSuiteXLS
				.getRowCount(currentTestCaseName); currentTestDataSetID++) {
			runMode = currentTestSuiteXLS.getCellData(currentTestCaseName, Constants.RUNMODE, currentTestDataSetID)
					.trim();
			dataSet.add(runMode);
		}
		return dataSet;
	}

	/**
	 * Get test steps from test suite
	 * 
	 * @param currentTestSuiteXLS
	 *            Current test suite
	 * @param currentTestCaseName
	 *            Current test case name
	 * @return Test steps
	 */
	public ArrayList<ArrayList<String>> getTestSteps(XlHelper currentTestSuiteXLS, String currentTestCaseName) {
		ArrayList<ArrayList<String>> testSteps = new ArrayList<ArrayList<String>>();

		for (int currentTestStepID = 2; currentTestStepID <= currentTestSuiteXLS
				.getRowCount(Constants.TEST_STEPS_SHEET); currentTestStepID++) {
			ArrayList<String> data = new ArrayList<String>();
			if (currentTestCaseName.equals(
					currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, currentTestStepID))) {

				data.add(currentTestSuiteXLS
						.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, currentTestStepID).trim());
				data.add(currentTestSuiteXLS
						.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT, currentTestStepID).trim());
				data.add(
						currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, currentTestStepID));
			}
			testSteps.add(data);
		}
		return testSteps;
	}

	public void readTestSteps(XlHelper currentTestSuiteXLS) {
		this.tsList = getSteps(currentTestSuiteXLS);
	}

	/**
	 * Get test steps
	 * 
	 * @param currentTestSuiteXLS
	 * @return Test steps
	 */
	public List<TestSteps> getSteps(XlHelper currentTestSuiteXLS) {
		List<TestSteps> tsList = new ArrayList<TestSteps>();
		TestSteps ts = null;
		String tc = "";
		for (int i = 2; i <= currentTestSuiteXLS.getRowCount(Constants.TEST_STEPS_SHEET); i++) {
			if (currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i).equalsIgnoreCase(tc)) {
				ts.addTCDesc(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DESC, i).trim());
				ts.addKeyWord(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, i).trim());
				ts.addObject(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT, i).trim());
				ts.addData(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, i));
				ts.addExecFlag(
						currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, "Proceed_on_Fail", i).trim());
				ts.addDbFlag(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DB_FLAG, i).trim());
			} else {
				if (i != 2) {
					tsList.add(ts);
				}

				tc = currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.TCID, i);
				ts = new TestSteps(tc);
				ts.addTCDesc(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DESC, i).trim());
				ts.addKeyWord(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.KEYWORD, i).trim());
				ts.addObject(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.OBJECT, i).trim());
				ts.addData(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DATA, i));
				ts.addExecFlag(
						currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, "Proceed_on_Fail", i).trim());
				ts.addDbFlag(currentTestSuiteXLS.getCellData(Constants.TEST_STEPS_SHEET, Constants.DB_FLAG, i).trim());
			}
		}
		tsList.add(ts);
		return tsList;
	}

	/**
	 * Get test case steps
	 * 
	 * @param tcName
	 *            Test Case Name
	 * @return Test Steps
	 */
	public TestSteps getTCSteps(String tcName) {
		TestSteps ts = null;
		for (TestSteps t : tsList) {
			if (t.getTCName().equals(tcName)) {
				return t;
			}
		}
		return ts;
	}
}
