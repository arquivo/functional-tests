package pt.arquivo.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;

public class DriverManager {

    private MutableCapabilities capabilities;

    private enum BrowserTypes {
        CHROME, FIREFOX, EDGE, SAFARI
    }

    private URL buildUrl() throws MalformedURLException {
        String user = System.getProperty("test.saucelabs.user");
        String key = System.getProperty("test.saucelabs.key");
        URL url = new URL(String.format("https://%s:%s@ondemand.saucelabs.com:443/wd/hub", user, key));
        System.out.println("Remote web driver URL: " + url);
        return url;
    }

    public RemoteWebDriver getDriver(Map<String, String> config, Map<String, Object> sauceOptions) throws MalformedURLException {
        if (config.containsKey("device")) {
            return getMobileDriver(config, sauceOptions);
        } else {
            return getDesktopDriver(config, sauceOptions);
        }
    }

    public RemoteWebDriver getDesktopDriver(Map<String, String> config, Map<String, Object> sauceOptions) throws MalformedURLException {
        BrowserTypes browser = BrowserTypes.valueOf(config.get(CapabilityType.BROWSER_NAME).toUpperCase());

        switch (browser) {
            case CHROME:
                capabilities = new ChromeOptions();
                break;
            case FIREFOX:
                capabilities = new FirefoxOptions();
                break;
            case EDGE:
                capabilities = new EdgeOptions();
                break;
            case SAFARI:
                capabilities = new SafariOptions();
                break;
            default:
                capabilities = new MutableCapabilities();
                capabilities.setCapability(CapabilityType.BROWSER_NAME, config.get(CapabilityType.BROWSER_NAME));
        }

        capabilities.setCapability(CapabilityType.PLATFORM_NAME, config.get(CapabilityType.PLATFORM_NAME));
        capabilities.setCapability(CapabilityType.BROWSER_VERSION, config.get(CapabilityType.BROWSER_VERSION));

        if (config.containsKey("screenResolution")) {
            sauceOptions.put("screenResolution", config.get("screenResolution"));
        }

        capabilities.setCapability("sauce:options", sauceOptions);

        return new RemoteWebDriver(buildUrl(), capabilities);
    }

    public RemoteWebDriver getMobileDriver(Map<String, String> config, Map<String, Object> sauceOptions) throws MalformedURLException {
       
        String automationName = config.get("automationName"); // Use the string literal or a custom enum/constant

        // We now use a base MutableCapabilities to combine all options later
        MutableCapabilities finalCapabilities;

        switch (automationName) {
            case AutomationName.IOS_XCUI_TEST: 
                XCUITestOptions xcuiOptions = new XCUITestOptions();
                // xcuiOptions.setNativeWebTap(true);
                // xcuiOptions.setNativeWebTapStrict(true);
                finalCapabilities = xcuiOptions;
                break;
            case AutomationName.ANDROID_UIAUTOMATOR2: 
                UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
                finalCapabilities = uiAutomator2Options;
                break;
            default:
                // Fallback for other/unknown drivers
                finalCapabilities = new MutableCapabilities();
                finalCapabilities.setCapability(CapabilityType.PLATFORM_NAME, config.get(CapabilityType.PLATFORM_NAME));
                finalCapabilities.setCapability("automationName", automationName);
                break;
        }

        // Apply common capabilities from the config map to the final capabilities object
        // Note: The Options classes (xcuiOptions/uiAutomator2Options) already set platformName/automationName internally.
        // We use standard W3C keys where possible.
        
        if (config.containsKey(CapabilityType.BROWSER_NAME)) {
           finalCapabilities.setCapability(CapabilityType.BROWSER_NAME, config.get(CapabilityType.BROWSER_NAME));
        }

        if (config.containsKey("platformVersion")) {
           // This will automatically be vendor-prefixed as 'appium:platformVersion' if needed
           finalCapabilities.setCapability("appium:platformVersion", config.get("platformVersion"));
        }
        
        if (config.containsKey("device")) {
            finalCapabilities.setCapability("appium:deviceName", config.get("device"));
        }

        if (config.containsKey("deviceOrientation")) {
            finalCapabilities.setCapability("appium:orientation", config.get("deviceOrientation"));
        }
        
        // Add sauce:options object as a standard capability
        finalCapabilities.setCapability("sauce:options", sauceOptions);

        String platform = config.get(CapabilityType.PLATFORM_NAME);
        if ("iOS".equalsIgnoreCase(platform)) {
            // Pass the options (which are MutableCapabilities) directly to the driver constructor
            return new IOSDriver(buildUrl(), finalCapabilities);
        } else if ("Android".equalsIgnoreCase(platform)) {
            return new AndroidDriver(buildUrl(), finalCapabilities);
        } else {
            return new AppiumDriver(buildUrl(), finalCapabilities);
        }
    }
}
