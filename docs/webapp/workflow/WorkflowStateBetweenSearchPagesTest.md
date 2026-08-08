# WorkflowStateBetweenSearchPagesTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/workflow/WorkflowStateBetweenSearchPagesTest.java`
- **Category:** webapp > workflow
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the search query and date-range filters remain intact when paging forward and backward through "Pages" (web page) search results, ensuring state persistence across pagination.

## Preconditions
- `test.url` system property set to the target environment.
- Browser/device matrix from `test.browsers.json` (or default).
- Homepage/search page loaded before the test via base class `setUp()`.

## Test Data
- Search query: `fccn`
- Start date: `20/06/1997` (expected year `1997`, day/month `20 Jun`)
- End date: `01/01/2014` (expected year `2014`, day/month `1 Jan`)

## Steps & Expected Results

### `stateBetweenSearchPagesTest()`
1. Clear search input and type `fccn` (no click here yet).
2. Set start date to `20/06/1997`.
3. Set end date to `01/01/2014`.
4. Click the search button (`#submit-search`).
5. Wait for `#pages-results` to be visible.
6. Click "next page" (`#pages-results section form button` via xpath).
7. Wait for `#pages-results` to be visible again.
8. Read back search input and date fields on page 2.
9. Click "previous page" (first `form` inside `#pages-results`, via xpath).
10. Wait for `#pages-results` to be visible.
11. Read back search input and date fields on page 1 again.
12. Additionally check (soft) that an element with `@value="fccn"` is present/visible via xpath.

**Expected results (all soft/appendError except navigation which is hard):**
- "Check if fccn is in search box on image search" (message reused, actually pages-search context) — search input equals `fccn`, checked after next-page and after previous-page.
- "Check if 1977 is in the year left datepicker" (copy-paste message) — `#start-year`=`1997`, `#start-day-month`=`20 Jun`, `#end-year`=`2014`, `#end-day-month`=`1 Jan`, checked after next-page and after previous-page.
- "Check if fccn is in search box on second page" — an element `//*[@value="fccn"]` is visible (final extra check).

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, workflow, pages-search, pagination, state-persistence

## Notes
- Same recurring copy-paste assertion-message quirk (message says "image search"/"1977" even though the test is about pages search and different years/values).
- Unlike the sibling tests, this one does not click the initial search button right after typing the query — clicking happens later, combined with the date-picker interactions.
- The final soft check for `//*[@value="fccn"]` is somewhat redundant with the earlier search-input value check but targets the element generically by its `value` attribute rather than by ID.
