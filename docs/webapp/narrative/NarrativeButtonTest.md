# NarrativeButtonTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/narrative/NarrativeButtonTest.java`
- **Category:** webapp > narrative
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies the "Narrative" search tool button: clicking it opens a confirmation modal warning the user they will leave Arquivo.pt for ContaMeHistorias.pt; the modal's cancel button closes it without navigating; and confirming ("OK") navigates to the external ContaMeHistorias.pt site with the original search query and default parameters carried over in the URL.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Homepage/search page loaded via base class `setUp()`.
- Depends on external site `contamehistorias.pt` being reachable and preserving expected URL query parameters.

## Test Data
- Search query: `fccn` (typed but search not submitted before clicking Narrative)
- Expected modal warning text: `"Vai sair do Arquivo.pt para o ContaMeHistorias.pt"`
- Expected destination URL must contain all of: `https://contamehistorias.pt/arquivopt/search`, `query=fccn`, `last_years=10`, `lang=pt`

## Steps & Expected Results

### `narrativeButtonTest()`
1. Clear search input, type `fccn` (no explicit search submit).
2. Click the Narrative search-tools button (`#search-tools-narrative-button`).
3. Wait for confirmation modal `#confirm-narrative-modal` to appear.
4. (soft) Assert text of modal paragraph (`#confirm-narrative-modal/ul/li[1]/p` xpath) equals `"Vai sair do Arquivo.pt para o ContaMeHistorias.pt"` — "Verify text from ContaMeHistorias.pt".
5. Click the modal's cancel button (`#confirm-narrative-modal/ul/li[3]/a/button` xpath).
6. (soft) Assert modal `#confirm-narrative-modal` becomes invisible within 20s — "Check if modal is closed".
7. Click the Narrative button again (`#search-tools-narrative-button`).
8. Wait for modal to reappear.
9. Click the "OK" confirm button (`#search-form-narrative/button` xpath).
10. (soft) Wait up to 20s for the current URL to satisfy all of: contains `https://contamehistorias.pt/arquivopt/search`, contains `query=fccn`, contains `last_years=10`, contains `lang=pt` (combined `ExpectedConditions.and(...)`) — "Verify text from ContaMeHistorias.pt".

**Expected results:**
- (soft) Modal warning text matches expected leave-site message.
- (soft) Modal closes (becomes invisible) after clicking cancel, without navigating away.
- (soft) After confirming, browser navigates to ContaMeHistorias.pt with the search query, a `last_years=10` default window, and `lang=pt`, all present in the URL.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, narrative, external-integration, modal-confirmation

## Notes
- There is a commented-out (dead) `assertEquals` block at the end comparing the full URL to a specific string (`https://contamehistorias.pt/arquivopt/searching?query=fccn&last_years=10&lang=pt`) — left in as a comment, not executed; the actual test uses the more lenient `urlContains` conditions instead.
- The `lang=pt` expectation is hardcoded regardless of any locale switching — this test does not exercise an English-locale variant.
- Tests a genuine cross-site navigation (external dependency on contamehistorias.pt); a Playwright reimplementation should consider whether to mock/stub this external navigation or accept it as an external dependency in CI.
