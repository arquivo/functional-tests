package pt.arquivo.tests.webapp.screenshot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.utils.AppendableErrorsBaseTest;
import pt.arquivo.utils.ImageUtils;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class ScreenshotTest extends AppendableErrorsBaseTest {

    private String testURL;

    public ScreenshotTest() {
        this.testURL = System.getProperty("test.url");
    }

    @Test
    @Retry
    public void screenshotTest() {

        String screenshotUrlStr = this.testURL + "/screenshot/?url=" + this.testURL
                + "/noFrame/replay/19961013145650/http://www.fccn.pt/&width=2560&height=1440";

        byte[] imageBytes = takeScreenshot(screenshotUrlStr);

        BufferedImage image = ImageUtils.readImage(imageBytes);

        assertEquals("Verify screenshot width", 2560, image.getWidth());
        assertEquals("Verify screenshot height", 1440, image.getHeight());
        assertTrue("Verify screenshot isn't blank", ImageUtils.nonWhitePercentage(image) > 0.5);

    }

    private byte[] takeScreenshot(String screenshotUrlStr) {
        URL url;
        try {
            url = new URL(screenshotUrlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Error generating URL to a screenshot of a page verify if you have passed correct test url parameter",
                    e);
        }

        byte[] imageBytes;
        try {
            InputStream is = url.openStream();
            imageBytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading a screenshot", e);
        }
        return imageBytes;
    }

}
