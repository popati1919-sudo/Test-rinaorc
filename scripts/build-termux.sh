#!/data/data/com.termux/files/usr/bin/bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

echo "== Rinaorc Mobile / Android apps =="
command -v java >/dev/null || { echo "JDK 17 requis : pkg install openjdk-17"; exit 1; }
command -v curl >/dev/null || { echo "curl requis : pkg install curl"; exit 1; }
command -v unzip >/dev/null || { echo "unzip requis : pkg install unzip"; exit 1; }
java -version

GRADLE_VERSION="8.11.1"
GRADLE_HOME="$HOME/.local/gradle-$GRADLE_VERSION"
GRADLE_BIN="$GRADLE_HOME/bin/gradle"
if [ ! -x "$GRADLE_BIN" ]; then
  mkdir -p "$HOME/.local" "$HOME/.cache/rinaorc"
  ARCHIVE="$HOME/.cache/rinaorc/gradle-$GRADLE_VERSION-bin.zip"
  echo "Téléchargement de Gradle $GRADLE_VERSION..."
  curl -fL "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$ARCHIVE"
  unzip -q -o "$ARCHIVE" -d "$HOME/.local"
fi

if [ -z "${ANDROID_HOME:-}" ] && [ -z "${ANDROID_SDK_ROOT:-}" ]; then
  echo "ANDROID_HOME/ANDROID_SDK_ROOT absent. Configure ton SDK Android avant la compilation."
  exit 1
fi

echo "Compilation des trois APK Rinaorc..."
"$GRADLE_BIN" --no-daemon assembleDebug

echo
echo "APK générés :"
find apps -path '*/build/outputs/apk/debug/*.apk' -type f -print
echo
echo "Note : Amethyst et ses bibliothèques natives sont compilés séparément dans GitHub Actions."
