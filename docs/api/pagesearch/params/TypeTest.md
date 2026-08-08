# TypeTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/TypeTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `type` query parameter correctly filters results to a specific document/mime type (`pdf`), ensuring every returned result's `mimeType` field is `application/pdf`.

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textsearch?q=fccn&type=pdf`.

## Test Data
- Query: `q=fccn`, `type=pdf`.
- Response JSON expected to contain `response_items` array; each item expected to have a `mimeType` string field equal to `"application/pdf"`.

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&type=pdf` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject` and extract the `response_items` `JSONArray`.
4. For each item, read `mimeType`.

**Expected results:**
- `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard).
- For each i-th result: `assertEquals("Verify 'mimeType' parameter of API " + i + "th reply is 'application/pdf'", "application/pdf", mimetype)` (hard) — every result's `mimeType` must be exactly `application/pdf`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, type

## Notes
- The query parameter value is the short type alias `pdf`, but the API's response field uses the full MIME type string `application/pdf` — confirms the API translates/maps friendly type aliases to MIME types internally; important to preserve this mapping if reimplementing.
- Relies on the target environment having at least one indexed PDF matching query `fccn`.
- All assertions are hard (direct `assertEquals`, no `appendError`).
