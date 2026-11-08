# Intégration moteur Amethyst

## Source vérifiée
- Dépôt : https://github.com/AngelAuraMC/Amethyst-Android
- Branche : `v3_openjdk`
- Module Android : `:app_pojavlauncher`
- Application Release : `org.angelauramc.amethyst`
- Application Debug : `org.angelauramc.amethyst.debug`
- Commande upstream documentée : `./gradlew :app_pojavlauncher:assembleDebug`

Le README upstream annonce les versions Minecraft jusqu'à `26.3-snapshot-1`. Il liste OpenJDK mobile 8, 17 et 21 ; il ne prouve pas à lui seul que le runtime requis pour cette snapshot est disponible dans chaque build.

## Pont Rinaorc
Le workflow GitHub clone Amethyst depuis la branche `v3_openjdk`, ajoute `RinaorcBridgeActivity`, puis compile un APK Amethyst debug dédié. Le pont accepte uniquement `26.3-snapshot-1` et transmet cette version à l'activité de jeu Amethyst via sa constante interne `MainActivity.INTENT_MINECRAFT_VERSION` (valeur `intent_version`).

Le bouton « Lancer Minecraft 26.3 » de Rinaorc Engine ouvre ce pont. Conditions préalables :
1. Installer l'APK Amethyst compilé par le workflow Rinaorc (pas un build upstream sans pont).
2. Ouvrir Amethyst une première fois et terminer sa configuration, notamment le profil/compte.
3. Installer/télécharger `26.3-snapshot-1` dans Amethyst au moins une fois.
4. Revenir dans Rinaorc Engine et appuyer sur « Lancer Minecraft 26.3 ».

Si la version n'est pas installée, le pont ouvre Amethyst pour permettre sa préparation. Le téléchargement initial de la version n'est pas encore automatisé par Rinaorc.

## Validation requise
Le dépôt upstream annonce la version cible, mais la compatibilité effective du runtime Java et le lancement sur Galaxy S21/Android 15 doivent être testés sur appareil. Ne pas annoncer le jeu comme opérationnel avant ce test et une connexion réelle au serveur Rinaorc.
