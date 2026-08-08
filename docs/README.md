# Functional test specs

Markdown specs extracted from this repository's Java/Selenium/JUnit test suite, one file per test
class. They exist to serve as the input for re-implementing each test natively with Playwright in
[`arquivo-webapp-eros`](https://github.com/arquivo/arquivo-webapp-eros), per
[arquivo/pwa-technologies#1596](https://github.com/arquivo/pwa-technologies/issues/1596).

Each spec was produced by reading the corresponding Java source and documenting, in plain English:
purpose/scenario, preconditions, test data, steps, expected results, and tags/categories. No Java
source was modified as part of this effort — this is a read-only extraction.

69 test classes are documented, covering the full `src/test/java/pt/arquivo/tests/**` tree.

## How to read a spec

Every file follows the same template:

- **Source** — path to the original Java file.
- **Category** — derived from the Java package path (there are no JUnit `@Category` annotations
  anywhere in this codebase, so the package is the closest thing to a tag/suite grouping).
- **Base class** — which shared base class the test extends (see [Shared framework
  conventions](#shared-framework-conventions) below).
- **Test type** — Browser (Selenium, cross-browser/device matrix) or HTTP API.
- **Purpose/Scenario** — what real user or system behavior the test verifies.
- **Preconditions** — target environment, config/fixture files loaded, required data.
- **Test Data** — notable hardcoded queries, URLs, dates, expected strings.
- **Steps & Expected Results** — one subsection per `@Test` method, with plain-English steps and
  the assertions each step is checked against (marked hard vs. soft — see below).
- **Tags/Annotations** — `@Retry`, `@Ignore`, browser/device matrix, suite membership.
- **Notes** — anything noteworthy: magic numbers, locale (pt/en) variants, flakiness, bugs spotted
  in the existing test code, inheritance.

## Shared framework conventions

These apply across most spec files and are documented once here instead of being repeated in
every file:

- **`WebDriverTestBaseParallel`** (`src/test/java/pt/arquivo/selenium/WebDriverTestBaseParallel.java`)
  is the base class most `webapp` and `cms` tests extend. It's `@RunWith(ConcurrentParameterized.class)`,
  so each test class runs once per browser/device configuration supplied via the `test.browsers.json`
  system property (or the `SAUCE_ONDEMAND_BROWSERS` env var) — typically a matrix such as Windows 10
  Chrome/Firefox/Edge, macOS Chrome/Firefox/Safari, Android Chrome, and iOS Safari, run against Sauce
  Labs or a local Selenium Grid (`docker-compose.yaml`). `testURL` comes from the `test.url` system
  property (e.g. `https://arquivo.pt` or `https://preprod.arquivo.pt`).
  `waitUntilElementIsVisibleAndGet(By)` waits up to 40s for an element to become visible.
- **`@Retry`** (`src/test/java/pt/arquivo/selenium/Retry.java`) + **`RetryRule`** (retry count 6): a
  test method annotated `@Retry` is retried up to 6 extra times with a 30s wait between attempts if
  it fails, before the failure is reported.
- **`AppendableErrorsBaseTest`** (`src/test/java/pt/arquivo/utils/AppendableErrorsBaseTest.java`)
  provides two assertion styles used throughout:
  - `run(String errorMessage, Runnable/Callable)` — wraps an action; on failure, rethrows
    immediately with the given description. This is a **hard** failure that stops the test method.
  - `appendError(String errorMessage, Runnable)` / `appendError(Runnable)` — a **soft** assertion:
    catches the failure, prints the stack trace, and stores the error. All accumulated errors are
    thrown together in `tearDown()`, so later soft assertions in the same method still run even if
    an earlier one failed.
- Tests under `api/**` extend `AppendableErrorsBaseTest` directly (no browser) — they exercise
  Arquivo.pt's Page Search / Memento Timemap HTTP APIs.
- Tests under `cms/**` use the Page Object pattern (`src/test/java/pt/arquivo/tests/cms/pages/*.java`);
  most (but not all — `SuggestionSiteTest` is excluded) are grouped by a JUnit `@Suite` in
  `src/test/java/pt/arquivo/tests/cms/suite/TestSuite.java`.

## Structure

```
docs/
├── webapp/       tests exercising the main Arquivo.pt search webapp (search, replay, menu, ...)
├── api/          tests exercising the Page Search / Memento Timemap HTTP APIs directly
├── cms/          tests exercising the institutional/CMS "Sobre" site
└── other/        standalone features that don't fit the above (404 widget, memorial, contamehistorias)
```

### webapp (46 files)

| Suite | Tests |
|---|---|
| [pagesearch](webapp/pagesearch/) | [PageSearchTest](webapp/pagesearch/PageSearchTest.md), [PageSearchEmptyTest](webapp/pagesearch/PageSearchEmptyTest.md), [PageSearchNotSpamTest](webapp/pagesearch/PageSearchNotSpamTest.md), [PageSearchNotArchivedFileTest](webapp/pagesearch/PageSearchNotArchivedFileTest.md), [PageSearchOverlapDatesTest](webapp/pagesearch/PageSearchOverlapDatesTest.md), [PageSearchLimitedDatesFromHomepageTest](webapp/pagesearch/PageSearchLimitedDatesFromHomepageTest.md), [PageSearchQuerySuggestionTest](webapp/pagesearch/PageSearchQuerySuggestionTest.md), [PageAdvancedSearchTest](webapp/pagesearch/PageAdvancedSearchTest.md), [PageAdvancedSearchMimeTypeTest](webapp/pagesearch/PageAdvancedSearchMimeTypeTest.md), [PageAdvancedSearchNegationOptionTest](webapp/pagesearch/PageAdvancedSearchNegationOptionTest.md), [PageAdvancedSearchWithPhraseOptionTest](webapp/pagesearch/PageAdvancedSearchWithPhraseOptionTest.md) |
| [imagesearch](webapp/imagesearch/) | [ImageSearchTest](webapp/imagesearch/ImageSearchTest.md), [ImageSearchDirectUrlTest](webapp/imagesearch/ImageSearchDirectUrlTest.md), [ImageSearchQuerySuggestionTest](webapp/imagesearch/ImageSearchQuerySuggestionTest.md), [ImageAdvancedSearchTest](webapp/imagesearch/ImageAdvancedSearchTest.md) |
| [menu](webapp/menu/) | [MenuTest](webapp/menu/MenuTest.md) (abstract base), [MenuAboutHomepageTest](webapp/menu/MenuAboutHomepageTest.md), [MenuChangeLanguageTest](webapp/menu/MenuChangeLanguageTest.md), [MenuImagesNewSearchHomepageTest](webapp/menu/MenuImagesNewSearchHomepageTest.md), [MenuImagesAdvancedSearchHomepageTest](webapp/menu/MenuImagesAdvancedSearchHomepageTest.md), [MenuPagesNewSearchHomepageTest](webapp/menu/MenuPagesNewSearchHomepageTest.md), [MenuPagesNewAvancedSearchHomepageTest](webapp/menu/MenuPagesNewAvancedSearchHomepageTest.md) |
| [replay](webapp/replay/) | [ReplayTest](webapp/replay/ReplayTest.md) |
| [replay/menu](webapp/replay/menu/) | [MenuAboutWaybackTest](webapp/replay/menu/MenuAboutWaybackTest.md), [MenuImagesNewSearchWaybackTest](webapp/replay/menu/MenuImagesNewSearchWaybackTest.md), [MenuImagesAdvancedSearchWaybackTest](webapp/replay/menu/MenuImagesAdvancedSearchWaybackTest.md), [MenuPagesNewSearchWaybackTest](webapp/replay/menu/MenuPagesNewSearchWaybackTest.md), [MenuPagesNewAvancedSearchWaybackTest](webapp/replay/menu/MenuPagesNewAvancedSearchWaybackTest.md), [MenuSavePageNowTest](webapp/replay/menu/MenuSavePageNowTest.md) |
| [replay/options](webapp/replay/options/) | [ReplayExpandTest](webapp/replay/options/ReplayExpandTest.md), [ReplayListVersionsTest](webapp/replay/options/ReplayListVersionsTest.md), [ReplayPrintTest](webapp/replay/options/ReplayPrintTest.md), [ReplayScreenshotTest](webapp/replay/options/ReplayScreenshotTest.md), [ReplayReconstructTest](webapp/replay/options/ReplayReconstructTest.md), [ReplayTechnicalDetailsTest](webapp/replay/options/ReplayTechnicalDetailsTest.md) |
| [workflow](webapp/workflow/) | [WorkflowStateBetweenSearchPagesTest](webapp/workflow/WorkflowStateBetweenSearchPagesTest.md), [WorkflowStateBetweenSearchImagesTest](webapp/workflow/WorkflowStateBetweenSearchImagesTest.md), [WorkflowStateBetweenSearchPageAndImageTest](webapp/workflow/WorkflowStateBetweenSearchPageAndImageTest.md) |
| [urlsearch](webapp/urlsearch/) | [URLSearchTableTest](webapp/urlsearch/URLSearchTableTest.md), [URLSearchListTest](webapp/urlsearch/URLSearchListTest.md), [URLSearchListNotContainsSomeHttpCodesTest](webapp/urlsearch/URLSearchListNotContainsSomeHttpCodesTest.md) |
| other webapp | [DatePickerTest](webapp/datepicker/DatePickerTest.md), [NarrativeButtonTest](webapp/narrative/NarrativeButtonTest.md), [PrintTest](webapp/print/PrintTest.md), [ScreenshotTest](webapp/screenshot/ScreenshotTest.md), [CitationSaverTest](webapp/citationsaver/CitationSaverTest.md), [SavePageNowURLNotFoundTest](webapp/savepagenow/SavePageNowURLNotFoundTest.md) |

### api (9 files)

| Suite | Tests |
|---|---|
| [memento](api/memento/) | [TimemapTest](api/memento/TimemapTest.md) |
| [pagesearch](api/pagesearch/) | [PageSearchAPITest](api/pagesearch/PageSearchAPITest.md) |
| [pagesearch/params](api/pagesearch/params/) | [CollectionTest](api/pagesearch/params/CollectionTest.md), [FieldsTest](api/pagesearch/params/FieldsTest.md), [FromAndToTest](api/pagesearch/params/FromAndToTest.md), [MaxItemsTest](api/pagesearch/params/MaxItemsTest.md), [OffsetTest](api/pagesearch/params/OffsetTest.md), [SiteSearchTest](api/pagesearch/params/SiteSearchTest.md), [TypeTest](api/pagesearch/params/TypeTest.md) |

### cms (10 files)

[CommonQuestionsTest](cms/CommonQuestionsTest.md), [ExamplesTest](cms/ExamplesTest.md),
[FooterTest](cms/FooterTest.md), [NavigationTest](cms/NavigationTest.md), [NewsTest](cms/NewsTest.md),
[PublicationsTest](cms/PublicationsTest.md), [SearchTest](cms/SearchTest.md),
[SiteMapTest](cms/SiteMapTest.md), [Soft404MessageTest](cms/Soft404MessageTest.md),
[SuggestionSiteTest](cms/SuggestionSiteTest.md)

### other (3 files)

[Arquivo404Test](other/Arquivo404Test.md) — embeddable 404 widget,
[MemorialTest](other/MemorialTest.md) — data-driven redirect checks for decommissioned "memorial" sites,
[ContaMeHistoriasTest](other/ContaMeHistoriasTest.md) — smoke test on an external partner site.

## Known issues spotted in the existing Java tests

While extracting these specs, a few pre-existing bugs/oddities in the current test code were noted
(see the "Notes" section of the relevant file for detail) — worth double-checking when porting to
Playwright rather than carrying the bug forward silently:

- `NavigationTest` reuses the PT `reports` link-check object in its EN pass (copy-paste bug), and has
  several assertions effectively commented out/no-ops.
- `Soft404MessageTest` has a likely missing-argument bug in one assertion and reversed `assertEquals`
  argument order in another.
- `SuggestionSiteTest` has a likely `By.id` vs `By.xpath` locator bug, is PT-only, and is explicitly
  excluded from `cms.suite.TestSuite`.
- `FooterTest` loads PT/EN fixture files under `src/test/resources/sobreTestsFiles/` that are dead
  code for the code path actually exercised.
- `SiteMapTest`'s `SiteMapTopicsPT.txt` fixture is likewise unused by the current implementation.
