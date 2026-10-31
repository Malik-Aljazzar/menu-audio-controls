# Menu Audio Controls – Projekt-Einrichtung (CurseForge + GitHub)

Checkliste, um das Mod zu veröffentlichen und ins Pack zu bringen. Texte in Codeblöcken sind zum
Kopieren gedacht (öffentlich, daher Englisch).

## Eckdaten

| Feld | Wert |
|---|---|
| Name | `Menu Audio Controls` |
| Mod-ID | `menu_audio_controls` |
| Version | `1.0.0` |
| Minecraft / Loader | 1.21.1 / NeoForge (ab 21.1.264) |
| Seite | **nur Client** |
| Abhängigkeit | FancyMenu ≥ 3.9.14 (Pflicht); Konkrete + Melody zieht FancyMenu selbst nach |
| Lizenz | MIT |
| Jar | `build/libs/menu_audio_controls-1.0.0.jar` |
| Logo | `icon.png` (512×512 Pixel-Art: Noten, Play-Button, Spulleiste; Generator `modpack_pipeline/menu/gen_addon_icon.py`) |
| GitHub | `Malik-Aljazzar/menu-audio-controls` (steht schon in `gradle.properties`) |

## 1. CurseForge-Projekt anlegen

curseforge.com → *Create a Project* → **Minecraft** → **Mods**

| Formularfeld | Eingabe |
|---|---|
| Project name | `Menu Audio Controls` |
| Summary | siehe unten |
| Description | Inhalt von `curseforge-description.md` einfügen (Markdown) |
| Logo / Avatar | `icon.png` |
| Main category | **Cosmetic** |
| Additional categories | **Utility & QoL**, **Miscellaneous** |
| License | **MIT License** |
| Source URL | `https://github.com/Malik-Aljazzar/menu-audio-controls` |
| Issues URL | `https://github.com/Malik-Aljazzar/menu-audio-controls/issues` |

Summary:
```
FancyMenu addon: a seek bar for Audio element music players, plus "seek" and "start track at (once per game start)" actions.
```

Einstellungen nach dem Anlegen:
- **Distribution / „Allow third-party distribution“: an lassen.** Sonst können Launcher außerhalb der
  CurseForge-App (Prism, ATLauncher …) das Mod nicht aus dem Pack-Manifest laden.
- Die **Projekt-ID** steht rechts unter „About Project“. Trag sie in `gradle.properties` ein:
  ```
  curseforge_project_id=<ID>
  ```
  Solange sie leer ist, bricht `release.yml` absichtlich ab.

CurseForge prüft neue Projekte und auch die erste Datei manuell. Das kann Stunden bis Tage dauern.

## 2. GitHub-Repo + Secret

Im Ordner `mod_dev/zeta-menu-audio-1.21.1`. Git ist schon initialisiert, alles ist gestaget, aber
noch nichts committet.

```powershell
git commit -m "Menu Audio Controls 1.0.0"
gh repo create Malik-Aljazzar/menu-audio-controls --public --source . --remote origin --push
# CurseForge-API-Token (derselbe wie für die Modpack-Uploads), getrimmt, damit kein Zeilenumbruch mitkommt:
gh secret set CURSEFORGE_API_KEY --repo Malik-Aljazzar/menu-audio-controls --body (Get-Content $HOME\.zeta\curseforge_token -Raw).Trim()
```

Danach die Projekt-ID aus Schritt 1 committen:
```powershell
git commit -am "CurseForge project id"
git push
```

## 3. Veröffentlichen

Wie bei deinen anderen Mods: ein Tag `v<mod_version>` startet `.github/workflows/release.yml`.
```powershell
git tag v1.0.0
git push origin v1.0.0
```
Der Workflow:
1. prüft, dass Tag, `mod_version`, CHANGELOG-Abschnitt `## [1.0.0]`, Projekt-ID und Secret passen,
2. baut das Mod und führt die Unit-Tests aus,
3. lädt das Jar mit dem CHANGELOG-Abschnitt als Release Notes auf CurseForge hoch
   (NeoForge, 1.21.1, Client, „requires FancyMenu“) und legt ein GitHub-Release an.

Vorher lokal testen (ohne Token ist das immer ein Trockenlauf, die Ausgabe landet in `build/publishMods`):
```powershell
.\gradlew.bat build publishMods
```

## 4. Ins Pack übernehmen

1. Warten, bis CurseForge Projekt und Datei freigegeben hat.
2. **Lokale Test-Kopie löschen:** `mods/menu_audio_controls-1.0.0.jar` in der Instanz (sonst ist das Mod doppelt drin).
3. Das Mod über die CurseForge-App in der Instanz installieren.
4. Beim nächsten Modpack-Release meldet `analyze` das neue Mod: Seite **client**.
   ```
   python -m zeta_pack decide side menu_audio_controls client --reason "FancyMenu menu addon (seek bar, menu audio actions), client-only (@Mod dist=CLIENT)"
   ```
5. **Vorher Kalamitys Zusage abwarten.** Die Musik und das Widget gehen mit demselben Release raus.

## Spätere Versionen

1. `mod_version` in `gradle.properties` erhöhen.
2. Abschnitt `## [<version>]` in `CHANGELOG.md` anlegen (Englisch).
3. Commit, dann `git tag v<version>` und `git push origin v<version>`.

Bei einem FancyMenu-Update vorher `specification/SPEC.md` („FancyMenu internals relied on“) gegen die
neue Version prüfen. Ändern sich die Interna, schaltet sich das Mod ab und schreibt das ins Log;
Abstürze gibt es dadurch nicht.
