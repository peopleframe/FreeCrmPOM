package com.selenium.framework.core;

public class TestCase {
	
	private String testCaseName;
	private String usName;
	private String testCaseDesc;
	private String runMode;
	private String dependency;
	private String status;
	private String priority;

	/**
	 * @param name
	 *            Test Case name
	 * @param us
	 *            Use case name
	 * @param desc
	 *            Test case description
	 * @param runMode
	 *            Run mode
	 * @param dependency
	 *            Dependency
	 * @param status
	 *            Status
	 * @param priority
	 *            Priority
	 */
	public TestCase(String name, String us, String desc, String runMode,
			String dependency, String status, String priority) {
		this.testCaseName = name;
		this.usName = us;
		this.testCaseDesc = desc;
		this.runMode = runMode;
		this.dependency = dependency;
		this.status = status;
		this.priority = priority;
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
	 * Get use case name
	 * 
	 * @return Use case name
	 */
	public String getUSName() {
		return usName;
	}

	/**
	 * Get test case decription
	 * 
	 * @return Test case description
	 */
	public String getTCDesc() {
		return testCaseDesc;
	}

	/**
	 * Get run mode
	 * 
	 * @return Run mode
	 */
	public String getRunMode() {
		return runMode;
	}

	/**
	 * Get dependency
	 * 
	 * @return dependency
	 */
	public String getDependency() {
		return dependency;
	}

	/**
	 * Get status
	 * 
	 * @return Status
	 */
	public String getStatus() {
		return status;
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
