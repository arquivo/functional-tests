# SearchTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/SearchTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that the Google Custom Search (GCSE) box embedded on the Arquivo.pt about site returns results whose top hit's bolded snippet text matches the search term typed in, for a fixed list of search terms, in both Portuguese and English.

## Preconditions
- Target env: `test.url`.
- Page objects used: `SearchPage` (constructed directly, not via `IndexSobrePage` navigation — its constructor loads both fixture files immediately).
- Fixture files loaded: `src/test/resources/sobreTestsFiles/SearchLinksPT.txt` and `SearchLinksEN.txt` (both read in the `SearchPage` constructor; failure to load either throws `FileNotFoundException`).
- No page navigation occurs before search — the test operates on whatever page `driver` currently is (the index/home page from `setUp()`), relying on the GCSE widget being present there.

## Test Data
- Both `SearchLinksPT.txt` and `SearchLinksEN.txt` contain the same three literal terms (not localized): `FCCN`, `web`, `investigação`.
- Search input located via XPath `//*[@id="gsc-i-id1"]`; submit button via `//*[@id="___gcse_0"]/div/form/table/tbody/tr/td[2]/button`.
- Result check (`checkResults`) reads the text of the first `<b>` element on the page and asserts it case-insensitively equals the searched topic.

## Steps & Expected Results

### `searchingTest()`
1. Construct `SearchPage` directly from the current driver (loads `SearchLinksPT.txt` into `topicsPT` and `SearchLinksEN.txt` into `topicsEN`).
2. Portuguese version: `search.checkSearch("PT")` → for each of the 3 PT topics: clear and type the topic into the GCSE input, wait ~4s, click the search button, wait ~6s, then assert the first bolded (`<b>`) result text equals the topic (case-insensitive).
3. English version: `search.checkSearch("EN")` → same flow (`searchEN`) using the 3 EN topics (identical strings to PT, since the fixture content is the same) against the same GCSE widget (no explicit page/language navigation before this).

**Expected results:**
- `assertTrue("Failed The Search Test in Portuguese", ...)` (hard) — for every PT topic, the first bold snippet on the search results page must equal that topic string.
- `assertTrue("Failed The Search Test in English", ...)` (hard) — same check for every EN topic.
- If `SearchPage` construction throws `IOException` (including `FileNotFoundException` from a missing fixture), hard-fails with `fail("IOException -> SearchingTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, search

## Notes
- Depends on fixture files `SearchLinksPT.txt` and `SearchLinksEN.txt` — both currently contain identical, non-localized content (`FCCN`, `web`, `investigação`), so the "PT" and "EN" search passes exercise the exact same three search terms.
- No explicit page navigation or language switch occurs in this test before searching — it relies on whatever page is currently loaded (from `setUp()`) already exposing the Google Custom Search embed (`___gcse_0` / `gsc-i-id1`).
- `SearchPage` also contains a large commented-out alternate implementation of `checkResults` (using `___gcse_0` div structure and multiple result rows) — dead code, not used by the current flow.
- Relies on fixed `Thread.sleep`-based waits (4s after typing, 6s after clicking) rather than explicit conditions for search results to load — a likely source of flakiness, and something a Playwright reimplementation should replace with proper wait-for-response/locator conditions.
- The result check only inspects the *first* `<b>` element on the page, not scoped to the search results container specifically, which could pick up unrelated bold text.
