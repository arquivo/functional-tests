# PageSearchLimitedDatesFromHomepageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchLimitedDatesFromHomepageTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that a user can restrict a search to a specific date range directly from the homepage date pickers (without going through the advanced search page), and that the results — estimated count message, every result's timestamp, the first result's URL/title, and the version date/summary of the archived `fccn.pt` home page — reflect that date range and search term correctly.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- Uses `DatePicker` utility for setting start/end dates via the homepage widgets.
- Assumes a specific, stable archived page for `fccn.pt` exists at timestamp `19961013145650` with known version date/summary content, and that it is returned somewhere in the results for this query/date range (test data is tied to the real archive content, not a fixture/mock).

## Test Data
- Search term: `fccn`
- Start date: `12/10/1996`
- End date: `01/01/1997`
- Expected estimated-results message substrings: `Cerca de ` and ` resultados desde 1996 até 1997` (Portuguese locale wording)
- Accepted result timestamp range (`data-tstamp` attribute of every result): from `19961012` (inclusive) to `19970102` (exclusive), i.e. 12 October 1996 to 1 January 1997, compared as strings
- Expected first result URL substring (`data-url` attribute and `.results-url` text): `fccn.pt`
- `fccn.pt` home page result: the result whose `data-tstamp` is `19961013145650` (located anywhere in the list)
- Expected home page result "version" date substring (PT, `.results-date`): `13 Outubro 1996`
- Expected home page result summary substring (`.results-summary`): `Av. Brasil`

## Steps & Expected Results

### `pageSearchLimitedDatesFromHomepageTest()`
1. Clear the search box and type `fccn` (note: does NOT click search yet at this point). (hard — via `run`)
2. Set start date picker to `12/10/1996` via `DatePicker.setStartDatePicker`. (hard — via `run`)
3. Set end date picker to `01/01/1997` via `DatePicker.setEndDatePicker`. (hard — via `run`)
4. Click the search button (`#submit-search`). (hard — via `run`)
5. Wait for `#pages-results` to appear. (hard — via `run`)
6. Read the estimated-results text (`#estimated-results`).
7. Collect all results (`#pages-results > ul`); verify at least one exists and read each one's `data-tstamp` attribute.
8. Locate the first result (`#pages-results > ul:first-of-type`, waited until visible); read its `data-url` attribute, its `.results-url` text and its `.results-title` text.
9. Locate the `fccn.pt` home page result anywhere in the list (`#pages-results > ul[data-tstamp="19961013145650"]`) and read its version date (`.results-date`).
10. Read the same home page result's summary (`.results-summary`).

**Expected results:**
- "Verify if the estimated results count message is displayed on page search" — text contains both `Cerca de ` and ` resultados desde 1996 até 1997` (soft/appendError, using `allOf`).
- "Check results are within the selected dates" (soft/appendError, multiple sub-assertions):
  - at least 1 result found ("Mininium of results should be 1")
  - every result's `data-tstamp` is `>= "19961012"` and `< "19970102"` ("Result <tstamp> should be between 12 October 1996 and 1 January 1997")
- "Check first result" (soft/appendError, multiple sub-assertions):
  - first result `ul`'s `data-url` attribute contains `fccn.pt`
  - first result's `.results-url` text contains `fccn.pt`
  - first result's `.results-title` text is not empty
- "Check fccn.pt home page result version" (soft/appendError): the home page result's `.results-date` text contains `13 Outubro 1996`.
- "Check fccn.pt home page result summary" (soft/appendError): the home page result's `.results-summary` text contains `Av. Brasil`.

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, date-range, homepage-search

## Notes
- Distinguishes itself from `PageAdvancedSearchTest`'s date-range coverage by using the homepage date pickers directly rather than the advanced search form.
- The ranking of results differs between environments, so the test no longer requires the `fccn.pt` home page (`19961013145650`) to be the first result: the first result only needs an `fccn.pt` URL and a non-empty title, and the home page result is looked up by its `data-tstamp` anywhere in the list.
- Results are read through class selectors (`.results-url`, `.results-title`, `.results-date`, `.results-summary`) inside each result `ul`, instead of positional XPaths.
- Test data (timestamp `19961013145650`, "Av. Brasil" summary, "13 Outubro 1996") is tightly coupled to real archived content for `fccn.pt` — this is fragile against content changes/re-indexing and should be considered when deciding whether the Playwright suite needs a stable fixture/mock instead.
- Portuguese-locale-specific date format string ("13 Outubro 1996") and results message wording ("Cerca de ... resultados desde ... até ...") are hardcoded — assumes the site defaults to PT locale when no `?l=` parameter is set.
