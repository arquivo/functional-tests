package pt.arquivo.tests.webapp.menu;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.Locale;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.utils.LocaleUtils;
import pt.arquivo.utils.LocalizedString;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MenuAboutHomepageTest extends MenuTest {

    public MenuAboutHomepageTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
        super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
    }

    @Test
    @Retry
    public void menuAboutHomepagePTTest() {
        LocaleUtils.changeLanguageToPT(this);
        menuAbout(LocaleUtils.PORTUGUESE);
    }

    @Test
    @Retry
    public void menuAboutHomepageENTest() {
        LocaleUtils.changeLanguageToEN(this);
        menuAbout(LocaleUtils.ENGLISH);
    }

    private void menuAbout(Locale locale) {
        openMenu();

        run("Click about button", () -> waitUntilElementIsVisibleAndGet(By.id("menu-about")).click());
        
        String expectedUrl = new LocalizedString()
            .pt("https://sobre.arquivo.pt/pt/")
            .en("https://sobre.arquivo.pt/en/")
            .apply(locale);

        String logoXpath = new LocalizedString()
            .pt("/html/body/header/div/div/aside/div/a/img")
            .en("/html/body/header/div/div/aside/div/p/a/img")
            .apply(locale);
        
        //waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"logoContainer\"]"));

        appendError("Check if Arquivo.pt log appears", () -> new WebDriverWait(driver, Duration.ofSeconds(100))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(logoXpath))));

                
        run("Verify sobre.arquivo.pt", () -> {
            assertEquals("Check if current url is the about page", expectedUrl, driver.getCurrentUrl());
        });

    }

}
