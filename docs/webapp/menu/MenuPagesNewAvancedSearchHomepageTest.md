# MenuPagesNewAvancedSearchHomepageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/menu/MenuPagesNewAvancedSearchHomepageTest.java`
- **Category:** webapp > menu
- **Base class:** MenuTest (abstract) — see `docs/webapp/menu/MenuTest.md` for the shared `openMenu()` step
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from the homepage, opening the left menu, expanding the "Pages" sub-menu, and clicking "Advanced search" navigates to the page (text) advanced search page.

## Preconditions
- Target env from `test.url`.
- Uses shared `openMenu()` from `MenuTest` (see `MenuTest.md`).

## Test Data
- Sub-menu clicked: `#menu-pages`
- Menu item clicked: `#menu-pages-advanced-search`
- Expected destination URL fragment: `/page/advanced/search?`

## Steps & Expected Results

### `menuPagesNewAvancedSearchHomepageTest()`
1. Open the left menu (`openMenu()`, inherited from `MenuTest` — see `MenuTest.md`).
2. Click the "Pages" sub-menu (`#menu-pages`). (hard — via `run`)
3. Click the "Advanced search" item (`#menu-pages-advanced-search`). (hard — via `run`)
4. Wait (up to 20s) for the URL to contain `/page/advanced/search?`.

**Expected results:**
- "Open left menu" succeeds (hard — inherited from `MenuTest.openMenu()`).
- "Open pages sub menu" click succeeds (hard — via `run`, no explicit assertion beyond the click itself).
- "Click new advanced search button" click succeeds (hard — via `run`, no explicit assertion beyond the click itself).
- "Check if current url is the advanced search" — URL contains `/page/advanced/search?` within 20s (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, menu, navigation, pagesearch, advanced-search

## Notes
- Class name preserves a typo from source ("Avanced" instead of "Advanced") — kept faithfully in this doc's filename/title to match the Java class name.
- Structurally identical to `MenuImagesAdvancedSearchHomepageTest`, `MenuImagesNewSearchHomepageTest`, and `MenuPagesNewSearchHomepageTest` — all four follow the same pattern (open menu → open sub-menu → click a link → assert URL fragment), differing only in which sub-menu/link is clicked and which URL fragment is expected.
- Only the final URL-check assertion is soft (`appendError`); the menu-opening/clicking steps are hard via `run`, so a failure to reach the sub-menu aborts the test before the URL check is even attempted.
