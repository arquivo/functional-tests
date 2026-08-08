# MemorialTest

- **Source:** `src/test/java/pt/arquivo/tests/memorial/MemorialTest.java`
- **Category:** other > memorial
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
"Memorial" sites are decommissioned/defunct real-world websites (government agencies, research programmes, personal/organisation pages) whose domains now redirect visitors to their preserved Arquivo.pt copy instead of a normal 404 or live site. This regression test loads a data-driven list of such memorial URLs, visits each live URL, verifies the redirect landing page shows an explanatory message, clicks the button that forwards the user into Arquivo.pt's Wayback-style replay, and then verifies the resulting archived (replayed) page shows the expected content at the expected timestamp/URL.

## Preconditions
- Target env: each `config.url` in the data file is visited directly (this test does not use `testURL`); the destination it ends up on is Arquivo.pt's wayback/replay URL.
- Resource file loaded: `src/test/resources/memorial_config.yaml`, deserialized via Jackson YAML into `MemorialTestConfigYAMLFile` → array of `MemorialTestConfig`.
- Assumes each memorial domain in the YAML currently exists, is configured to show a redirect/"site decommissioned" page with a button (`form > button`), and forwards to Arquivo.pt where the given timestamp/memento is still available.
- Assumes the replay page renders inside an iframe named `replay_iframe` (used by `ReplayUtils.checkTextOnReplayPage`).

## Test Data
`memorial_config.yaml` contains a top-level `configs` list; currently 48 entries. Each entry has:
- `url` (required) — live memorial-site URL to visit.
- `timestamp` (required) — expected Arquivo.pt memento timestamp that should appear in the final replay URL.
- `waybackUrl` (optional) — expected original URL that should appear in the replay URL; if absent, defaults to `url` itself (via `getWaybackUrl()` fallback).
- `redirectPageText` (optional) — text expected on the interim redirect/decommission page before the user clicks through.
- `waybackTextXPath` (optional) — XPath locating an element on the replayed archived page.
- `waybackText` (optional) — expected text content at `waybackTextXPath` on the replayed page.

Not every entry sets every optional field (e.g. some entries only have `url`/`timestamp`, skipping `redirectPageText`/`waybackText`/`waybackTextXPath`).

## Steps & Expected Results

### `testMemorialSites()`
For each `config` entry in `memorial_config.yaml` (looped in a single `@Test` method, not JUnit-parameterized):
1. Load YAML config via Jackson (`ObjectMapper` + `YAMLFactory`) from classpath resource `memorial_config.yaml`.
2. Navigate the browser to `config.getUrl()`.
3. If `redirectPageText` is set, check the full page (`/html`) text contains it.
4. Click the first visible `form > button` element on the redirect page (uses filtered `findElements` + `.click()`, since a plain `click()` was unreliable across browsers).
5. Wait (up to 120s) until the resulting URL contains both `config.getTimestamp()` and a normalized form of `config.getWaybackUrl()` (protocol and leading `www.` stripped).
6. Call `ReplayUtils.checkTextOnReplayPage(driver, config.getWaybackTextXPath(), config.getWaybackText())`, which switches into the `replay_iframe`, waits (up to 30s) for `waybackText` to be present at `waybackTextXPath` (defaulting to `/html` if no xpath given), then switches back to default content.

**Expected results:**
- (soft/appendError) "Check some visible text on redirect page should contain some text to inform user before redirect to Arquivo.pt" — only evaluated when `redirectPageText` is non-null.
- (hard/`run`) "Click on button to redirect to Arquivo.pt" — clicking the visible form button must succeed (any exception is rethrown, halting further per-entry checks in `run`, but the outer forEach continues to the next config since exceptions are handled at each config's own step... actually since `run` rethrows and the loop is a plain `forEach`, an exception here would abort processing of remaining configs for that test invocation).
- (soft/appendError) "Check wayback page url" — final URL contains both the expected timestamp and the (normalized) wayback URL.
- (hard/`run`) "Check some text on wayback page" — the expected `waybackText` (if any) is present at `waybackTextXPath` inside the iframe.

## Tags / Annotations
- `@Retry`: no (not used in this class)
- `@Ignore`: no
- Runs across configured browser/device matrix (Windows/MacOS/Android/iOS combinations from `test.browsers.json`)
- Category tags: other, memorial

## Notes
- Data-driven: a single `@Test` method iterates all 48 entries in `memorial_config.yaml` internally (not via `ConcurrentParameterized`/JUnit parameterization) — a failure in one entry's hard (`run`) steps can abort the remaining forEach iterations for that browser run, since `forEach` does not catch/continue past a thrown exception.
- Strong external dependency on third-party domains staying alive and continuing to serve their "site decommissioned, redirecting to Arquivo.pt" interstitial exactly as before, and on the referenced Arquivo.pt mementos (by timestamp) remaining archived/unchanged — high risk of flakiness/staleness as external sites or archived content change over time.
- Text is largely Portuguese (`redirectPageText` commonly "O site foi desactivado...", `waybackText` values in PT or EN depending on the original site's language).
- `waybackUrl` mismatch handling: URL-contains check strips `http://`, `https://`, and leading `www.` before comparing, to be resilient to protocol/host normalization by the replay system.
- `ReplayUtils` also has a deprecated single-text overload (`checkTextOnReplayPage(driver, text)`) not used by this test — the class instead uses the xpath-aware overload.
