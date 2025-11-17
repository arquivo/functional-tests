/*
    Copyright (C) 2011 David Cruz <david.cruz@fccn.pt>
    Copyright (C) 2011 SAW Group - FCCN <saw@asa.fccn.pt>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pt.arquivo.selenium;

import static org.junit.Assert.assertNotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import pt.arquivo.utils.AppendableErrorsBaseTest;

/**
 * The base class for tests using WebDriver to test specific browsers. This test
 * read system properties to know which browser to test or, if tests are te be
 * run remotely, it also read login information and the browser, browser version
 * and OS combination to be used.
 *
 * The WebDriver tests provide the more precise results without the restrictions
 * present in selenium due to browers' security models.
 */
@Ignore
@RunWith(ConcurrentParameterized.class)
public class WebDriverTestBaseParallel extends AppendableErrorsBaseTest implements SauceOnDemandSessionIdProvider {
    public static String seleniumURI;

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied
     * user name/access key. To use the authentication supplied by environment
     * variables or from an external file, use the no-arg
     * {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(System.getProperty("test.saucelabs.user"), System.getProperty("test.saucelabs.key"));

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test
     * succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = authentication.getUsername() != null
            && !authentication.getUsername().isEmpty() ? new SauceOnDemandTestWatcher(this, authentication) : null;

    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    /**
     * Test decorated with @Retry will be run X times in case they fail using this
     * rule.
     */
    @Rule
    public RetryRule rule = new RetryRule(2);

    protected String sessionId;
    protected RemoteWebDriver driver;

    protected String testURL;
    protected Map<String, String> config;


    public WebDriverTestBaseParallel(Map<String, String> config) {
        super();
        this.config = config;
        for(String key : config.keySet()) {
            System.out.println(key + " : " + config.get(key));
        }

        this.testURL = System.getProperty("test.url");
        assertNotNull("test.url property is required", this.testURL);
    }

    /**
     * @return a LinkedList containing String arrays representing the browser
     *         combinations the test should be run against. The values in the String
     *         array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList<Map<String, String>[]> browsersStrings() {
        String browsersJSON = System.getProperty("test.browsers.json");
        LinkedList<Map<String, String>[]> configList = new LinkedList<>();
        System.out.println("SAUCE_ONDEMAND_BROWSERS: " + browsersJSON);

        if (browsersJSON == null || browsersJSON.isEmpty()) {
            HashMap<String, String> defaultConfig = new HashMap<>();
            defaultConfig.put("platformName", "Windows 10");
            defaultConfig.put("browserName", "Chrome");
            defaultConfig.put("browserVersion", "latest");
            defaultConfig.put("screenResolution", "1280x960");
            configList.add(new Map[]{defaultConfig});
        } else {
            JSONObject browsersJSONObject = new JSONObject("{browsers:" + browsersJSON + "}");
            JSONArray browsersJSONArray = browsersJSONObject.getJSONArray("browsers");
            for (int i = 0; i < browsersJSONArray.length(); i++) {
                JSONObject browserConfigs = browsersJSONArray.getJSONObject(i);
                HashMap<String, String> config = new HashMap<>();
                for (String key : browserConfigs.keySet()) {
                    config.put(key, browserConfigs.getString(key));
                }
                configList.add(new Map[]{config});
            }
        }

        return configList;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use
     * the capabilities defined by the {@link #browser}, {@link #browserVersion} and
     * {@link #os} instance variables, and which is configured to run against
     * ondemand.saucelabs.com, using the username and access key populated by the
     * {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the
     *                   {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DriverManager driveManager = new DriverManager();

        this.driver = driveManager.getDriver(config, buildSauceOptions());
        this.driver.get(testURL);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

        Timeouts timeouts = driver.manage().timeouts();
        timeouts.pageLoadTimeout(Duration.ofMinutes(5));
        timeouts.implicitlyWait(Duration.ofMinutes(5));
        timeouts.scriptTimeout(Duration.ofMinutes(5));

        System.out.println(String.format("Start running test: %s\n", this.getClass().getSimpleName()));
    }

    private Map<String, Object> buildSauceOptions() {
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", System.getProperty("test.saucelabs.user"));
        sauceOptions.put("accessKey", System.getProperty("test.saucelabs.key"));
        sauceOptions.put("build", System.getProperty("test.build.id"));
        sauceOptions.put("name", name.getMethodName());
        sauceOptions.put("tunnelName", System.getProperty("test.saucelabs.tunnelname"));

        String message = String.format("SessionID=%1$s job-name=%2$s", this.sessionId, name.getMethodName());
        System.out.println(message);
        //the default value is false
        //sauceOptions.put("acceptInsecureCerts", true);
        return sauceOptions;
    }

    /**
     * Releases the resources used for the tests, i.e., It closes the WebDriver.
     */
    @After
    public void tearDown() throws Exception {
        driver.quit();
        super.tearDown();
    }

    /**
     * Utility class to obtain the Class name in a static context.
     */
    public static class CurrentClassGetter extends SecurityManager {
        public String getClassName() {
            return getClassContext()[1].getName();
        }
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Checks if an element is present in the page
     */
    protected boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected WebElement waitUntilElementIsVisibleAndGet(By by) {
//		WebElement element = driver.findElement(by);
//		driver.executeScript("arguments[0].click();", element);

        new WebDriverWait(driver, Duration.ofSeconds(40)).until(ExpectedConditions.visibilityOfElementLocated(by));
        return driver.findElement(by);
    }

    public RemoteWebDriver getDriver() {
        return driver;
    }

    public String getTestURL() {
        return testURL;
    }
}
