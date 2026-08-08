# MenuChangeLanguageTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/menu/MenuChangeLanguageTest.java`
- **Category:** webapp > menu
- **Base class:** WebDriverTestBaseParallel (does **NOT** extend `MenuTest`, despite living in the same package — it re-implements its own inline "open menu" step instead of reusing `MenuTest.openMenu()`; see `docs/webapp/menu/MenuTest.md` Notes)
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that switching the site language via the left-menu "language" toggle correctly flips all visible locale-dependent labels (Pages/Images/Advanced search labels, and the menu's own language-toggle label) and updates the URL's language query parameter, in both directions (PT→EN and EN→PT).

## Preconditions
- Target env from `test.url`.
- Uses `pt.arquivo.utils.LocaleUtils.changeLanguageToPT` / `changeLanguageToEN` to force the starting locale via URL query parameter before running the shared verification flow.
- Does not use `MenuTest.openMenu()`; opens the menu inline via `run("Open menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click())` — note this does **not** additionally wait for `#left-nav` visibility, unlike `MenuTest.openMenu()`.

## Test Data
- Labels checked, PT vs EN pairs:
  - Pages label (`#search-form-pages`): `Páginas` / `Pages`
  - Images label (`#search-form-images`): `Imagens` / `Images`
  - Advanced search label (`#search-form-advanced`): `Pesquisa avançada` / `Advanced search`
  - Language menu item (`#menu-language`) — shows the *other* language's name as the action label: `English` (when on PT) / `Português` (when on EN)
- Expected URL fragment after toggling: `l=en` (when starting from PT) / `l=pt` (when starting from EN)
- After toggling, labels are expected to have flipped to the opposite language, and the language menu item now shows `Português` (was `English`) or `English` (was `Português`).

## Steps & Expected Results
Both test methods delegate to the shared private helper `menuChangeLaguageTest(Locale locale)` after forcing the starting locale. (Note: method name has a typo, "Laguage", preserved from source.)

### `menuChangeLaguagePTtoENTest()`
1. Force language to PT via `LocaleUtils.changeLanguageToPT`.
2. Run shared flow `menuChangeLaguageTest(PORTUGUESE)` (see below).

### `menuChangeLaguageENtoPTTest()`
1. Force language to EN via `LocaleUtils.changeLanguageToEN`.
2. Run shared flow `menuChangeLaguageTest(ENGLISH)` (see below).

### `menuChangeLaguageTest(Locale locale)` (shared private helper used by both tests above)
1. Read the Pages label text (`#search-form-pages`).
2. Read the Images label text (`#search-form-images`).
3. Read the Advanced search label text (`#search-form-advanced`).
4. Open the menu (`#nav-menu-button-left`). (hard — via `run`)
5. Read the language menu item text (`#menu-language`).
6. Click the language menu item to trigger the language switch. (hard — via `run`)
7. Wait (up to 10s) for the URL to contain the opposite language's query fragment (`l=en` or `l=pt`). (hard — via `run`)
8. Re-read the Pages label text (expecting the opposite-language value).
9. Re-read the Images label text (expecting the opposite-language value).
10. Re-read the Advanced search label text (expecting the opposite-language value).
11. Re-open the menu (`#nav-menu-button-left`). (hard — via `run`)
12. Re-read the language menu item text (expecting it now shows the original starting language's name).

**Expected results:**
- "Verify page label" (pre-toggle) — `#search-form-pages` text contains the current-locale label (hard — plain `assertThat`, not wrapped in `run`/`appendError`).
- "Verify image label" (pre-toggle) — `#search-form-images` text contains the current-locale label (hard — plain `assertThat`).
- "Verify advanced search label" (pre-toggle) — `#search-form-advanced` text contains the current-locale label (hard — plain `assertThat`).
- "Open menu" succeeds (hard — via `run`).
- "Verify language label" (pre-toggle) — `#menu-language` text contains the *other* language's name (hard — plain `assertThat`).
- "Change language" click succeeds (hard — via `run`).
- "Wait for page to change" — URL contains the new language fragment within 10s (hard — via `run`).
- "Verify page label" (post-toggle) — `#search-form-pages` text now contains the opposite-language label (hard — plain `assertThat`).
- "Verify image label" (post-toggle) — `#search-form-images` text now contains the opposite-language label (hard — plain `assertThat`).
- "Verify advanced search label" (post-toggle) — `#search-form-advanced` text now contains the opposite-language label (hard — plain `assertThat`).
- "Open menu" (post-toggle) succeeds (hard — via `run`).
- "Verify language label" (post-toggle) — `#menu-language` text now contains the original starting language's name (hard — plain `assertThat`).

## Tags / Annotations
- `@Retry`: yes (both `@Test` methods, retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, menu, localization, language-toggle, navigation

## Notes
- Unlike the other Menu tests in this package, this class does not extend the abstract `MenuTest` base class and duplicates its own simplified "open menu" logic (no wait for `#left-nav` visibility) — worth flagging as an inconsistency for the Playwright port to potentially unify.
- All assertions in this test are hard (plain `assertThat`, not `appendError`), so the first failing check (e.g. pre-toggle label mismatch) aborts the rest of the test immediately.
- Method/variable names contain a recurring typo ("Laguage" instead of "Language") preserved faithfully from source.
- Uses the `LocalizedString` helper pattern (`.pt(...).en(...).apply(locale)`) extensively to compute both "current locale" and "opposite locale" expected strings.
