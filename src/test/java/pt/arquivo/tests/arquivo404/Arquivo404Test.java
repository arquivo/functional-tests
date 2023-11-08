package pt.arquivo.tests.arquivo404;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dev.failsafe.internal.util.Assert;
import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.LocaleUtils;
import pt.arquivo.utils.LocalizedString;

/**
 *
 * @author Vasco Rato <vasco.rato@fccn.pt>
 *
 */
public class Arquivo404Test extends WebDriverTestBaseParallel {

    
    public Arquivo404Test(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void arquivo404Test() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");

        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });
        
        appendError("Verify that Arquivo404 is installed", () -> {
            assertTrue((Boolean) ((JavascriptExecutor) driver).executeScript("return (typeof ARQUIVO_NOT_FOUND_404 == 'undefined' ? false : true)"));
        });

        appendError("Verify that Arquivo404 doesn't change anything when the requested page isn't archived", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.invisibilityOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that url method works", () -> {
            ((JavascriptExecutor) driver).executeScript("ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.visibilityOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that message method works", () -> {
            // Reload page to clear previous configurations
            driver.get(testURL + "/thispagedoesnotexist/");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));

            String testText = "Test message";
            ((JavascriptExecutor) driver).executeScript("ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').message('"+testText+"').call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.textToBe(By.id(messageDivId),testText));
        });

        appendError("Verify that messageElementId method works", () -> {
            // Reload page to clear previous configurations
            driver.get(testURL + "/thispagedoesnotexist/");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));

            // create a new element to receive the message
            String newDivId = "test-div";
            ((JavascriptExecutor) driver).executeScript("g = document.createElement('div'); g.setAttribute('id', '"+newDivId+"'); document.getElementById('"+messageDivId+"').parentElement.appendChild(g)");
            
            ((JavascriptExecutor) driver).executeScript("ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').messageElementId('"+newDivId+"').call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.visibilityOfElementLocated(By.id(newDivId)));
        });

        appendError("Verify that setDateFormatter method works", () -> {
            // Reload page to clear previous configurations
            driver.get(testURL + "/thispagedoesnotexist/");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));

            // We change the date formatter to only display the year, and the message to only display the date.
            //   This should make the message be just the year of the archived version.
            ((JavascriptExecutor) driver).executeScript(
                "ARQUIVO_NOT_FOUND_404" +
                ".url('https://www.arquivo.pt')" +
                ".setDateFormatter(date => '' + date.getFullYear())" +
                ".message('{date}')" +
                ".call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.visibilityOfElementLocated(By.id(messageDivId)));
            assertTrue(Integer.valueOf(driver.findElement(By.id(messageDivId)).getText()) > 1990); //Checks that the date is an integer greater than 1990
        });

        appendError("Verify that setMinimumDate method works", () -> {
            // Reload page to clear previous configurations
            driver.get(testURL + "/thispagedoesnotexist/");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));

            // create a new element to receive the message
            ((JavascriptExecutor) driver).executeScript(
                "ARQUIVO_NOT_FOUND_404" +
                ".url('https://www.arquivo.pt')" +
                ".setDateFormatter(date => '' + date.getFullYear())" +
                ".setMinimumDate(new Date('2010-01-01 GMT'))" +
                ".message('{date}')" +
                ".call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.textToBe(By.id(messageDivId),"2010"));
        });

        appendError("Verify that setMostRelevantMemento method works", () -> {
            // Reload page to clear previous configurations
            driver.get(testURL + "/thispagedoesnotexist/");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));

            // create a new element to receive the message
            ((JavascriptExecutor) driver).executeScript(
                "ARQUIVO_NOT_FOUND_404" +
                ".url('https://www.arquivo.pt')" +
                ".setDateFormatter(date => '' + date.getFullYear())" +
                ".setMostRelevantMemento('most-recent')" +
                ".message('{date}')" +
                ".call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.visibilityOfElementLocated(By.id(messageDivId)));
            assertTrue(Integer.valueOf(driver.findElement(By.id(messageDivId)).getText()) > 2010); //Checks that the date is an integer greater than 2010
        });

        appendError("Verify that setMaximumDate method works", () -> {
            // Reload page to clear previous configurations
            driver.get(testURL + "/thispagedoesnotexist/");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));

            // create a new element to receive the message
            ((JavascriptExecutor) driver).executeScript(
                "ARQUIVO_NOT_FOUND_404" +
                ".url('https://www.arquivo.pt')" +
                ".setDateFormatter(date => '' + date.getFullYear())" +
                ".setMostRelevantMemento('most-recent')" +
                ".setMaximumDate(new Date('2010-12-31 GMT'))" +
                ".message('{date}')" +
                ".call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.textToBe(By.id(messageDivId),"2010"));
        });

    }

}
