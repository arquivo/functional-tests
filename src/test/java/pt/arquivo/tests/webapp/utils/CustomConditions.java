package pt.arquivo.tests.webapp.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CustomConditions {

   /**
   * Makes sure the context isn't an iframe, then uses <code>window.location.href</code> instead of <code>document.URL</code> to check if <code>fraction</code> is contained. On archived pages, <code>document.URL</code> returns the original URL, this method returns the current URL on the browser.
   */
    public static ExpectedCondition<Boolean> browserUrlContains(String fraction) {
        return (new ExpectedCondition <Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                d.switchTo().defaultContent();
                JavascriptExecutor j = (JavascriptExecutor) d;
                return Boolean.valueOf(j.executeScript("return window.location.href").toString().contains(fraction));
            }
        });
    }


    /**
     * Workaround for some webdrivers that break when we try to use <code>ExpectedConditions.invisibilityOfElementLocated()</code> on elements that aren't present. 
     */ 
    public static ExpectedCondition<Boolean> invisibilityOfElementLocatedById(String id) {
        return (new ExpectedCondition <Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                if(d.findElements(By.cssSelector("head, #"+id)).stream().count() == 1){
                    return Boolean.valueOf(true);
                } else {
                    return Boolean.valueOf(d.findElement(By.id(id)).isDisplayed());
                }
            }
        });
    }
}
