# PageSearchNotSpamTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchNotSpamTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that search results for a common term ("lisboa") do not surface spam/unrelated commercial content — specifically that no result mentions "olx" (a classifieds site used here as a stand-in indicator of spam/irrelevant content).

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.

## Test Data
- Search term: `lisboa`
- Spam indicator term checked: `olx`

## Steps & Expected Results

### `pageSearchNotSpamTest()`
1. Clear the search box, type `lisboa`, and click search. (hard — via `run`)
2. Wait for `#pages-results` to appear.
3. Count how many `.page-search-result` elements have text containing "olx" (case-insensitive).
4. Print the count to stdout for diagnostics.

**Expected results:**
- "None of the results should show something related with olx" — the OLX-matching count equals 0 (hard — plain `assertTrue`, not wrapped in `run`/`appendError`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, spam-filtering, relevance

## Notes
- Very small, single-assertion test — no helper methods, no soft assertions.
- The single assertion is hard (not `appendError`), so this test either passes cleanly or fails outright with no additional diagnostic assertions beyond the printed count.
- The choice of "olx" as a spam proxy is a real-world/content-dependent heuristic tied to the live index rather than a controlled fixture — a candidate for flagging when deciding how to make this deterministic in the Playwright suite.
