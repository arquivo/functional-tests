# MaxItemsTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/MaxItemsTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `maxItems` query parameter correctly caps the number of results returned, using `dedupValue=1` to disable/limit Solr deduplication so the count isn't inflated by dedup grouping.

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textsearch?q=fccn&maxItems=5&dedupValue=1`.

## Test Data
- Query: `q=fccn`, `maxItems=5`, `dedupValue=1`.
- Response JSON expected to contain `response_items` array with exactly 5 elements.

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&maxItems=5&dedupValue=1` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject` and extract the `response_items` `JSONArray`.

**Expected results:**
- `assertEquals("Verify that the API reply has exactly 5 results", 5, apiResults.length())` (hard) — result count must equal exactly `maxItems`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, maxItems

## Notes
- Code comment explains the reasoning for `dedupValue=1`: "We set dedupValue to 1 becaue deduplication with solr means that repeated results may exceed the maxItems" — i.e. without disabling dedup, Solr grouping could cause more (or fewer distinct) items than `maxItems` to be returned, so this parameter combination isolates the `maxItems` behavior specifically.
- Relies on the target environment having at least 5 matching results for `q=fccn` (otherwise the exact-equality assertion would fail if fewer than 5 exist).
- Only one assertion in this test — no per-item field checks.
