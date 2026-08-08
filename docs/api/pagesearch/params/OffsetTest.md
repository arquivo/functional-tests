# OffsetTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/OffsetTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `offset` query parameter is accepted and correctly echoed back in the response's `request_parameters` metadata, confirming the API acknowledges and applies pagination offset.

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textsearch?q=fccn&offset=10`.

## Test Data
- Query: `q=fccn`, `offset=10`.
- Response JSON expected to contain `response_items` array (non-empty) and a `request_parameters` object with an `offset` integer field equal to `10`.

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&offset=10` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject`, extract `response_items` `JSONArray` and the `request_parameters` nested `JSONObject`.

**Expected results:**
- `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard).
- `assertEquals("Verify that the 'offset' parameter was received", 10, reply.getJSONObject("request_parameters").getInt("offset"))` (hard) — confirms the API's response metadata reflects the requested offset value.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, offset

## Notes
- This test does NOT verify that the returned items are actually the 10th-onward results (no comparison against an unpaginated baseline) — it only checks that the response is non-empty and that the API echoes back the requested `offset` value in `request_parameters`. A more thorough reimplementation might additionally compare item sets/order against an offset=0 baseline call.
- Response shape includes a `request_parameters` object alongside `response_items`, useful for confirming the API's parameter parsing/normalization.
