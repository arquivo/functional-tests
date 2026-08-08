# ImageSearchTest

- **Source:** `src/test/java/pt/arquivo/tests/webapp/imagesearch/ImageSearchTest.java`
- **Category:** webapp > imagesearch
- **Base class:** WebDriverTestBaseParallel
- **Test type:** Browser (Selenium, cross-browser/device matrix)

## Purpose / Scenario
Verifies the end-to-end image search and image-viewer flow: searching for a term scoped to a specific collection, checking the estimated result count, opening the first image in the viewer modal, checking all of the image/page metadata shown there, then opening the technical-details panel and verifying its content, and finally exercising the "copy API data" and modal-close controls.

## Preconditions
- Target env comes from the `test.url` system property; the collection filter (`collection:Roteiro`) is used specifically "to ensure every environment (dev, preprod, prod) retrieves the same data" (per code comment).
- No external config/resource files loaded.
- Depends on fixed historical crawl content for the domain `shiva.di.uminho.pt` (page "Jose Miranda - HOME PAGE") captured in October 1996.

## Test Data
- Search term: `Publico collection:Roteiro`
- Minimum expected estimated results: `>= 1.000` (third whitespace-separated token of the `#estimated-results` text, parsed as a `Double`)
- Image alt/description text: `PUBLICO`
- Image original link: `http://shiva.di.uminho.pt:80/~pinj/0/6/20.gif`
- Image capture date: `13 Outubro 15h00, 1996`
- Page title: `Jose Miranda - HOME PAGE`
- Page URL: `http://shiva.di.uminho.pt:80/~pinj/`
- Page capture date: `13 Outubro 14h59, 1996`
- Technical details expected to contain timestamp `19961013150044`, a Portuguese image caption starting "PÚBLICO ON-LINE é um projecto experimental...", and archive link `arquivo.pt/wayback/19961013145930/http://shiva.di.uminho.pt:80/~pinj/`

## Steps & Expected Results

### `imageSearchOneTermTest()`
1. Clear search box, type `Publico collection:Roteiro`, click search. (hard — via `run`)
2. Switch to image search results (`#search-form-images`). (hard — via `run`)
3. Read `#estimated-results` text, split on whitespace, parse the 3rd token as a `Double`.
4. Click the first image result (`#image-card-1`) to open the viewer. (hard — via `run`)
5. Check the modal (`#modal`) is displayed.
6. Read image alt/description span text.
7. Read image original link text.
8. Read image capture date text.
9. Read page title link text.
10. Read page URL text.
11. Read page capture date text.
12. Click the "Details" button (`#image-details-button`).
13. Read the technical-details panel text (three separate checks against the same element).
14. Click "copy API details" (`#copy-raw-api-data`). (hard — via `run`)
15. Close the modal (`//*[@id="close-modal-tecnhical"]/button`). (hard — via `run`)

**Expected results:**
- "Verify if the estimated results count message is displayed on image search is greater than 2.200" — parsed number `>= 1.000` (hard — plain `assertTrue`; note the assertion message says "greater than 2.200" but the code actually checks `>= 1.000`, a mismatch between message and logic).
- "First image details should be shown after clicking on it" — `#modal` is displayed (soft/appendError).
- "Check image alt text in image viewer" — description span text equals `PUBLICO` (soft/appendError).
- "Check image original link in image viewer" — text equals `http://shiva.di.uminho.pt:80/~pinj/0/6/20.gif` (soft/appendError).
- (Commented-out/disabled check for image type/size — dead code left in place, not executed.)
- "Check image capture date in image viewer" — text equals `13 Outubro 15h00, 1996` (soft/appendError).
- "Check page title in image viewer" — text equals `Jose Miranda - HOME PAGE` (soft/appendError).
- "Check page URL in image viewer" — text equals `http://shiva.di.uminho.pt:80/~pinj/` (soft/appendError).
- "Check page capture date in image viewer" — text equals `13 Outubro 14h59, 1996` (soft/appendError).
- Click on Details button wrapped in appendError, no explicit assertion (soft/appendError).
- "Check image detail page contains page timestamp" — technical-details text contains `19961013150044` (soft/appendError).
- "Check image detail page contains image caption" — technical-details text contains the Portuguese caption fragment (soft/appendError).
- "Check image detail page contains page link to archive" — technical-details text contains `arquivo.pt/wayback/19961013145930/http://shiva.di.uminho.pt:80/~pinj/` (soft/appendError).

## Tags / Annotations
- `@Retry`: yes (retried up to 6x, 30s backoff)
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: webapp, imagesearch, image-viewer, technical-details, collection-filter

## Notes
- Uses `collection:Roteiro` search-operator scoping specifically to make results deterministic/environment-independent — worth preserving this pattern in the Playwright port.
- Contains a commented-out/dead assertion block (image type/size check against `#card0`) that is not executed — flag as intentionally disabled, not an oversight to "fix".
- The estimated-results assertion message text ("greater than 2.200") does not match the actual threshold in code (`>= 1.000`) — a pre-existing inconsistency in the Java test, noted here for fidelity rather than corrected.
- Heavy reliance on brittle positional XPath (e.g. `section[3]/div[3]/p/span`) for modal internals — flag as selector-fragility risk for the rewrite.
- Depends on fixed historical crawl content that must exist in the target environment's index.
