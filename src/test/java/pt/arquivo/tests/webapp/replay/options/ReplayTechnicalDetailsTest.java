package pt.arquivo.tests.webapp.replay.options;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayTechnicalDetailsTest extends WebDriverTestBaseParallel {

        private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

        public ReplayTechnicalDetailsTest(Map<String, String> config) {
                super(config);
        }

        public String getBaseServiceUrl() {
                return this.testURL.replace("http://", "https://");
        }

        public String getBaseServiceHost() {
                try {
                        return new URL(getBaseServiceUrl()).getHost();
                } catch (MalformedURLException e) {
                        throw new RuntimeException("Error getting host of the test url", e);
                }
        }

        // In Solr URLs are generated from the SURTs, so HTTPS is always assumed and www is removed
        private String convertUrToDefaultSolrFormat(String url){
                return url
                        .replace("http://","https://")
                        .replace("https://www.", "https://");
        }

        @Test
        // @Retry
        public void replayTecnicalDetailsTest() {
                driver.get(this.testURL + WAYBACK_EXAMPLE);

                run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

                run("Click on tecnical details anchor", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#a_moreinfo > h4:nth-child(1)")).click());

                waitUntilElementIsVisibleAndGet(By.id("uglipop_popbox"));

                appendError(() -> assertThat(
                        "Check originalURL",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"originalURL\")]/a")).getText().trim(),
                        CoreMatchers.containsString("fccn.pt")
                ));

                appendError(() -> assertThat(
                        "Check linkToArchive", 
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"linkToArchive\")]/a")).getText().trim(),
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo(this.testURL + WAYBACK_EXAMPLE),
                                CoreMatchers.equalTo(this.testURL + convertUrToDefaultSolrFormat(WAYBACK_EXAMPLE))
                        )
                ));

                appendError(() -> assertEquals(
                        "Check tstamp", 
                        "tstamp: 19961013145650",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"tstamp\")]")).getText().trim()
                ));

                // Solr content length is calculated differently
                appendError(() -> assertThat(
                        "Check contentLength",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"contentLength\")]")).getText().trim(), 
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo("contentLength: 3760"),
                                CoreMatchers.equalTo("contentLength: 1373")
                        )

                ));
                // Solr digest is calculated differently
                appendError(() -> assertThat(
                        "Check digest", 
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"digest\")]")).getText().trim(),
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo("digest: b5f96e1014f99bbd9ef0277cde883f37"),
                                CoreMatchers.equalTo("digest: OWMAVER7CCNJWL2E5ZURDDKGCHWS7JJO")
                        )
                ));

                appendError(() -> assertEquals(
                        "Check mimeType",
                        "mimeType: text/html",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"mimeType\")]")).getText().trim()
                ));

                // Removed encoding and date support with Solr

                // appendError(() -> assertEquals(
                //         "Check encoding", 
                //         "encoding: windows-1252",
                //         waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[7]")).getText().trim()
                // ));

                // appendError(() -> assertEquals(
                //         "Check date", 
                //         "date: 0845218610",
                //         waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[8]")).getText().trim()
                // ));

                appendError(() -> {
                        try {
                                assertThat(
                                        "Check linkToScreenshot", 
                                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"linkToScreenshot\")]/a")).getText().trim(),
                                        CoreMatchers.anyOf(
                                                CoreMatchers.equalTo(this.testURL + "/screenshot?url=" + URLEncoder.encode(this.testURL, StandardCharsets.UTF_8.toString()) + "%2FnoFrame%2Freplay%2F19961013145650%2Fhttp%3A%2F%2Fwww.fccn.pt%2F"),
                                                CoreMatchers.equalTo(this.testURL + "/screenshot?url=" + URLEncoder.encode(this.testURL, StandardCharsets.UTF_8.toString()) + "%2FnoFrame%2Freplay%2F19961013145650%2Fhttps%3A%2F%2Ffccn.pt%2F")
                                        )
                                );
                        } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException("Error calculating screenshot expected URL.", e);
                        }
                });

                appendError(() -> assertThat(
                        "Check linkToNoFrame:", 
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"linkToNoFrame\")]/a")).getText().trim(),
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo(this.testURL + "/noFrame/replay/19961013145650/http://www.fccn.pt/"),
                                CoreMatchers.equalTo(this.testURL + convertUrToDefaultSolrFormat("/noFrame/replay/19961013145650/http://www.fccn.pt/"))
                        )
                ));

                appendError(() -> assertThat(
                        "Check linkToExtractedText",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"linkToExtractedText\")]/a")).getText().trim(),
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo(this.testURL + "/textextracted?m=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650"),
                                CoreMatchers.equalTo(this.testURL + "/textextracted?m=https%3A%2F%2Ffccn.pt%2F%2F19961013145650")
                        )
                ));

                appendError(() -> assertThat(
                        "Check linkToMetadata", 
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"linkToMetadata\")]/a")).getText().trim(),
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo(this.testURL + "/textsearch?metadata=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650"),
                                CoreMatchers.equalTo(this.testURL + "/textsearch?metadata=https%3A%2F%2Ffccn.pt%2F%2F19961013145650")
                        )
                ));

                appendError(() -> assertThat(
                        "Check linkToOriginalFile",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"linkToOriginalFile\")]/a")).getText().trim(),
                        CoreMatchers.anyOf(
                                CoreMatchers.equalTo(this.testURL + "/noFrame/replay/19961013145650id_/http://www.fccn.pt/"),
                                CoreMatchers.equalTo(this.testURL + "/noFrame/replay/19961013145650id_/https://fccn.pt/")
                        )
                ));

                // Disabling snippet check because it's dependent on the query on solr

                // appendError(() -> assertEquals(
                //         "Check snippet", 
                //         "snippet: Fundação para a Computação Científica Nacional \" A promoção de infraestruturas no domínio da ...",
                //         waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"snippet\")]")).getText().trim()
                // ));

                appendError(() -> assertThat(
                        "Check fileName", 
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"fileName\")]")).getText().trim(),
                        CoreMatchers.containsString("fileName: AWP-Roteiro-20090510220155-00000")
                ));

                appendError(() -> assertEquals(
                        "Check collection", 
                        "Roteiro",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"collection\")]/a")).getText().trim()));

                appendError(() -> assertEquals(
                        "Check offset", 
                        "offset: 45198",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"offset\")]")).getText().trim()
                ));

                appendError(() -> assertEquals(
                        "Check statusCode", 
                        "statusCode: 200",
                        waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[contains(strong,\"statusCode\")]")).getText().trim()
                ));

                appendError("Close technical detail modal", () -> waitUntilElementIsVisibleAndGet(By.id("removeModal")).click());

                appendError("Check that tecnical details modal is closed when clicking on close button",
                                () -> new WebDriverWait(driver, Duration.ofSeconds(20))
                                                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_popbox"))));
        }
}