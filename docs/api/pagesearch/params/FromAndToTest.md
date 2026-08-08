# FromAndToTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/FromAndToTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `from` and `to` date-range query parameters correctly filter results by timestamp — `from=20000101000000` should only return results with a `tstamp` on/after year 2000, and `to=19991231000000` should only return results with a `tstamp` before year 2000.

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoints under test: `<testURL>/textsearch?q=fccn&from=20000101000000` and `<testURL>/textsearch?q=fccn&to=19991231000000`.

## Test Data
- Query 1: `q=fccn`, `from=20000101000000`.
- Query 2: `q=fccn`, `to=19991231000000`.
- Response JSON expected to contain `response_items` array; each item expected to have a `tstamp` string field.
- Assertion is a crude decade check: first character of `tstamp` must be `"2"` (for `from`, meaning year 2000s) or `"1"` (for `to`, meaning 1900s).

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&from=20000101000000` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject`, extract `response_items` `JSONArray`.
4. For each item, read `tstamp` and check its first character.
5. Build a second URL `<testURL>/textsearch?q=fccn&to=19991231000000`, repeat steps 2-3 (reusing the same local variables).
6. For each item in this second response, read `tstamp` and check its first character.

**Expected results:**
- After query 1: `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard).
- After query 1, for each i-th result: `assertEquals("Verify 'tstamp' parameter of API " + i + "th reply is later than 2000/01/01", timestamp.substring(0,1), "2")` (hard).
- After query 2: `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard).
- After query 2, for each i-th result: `assertEquals("Verify 'tstamp' parameter of API " + i + "th reply is before 2000/01/01", timestamp.substring(0,1), "1")` (hard).

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, fromAndTo

## Notes
- Both `from` and `to` sub-scenarios live in a single `@Test` method (not split into two `@Test`s), so a failure in the first half prevents the second half's HTTP call/assertions from running (all assertions are hard, no `appendError`).
- The date-range check is approximate (only inspects the leading digit of `tstamp`, i.e. century/decade-level granularity: `"2"` for 2000s, `"1"` for 1900s), not a precise boundary check against the exact `from`/`to` timestamps requested.
- Relies on the target environment having archived content both before and after year 2000 for query `fccn`.
