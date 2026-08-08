# ImageSearchDirectUrlTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/imagesearch/ImageSearchDirectUrlTest.java`
- **Category:** webapp > imagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that navigating directly to a fully-parameterized image-search URL (query, date range, size/type/safe-search flags, and optionally a language query parameter) renders correct results and, when a language is specified, that the UI text is localized correctly (PT/EN).

## Preconditions
- Target env comes from the `test.url` system property (e.g. https://arquivo.pt or https://preprod.arquivo.pt); `testURL` is prefixed onto the hardcoded relative URL.
- No external config/resource files loaded.
- Uses `pt.arquivo.utils.LocaleUtils.languagePTUrlQueryParameter()` / `languageENUrlQueryParameter()` to append `l=pt` / `l=en` to the URL.

## Test Data
- Base direct URL (constant `IMAGE_SEARCH_DIRECT_URL`): `/image/search?size=all&type=&tools=off&safeSearch=on&query=fccn&btnSubmit=Search&dateStart=26%2F06%2F2007&dateEnd=27%2F06%2F2007`
- No-language variant: base URL only, no text-localization assertions.
- PT variant: base URL + `l=pt`; expects images button text `Imagens`; expects results-estimate text containing `resultados desde 2007 até 2007`.
- EN variant: base URL + `l=en`; expects images button text `Images`; expects results-estimate text containing `results from 2007 to 2007`.
- Expected first result domain text contains `fccn.pt` (all variants).

## Steps & Expected Results

### `imageSearchDirectUrlNoLanguageTest()`
1. Navigate directly to `testURL + IMAGE_SEARCH_DIRECT_URL` (no language parameter).
2. Wait for the first image result (`#image-card-1`) to appear.
3. Read the first result's domain text (`#image-card-1/ul/li[4]/a`).

**Expected results:**
- "Should exist at least one image" — first image element is not null (hard — plain `assertNotNull`).
- "Check image original origin/domain" — first result text contains `fccn.pt` (soft/appendError).
- (No results-estimate or language-button checks — both `Optional`s are empty for this variant.)

### `imageSearchDirectUrlPTTest()`
1. Navigate directly to `testURL + IMAGE_SEARCH_DIRECT_URL + "&l=pt"`.
2. Wait for the first image result (`#image-card-1`) to appear.
3. Read `#estimated-results` text.
4. Read the first result's domain text.
5. Read the images button (`#search-form-images`) text.

**Expected results:**
- "Should exist at least one image" — first image element is not null (hard).
- "Verify results count" — `#estimated-results` text contains `resultados desde 2007 até 2007` (soft/appendError).
- "Check image original origin/domain" — first result text contains `fccn.pt` (soft/appendError).
- "Check page language by verifying images button text" — `#search-form-images` text equals `Imagens` (soft/appendError).

### `imageSearchDirectUrlENTest()`
1. Navigate directly to `testURL + IMAGE_SEARCH_DIRECT_URL + "&l=en"`.
2. Wait for the first image result (`#image-card-1`) to appear.
3. Read `#estimated-results` text.
4. Read the first result's domain text.
5. Read the images button (`#search-form-images`) text.

**Expected results:**
- "Should exist at least one image" — first image element is not null (hard).
- "Verify results count" — `#estimated-results` text contains `results from 2007 to 2007` (soft/appendError).
- "Check image original origin/domain" — first result text contains `fccn.pt` (soft/appendError).
- "Check page language by verifying images button text" — `#search-form-images` text equals `Images` (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (all three methods, retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, imagesearch, direct-url, localization, deep-link

## Notes
- All three test methods delegate to a shared private helper `imageSearchDirectUrlTest(url, imageButtonText, resultsEstimateText)` that takes `Optional<String>` params to make the language-specific assertions conditional.
- Exercises deep-linking directly via URL query parameters rather than interacting with the search UI — a good candidate to preserve as a direct `page.goto(url)` test in Playwright.
- Relies on fixed historical crawl data for `fccn.pt` around 26/27 June 2007.
