package pt.fccn.mobile.arquivo.tests.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.mobile.arquivo.utils.LocaleUtils;

/**
 *
 * @author ivo.branco@fccn.pt
 *
 */
public class ImageSearchDirectUrlTest extends WebDriverTestBaseParallel {

    private static final String IMAGE_SEARCH_DIRECT_URL = "/image/search?size=all&type=&tools=off&safeSearch=on&query=fccn&btnSubmit=Search&dateStart=26%2F06%2F2007&dateEnd=27%2F06%2F2007";

    public ImageSearchDirectUrlTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void imageSearchDirectUrlNoLanguageTest() throws Exception {
        imageSearchDirectUrlTest(testURL + IMAGE_SEARCH_DIRECT_URL, Optional.empty());
    }

    @Test
    @Retry
    public void imageSearchDirectUrlPTTest() throws Exception {
    	imageSearchDirectUrlTest(testURL + IMAGE_SEARCH_DIRECT_URL + "&" + LocaleUtils.languagePTUrlQueryParameter(),
    			Optional.of("Imagens"));
    }

    @Test
    @Retry
    public void imageSearchDirectUrlENTest() throws Exception {
    	imageSearchDirectUrlTest(testURL + IMAGE_SEARCH_DIRECT_URL + "&" + LocaleUtils.languageENUrlQueryParameter(),
    			Optional.of("Images"));
    }

    private void imageSearchDirectUrlTest(String url, Optional<String> imageButtonText) {
        driver.get(url);

        WebElement firstImage = waitUntilElementIsVisibleAndGet(By.id("image-card-1"));
        assertNotNull("Should exist at least one image", firstImage);

        appendError(() -> {
            List<WebElement> photoDivList = driver.findElements(By.xpath("//*[@id=\"image-cards-container\"]/li"));
            assertEquals("Verify results count", 24, photoDivList.size());
        });

        appendError(() -> assertThat("Check image original origin/domain",
                driver.findElement(By.xpath("//*[@id=\"image-card-1\"]/ul/li[4]/a")).getText(),
                containsString("fccn.pt")));

        if (imageButtonText.isPresent()) {
            appendError(() -> assertEquals("Check page language by verifying images button text", imageButtonText.get(),
                    driver.findElement(By.id("search-form-images")).getText()));
        }
    }
}
