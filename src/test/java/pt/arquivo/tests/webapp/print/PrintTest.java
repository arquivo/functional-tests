package pt.arquivo.tests.webapp.print;

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
public class PrintTest extends AppendableErrorsBaseTest {

    private String testURL;

    public PrintTest() {
        this.testURL = System.getProperty("test.url");
    }

    @Test
    @Retry
    public void printTest() {
        String screenshotUrlStr = this.testURL + "/screenshot?url=" + this.testURL
                + "/noFrame/replay/19961013145650/http://www.fccn.pt/&download=false";

        System.out.println("Test URL: "+screenshotUrlStr);

        byte[] bytes = print(screenshotUrlStr);

        // The default size differs between environments, so only check it's a non blank image
        BufferedImage image = ImageUtils.readImage(bytes);

        assertTrue("Verify print isn't blank", ImageUtils.nonWhitePercentage(image) > 0.5);
    }

    private byte[] print(String screenshotUrlStr) {
        URL url;
        try {
            url = new URL(screenshotUrlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Error generating URL to a screenshot of a page verify if you have passed correct test url parameter",
                    e);
        }

        byte[] bytes;
        try {
            InputStream is = url.openStream();
            bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading a screenshot", e);
        }
        return bytes;
    }

}
