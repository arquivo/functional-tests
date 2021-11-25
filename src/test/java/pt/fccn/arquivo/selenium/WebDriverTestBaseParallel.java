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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

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
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import pt.fccn.arquivo.util.AppendableErrorsBaseTest;

//import org.json.*;

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
    private String port = System.getProperty("test.remote.access.port");

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
    public RetryRule rule = new RetryRule(2);

    /**
     * Represents the browser to be used as part of the test run.
     */
    protected String browser;
    /**
     * Represents the operating system to be used as part of the test run.
     */
    protected String os;
    /**
     * Represents the version of the browser to be used as part of the test run.
     */
    protected String version;
    /**
     * Represents the deviceName of mobile device
     */
    protected String deviceName;
    /**
     * Represents the device-orientation of mobile device
     */
    protected String deviceOrientation;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    protected String sessionId;

    protected RemoteWebDriver driver;
    // protected static ArrayList<WebDriver> drivers;

    protected String screenResolution;
    protected String testURL;
    protected String browserVersion;
    protected String titleOfFirstResult;

    @Deprecated
    protected static String pre_prod = "preprod";

    protected final boolean isPreProd;

    public WebDriverTestBaseParallel(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.deviceOrientation = deviceOrientation;
        this.testURL = System.getProperty("test.url");
        assertNotNull("test.url property is required", this.testURL);
        this.isPreProd = this.testURL.contains(pre_prod);
        this.screenResolution = System.getProperty("test.resolution");

        System.out.println("OS: " + os);
        System.out.println("Version: " + version);
        System.out.println("Browser: " + browser);
        System.out.println("Device: " + deviceName);
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
                String browserOS = browserConfigs.getString("os");
                //String browserPlatform = browserConfigs.getString("platform");
                String browserName = browserConfigs.getString("browser");
                String browserVersion = browserConfigs.optString("browser-version");
                String device = browserConfigs.optString("device", null);
                String deviceOrientation = browserConfigs.optString("device-orientation", null);
                browsers.add(new String[] { browserOS, browserVersion, browserName, device, deviceOrientation });
            }
        }


        return browsers;
    }

    /**
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use
     * the capabilities defined by the {@link #browser}, {@link #version} and
     * {@link #os} instance variables, and which is configured to run against
     * ondemand.saucelabs.com, using the username and access key populated by the
     * {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the
     *                   {@link RemoteWebDriver} instance.
     */
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browser != null)
            capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        if (version != null)
            capabilities.setCapability(CapabilityType.VERSION, version);
        if (deviceName != null)
            capabilities.setCapability("deviceName", deviceName);
        if (deviceOrientation != null)
            capabilities.setCapability("device-orientation", deviceOrientation);

        capabilities.setCapability(CapabilityType.PLATFORM, os);
//		capabilities.setCapability(CapabilityType.PLATFORM, "ANY");

        String methodName = name.getMethodName() + " " + browser + " " + version;
        capabilities.setCapability("name", methodName);

        capabilities.setCapability("acceptSslCerts", true);

        capabilities.setCapability("acceptInsecureCerts", true);

        System.out.println("Screen Resolution: " + screenResolution);
        if (screenResolution != null && !screenResolution.isEmpty()) {
            capabilities.setCapability("screenResolution", screenResolution);
        }
        // Getting the build name.
        // Using the Jenkins ENV var. You can use your own. If it is not set test will
        // run without a build id.
        /*
         * if (buildTag != null) { capabilities.setCapability("build", buildTag); }
         */
        capabilities.setCapability("build", System.getenv("JOB_NAME") + "__" + System.getenv("BUILD_NUMBER"));

        SauceHelpers.addSauceConnectTunnelId(capabilities);

        this.driver = new RemoteWebDriver(buildUrl(), capabilities);
        this.driver.get(testURL);

        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();

        String message = String.format("SessionID=%1$s job-name=%2$s", this.sessionId, methodName);
        System.out.println(message);

        Timeouts timeouts = driver.manage().timeouts();
        // it isn't working on latest firefox
//		timeouts.pageLoadTimeout(25, TimeUnit.SECONDS);
        timeouts.implicitlyWait(5, TimeUnit.SECONDS);
        timeouts.setScriptTimeout(5, TimeUnit.SECONDS);

        System.out.println(String.format("Start running test: %s\n", this.getClass().getSimpleName()));
    }

    private URL buildUrl() throws MalformedURLException {
        String username = authentication.getUsername();
        String accessKey = authentication.getAccessKey();

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://");
        if (username != null && !username.isEmpty()) {
            urlBuilder.append(username);
            urlBuilder.append(":");
        }
        if (accessKey != null && !accessKey.isEmpty()) {
            urlBuilder.append(accessKey);
            urlBuilder.append("@");
        }
        urlBuilder.append("127.0.0.1:");
        urlBuilder.append(port);
        urlBuilder.append("/wd/hub");

        URL url = new URL(urlBuilder.toString());

        System.out.println(url);

        return url;
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

        new WebDriverWait(driver, 40).until(ExpectedConditions.visibilityOfElementLocated(by));
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
