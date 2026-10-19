# ScreenshotTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/screenshot/ScreenshotTest.java`
- **Category:** webapp > screenshot
- **Base class:** `extends AppendableErrorsBaseTest` (NOT `WebDriverTestBaseParallel` — no Selenium/WebDriver is used at all)
- **Test type:** Pure HTTP check (plain Java `URL.openStream()` download over HTTP; no browser, no cross-browser/device matrix, runs once)

## Purpose / Scenario
Verifies that the server-side screenshot endpoint (`/screenshot/?url=...&width=...&height=...`) returns a real, non-blank image of the requested resolution for a specific archived page, by downloading the response, decoding it as an image and checking its size and how many of its pixels aren't white.

## Preconditions
- `test.url` system property read directly in the constructor (`System.getProperty("test.url")`), not via a WebDriver-based base class.
- No browser/WebDriver session is created — this class does not extend `WebDriverTestBaseParallel`; it only extends `AppendableErrorsBaseTest` for the `run`/`appendError`/`tearDown` helpers (though this test doesn't actually use `run`/`appendError` — it uses plain `assertEquals`/`assertTrue`).
- Depends on network access to `<testURL>/screenshot/?...` and on the archived page `19961013145650/http://www.fccn.pt/` rendering server-side at the specified resolution.

## Test Data
- Target URL constructed as: `<testURL>/screenshot/?url=<testURL>/noFrame/replay/19961013145650/http://www.fccn.pt/&width=2560&height=1440`
- Expected image size: `2560` x `1440` pixels
- Blank-image threshold: more than `0.5`% of the pixels must be non-white (any pixel whose RGB isn't `0xFFFFFF`)

## Steps & Expected Results

### `screenshotTest()`
1. Build the screenshot URL string from `testURL` + fixed path/timestamp/target-site + `&width=2560&height=1440`.
2. Download the image bytes via `takeScreenshot(screenshotUrlStr)` (plain `java.net.URL.openStream()` + `IOUtils.toByteArray`).
3. (hard) Decode the bytes as an image via `ImageUtils.readImage(imageBytes)` (`javax.imageio.ImageIO`); if they can't be decoded (e.g. an HTML error page), fail with "Response isn't an image, it starts with: <first 100 bytes>".
4. (hard) Assert the image width equals `2560` — "Verify screenshot width".
5. (hard) Assert the image height equals `1440` — "Verify screenshot height".
6. (hard) Compute the percentage of non-white pixels via `ImageUtils.nonWhitePercentage(image)` and assert it is greater than `0.5` — "Verify screenshot isn't blank".

**Expected results:**
- (hard) The downloaded screenshot decodes as an image.
- (hard) The image is exactly 2560x1440 pixels.
- (hard) More than 0.5% of its pixels are non-white, i.e. the render isn't blank.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Does NOT run across a browser/device matrix — it is a single plain HTTP download test, independent of `WebDriverTestBaseParallel`/Sauce Labs config.
- Category tags: webapp, screenshot, http-only, image-validation, fixed-resolution-rendering

## Notes
- The test used to compare the image's MD5 checksum against a single known-good hash, which broke whenever the rendered pixels changed between environments or renderer versions (fonts, how broken images are drawn). It now only checks that the response is a valid, non-blank image of the requested size, like the sibling `PrintTest` (which additionally skips the size check).
- Uses a large, fixed viewport of `2560x1440` — the test checks the returned image has exactly that size, so any change to how the endpoint applies `width`/`height` (e.g. device pixel ratio scaling) would break it.
- Because this class extends `AppendableErrorsBaseTest` directly (not `WebDriverTestBaseParallel`), there is no WebDriver setup/teardown, no Sauce Labs reporting, and no parameterized browser matrix — runs once as a pure backend/HTTP integration check, structurally identical in shape to `PrintTest`.
- A Playwright/native reimplementation would likely not need a browser at all for this check — a simple HTTP client + an image decoder to check the size and count non-white pixels suffices, matching the current Java approach (shared helper `pt.arquivo.utils.ImageUtils`).
