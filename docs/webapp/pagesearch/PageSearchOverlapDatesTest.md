# PageSearchOverlapDatesTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchOverlapDatesTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the date-range picker UI self-corrects / prevents an invalid state where the user sets a start date that is chronologically after the end date — i.e., it checks that after entering an "overlapping"/inverted date range, the resulting effective start date is still not later than the end date.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- Uses `DatePicker` utility to set start/end dates and to read back the currently selected dates (`DatePicker.getStartDate`/`getEndDate`) as `LocalDate` objects.

## Test Data
- Search term: `fccn`
- Start date entered: `20/05/1997`
- End date entered: `22/08/1996` (earlier than the start date — deliberately inverted/overlapping)

## Steps & Expected Results

### `pageSearchOverlapDatesTest()`
1. Clear the search box, type `fccn`, and click search. (hard — via `run`)
2. Set start date picker to `20/05/1997`. (hard — via `run`)
3. Set end date picker to `22/08/1996`. (hard — via `run`)
4. Read back the currently selected start and end dates via `DatePicker.getStartDate`/`getEndDate` and compare them (helper `checkDatePicker()`).

**Expected results:**
- "Check if it is possible to do date overlap: " — `checkDatePicker()` returns true, i.e. the (possibly auto-adjusted) start date is before or equal to the end date (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, date-range, date-validation

## Notes
- Does not click the main search button at all — the test only manipulates the date pickers and reads their resulting values; no actual search/results are exercised.
- Relies on the UI to auto-correct an inverted date range (e.g., snapping the end date forward, or clamping) — the exact correction behavior is implicit in whatever the widget does; the test only asserts the invariant `start <= end` holds afterward, not a specific corrected value.
- `DatePicker.getStartDate`/`getEndDate` parse the raw `yyyymmdd`-formatted hidden input value into a `LocalDate` for comparison.
