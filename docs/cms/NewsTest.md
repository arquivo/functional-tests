# NewsTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/NewsTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that all outbound links listed in the "News" section of the Arquivo.pt about site are reachable (resolve with HTTP 200 after following redirects), in both Portuguese and English.

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` (entry point), `NewsPage` (navigated to via `index.goToNewsPage()`, clicking menu item `menu-item-4388`).
- No fixture files loaded from `sobreTestsFiles/`.
- No notable hardcoded expected text; the check is purely link-reachability based.

## Test Data
- `NewsPage.checkNewsLinks()` locates a news-links container via XPath `//*[@id="<idDiv>"]/div/div/aside/div` where `idDiv` is `post-1857` (PT) or `post-2336` (EN), then collects all `<a>` tags inside it.
- Each link's `href` is checked via `AnalyzeURLs.linkExists(url)` (HTTP HEAD, follows redirects) then `AnalyzeURLs.checkOk(statusCode)` (must be HTTP 200).

## Steps & Expected Results

### `newsTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Navigate to the News page: `index.goToNewsPage()`.
3. Portuguese version: `news.checkNewsLinks("PT")` — collects `<a>` elements under `post-1857`'s aside div and verifies each resolves with HTTP 200.
4. English version: `news.checkNewsLinks("EN")` — internally calls `switchLanguage()` (clicks the English toggle) first, then collects `<a>` elements under `post-2336`'s aside div and verifies each resolves with HTTP 200.

**Expected results:**
- `assertTrue("Failed The News Page Test in Portuguese", ...)` (hard) — all PT news links must return HTTP 200.
- `assertTrue("Failed The News Page Test in English", ...)` (hard) — all EN news links must return HTTP 200.
- If `IndexSobrePage` construction or `goToNewsPage()` throws `IOException`/`FileNotFoundException`, hard-fails with `fail("IOException -> newsTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, news

## Notes
- No fixture-file dependency; expected link set is scraped live from the page rather than compared to a stored baseline.
- Same News page/check logic (`NewsPage.checkNewsLinks`) is also exercised inline as part of `NavigationTest`.
- Hardcoded WordPress post IDs (`post-1857`, `post-2336`) are brittle if CMS content changes.
- Link check uses `HttpURLConnection` HEAD requests with SSL verification disabled, 5s connect timeout, manual redirect following — a native Playwright reimplementation would need an explicit HTTP request per link rather than relying on browser navigation alone.
