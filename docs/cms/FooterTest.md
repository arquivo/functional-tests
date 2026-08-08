# FooterTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/FooterTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that every link present in the site-wide footer widget of the Arquivo.pt about site is reachable (resolves with HTTP 200 after following redirects), in both Portuguese and English.

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` only — the test calls `index.checkFooterURLs(language)` directly (does not navigate away from the index page).
- No fixture files are actually read by the code path this test exercises (see Notes — `checkFooterURLs` does not load `FooterLinksPT.txt`/`FooterLinksEN.txt`).

## Test Data
- `IndexSobrePage.checkFooterURLs()` locates footer links via XPath `//*[@id="footer-widgets"]/div/div/div/aside/ul/li/a`.
- For each link, if its `href` does NOT start with `http://www.facebook.com/`, `https://www.facebook.com/`, or `https` generally, it is checked via `AnalyzeURLs.checkLink(url)` (HTTP HEAD, follows redirects, requires final status 200). Facebook/https links are skipped (marked `TODO` in source — not actually verified).

## Steps & Expected Results

### `footerTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Portuguese version: `index.checkFooterURLs("PT")` — collects all footer `<a>` elements and checks each non-Facebook/non-https link resolves with HTTP 200.
3. English version: `index.checkFooterURLs("EN")` — internally calls `switchLanguage()` (clicks English toggle) first, then repeats the same footer link check.

**Expected results:**
- `assertTrue("Failed The Footer Test in Portuguese", ...)` (hard) — all checked PT footer links must resolve OK.
- `assertTrue("Failed The Footer Test in English", ...)` (hard) — all checked EN footer links must resolve OK.
- If `IndexSobrePage` construction throws `IOException`, hard-fails with `fail("IOException -> footerTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, footer-links

## Notes
- **Fixture files not actually used by this test**: `sobreTestsFiles/FooterLinksPT.txt` and `FooterLinksEN.txt` exist and map link text to expected URLs, but they are consumed only by `IndexSobrePage.checkFooterLinks(language)` — a *different*, unused method. `FooterTest` calls `checkFooterURLs(language)` instead, which only checks link reachability (HTTP 200), not that the link text matches an expected URL. This is a notable discrepancy: the richer text→URL fixture-based check exists in the codebase but is dead code as far as this test suite is concerned.
- Links pointing to Facebook or any `https` URL are explicitly skipped from the reachability check (marked with a `TODO` comment in `IndexSobrePage.checkFooterURLs`), so HTTPS footer links are not actually validated by this test.
- A native Playwright reimplementation should decide whether to restore the stronger text-to-URL fixture comparison (using `FooterLinksPT.txt`/`FooterLinksEN.txt`) or preserve the current (weaker) reachability-only behavior.
