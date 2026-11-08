#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
AMETHYST_DIR="${1:?Usage: patch-amethyst.sh /path/to/Amethyst-Android}"
JAVA_DIR="$AMETHYST_DIR/app_pojavlauncher/src/main/java/net/kdt/pojavlaunch"
MANIFEST="$AMETHYST_DIR/app_pojavlauncher/src/main/AndroidManifest.xml"
mkdir -p "$JAVA_DIR"
cp "$ROOT/amethyst-patches/RinaorcBridgeActivity.java" "$JAVA_DIR/RinaorcBridgeActivity.java"
python3 - "$MANIFEST" <<'PY'
from pathlib import Path
import sys
p = Path(sys.argv[1])
s = p.read_text()
activity = '''        <activity
            android:name=".RinaorcBridgeActivity"
            android:exported="true" />
'''
if 'android:name=".RinaorcBridgeActivity"' not in s:
    marker = '        <activity\n            android:name=".MainActivity"'
    if marker not in s:
        raise SystemExit("Could not find MainActivity declaration in Amethyst manifest")
    s = s.replace(marker, activity + marker, 1)
    p.write_text(s)
PY
python3 - "$AMETHYST_DIR/app_pojavlauncher/src/main/java/net/kdt/pojavlaunch/LauncherActivity.java" <<'PY'
from pathlib import Path
import sys
p = Path(sys.argv[1])
s = p.read_text()
needle = '''        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pojav_launcher);'''
replacement = '''        super.onCreate(savedInstanceState);
        String rinaorcVersion = getIntent().getStringExtra("net.rinaorc.engine.EXTRA_VERSION");
        if ("26.3-snapshot-1".equals(rinaorcVersion)) {
            LauncherProfiles.load();
            String selectedId = LauncherPreferences.DEFAULT_PREF.getString(
                LauncherPreferences.PREF_KEY_CURRENT_PROFILE, "");
            if (!LauncherProfiles.mainProfileJson.profiles.containsKey(selectedId)) {
                selectedId = LauncherProfiles.mainProfileJson.profiles.keySet().iterator().next();
                LauncherPreferences.DEFAULT_PREF.edit()
                    .putString(LauncherPreferences.PREF_KEY_CURRENT_PROFILE, selectedId).apply();
            }
            MinecraftProfile selectedProfile = LauncherProfiles.mainProfileJson.profiles.get(selectedId);
            if (selectedProfile != null) {
                selectedProfile.lastVersionId = rinaorcVersion;
                LauncherProfiles.write();
            }
        }
        setContentView(R.layout.activity_pojav_launcher);'''
if 'String rinaorcVersion = getIntent().getStringExtra("net.rinaorc.engine.EXTRA_VERSION")' not in s:
    if needle not in s:
        raise SystemExit("Could not find LauncherActivity onCreate insertion point")
    s = s.replace(needle, replacement, 1)
    p.write_text(s)
PY
echo "Rinaorc bridge and 26.3 preselection patched into Amethyst."
