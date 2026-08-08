# PageAdvancedSearchTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageAdvancedSearchTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the Advanced Search form correctly combines multiple filters at once — a date range, a file-format restriction (PDF), and a site restriction (`www.fccn.pt`) — into a single generated query, and that results and date-picker fields reflect all combined filters.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.
- Uses shared `DatePicker` utility (`pt.arquivo.utils.DatePicker`) to drive the custom date-picker widget (desktop text-input flow vs. mobile scroll-wheel flow, detected via platform/user-agent).

## Test Data
- Search term: `fccn`
- Start date: `31/05/2000` (comment in code says "31 may 2010" but the literal value passed is `31/05/2000`)
- End date: `01/01/2010` (comment says "1 jan 2019" but literal value passed is `01/01/2010`)
- Format filter: PDF
- Site filter (`#website`): `www.fccn.pt`
- Expected resulting query string: `fccn site:www.fccn.pt type:pdf`
- Expected `#start-date` value: `20000531`
- Expected `#end-date` value: `20100101`

## Steps & Expected Results

### `pageAdvancedSearchTest()`
1. Clear the search box, type `fccn`, and click search. (hard — via `run`)
2. Click the advanced-search link to navigate to the advanced search page. (hard — via `run`)
3. Read the `words` field value.
4. Set the start date picker to `31/05/2000` via `DatePicker.setStartDatePicker`. (hard — via `run`)
5. Set the end date picker to `01/01/2010` via `DatePicker.setEndDatePicker`. (hard — via `run`)
6. Uncheck "All formats".
7. Check "PDF" format.
8. Type `www.fccn.pt` into the site field (`#website`).
9. Click the advanced-search submit button.
10. Read the resulting value of the main search input.
11. Wait for `#pages-results` to appear.
12. Check that every result's mime label (`#pages-results span.mime`) contains `[pdf]`.
13. Check that every result under `#pages-results > ul > li:nth-child(2)` contains `fccn.pt`.
14. Read `#start-date` value (waited up to 20s via `WebDriverWait`/`ExpectedConditions.presenceOfElementLocated`).
15. Read `#end-date` value similarly.

**Expected results:**
- "Check if search words maintain fccn term" — `words` field equals `fccn` (soft/appendError).
- "Unselect 'All formats'" succeeds (soft/appendError).
- "Set format type to 'PDF'" succeeds (soft/appendError).
- "Set site" succeeds (soft/appendError).
- "Click on search on arquivo.pt button" succeeds (soft/appendError).
- "After advanced search check search term contains" — search input value equals `fccn site:www.fccn.pt type:pdf` (soft/appendError).
- "Verify that all returned mimetypes are PDF" — all result mime spans contain `[pdf]` (soft/appendError).
- "Verify that fccn.pt is present in all returned URLs" — all matching result rows' text contains `fccn.pt` (soft/appendError).
- "After advanced search check day start date contains" — `#start-date` value equals `20000531` (soft/appendError).
- "After advanced search check day end date contains" — `#end-date` value equals `20100101` (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, advanced-search, date-range, mime-type-filter, site-filter

## Notes
- Shares the "search fccn → open advanced search → verify words field" preamble with the other `PageAdvancedSearch*` tests.
- Discrepancy between code comments and actual literal date values (comments mention 2010/2019, code passes 2000/2010) — flag this when re-implementing so the new spec uses the literal (actual behavior) values, not the misleading comments.
- Combines three filter types (date range + format + site) in one flow, unlike the mimetype/negation/phrase tests which each isolate a single filter.
- Almost all assertions here are soft (`appendError`), so this test will surface every failing filter in one run rather than stopping at the first one.
