# PageSearchNotArchivedFileTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchNotArchivedFileTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that clicking through search results (1st, 2nd, and 4th positions) into the Wayback/replay view successfully loads an archived page (rather than landing on a "page is not archived" error), confirming search results reliably link to actually-archived captures.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.
- Relies on a private helper `isPageArchived()` that inspects the DOM for `head, #pageIsNotArchived` — counts as "archived" only if exactly one such element is found — and `waitForWaybackThenRun(Runnable)` which waits (up to 20s) for either `#pageIsNotArchived` or `#replay_iframe` to appear before running a check.

## Test Data
- Search term: `fccn` (note: the `run` step's description text says "Search with Lisboa" but the literal query sent is `fccn` — leftover/copy-paste label mismatch)
- Result positions checked: 1st, 2nd, and 4th (`#pages-results/ul[1]`, `ul[2]`, `ul[4]`)

## Steps & Expected Results

### `pageSearchNotArchivedFileTest()`
1. Clear the search box, type `fccn`, and click search (labeled "Search with Lisboa" in code, but actual term sent is `fccn`). (hard — via `run`)
2. Wait for `#pages-results` to appear.
3. Click the first result (`#pages-results/ul[1]`). (hard — via `run`)
4. Wait for wayback page to load (either `#pageIsNotArchived` or `#replay_iframe`), then assert the page is archived. (soft/appendError, via `waitForWaybackThenRun`)
5. Navigate back in browser history.
6. Wait for `#pages-results/ul[1]` to reappear.
7. Click the second result (`#pages-results/ul[2]`). (hard — via `run`)
8. Wait for wayback page to load, then assert archived. (soft/appendError)
9. Navigate back.
10. Wait for `#pages-results/ul[1]` to reappear.
11. Click the fourth result (`#pages-results/ul[4]`). (hard — via `run`)
12. Wait for wayback page to load, then assert archived. (soft/appendError)

**Expected results:**
- "Verify that first result is archived" — `isPageArchived()` returns true (soft/appendError, wrapped inside `waitForWaybackThenRun`).
- "Verify that second result is archived" — same, for the second result (soft/appendError).
- "Verify that fourth result is archived" — same, for the fourth result (soft/appendError).
- All three checks share the "Wait for wayback page to load" description as their appendError label (the actual assertion message like "Verify that first result is archived" is the inner `assertTrue` message).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, replay-integration, archived-file-check

## Notes
- `isPageArchived()` logic: counts elements matching CSS selector `head, #pageIsNotArchived` and considers the page archived only if that count is exactly 1 (i.e., only `<head>` matched, meaning `#pageIsNotArchived` was NOT present) — a slightly indirect way of checking absence of the "not archived" error element.
- Skips checking the 3rd result deliberately (only 1st, 2nd, 4th are checked) — no explanation given in code/comments for why 3rd is skipped.
- Uses `driver.navigate().back()` between each result click to return to the search results page — this is a real cross-page navigation flow relevant for the Playwright rewrite (back/forward navigation semantics).
- Label/description mismatch: step 1's description says "Search with Lisboa" but sends `fccn` — likely copy-pasted from `PageSearchNotSpamTest`.
