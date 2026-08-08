# PageSearchTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that a basic search combining a free-text term with a `collection:` filter returns a properly formatted estimated-results message covering the full archive date range (1991 to the current year), and that a strong majority (at least 80%) of the returned results are actually relevant to the search term.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.
- Uses the JVM's current-year value at run time (via `java.util.Calendar.getInstance().get(Calendar.YEAR)`), so the expected message text changes depending on the date the test is executed.

## Test Data
- Search query: `fccn collection:Roteiro`
- Expected estimated-results message substrings: `Cerca de ` and ` resultados desde 1991 até <current year>` (Portuguese locale wording; `<current year>` computed at test run time)
- Relevance keyword checked in results: `fccn`
- Relevance threshold: at least 80% of `.page-search-result` elements must contain "fccn" (case-insensitive)

## Steps & Expected Results

Shared helper `pageSearch(String query)` used by the test method:
1. Clear the search box (`#submit-search-input`), type the query, and click the search button (`#submit-search`). (hard — via `run`)
2. Wait for `#pages-results` to appear.
3. Read the estimated-results text (`#estimated-results`).
4. Count total `.page-search-result` elements and how many of them contain "fccn" (case-insensitive).
5. Print `relevantResults / totalResults` to stdout for diagnostics.

### `pageSearchTest()`
1. Calls `pageSearch("fccn collection:Roteiro")`, executing the shared flow above with that query.

**Expected results:**
- "Verify if the estimated results count message is displayed on page search" — text contains both `Cerca de ` and ` resultados desde 1991 até <current year>` (soft/appendError, using `allOf`).
- "At least 80 percent of results should show something related to search criteria" — `10 * relevantResults >= 8 * totalResults` (hard — plain `assertTrue`, not wrapped in `run`/`appendError`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, relevance, collection-filter, date-range

## Notes
- Hardcoded current-year logic: the expected message uses `Calendar.getInstance().get(Calendar.YEAR)` at test execution time, so the "until year" portion of the expected string is dynamic, not a fixed literal — the Playwright rewrite should compute this the same way (current year) rather than hardcoding a specific year.
- 80% relevance threshold (`10*relevantResults >= 8*totalResults`, i.e. `relevantResults/totalResults >= 0.8`) is a magic-number tolerance for real-world search relevance rather than an exact match — reflects that this test runs against live/real archive content, not a fixed fixture.
- The relevance assertion is hard (not `appendError`), so a relevance failure stops the test without any further checks (though there are none after it anyway).
- A commented-out stray line (`// 	`) sits between the two methods with no content — dead/vestigial, not functionally meaningful.
- Uses the `collection:Roteiro` search operator alongside the free-text `fccn` term, distinguishing this test's query complexity from the plain single-term searches in `PageSearchNotSpamTest`/`PageSearchQuerySuggestionTest`.
