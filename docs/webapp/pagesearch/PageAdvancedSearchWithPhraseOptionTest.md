# PageAdvancedSearchWithPhraseOptionTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/pagesearch/PageAdvancedSearchWithPhraseOptionTest.java`
- **Category:** webapp > pagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the Advanced Search form's "exact phrase" field wraps the entered text in quotes in the generated query, and that results returned actually contain the exact phrase alongside the original search term.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files loaded.

## Test Data
- Search term: `fccn`
- Phrase field (`#phrase`) input: `speedmeter`
- Expected resulting query string: `fccn "speedmeter"`
- Expected result text substring: `speedmeter fccn`

## Steps & Expected Results

### `pageAdvancedSearchWithPhraseOptionTest()`
1. Clear the search box, type `fccn`, and click search. (hard — via `run`)
2. Click the advanced-search link to navigate to the advanced search page. (hard — via `run`)
3. Read the `words` field value.
4. Type `speedmeter` into the phrase field (`#phrase`).
5. Click the advanced-search submit button.
6. Read the resulting value of the main search input.
7. Wait for `#pages-results` to appear.
8. Check whether any result contains "fccn".
9. Check whether any result contains the phrase "speedmeter fccn".

**Expected results:**
- "Check if search words maintain fccn term" — `words` field equals `fccn` (soft/appendError).
- "Insert the option on form field" succeeds (soft/appendError, wraps the `sendKeys`).
- "Click on search on arquivo.pt button" succeeds (soft/appendError).
- "Verify if the - operator is on text box" — search input value trimmed equals `fccn "speedmeter"` (soft/appendError).
- "Verify if the term fccn is displayed on any search result" — at least one result contains "fccn" (hard — plain `assertEquals(true, ...)`, not wrapped).
- "Verify if the term 'speedmeter fccn' is displayed on any search result" — at least one result contains "speedmeter fccn" (hard — plain `assertEquals(true, ...)`, not wrapped).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, pagesearch, advanced-search, phrase-search

## Notes
- Shares the "search fccn → open advanced search → verify words field" preamble with the other `PageAdvancedSearch*` tests.
- The last two assertions are hard (not `appendError`-wrapped), unlike most other assertions in the file — a failure in the "fccn is displayed" check would prevent the phrase-match check from running.
- Assertion message wording ("Verify if the - operator is on text box") is copy-pasted from the negation test and is misleading here since this test checks quoting (`"..."`), not the `-` operator — worth correcting in the new spec's wording even though it's faithfully reproduced from source.
