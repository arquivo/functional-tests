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

package pt.fccn.arquivo.selenium;

import static org.junit.Assert.assertNotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.util.AppendableErrorsBaseTest;

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

    public static String buildTag;
    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied
     * user name/access key. To use the authentication supplied by environment
     * variables or from an external file, use the no-arg
     * {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

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
    public RetryRule rule = new RetryRule(1);

    /**
     * Represents the browser to be used as part of the test run.
     */
    protected String browser;
    /**
     * Represents the operating system to be used as part of the test run.
     */
    protected String platformName;
    /**
     * Represents the version of the browser to be used as part of the test run.
     */
    protected String browserVersion;
    /**
     * Represents the deviceName of mobile device
     */
    protected String device;
    /**
     * Represents the device-orientation of mobile device
     */
    protected String deviceOrientation;

    protected String automationName;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    protected String sessionId;

    protected RemoteWebDriver driver;
    // protected static ArrayList<WebDriver> drivers;

    protected String screenResolution;
    protected String testURL;
    protected String titleOfFirstResult;

    @Deprecated
    protected static String pre_prod = "preprod";

    protected final boolean isPreProd;

    public WebDriverTestBaseParallel(String platformName, String browser, String browserVersion,
        String device, String deviceOrientation, String automationName) {

        super();
        this.platformName = platformName;
        this.browserVersion = browserVersion;
        this.browser = browser;
        this.device = device;
        this.deviceOrientation = deviceOrientation;
        this.automationName = automationName;
        this.testURL = System.getProperty("test.url");
        assertNotNull("test.url property is required", this.testURL);
        this.isPreProd = this.testURL.contains(pre_prod);
        this.screenResolution = System.getProperty("test.resolution");

        System.out.println("Platform name: " + platformName);
        System.out.println("Browser: " + browser);
        System.out.println("Browser version: " + browserVersion);
        System.out.println("Device: " + device);
        System.out.println("Orientation: " + deviceOrientation);
    }

    /**
     * @return a LinkedList containing String arrays representing the browser
     *         combinations the test should be run against. The values in the String
     *         array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList<String[]> browsersStrings() {
        String browsersJSON = System.getenv("SAUCE_ONDEMAND_BROWSERS");
        System.out.println("SAUCE_ONDEMAND_BROWSERS: " + browsersJSON);

        LinkedList<String[]> browsers = new LinkedList<String[]>();
        if (browsersJSON == null || browsersJSON.isEmpty()) {
            System.out.println("You did not specify browsers, testing with latest firefox on Linux...");
            //browsers.add(new String[] { "Windows 8.1", "latest", "chrome", null, null });
            //browsers.add(new String[] { "Mac 10.14", "latest", "safari", null, null });
            //browsers.add(new String[] { "Linux", null, "chrome", null, null });
            browsers.add(new String[] { "Linux", null, "firefox", null, null });
        } else {
            JSONObject browsersJSONObject = new JSONObject("{browsers:" + browsersJSON + "}");

            JSONArray browsersJSONArray = browsersJSONObject.getJSONArray("browsers");

            for (int i = 0; i < browsersJSONArray.length(); i++) {
                JSONObject browserConfigs = browsersJSONArray.getJSONObject(i);
                browsers.add(new String[] {
                    browserConfigs.getString("platform"),
                    browserConfigs.getString("browser"),
                    browserConfigs.optString("browser-version"),
                    browserConfigs.optString("device", null),
                    browserConfigs.optString("device-orientation", null),
                    browserConfigs.optString("automation-name", null)
                });
            }
        }


        return browsers;
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

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("username", authentication.getUsername());
        sauceOptions.put("accessKey", authentication.getAccessKey());

        String methodName = name.getMethodName() + " " + browser + " " + browserVersion;
        sauceOptions.put("name", methodName);

        //the default value is false
        //sauceOptions.put("acceptInsecureCerts", true);

        if (screenResolution != null && !screenResolution.isEmpty()) {
            System.out.println("Screen Resolution: " + screenResolution);
            sauceOptions.put("screenResolution", screenResolution);
        }

        
        // Getting the build name.
        // Using the Jenkins ENV var. You can use your own. If it is not set test will
        // run without a build id.
        sauceOptions.put("build", System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));

        //SauceHelpers.addSauceConnectTunnelId(capabilities);

        DriveManager driveManager = new DriveManager();

        this.driver = driveManager.getDriver(platformName, browser, browserVersion, device, deviceOrientation, automationName, sauceOptions);
        this.driver.get(testURL);

        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

        String message = String.format("SessionID=%1$s job-name=%2$s", this.sessionId, methodName);
        System.out.println(message);


        Timeouts timeouts = driver.manage().timeouts();
        timeouts.pageLoadTimeout(5, TimeUnit.MINUTES);
        timeouts.implicitlyWait(5, TimeUnit.MINUTES);
        timeouts.setScriptTimeout(5, TimeUnit.MINUTES);

        System.out.println(String.format("Start running test: %s\n", this.getClass().getSimpleName()));
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

    @BeforeClass
    public static void setupClass() {
        // get the uri to send the commands to.
        seleniumURI = SauceHelpers.buildSauceUri();
        // If available add build tag. When running under Jenkins BUILD_TAG is
        // automatically set.
        // You can set this manually on manual runs.
        buildTag = System.getenv("BUILD_TAG");
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

    protected boolean isPreprod() {
        return this.isPreProd;
    }

    protected boolean isProduction() {
        return this.testURL.contains("://arquivo.pt") || this.testURL.contains("://m.arquivo.pt");
    }
}
