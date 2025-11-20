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
import io.appium.java_client.remote.MobileCapabilityType;

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
        if (config.containsKey(MobileCapabilityType.DEVICE_NAME)) {
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
        switch (config.get(MobileCapabilityType.AUTOMATION_NAME)) {
            case "XCUITest":
                XCUITestOptions xcuiOptions = new XCUITestOptions();
                capabilities = xcuiOptions;
                capabilities.setCapability("appium:nativeWebTap", true);
                capabilities.setCapability("appium:nativeWebTapStrict", true);
                break;
            case "UiAutomator2":
                capabilities = new UiAutomator2Options();
                break;
            default:
                capabilities = new MutableCapabilities();
        }

        capabilities.setCapability(CapabilityType.PLATFORM_NAME, config.get(CapabilityType.PLATFORM_NAME));
        capabilities.setCapability(CapabilityType.BROWSER_NAME, config.get(CapabilityType.BROWSER_NAME));
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.get(MobileCapabilityType.AUTOMATION_NAME));
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, config.get(MobileCapabilityType.PLATFORM_VERSION));
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, config.get(MobileCapabilityType.DEVICE_NAME));

        if (config.containsKey(MobileCapabilityType.ORIENTATION)) {
            capabilities.setCapability(MobileCapabilityType.ORIENTATION, config.get(MobileCapabilityType.ORIENTATION));
        }

        capabilities.setCapability("sauce:options", sauceOptions);

        String platform = config.get(CapabilityType.PLATFORM_NAME);
        if ("iOS".equalsIgnoreCase(platform)) {
            return new IOSDriver(buildUrl(), capabilities);
        } else if ("Android".equalsIgnoreCase(platform)) {
            return new AndroidDriver(buildUrl(), capabilities);
        } else {
            return new AppiumDriver(buildUrl(), capabilities);
        }
    }
}
