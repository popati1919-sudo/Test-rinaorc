# Rinaorc Mobile

Projet Android ARM64 pour préparer le lancement de Minecraft Java Edition avec une interface Rinaorc.

## Version cible
- Minecraft Java : **26.3-snapshot-1** (version présélectionnée)
- ABI : `arm64-v8a`
- Appareil de validation visé : Samsung Galaxy S21 / Android 15
- Moteur amont : [Amethyst Android — branche v3_openjdk](https://github.com/AngelAuraMC/Amethyst-Android/tree/v3_openjdk)

## Applications prévues
- `apps/launcher` — profils, version Minecraft, compte Microsoft et serveur Rinaorc.
- `apps/engine` — menu moteur, réglages graphiques et lancement du moteur Amethyst.
- `apps/control` — réglages avancés et diagnostics.

## État réel de cette première base
Le module `apps/engine` est une application Android indépendante qui fournit un menu Rinaorc et un transfert vers Amethyst déjà installé. Elle **ne contient pas encore le code source Amethyst ni ses bibliothèques natives**. Le lancement d'Amethyst ne garantit donc pas à lui seul que le profil 26.3 est sélectionné ou lancé automatiquement. La validation du runtime Java et du lancement sur le téléphone reste obligatoire.

La branche Amethyst annonce la prise en charge des versions jusqu'à 26.3-snapshot-1, mais son README ne documente des ports OpenJDK mobiles que pour Java 8, 17 et 21. La compatibilité Java requise par cette snapshot doit être vérifiée avant de déclarer le jeu opérationnel.

## Compiler
La CI GitHub Actions utilise Gradle 8.11.1, JDK 17 et Android SDK 35 :

```bash
gradle :apps:engine:assembleDebug
```

APK attendu : `apps/engine/build/outputs/apk/debug/engine-debug.apk`.

## Compilation sur Termux
Le dépôt inclut un script `scripts/build-termux.sh` pour vérifier les prérequis et compiler. Le NDK officiel fourni avec les outils hôtes `linux-x86_64` ne peut pas être exécuté directement sur un téléphone ARM64 ; la compilation de l'application Android peut se faire avec Gradle/JDK si le SDK et les outils compatibles sont installés.

## Licence
Le code du menu Rinaorc est original. Amethyst est un projet séparé sous LGPL-3.0 ; conserver ses notices, licences et obligations si ses sources ou binaires sont redistribués.
