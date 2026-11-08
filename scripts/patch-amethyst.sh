#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
AMETHYST_DIR="${1:?Usage: patch-amethyst.sh /path/to/Amethyst-Android}"
APP="$AMETHYST_DIR/app_pojavlauncher"
JAVA_DIR="$APP/src/main/java/net/kdt/pojavlaunch"
MANIFEST="$APP/src/main/AndroidManifest.xml"
mkdir -p "$JAVA_DIR"
cp "$ROOT/amethyst-patches/RinaorcHomeActivity.java" "$JAVA_DIR/RinaorcHomeActivity.java"

python3 - "$MANIFEST" <<'PY'
from pathlib import Path
import re, sys
p = Path(sys.argv[1])
s = p.read_text()
# Keep Amethyst's storage/bootstrap activity, but make the Rinaorc screen the app entry.
pattern = r'(\s*<activity\s+android:name="\.TestStorageActivity"[^>]*>).*?(</activity>)'
m = re.search(pattern, s, re.S)
if not m:
    raise SystemExit("Could not find TestStorageActivity in Amethyst manifest")
block = m.group(0)
block = re.sub(r'\s*<intent-filter>.*?</intent-filter>', '', block, flags=re.S)
s = s[:m.start()] + block + s[m.end():]
if 'android:name=".RinaorcHomeActivity"' not in s:
    activity = """        <activity
            android:name=".RinaorcHomeActivity"
            android:exported="true"
            android:label="Rinaorc Mobile">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
"""
    marker = '        <activity\n            android:name=".TestStorageActivity"'
    s = s.replace(marker, activity + marker, 1)
p.write_text(s)
PY

python3 - "$APP/build.gradle" <<'PY'
from pathlib import Path
import sys
p = Path(sys.argv[1])
s = p.read_text()
s = s.replace('resValue "string", "app_name", "Amethyst (Debug)"',
              'resValue "string", "app_name", "Rinaorc Mobile"')
s = s.replace('resValue "string", "app_short_name", "Amethyst (Debug)"',
              'resValue "string", "app_short_name", "Rinaorc Mobile"')
s = s.replace('resValue "string", "app_name", "Amethyst"',
              'resValue "string", "app_name", "Rinaorc Mobile"')
s = s.replace('resValue "string", "app_short_name", "Amethyst"',
              'resValue "string", "app_short_name", "Rinaorc Mobile"')
p.write_text(s)
PY

echo "Rinaorc home screen integrated into the Amethyst application."
