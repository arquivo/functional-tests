# WorkflowStateBetweenSearchPageAndImageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/workflow/WorkflowStateBetweenSearchPageAndImageTest.java`
- **Category:** webapp > workflow
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the search query and date-range filters persist correctly when switching between the "Images" search tab and the "Pages" search tab, so filter state is not lost when a user changes result type.

## Preconditions
- `test.url` system property set to the target environment.
- Browser/device matrix from `test.browsers.json` (or default).
- Homepage/search page loaded before the test via base class `setUp()`.

## Test Data
- Search query: `fccn`
- Start date: `20/06/1997` (expected year `1997`, day/month `20 Jun`)
- End date: `01/01/2014` (expected year `2014`, day/month `1 Jan`)

## Steps & Expected Results

### `stateBetweenSearchPageAndImageTest()`
1. Clear the search input and type `fccn`; click search.
2. Set start date to `20/06/1997`.
3. Set end date to `01/01/2014`.
4. Click the "Images" search button (`#search-form-images`).
5. Wait for `#images-results` to be visible.
6. Read back search input value and date fields (start/end year, start/end day-month) on the image results view.
7. Click the "Pages" search button (`#search-form-pages`).
8. Wait for `#pages-results` to be visible.
9. Read back search input value and date fields again on the page results view.

**Expected results (all soft/appendError):**
- "Check if fccn is in search box on image search" — search input value equals `fccn` (checked on both images view and pages view).
- "Check if 1977 is in the year left datepicker" (copy-paste message reused for all 4 date checks) — `#start-year` = `1997`, `#start-day-month` = `20 Jun`, `#end-year` = `2014`, `#end-day-month` = `1 Jan` — verified on both the images view and the pages view.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, workflow, images-search, pages-search, state-persistence

## Notes
- Same copy-paste assertion-message quirk as the sibling `WorkflowStateBetweenSearchImagesTest`: the message text "Check if 1977 is in the year left datepicker" is reused for start-year, start-day-month, end-year, and end-day-month checks even though each checks a different field/value.
- Navigation clicks are hard failures (`run`); state-verification reads are soft (`appendError`).
