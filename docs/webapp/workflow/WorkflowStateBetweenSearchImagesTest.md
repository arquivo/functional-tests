# WorkflowStateBetweenSearchImagesTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/workflow/WorkflowStateBetweenSearchImagesTest.java`
- **Category:** webapp > workflow
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that search query and date-range filter state is preserved when navigating within the images search results (from page 1 to page 2 and back), so users don't lose their search context while paging through image results.

## Preconditions
- `test.url` system property set to the target environment.
- Browser/device matrix from `test.browsers.json` (or default Windows 10 Chrome if unset).
- Homepage/search page loaded by `WebDriverTestBaseParallel.setUp()` before the test runs.

## Test Data
- Search query: `fccn`
- Start date: `20/06/1997` (expected rendered as year `1997`, day/month `20 Jun`)
- End date: `01/01/2014` (expected rendered as year `2014`, day/month `1 Jan`)

## Steps & Expected Results

### `stateBetweenSearchImagesTest()`
1. Clear the search input (`#submit-search-input`) and type `fccn`; click search (`#submit-search`).
2. Click the "Images" search tab (`#search-form-images`, waited up to 20s).
3. Set the start date picker to `20/06/1997` via `DatePicker.setStartDatePicker`.
4. Set the end date picker to `01/01/2014` via `DatePicker.setEndDatePicker`.
5. Click the search button (`#submit-search`).
6. Click "next page" button inside images results (`#images-results section form button` via xpath).
7. Wait for `#images-results` to be visible.
8. On page 2: read back the search input, start/end year, and start/end day-month fields.
9. Click "previous page" button (first `form` inside `#images-results`, via xpath).
10. Wait for `#images-results` to be visible again.
11. On page 1 (after going back): read back the same fields again.

**Expected results (all soft/appendError):**
- "Check if fccn is in search box on second page" — search input value trimmed equals `fccn` (checked after next-page and again after previous-page).
- "Check if 1977 is in the year left datepicker" (message text is a copy/paste artifact, actually verifies dates) — `#start-year` equals `1997`.
- Same message — `#start-day-month` equals `20 Jun`.
- Same message — `#end-year` equals `2014`.
- Same message — `#end-day-month` equals `1 Jan`.
- All five assertions repeated identically after returning to the previous page.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, workflow, images-search, pagination, state-persistence

## Notes
- The assertion messages say "Check if 1977 is in the year left datepicker" for all four date-field checks (start year, start day/month, end year, end day/month) — this is a copy-paste artifact in the source; the actual expected values differ per assertion (1997/20 Jun/2014/1 Jan) despite the message text.
- Navigation (`run`) steps are hard failures; the field-value checks after paging are soft (`appendError`), so all of them will be evaluated and reported together even if some fail.
- Relies on `DatePicker` utility which behaves differently for desktop vs. mobile platforms (detected via WebDriver capabilities/user-agent).
