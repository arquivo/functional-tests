package pt.arquivo.tests.webapp.utils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DatePicker {

    private final static String startId = "date-container-start";
    private final static String endId = "date-container-end";

    private final static String startInputId = "start-date";
    private final static String endInputId = "end-date";

    public static void openStart(WebDriver driver){
        DatePicker.open(driver,DatePicker.startId);
    }
    public static void openEnd(WebDriver driver){
        DatePicker.open(driver,DatePicker.endId);
    }
    public static void open(WebDriver driver, String id){
        new WebDriverWait(driver, Duration.ofSeconds(40)).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        ((JavascriptExecutor) driver).executeScript("document.querySelector('#'+arguments[0]+' > li.search-calendar-year > input').click()", id);
        
    }

    private static LocalDate stringToLocalDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static void changeTo(WebDriver driver, String date) {
        DatePicker.changeTo(driver, DatePicker.stringToLocalDate(date));
	}

    private static Boolean isDesktop(WebDriver driver){
        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatformName().name();
        String userAgent = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
        System.out.println("Platform: " + platform + " User agent: " + userAgent);
        return ( 
               ( platform.equals("LINUX") && !userAgent.contains("Android") ) 
            || platform.equals("WINDOWS") 
            || platform.equals("ANY")
            || ( platform.equals("MAC") && userAgent.contains("Macintosh") ) 
            );
    }

	public static void changeTo(WebDriver driver, LocalDate date) {
        if (DatePicker.isDesktop(driver)) {
            new WebDriverWait(driver, Duration.ofSeconds(40))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-datepicker-container")))
                .sendKeys(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            new WebDriverWait(driver, Duration.ofSeconds(40)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ap-cont")));
            DatePicker.mobileDatepickerChangeYearTo(driver,date.getYear());
            DatePicker.mobileDatepickerChangeMonthTo(driver,date.getMonth().ordinal());
            DatePicker.mobileDatepickerChangeDayTo(driver,date.getDayOfMonth());
        }
	}

    public static LocalDate getStartDate(WebDriver driver){
        return stringToLocalDate(getStartDateString(driver));
    }

    public static String getStartDateString(WebDriver driver){
        return getDateString(driver, DatePicker.startInputId);
    }

    public static LocalDate getEndDate(WebDriver driver){
        return stringToLocalDate(getEndDateString(driver));
    }

    public static String getEndDateString(WebDriver driver){
        return getDateString(driver, DatePicker.endInputId);
    }

    private static String getDateString(WebDriver driver, String inputId){
        String originalString = (String) ((JavascriptExecutor) driver).executeScript("return document.querySelector('#"+inputId+"').value");
        
        String year = originalString.substring(0, 4);
        String month = originalString.substring(4, 6);
        String day = originalString.substring(6, 8);
        return day + "/" + month + "/" + year;
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
        Integer selectedValue = mobileGetCurrentValue(driver,type);
        int steps = Math.abs(value-selectedValue);  
 
        for (int step = 0; step < steps; step++){
            DatePicker.changeValueByOne(driver, value > selectedValue, type);
            selectedValue = mobileGetCurrentValue(driver,type);
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

    private static void changeValueByOne(WebDriver driver, Boolean increment, char type){
        if(((RemoteWebDriver) driver).getCapabilities().getPlatformName().equals(Platform.IOS)){
            changeValueByOneIOS(driver, increment, type);
            return;
        }
        
        if(((RemoteWebDriver) driver).getCapabilities().getPlatformName().equals(Platform.ANDROID)){
            changeValueByOneAndroid(driver, increment, type);
            return;
        }

        throw new Error("Failed to change value by one: Unrecognized mobile platform: "+((RemoteWebDriver) driver).getCapabilities().getPlatformName() );
    }

    private static void changeValueByOneIOS(WebDriver driver, Boolean increment, char type){
        int startValue = mobileGetCurrentValue(driver,type);
        int endValue = startValue + (increment ? 1 : -1);
       
        String startSelector = "#ap-component-"+type+" .ap-row[data-value=\""+endValue+"\"]";
        String endSelector = "#ap-component-"+type+" .ap-row-selected";
        jsTouchAndDrag(driver,startSelector,endSelector);

        int currentValue = mobileGetCurrentValue(driver,type);

        if(currentValue == endValue){
            return;
        } 
        throw new Error("Failed to change value by one via touch and drag: Value changed unexpectedly. Started as [ " + startValue + " ], expected: [ " + endValue + " ] but got [ " + currentValue + " ]." );
    }

    private static void changeValueByOneAndroid(WebDriver driver, Boolean increment, char type){
        int startValue = mobileGetCurrentValue(driver,type);
        int endValue = startValue + (increment ? 1 : -1);
        int currentValue;

        String selector = "#ap-component-selector-" + type;
        Integer dy = increment ? -100 : 100;
        DatePicker.twoFingerDrag(driver,selector,dy);

        currentValue = mobileGetCurrentValue(driver,type);

        if(currentValue == endValue){
            return;
        }

        throw new Error("Failed to change value by one via moveMouseWheel: Value changed unexpectedly. Started as [ " + startValue + " ], expected: [ " + endValue + " ] but got [ " + currentValue + " ]." );

    }
    private static int mobileGetCurrentValue(WebDriver driver, char type){
        WebElement selectedWebElement = driver.findElement(By.cssSelector("#ap-component-"+type+" .ap-row-selected"));
        Integer selectedValue = Integer.parseInt(selectedWebElement.getAttribute("data-value"));
        return selectedValue;
    }

    // Touch and drag using selenium getLocation() function on web elements. Fails to properly find the elements on iOS, does not work at all on Android.
    // Leaving this here for future reference and in case they update Appium/Selenium to make this work properly.
    private static void touchAndDrag(WebDriver driver, WebElement start, WebElement end){
        Point source = start.getLocation();
        Point destination = end.getLocation();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), source.x, source.y));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(new Pause(finger, Duration.ofMillis(600)));
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), destination.x, destination.y));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        RemoteWebDriver rwd = ((RemoteWebDriver) driver);
        rwd.perform(Arrays.asList(sequence));

        // Pause for 1 second to make sure animations finish playing and any events that need to trigger do so.
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println("Exception while waiting: "+e);
        }
    }

    // Touch and drag using javascript to find the location of elements. Fails to properly find the elements on Android.
    private static void jsTouchAndDrag (WebDriver driver, String startSelector, String endSelector) {
        
        int startX = ((Number) ((JavascriptExecutor) driver).executeScript("var r = document.querySelector(arguments[0]).getBoundingClientRect(); return Math.floor(r.left + r.width/2)", startSelector)).intValue();
        int startY = ((Number) ((JavascriptExecutor) driver).executeScript("var r = document.querySelector(arguments[0]).getBoundingClientRect(); return Math.floor(r.top + r.height/2)", startSelector)).intValue();
        int endX   = ((Number) ((JavascriptExecutor) driver).executeScript("var r = document.querySelector(arguments[0]).getBoundingClientRect(); return Math.floor(r.left + r.width/2)", endSelector)).intValue();
        int endY   = ((Number) ((JavascriptExecutor) driver).executeScript("var r = document.querySelector(arguments[0]).getBoundingClientRect(); return Math.floor(r.top + r.height/2)", endSelector)).intValue();

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1);
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(new Pause(finger, Duration.ofMillis(600)));
        sequence.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        // sequence.addAction(new Pause(finger, Duration.ofMillis(1000)));
        RemoteWebDriver rwd = ((RemoteWebDriver) driver);
        rwd.perform(Arrays.asList(sequence));

        // Pause for 1 second to make sure animations finish playing and any events that need to trigger do so.
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println("Exception while waiting: "+e);
        }
        
    }

    /**
     * Uses javascript to make the browser think a two-finger drag was used over the element located by selector. 
     * In practice this means creating a 'wheel' event after moving the mouse over the element. deltaY represents the
     *   strength of the mouseWheel event, and is 100 (or -100) for most mouses. 
     * On mobile, a two finger drag is also handled as a 'wheel' event but with a variable deltaY.    
     * This method works well on Android but not on iOS. Also, usually iOS handles deltaY the opposite way od Android,  
     *   so 100 deltaY on Android is the same as -100 deltaY on iOS and vice-versa.
     * @param driver
     * @param cssSelector
     * @param deltaY
     */
    private static void twoFingerDrag(WebDriver driver, String cssSelector, int deltaY){
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
        ((JavascriptExecutor) driver).executeScript(script, cssSelector, deltaY);
        
        // Pause for 1 second to make sure animations finish playing and any events that need to trigger do so.
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println("Exception while waiting: "+e);
        }
    }
}
