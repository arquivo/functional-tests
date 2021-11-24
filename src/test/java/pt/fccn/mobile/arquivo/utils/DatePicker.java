package pt.fccn.mobile.arquivo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class with utility method to deal with ionic date picker.
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class DatePicker {

    private static String startId = "date-container-start";
    private static String endId = "date-container-end";

    public static void openStart(WebDriver driver){
        DatePicker.open(driver,DatePicker.startId);
    }
    public static void openEnd(WebDriver driver){
        DatePicker.open(driver,DatePicker.endId);
    }
    public static void open(WebDriver driver, String id){
        new WebDriverWait(driver, 40).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        driver.findElement(By.id(id)).click();
    }

    private static LocalDate stringToLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static void changeTo(WebDriver driver, String date) {
        DatePicker.changeTo(driver, DatePicker.stringToLocalDate(date));
	}

    private static Boolean isDesktop(WebDriver driver){
        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatform().name();
        String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
        System.out.println("Platform: "+platform+" User Agent: "+userAgent);
        return ( (platform.equals("LINUX") && !userAgent.contains("Android") ) || platform.equals("WINDOWS") || platform.equals("ANY"));
    }

	public static void changeTo(WebDriver driver, LocalDate date) {
        if (DatePicker.isDesktop(driver)) {
            new WebDriverWait(driver, 40).until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-datepicker-container")));
            driver.findElement(By.id("modal-datepicker-input")).sendKeys(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            new WebDriverWait(driver, 40).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ap-cont")));
            DatePicker.mobileDatepickerChangeYearTo(driver,date.getYear());
            DatePicker.mobileDatepickerChangeMonthTo(driver,date.getMonth().ordinal());
            DatePicker.mobileDatepickerChangeDayTo(driver,date.getDayOfMonth());
        }
	}

    public static void setDatePicker(WebDriver driver, LocalDate date, String openerId){
        DatePicker.open(driver, openerId);
        DatePicker.changeTo(driver, date);
        DatePicker.confirmSelection(driver);
    }

    public static void setStartDatePicker(WebDriver driver, LocalDate date){
        DatePicker.setDatePicker(driver, date, DatePicker.startId);
    }

    public static void setEndDatePicker(WebDriver driver, LocalDate date){
        DatePicker.setDatePicker(driver, date, DatePicker.endId);
    }

    public static void setDatePicker(WebDriver driver, String date, String openerId){
        DatePicker.setDatePicker(driver, stringToLocalDate(date), openerId);
    }

    public static void setStartDatePicker(WebDriver driver, String date){
        DatePicker.setDatePicker(driver, date, DatePicker.startId);
    }

    public static void setEndDatePicker(WebDriver driver, String date){
        DatePicker.setDatePicker(driver, date, DatePicker.endId);
    }

    private static void mobileDatepickerChangeValueTo(WebDriver driver, Integer value, char type){
        WebElement selectedWebElement = driver.findElement(By.cssSelector("#ap-component-"+type+" .ap-row-selected"));
            Integer selectedValue = Integer.parseInt(selectedWebElement.getAttribute("data-value"));
            Integer counter = Math.abs(value - selectedValue);
            
            while (selectedValue != value && counter > 0){
                DatePicker.changeValueByOne(
                    driver,
                    value > selectedValue,
                    "#ap-component-selector-"+type
                );
                selectedWebElement = driver.findElement(By.cssSelector("#ap-component-"+type+" .ap-row-selected"));
                selectedValue = Integer.parseInt(selectedWebElement.getAttribute("data-value"));
                counter--;
            }
    }

    private static void mobileDatepickerChangeDayTo(WebDriver driver, Integer day){
        mobileDatepickerChangeValueTo(driver, day, '0');
    }
    private static void mobileDatepickerChangeMonthTo(WebDriver driver, Integer month){
        mobileDatepickerChangeValueTo(driver, month, '1');
    }
    private static void mobileDatepickerChangeYearTo(WebDriver driver, Integer year){
        mobileDatepickerChangeValueTo(driver, year, '2');
    }

	public static void confirmSelection(WebDriver driver) {
        if (DatePicker.isDesktop(driver)) {
            driver.findElement(By.id("modal-datepicker-confirm-button-span")).click();
        } else {
            driver.findElement(By.id("ap-button-set")).click();
        }
    }

    private static void changeValueByOne(WebDriver driver, Boolean increment, String selector){
        Integer dy = increment ? 100 : -100;
        DatePicker.moveMouseWheel(driver,selector,dy);
    }

    private static void moveMouseWheel(WebDriver driver, String selector, int deltaY)
    {
        try{
              String script = 
                 "var selector = arguments[0];"
                +"var deltaY = arguments[1];"
                +"var element = document.querySelector(selector);"
                +"var box = element.getBoundingClientRect();"
                +"var clientX = box.left + (box.width / 2);"
                +"var clientY = box.top + (box.height / 2);"
                +"var target = element.ownerDocument.elementFromPoint(clientX, clientY);"
                +"for (var e = target; e; e = e.parentElement) {"
                +    "if (e === element) {"
                +        "target.dispatchEvent(new MouseEvent('mouseover', {view: window, bubbles: true, cancelable: true, clientX: clientX, clientY: clientY}));"
                +        "target.dispatchEvent(new MouseEvent('mousemove', {view: window, bubbles: true, cancelable: true, clientX: clientX, clientY: clientY}));"
                +        "target.dispatchEvent(new WheelEvent('wheel',     {view: window, bubbles: true, cancelable: true, clientX: clientX, clientY: clientY, deltaY: deltaY}));"
                +        "return;"
                +    "}"
                +"}";  
              ((JavascriptExecutor) driver).executeScript(script, selector, deltaY);
            }
            catch(WebDriverException e)
            {
            System.out.println("Exception caught in Catch block");
            }
    }
}
