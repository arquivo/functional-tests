package pt.arquivo.tests.memorial;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import net.bytebuddy.asm.Advice.Enter;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.ReplayUtils;

/**
 * Regression memorial sites test.
 *
 * <p>
 * 1. Go to memorial site URL
 * </p>
 *
 * <p>
 * 2. Check some visible text on redirect page should contain some text to
 * inform user before redirect to Arquivo.pt
 * </p>
 *
 * <p>
 * 3. Click on button to redirect to Arquivo.pt
 * </p>
 * <p>
 * 4. Check some text on wayback page
 * </p>
 *
 * <p>
 * 5. Check wayback page url
 * </p>
 *
 * </ul>
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MemorialTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_PATH = "/wayback";

	public MemorialTest(Map<String, String> config) {
		super(config);
	}

	@Test
	public void testMemorialSites() throws Exception {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		MemorialTestConfigYAMLFile configsy = mapper.readValue(
				new InputStreamReader(ClassLoader.getSystemResourceAsStream("memorial_config.yaml")),
				MemorialTestConfigYAMLFile.class);

		Arrays.asList(configsy.getConfigs()).forEach(config -> {

			String url = config.getUrl();
			System.out.println(String.format("Begin checking url: %s with full configuration as: %s", url,
					ReflectionToStringBuilder.toString(config, ToStringStyle.MULTI_LINE_STYLE)));

			// go to memorial site URL
			driver.get(url);

			String redirectPageText = config.getRedirectPageText();
			if (redirectPageText != null) {
				appendError(() -> {
					assertThat(
							"Check some visible text on redirect page should contain some text to inform user before redirect to Arquivo.pt",
							driver.findElement(By.xpath("/html")).getText(),
							containsString(redirectPageText));
				});
			}

			// click() didn't work consistently on every browser, so the solution was to use
			// sendkeys method!
			run("Click on button to redirect to Arquivo.pt",() -> {
					driver.findElements(By.cssSelector("form > button")).stream().filter(button -> button.isDisplayed()).findFirst().orElse(null).click();
				});

			appendError("Check wayback page url",
					() -> new WebDriverWait(driver, Duration.ofSeconds(120)).until(
						ExpectedConditions.and(
							ExpectedConditions.urlContains(config.getTimestamp()),
							// ignore protocol and www
							ExpectedConditions.urlContains(config.getWaybackUrl().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)",""))
						)
					));

			run("Check some text on wayback page", () -> ReplayUtils.checkTextOnReplayPage(driver,
					config.getWaybackTextXPath(), config.getWaybackText()));

			System.out.println(String.format("End checking url: %s", url));

		});
	}

	public static class MemorialTestConfigYAMLFile {
		MemorialTestConfig[] configs;

		public MemorialTestConfig[] getConfigs() {
			return configs;
		}

		public void setConfigs(MemorialTestConfig[] configs) {
			this.configs = configs;
		}

	}

	public static class MemorialTestConfig {
		private String url;
		private String redirectPageText;
		private String timestamp;
		private String waybackUrl;
		private String waybackTextXPath;
		private String waybackText;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getRedirectPageText() {
			return redirectPageText;
		}

		public void setRedirectPageText(String redirectPageText) {
			this.redirectPageText = redirectPageText;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}

		public String getWaybackUrl() {
			if (this.waybackUrl != null) {
				return this.waybackUrl;
			} else {
				return getUrl();
			}
		}

		public void setWaybackUrl(String waybackUrl) {
			this.waybackUrl = waybackUrl;
		}

		public String getWaybackTextXPath() {
			return waybackTextXPath;
		}

		public void setWaybackTextXPath(String waybackTextXPath) {
			this.waybackTextXPath = waybackTextXPath;
		}

		public String getWaybackText() {
			return waybackText;
		}

		public void setWaybackText(String waybackText) {
			this.waybackText = waybackText;
		}

	}
}
