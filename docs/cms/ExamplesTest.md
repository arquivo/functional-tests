# ExamplesTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/ExamplesTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that all outbound links listed on the "Examples of preserved pages" page of the Arquivo.pt about site are reachable (resolve with an HTTP 200 response, following redirects), in both Portuguese and English.

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` (entry point), `ExamplesPage` (navigated to via `index.goToExamplePage()`, clicking the "Examples" menu item `menu-item-4389`).
- No fixture files loaded from `sobreTestsFiles/`.
- No notable hardcoded expected text; the check is purely link-reachability based.

## Test Data
- `ExamplesPage.checkLinksExamples()` locates example-page links via XPath:
  - PT: `//*[@id="post-1861"]/div/div/div/div/div/div/p[2]/a`
  - EN: `//*[@id="post-2357"]/div/div/div/div/div/div/p[2]/a`
- Each link's `href` is checked via `AnalyzeURLs.linkExists(url)` (HTTP HEAD request, follows 301/302/303 redirects) then `AnalyzeURLs.checkOk(statusCode)` (must be HTTP 200).

## Steps & Expected Results

### `examplesTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Navigate to Examples page: `index.goToExamplePage()`.
3. Portuguese version: `examplePage.checkLinksExamples("PT")` — collects all `<a>` links under the PT post XPath and verifies each resolves with HTTP 200.
4. English version: `checkLinksExamples("EN")` — internally calls `switchLanguage()` (clicks the English toggle) then collects links under the EN post XPath and verifies each resolves with HTTP 200.

**Expected results:**
- `assertTrue("Failed The Example Page Test in Portuguese", ...)` (hard) — all PT example links must return HTTP 200 (after following redirects).
- `assertTrue("Failed The Example Page Test in English", ...)` (hard) — all EN example links must return HTTP 200.
- If `IndexSobrePage` construction throws `IOException`, hard-fails with `fail("IOException -> examplesTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, examples-links

## Notes
- No fixture-file dependency; all expected data (link list) is scraped live from the page itself, not compared against a stored baseline — the test only validates link health, not link identity/content.
- Link check uses `HttpURLConnection` HEAD requests with SSL verification disabled (`AnalyzeURLs.ignoreSSLCerts()`), a 5s connect timeout, and manual redirect following (not comparable 1:1 with Playwright's default navigation/response handling — a native reimplementation would need an explicit HTTP check per link, not just page navigation).
- Hardcoded WordPress post IDs (`post-1861`, `post-2357`) are brittle if CMS content changes.
