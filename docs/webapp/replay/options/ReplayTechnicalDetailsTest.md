# ReplayTechnicalDetailsTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/options/ReplayTechnicalDetailsTest.java`
- **Category:** webapp > replay > options
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from a replayed (wayback) archived page, the replay right menu's "technical details" (more info) option opens a modal listing detailed metadata about the archived capture (original URL, archive link, timestamp, content length, digest, mime type, links to screenshot/no-frame/extracted-text/metadata/original-file, filename, collection, offset, status code), and that every field shows the expected value(s) for this specific capture, and that the modal can be closed.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance, with metadata matching the hardcoded expectations below (this capture is backed by a "Roteiro" collection item).
- `getBaseServiceUrl()` derives the HTTPS form of `testURL` (replaces `http://` with `https://`); `getBaseServiceHost()` parses its host. Both are defined but not directly used within the test method body shown (available as helpers).
- `convertUrToDefaultSolrFormat(String)` converts a URL to the "default Solr format" (forces `https://` and strips `www.`), used because Solr-backed URLs are generated from SURTs, so HTTPS is always assumed and `www` is removed — several assertions accept either the raw or Solr-normalized form via `anyOf`.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Replay right menu button: `#replayMenuButton`
- "Technical details" link: `#a_moreinfo > h4:nth-child(1)`
- Modal container: `#uglipop_popbox` (visibility wait), `#uglipop_content_fixed`-style popup, closed via `#removeModal`
- Field locators: XPath `//*[@id="uglipop_popbox"]/div/p[contains(strong,"<fieldName>")]` (and `/a` variants for link fields)
- Expected field values:
  - `originalURL` — contains `fccn.pt`
  - `linkToArchive` — equals `{testURL}{WAYBACK_EXAMPLE}` or its Solr-normalized form
  - `tstamp` — exactly `tstamp: 19961013145650`
  - `contentLength` — `contentLength: 3760` or `contentLength: 1373` (Solr calculates differently)
  - `digest` — `digest: b5f96e1014f99bbd9ef0277cde883f37` or `digest: OWMAVER7CCNJWL2E5ZURDDKGCHWS7JJO` (Solr calculates differently)
  - `mimeType` — exactly `mimeType: text/html`
  - `linkToScreenshot` — `{testURL}/screenshot?url={encoded testURL}%2FnoFrame%2Freplay%2F19961013145650%2Fhttp%3A%2F%2Fwww.fccn.pt%2F` or the `https%3A%2F%2Ffccn.pt%2F` variant
  - `linkToNoFrame` — `{testURL}/noFrame/replay/19961013145650/http://www.fccn.pt/` or Solr-normalized form
  - `linkToExtractedText` — `{testURL}/textextracted?m=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650` or `https%3A%2F%2Ffccn.pt%2F%2F19961013145650` variant
  - `linkToMetadata` — `{testURL}/textsearch?metadata=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650` or https/no-www variant
  - `linkToOriginalFile` — `{testURL}/noFrame/replay/19961013145650id_/http://www.fccn.pt/` or `https://fccn.pt/` variant
  - `fileName` — contains `fileName: AWP-Roteiro-20090510220155-00000`
  - `collection` — exactly `Roteiro`
  - `offset` — exactly `offset: 45198`
  - `statusCode` — exactly `statusCode: 200`
  - (Commented out / disabled, not asserted): `encoding: windows-1252`, `date: 0845218610`, and a `snippet` check — all disabled with code comments ("Removed encoding and date support with Solr", "Disabling snippet check because it's dependent on the query on solr").

## Steps & Expected Results

### `replayTecnicalDetailsTest()`
1. Navigate to the wayback example URL.
2. Open the replay right menu (click `#replayMenuButton`).
3. Click the "technical details" / "more info" anchor (`#a_moreinfo > h4:nth-child(1)`).
4. Wait for the details modal (`#uglipop_popbox`) to become visible.
5. Read and check each metadata field listed in Test Data, in order: originalURL, linkToArchive, tstamp, contentLength, digest, mimeType, linkToScreenshot, linkToNoFrame, linkToExtractedText, linkToMetadata, linkToOriginalFile, fileName, collection, offset, statusCode.
6. Click the close/remove-modal button (`#removeModal`).
7. Wait for the modal (`#uglipop_popbox`) to become invisible.

**Expected results (all soft/appendError unless noted):**
- "Check originalURL" — text contains `fccn.pt`.
- "Check linkToArchive" — text equals `{testURL}{WAYBACK_EXAMPLE}` or its Solr-normalized equivalent.
- "Check tstamp" — text equals `tstamp: 19961013145650`.
- "Check contentLength" — text equals `contentLength: 3760` or `contentLength: 1373`.
- "Check digest" — text equals one of two digest values (raw vs Solr-computed).
- "Check mimeType" — text equals `mimeType: text/html`.
- "Check linkToScreenshot" — text equals one of two expected screenshot URLs (http vs https/no-www variant); wraps `URLEncoder.encode` which can throw `UnsupportedEncodingException`, rethrown as `RuntimeException`.
- "Check linkToNoFrame:" — text equals one of two expected no-frame URLs.
- "Check linkToExtractedText" — text equals one of two expected extracted-text URLs.
- "Check linkToMetadata" — text equals one of two expected metadata URLs.
- "Check linkToOriginalFile" — text equals one of two expected original-file URLs.
- "Check fileName" — text contains `fileName: AWP-Roteiro-20090510220155-00000`.
- "Check collection" — text equals `Roteiro`.
- "Check offset" — text equals `offset: 45198`.
- "Check statusCode" — text equals `statusCode: 200`.
- "Close technical detail modal" — click succeeds (soft/appendError).
- "Check that tecnical details modal is closed when clicking on close button" — modal (`#uglipop_popbox`) becomes invisible within 20s (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, options, technical-details, metadata

## Notes
- By far the most detailed/exhaustive test in this batch (214 lines, ~16 distinct soft assertions), reflecting an ongoing migration from a legacy (W)ARC-index-based metadata backend to Solr: many fields accept two possible values via `CoreMatchers.anyOf` to tolerate differences between the two backends (content length, digest, and several URLs normalized to https/no-www "SURT-style" form via `convertUrToDefaultSolrFormat`).
- `encoding` and `date` field checks, and a `snippet` check, are present in the source but fully commented out, with explanatory comments that Solr migration removed/changed support for these — worth deciding whether to port these as removed/obsolete fields or omit entirely in the Playwright rewrite.
- Hardcoded values (digest hashes, offset `45198`, content lengths, filename) are tightly coupled to this exact archived capture (`AWP-Roteiro-20090510220155-00000`, collection `Roteiro`) — if the underlying test fixture/index data changes, these will need updating.
- Note the method name typo "Tecnical" (missing an `h`) is present in the actual source method name `replayTecnicalDetailsTest()`, kept verbatim here.
- All assertions in this test are soft (`appendError`), so a failure in an early field check does not prevent later fields from being checked — all failures are collected and reported together at tearDown.
