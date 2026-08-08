# MenuAboutWaybackTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/menu/MenuAboutWaybackTest.java`
- **Category:** webapp > replay > menu
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from an archived (wayback) page, opening the hamburger menu and clicking the "About" entry navigates to the correct locale-specific "about" page (`sobre.arquivo.pt`), for both Portuguese and English UI languages.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.
- Uses `LocaleUtils.changeLanguageToPT`/`changeLanguageToEN` (navigates to `testURL + "?l=pt"` / `"?l=en"` first) to set the UI language before loading the wayback page.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Expected "about" URL (PT): `https://sobre.arquivo.pt/pt/`
- Expected "about" URL (EN): `https://sobre.arquivo.pt/en/`
- Menu button: `#menuButton` (desktop: `#menuButton > span`, mobile: `#menuButton > div`)
- Language toggle button: `#changeLanguage` (text `"English"` or `"Português"`)
- About link selector: `div.swiper-slide:nth-child(1) > a:nth-child(9) > h4:nth-child(1)`

## Steps & Expected Results

### `menuAboutWaybackPTTest()`
1. Set UI language to Portuguese via `LocaleUtils.changeLanguageToPT`.
2. Navigate to the wayback example URL.
3. Run shared `menuAbout("https://sobre.arquivo.pt/pt/")` helper (see below).

### `menuAboutWaybackENTest()`
1. Set UI language to English via `LocaleUtils.changeLanguageToEN`.
2. Navigate to the wayback example URL.
3. Run shared `menuAbout("https://sobre.arquivo.pt/en/")` helper (see below).

### Shared helper `menuAbout(expectedUrl)`
1. Wait for and click the menu button (desktop `span` or mobile `div`, whichever is displayed).
2. Read the current text of the `#changeLanguage` button. If it doesn't match the expected locale (i.e. shows the wrong-language label), click it to switch language, then re-open the menu button again.
3. Click the "about" link (`div.swiper-slide:nth-child(1) > a:nth-child(9) > h4:nth-child(1)`).

**Expected results:**
- "Click menu button" — menu opens (hard, via `run`).
- "Change to correct language and click menu button again" — conditional step, only runs if the language toggle button shows the wrong language (hard, via `run`).
- "Click about button" — click succeeds (hard, via `run`).
- "Check if current url is the about page" — URL contains `expectedUrl` within 20s (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (both test methods; retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, menu, about-page, locale

## Notes
- Not a shared abstract base class in the OO sense, but both `@Test` methods delegate to a private shared method `menuAbout(String)` within this same class — documented once here.
- Locale handling: this test proactively checks/corrects the UI language, meaning it may click the language switcher even though `LocaleUtils` was already used to set language via URL param — a safety-net for cases where the `?l=` param didn't take effect.
- Desktop vs mobile branch for the menu button click is duplicated across menu tests in this package (see other files) — same pattern each time.
