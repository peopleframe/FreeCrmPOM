package com.selenium.framework.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class MyTestDriver {
	//Hello Test
	
	//New
	
	//HI kk
	
	public static void main(String[] args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, IOException {
		Keywords kw = new Keywords();

		TestDriver td = new TestDriver(kw, System.getProperty("user.dir")
				+ "/config/config.properties");
		td.start();
	}
}
