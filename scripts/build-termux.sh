#!/data/data/com.termux/files/usr/bin/bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
echo "== Rinaorc Engine build =="
command -v java >/dev/null || { echo "JDK 17 requis : pkg install openjdk-17"; exit 1; }
command -v gradle >/dev/null || { echo "Gradle requis : pkg install gradle"; exit 1; }
java -version
echo "Vérification Android SDK..."
if [ -z "${ANDROID_HOME:-}" ] && [ -z "${ANDROID_SDK_ROOT:-}" ]; then
  echo "ANDROID_HOME/ANDROID_SDK_ROOT absent. Configure ton SDK Android avant la compilation."
  exit 1
fi
gradle --no-daemon :apps:engine:assembleDebug
echo "APK : apps/engine/build/outputs/apk/debug/engine-debug.apk"
