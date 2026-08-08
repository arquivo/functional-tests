# PageSearchLimitedDatesFromHomepageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchLimitedDatesFromHomepageTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that a user can restrict a search to a specific date range directly from the homepage date pickers (without going through the advanced search page), and that the results — estimated count message, first result URL/title/timestamp/summary — reflect that date range and search term correctly.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- Uses `DatePicker` utility for setting start/end dates via the homepage widgets.
- Assumes a specific, stable archived page for `fccn.pt` exists at timestamp `19961013145650` with known title/summary content (test data is tied to the real archive content, not a fixture/mock).

## Test Data
- Search term: `fccn`
- Start date: `12/10/1996`
- End date: `01/01/1997`
- Expected estimated-results message substrings: `Cerca de ` and ` resultados desde 1996 até 1997` (Portuguese locale wording)
- Expected first result timestamp (`data-tstamp` attribute): `19961013145650`
- Expected first result URL/text substring: `fccn.pt`
- Expected first result "version" date substring (PT): `13 Outubro 1996`
- Expected first result summary substring: `Av. Brasil`

## Steps & Expected Results

### `pageSearchLimitedDatesFromHomepageTest()`
1. Clear the search box and type `fccn` (note: does NOT click search yet at this point). (hard — via `run`)
2. Set start date picker to `12/10/1996` via `DatePicker.setStartDatePicker`. (hard — via `run`)
3. Set end date picker to `01/01/1997` via `DatePicker.setEndDatePicker`. (hard — via `run`)
4. Click the search button (`#submit-search`). (hard — via `run`)
5. Wait for `#pages-results` to appear. (hard — via `run`)
6. Read the estimated-results text (`#estimated-results`).
7. Locate first result link(s) under `#pages-results/ul[1]/li[1]/a`; verify at least one exists, check its text and the parent `ul`'s `data-tstamp`/`data-url` attributes.
8. Re-check the same first result link's text for the title check.
9. Read the first result's "version" paragraph (`ul[1]/li[3]/p`).
10. Read the first result's summary paragraph (`ul[1]/li[4]/p`).

**Expected results:**
- "Verify if the estimated results count message is displayed on page search" — text contains both `Cerca de ` and ` resultados desde 1996 até 1997` (soft/appendError, using `allOf`).
- "Check first result url" (soft/appendError, multiple sub-assertions):
  - at least 1 result link found ("Mininium of urls should be 1")
  - first result's text contains `fccn.pt`
  - first result `ul`'s `data-tstamp` attribute equals `19961013145650`
  - first result `ul`'s `data-url` attribute contains `fccn.pt`
- "Check first result title" (soft/appendError): at least 1 result found, and its text contains `fccn.pt`.
- "Check first result version" (soft/appendError): version paragraph text contains `13 Outubro 1996`.
- "Check first result summary" (soft/appendError): summary paragraph text contains `Av. Brasil`.

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, date-range, homepage-search

## Notes
- Distinguishes itself from `PageAdvancedSearchTest`'s date-range coverage by using the homepage date pickers directly rather than the advanced search form.
- Test data (timestamp `19961013145650`, "Av. Brasil" summary, "13 Outubro 1996") is tightly coupled to real archived content for `fccn.pt` — this is fragile against content changes/re-indexing and should be considered when deciding whether the Playwright suite needs a stable fixture/mock instead.
- Portuguese-locale-specific date format string ("13 Outubro 1996") and results message wording ("Cerca de ... resultados desde ... até ...") are hardcoded — assumes the site defaults to PT locale when no `?l=` parameter is set.
