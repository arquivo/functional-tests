package pt.arquivo.tests.webapp.menu;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.LocaleUtils;
import pt.arquivo.utils.LocalizedString;

/**
 *
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class MenuChangeLanguageTest extends WebDriverTestBaseParallel {

    public MenuChangeLanguageTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void menuChangeLaguagePTtoENTest() {
        LocaleUtils.changeLanguageToPT(this);
        menuChangeLaguageTest(LocaleUtils.PORTUGUESE);
    }

    @Test
    @Retry
    public void menuChangeLaguageENtoPTTest() {
        LocaleUtils.changeLanguageToEN(this);
        menuChangeLaguageTest(LocaleUtils.ENGLISH);
    }

    private void menuChangeLaguageTest(Locale locale) {

        //regular verification

        String pageLabel = new LocalizedString().pt("Páginas").en("Pages").apply(locale);

        assertThat("Verify page label",
                waitUntilElementIsVisibleAndGet(By.id("search-form-pages")).getText().trim(), containsString(pageLabel));

        String ImageLabel = new LocalizedString().pt("Imagens").en("Images").apply(locale);

        assertThat("Verify image label",
                waitUntilElementIsVisibleAndGet(By.id("search-form-images")).getText().trim(), containsString(ImageLabel));

        String advancedLabel = new LocalizedString().pt("Pesquisa avançada").en("Advanced search").apply(locale);

        assertThat("Verify advanced search label",
                waitUntilElementIsVisibleAndGet(By.id("search-form-advanced")).getText().trim(), containsString(advancedLabel));

        run("Open menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click());

        String languageLabel = new LocalizedString().pt("English").en("Português").apply(locale);

        assertThat("Verify language label",
                waitUntilElementIsVisibleAndGet(By.id("menu-language")).getText().trim(), containsString(languageLabel));

        run("Change language", () -> waitUntilElementIsVisibleAndGet(By.id("menu-language")).click());

        String newLanguageUrlLabel = new LocalizedString().pt("l=en").en("l=pt").apply(locale);

        run("Wait for page to change", () -> new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(newLanguageUrlLabel)));

        //opposite verification after changing the language

        String pageLabel_changeLang = new LocalizedString().pt("Pages").en("Páginas").apply(locale);

        assertThat("Verify page label",
                waitUntilElementIsVisibleAndGet(By.id("search-form-pages")).getText().trim(), containsString(pageLabel_changeLang));

        String ImageLabel_changeLang = new LocalizedString().pt("Images").en("Imagens").apply(locale);

        assertThat("Verify image label",
                waitUntilElementIsVisibleAndGet(By.id("search-form-images")).getText().trim(), containsString(ImageLabel_changeLang));

        String advancedLabel_changeLang = new LocalizedString().pt("Advanced search").en("Pesquisa avançada").apply(locale);

        assertThat("Verify advanced search label",
                waitUntilElementIsVisibleAndGet(By.id("search-form-advanced")).getText().trim(), containsString(advancedLabel_changeLang));

        run("Open menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click());

        String languageLabel_changeLang = new LocalizedString().pt("Português").en("English").apply(locale);

        assertThat("Verify language label",
                waitUntilElementIsVisibleAndGet(By.id("menu-language")).getText().trim(), containsString(languageLabel_changeLang));

    }

}
