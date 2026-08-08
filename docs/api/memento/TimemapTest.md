# TimemapTest

- **Source:** `src/test/java/pt/arquivo/tests/api/memento/TimemapTest.java`
- **Category:** api > memento
- **Base class:** AppendableErrorsBaseTest (confirmed: `extends AppendableErrorsBaseTest`)
- **Test type:** HTTP API (no browser)

## Purpose / Scenario
Verifies that the Memento Timemap API (`/wayback/timemap/link/<url>`) returns a well-formed link-format Timemap for a known archived URL (`www.fccn.pt`), including the standard `self`, `timegate`, `original`, and `memento` relation entries with their expected attributes.

## Preconditions
- Base URL is read from the `test.url` system property (`System.getProperty("test.url")`), set once in the constructor.
- No auth, no config/resource files loaded.
- Endpoint under test: `<testURL>/wayback/timemap/link/www.fccn.pt`.

## Test Data
- Target site: `www.fccn.pt`.
- Expected `self` line: URL contains `<testURL>/wayback/timemap/link/http://www.fccn.pt`, `rel="self"`, `type="application/link-format"`, `from="Sun, 13 Oct 1996 14:56:50 GMT"`.
- Expected `timegate` line: URL contains `<testURL>/wayback/http://www.fccn.pt>`, `rel="timegate"`.
- Expected `original` line: URL contains `<http://www.fccn.pt>`, `rel="original"`.
- Expected `memento` line(s): URL contains `fccn.pt` and `<testURL`, `rel="memento"`, has a `datetime="..."` attribute. For the specific memento with timestamp `19961013145650mp_`, URL (with `:80` stripped) must equal `<testURL>/wayback/19961013145650mp_/http://www.fccn.pt/>` and `datetime="Sun, 13 Oct 1996 14:56:50 GMT"`.

## Steps & Expected Results

### `timemapTest()`
1. Build the URL `<testURL>/wayback/timemap/link/www.fccn.pt` (throws `RuntimeException` on `MalformedURLException`, wrapping message "Error generating URL to a timemap of a page verify if you have passed correct test url parameter").
2. Open a stream to that URL and read all bytes into a byte array (throws `RuntimeException` "Error downloading the timemap" on `IOException`).
3. Convert bytes to a `String` and split into lines on `line.separator`.
4. For each line, split on `;` and, based on which `rel="..."` substring the line contains, run a set of `assertThat`/`containsString` checks against the individual `;`-separated fields (see Test Data above). Track booleans `existSelf`, `existTimegate`, `existOriginal`, `existMemento`.
5. If a line matches none of `rel="self"|"timegate"|"original"|"memento"`, immediately throw `IllegalStateException("Missing element(s) \"rel=*\"")`.

**Expected results:**
- Each relevant line passes its per-field `containsString` assertions (hard, via `assertThat`, e.g. "Check url self", "Check rel self", "Check type self", "Check from self", "Check url timegate", "Check rel timegate", "Check url original", "Check rel original", "Check if exists fccn.pt on url memento", "Check first rel memento", "Check first datetime memento", "Check first url memento").
- `assertEquals("There is no parameter Self", existSelf, true)` (hard).
- `assertEquals("There is no parameter Timegate", existTimegate, true)` (hard).
- `assertEquals("There is no parameter Original", existOriginal, true)` (hard).
- `assertEquals("There is no parameter Memento", existMemento, true)` (hard).
- Any unrecognized `rel=` line causes a hard failure via thrown `IllegalStateException`.

## Tags / Annotations
- `@Retry`: no
- `@Ignore`: no (the abstract base class `AppendableErrorsBaseTest` itself is annotated `@Ignore`, which is standard JUnit practice to prevent the abstract class from being run directly; this concrete subclass is not ignored)
- Category tags: api, memento

## Notes
- Parses a raw link-format (RFC 8288-style) Timemap response by naive `;`-splitting rather than a proper link-header parser; fragile to reordering of fields within a line.
- All assertions in this test use `run`/plain JUnit `assertThat`/`assertEquals` directly (not `appendError`), so any failure is a hard failure that stops the test method at that point — the base class's soft-assertion mechanism (`appendError`) is not used here.
- Hardcoded expected memento timestamp `19961013145650mp_` and date `Sun, 13 Oct 1996 14:56:50 GMT` tie this test to a specific fixture/dataset (`www.fccn.pt` archived collection) that must exist in the target environment.
- `:80` is stripped from the memento URL before comparison, suggesting the archive sometimes includes an explicit default port that must be normalized away.
