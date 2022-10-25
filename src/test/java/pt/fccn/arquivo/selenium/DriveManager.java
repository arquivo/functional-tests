package pt.fccn.arquivo.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.appmanagement.BaseOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.MobileCapabilityType;

public class DriveManager {

    // Saucelabs port
    private static final String PORT = System.getProperty("test.remote.access.port");

    // Web driver capabilities
    private MutableCapabilities capabilities;

    // Browser types
    private enum BrowsersTypes {
        CHROME, FIREFOX, MICROSOFTEDGE, SAFARI,
    }

    private URL buildUrl() throws MalformedURLException {

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://");
        urlBuilder.append("127.0.0.1:");
        urlBuilder.append(PORT);
        urlBuilder.append("/wd/hub");

        URL url = new URL(urlBuilder.toString());

        System.out.println("Remote web driver URL:" + url);

        return url;
    }

    public RemoteWebDriver getDriver(String platformName, String browser, String browserVersion,
            String device, String deviceOrientation, String automationName, Map<String, Object> sauceOptions) throws MalformedURLException {
        if(device != null) {
            return getMobileDriver(platformName, browser, device, browserVersion, deviceOrientation, automationName, sauceOptions);
        } else {
            return getDesktopDriver(browser, platformName, browserVersion, sauceOptions);
        }
    }

    public RemoteWebDriver getDesktopDriver(String browserName, String platformName, String browserVersion,
            Map<String, Object> sauceOptions) throws MalformedURLException {

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
        capabilities.setCapability("sauce:options", sauceOptions);

        System.out.println("Web driver configurations: browser[" + browserName +
            "], browser version[" + browserVersion +
            "], platform name[" + platformName +
            "], sauce options[" + sauceOptions.toString() + "]"
        );
        System.out.println("Capabilities: " + capabilities.toString());
        return new RemoteWebDriver(buildUrl(), capabilities);
    }

    public RemoteWebDriver getMobileDriver(String PlatformName, String browserName, String deviceName,
     String platformVersion, String deviceOrientation, String automationName, Map<String, Object> sauceOptions) throws MalformedURLException {

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
        capabilities.setCapability(CapabilityType.PLATFORM_NAME,PlatformName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, browserName);

        if(deviceOrientation != null)
            capabilities.setCapability(MobileCapabilityType.ORIENTATION, deviceOrientation);

        sauceOptions.remove("screenResolution");
        capabilities.setCapability("sauce:options", sauceOptions);

        System.out.println("moibile Web driver configurations: browser[" + browserName +
            "], device name[" + deviceName +
            "], platform version[" + platformVersion +
            "], sauce options[" + sauceOptions.toString() + "]"
        );
        System.out.println("Capabilities: " + capabilities.toString());

        if(PlatformName.equals("iOS")){
            capabilities.setCapability("appium:nativeWebTap", true);
            capabilities.setCapability("appium:nativeWebTapStrict", true);
            return new IOSDriver(buildUrl(), capabilities);
        } else if (PlatformName.equals("Android")){
            return new AndroidDriver(buildUrl(), capabilities);
        } else {
            return new AppiumDriver(buildUrl(), capabilities);
        }
    }
}
