# URLSearchListTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/urlsearch/URLSearchListTest.java`
- **Category:** webapp > urlsearch
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the "list" view of archived versions for a URL correctly groups captures by year and month, shows correct version counts and localized labels, hides years/months with no captures, and that drilling down (year to month to specific timestamp) leads to the correct capture with a correctly formatted date/time label and a correct replay link.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Relies on live archived data for `fccn.pt` existing at the target environment, specifically a single capture in 1996 (October) with timestamp `19961013145650`, and no captures in 1995 or in September/November 1996.
- Locale switched via `LocaleUtils.changeLanguageTo` (PT and EN variants run as separate tests).

## Test Data
- Search query: `fccn.pt`
- Expected year present: `1996` (exactly `1` version); year `1995` expected absent.
- Expected month present: October 1996 (`list-results-month-1996-10`), exactly `1 versão`/`1 version`; September (`1996-09`) and November (`1996-11`) expected absent.
- Expected specific capture timestamp: `19961013145650`, with expected display text formatted via `MessageFormat` pattern `"{0,date,d} {0,date,MMMM} {0,time,HH}h{0,time,mm}, {0,time,yyyy}"` per locale, and expected href containing `/wayback/19961013145650/http://www.fccn.pt/` (with `:80` stripped).

## Steps & Expected Results
Both test methods delegate to the private `urlSearchListTest(url, locale)` helper.

### `urlSearchListPTTest()`
Calls `urlSearchListTest("fccn.pt", LocaleUtils.PORTUGUESE)`.

### `urlSearchListENTest()`
Calls `urlSearchListTest("fccn.pt", LocaleUtils.ENGLISH)`.

### Shared flow (`urlSearchListTest`)
1. Switch UI language to given locale.
2. Clear search input, type `fccn.pt`, click search.
3. (hard) Wait for year header `#list-results-year-1996`, assert not null.
4. (soft) Assert `#list-results-year-1995` is invisible — "Year 1995 shouldn't be visible".
5. (hard) Find the `<a>` inside the 1996 year header, assert not null and its text contains `1996`.
6. (hard) Assert the version-count span under the 1996 year link contains `1 versão`/`1 version` (localized).
7. Click the 1996 year link to expand it.
8. (hard) Read text of `#list-results-month-1996-10`; assert it contains the localized "1 versão"/"1 version" and the localized month name ("Outubro"/"October").
9. (soft) Assert September (`list-results-month-1996-09`) is invisible.
10. (soft) Assert November (`list-results-month-1996-11`) is invisible.
11. (hard) Wait for October month header, assert not null, click it to expand.
12. (soft, wrapped as single appendError block) Wait for the specific timestamp element `#list-results-timestamp-19961013145650`; build expected formatted date/time string via `MessageFormat` + `DateUtils.asDateFromTimestamp`; assert the element's text equals the expected formatted string; assert the timestamp link's `href` (with `:80` removed) contains `/wayback/19961013145650/http://www.fccn.pt/`.

**Expected results:**
- (hard) Year header 1996 exists; year 1995 absent (soft).
- (hard) Year link text contains "1996"; version count text contains "1 versão"/"1 version".
- (hard) Month header text contains correct localized month name and version count.
- (soft) September/November month headers absent.
- (hard) October month header exists and is clickable.
- (soft) Formatted display date/time matches locale-specific `MessageFormat` output; replay link href matches expected wayback URL pattern.

## Tags / Annotations
- `@Retry`: yes (both test methods)
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, urlsearch, list-view, date-grouping, localization

## Notes
- Contains a `public static void main(String[] args)` scratch/debug method unrelated to JUnit execution — prints the `MessageFormat` pattern and a sample formatted date for `19961013145650` using the default locale; not part of the test suite run, just leftover debug tooling.
- Depends on real archived crawl data for `fccn.pt` — specifically that there is exactly one capture in 1996 (in October, at `19961013145650`) and none in 1995 or in September/November 1996. Brittle against re-crawls or new captures being added.
- Uses `DateUtils.asDateFromTimestamp` and `java.text.MessageFormat` with locale-specific date/time patterns to build the expected localized display string — Playwright reimplementation will need equivalent date-formatting logic per locale (pt/en).
- The href assertion strips `:80` from the URL before matching, suggesting default port sometimes appears in links.
