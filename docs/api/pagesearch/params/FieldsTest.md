# FieldsTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/FieldsTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `fields` query parameter correctly limits the fields returned per result item — only the requested fields (`title`, `collection`) should be present, and other fields (`tstamp`, `digest`) should be absent.

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textsearch?q=fccn&fields=title,collection`.

## Test Data
- Query: `q=fccn`, `fields=title,collection`.
- Response JSON expected to contain `response_items` array; each item expected to have `title` and `collection` keys present, and `tstamp` and `digest` keys absent.

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&fields=title,collection` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject` and extract the `response_items` `JSONArray`.
4. Iterate over each item in `response_items`, checking presence/absence of specific keys via `.has(...)`.

**Expected results:**
- `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard).
- For each i-th result: `assertEquals("Verify 'title' is present on the " + i + "th reply", true, apiResults.getJSONObject(i).has("title"))` (hard).
- For each i-th result: `assertEquals("Verify 'collection' is present on the " + i + "th reply", true, apiResults.getJSONObject(i).has("collection"))` (hard).
- For each i-th result: `assertEquals("Verify 'tstamp' is NOT present on the " + i + "th reply", false, apiResults.getJSONObject(i).has("tstamp"))` (hard).
- For each i-th result: `assertEquals("Verify 'digest' NOT is present on the " + i + "th reply", false, apiResults.getJSONObject(i).has("digest"))` (hard).

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, fields

## Notes
- All assertions are hard (direct `assertEquals`, no `appendError`).
- Confirms field-projection behavior of the API — a useful contract to preserve if reimplementing (whitelist semantics: only listed fields returned, everything else omitted).
- Relies on the target environment returning at least one result for `q=fccn`.
