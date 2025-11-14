package pt.arquivo.tests.arquivo404;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

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
    public void checkThatPageHasArquivo404() {
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

    }

    @Test
    @Retry
    public void testUrlMethod() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that url method works", () -> {
            ((JavascriptExecutor) driver).executeScript("ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.visibilityOfElementLocated(By.id(messageDivId)));
        });
    }

    @Test
    @Retry
    public void testMessageMethod() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that message method works", () -> {
            String testText = "Test message";
            ((JavascriptExecutor) driver).executeScript("ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').message('"+testText+"').call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.textToBe(By.id(messageDivId),testText));
        });

    }

    @Test
    @Retry
    public void testMessageElementIdMethod() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that messageElementId method works", () -> {
            // create a new element to receive the message
            String newDivId = "test-div";
            ((JavascriptExecutor) driver).executeScript("g = document.createElement('div'); g.setAttribute('id', '"+newDivId+"'); document.getElementById('"+messageDivId+"').parentElement.appendChild(g)");
            
            ((JavascriptExecutor) driver).executeScript("ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').messageElementId('"+newDivId+"').call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.visibilityOfElementLocated(By.id(newDivId)));
        });

    }

    @Test
    @Retry
    public void testSetDateFormatter() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that setDateFormatter method works", () -> {
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

    }

    @Test
    @Retry
    public void testSetMinimumDate() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that setMinimumDate method works", () -> {
            // create a new element to receive the message
            ((JavascriptExecutor) driver).executeScript(
                "ARQUIVO_NOT_FOUND_404" +
                ".url('https://www.arquivo.pt')" +
                ".setDateFormatter(date => '' + date.getFullYear())" +
                ".setMinimumDate(new Date('2010-01-01'))" +
                ".message('{date}')" +
                ".call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.textToBe(By.id(messageDivId),"2010"));
        });

    }

    @Test
    @Retry
    public void testSetMostRelevantMemento() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });

        appendError("Verify that setMostRelevantMemento method works", () -> {
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

    }

    @Test
    @Retry
    public void testSetMaximumDate() {
        String messageDivId = "arquivo404-message";
        driver.get(testURL + "/thispagedoesnotexist/");
        appendError("Wait for 404 page to load", () -> {
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.presenceOfElementLocated(By.id(messageDivId)));
        });
        
        appendError("Verify that setMaximumDate method works", () -> {
            // create a new element to receive the message
            ((JavascriptExecutor) driver).executeScript(
                "ARQUIVO_NOT_FOUND_404" +
                ".url('https://www.arquivo.pt')" +
                ".setDateFormatter(date => '' + date.getFullYear())" +
                ".setMostRelevantMemento('most-recent')" +
                ".setMaximumDate(new Date('2010-12-31'))" +
                ".message('{date}')" +
                ".call();");
            new WebDriverWait(driver, Duration.ofSeconds(100)).until(ExpectedConditions.textToBe(By.id(messageDivId),"2010"));
        });

    }

}
