# ContaMeHistoriasTest

- **Source:** `src/test/java/pt/arquivo/tests/contamehistorias/ContaMeHistoriasTest.java`
- **Category:** other > contamehistorias
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
"ContaMeHistorias" ("Tell Me Stories") is an external storytelling/exhibit website (`http://contamehistorias.pt`) that is built on/associated with Arquivo.pt content. This is a minimal smoke test confirming that site's footer still credits/references Arquivo.pt, as a basic check that the partner site's integration/attribution hasn't been removed or broken.

## Preconditions
- Target env: hardcoded external URL `http://contamehistorias.pt` — this test does **not** use `testURL`/`test.url`, it always hits the same fixed live site regardless of the environment under test.
- No YAML/config resource files are loaded; the expected string is hardcoded in the test source.
- Assumes the site is reachable and has a `<footer>` element directly under `<body>` (located via absolute XPath `/html/body/footer`).

## Test Data
None — no external config/resource file; a single hardcoded target URL and expected substring (`"Arquivo.pt"`).

## Steps & Expected Results

### `findArquivoReference()`
1. Navigate to `http://contamehistorias.pt`.
2. Locate the `<footer>` element via XPath `/html/body/footer` and read its text.

**Expected results:**
- (soft/appendError) "Check footer contains arquivo.pt reference" — footer text contains the substring `"Arquivo.pt"`.

## Tags / Annotations
- `@Retry`: no (not used in this class)
- `@Ignore`: no
- Runs across configured browser/device matrix (Windows/MacOS/Android/iOS combinations from `test.browsers.json`)
- Category tags: other, contamehistorias

## Notes
- Unlike most other suites in this repo, this test targets a fixed external domain rather than the environment-configurable `test.url`, so it always exercises the same live third-party site regardless of which Arquivo.pt environment (prod/pre-prod) the test run targets.
- Very brittle to any redesign of `contamehistorias.pt` that changes the DOM structure (absolute XPath `/html/body/footer`) or removes/alters the attribution wording.
- No `@Retry` annotation, so a transient network hiccup against the external site is not automatically retried at the test-method level (only whatever retry/wait behavior exists in the base class applies).
