# ReplayScreenshotTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/options/ReplayScreenshotTest.java`
- **Category:** webapp > replay > options
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from a replayed (wayback) archived page, the replay right menu's "screenshot" option opens a confirmation popup that can be cancelled, and that confirming/taking the screenshot and closing the resulting view returns the user to the normal Arquivo.pt page (identified by the Arquivo.pt logo becoming visible again).

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Replay right menu button: `#replayMenuButton`
- Screenshot option link: `#screenshotOption > h4:nth-child(1)`
- Cancel button: `#cancelPopup`
- Confirm popup container: `#uglipop_content_fixed`
- Take-screenshot confirm button: `#takeScreenshot`
- Close popup button: `#closeSpecPopUp`
- Arquivo.pt logo (post-close indicator): `#arquivoLogo`

## Steps & Expected Results

### `replayScreenshotTest()`
1. Navigate to the wayback example URL.
2. Open the replay right menu (click `#replayMenuButton`).
3. Click the "screenshot" link (`#screenshotOption > h4:nth-child(1)`).
4. Click "cancel" on the popup (`#cancelPopup`).
5. Click the "screenshot" link again.
6. Click "take screenshot" (`#takeScreenshot`).
7. Close the replay menu/popup (`#closeSpecPopUp`).
8. Wait for the Arquivo.pt logo (`#arquivoLogo`) to become visible again.

**Expected results:**
- "Open replay right menu" — click succeeds (hard, via `run`).
- "Click screenshot page link" — click succeeds (hard, via `run`).
- "Cancel take screenshot" — click succeeds (hard, via `run`).
- "Check save page as image modal is closed" — popup (`#uglipop_content_fixed`) becomes invisible within 20s (soft/appendError).
- "Click again on screenshot link" — click succeeds (hard, via `run`).
- "Take screenshot" — click succeeds (hard, via `run`).
- "Close replay menu" — click succeeds (hard, via `run`).
- "Wait until Arquivo.pt logo is again displayed" — element becomes visible (hard, via `run`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, options, screenshot

## Notes
- Unlike `ReplayPrintTest`/`ReplayReconstructTest`, this test does not assert on a resulting URL/service after confirming the action — it only verifies the popup closes and the main page UI (logo) reappears, implying the screenshot itself opens/renders inline rather than navigating away.
- Same cancel-then-confirm double-pass pattern as `ReplayPrintTest` and `ReplayReconstructTest`.
