# PrintTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/print/PrintTest.java`
- **Category:** webapp > print
- **Base class:** `extends AppendableErrorsBaseTest` (NOT `WebDriverTestBaseParallel` — no Selenium/WebDriver is used at all)
- **Test type:** Pure HTTP check (plain Java `URL.openStream()` download over HTTP; no browser, no cross-browser/device matrix, runs once)

## Purpose / Scenario
Verifies that the server-side "print" endpoint (`/screenshot?url=...&download=false`) that renders a printable version of an archived page returns a real, non-blank image, by downloading the response, decoding it as an image and checking that enough of its pixels aren't white.

## Preconditions
- `test.url` system property read directly in the constructor (`System.getProperty("test.url")`), not via a WebDriver-based base class.
- No browser/WebDriver session is created — this class does not extend `WebDriverTestBaseParallel`, so none of the `@Before`/`setUp` WebDriver machinery runs; it only extends `AppendableErrorsBaseTest` for the `run`/`appendError`/`tearDown` helpers.
- Depends on network access to `<testURL>/screenshot?...` and on the archived page `19961013145650/http://www.fccn.pt/` still rendering server-side.

## Test Data
- Target URL constructed as: `<testURL>/screenshot?url=<testURL>/noFrame/replay/19961013145650/http://www.fccn.pt/&download=false`
- Blank-image threshold: more than `0.5`% of the pixels must be non-white (any pixel whose RGB isn't `0xFFFFFF`)
- No expected image size is checked (the default print size differs between environments)

## Steps & Expected Results

### `printTest()`
1. Build the screenshot/print URL string from `testURL` + fixed path/timestamp/target-site + `&download=false`.
2. Print the constructed URL to stdout for debugging.
3. Download the resource bytes via `print(screenshotUrlStr)` (plain `java.net.URL.openStream()` + `IOUtils.toByteArray`).
4. (hard) Decode the bytes as an image via `ImageUtils.readImage(bytes)` (`javax.imageio.ImageIO`); if they can't be decoded (e.g. an HTML error page), fail with "Response isn't an image, it starts with: <first 100 bytes>".
5. (hard) Compute the percentage of non-white pixels via `ImageUtils.nonWhitePercentage(image)` and assert it is greater than `0.5` — "Verify print isn't blank".

**Expected results:**
- (hard) The downloaded print output decodes as an image.
- (hard) More than 0.5% of its pixels are non-white, i.e. the render isn't blank.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Does NOT run across a browser/device matrix — it is a single plain HTTP download test, independent of `WebDriverTestBaseParallel`/Sauce Labs config.
- Category tags: webapp, print, http-only, image-validation, screenshot-rendering

## Notes
- The test used to compare the MD5 checksum of the output against a list of known-good hashes, which kept growing because the rendered pixels vary between environments and renderer versions (fonts, how broken images are drawn, default size). It now only checks that the response is a valid, non-blank image; per the code comment, the size isn't checked either because "The default size differs between environments".
- Because this class extends `AppendableErrorsBaseTest` directly rather than `WebDriverTestBaseParallel`, there is no automatic WebDriver setup/teardown, no Sauce Labs reporting, and no parameterized browser matrix — it runs exactly once per test invocation, as a pure backend/HTTP integration check disguised among the Selenium suite.
- Uses `org.apache.commons.io.IOUtils.toByteArray` to read the full response body and the shared `pt.arquivo.utils.ImageUtils` helper (`readImage`, `nonWhitePercentage`) to decode and inspect the image.
- A Playwright/native reimplementation would likely not need a browser at all for this check — a simple HTTP client + an image decoder to count non-white pixels suffices, matching the current Java approach.
