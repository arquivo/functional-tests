# URLSearchListNotContainsSomeHttpCodesTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/urlsearch/URLSearchListNotContainsSomeHttpCodesTest.java`
- **Category:** webapp > urlsearch
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the "list" view of archived versions for a given URL correctly filters out captures with certain HTTP status codes (404, 403, 406, 503, and near-duplicate redirects like 302) while still showing the legitimate captures — i.e. the version list should not surface "noise" captures that returned error/redirect codes close in time to good 200 captures.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Relies on live archived data for specific domains/timestamps existing in the target environment (external data dependency, not seeded by the test).
- Uses `LocaleUtils.changeLanguageTo` to force PT or EN locale via URL query parameter before each search.

## Test Data
| Test method | URL searched | Locale | Visible (expected present) timestamp | Invisible (expected absent) timestamp |
|---|---|---|---|---|
| `urlSearchListNotContains406PTTest` | `http://www.caleida.pt/saramago/` | PT | `19980205082901` | `20120131163447` |
| `urlSearchListNotContains404ENTest` | `http://www.caleida.pt/saramago/` | EN | `20000413142115` | `20160210151550` |
| `urlSearchListNotContains403PTTest` | `sapo.pt` | PT | `19971210144509` | `20150424043204` |
| `urlSearchListNotContains503ENTest` | `record.pt` | EN | `19981202152653` | `20171031183600` |
| `urlSearchListNotContains302PTTest` | `fccn.pt` | PT | `20090904173659` | `20161213184117` |
| `urlSearchListNotContains302ENTest` | `fccn.pt` | EN | `20090904173659` | `20161213184117` |

## Steps & Expected Results
All 6 `@Test` methods delegate to the same private `test(url, locale, visibleVersions, invisibleVersions)` helper.

### `urlSearchListNotContains406PTTest()`, `urlSearchListNotContains404ENTest()`, `urlSearchListNotContains403PTTest()`, `urlSearchListNotContains503ENTest()`, `urlSearchListNotContains302PTTest()`, `urlSearchListNotContains302ENTest()`
1. Switch UI language via `LocaleUtils.changeLanguageTo` (navigates to `testURL?l=pt` or `?l=en`).
2. Clear the search input and type the target URL/domain; click search.
3. Verify (hard, via `assertThat`) that the "List"/"Lista" toggle button (`#replay-list-button`) text contains the localized label.
4. If the list view is not already active (button not `disabled`), click `#replay-list-button` to switch to list mode.
5. For each expected-visible timestamp: wait up to 20s for presence of an element with `id=list-results-timestamp-<timestamp>` (hard, wrapped in `run`, asserts not null).
6. For each expected-invisible timestamp: wait up to 20s (soft/appendError) for invisibility of an element with `id=list-results-timestamp-<timestamp>` via `CustomConditions.invisibilityOfElementLocatedById`.

**Expected results:**
- (hard) "List button is available" — list toggle button text contains localized "List"/"Lista".
- (hard, inside `run`) Each visible-version timestamp element exists — message: "The timestamp %s for %s should be presented".
- (soft/appendError) Each invisible-version timestamp element does NOT exist — message: "The timestamp %s for %s should not exist".

## Tags / Annotations
- `@Retry`: yes (on all 6 test methods)
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, urlsearch, list-view, http-status-filtering, localization

## Notes
- Depends on real archived crawl data at the target environment for specific domains and exact timestamps — brittle against data changes/re-crawls.
- Mixes hard failures (visible versions, which stop the test) with soft failures (invisible versions, which are collected and reported together at teardown).
- Covers both PT and EN locales for some HTTP codes (302) but only one locale for others (404 EN only, 403 PT only, 406 PT only, 503 EN only) — asymmetric coverage.
- `CustomConditions.invisibilityOfElementLocatedById` is a custom Selenium `ExpectedCondition` (see `pt.arquivo.utils.CustomConditions`).
