package pt.fccn.arquivo.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

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

    public RemoteWebDriver getDriver(String browserName, String platformName, String browserVersion,
            Map<String, Object> sauceOptions) throws Exception {

        BrowsersTypes browser = BrowsersTypes.valueOf(browserName.toUpperCase());

        switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (MutableCapabilities) chromeOptions;
                break;
            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (MutableCapabilities) firefoxOptions;
                break;
            case MICROSOFTEDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (MutableCapabilities) edgeOptions;
                break;
            case SAFARI:
                SafariOptions safariOptions = new SafariOptions();
                safariOptions.setCapability("sauce:options", sauceOptions);
                capabilities = (MutableCapabilities) safariOptions;
                break;
            default:
                throw new Exception("Driver not supported");
        }

        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("browserVersion", browserVersion);

        System.out.println("Web driver configurations: browser[" + browserName +
            "], browser version[" + browserVersion +
            "], platform name[" + platformName +
            "], sauce options[" + sauceOptions.toString() + "]"
        );
        return new RemoteWebDriver(buildUrl(), capabilities);
    }
}
