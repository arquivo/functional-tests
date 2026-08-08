# CommonQuestionsTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/CommonQuestionsTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
Verifies that the "Common Questions" (FAQ) page of the Arquivo.pt "Sobre" (about) site displays the exact expected list of frequently-asked questions, in both Portuguese and English.

## Preconditions
- Target env: `test.url` (loaded via `WebDriverTestBaseParallel`, driver navigates there in `setUp()`).
- Page objects used: `IndexSobrePage` (constructed from the loaded home page; reads `sobreTestsFiles/props_indexPage.properties` for expected page titles, though the title check itself is commented out/dead code), `CommonQuestionsPage` (navigated to via `index.goToCommonQuestionsPage()`, clicking the FAQ link in a sidebar widget).
- Fixture files loaded (by `CommonQuestionsPage` constructor): `src/test/resources/sobreTestsFiles/CommonQuestions_pt.txt` and `CommonQuestions_en.txt`.
- No other hardcoded data in the test class itself; an unused field `isPreProd = true` is present but never referenced.

## Test Data
- `CommonQuestions_pt.txt` contains 9 expected Portuguese question strings, e.g.:
  1. Para que serve?
  2. Como surgiu o Arquivo.pt?
  3. O que motivou a sua criação?
  ... through 9. Outra questão acerca do Arquivo.pt?
- `CommonQuestions_en.txt` contains the corresponding 9 English question strings, e.g.:
  1. What is it for?
  2. When was Arquivo.pt born?
  ... through 9. Others questions?
- `CommonQuestionsPage.inspectQuestions()` locates FAQ headings via XPath `//*[@id="post-2096"]/div/div/div/h3` (PT) or `//*[@id="post-2392"]/div/div/div/h3` (EN), requires the count to match the fixture list size, and each heading's text to equal the fixture line at the same index, in order.

## Steps & Expected Results

### `commonQuestionsTest()`
1. Construct `IndexSobrePage` from the current driver (loads `props_indexPage.properties`, waits 5s for the index page to load).
2. Navigate to the Common Questions page: `index.goToCommonQuestionsPage()` (clicks the FAQ sidebar widget link).
3. Portuguese version: call `commonQuestions.inspectQuestions("PT")`, which reads the 9 `<h3>` question headings under `post-2096` and compares them one-by-one, in order, against `CommonQuestions_pt.txt`.
4. English version: `switchLanguage()` is invoked internally by `inspectQuestions("EN")` (clicks the "English" language toggle link), then reads `<h3>` headings under `post-2392` and compares against `CommonQuestions_en.txt`.

**Expected results:**
- `assertTrue("Failed The Common Question in Portuguese", ...)` (hard) — the 9 PT questions must match fixture content exactly, in order.
- `assertTrue("Failed The Common Question in English", ...)` (hard) — the 9 EN questions must match fixture content exactly, in order.
- If `IndexSobrePage` construction throws `IOException`, the test hard-fails with `fail("IOException -> IndexSobrePage")`.
- If `goToCommonQuestionsPage()` throws `FileNotFoundException`, the test hard-fails with `fail("FileNotFoundException -> goToCommonQuestionsPage")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, faq

## Notes
- Depends on fixture files `CommonQuestions_pt.txt` and `CommonQuestions_en.txt` (both 9 lines) — a native Playwright reimplementation should keep these as the source of truth for expected question text and count.
- `CommonQuestionsPage` constructor itself throws `FileNotFoundException` if either fixture is missing, which the test also catches generically at the `goToCommonQuestionsPage()` call site.
- The XPath-based element IDs (`post-2096`, `post-2392`) are hardcoded WordPress post IDs and may be fragile/brittle if CMS content changes.
- Comparison is strict equality per line index (order-sensitive), not just membership.
