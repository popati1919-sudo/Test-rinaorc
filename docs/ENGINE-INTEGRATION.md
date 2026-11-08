# Intégration moteur Amethyst

## Source vérifiée
- Dépôt : https://github.com/AngelAuraMC/Amethyst-Android
- Branche : `v3_openjdk`
- Module Android : `:app_pojavlauncher`
- Application Release : `org.angelauramc.amethyst`
- Application Debug : `org.angelauramc.amethyst.debug`
- Commande upstream documentée : `./gradlew :app_pojavlauncher:assembleDebug`

Le README upstream annonce les versions Minecraft jusqu'à `26.3-snapshot-1`. Il liste OpenJDK mobile 8, 17 et 21 ; il ne prouve pas à lui seul que le runtime requis pour cette snapshot est disponible dans chaque build.

## Pourquoi le menu ne force pas encore la version
Le dépôt upstream ne documente pas d'API publique stable permettant à une autre application Android de choisir et lancer une version Minecraft. Le module Engine actuel ouvre l'activité Amethyst installée. Il ne transmet pas de faux extras Intent, ne lit pas les fichiers privés d'Amethyst et ne prétend pas lancer le jeu automatiquement.

## Prochaine intégration nécessaire
1. Cloner Amethyst avec ses sous-modules et conserver ses licences.
2. Examiner les classes de sélection de version et de démarrage dans `app_pojavlauncher`.
3. Ajouter une interface interne ou une activité explicite prise en charge pour sélectionner `26.3-snapshot-1`.
4. Vérifier le runtime Java requis pour cette version et l'ABI `arm64-v8a`.
5. Tester l'installation, le téléchargement de la version, l'écran titre et la connexion au serveur Rinaorc sur appareil réel.

Ne pas marquer l'intégration terminée avant ces tests.
