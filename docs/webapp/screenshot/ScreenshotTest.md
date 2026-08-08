# ScreenshotTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/screenshot/ScreenshotTest.java`
- **Category:** webapp > screenshot
- **Base class:** `extends AppendableErrorsBaseTest` (NOT `WebDriverTestBaseParallel` — no Selenium/WebDriver is used at all)
- **Test type:** Pure HTTP check (plain Java `URL.openStream()` download over HTTP; no browser, no cross-browser/device matrix, runs once)

## Purpose / Scenario
Verifies that the server-side screenshot endpoint (`/screenshot/?url=...&width=...&height=...`) produces a byte-for-byte consistent image for a specific archived page at a fixed resolution, by downloading the image and checking its MD5 checksum against one known-good value.

## Preconditions
- `test.url` system property read directly in the constructor (`System.getProperty("test.url")`), not via a WebDriver-based base class.
- No browser/WebDriver session is created — this class does not extend `WebDriverTestBaseParallel`; it only extends `AppendableErrorsBaseTest` for the `run`/`appendError`/`tearDown` helpers (though this test doesn't actually use `run`/`appendError` — it uses a plain `assertEquals`).
- Depends on network access to `<testURL>/screenshot/?...` and on the archived page `19961013145650/http://www.fccn.pt/` rendering identically server-side at the specified resolution.

## Test Data
- Target URL constructed as: `<testURL>/screenshot/?url=<testURL>/noFrame/replay/19961013145650/http://www.fccn.pt/&width=2560&height=1440`
- Expected MD5 hash (single exact value, no fallback): `223b57dd7543af7b094ec4c5b9d45dc4`

## Steps & Expected Results

### `screenshotTest()`
1. Build the screenshot URL string from `testURL` + fixed path/timestamp/target-site + `&width=2560&height=1440`.
2. Download the image bytes via `takeScreenshot(screenshotUrlStr)` (plain `java.net.URL.openStream()` + `IOUtils.toByteArray`).
3. Compute the MD5 hex digest of the downloaded bytes via `getMd5(imageBytes)`.
4. (hard) Assert the MD5 equals exactly `"223b57dd7543af7b094ec4c5b9d45dc4"` — "Verify screenshot md5sum".

**Expected results:**
- (hard) Downloaded screenshot image's MD5 checksum exactly matches the single known-good hash, confirming pixel-identical rendering at 2560x1440 for the fixed archived page/timestamp.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Does NOT run across a browser/device matrix — it is a single plain HTTP download test, independent of `WebDriverTestBaseParallel`/Sauce Labs config.
- Category tags: webapp, screenshot, http-only, checksum-verification, fixed-resolution-rendering

## Notes
- Unlike the sibling `PrintTest`, this test accepts only ONE exact MD5 value (no dual-hash tolerance for nondeterminism) — meaning it may be more prone to flaky failures if the server-side screenshot renderer is at all non-deterministic (as `PrintTest`'s comment suggests it can be for the print/PDF-like endpoint). Notably the accepted hash here (`223b57dd7543af7b094ec4c5b9d45dc4`) is one of the two values `PrintTest` accepts, suggesting the two endpoints may share underlying rendering.
- Uses a large, fixed viewport of `2560x1440` — any change to default rendering viewport/device pixel ratio would break this test.
- Because this class extends `AppendableErrorsBaseTest` directly (not `WebDriverTestBaseParallel`), there is no WebDriver setup/teardown, no Sauce Labs reporting, and no parameterized browser matrix — runs once as a pure backend/HTTP integration check, structurally identical in shape to `PrintTest`.
- A Playwright/native reimplementation would likely not need a browser at all for this check — a simple HTTP client + hash comparison suffices, matching the current Java approach. Consider whether image-diff/perceptual-hash tolerance (instead of exact MD5) would be more robust against minor rendering nondeterminism, mirroring the dual-hash workaround already used in `PrintTest`.
