# MenuAboutHomepageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/menu/MenuAboutHomepageTest.java`
- **Category:** webapp > menu
- **Base class:** MenuTest (abstract) — see `docs/webapp/menu/MenuTest.md` for the shared `openMenu()` step
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that, from the homepage in either Portuguese or English, opening the left menu and clicking "About" navigates to the correct locale-specific `sobre.arquivo.pt` page, and that the Arquivo.pt logo is present on that destination page.

## Preconditions
- Target env from `test.url`.
- Uses `pt.arquivo.utils.LocaleUtils.changeLanguageToPT` / `changeLanguageToEN` to force the starting locale via URL query parameter (`?l=pt` / `?l=en`) before running the shared flow.
- Uses shared `openMenu()` from `MenuTest` (see `MenuTest.md`).

## Test Data
- Menu item clicked: `#menu-about`
- Expected destination URL (PT): `https://sobre.arquivo.pt/pt/`
- Expected destination URL (EN): `https://sobre.arquivo.pt/en/`
- Logo selector checked on destination page: `.headerLogoAndSearch a img`

## Steps & Expected Results
Both test methods delegate to the shared private helper `menuAbout(Locale locale)` after forcing the locale.

### `menuAboutHomepagePTTest()`
1. Force language to PT via `LocaleUtils.changeLanguageToPT`.
2. Run shared flow `menuAbout(PORTUGUESE)` (see below).

### `menuAboutHomepageENTest()`
1. Force language to EN via `LocaleUtils.changeLanguageToEN`.
2. Run shared flow `menuAbout(ENGLISH)` (see below).

### `menuAbout(Locale locale)` (shared private helper used by both tests above)
1. Open the left menu (`openMenu()`, inherited from `MenuTest` — see `MenuTest.md`).
2. Click the "About" menu item (`#menu-about`). (hard — via `run`)
3. Wait (up to 100s) for the Arquivo.pt logo (`.headerLogoAndSearch a img`) to become visible.
4. Read the current URL.

**Expected results:**
- "Open left menu" succeeds (hard — inherited from `MenuTest.openMenu()`).
- "Click about button" succeeds (hard — via `run`, no explicit assertion beyond the click itself).
- "Check if Arquivo.pt log appears" — logo element becomes visible within 100s (soft/appendError; note: comment/message says "log" — likely a typo for "logo").
- "Verify sobre.arquivo.pt" — current URL equals the locale-specific expected URL (`https://sobre.arquivo.pt/pt/` or `.../en/`) (hard — via `run`).

## Tags / Annotations
- `@Retry`: yes (both `@Test` methods, retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, menu, navigation, localization, external-link

## Notes
- Navigates off-domain, to a separate `sobre.arquivo.pt` site — the destination is an external microsite, not part of the main arquivo.pt app.
- Contains commented-out/dead code for a locale-specific logo XPath (`logoXpath`), superseded by a single CSS selector shared across locales — flagged as intentionally simplified, not an oversight.
- Uses `LocalizedString` helper (`.pt(...).en(...).apply(locale)`) to select expected values per locale — the same pattern appears across other Menu tests.
