# SuggestionSiteTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/SuggestionSiteTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (**no** — `SuggestionSiteTest` is explicitly NOT listed in `pt.arquivo.tests.cms.suite.TestSuite`'s `@SuiteClasses`, unlike the other 9 tests documented in this batch)

## Purpose / Scenario
Checks that the "Suggest a site" form of the Arquivo.pt about site (Collaborate menu) is shown, in Portuguese only. The form is a Google Form embedded in the page. It isn't filled in or submitted: that would send a suggestion to the team, and the form has a reCAPTCHA. Submitting it is a manual test.

## Preconditions
- Target env: `test.url`.
- Page objects used: `IndexSobrePage` (entry point), `CollaboratePage` (via `index.goToCollaboratePage("PT")`, which hovers the "Colabore" menu item `menu-item-11376`), `SuggestionPage` (via `collaborate.goToSuggestionSitePage("PT")`, which clicks the "Sugerir site" sub-menu item `menu-item-4408`; EN would be `menu-item-4435`).
- No fixture files loaded.
- English version is not implemented (`//TODO english version missing`).

## Test Data
- The form iframe is located with the CSS selector `iframe.contact-form`, and its `src` must start with `https://docs.google.com/forms/`.
- Inside the iframe, a form field is located with `form textarea, form input[type=text]`.

## Steps & Expected Results

### `suggestionsSiteTest()`
1. Construct `IndexSobrePage` from the current driver.
2. Hover the Collaborate menu: `index.goToCollaboratePage("PT")`.
3. Click the suggestion sub-menu item: `collaborate.goToSuggestionSitePage("PT")`.
4. Call `sug.checkSuggestionForm("PT")`, which:
   a. Waits (up to 50s) for the `iframe.contact-form` to be visible and checks its `src` is a Google Form.
   b. Switches into the iframe and waits (up to 50s) for a visible text field of the form.
   c. Switches back to the page.

**Expected results:**
- `assertTrue("Failed The Suggestion Site Page Test in Portuguese", sug.checkSuggestionForm("PT"))` (hard) — the embedded form is a Google Form and shows its fields.
- If `IndexSobrePage` construction, `goToCollaboratePage`, or `goToSuggestionSitePage` throws `IOException`/`FileNotFoundException`, hard-fails with `fail("IOException -> suggestionsSiteTest")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, suggestion-form
- **Not included in `TestSuite`** — of the 10 classes documented in this batch, `SuggestionSiteTest` is the one exception not wired into `pt.arquivo.tests.cms.suite.TestSuite`.

## Notes
- The test used to fill in and submit the old WordPress contact form and tried to solve the Google reCAPTCHA by clicking image tiles at fixed positions, which never worked reliably. The site now embeds a Google Form, so the test only checks the form is shown. A native reimplementation shouldn't try to solve a real reCAPTCHA either.
- Only the Portuguese path is exercised.
