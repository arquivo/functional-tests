# PublicationsTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/PublicationsTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that all outbound links listed on the "Publications" page of the Arquivo.pt about site are reachable (resolve with HTTP 200 after following redirects), in both Portuguese and English.

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` (entry point), `PublicationsPage` (navigated to via `index.goToPublicationsPage("PT")` — hovers the Publications menu item then clicks the "Scientific Publications" sub-menu item; PT uses `menu-item-4390`/`menu-item-4401`, EN would use `menu-item-4395`/`menu-item-4428`, but the test always navigates in PT first — see Notes).
- No fixture files loaded from `sobreTestsFiles/`.
- No notable hardcoded expected text; the check is purely link-reachability based.

## Test Data
- `PublicationsPage.checkPubicationsLinks()` locates a publications container via XPath `//*[@id="post-2225"]/div/div/div` (PT) or, after calling `switchLanguage()`, `//*[@id="post-2814"]/div/div` (EN), then collects all `<a>` tags inside it.
- Each link's `href` is checked via `AnalyzeURLs.linkExists(url)` then `AnalyzeURLs.checkOk(statusCode)` (must be HTTP 200).

## Steps & Expected Results

### `publicationsTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Navigate to the Publications page in Portuguese: `index.goToPublicationsPage("PT")` (this single navigation is reused for both language checks below — the test does not navigate again before the English check).
3. Portuguese version: `publications.checkPubicationsLinks("PT")` — collects `<a>` elements under `post-2225` and verifies each resolves with HTTP 200.
4. English version: `publications.checkPubicationsLinks("EN")` — internally calls `switchLanguage()` (clicks the English toggle from the already-loaded PT publications page) then collects `<a>` elements under `post-2814` and verifies each resolves with HTTP 200.

**Expected results:**
- `assertTrue("Failed The Publications Page Test in Portuguese", ...)` (hard) — all PT publication links must return HTTP 200.
- `assertTrue("Failed The Publications Page Test in Portuguese", ...)` (hard) — note the English-check assertion message is also literally "...in Portuguese" (copy-paste artifact in source, not a functional issue) — all EN publication links must return HTTP 200.
- If `IndexSobrePage` construction or `goToPublicationsPage()` throws `IOException`/`FileNotFoundException`, hard-fails with `fail("IOException -> publicationsTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, publications

## Notes
- The test only calls `index.goToPublicationsPage("PT")` once; the English check relies entirely on `PublicationsPage.checkPubicationsLinks("EN")` internally switching the page language via the in-page toggle rather than re-navigating through the EN menu path — unlike some other tests (e.g. `NavigationTest`) which call the language-specific navigation method directly.
- The assertion message for the English check incorrectly says "Failed The Publications Page Test in **Portuguese**" — a copy-paste artifact; the actual language argument passed (`"EN"`) is correct.
- No fixture-file dependency; expected link set is scraped live rather than compared to a stored baseline.
- `ReportsPage` (a related page object with its own `checkReportsLinks`) is exercised separately, only as part of `NavigationTest`, not here.
- Hardcoded WordPress post IDs (`post-2225`, `post-2814`) are brittle if CMS content changes.
