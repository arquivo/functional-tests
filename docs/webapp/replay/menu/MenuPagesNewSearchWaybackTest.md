# MenuPagesNewSearchWaybackTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/menu/MenuPagesNewSearchWaybackTest.java`
- **Category:** webapp > replay > menu
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from an archived (wayback) page, opening the hamburger menu, then the "Pages" sub-menu, and clicking "new search" navigates to the page search page.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Depends on the archived FCCN snapshot at `WAYBACK_EXAMPLE` existing on the target instance.

## Test Data
- `WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/"`
- Menu button: `#menuButton` (desktop: `#menuButton > span`, mobile: `#menuButton > div`)
- Pages sub-menu: `#pagesMenu > h4:nth-child(1)`
- New search link: `#pageOptions > a:nth-child(1) > h4:nth-child(1)`
- Expected URL fragment: `/page/search?`

## Steps & Expected Results

### `menuPagesNewSearchWaybackTest()`
1. Navigate to the wayback example URL.
2. Wait for and click the menu button (desktop `span` or mobile `div`, whichever is displayed).
3. Click the "Pages" sub-menu (`#pagesMenu > h4:nth-child(1)`).
4. Click the "new search" option (`#pageOptions > a:nth-child(1) > h4:nth-child(1)`).

**Expected results:**
- "Click menu button" — menu opens (hard, via `run`).
- "Open pages sub menu" — click succeeds (hard, via `run`).
- "Click new search button" — click succeeds (hard, via `run`).
- "Check if current url is the page search" — URL contains `/page/search?` within 20s (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, menu, pages, new-search

## Notes
- Structurally near-identical to `MenuPagesNewAvancedSearchWaybackTest`, `MenuImagesNewSearchWaybackTest`, and `MenuImagesAdvancedSearchWaybackTest` — same menu-open pattern, differing only in which sub-menu/option is clicked and the expected URL fragment. No shared abstract base class exists; each duplicates the desktop/mobile menu-button branch inline.
