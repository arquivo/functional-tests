package pt.fccn.arquivo.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

public class DriveManager {

    // Saucelabs port
    private static final String PORT = System.getProperty("test.remote.access.port");

    // Web driver capabilities
    private Capabilities capabilities;

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

        System.out.println("Remove web driver URL:" + url);

        return url;
    }

    public RemoteWebDriver getDriver(String browserName, String platformName, String browserVersion,
            HashMap<String, Object> sauceOptions) throws Exception {

        BrowsersTypes browser = BrowsersTypes.valueOf(browserName.toUpperCase());

        switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPlatformName(platformName);
                chromeOptions.setBrowserVersion(browserVersion);
                chromeOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (Capabilities) chromeOptions;
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPlatformName(platformName);
                firefoxOptions.setBrowserVersion(browserVersion);
                firefoxOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (Capabilities) firefoxOptions;
                break;
            case MICROSOFTEDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPlatformName(platformName);
                edgeOptions.setBrowserVersion(browserVersion);
                edgeOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (Capabilities) edgeOptions;
                break;
            case SAFARI:
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setPlatformName(platformName);
                safariOptions.setBrowserVersion(browserVersion);
                safariOptions.setCapability("sauce:options", sauceOptions);
                break;
            default:
                throw new Exception("Driver not supported");
        }
        System.out.println("Web driver configurations: browser[" + browserName +
            "], browser version[" + browserVersion +
            "], platform name[" + platformName +
            "], sauce options[" + sauceOptions.toString() + "]"
        );
        return new RemoteWebDriver(buildUrl(), capabilities);
    }
}
