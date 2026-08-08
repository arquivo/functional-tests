# ReplayListVersionsTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/options/ReplayListVersionsTest.java`
- **Category:** webapp > replay > options
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from a replayed (wayback) archived page, the replay right menu's "list versions" link both points to (via its `href`) and, when clicked, actually navigates to a URL search listing all captured versions of the current page, filtered to start from a specific "from" date and ending at the current year.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.
- Uses `java.util.Calendar.getInstance().get(Calendar.YEAR)` to compute the expected "to" year dynamically (current year at test-run time).

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Replay right menu button: `#replayMenuButton`
- List-versions anchor (href check): `#swiperWrapper > div.swiper-slide.replayMenu.swiper-slide-next > a:nth-child(2)`
- List-versions clickable link: `div.swiper-slide:nth-child(3) > a:nth-child(2) > h4:nth-child(1)`
- Expected resulting URL: `{testURL}/url/search?q=http%3A%2F%2Fwww.fccn.pt%2F&from=19910806` (with `to={current year}` also checked)

## Steps & Expected Results

### `replayListVersionsTest()`
1. Navigate to the wayback example URL.
2. Open the replay right menu (click `#replayMenuButton`).
3. Get the "list versions" anchor element (without clicking yet).
4. Read the anchor's `href` attribute and check its content.
5. Click the list-versions link (`div.swiper-slide:nth-child(3) > a:nth-child(2) > h4:nth-child(1)`).

**Expected results:**
- "Open replay right menu" — click succeeds (hard, via `run`).
- "Get list versions button" — element retrieved (hard, via `run`).
- "Check list version button to correct page" — `href` contains `query=http%3A%2F%2Fwww.fccn.pt%2F` and `href` contains `/page/search` (hard, via `run`, using `assertThat` with a boolean condition rather than a matcher — so any assertion message shown includes the actual href).
- "Click list versions anchor" — click succeeds (hard, via `run`).
- "Check url is on list versions" — resulting URL contains all of: `/url/search`, `q=http%3A%2F%2Fwww.fccn.pt%2F`, `from=19910806`, and `to={current year}` within 20s (hard, via `run`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, options, list-versions, url-search

## Notes
- Two-stage check: the anchor's `href` (before navigation) is checked against a `/page/search?query=...` pattern, but after actually clicking, the real resulting page is a different endpoint (`/url/search?q=...`) — these look like two distinct related links/behaviors bundled in the same href/click sequence; worth double-checking against current app behavior when porting.
- `from=19910806` is a hardcoded fixed lower-bound date; `to=` is computed dynamically as the current calendar year, so this test's expected value changes each year it runs — flag this dynamic dependency for the Playwright rewrite.
- The `assertThat("...", booleanExpr)` calls use the single-arg-message + boolean overload (equivalent to `assertTrue` with a message), not a Hamcrest matcher.
