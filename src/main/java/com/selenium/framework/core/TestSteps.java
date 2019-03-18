package com.selenium.framework.core;

import java.util.ArrayList;
import java.util.List;


public class TestSteps {
	
	private String tcName;
	private List<String> tcDesc = new ArrayList<String>();
	private List<String> keywords = new ArrayList<String>();
	private List<String> objects = new ArrayList<String>();
	private List<String> data = new ArrayList<String>();
	private List<String> execFlag = new ArrayList<String>();
	private List<String> dbFlag = new ArrayList<String>();

	public TestSteps() {

	}

	/**
	 * Assigning test case name
	 * 
	 * @param name
	 */
	public TestSteps(String name) {
		this.tcName = name;
	}

	/**
	 * Adding test case description
	 * 
	 * @param desc
	 */
	public void addTCDesc(String desc) {
		this.tcDesc.add(desc);
	}

	/**
	 * Adding keyword
	 * 
	 * @param keyword
	 */
	public void addKeyWord(String keyword) {
		this.keywords.add(keyword);
	}

	/**
	 * Adding object
	 * 
	 * @param object
	 */
	public void addObject(String object) {
		this.objects.add(object);
	}

	/**
	 * Adding database flag
	 * 
	 * @param flag
	 */
	public void addDbFlag(String flag) {
		this.dbFlag.add(flag);
	}

	/**
	 * Adding data
	 * 
	 * @param data
	 */
	public void addData(String data) {
		this.data.add(data);
	}

	/**
	 * Adding run mode
	 * 
	 * @param flag
	 */
	public void addExecFlag(String flag) {
		this.execFlag.add(flag);
	}

	/**
	 * Get test case name
	 * 
	 * @return Test case name
	 */
	public String getTCName() {
		return this.tcName;
	}

	/**
	 * Get test case description
	 * 
	 * @return tcDesc
	 */
	public List<String> getTCDesc() {
		return this.tcDesc;
	}

	/**
	 * Get keyword
	 * 
	 * @return keywords
	 */
	public List<String> getKeywords() {
		return this.keywords;
	}

	/**
	 * Get objects
	 * 
	 * @return objects
	 */
	public List<String> getObjects() {
		return this.objects;
	}

	/**
	 * Get data
	 * 
	 * @return data
	 */
	public List<String> getData() {
		return this.data;
	}

	/**
	 * Get run mode
	 * 
	 * @return execFlag
	 */
	public List<String> getExecFlag() {
		return this.execFlag;
	}

	/**
	 * Get database flag
	 * 
	 * @return dbFlag
	 */
	public List<String> getDbFlag() {
		return this.dbFlag;
	}
}
