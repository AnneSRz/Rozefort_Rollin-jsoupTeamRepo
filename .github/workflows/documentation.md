Tâche #3: test sur divers environnements
<code>
critère	 description
flags	  l'action exécute la compilation et les tests avec 5 flags différents de la JVM *
structure	  l'action génère des logs clairs qui documentent quels flags sont exécutés
documentation le repo inclut une page qui documente les changements apportés à la Github action pour permettre l'exécution avec cinq flags
motivation  la documentation inclut une section qui justifie le choix de chaque flag vis-à-vis de son impact possible sur la qualité, la performance, l'observabilité
qualité	la mesure de la couverture est automatisée et le taux de couverture est mesuré à chaque build avec un flag différent; cinq taux de couverture sont mesurés par l'action
humour	  le repo inclut un élément d'humour responsable et documenté


!les flags doivent être de différents types (par exemple pas 2 flags de type print ou GC)
!Chaque critère compte pour un point.
!Si l'action ne s'exécute pas correctement, la note maximale pour cette tâche ne pourra pas dépasser 4/10.
!Bonus: les commits pour développer cette action sont documentés avec lolcommits.




Cette tâche a 2 objectifs principaux

modifier une github action pour exécuter plusieurs builds avec différentes configurations de la JVM
explorer les centaines d'options (flags) de la JVM
Pour lister les flags disponibles pour votre JVM :  java -XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsFinal -version
Pour utiliser un flag avec Maven (par exemple ici, ExtendedDTraceProbes) : export MAVEN_OPTS=" -XX:+ExtendedDTraceProbes"
Pour réinitialiser les flags avec Maven : unset MAVEN_OPTS



Rendu pour la tâche 3
Une fois la tâche accomplie, les étudiants font une 'pull request' sur ce répertoire avec un répertoire de la forme 'NOM1-NOM2/', qui inclut un fichier readme.md indiquant

le référentiel (repository) Github qui inclut la github action qui a été modifiée
un lien vers une page (par ex. readme.md) qui documente votre travail pour la tâche 3
La date limite pour cette 'pull request' est indiquée sur la page principale du cours.
</code>

---

Résumé : 

1. modifier une github action pour exécuter plusieurs builds avec différentes configurations de la JVM
3. l'action exécute la compilation et les tests avec 5 flags différents de la JVM *
4. l'action génère des logs clairs qui documentent quels flags sont exécutés
5. la mesure de la couverture est automatisée et le taux de couverture est mesuré à chaque build avec un flag différent; cinq taux de couverture sont mesurés par l'action

# Flag 1 : Catégorie Memory Management
"-Xms256m -Xmx1g"
-Xms256m sets the initial heap size to 256 MB.
-Xmx1g sets the maximum heap size to 1 GB. Adjust this based on the size of data JSoup is expected to process.

JSoup processes large HTML documents in memory, so managing heap size effectively can help avoid out-of-memory errors. Diminue le overhead associé à l'allocation dynamique de l'espace.

# Flag 2 : Compressed object pointers
Permet de diminuer la taille des pointeurs à 64 bits pour sauver de l'espace mémoire.
"-XX:+UseCompressedOops" 

# Flag 3 : Garbage Collection détaillés
But et pouquoi : Supprimer les objects inutiles et non utilisés de la mémoire.
C'est avantageux,car cela nous permet de ne pas gérer manuellement la mémorie allouée ou encore les fuites de mémoire.

On utilise UseG1GC, car ce flag est conçu pour les applications avec une quantité de mémoire vaste. Dans le cas où c'est applications nécéssitent des pauses de GarbageCollection,  G1 diminue le temps des longues pauses. Elle reste pertinante pour la libraire jSoup, car si on a une application qui utilise jsoup dans un environnement de production par exemple (pages webs avec beacoup de traffics tels que les moteurs de recherches comme Google, Wikipédia, etc.) pour des outils, parser ou encore extraire des données, ça diminue le temps de latence.

PrintGCDetails permet d'afficher des infos détaillés sur le processus de collection des Garbage. Ce qui peut être bien pour débugger et surveiller les performances. Dans un librairie comme jSoup, ce serait pertinent de l'utiliser dans un environnement de test lorsqu'il y a des problèemes de mémoire ou d'optimisation.
PrintGCDateStamps fournit un date stamp c'est pertinent pour analyser les performances de la JVM ou diagnostiquer des problèmes de latence dans les applications qui utilisent jSoup.

Les 2 derniers sont utiles dans un environnement de développement ou de test,puisque la tâche 3 concerne l'automatisation des tests sur divers environnements, cela semble quand même pertinent.
"-XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps"



#
