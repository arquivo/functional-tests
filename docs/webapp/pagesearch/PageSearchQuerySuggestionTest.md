# PageSearchQuerySuggestionTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageSearchQuerySuggestionTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that searching for a malformed/likely-misspelled query (a domain name typed without punctuation) triggers a "did you mean" style query-suggestion feature, correctly proposing the properly formatted equivalent.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.

## Test Data
- Search term: `amazoncouk`
- Expected suggestion text substring: `→ Será que quis dizer: amazon.co.uk` (Portuguese "did you mean" phrasing)

## Steps & Expected Results

### `pageSearchQuerySuggestionTest()`
1. Clear the search box, type `amazoncouk`, and click search. (hard — via `run`)
2. Read the text of the suggestion element (`#term-suggested`).

**Expected results:**
- "Verify if a suggestion is presented" — suggestion text contains `→ Será que quis dizer: amazon.co.uk` (hard — plain `assertThat`, not wrapped in `run`/`appendError`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, query-suggestion

## Notes
- Simplest test in the package: one search action, one assertion, no helper methods.
- Assertion and expected text are locale-specific (Portuguese) with no English-locale counterpart test (unlike `PageSearchEmptyTest`, which has PT/EN variants) — implies this test assumes the site's default/no-`?l=` locale is Portuguese.
- The single assertion is hard, so failure here does not allow any other diagnostic checks to run (there are none anyway).
