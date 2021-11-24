package pt.fccn.mobile.arquivo.utils;

import java.text.MessageFormat;
import java.util.Locale;

import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class LocaleUtils {

	public static Locale ENGLISH = Locale.ENGLISH;
	public static Locale PORTUGUESE = Locale.forLanguageTag("pt");

	private static final String EN_LANGUAGE = "en";
	private static final String PT_LANGUAGE = "pt";

	public static void changeLanguageTo(WebDriverTestBaseParalell test, Locale locale) {
		changeLanguageTo(test, locale.getLanguage());
	}

	public static void changeLanguageToPT(WebDriverTestBaseParalell test) {
		String language = PT_LANGUAGE;
		changeLanguageTo(test, language);
	}

	public static void changeLanguageToEN(WebDriverTestBaseParalell test) {
		String language = EN_LANGUAGE;
		changeLanguageTo(test, language);
	}

	private static void changeLanguageTo(WebDriverTestBaseParalell test, String language) {
		System.out.println(test.getTestURL());
		System.out.println(test.getTestURL() + "?" + "l=" + language);
		test.getDriver().get(test.getTestURL() + "?" + "l=" + language);
	}

	public static String languagePTUrlQueryParameter() {
		return "l=" + PT_LANGUAGE;
	}

	public static String languageENUrlQueryParameter() {
		return "l=" + EN_LANGUAGE;
	}

	public static String format(String template, Locale locale, Object... arguments) {
		return new MessageFormat(template, locale).format(arguments, new StringBuffer(), null).toString();
	}
}
