package com.selenium.framework.core;


public class TestResults {

	private String testSuite;
	private String testCaseName;
	private String testCaseDesc;
	private String userStory;
	private String testStartTime;
	private String testEndTime;
	private String testStatus;
	private String isAuto;
	private String priority;

	/**
	 * Parameterized constructor with test results
	 * 
	 * @param suite
	 *            Suite name
	 * @param name
	 *            Test case name
	 * @param desc
	 *            Test case description
	 * @param us
	 *            User story id
	 * @param startTime
	 *            Start time
	 * @param endTime
	 *            End time
	 * @param status
	 *            Status
	 * @param isAuto
	 *            Automated
	 * @param priority
	 *            Priority
	 */
	public TestResults(String suite, String name, String desc, String us, String startTime, String endTime,
			String status, String isAuto, String priority) {
		this.testSuite = suite;
		this.testCaseName = name;
		this.testCaseDesc = desc;
		this.userStory = us;
		this.testStartTime = startTime;
		this.testEndTime = endTime;
		this.testStatus = status;
		this.isAuto = isAuto;
		this.priority = priority;
	}

	/**
	 * Get suite
	 * 
	 * @return Test suite
	 */
	public String getSuite() {
		return testSuite;
	}

	/**
	 * Get test case name
	 * 
	 * @return Test case name
	 */
	public String getTCName() {
		return testCaseName;
	}

	/**
	 * Get test case description
	 * 
	 * @return Test case description
	 */
	public String getTCDesc() {
		return testCaseDesc;
	}

	/**
	 * Get user story id
	 * 
	 * @return User story id
	 */
	public String getUS() {
		return userStory;
	}

	/**
	 * Get test start time
	 * 
	 * @return Test start time
	 */
	public String getStartTime() {
		return testStartTime;
	}

	/**
	 * Get test end time
	 * 
	 * @return Test end time
	 */
	public String getEndTime() {
		return testEndTime;
	}

	/**
	 * Get test status
	 * 
	 * @return Test Status
	 */
	public String getStatus() {
		return testStatus;
	}

	/**
	 * Get Automated
	 * 
	 * @return Automated
	 */
	public String isAutomated() {
		return isAuto;
	}

	/**
	 * Get priority
	 * 
	 * @return Priority
	 */
	public String getPriority() {
		return priority;
	}
}
