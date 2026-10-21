# Soft404MessageTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/Soft404MessageTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that navigating to a known-missing/expired Wayback capture URL on the live `arquivo.pt` domain shows the expected "soft 404" page: a message pointing to an earlier archived version of the page, a Wayback link to that earlier capture, a "Maybe try searching?" prompt, and a visible search widget.

## Preconditions
- Target env: `test.url` — this test explicitly **skips itself (no-op) when `test.url` contains `preprod.sobre.arquivo.pt`** (comment: "the test does not work in preprod... since Arquivo.pt only collects sobre.arquivo.pt"), i.e. it is only meaningful against the production-like/collected URL.
- No page objects used — this test drives `driver` directly with raw Selenium `By` locators (no `pages/*.java` classes involved).
- No fixture files loaded.
- Hardcoded path: `WAYBACK_404_PAGE_EXAMPLE = "/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview"`, appended to `testURL`.

## Test Data
- The page is shown in Portuguese or English, so the texts are checked in both languages.
- Expected earlier-capture message: `"Visite uma versão anterior desta página"` or `"Visit an earlier version of this page"` (checked via `containsString`).
- Expected Wayback link: matches `https://arquivo.pt/wayback/<14 digit timestamp>mp_/http://sobre.arquivo.pt/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview`. The link is added by a script that looks the page up on Arquivo.pt, which can link a different capture, so the timestamp isn't checked.
- Element locators: `By.id("post-9818")` (main soft-404 container, waited on via `waitUntilElementIsVisibleAndGet`, up to 40s), link at `//*[@id="post-9818"]/div/div/p[2]/div/a` (waited up to 20s), search message at `By.id("messageSearch")`, Google Custom Search embed at `By.id("___gcse_2")` (waited up to 20s).

## Steps & Expected Results

### `soft404MessageTest()`
1. Guard: if `testURL` contains `preprod.sobre.arquivo.pt` (case-insensitive), the test body is skipped entirely (no navigation, no assertions run).
2. Otherwise, navigate directly to `testURL + WAYBACK_404_PAGE_EXAMPLE`.
3. Wait until the element `#post-9818` is visible (up to 40s).
4. Wait (up to 20s) for the link at `//*[@id="post-9818"]/div/div/p[2]/div/a` to be visible and read its text.
5. Read the same link's `href` attribute.
6. Read the text of `#messageSearch`.
7. Wait (up to 20s) for the Google Custom Search element `#___gcse_2` to become visible.

**Expected results:**
- (soft/`appendError`) Link text contains `"Visite uma versão anterior desta página"` or `"Visit an earlier version of this page"` — message: `"Verify text from Arquivo.pt link"`.
- (soft/`appendError`) The link `href` matches `https://arquivo\.pt/wayback/\d{14}mp_/http://sobre\.arquivo\.pt/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview` — message: `"Check link to wayback, was <href>"`.
- (soft/`appendError`) `#messageSearch` text equals `"Experimente pesquisar:"` or `"Maybe try searching?"`.
- (soft/`appendError`, labeled `"Check if page is not archived"`) `#___gcse_2` becomes visible within 20 seconds.
- All soft-assertions are collected and only thrown together at `tearDown()` (via `AppendableErrorsBaseTest`), so a failure in one does not stop the others from being checked within the same test run.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, soft-404

## Notes
- **Environment-conditional test**: entirely no-op (all steps skipped, test trivially passes) when run against `preprod.sobre.arquivo.pt`. A native reimplementation must replicate this environment guard or explicitly decide to drop it.
- Uses `appendError` (soft assertions, from `AppendableErrorsBaseTest`) for all 4 checks, so the test can report several independent findings together, unlike most others in this package which use only hard `assertTrue`.
- The link only appears if Arquivo.pt has the page, so the test still depends on `sobre.arquivo.pt` captures of this path existing in Arquivo.pt.
- Does not use any `pages/*.java` page object — purely raw Selenium locators inline in the test class.
