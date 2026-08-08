# MenuImagesNewSearchHomepageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/menu/MenuImagesNewSearchHomepageTest.java`
- **Category:** webapp > menu
- **Base class:** MenuTest (abstract) — see `docs/webapp/menu/MenuTest.md` for the shared `openMenu()` step
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from the homepage, opening the left menu, expanding the "Images" sub-menu, and clicking "New search" navigates to the image search page.

## Preconditions
- Target env from `test.url`.
- Uses shared `openMenu()` from `MenuTest` (see `MenuTest.md`).

## Test Data
- Sub-menu clicked: `#menu-images`
- Menu item clicked: `#menu-images-new-search`
- Expected destination URL fragment: `/image/search?`

## Steps & Expected Results

### `menuImagesNewSearchHomepageTest()`
1. Open the left menu (`openMenu()`, inherited from `MenuTest` — see `MenuTest.md`).
2. Click the "Images" sub-menu (`#menu-images`). (hard — via `run`)
3. Click the "New search" item (`#menu-images-new-search`). (hard — via `run`)
4. Wait (up to 20s) for the URL to contain `/image/search?`.

**Expected results:**
- "Open left menu" succeeds (hard — inherited from `MenuTest.openMenu()`).
- "Open images sub menu" click succeeds (hard — via `run`, no explicit assertion beyond the click itself).
- "Click new search button" click succeeds (hard — via `run`, no explicit assertion beyond the click itself).
- "Check if current url is the image search" — URL contains `/image/search?` within 20s (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, menu, navigation, imagesearch

## Notes
- Structurally identical to `MenuImagesAdvancedSearchHomepageTest`, `MenuPagesNewSearchHomepageTest`, and `MenuPagesNewAvancedSearchHomepageTest` — all four follow the same pattern (open menu → open sub-menu → click a link → assert URL fragment), differing only in which sub-menu/link is clicked and which URL fragment is expected.
- Only the final URL-check assertion is soft (`appendError`); the menu-opening/clicking steps are hard via `run`, so a failure to reach the sub-menu aborts the test before the URL check is even attempted.
