# PrintTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/print/PrintTest.java`
- **Category:** webapp > print
- **Base class:** `extends AppendableErrorsBaseTest` (NOT `WebDriverTestBaseParallel` — no Selenium/WebDriver is used at all)
- **Test type:** Pure HTTP check (plain Java `URL.openStream()` download over HTTP; no browser, no cross-browser/device matrix, runs once)

## Purpose / Scenario
Verifies that the server-side "print" endpoint (`/screenshot?url=...&download=false`) that renders a printable version of an archived page produces byte-for-byte consistent output, by downloading the response and checking its MD5 checksum against known-good values.

## Preconditions
- `test.url` system property read directly in the constructor (`System.getProperty("test.url")`), not via a WebDriver-based base class.
- No browser/WebDriver session is created — this class does not extend `WebDriverTestBaseParallel`, so none of the `@Before`/`setUp` WebDriver machinery runs; it only extends `AppendableErrorsBaseTest` for the `run`/`appendError`/`tearDown` helpers.
- Depends on network access to `<testURL>/screenshot?...` and on the archived page `19961013145650/http://www.fccn.pt/` still rendering identically server-side.

## Test Data
- Target URL constructed as: `<testURL>/screenshot?url=<testURL>/noFrame/replay/19961013145650/http://www.fccn.pt/&download=false`
- Accepted MD5 hashes (either is valid): `c65787ae99ea0e04848ed324e790cf49` or `223b57dd7543af7b094ec4c5b9d45dc4`

## Steps & Expected Results

### `printTest()`
1. Build the screenshot/print URL string from `testURL` + fixed path/timestamp/target-site + `&download=false`.
2. Print the constructed URL to stdout for debugging.
3. Download the resource bytes via `print(screenshotUrlStr)` (plain `java.net.URL.openStream()` + `IOUtils.toByteArray`).
4. Compute the MD5 hex digest of the downloaded bytes via `getMd5(bytes)`.
5. (hard) Assert the MD5 equals `"c65787ae99ea0e04848ed324e790cf49"` OR `"223b57dd7543af7b094ec4c5b9d45dc4"` — "Verify print md5sum".

**Expected results:**
- (hard) Downloaded print output's MD5 checksum matches one of two accepted known-good hashes, confirming the print rendering is byte-identical to a previously verified output.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Does NOT run across a browser/device matrix — it is a single plain HTTP download test, independent of `WebDriverTestBaseParallel`/Sauce Labs config.
- Category tags: webapp, print, http-only, checksum-verification, screenshot-rendering

## Notes
- Explicit code comment: "md5 sometimes is c65787ae99ea0e04848ed324e790cf49, other times it's 223b57dd7543af7b094ec4c5b9d45dc4. No idea why, so we check for both." — indicates known non-determinism/flakiness in the server-side rendering output that the test works around by accepting two hashes.
- Because this class extends `AppendableErrorsBaseTest` directly rather than `WebDriverTestBaseParallel`, there is no automatic WebDriver setup/teardown, no Sauce Labs reporting, and no parameterized browser matrix — it runs exactly once per test invocation, as a pure backend/HTTP integration check disguised among the Selenium suite.
- Uses `org.apache.commons.io.IOUtils.toByteArray` to read the full response body and Java's `MessageDigest`/`BigInteger` to compute and hex-format the MD5.
- A Playwright/native reimplementation would likely not need a browser at all for this check — a simple HTTP client + hash comparison suffices, matching the current Java approach.
