# ReplayPrintTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/options/ReplayPrintTest.java`
- **Category:** webapp > replay > options
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from a replayed (wayback) archived page, the replay right menu's "print" option opens a confirmation popup that can be cancelled (closing the popup), and that re-opening and confirming triggers the browser print action.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Replay right menu button: `#replayMenuButton`
- Print option link: `#printOption > h4:nth-child(1)`
- Cancel button: `#cancelPopup`
- Print confirm popup container: `#uglipop_content_fixed`
- Print confirm button: `#printPage`

## Steps & Expected Results

### `replayPrintTest()`
1. Navigate to the wayback example URL.
2. Open the replay right menu (click `#replayMenuButton`).
3. Click the "print" link (`#printOption > h4:nth-child(1)`).
4. Click "cancel" on the resulting popup (`#cancelPopup`).
5. Click the "print" link again.
6. Click the "print" confirm button (`#printPage`).

**Expected results:**
- "Open replay right menu" — click succeeds (hard, via `run`).
- "Click print link" — click succeeds (hard, via `run`).
- "Cancel print page" — click succeeds (hard, via `run`).
- "Check print confirm page is closed" — the popup (`#uglipop_content_fixed`) becomes invisible within 20s (soft/appendError).
- "Click again on print link" — click succeeds (hard, via `run`).
- "Print page" — click succeeds (hard, via `run`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, options, print

## Notes
- The test deliberately stops after clicking the final "print" confirm button, with an explicit code comment: "stop here the test because next step is browser specific" — i.e. it does not attempt to verify the native browser print dialog itself, since that's OS/browser-chrome and not automatable/verifiable via Selenium.
- No cleanup/dismissal of the native print dialog is performed by this test; a Playwright rewrite would need to decide how to handle (or avoid triggering) the native print dialog in headless/CI contexts.
