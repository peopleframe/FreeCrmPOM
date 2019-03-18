package com.selenium.framework.core;

import static com.selenium.framework.core.TestDriver.APP_LOGS;
import static com.selenium.framework.core.TestDriver.CONFIG;
import static com.selenium.framework.core.TestDriver.UIMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Keywords {

	public WebDriver driver;

	public EventFiringWebDriver e_driver;
	public Wait<WebDriver> driverWait;
	public long implicitWaitTime;
	public ArrayList<ArrayList<String>> dbResult = new ArrayList<ArrayList<String>>();
	public HashMap<Integer, List<String>> barChartData;
	public boolean demoMode = false;
	public List<String> storeData = null;

	public static List<Map<Integer, String>> table = new ArrayList<Map<Integer, String>>();
	public Hashtable<String, String> hashTable = new Hashtable<String, String>();

	public String getTextValue;

	public Keywords() {
	}

	/**
	 * This is used to perform scroll down operations
	 * 
	 * @param object
	 * @param data
	 * @return pass or fail
	 */
	public String scrollDown(String object, String data) {
		APP_LOGS.debug("Scrolling down the side bar");
		try {
			e_driver = new EventFiringWebDriver(driver);
			e_driver.executeScript("scroll(0," + data + ")");

		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to scroll";
		}
		return Constants.KEYWORD_PASS;
	}

	/**
	 * Open browser
	 * 
	 * @param object
	 * @param data
	 *            Browser name
	 * @return pass or fail
	 */
	public String openBrowser(String object, String data) {
		APP_LOGS.debug("Opening browser");
		String demoProp = CONFIG.getProperty("demoMode");
		if (demoProp != null) {
			if (demoProp.equalsIgnoreCase("y")) {
				demoMode = true;
			}
		}
		String prof = CONFIG.getProperty("firefoxProfile");
		if (data.equalsIgnoreCase("firefox")) {
			if (prof == null) {
				driver = new FirefoxDriver();
			} else {
				File profileDirectory = new File(prof);
				FirefoxProfile profile = new FirefoxProfile(profileDirectory);
				DesiredCapabilities capability = DesiredCapabilities.firefox();
				capability.setCapability(FirefoxDriver.PROFILE, profile);

				driver = new FirefoxDriver(capability);
			}
		} else if (data.equalsIgnoreCase("ie")) {
			String iepath = CONFIG.getProperty("iedriverPath");
			if (!iepath.endsWith("/") || !iepath.endsWith("//")) {
				iepath = iepath + "/";
			}
			File file = new File(iepath + "IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());

			driver = new InternetExplorerDriver();
		} else if (data.equalsIgnoreCase("chrome")) {
			String chromepath = CONFIG.getProperty("chromedriverPath");
			if (!chromepath.endsWith("/") || !chromepath.endsWith("//")) {
				chromepath = chromepath + "/";
			}
			File file;
			String os = System.getProperty("os.name");
			if (os.startsWith("Windows")) {
				file = new File(chromepath + "chromedriver.exe");
			} else {
				file = new File(chromepath + "chromedriver");
			}

			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

			driver = new ChromeDriver();
		}
		implicitWaitTime = Long.parseLong(CONFIG.getProperty("implicitwait"));


		return Constants.KEYWORD_PASS;
	}

	/**
	 * Navigate to url
	 * 
	 * @param object
	 * @param data
	 *            URL
	 * @return pass or fail
	 */
	public String navigate(String object, String data) {
		APP_LOGS.debug("Navigating to URL");
		try {
			driver.navigate().to(data);
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " -- Not able to navigate";
		}
		return Constants.KEYWORD_PASS;
	}

	/**
	 * Write input in textbox
	 * 
	 * @param object
	 *            Textbox(WebElement) Id
	 * @param data
	 *            User input
	 * @return pass or fail
	 */
	public String sendKeys(String object, String data) {
		String locator = UIMap.getProperty(object.split(",")[0]);
		APP_LOGS.debug("Writing in text box:: Object is " + locator + " Data is " + data);
		WebElement element = getWebElement(object, data);
		if (element != null) {
			element.clear();
			element.sendKeys(data);
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(Constants.KEYWORD_FAIL + " -- Unable to write  -- Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Unable to write  -- Element " + locator + " Not Found";
		}
	}

	protected By getElementByLocator(String locator) {
		By by = null;

		if (locator.startsWith("//"))
			by = By.xpath(locator);
		else if (locator.startsWith("css:"))
			by = By.cssSelector(locator.substring(4));
		else
			by = By.id(locator);

		return by;
	}

	/**
	 * 
	 * @param object
	 * 
	 * @param data
	 * 
	 * @return pass or fail
	 */
	public WebElement getWebElement(final String object, String data) {
		driverWait = new WebDriverWait(driver, 25).ignoring(org.openqa.selenium.TimeoutException.class);
		String locator = "";
		boolean flag;
		WebElement element = null;
		locator = UIMap.getProperty(object.split(",")[0]);
		if (locator == null) {
			APP_LOGS.debug("Object property not defined in UIMap or not passed in XL");
			return element;
		}
		APP_LOGS.debug("Locator = " + locator + " Data " + data);
		if (!data.equals("")) {
			locator = locator.replace("DATA", data);
			APP_LOGS.debug("Locator = " + locator + " Data " + data);
		}
		final By by = getElementByLocator(locator);
		if (object.contains(",")) {
			try {
				flag = driverWait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						APP_LOGS.debug("Waiting for Elements to completely load ...");
						APP_LOGS.debug("element = "
								+ d.findElements(by).get(Integer.parseInt(object.split(",")[1])).getText());
						return d.findElements(by).get(Integer.parseInt(object.split(",")[1])).isDisplayed();
					}
				});
			} catch (org.openqa.selenium.TimeoutException ex) {
				APP_LOGS.debug("Failed to Load Element after 5 min ..." + ex);
				return null;
			}
			if (flag) {
				element = driver.findElements(by).get(Integer.parseInt(object.split(",")[1]));
			}
		} else {
			try {
				flag = driverWait.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						APP_LOGS.debug("Waiting for Elements to completely load ...");
						return d.findElement(by).isDisplayed();
					}
				});
			} catch (org.openqa.selenium.TimeoutException ex) {
				APP_LOGS.debug("Failed to Load Element after 5 min ..." + ex);
				return null;
			}
			if (flag) {
				element = driver.findElement(by);

			}
		}
		return element;
	}

	/**
	 * @param locator
	 * @return pass or fail
	 */
	protected List<WebElement> getWebElements(String locator) {

		final By by = getElementByLocator(locator);
		driverWait = new WebDriverWait(driver, implicitWaitTime).withTimeout(15, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(org.openqa.selenium.TimeoutException.class);
		List<WebElement> eleList = null;
		try {
			eleList = driverWait.until(new ExpectedCondition<List<WebElement>>() {
				public List<WebElement> apply(WebDriver d) {
					APP_LOGS.debug("Waiting for Elements to completely load ...");
					try {
						return d.findElements(by);
					} catch (org.openqa.selenium.NoSuchElementException exc) {
						APP_LOGS.debug("Failed to Load Element ..." + exc);
						return null;
					}
				}
			});
		} catch (org.openqa.selenium.TimeoutException ex) {
			APP_LOGS.debug("Failed to Load Element after 5 min ..." + ex);
			return null;
		}
		return eleList;
	}

	public String clickButton(String object, String data) {

		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Clicking on Button ");
		WebElement element = getWebElement(object, data);
		if (element != null) {
			element.click();
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(
					Constants.KEYWORD_FAIL + " -- Not able to click on Button -- Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Not able to click on Button -- Element " + locator + " Not Found";
		}
	}

	public String selectRadio(String object, String data) {
		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Selecting a radio button --Locator " + locator);
		WebElement element = getWebElement(object, data);
		if (element != null) {
			element.click();
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(
					Constants.KEYWORD_FAIL + " -- Not able to find radio button -- Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Not able to find radio button -- Element " + locator + " Not Found";
		}
	}

	/**
	 * selectByVisibleText (drop down selection by text)
	 * 
	 * @param object
	 * @param data
	 * @return pass or fail
	 */
	public String selectByVisibleText(String object, String data) {
		String locator = UIMap.getProperty(object.split(",")[0]);
		APP_LOGS.debug("Select dropdown :: Object is " + locator + " Data is " + data);
		WebElement element = getWebElement(object, data);
		if (element != null) {
			Select sl = new Select(element);
			sl.selectByVisibleText(data);
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(Constants.KEYWORD_FAIL + " -- Unable to select  -- Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Unable to select  -- Element " + locator + " Not Found";
		}
	}

	/**
	 * selectByIndex (drop down selection by index)
	 * 
	 * @param object
	 * 
	 * @param data
	 * 
	 * @return pass or fail
	 */
	public String selectByIndex(String object, String data) {
		String locator = UIMap.getProperty(object.split(",")[0]);
		APP_LOGS.debug("Select dropdown :: Object is " + locator + " Data is " + data);
		WebElement element = getWebElement(object, data);
		if (element != null) {
			Select sl = new Select(element);
			sl.selectByIndex(Integer.parseInt(data));
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(Constants.KEYWORD_FAIL + " -- Unable to select  -- Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Unable to select  -- Element " + locator + " Not Found";
		}
	}

	/**
	 * 
	 * Clear text in element
	 * @param object
	 * @param data
	 * @return pass or Fail
	 */
	public String clearText(String object, String data) {
		String locator = UIMap.getProperty(object.split(",")[0]);
		APP_LOGS.debug("Writing in text box:: Object is " + locator + " Data is " + data);
		WebElement element = getWebElement(object, data);
		if (element != null) {
			element.clear();
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(Constants.KEYWORD_FAIL + " -- Unable to clear text  -- Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Unable to clear text  -- Element " + locator + " Not Found";
		}
	}

	/**
	 * 
	 * Get text from UI
	 * @param object
	 * @param data
	 * @return
	 */
	public String getText(String object, String data) {

		String locator = UIMap.getProperty(object);
		APP_LOGS.debug("Verifying the text --Locator " + locator);
		WebElement element = getWebElement(object, data);
		if (element != null) {
			getTextValue = element.getText().trim();
			return Constants.KEYWORD_PASS;
		} else {
			APP_LOGS.debug(Constants.KEYWORD_FAIL + " -- Could not find Element " + locator + " Not Found");
			return Constants.KEYWORD_FAIL + " -- Could not find Element " + locator + " Not Found";
		}
	}

	/**
	 * Assert expected values with actual values
	 * @param object
	 * @param data
	 * @return
	 */
	public String verifyTitle(String object, String data) {

		APP_LOGS.debug("Verifying title");
		try {
			String actualTitle = driver.getTitle();
			String expectedTitle = data;
			if (actualTitle.equals(expectedTitle))
				return Constants.KEYWORD_PASS;
			else
				return Constants.KEYWORD_FAIL + " -- Title not verified " + expectedTitle + " -- " + actualTitle;
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + " Error in retrieving title";
		}
	}

	/**
	 * @param object
	 * @param data
	 * @return Pass or fail
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	public static String Wait(String object, String data) throws NumberFormatException, InterruptedException {

		if (!data.trim().equals("")) {
			APP_LOGS.debug("Waiting for  " + data);
			Thread.sleep(Long.parseLong(data));
		} else {
			APP_LOGS.debug("Waiting for  5000");
			Thread.sleep(5000);
		}
		return Constants.KEYWORD_PASS;
	}

	/**
	 * 
	 * close the browser
	 * 
	 * @param object
	 * @param data
	 * @return pass or Fail
	 */
	public String closeBrowser(String object, String data) {
		APP_LOGS.debug("Closing the browser");
		try {
			driver.quit();
		} catch (Exception e) {
			return Constants.KEYWORD_FAIL + "Unable to close browser. Check if its open" + e.getMessage();
		}
		return Constants.KEYWORD_PASS;
	}

	/**
	 * Navigate to previous page
	 * 
	 * @param object
	 * @param data
	 * @return  Pass or Fail
	 */
	public String navigateBack(String object, String data) {
		String result;

		String currentTitle = driver.getTitle();

		System.out.println(currentTitle);

		driver.navigate().back();

		if (driver.getTitle().equals(currentTitle))
			result = Constants.KEYWORD_FAIL;
		else
			result = Constants.KEYWORD_PASS;

		return result;
	}

	protected List<WebElement> findElementsInGroup(String object, String data) {
		return getWebElements(UIMap.getProperty(object));
	}

}
