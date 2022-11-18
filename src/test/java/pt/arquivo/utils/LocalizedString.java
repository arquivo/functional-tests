package pt.arquivo.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class LocalizedString implements Function<Locale, String> {

	Map<Locale, String> m = new HashMap<>();

	public String put(Locale locale, String template) {
		return m.put(locale, template);
	}

	public LocalizedString add(Locale locale, String template) {
		put(locale, template);
		return this;
	}

	public LocalizedString pt(String s) {
		put(Locale.forLanguageTag("pt"), s);
		return this;
	}

	public LocalizedString en(String s) {
		put(Locale.ENGLISH, s);
		return this;
	}

	@Override
	public String apply(Locale locale) {
		return m.get(locale);
	}

}
