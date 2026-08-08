# MenuTest (abstract base class)

- **Source:** `src/test/java/pt/arquivo/tests/webapp/menu/MenuTest.java`
- **Category:** webapp > menu
- **Base class:** WebDriverTestBaseParallel (abstract; itself extended by concrete menu test classes)
- **Test type:** Browser (Selenium, cross-browser/device matrix) — NOT independently runnable, has no `@Test` methods.

## Purpose / Scenario
`MenuTest` is an abstract base class providing the single shared template step used by its concrete subclasses: opening the site's left-hand navigation ("hamburger") menu. It defines no `@Test` methods itself and is not collected/run by JUnit on its own.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class (`WebDriverTestBaseParallel`).
- No external config/resource files loaded.

## Test Data
- None (no hardcoded search terms/dates; only element IDs used).

## Steps & Expected Results
This class has no `@Test` methods. It defines one protected template/shared method that subclasses call as the first step of their own test flows:

### `openMenu()` (protected, shared template method — not a `@Test`)
1. Click the left-nav menu button (`#nav-menu-button-left`).
2. Wait for the left nav panel (`#left-nav`) to become visible.

**Expected results:**
- Both actions are wrapped together in a single `run("Open left menu", ...)` call — i.e. a hard failure ("Open left menu") is thrown immediately if either the click or the visibility wait fails, aborting the calling test method.

## Tags / Annotations
- `@Retry`: no (no `@Test` methods on this class; concrete subclasses declare their own `@Retry` on their `@Test` methods)
- `@Ignore`: no
- Not run directly — abstract class
- Category tags: webapp, menu, shared-base, navigation

## Notes
- This is a *template method* base class: subclasses call `openMenu()` and then implement their own menu-item click(s) and destination-page assertion(s).
- Concrete subclasses documented separately, each referencing this file for the shared `openMenu()` step rather than repeating its description:
  - `MenuAboutHomepageTest` (extends `MenuTest`)
  - `MenuImagesAdvancedSearchHomepageTest` (extends `MenuTest`)
  - `MenuImagesNewSearchHomepageTest` (extends `MenuTest`)
  - `MenuPagesNewAvancedSearchHomepageTest` (extends `MenuTest`)
  - `MenuPagesNewSearchHomepageTest` (extends `MenuTest`)
  - `MenuChangeLanguageTest` does **NOT** extend `MenuTest` — it extends `WebDriverTestBaseParallel` directly and re-implements its own inline "open menu" step (`run("Open menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click())`), without waiting for `#left-nav` visibility. See its own doc file for details.
