# SavePageNowURLNotFoundTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/savepagenow/SavePageNowURLNotFoundTest.java`
- **Category:** webapp > savepagenow
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that searching for a URL/query that has no archived results shows a "no results found" message offering to use SavePageNow/ArchivePageNow to save the missing page, and that clicking the offered link navigates to ArchivePageNow with the original query pre-filled in its search bar.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Relies on the searched query genuinely returning zero archived results (a nonsense domain is used to guarantee this).

## Test Data
- Search query (guaranteed to have no results): `ddadfcfe.cdsffds`
- Expected message text (either of, PT phrasing, tool name varies): `"Use o SavePageNow para gravar a página em falta"` OR `"Use o ArchivePageNow para gravar a página em falta"`

## Steps & Expected Results

### `savePageNowURLNotFound()`
Calls private helper `savePageNowURLNotFound("ddadfcfe.cdsffds")`.

1. Clear search input, type `ddadfcfe.cdsffds`, click search.
2. Wait for the "no results" element `#no-results-were-found` to be visible.
3. (soft) Assert text of `#not-found-message > ul > li:nth-child(3)` (CSS) contains either `"Use o SavePageNow para gravar a página em falta"` or `"Use o ArchivePageNow para gravar a página em falta"` — "Verify text from ArchivePageNow".
4. Click the link inside that same list item (`#not-found-message > ul > li:nth-child(3) > a`).
5. (soft) Assert search input (`#submit-search-input`) value equals the original query `ddadfcfe.cdsffds` — "Check that the query is placed on the search bar".

**Expected results:**
- (soft) "No results" message text matches one of two accepted phrasings mentioning SavePageNow/ArchivePageNow.
- (soft) After navigating via the offered link, the search bar on the destination page is pre-populated with the original search query.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, savepagenow, no-results, archivepagenow-integration

## Notes
- Accepts two different possible message texts (`CoreMatchers.anyOf(...)`) referencing either "SavePageNow" or "ArchivePageNow" — suggesting the tool/service was renamed at some point and the test tolerates both, or a locale/rollout inconsistency.
- Both verification steps are soft assertions; there is no hard assertion beyond implicit element-visibility waits.
- Uses a deliberately non-existent/garbage domain (`ddadfcfe.cdsffds`) as test data to reliably trigger the "not found" state without depending on real archive content gaps.
