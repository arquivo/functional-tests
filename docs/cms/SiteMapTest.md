# SiteMapTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/SiteMapTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that all outbound links listed on the "Site Map" page of the Arquivo.pt about site are reachable (resolve with HTTP 200 after following redirects), in both Portuguese and English.

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` (entry point), `SiteMapPage` (navigated to via `index.goToSiteMapPage()`, which hovers the "Ajuda" menu item `menu-item-4391` then clicks the sub-menu item `menu-item-4425`).
- No fixture files are actually consumed by this test's code path (see Notes — `SiteMapTopicsPT.txt` exists but is unreferenced).

## Test Data
- `SiteMapPage.checkSiteMap()` locates a container via XPath `//*[@id="<idDiv>"]/div/div/div` where `idDiv` is `post-2659` (PT) or, after `switchLanguage()`, `post-2764` (EN), then collects all `<a>` tags inside it.
- Each link's `href` is checked via `AnalyzeURLs.linkExists(url)` then `AnalyzeURLs.checkOk(statusCode)` (must be HTTP 200).

## Steps & Expected Results

### `SiteMTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Navigate to the Site Map page: `index.goToSiteMapPage()`.
3. Portuguese version: `siteMap.checkSiteMap("PT")` — collects `<a>` elements under `post-2659` and verifies each resolves with HTTP 200.
4. English version: `siteMap.checkSiteMap("EN")` — internally calls `switchLanguage()` first, then collects `<a>` elements under `post-2764` and verifies each resolves with HTTP 200.

**Expected results:**
- `assertTrue("Failed The SiteMap Page Test in Portuguese", ...)` (hard) — all PT site-map links must return HTTP 200.
- `assertTrue("Failed The SiteMap Page Test in English", ...)` (hard) — all EN site-map links must return HTTP 200.
- If `IndexSobrePage` construction or `goToSiteMapPage()` throws `IOException`/`FileNotFoundException`, hard-fails with `fail("IOException -> SiteMTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, sitemap

## Notes
- **Fixture file not used**: `src/test/resources/sobreTestsFiles/SiteMapTopicsPT.txt` (85 lines, e.g. "Acerca", "Funcionamento do Arquivo.pt", "Arquitectura", ...) exists in the resources but is not referenced by any code reachable from this test (or anywhere in the current source tree) — it appears to be an orphaned/legacy fixture, possibly intended for a stricter topic-list comparison that was never implemented or was later removed. Worth flagging when deciding what the native reimplementation should assert (link-health only vs. also verifying the expected topic list).
- No topic/content comparison is performed — only link reachability.
- Hardcoded WordPress post/menu IDs (`post-2659`, `post-2764`, `menu-item-4391`, `menu-item-4425`) are brittle if CMS content/menu structure changes.
