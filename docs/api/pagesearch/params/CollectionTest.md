# CollectionTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/CollectionTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `collection` query parameter correctly restricts results to items belonging to the specified collection (`AWP2`), ensuring every returned result item's `collection` field matches the requested value.

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textsearch?q=fccn&collection=AWP2`.

## Test Data
- Query: `q=fccn`, `collection=AWP2`.
- Response JSON is expected to contain a `response_items` array; each item is expected to have a `collection` field equal to `"AWP2"`.

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&collection=AWP2` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject` and extract the `response_items` `JSONArray`.
4. Iterate over each item in `response_items`, reading its `collection` string field.

**Expected results:**
- `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard) — response must contain at least one result.
- For each i-th result: `assertEquals("Verify 'collection' parameter of API " + i + "th reply is AWP2", "AWP2", collection)` (hard) — every item's `collection` field must equal `"AWP2"`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, collection

## Notes
- All assertions are hard (`assertEquals` directly, not via `appendError`), so the first failing item stops the loop/test.
- Relies on the target environment having at least one indexed item in collection `AWP2` matching query `fccn`.
- JSON parsing uses `org.json` (`JSONObject`, `JSONArray`, `JSONTokener`) rather than a typed model — response shape is `{ "response_items": [ { "collection": "...", ... }, ... ] }`.
