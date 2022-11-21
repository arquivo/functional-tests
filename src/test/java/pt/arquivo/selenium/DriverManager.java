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

    // Saucelabs port
    private static final String PORT = System.getProperty("test.selenium.port");
    private static final String HOST = System.getProperty("test.selenium.host");

    // Web driver capabilities
    private MutableCapabilities capabilities;

    // Browser types
    private enum BrowsersTypes {
        CHROME, FIREFOX, MICROSOFTEDGE, SAFARI,
    }

    private URL buildUrl() throws MalformedURLException {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(PORT.equals("443") ? "https://" : "http://");
        urlBuilder.append(HOST);
        urlBuilder.append(PORT.equals("443") ? "" : ":" + PORT);
        urlBuilder.append("/wd/hub");

        URL url = new URL(urlBuilder.toString());

        System.out.println("Remote web driver URL:" + url);

        return url;
    }

    public RemoteWebDriver getDriver(String platformName, String platformVersion, String browser, String browserVersion,
            String device, String deviceOrientation, String automationName, String resolution, Map<String, Object> sauceOptions) throws MalformedURLException {
        if(device != null) {
            return getMobileDriver(browser, platformName, platformVersion, device, deviceOrientation, automationName, sauceOptions);
        } else {
            return getDesktopDriver(browser, browserVersion, platformName, resolution, sauceOptions);
        }
    }

    public RemoteWebDriver getDesktopDriver(String browserName, String browserVersion, String platformName,
            String resolution, Map<String, Object> sauceOptions) throws MalformedURLException {

        BrowsersTypes browser = BrowsersTypes.valueOf(browserName.toUpperCase());

        switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                capabilities = (MutableCapabilities) chromeOptions;
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                capabilities = (MutableCapabilities) firefoxOptions;
                break;
            case MICROSOFTEDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                capabilities = (MutableCapabilities) edgeOptions;
                break;
            case SAFARI:
                SafariOptions safariOptions = new SafariOptions();
                capabilities = (MutableCapabilities) safariOptions;
                break;
            default:
                capabilities = new MutableCapabilities();
                capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);
        }

        capabilities.setCapability(CapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(CapabilityType.BROWSER_VERSION, browserVersion);

        if (resolution != null && !resolution.isEmpty()) {
            sauceOptions.put("screenResolution", resolution);
        }

        capabilities.setCapability("sauce:options", sauceOptions);

        System.out.println("Web driver configurations: browser[" + browserName +
            "], browser version[" + browserVersion +
            "], platform name[" + platformName +
            "], sauce options[" + sauceOptions.toString() + "]"
        );
        System.out.println("Capabilities: " + capabilities.toString());
        return new RemoteWebDriver(buildUrl(), capabilities);
    }

    public RemoteWebDriver getMobileDriver(String browserName, String platformName, String platformVersion,
     String device, String deviceOrientation, String automationName, Map<String, Object> sauceOptions) throws MalformedURLException {

        switch (automationName) {
            case "XCUITest":
                XCUITestOptions xcuiOptions = new XCUITestOptions();
                capabilities = xcuiOptions;
                break;
            case "UiAutomator2":
                UiAutomator2Options uiautomatorOptions = new UiAutomator2Options();
                capabilities = uiautomatorOptions;
                break;
            default:
                capabilities = new MutableCapabilities();
        }

        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, automationName);
        capabilities.setCapability(CapabilityType.PLATFORM_NAME,platformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);

        if(deviceOrientation != null)
            capabilities.setCapability(MobileCapabilityType.ORIENTATION, deviceOrientation);

        sauceOptions.remove("screenResolution");
        capabilities.setCapability("sauce:options", sauceOptions);

        System.out.println("moibile Web driver configurations: browser[" + browserName +
            "], device name[" + device +
            "], platform version[" + platformVersion +
            "], sauce options[" + sauceOptions.toString() + "]"
        );
        System.out.println("Capabilities: " + capabilities.toString());

        if(platformName.equals("iOS")){
            capabilities.setCapability("appium:nativeWebTap", true);
            capabilities.setCapability("appium:nativeWebTapStrict", true);
            return new IOSDriver(buildUrl(), capabilities);
        } else if (platformName.equals("Android")){
            return new AndroidDriver(buildUrl(), capabilities);
        } else {
            return new AppiumDriver(buildUrl(), capabilities);
        }
    }
}
