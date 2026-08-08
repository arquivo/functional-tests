# SuggestionSiteTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/SuggestionSiteTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (**no** — `SuggestionSiteTest` is explicitly NOT listed in `pt.arquivo.tests.cms.suite.TestSuite`'s `@SuiteClasses`, unlike the other 9 tests documented in this batch)

## Purpose / Scenario
Fills in and submits the "Suggest a site" form on the Arquivo.pt about site (Collaborate section), including attempting to solve the Google reCAPTCHA challenge, in Portuguese only. The class-level Javadoc explicitly states: **"Test has no effect. Turned into a manual test."**

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` (entry point), `CollaboratePage` (navigated to via `index.goToCollaboratePage("PT")`, menu item `menu-item-4397`), `SuggestionPage` (navigated to via `collaborate.goToSuggestionSitePage()`, clicking `//*[@id="parent-fieldname-text"]/ul/li[2]/a`).
- No fixture files loaded.
- Hardcoded form input strings (see Test Data).
- English version is explicitly not implemented (`//TODO english version missing`).

## Test Data
- Form field values submitted (`SuggestionPage.sendSuggestion`):
  - "Sites" / URL textarea (`xpathSites`): `"[Test Selenium] Url field."`
  - Description input (`xpathDescription`): `"[Test Selenium] Description field."`
  - Email input (`xpathEmail`): `"[Test Selenium] Email field."`
- A reCAPTCHA-solving routine (`captchaRobot()`/`inicializar()`) attempts to click the reCAPTCHA checkbox and, if an image challenge appears, clicks images at fixed indices (0, 1, 5) in the challenge grid, then clicks "verify" — a brittle, best-effort automation of a live Google reCAPTCHA, looping until the checkbox is marked checked or an error state forces a retry.

## Steps & Expected Results

### `suggestionsSiteTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Navigate to the Collaborate page: `index.goToCollaboratePage("PT")`.
3. From there, navigate to the Suggestion form: `collaborate.goToSuggestionSitePage()`.
4. Call `sug.sendSuggestion("PT")`, which:
   a. Fills the Sites/URL, Description, and Email fields with the fixed test strings above.
   b. Runs `captchaRobot()` (see Test Data) attempting to solve the reCAPTCHA challenge via checkbox + image-tile clicks.
   c. Waits (`IndexSobrePage.sleepThread()`, 4s), then locates and clicks a submit button located via `By.id(xpathSendButton)` — note: `xpathSendButton` actually holds an XPath string (`//*[@id="wpcf7-f2384-p2063-o1"]/form/p/input`) but is passed to `By.id(...)` rather than `By.xpath(...)`, which is very likely a latent bug that would cause this locator lookup to fail (see Notes).

**Expected results:**
- `assertTrue("Failed The Suggestion Site Page Test in Portuguese", sug.sendSuggestion("PT"))` (hard) — `sendSuggestion` returning `true` (it returns `true` immediately after clicking the submit button if no `NoSuchElementException` was thrown along the way; there is no verification of a post-submit success message/state).
- If `IndexSobrePage` construction, `goToCollaboratePage`, or `goToSuggestionSitePage` throws `IOException`/`FileNotFoundException`, hard-fails with `fail("IOException -> suggestionsSiteTest")`.
- If any locator lookup inside `sendSuggestion` throws `NoSuchElementException` (uncaught upward beyond `SuggestionPage`'s own catch-and-rethrow), the test would error out rather than cleanly assert `false`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no (present at class level as an active `@Test`, despite the Javadoc calling it a "manual test")
- Runs across configured browser/device matrix
- Category tags: cms, suggestion-form
- **Not included in `TestSuite`** — call this out explicitly: of the 10 classes documented in this batch, `SuggestionSiteTest` is the one exception not wired into `pt.arquivo.tests.cms.suite.TestSuite`.

## Notes
- The class Javadoc says **"Test has no effect. Turned into a manual test"** — strongly suggesting this test is not expected to run reliably/automatically in CI, consistent with it being excluded from `TestSuite`.
- Automating a live Google reCAPTCHA (`captchaRobot`/`inicializar` in `SuggestionPage`) is inherently unreliable (image challenges, anti-automation detection) — this is almost certainly why the test is considered non-functional/manual-only. A native Playwright reimplementation should likely not attempt to replicate real reCAPTCHA solving, and should instead flag this scenario as needing a test-environment CAPTCHA bypass or an explicit manual/skip marker.
- Likely bug in `SuggestionPage.sendSuggestion()`: the submit button lookup uses `By.id(xpathSendButton)` where `xpathSendButton` is actually an XPath expression string, not an element ID — this mismatch would cause the element lookup to fail in practice.
- English-language submission is not implemented (`//TODO english version missing` in source) — only the Portuguese path is exercised.
- No fixture files are used; all form data is hardcoded literal placeholder text clearly marked as test data (`"[Test Selenium] ..."`), which is a sensible convention to preserve if this scenario is reimplemented.
- The `sendSuggestion` return value does not verify actual successful submission (e.g. a confirmation message) — it only confirms the form-fill and click sequence executed without a `NoSuchElementException`.
