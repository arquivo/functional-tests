# PageAdvancedSearchMimeTypeTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageAdvancedSearchMimeTypeTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that a user can perform a basic search, jump into the Advanced Search form, restrict results to a single file format (PDF), and that both the resulting search-box query string and the search results themselves correctly reflect the `type:pdf` filter.

## Preconditions
- Target env comes from the `test.url` system property (e.g. https://arquivo.pt or https://preprod.arquivo.pt), loaded automatically by the base class before the test runs.
- No external config/resource files are loaded by this test.
- Hardcoded query term `fccn` is used as the base search.

## Test Data
- Search term: `fccn`
- Expected advanced-search word field value: `fccn`
- Format filter: PDF (checkbox `input[type=checkbox][format=pdf]`), after unchecking "All formats" (`input[type=checkbox][format=all]`)
- Expected resulting query string in the search box: `fccn type:pdf`
- Expected mime label of the first result: `[PDF]`

## Steps & Expected Results

### `pageAdvancedSearchMimeTypeTest()`
1. Clear the search box (`#submit-search-input`), type `fccn`, and click the search button (`#submit-search`). (hard — via `run`)
2. Click the "advanced search" link/button (`#search-form-advanced button`) to navigate to the advanced search page. (hard — via `run`)
3. Read the `words` field value.
4. Uncheck the "All formats" checkbox.
5. Check the "PDF" format checkbox.
6. Click the advanced-search submit button (`#advanced-search-form-pages fieldset section[2] button`).
7. Read the resulting value of the main search input.
8. Wait for `#pages-results` to appear.
9. Check whether any result text contains "fccn".
10. Read the mime-type label of the first result item.

**Expected results:**
- "Check if search words maintain fccn term" — `words` field value equals `fccn` (soft/appendError).
- "Unselect 'All formats'" click succeeds (soft/appendError, no explicit assertion — just wraps the click).
- "Set format type to 'PDF'" click succeeds (soft/appendError, no explicit assertion — just wraps the click).
- "Click on search on arquivo.pt button" succeeds (soft/appendError, no explicit assertion — just wraps the click).
- "Verify if the - operator is on text box" — search input value trimmed equals `fccn type:pdf` (soft/appendError).
- "Verify if the term fccn is displayed on any search result" — at least one `#pages-results > ul > li` result's text contains "fccn" (hard — plain `assertTrue`, not wrapped in `run`/`appendError`).
- "Check mime of first result" — mime span text of the first result (`#pages-results/ul[1]/li[2]/a/span`) trims to `[PDF]` (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, advanced-search, mime-type-filter

## Notes
- Shares the identical "search fccn → open advanced search → verify words field" preamble with the other `PageAdvancedSearch*` tests in this package.
- The "fccn is displayed" assertion (step 9) is a hard `assertTrue`, not wrapped in `appendError`/`run`, so a failure there would abort the test immediately without evaluating the final mime-type check.
- XPath locators are brittle/positional (e.g. `fieldset/section[2]/button`, `ul[1]/li[2]/a/span`) — worth flagging for the Playwright rewrite as candidates for more robust selectors.
