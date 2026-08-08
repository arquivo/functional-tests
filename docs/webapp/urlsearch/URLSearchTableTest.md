# URLSearchTableTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/urlsearch/URLSearchTableTest.java`
- **Category:** webapp > urlsearch
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the "table" view of archived versions for a URL correctly displays the first/earliest capture with the correctly formatted, localized date label (day + abbreviated month), for both PT and EN locales.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Relies on live archived data for `fccn.pt` at the target environment, specifically the earliest capture being timestamp `19961013145650` (13 October 1996).
- Locale switched via `LocaleUtils.changeLanguageToPT`/`changeLanguageToEN` before running the shared flow.

## Test Data
- Search query: `fccn.pt`
- PT: table toggle label `"Tabela"`, expected first-result cell text `"13 Out"`.
- EN: table toggle label `"Table"`, expected first-result cell text `"13 Oct"`.
- Target cell id: `table-cell-19961013145650`.

## Steps & Expected Results

### `urlSearchTablePTTest()`
1. Switch language to PT (`LocaleUtils.changeLanguageToPT`).
2. Call shared flow with url=`fccn.pt`, tableText=`Tabela`, firstResultText=`13 Out`, locale=PT.

### `urlSearchTableENTest()`
1. Switch language to EN (`LocaleUtils.changeLanguageToEN`).
2. Call shared flow with url=`fccn.pt`, tableText=`Table`, firstResultText=`13 Oct`, locale=EN.

### Shared flow (`urlSearchTableTest`)
1. Clear search input, type `fccn.pt`, click search.
2. If table view is not already active (`#replay-table-button` not `disabled`), click it to switch to table mode.
3. (hard) Wait for `#url-results`; assert the text of `#table-cell-19961013145650` equals the expected `firstResultText` (`13 Out`/`13 Oct`) — message: "Check if first version match".
4. (soft) Re-check the same cell's text equals `firstResultText` again — message: "Verify specific timetamp" (sic).

**Expected results:**
- (hard) First result cell (`table-cell-19961013145650`) text equals localized `"13 Out"` (PT) or `"13 Oct"` (EN).
- (soft) Same check repeated, collected as a soft assertion.

## Tags / Annotations
- `@Retry`: yes (both test methods)
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, urlsearch, table-view, localization

## Notes
- The `tableText` parameter (`"Tabela"`/`"Table"`) is passed into the helper but never actually used/asserted in the method body — dead parameter, likely leftover from an earlier version of the test that checked the table toggle button label.
- Depends on real archived data: the earliest capture of `fccn.pt` must remain `19961013145650` for the test to keep passing — brittle against new/earlier captures being added.
- Note the duplicated logic: steps 3 and 4 check the exact same element/value once as a hard assertion and once as a soft one, i.e. redundant verification.
