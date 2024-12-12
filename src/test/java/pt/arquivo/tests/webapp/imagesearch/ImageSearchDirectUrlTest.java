package pt.arquivo.tests.webapp.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import net.bytebuddy.matcher.StringMatcher;
import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.LocaleUtils;

/**
 *
 * @author ivo.branco@fccn.pt
 *
 */
public class ImageSearchDirectUrlTest extends WebDriverTestBaseParallel {

    private static final String IMAGE_SEARCH_DIRECT_URL = "/image/search?size=all&type=&tools=off&safeSearch=on&query=fccn&btnSubmit=Search&dateStart=26%2F06%2F2007&dateEnd=27%2F06%2F2007";

    public ImageSearchDirectUrlTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void imageSearchDirectUrlNoLanguageTest() throws Exception {
        imageSearchDirectUrlTest(testURL + IMAGE_SEARCH_DIRECT_URL, Optional.empty(), Optional.empty());
    }

    @Test
    @Retry
    public void imageSearchDirectUrlPTTest() throws Exception {
    	imageSearchDirectUrlTest(testURL + IMAGE_SEARCH_DIRECT_URL + "&" + LocaleUtils.languagePTUrlQueryParameter(),
    			Optional.of("Imagens"),
                Optional.of("resultados desde 2007 até 2007"));
    }

    @Test
    @Retry
    public void imageSearchDirectUrlENTest() throws Exception {
    	imageSearchDirectUrlTest(testURL + IMAGE_SEARCH_DIRECT_URL + "&" + LocaleUtils.languageENUrlQueryParameter(),
    			Optional.of("Images"),
                Optional.of("results from 2007 to 2007"));
    }

    private void imageSearchDirectUrlTest(String url, Optional<String> imageButtonText, Optional<String> resultsEstimateText) {
        driver.get(url);

        WebElement firstImage = waitUntilElementIsVisibleAndGet(By.id("image-card-1"));
        assertNotNull("Should exist at least one image", firstImage);

        if (resultsEstimateText.isPresent()) {
            appendError(() -> {
                WebElement resultsEstimate = driver.findElement(By.id("estimated-results"));
                assertThat("Verify results count", resultsEstimate.getText(), CoreMatchers.containsString(resultsEstimateText.get()));
            });
        }

        appendError(() -> assertThat("Check image original origin/domain",
                waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"image-card-1\"]/ul/li[4]/a")).getText().trim(),
                containsString("fccn.pt")));

        if (imageButtonText.isPresent()) {
            appendError(() -> assertEquals("Check page language by verifying images button text", imageButtonText.get(),
                    waitUntilElementIsVisibleAndGet(By.id("search-form-images")).getText().trim()));
        }
    }
}
