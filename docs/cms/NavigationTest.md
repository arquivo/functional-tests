# NavigationTest

- **Source:** `src/test/java/pt/arquivo/tests/cms/tests/NavigationTest.java`
- **Category:** cms > tests
- **Base class:** `WebDriverTestBaseParallel`
- **Test type:** Browser (Selenium, cross-browser/device matrix)
- **Suite membership:** cms.suite.TestSuite (yes)

## Purpose / Scenario
An end-to-end smoke test that navigates through the main "Publications" menu tree of the Arquivo.pt about site (News, Publications, Reports, News-on-Media, Audio, Presentations, Collaborate, About) via hover/click menu interactions, checking that the outbound links on each destination page are reachable — repeated once in Portuguese and once in English.

## Preconditions
- Target env: `test.url`.
- Page objects used (all reached from `IndexSobrePage`): `NewsPage` (`goToNewsPage()`), `PublicationsPage` (`goToPublicationsPage(lang)`, page object is fetched but not asserted on), `ReportsPage` (`goToReportsPage(lang)`), `NewsOnMediaPage` (`goToNewOnMediaPage(lang)`), `AudioPage` (`goToAudioPage(lang)`), `PresentationsPage` (`goToPresentationsPage(lang)`), `CollaboratePage` (`goToCollaboratePage(lang)`), `AboutPage` (`goToAboutPage(lang)`). `VideoPage`/`goToVideoPage` is imported/available but commented out ("TODO to slow").
- No fixture files loaded.
- No notable hardcoded expected text; all checks are link-reachability based (HTTP 200) via each page object's respective `check*Links(language)` method.

## Test Data
- Navigation uses hardcoded WordPress menu item ids: `menu-item-4388` News, `menu-item-4401`/`4428` Publications PT/EN, `menu-item-4402`/`19137` Reports PT/EN, `menu-item-4403`/`4430` News-on-Media PT/EN, `menu-item-4404`/`4431` Audio PT/EN, `menu-item-4406`/`4433` Presentations PT/EN. Menu items are reached with `IndexSobrePage.clickMenuItem`/`hoverMenuItem`, which hover each ancestor menu item from the top menu down (the sub-menus open on hover) and click the item by its WordPress id `menu-item-<id>`.
- Collaborate and About have no page of their own: `goToCollaboratePage`/`goToAboutPage` only hover the top menu items `menu-item-11376`/`11510` (Colabore, PT/EN) and `menu-item-11380`/`11519` (Acerca, PT/EN), and their check methods collect the links of the sub-menu (`//*[@id="menu-item-<id>"]/ul/li/a`).
- Each page object's check method requires all extracted links to return HTTP 200.
- Note: `NewsOnMediaPage.checkNewsLinks`, `PresentationsPage.checkPresentationLinks`, and `CollaboratePage.checkCollaborateLinks` currently have their actual `AnalyzeURLs.checkOk(...)` failure branch commented out — they always return `true` (log-only), so those three sub-checks are effectively no-ops today.

## Steps & Expected Results

### `navigation()`
Portuguese pass:
1. Construct `IndexSobrePage`.
2. `index.goToNewsPage()` then assert `news.checkNewsLinks("PT")`.
3. `index.goToPublicationsPage("PT")` (result unused/unchecked).
4. `index.goToReportsPage("PT")` then assert `reports.checkReportsLinks("PT")`.
5. `index.goToNewOnMediaPage("PT")` then assert `newsonmedia.checkNewsLinks("PT")` (effectively always true — see Notes).
6. `index.goToAudioPage("PT")` then assert `audio.checkAudioLinks("PT")`.
7. (Video page navigation is commented out / skipped — "too slow".)
8. `index.goToPresentationsPage("PT")` then assert `pres.checkPresentationLinks("PT")` (effectively always true).
9. `index.goToCollaboratePage("PT")` then assert `coll.checkCollaborateLinks("PT")` (effectively always true).
10. `index.goToAboutPage("PT")` then assert `about.checkAboutLinks("PT")`.

English pass (repeats the same navigation sequence with `"EN"`):
11. `index.goToNewsPage()` again, assert `newsEN.checkNewsLinks("EN")`.
12. `index.goToPublicationsPage("EN")` (unused).
13. `index.goToReportsPage("EN")`, assert `reportsEN.checkReportsLinks("EN")`.
14. `index.goToNewOnMediaPage("EN")`, assert `newsonmediaEN.checkNewsLinks("EN")`.
15. `index.goToAudioPage("EN")`, assert `audioEN.checkAudioLinks("EN")`.
16. `index.goToPresentationsPage("EN")`, assert `presEN.checkPresentationLinks("EN")`.
17. `index.goToCollaboratePage("EN")`, assert `collEN.checkCollaborateLinks("EN")`.
18. `index.goToAboutPage("EN")`, assert `aboutEN.checkAboutLinks("EN")`.

**Expected results:**
- All eight `assertTrue(...)` calls per language pass (hard) use the message prefix `"[Nabigation Test] Failed The <X> Page Test in Portuguese"` (note: message literally says "Portuguese" even in the English-pass assertions — a copy-paste artifact in the source, not a real localization bug).
- If any step throws `IOException` (e.g. page navigation failures), the whole test hard-fails with `fail("IOException -> navigation")`.

## Tags / Annotations
- `@Retry`: yes
- `@Ignore`: no
- Runs across configured browser/device matrix
- Category tags: cms, navigation

## Notes
- **Video page is explicitly skipped** in both PT and EN passes with a `TODO to slow` comment — `VideoPage`/`checkVideoLinks` is never exercised by this test despite being imported.
- **All assertion messages read "Failed The X Page Test in Portuguese"** even in the English-language block — a copy/paste artifact, not a functional bug (the actual language argument passed is correct).
- Several link-check methods (`NewsOnMediaPage`, `PresentationsPage`, `CollaboratePage`) have their failure-return code path commented out, so those steps currently always pass regardless of actual link health — only log to stdout. A native reimplementation should decide whether to restore real assertions or intentionally keep these as smoke/log-only checks.
- `PublicationsPage` returned by `goToPublicationsPage()` is fetched but never asserted on in this test (separately covered by `PublicationsTest`).
