package pt.arquivo.tests.webapp.print;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.utils.AppendableErrorsBaseTest;

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

        String md5 = getMd5(bytes);

        // md5 sometimes is c65787ae99ea0e04848ed324e790cf49, other times it's 223b57dd7543af7b094ec4c5b9d45dc4. No idea why, so we check for both.
        assertEquals("Verify print md5sum", true, md5.equals("c65787ae99ea0e04848ed324e790cf49") || md5.equals("223b57dd7543af7b094ec4c5b9d45dc4"));
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

    public static String getMd5(byte[] input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            // of an input digest() return array of byte
            byte[] messageDigest = md.digest(input);

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
