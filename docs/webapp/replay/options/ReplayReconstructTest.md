# ReplayReconstructTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/options/ReplayReconstructTest.java`
- **Category:** webapp > replay > options
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from a replayed (wayback) archived page, the replay right menu's "complete page" (reconstruct, formerly Memento-style "reconstruct") option opens a confirmation popup that can be cancelled, and that confirming it navigates to the `complete-page` service with the correct original `url` and `timestamp` query parameters.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.
- Uses `CustomConditions.browserUrlContains` for URL checks, since replay pages report the archived page's original URL via `document.URL`, not the real browser address.
- Contains a documented workaround for a flaky driver/SauceLabs behavior where the browser has visibly navigated to the CompletePage service (per video) but `driver.getCurrentUrl()`/element waits still reflect the prior Replay page; in that case the test force-navigates via `driver.get(href)` using the `#a_reconstruct` anchor's `href`.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Replay right menu button: `#replayMenuButton`
- "Complete page" link: `#a_reconstruct > h4:nth-child(1)`
- Cancel button: `#cancelPopup`
- Confirm popup container: `#uglipop_content_fixed`
- Confirm button: `#completePage`
- Expected destination fragment: `/services/complete-page`
- Expected query params: `url=` URL-encoded `http://www.fccn.pt/`, and `timestamp=19961013145650`

## Steps & Expected Results

### `replayReconstructTest()`
1. Navigate to the wayback example URL.
2. Open the replay right menu (click `#replayMenuButton`).
3. Click the "complete page" link (`#a_reconstruct > h4:nth-child(1)`).
4. Click "cancel" on the popup (`#cancelPopup`).
5. Click the "complete page" link again.
6. Click "confirm" (`#completePage`).
7. Wait (up to 20s) for the browser URL to contain `/services/complete-page`; if this times out, and there's more than one `#a_reconstruct`/`head` element present (driver appears "stuck"), force-navigate directly to the `#a_reconstruct` anchor's `href`.
8. Verify final destination.

**Expected results:**
- "Open replay right menu" — click succeeds (hard, via `run`).
- "Click complete page link" — click succeeds (hard, via `run`).
- "Click cancel on the popup" — click succeeds (hard, via `run`).
- "Check complete page confirm page is closed" — popup (`#uglipop_content_fixed`) becomes invisible within 20s (soft/appendError).
- "Click complete page link" (again) — click succeeds (hard, via `run`).
- "Click confirm on the popup" — click succeeds (hard, via `run`).
- (Unlabeled recovery step) — attempts to wait for URL `/services/complete-page`; on failure/timeout, forces a `driver.get()` redirect as a fallback (not an assertion — a defensive workaround, wrapped in try/catch, no failure raised here even if it doesn't resolve).
- "Check that we moved to CompletePage service" — final browser URL contains all of `/services/complete-page`, `url={URL-encoded http://www.fccn.pt/}`, and `timestamp=19961013145650` within 20s (hard, via `run`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, options, reconstruct, complete-page

## Notes
- Contains a commented-out constant `MEMENTO_TIMETRAVEL` referencing `https://timetravel.mementoweb.org/reconstruct/...`, suggesting this feature was historically modeled after (or compared against) the Memento TimeTravel reconstruct service; currently unused/dead code.
- The "stuck driver" workaround (steps 7) is a notable flakiness mitigation specific to this test (SauceLabs video showing the page loaded while WebDriver APIs still report the old page) — worth flagging as a known-flaky interaction pattern for the Playwright rewrite, though Playwright's auto-waiting may not need an equivalent workaround.
- Uses `CustomConditions.browserUrlContains` (see `ReplayExpandTest` notes) rather than plain Selenium `ExpectedConditions.urlContains`.
