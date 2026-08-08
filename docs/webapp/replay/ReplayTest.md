# ReplayTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/replay/ReplayTest.java`
- **Category:** webapp > replay > (top-level)
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Smoke-tests basic replay of several very old (1996) archived pages from different institutions (FCCN, Uminho, ISCTE, IST), confirming that each archived page loads inside the replay iframe and shows page-specific expected text.

## Preconditions
- Target env comes from the `test.url` system property, loaded automatically by the base class.
- No external config/resource files are loaded.
- Relies on specific archived snapshots existing in the target Arquivo.pt instance at the hardcoded timestamps/URLs below.
- Uses a `JavascriptExecutor`-based workaround (`getTextContentUsingJs`) instead of `WebElement.getText()`, because desktop Safari's driver misbehaves after switching into iframes.

## Test Data
- `/wayback/19961013145650/http://www.fccn.pt/` — expects text containing `rccn` at CSS selector `body > blockquote:nth-child(5) > h1 > a`.
- `/wayback/19961013171554/http://www.fccn.pt/index_i.html` — expects text containing `portuguese` at `body > b:nth-child(12)`.
- `/wayback/19961013145852/http://s700.uminho.pt:80/homepage-pt.html` — expects text containing `portugal` at `body > center:nth-child(1) > h1`.
- `/wayback/19961013202814/http://www.iscte.pt/` — expects text containing `iscte` at `body > center:nth-child(3) > table > tbody > tr > td:nth-child(1) > h1 > center`.
- `/wayback/19961013171626/http://www.ist.utl.pt/` — expects text containing `ist` at `body > p:nth-child(3) > b`.

## Steps & Expected Results

### `replayTest()`
1. Navigate to the FCCN wayback snapshot (`19961013145650`).
2. Switch driver context into the `#replay_iframe` iframe (wait up to 10s).
3. Read text of `body > blockquote:nth-child(5) > h1 > a` via JS and lower-case it.
4. Navigate to a second FCCN snapshot (`19961013171554`, `index_i.html`).
5. Switch context into `#replay_iframe` again.
6. Read text of `body > b:nth-child(12)` via JS.
7. Navigate to the Uminho snapshot (`19961013145852`).
8. Switch context into `#replay_iframe`.
9. Read text of `body > center:nth-child(1) > h1` via JS.
10. Navigate to the ISCTE snapshot (`19961013202814`).
11. Switch context into `#replay_iframe`.
12. Read text of `body > center:nth-child(3) > table > tbody > tr > td:nth-child(1) > h1 > center` via JS.
13. Navigate to the IST snapshot (`19961013171626`).
14. Switch context into `#replay_iframe`.
15. Read text of `body > p:nth-child(3) > b` via JS.

**Expected results:**
- "Switch context to iframe" — succeeds each time (hard, via `run`).
- "Verify that the term RCCN is displayed on the FCCN web page" — text contains `rccn` (hard, plain `assertThat`, not wrapped in `run`/`appendError`).
- "Verify if the term Portuguese is displayed on the FCCN web page" — text contains `portuguese` (hard, plain `assertThat`).
- "Verify if the term Portugal is displayed on the Uminho web page" — text contains `portugal` (hard, plain `assertThat`).
- "Verify if the term ISCTE is displayed on the ISCTE web page" — text contains `iscte` (hard, plain `assertThat`).
- "Verify if the term IST is displayed on the IST web page" — text contains `ist` (hard, plain `assertThat`).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, replay, iframe, smoke-test

## Notes
- All assertions use plain `assertThat` (not `run`/`appendError`), so the first failing check aborts the whole test — the four site checks are not independent soft assertions.
- The `getTextContentUsingJs` helper is a documented Safari-iframe workaround (see the Javadoc comment in the source): `document.querySelector(...).textContent` via `JavascriptExecutor`, since `WebElement` interaction breaks on desktop Safari after an iframe switch.
- CSS selectors are brittle/positional (`nth-child` chains) reflecting the exact DOM of these 1996-era archived pages — worth flagging for the Playwright rewrite.
