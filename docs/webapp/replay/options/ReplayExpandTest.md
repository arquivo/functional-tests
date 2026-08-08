# ReplayExpandTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/options/ReplayExpandTest.java`
- **Category:** webapp > replay > options
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from a replayed (wayback) archived page, opening the replay side/right menu and clicking "Expand" ("no frame" view) navigates the browser to the equivalent `/noFrame/replay/...` URL (i.e. the archived page rendered without the surrounding Arquivo.pt frame/toolbar).

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_PATH` existing on the target instance.
- Uses `CustomConditions.browserUrlContains` (checks `window.location.href` after switching to default content, since `document.URL` on an archived page returns the original archived URL, not the current browser URL).

## Test Data
- `WAYBACK_SITE = "http://www.fccn.pt/"`
- `WAYBACK_PATH = "/wayback/19961013145650/http://www.fccn.pt/"`
- `NOFRAME_EXAMPLE = "/noFrame/replay/19961013145650/http://www.fccn.pt/"`
- Replay right menu button: `#replayMenuButton`
- Expand link: `#expandPage`

## Steps & Expected Results

### `replayExpandTest()`
1. Navigate to the wayback path (`WAYBACK_PATH`).
2. Open the replay right menu (click `#replayMenuButton`).
3. Click the "Expand" link (`#expandPage`).

**Expected results:**
- "Open replay right menu" — click succeeds (hard, via `run`).
- "Click expand link" — click succeeds (hard, via `run`).
- "Check we go to correct no frame url" — browser URL contains `NOFRAME_EXAMPLE` within 20s, using `CustomConditions.browserUrlContains` (hard, via `run`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, options, expand, no-frame

## Notes
- Uses `CustomConditions.browserUrlContains` rather than plain `ExpectedConditions.urlContains`, because on replayed pages `document.URL` reflects the archived page's original URL, not the actual browser address bar URL — a peculiarity worth preserving in the Playwright rewrite (checking `page.url()` after navigation, not any DOM/JS-exposed URL).
- Method declares `throws InterruptedException` though nothing in the body appears to throw it directly (likely a vestige/lambda signature artifact).
