# DatePickerTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/datepicker/DatePickerTest.java`
- **Category:** webapp > datepicker
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the start and end date pickers on the search page can be set to specific dates and that, once set, their displayed values correctly reflect the chosen dates.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Homepage/search page loaded via base class `setUp()`.
- `DatePicker` utility behaves differently on desktop vs. mobile platforms (detected via WebDriver capabilities/user agent) — desktop uses a text-input modal, mobile uses a scrollable wheel-style picker driven via touch/JS wheel-event simulation.

## Test Data
- Start date: `31/05/2010`
- End date: `01/01/2012`

## Steps & Expected Results

### `datePickerTest()`
1. Set the start date picker to `31/05/2010` via `DatePicker.setStartDatePicker(driver, "31/05/2010")` (opens picker, enters/scrolls to date, confirms selection).
2. Set the end date picker to `01/01/2012` via `DatePicker.setEndDatePicker(driver, "01/01/2012")`.
3. (hard) Assert `DatePicker.getStartDateString(driver)` equals `"31/05/2010"` — "Check that the start date was correctly set".
4. (hard) Assert `DatePicker.getEndDateString(driver)` equals `"01/01/2012"` — "Check that the end date was correctly set".

**Expected results:**
- (hard) Start date picker displays/stores `31/05/2010` after being set.
- (hard) End date picker displays/stores `01/01/2012` after being set.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, datepicker, date-selection, cross-platform-widget

## Notes
- Both assertions use `org.junit.Assert.assertEquals` directly (not wrapped in `run`/`appendError`), so both are hard JUnit failures that stop the test immediately on first failure — no soft-assertion aggregation is used here.
- The underlying `DatePicker` utility (`pt.arquivo.utils.DatePicker`) has substantial platform-specific complexity worth noting for the Playwright rewrite: on desktop it types into a text input (`#modal-datepicker-input`) with `HOME` key then the date string in `dd/MM/yyyy` format (see the desktop retry flow below); on mobile it opens a wheel-style picker (`.ap-cont`) and increments/decrements day/month/year values one step at a time via simulated touch-drag (iOS) or two-finger wheel-event drag (Android), reading the "selected" row's `data-value` attribute to know current position before each step.
- Desktop flow of `DatePicker.setDatePicker` (used by `setStartDatePicker`/`setEndDatePicker`), which guards against keystrokes or the selection being lost when many tests run concurrently:
  1. Open the picker (`#date-container-start`/`#date-container-end`) and wait (up to 40s) for `#modal-datepicker-container` to be visible.
  2. Wait (up to 40s) until `#modal-datepicker-input` holds a `dd/mm/yyyy` value (i.e. the modal's script has filled in the current date) before typing.
  3. Click the input, press `HOME`, type the date, then wait (up to 5s) for the input value to equal the typed date; if it doesn't, retype — up to 3 attempts, after which it fails with "Date picker input is [<actual>] instead of [<expected>]".
  4. Click the confirm button (`#modal-datepicker-confirm-button-span`).
  5. Wait (up to 10s each) for the hidden input (`#start-date`/`#end-date`) to have value `yyyymmdd` for the chosen date, and for `#modal-datepicker-container` to close.
  6. If either wait times out, close the modal via `jQuery.modal.close()` (when available) and retry the whole open/type/confirm sequence — up to 3 attempts, after which the timeout is rethrown.
  The mobile flow is unchanged: open, change the wheels, confirm, with no verification or retry.
- `DatePicker.getDateString` reads a raw `YYYYMMDD`-style value directly from a hidden input via JavaScript and reformats it to `dd/MM/yyyy` for comparison — the Playwright equivalent should read the same underlying input value/format.
