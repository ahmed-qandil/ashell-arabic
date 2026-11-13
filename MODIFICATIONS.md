# Modifications vs upstream aShell v0.31 (sunilpaulmathew/aShell, GPLv3)

1. Full Arabic localization: `app/src/main/res/values-ar/strings.xml` now translates every translatable string (upstream Arabic covered only ~58 of ~150). App name stays English "aShell" (upstream marks it translatable=false).
2. Hardcoded English UI strings moved to string resources and translated: "Copied to clipboard" toast/clip label (Utils), "Bookmark command" and bookmark-state TalkBack labels (aShellFragment), "Expand"/"Collapse" TalkBack labels (WelcomeAdapter). New string keys: cd_bookmark_add, cd_bookmark_remove, cd_expand, cd_collapse.
3. Accessibility: contentDescription added to the icon-only settings button (activity_ashell). Other decorative icons were already marked not-important upstream; remaining controls are text buttons.
4. Settings trimmed (user rule: remove About/developer/links/useless settings): removed the app-version/copyright row, "Learn Shizuku" link, "Translations" POEditor link, "Privacy policy" entry, "Contact developer" mailto, and the now-empty "Miscellaneous" header. Remaining settings: theme, amoled toggle, language, extras, examples.
5. No update checker exists upstream - nothing update-related to strip.
6. No default of any user-toggleable setting changed (standing rule).
7. Version set to v0.31-ar1 (versionCode 32). Same package name: uninstall the original aShell before installing this one.
8. Removed after user review ("completely clean"): the version/copyright settings row, the GitLab/POEditor contribution sentence in the welcome disclaimer, and the "Learn Shizuku" link button in the Shizuku-not-found dialog (the explanatory message stays).

Original LICENSE (GPLv3) applies; source of this modified version is published in this repository as required.
