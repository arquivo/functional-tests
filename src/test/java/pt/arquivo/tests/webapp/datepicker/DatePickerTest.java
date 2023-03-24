package pt.arquivo.tests.webapp.datepicker;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.DatePicker;

import static org.junit.Assert.assertEquals;

import java.util.Map;

/**
 * Test the datepicker interface
 *
 * @author vasco.rato@fccn.pt
 *
 */
public class DatePickerTest extends WebDriverTestBaseParallel {

    public DatePickerTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void datePickerTest() throws Exception {

        run("Set start date to 31 may 2010", () -> DatePicker.setStartDatePicker(driver, "31/05/2010"));

        run("Set end date to 1 jan 2012", () -> DatePicker.setEndDatePicker(driver, "01/01/2012"));

        assertEquals("Check that the start date was correctly set","31/05/2010", DatePicker.getStartDateString(driver));
        
        assertEquals("Check that the end date was correctly set","01/01/2012", DatePicker.getEndDateString(driver));

    }
}
