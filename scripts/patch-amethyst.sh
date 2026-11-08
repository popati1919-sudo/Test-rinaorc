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
echo "Rinaorc bridge patched into Amethyst."
