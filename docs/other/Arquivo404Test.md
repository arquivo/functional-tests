# Arquivo404Test

- **Source:** `src/test/java/pt/arquivo/tests/arquivo404/Arquivo404Test.java`
- **Category:** other > arquivo404
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Arquivo404 is a client-side JavaScript widget (exposed globally as `ARQUIVO_NOT_FOUND_404`) that site owners can embed so that, when a visitor lands on a real 404 "page not found" page, the widget checks whether Arquivo.pt has an archived version of the missing URL and injects a message into the page (e.g. "an older version of this page is available in the archive"). This test suite verifies the widget's public API (`url`, `message`, `messageElementId`, `setDateFormatter`, `setMinimumDate`, `setMaximumDate`, `setMostRelevantMemento`) behaves correctly on the test site's own 404 page.

## Preconditions
- Target env: `testURL` (from `test.url` system property) + path `/thispagedoesnotexist/`, a URL guaranteed to trigger the site's real 404 page.
- No YAML/config resource files are loaded by this test; all URLs and expected values are hardcoded in the Java source (e.g. `https://www.arquivo.pt` is used as the memento URL argument to the widget API).
- Assumes the Arquivo404 script is already installed on the test site's 404 page and exposes a `div#arquivo404-message` element plus the global `ARQUIVO_NOT_FOUND_404` object.

## Test Data
None — no external config/resource file is used. Each test method drives the widget directly via `driver.executeScript(...)` calls with literal strings/dates written inline in the test method.

## Steps & Expected Results

### `checkThatPageHasArquivo404()`
1. Navigate to `testURL/thispagedoesnotexist/`.
2. Wait for the `#arquivo404-message` element to be present.
3. Check the `ARQUIVO_NOT_FOUND_404` global exists.
4. Wait for `#arquivo404-message` to become invisible again (since no `.call()` was invoked, the widget should not display a message for this un-configured/not-archived request).

**Expected results:**
- (soft/appendError) "Wait for 404 page to load" — message div becomes present.
- (soft/appendError) "Verify that Arquivo404 is installed" — `typeof ARQUIVO_NOT_FOUND_404 !== 'undefined'` is true.
- (soft/appendError) "Verify that Arquivo404 doesn't change anything when the requested page isn't archived" — message div becomes invisible.

### `testUrlMethod()`
1. Navigate to 404 page, wait for message div presence.
2. Execute `ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').call();`.
3. Wait for the message div to become visible.

**Expected results:**
- (soft/appendError) "Verify that url method works" — message div becomes visible after calling `.url(...).call()`.

### `testMessageMethod()`
1. Navigate to 404 page, wait for message div presence.
2. Execute `ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').message('Test message').call();`.
3. Wait for the message div's text to equal `"Test message"`.

**Expected results:**
- (soft/appendError) "Verify that message method works" — div text becomes exactly `"Test message"`.

### `testMessageElementIdMethod()`
1. Navigate to 404 page, wait for message div presence.
2. Via JS, create a new `div#test-div` and append it as a sibling of the message div's parent.
3. Execute `ARQUIVO_NOT_FOUND_404.url('https://www.arquivo.pt').messageElementId('test-div').call();`.
4. Wait for `#test-div` to become visible.

**Expected results:**
- (soft/appendError) "Verify that messageElementId method works" — the custom target element becomes visible (i.e. the widget writes into the specified element instead of the default one).

### `testSetDateFormatter()`
1. Navigate to 404 page, wait for message div presence.
2. Execute widget chain with `.url(...).setDateFormatter(date => '' + date.getFullYear()).message('{date}').call();`.
3. Wait for message div to become visible.
4. Assert the div's text, parsed as an integer, is greater than 1990.

**Expected results:**
- (soft/appendError) "Verify that setDateFormatter method works" — displayed year is a valid integer > 1990 (hard `assertTrue` inside the appendError lambda).

### `testSetMinimumDate()`
1. Navigate to 404 page, wait for message div presence.
2. Execute widget chain with `.setDateFormatter(year formatter).setMinimumDate(new Date('2010-01-01')).message('{date}').call();`.
3. Wait for message div text to equal `"2010"`.

**Expected results:**
- (soft/appendError) "Verify that setMinimumDate method works" — the most relevant memento's displayed year is clamped to (or found as) 2010.

### `testSetMostRelevantMemento()`
1. Navigate to 404 page, wait for message div presence.
2. Execute widget chain with `.setDateFormatter(year formatter).setMostRelevantMemento('most-recent').message('{date}').call();`.
3. Wait for message div to become visible.
4. Assert displayed year (as integer) is greater than 2010.

**Expected results:**
- (soft/appendError) "Verify that setMostRelevantMemento method works" — with the "most-recent" memento strategy, the displayed year is > 2010.

### `testSetMaximumDate()`
1. Navigate to 404 page, wait for message div presence.
2. Execute widget chain with `.setDateFormatter(year formatter).setMostRelevantMemento('most-recent').setMaximumDate(new Date('2010-12-31')).message('{date}').call();`.
3. Wait for message div text to equal `"2010"`.

**Expected results:**
- (soft/appendError) "Verify that setMaximumDate method works" — even with "most-recent" strategy, capping at maximum date 2010-12-31 forces the displayed memento year back to 2010.

## Tags / Annotations
- `@Retry`: yes (all 8 test methods)
- `@Ignore`: no
- Runs across configured browser/device matrix (Windows/MacOS/Android/iOS combinations from `test.browsers.json`)
- Category tags: other, arquivo404

## Notes
- All assertions use `appendError` (soft assertions collected and thrown together at tear-down), except the inner `assertTrue` calls in `testSetDateFormatter`/`testSetMostRelevantMemento`, which are hard JUnit assertions executed inside the soft-assertion lambda (a failure there still surfaces via the lambda's appendError wrapping, but as an assertion error rather than a custom message).
- This test depends on the target site under test (`test.url`) already having the Arquivo404 widget script installed and configured to serve its own real 404 page at `/thispagedoesnotexist/` — it is not testing Arquivo.pt itself but a reusable client-side integration script.
- No dependency on aging archived snapshots for most tests since URLs/dates are widget-internal; `testSetMostRelevantMemento`/`testSetMaximumDate` implicitly depend on there being at least one archived memento of `https://www.arquivo.pt` after 2010 and one on/before 2010-12-31.
