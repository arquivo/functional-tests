# PageAdvancedSearchNegationOptionTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageAdvancedSearchNegationOptionTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that a user can use the Advanced Search form's "without these words" (negation) field to exclude a term from results, and that the generated query uses the `-` negation operator and actually filters out results containing the excluded word.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Hardcoded base search term `fccn`; negation term `Fundação` (Portuguese, with diacritic).

## Test Data
- Search term: `fccn`
- Negation field (`#without`) input: `Fundação`
- Expected resulting query string: `fccn -Fundação`

## Steps & Expected Results

### `pageAdvancedSearchNegationOptionTest()`
1. Clear the search box, type `fccn`, and click search. (hard — via `run`)
2. Click the advanced-search link to navigate to the advanced search page. (hard — via `run`)
3. Read the `words` field value.
4. Type `Fundação` into the negation field (`#without`).
5. Click the advanced-search submit button.
6. Read the resulting value of the main search input.
7. Wait for `#pages-results` to appear.
8. Check whether any result text contains "fccn".
9. Check whether any result under `#pages-results > ul` visibly contains "Fundação".

**Expected results:**
- "Check if search words maintain fccn term" — `words` field value equals `fccn` (soft/appendError).
- "Insert the negation option on form field" succeeds (soft/appendError, wraps the `sendKeys` action).
- "Click on search on arquivo.pt button" succeeds (soft/appendError).
- "Verify if the - operator is on text box" — search input value trimmed equals `fccn -Fundação` (soft/appendError).
- "Verify if the term fccn is displayed on any search result" — at least one result contains "fccn" (hard — plain `assertEquals(true, ...)`, not wrapped).
- "Verify that no search result contains the visible text Fundação" — count of matching elements is 0 (soft/appendError, message "Check result count should be zero").

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, advanced-search, negation-operator

## Notes
- Shares the same "search fccn → open advanced search → verify words field" preamble as other `PageAdvancedSearch*` tests.
- The final "no Fundação" filter uses `em.getText().toLowerCase().contains("Fundação")` — note the filter calls `.toLowerCase()` on the element text but compares against `"Fundação"` (mixed case, not lowercased), so the containment check is effectively case-sensitive/likely a latent bug in the original test (it would only match if the literal capitalized substring "Fundação" appears in the lowercased text, which normally won't happen) — worth noting for behavioral parity discussions when re-implementing.
- The "fccn is displayed" assertion (step 8) is hard, not soft, so it can short-circuit before the final negation check runs.
