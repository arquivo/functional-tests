# Soft404MessageTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/Soft404MessageTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that navigating to a known-missing/expired Wayback capture URL on the live `arquivo.pt` domain shows the expected "soft 404" page: a message pointing to an earlier archived version of the page, a correct Wayback link to that earlier capture, a "Maybe try searching?" prompt, and a visible search widget.

## Preconditions
- Target env: `test.url` — this test explicitly **skips itself (no-op) when `test.url` contains `preprod.sobre.arquivo.pt`** (comment: "the test does not work in preprod... since Arquivo.pt only collects sobre.arquivo.pt"), i.e. it is only meaningful against the production-like/collected URL.
- No page objects used — this test drives `driver` directly with raw Selenium `By` locators (no `pages/*.java` classes involved).
- No fixture files loaded.
- Hardcoded path: `WAYBACK_404_PAGE_EXAMPLE = "/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview"`, appended to `testURL`.

## Test Data
- Expected earlier-capture message: `"Visit an earlier version of this page on 27 February, 2017"` (checked via `containsString`).
- Expected Wayback link: `https://arquivo.pt/wayback/20170227184149mp_/http://sobre.arquivo.pt/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview`.
- Element locators: `By.id("post-9818")` (main soft-404 container, waited on via `waitUntilElementIsVisibleAndGet`, up to 40s), link at `//*[@id="post-9818"]/div/div/p[2]/div/a`, search message at `By.id("messageSearch")`, Google Custom Search embed at `By.id("___gcse_2")` (waited up to 20s).

## Steps & Expected Results

### `soft404MessageTest()`
1. Guard: if `testURL` contains `preprod.sobre.arquivo.pt` (case-insensitive), the test body is skipped entirely (no navigation, no assertions run).
2. Otherwise, navigate directly to `testURL + WAYBACK_404_PAGE_EXAMPLE`.
3. Wait until the element `#post-9818` is visible (up to 40s).
4. Read the text of the link at `//*[@id="post-9818"]/div/div/p[2]/div/a`.
5. Read the same link's `href` attribute (via `findElements(...).get(0)`).
6. Read the text of `#messageSearch`.
7. Wait (up to 20s) for the Google Custom Search element `#___gcse_2` to become visible.

**Expected results:**
- (soft/`appendError`) Link text contains `"Visit an earlier version of this page on 27 February, 2017"` — message: `"Verify text from Arquivo.pt link"`.
- (hard) `assertEquals("Check link to wayback", href, "https://arquivo.pt/wayback/20170227184149mp_/http://sobre.arquivo.pt/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview")` — note: the actual and expected arguments are effectively swapped in the JUnit `assertEquals(message, expected, actual)` signature (source passes `href` as "expected" and the literal string as "actual"), which does not change pass/fail semantics but would print reversed diff wording on failure.
- (soft/`appendError`) `#messageSearch` text equals `"Maybe try searching?"` — note: the lambda only supplies one `assertEquals` argument (`getText()`), so this call is missing an explicit expected-value argument in source; effectively this assertion has no real comparison target as written (see Notes) — flagged as a probable latent bug.
- (soft/`appendError`, labeled `"Check if page is not archived"`) `#___gcse_2` becomes visible within 20 seconds.
- All soft-assertions are collected and only thrown together at `tearDown()` (via `AppendableErrorsBaseTest`), so a failure in one does not stop the others from being checked within the same test run.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, soft-404

## Notes
- **Environment-conditional test**: entirely no-op (all steps skipped, test trivially passes) when run against `preprod.sobre.arquivo.pt`. A native reimplementation must replicate this environment guard or explicitly decide to drop it.
- Uses `appendError` (soft assertions, from `AppendableErrorsBaseTest`) for 3 of the 4 checks, and a genuine hard `assertEquals` for the Wayback-link check — meaning this test can partially fail (multiple independent findings surfaced together) unlike most others in this package which use only hard `assertTrue`.
- The `messageSearch` assertion (`assertEquals("Maybe try searching?", driver.findElement(By.id("messageSearch")).getText())`) appears to be missing its second comparison argument — as written it calls the 2-arg `assertEquals(String message, Object actual)` overload comparing against `null` implicitly is not accurate; more likely this is simply a latent bug where the intended expected string was meant to be compared against the element's text but the argument list is short by one. Flag this for the reimplementation: the intended assertion is presumably "the element's text equals 'Maybe try searching?'".
- The Wayback capture timestamp (`20170227184149`) and date text (`27 February, 2017`) are hardcoded against one specific historical capture — this test will break if that capture is ever removed/re-indexed.
- Does not use any `pages/*.java` page object — purely raw Selenium locators inline in the test class.
