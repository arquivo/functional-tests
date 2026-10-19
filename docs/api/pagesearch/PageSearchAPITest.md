# PageSearchAPITest

- **Source:** `src/test/java/pt/arquivo/tests/api/pagesearch/PageSearchAPITest.java`
- **Category:** api > pagesearch
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the "text extracted" endpoint (`/textextracted`) returns the exact plain-text content extracted from a specific archived memento of `www.fccn.pt`, confirming the text-extraction pipeline works correctly for a known page/timestamp.

## Preconditions
- Base URL comes from the `test.url` system property, read in the constructor.
- No auth, no config/resource files.
- Endpoint under test: `<testURL>/textextracted?m=http://www.fccn.pt//19961013145650`.

## Test Data
- `m` parameter: `http://www.fccn.pt//19961013145650` (note the double slash before the timestamp).
- Expected exact extracted text is the page title, `Fundação para a Computação Científica Nacional " A` (as extracted, including the stray `" A`), followed by `, ` and then the page's body text: a long hardcoded Portuguese string describing "Fundação para a Computação Científica Nacional (FCCN)", including visitor counter text ("5685 visitantes desde 95-11-22"), contact details, and a trailing "Internet URL- http://www.fccn.pt:80/".

## Steps & Expected Results

### `pageSearchAPITest()`
1. Build the URL `<testURL>/textextracted?m=http://www.fccn.pt//19961013145650` (throws `RuntimeException` "Error generating URL to a Page Search API" on `MalformedURLException`).
2. Open a stream and read all bytes (throws `RuntimeException` "Error downloading the text" on `IOException`).
3. Decode the bytes as UTF-8 into a `String` (`timemap` variable, despite the misleading name — it holds extracted text, not a timemap).
4. Compare the full string against the hardcoded expected text (`checkTest`), which starts with the page title, then `, `, then the body text.

**Expected results:**
- `assertEquals("Verify test search API", checkTest, timemap)` (hard) — the entire extracted text body (title + `, ` + body text) must match the hardcoded expected string exactly.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Category tags: api, pagesearch

## Notes
- Test is brittle: it asserts byte-for-byte equality against a large hardcoded text blob, so any change in extraction/encoding/whitespace handling on the target environment's fixture data would break it.
- The `m` query parameter double-slash (`www.fccn.pt//19961013145650`) appears intentional/tolerated by the API but is unusual — worth confirming behavior when reimplementing.
- The response is decoded explicitly as UTF-8, so the accented Portuguese characters compare correctly regardless of the JVM's default charset.
- `@Retry` suggests this test is known to be flaky (likely due to whitespace/formatting sensitivity or transient network/API issues) and gets automatically retried by the test runner infrastructure.
