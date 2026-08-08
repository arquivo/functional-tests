# ImageAdvancedSearchTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/imagesearch/ImageAdvancedSearchTest.java`
- **Category:** webapp > imagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that a user can run a basic image search, switch to the Advanced Image Search form, fill in a date range, image size, format, and site filters, submit, and that both the result list and the (re-populated) advanced-search form fields correctly reflect all of the applied filters.

## Preconditions
- Target env comes from the `test.url` system property (e.g. https://arquivo.pt or https://preprod.arquivo.pt), loaded automatically by the base class before the test runs.
- No external config/resource files are loaded by this test.
- Relies on `pt.arquivo.utils.DatePicker` helper for setting the start/end date pickers (desktop vs. mobile-aware).
- Contains iOS-specific workarounds: `iosCompatibleWaitUntilVisibleAndClick` / `iosCompatibleWaitUntilVisibleAndSelect` use `JavascriptExecutor` to click/select on iOS because the native iOS driver click/select is unreliable ("IOS driver is dumb").

## Test Data
- Search term: `fccn`
- Start date: `31/05/2010`; end date: `01/01/2012`
- Image size filter: small (`#image-size` value `sm`)
- Format filter: PNG (`input[type=checkbox][format=png]`), after unchecking "All formats" (`input[type=checkbox][format=all]`)
- Site filter: `fccn.pt` (`#website`)
- Expected resulting search-box value: `fccn site:fccn.pt size:sm type:png`
- Expected first result domain text contains `fccn.pt`
- Expected first result date: `20 Janeiro 2011`
- Expected first result image `src`: `<testURL>/wayback/20110120225358im_/http://fccn.pt/images/announce/modulo_moodle_04109.jpg`
- Expected re-populated date fields: start day/month `31 Mai`, start year `2010`, end day/month `1 Jan`, end year `2012`

## Steps & Expected Results

### `imageAdvancedSearchPageTest()`
1. Clear the search box (`#submit-search-input`), type `fccn`, click search (`#submit-search`). (hard — via `run`)
2. Switch to image search results (`#search-form-images`). (hard — via `run`)
3. Click the advanced search link/button (`//*[@id="search-form-advanced"]/button`) to navigate to the advanced image search page. (hard — via `run`)
4. Read the advanced-search word field value (`//*[@id="advanced-search-form-images"]/fieldset/input[1]`).
5. Set the start date picker to `31/05/2010` via `DatePicker.setStartDatePicker`. (hard — via `run`)
6. Set the end date picker to `01/01/2012` via `DatePicker.setEndDatePicker`. (hard — via `run`)
7. Select image size "small" (`#image-size` = `sm`), using the iOS-compatible select helper.
8. Uncheck "All formats" (`input[type=checkbox][format=all]`), using the iOS-compatible click helper.
9. Check the "PNG" format checkbox (`input[type=checkbox][format=png]`), using the iOS-compatible click helper.
10. Type `fccn.pt` into the site field (`#website`).
11. Click the advanced-search submit button (`//*[@id="advanced-search-form-images"]/fieldset/section[2]/button`).
12. Print the current URL (debug only, not an assertion).
13. Read the first result's text, date, and image `src` from the result list.
14. Read the re-populated search box value and the re-populated start/end date-picker fields.

**Expected results:**
- "Check if search words maintain fccn term" — advanced-search word field value equals `fccn` (soft/appendError).
- "Select size to small images" click/select succeeds (soft/appendError, no explicit assertion).
- "Unselect 'All formats'" click succeeds (soft/appendError, no explicit assertion).
- "Set format type to 'PNG'" click succeeds (soft/appendError, no explicit assertion).
- "Set site" typing succeeds (soft/appendError, no explicit assertion).
- "Click on search on arquivo.pt button" click succeeds (soft/appendError, no explicit assertion).
- "Check image original origin/domain" — first result's text contains `fccn.pt` (soft/appendError).
- "Check image date" — first result date text equals `20 Janeiro 2011` (soft/appendError).
- "Check image src" — first result image `src` equals `<testURL>/wayback/20110120225358im_/http://fccn.pt/images/announce/modulo_moodle_04109.jpg` (soft/appendError).
- "After advanced search check search term contains" — search box value equals `fccn site:fccn.pt size:sm type:png` (soft/appendError).
- "After advanced search check day start date contains" — `#start-day-month` value equals `31 Mai` (soft/appendError).
- "After advanced search check year start date contains" — `#start-year` value equals `2010` (soft/appendError).
- "After advanced search check month end date contains" — `#end-day-month` value equals `1 Jan` (soft/appendError).
- "After advanced search check year end date contains" — `#end-year` value equals `2012` (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, imagesearch, advanced-search, date-range, format-filter

## Notes
- Most of the test uses `appendError` (soft assertions), so a single field mismatch does not stop later checks from being evaluated; all accumulated errors are thrown together in `tearDown()`.
- Contains platform-detection logic (`Capabilities.getPlatformName()`) purely to route iOS through JS-based click/select workarounds — a Playwright rewrite likely would not need this workaround, but the underlying business assertions should be preserved.
- Uses positional/brittle XPath locators (e.g. `fieldset/section[2]/button`, `li[1]/ul/li[5]/p`) — worth flagging for the Playwright rewrite as candidates for more robust selectors.
- The expected image URL/date strings are tied to fixed, presumably static, historical crawl content (`fccn.pt`, 2011 capture) — the environment must contain this fixed test fixture data.
