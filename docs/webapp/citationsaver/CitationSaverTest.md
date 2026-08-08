# CitationSaverTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/citationsaver/CitationSaverTest.java`
- **Category:** webapp > citationsaver
- **Base class:** `extends WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies that the CitationSaver service page loads correctly, displays the expected slogan and instructional text (in Portuguese), and that the "Ficheiros" (Files) upload tab can be opened to reveal the file-upload label text.

## Preconditions
- `test.url` system property set to target environment.
- Browser/device matrix from `test.browsers.json`.
- Navigates directly to `<testURL>/services/citationsaver` (overriding whatever page `setUp()` initially loaded).

## Test Data
- Page path: `/services/citationsaver`
- Expected slogan (PT only, hardcoded): `"Preserva citações a conteúdos online"`
- Expected instructional text: `"Submeta um documento e o CitationSaver preservará as ligações nele citadas:"`
- Expected label text: `"Insira o URL do documento:"`
- Expected file-upload label text: `"Carregue um documento a partir do seu computador:"`

## Steps & Expected Results

### `citationSaverTest()`
1. Navigate directly to `<testURL>/services/citationsaver`.
2. Wait for the page logo (`#logo-citation-saver`) to be visible.
3. (soft) Assert `#citation-saver-slogan` text equals `"Preserva citações a conteúdos online"` — "Verify slogan from CitationSaver".
4. (soft) Assert text of `#citation-saver-main > p[3]` (xpath) equals `"Submeta um documento e o CitationSaver preservará as ligações nele citadas:"` — "Verify text from CitationSaver".
5. (soft) Assert text of label `form label[for="input-url-url"]` equals `"Insira o URL do documento:"` — "Verify text from CitationSaver".
6. Click the "Ficheiros" tab label (`label[for="input-file-upload"]`) — on iOS platform, uses `JavascriptExecutor` to click via JS instead of native click (worked around because "IOS driver is dumb and sometimes fails to click properly").
7. (soft) Assert text of label `form label[for="input-file-file"]` equals `"Carregue um documento a partir do seu computador:"` — "Verify text from CitationSaver".

**Expected results:**
- All four text checks are soft assertions (appendError), collected and reported together at teardown if any fail.
- No explicit hard assertions in this test beyond implicit waits (visibility timeouts act as hard failures if elements never appear).

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix (WebDriverTestBaseParallel-based)
- Category tags: webapp, citationsaver, static-content, localization (PT only), file-upload-ui

## Notes
- All expected strings are hardcoded in Portuguese only — there is no English variant/locale switch tested for this page, unlike the urlsearch tests.
- Special-cases iOS: detects platform via `RemoteWebDriver` capabilities and uses JS-based `.click()` instead of Selenium's native click due to known flakiness on iOS ("IOS driver is dumb...").
- Does not exercise the actual citation-saving submission behavior (no URL/file is actually submitted) — purely UI/text/tab-switch verification.
