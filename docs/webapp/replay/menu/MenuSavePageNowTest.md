# MenuSavePageNowTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/menu/MenuSavePageNowTest.java`
- **Category:** webapp > replay > menu
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from an archived (wayback) page, opening the hamburger menu and clicking the "Save Page Now" entry navigates to the save/archive-page-now service.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Menu button: `#menuButton` (desktop: `#menuButton > span`, mobile: `#menuButton > div`)
- SavePageNow link: `#swiperWrapper > div.swiper-slide.menu.swiper-slide-active > a:nth-child(8) > h4`
- Expected URL fragment: either `/services/savepagenow?` or `/services/archivepagenow?`

## Steps & Expected Results

### `menuSavePageNowTest()`
1. Navigate to the wayback example URL.
2. Wait for and click the menu button (desktop `span` or mobile `div`, whichever is displayed).
3. Click the "SavePageNow" option (`#swiperWrapper > div.swiper-slide.menu.swiper-slide-active > a:nth-child(8) > h4`).

**Expected results:**
- "Click menu button" — menu opens (hard, via `run`).
- "Click SavePageNow button" — click succeeds (hard, via `run`).
- "Check if current url is savepagenow" — URL contains `/services/savepagenow?` OR `/services/archivepagenow?` within 20s (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, menu, save-page-now

## Notes
- Two acceptable URL fragments (`savepagenow` vs `archivepagenow`) are checked with `ExpectedConditions.or(...)`, suggesting the service/endpoint name may have changed or differs between environments; both are tolerated.
- Same desktop/mobile menu-button branch pattern duplicated inline as in the other `replay/menu` tests.
