# PageSearchEmptyTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchEmptyTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that searching for a query that returns no results shows a proper "no results found" message, in both Portuguese and English locales, and that the message includes the original search query.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- Uses `LocaleUtils` (`pt.arquivo.utils.LocaleUtils`) to switch the site language by navigating to `<testURL>?l=pt` or `<testURL>?l=en` before searching.
- No other external config/resource files loaded.

## Test Data
- Query (deliberately nonsensical/no-match): `xptoxptoxptoxptoxptoxptoxptoxptoxpto` (constant `QUERY`)
- Expected PT no-results message substring: `Não foram encontrados resultados para a sua pesquisa: `
- Expected EN no-results message substring: `No results were found for the query: `

## Steps & Expected Results

Both test methods delegate to the shared private helper `pageSearchTest(String noResultsMessage)`:
1. Clear the search box, type `QUERY`, and click search. (hard — via `run`)
2. Wait for `#pages-results` to appear. (hard — via `run`)
3. Check that the "no results" element (`#no-results-were-found`) becomes visible (via `ExpectedConditions.visibilityOfElementLocated`, though note this call's return value is not actually waited on/asserted — see Notes).
4. Read the trimmed text of `#no-results-were-found`.
5. Check that this text contains the locale-specific `noResultsMessage`.
6. Check that this text also contains the original `QUERY` string.

### `pageSearchEmptyPTTest()`
1. Switch site language to Portuguese via `LocaleUtils.changeLanguageToPT`.
2. Run the shared `pageSearchTest` flow with the PT no-results message.

**Expected results:**
- "Empty result message should be visible" (soft/appendError) — see Notes on this being a no-op check.
- "Empty result message should contains specific text" — message contains `Não foram encontrados resultados para a sua pesquisa: ` (soft/appendError).
- "Empty result message should show search criteria" — message contains the `QUERY` string (soft/appendError).

### `pageSearchEmptyENTest()`
1. Switch site language to English via `LocaleUtils.changeLanguageToEN`.
2. Run the shared `pageSearchTest` flow with the EN no-results message.

**Expected results:**
- Same three checks as above but expecting the English message `No results were found for the query: `.

## Tags / Annotations
- `@Retry`: yes on both test methods (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, empty-query, locale (pt/en)

## Notes
- Two locale variants of the same scenario (PT/EN) share all logic through the private `pageSearchTest` helper — parameterize this in the Playwright rewrite (e.g. table-driven test over locale + expected message).
- The "Empty result message should be visible" step calls `ExpectedConditions.visibilityOfElementLocated(emptyResultMessageBy)` inside `appendError`, but never actually applies it to a `WebDriverWait` or asserts on it — the `ExpectedCondition` object is constructed and discarded without evaluation, so this check is effectively a no-op in the current code (a latent bug/dead code, faithfully documented here rather than fixed).
- Relies on `LocaleUtils.changeLanguageTo*` which navigates via URL query parameter `?l=pt`/`?l=en` rather than a UI language switcher.
