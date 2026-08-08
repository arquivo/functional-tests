# SiteSearchTest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/params/SiteSearchTest.java`
- **Category:** api > pagesearch/params
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Page Search API's `siteSearch` query parameter correctly restricts results to pages originating from the specified site (`www.publico.pt`), by checking each result's `originalURL` matches that domain (including subdomains).

## Preconditions
- Base URL from `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textsearch?q=fccn&siteSearch=www.publico.pt`.

## Test Data
- Query: `q=fccn`, `siteSearch=www.publico.pt`.
- Response JSON expected to contain `response_items` array; each item expected to have an `originalURL` string field.
- Validation regex: `^https?:\/\/([^\.]+\.)?publico\.pt.*$` — matches `http(s)://publico.pt...` or `http(s)://<subdomain>.publico.pt...` (single-label subdomain prefix, e.g. `www.publico.pt`).

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textsearch?q=fccn&siteSearch=www.publico.pt` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and wrap it in a `JSONTokener` (throws `RuntimeException` "Error starting a request to the Page Search API" on `IOException`).
3. Parse the response as a `JSONObject` and extract the `response_items` `JSONArray`.
4. Compile the regex pattern `^https?:\/\/([^\.]+\.)?publico\.pt.*$`.
5. For each item, read `originalURL` and test it against the pattern.

**Expected results:**
- `assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0)` (hard).
- For each i-th result: `assertEquals("Verify 'originalURL' parameter of API " + i + "th reply is from www.publico.pt", true, pattern.matcher(originalURL).find())` (hard) — every result's `originalURL` must match the `publico.pt` domain pattern.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch, siteSearch

## Notes
- The regex only allows one optional subdomain label before `publico.pt` (e.g. `www.publico.pt` or `desporto.publico.pt` would match, but a deeper subdomain like `a.b.publico.pt` would not) — worth confirming this is the intended real-world constraint when reimplementing, since the query parameter itself specifies `www.publico.pt` but the validation is looser (any single-label subdomain or bare domain).
- Uses `.find()` rather than `.matches()`, but the pattern itself is anchored (`^...$`), so behavior is effectively a full-string match.
- Relies on the target environment having indexed content from `publico.pt` matching query `fccn`.
