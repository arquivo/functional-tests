# ImageSearchQuerySuggestionTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/imagesearch/ImageSearchQuerySuggestionTest.java`
- **Category:** webapp > imagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that searching images for a misspelled/partial query (`lisbao`) triggers a "did you mean" style query-suggestion link pointing to the correct term (`lisboa`).

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.

## Test Data
- Search term: `lisbao`
- Expected suggested-term text contains: `lisboa`

## Steps & Expected Results

### `imageSearchQuerySuggestionTest()`
1. Clear the search box (`#submit-search-input`), type `lisbao`, click search (`#submit-search`). (hard — via `run`)
2. Switch to image search results (`#search-form-images button`). (hard — via `run`)
3. Read the suggested-term link text (`#term-suggested a`).

**Expected results:**
- "Verify if a suggestion is presented" — suggested-term link text contains `lisboa` (hard — plain `assertThat`, not wrapped in `run`/`appendError`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, imagesearch, query-suggestion, spell-correction

## Notes
- Very small/focused test: only one final assertion, and it's a hard assertion (not soft), so no accumulated-error mechanism is exercised here.
- Depends on the search backend's query-suggestion/spell-correction feature actually surfacing `lisboa` for the misspelled input `lisbao`.
