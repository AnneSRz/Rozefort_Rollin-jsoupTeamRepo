Tâche #3: test sur divers environnements

---

Résumé : 

1. modifier une github action pour exécuter plusieurs builds avec différentes configurations de la JVM
3. l'action exécute la compilation et les tests avec 5 flags différents de la JVM *
4. l'action génère des logs clairs qui documentent quels flags sont exécutés
5. la mesure de la couverture est automatisée et le taux de couverture est mesuré à chaque build avec un flag différent; cinq taux de couverture sont mesurés par l'action



---

# Intro
### Changements apportés à la Github action pour permettre l'exécution avec cinq flags

Tous les flags ont été ajoutés dans le build.yml (modifié) dans le tableau jvm_flags. 

Pour chaque JDK version (8,17,21) et chaque OS (windows, ubuntu, macOS), on exécute les flags listés dans jvm_flags dans un nouveau workflow pour chaque combinaison de paramètres. Comme il y a 3 JDK, 3 OS et 5 flags, il y a 3*3*5 = 45 builds à tester. 

La tâche Apply JVM Flags log la tâche en cours d'exécution.

La tâche Generate Coverage Report fait un rapport de couverture avec Jacoco

Le flag -XX:+PrintFlagsFinal sert à logger l'état final des flags et est ajouté à chaque build.


# Flags de pom.XML
Il y a déjà des flags qui sont fournis avec la configuration de certains plugins, listons-les pour éviter la redondance et prenons quelques instants pour expliquer leur utilité:

1. -Xmx1024m -XX:+PrintGCDetails  : <br>
    -Xmx1024m Définit la taille maximale du heap à 1 GB (1024 MB) et  -XX:+PrintGCDetails sert au logging du garbage collector.

2. -Xss640k: <br>
    Définit la taille du stack d'un thread comme étant 640KB.

3. -Xpkginfo:always: <br>
    Génère un fichier package-info.class selon les annotations définies dans package-info.java.


# Flag 1 : Compressed object pointers & Memory management
**"-XX:+UseCompressedOops"** permet de diminuer la taille des pointeurs de taille par défaut de 64 bits à des pointeurs de taille 32bits pour sauver de l'espace mémoire dans le heap. Permet aussi de stocker davantage de données dans le cache du CPU. Bénéfices en vitesse et en mémoire.

Fonctionne avec un système d'adresses mémoire relatifs au début du heap plutôt qu'à la position absolue de l'adresse. Tellement utile que souvent activé par défaut dans les JVM modernes.

JSoup manipule et stocke des documents HTML qui peuvent avoir une taille significative et un impact sur la mémoire. Le parsing du document génère beaucoup d'objets ayant des types associés aux éléments HTML. Leurs instances a un coût en mémoire et réduire leur impact semble une optimisation intéressante.

# Flag 2 : Gestion accrue des erreurs et de débogage
"-XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log"

**-XX:+HeapDumpOnOutOfMemoryError** est un flag de gestion des erreurs (comme celle liés à la mémoire) et de débogage dans la JVM. Cela ce produit lorsqu'une erreur de type OutOfMemoryError est rencontrée. JVM crée un Heap Dump qui est un fichier qui représente la mémoire pour détaillé le quand, où et comment de l'erreur. C'est assez utile pour analyser l'état de la mémoire, la diagnostiquer par exemple en cas de fuites oupsie daisy ou de consommation excessive. De plus, c'est aussi possible d'identifier les objets qui sont en cause de l'épuisement de la mémoire, dans le cas par exemple où il y a des fichiers HTML trop volumineux qui conduise à une utilisation excessive comme dans le cas de JSoup qui manipule des grands fichiers HTML.
-XX:+HeapDumpOnOutOfMemoryError

# Flag 3 : Gestion du Garbage dans la mémoire
But et pouquoi : Supprimer les objects inutiles et non utilisés de la mémoire.
C'est avantageux,car cela nous permet de ne pas gérer manuellement la mémorie allouée ou encore les fuites de mémoire.

On utilise **UseG1GC**, car ce flag est conçu pour les applications avec une grande quantité de mémoire. Dans le cas où ces applications nécessitent des pauses de garbage collection, G1 réduit le temps des longues pauses. Il reste pertinent pour la librairie jsoup, car si on a une application qui utilise jsoup dans un environnement de production (par exemple, des pages web avec beaucoup de trafic, telles que les moteurs de recherche comme Google, Wikipédia, etc.) pour des outils de parsing ou d'extraction de données, cela diminue le temps de latence.

**PrintGCDetails** permet d'afficher des informations détaillées sur le processus de collection des garbage. Cela peut être utile pour déboguer et surveiller les performances. Dans une librairie comme jsoup, ce serait pertinent de l'utiliser dans un environnement de test lorsqu'il y a des problèmes de mémoire ou d'optimisation.

**PrintGCDateStamps** fournit un timestamp, ce qui est pertinent pour analyser les performances de la JVM ou diagnostiquer des problèmes de latence dans les applications qui utilisent jsoup.

Les deux derniers sont utiles dans un environnement de développement ou de test. Puisque la tâche 3 concerne l'automatisation des tests sur divers environnements, cela semble quand même pertinent.

**Xloggc:gc.log** est un logger qui enregistre les informations du garbage collector dans un fichier gc.log. Cela permet d'offrir une timeline des événements du garbage collector, ce qui peut être utile pour résoudre des problèmes.

# Flag 4 : HTTP Connection Optimization flags 
**-Dhttp.keepAlive=true** Cela permet à une connexion HTTP de rester ouverte et d'être réutilisée pour plusieurs requêtes vers le même serveur. Cela évite de créer une nouvelle connexion pour chaque requête pour diminuer l'effet de la latence.

**-Dsun.net.http.errorstream.enableBuffering=true** Ajoute un tampon qui permet la lecture des responses HTTP en chunks plutôt que byte par byte. Accélère supposément le traitement des erreurs 404.


Ces stratégies diminuent la latence / overhead qui vient avec l'établissement d'une connexion et les I/O operations associées au traitement d'un stream de données.   

# Flag 5 : Optimisation 
**-XX:+AggressiveOpts** active certaines fonctionnalités pour améliorer les performances de l'application. Puisque jsoup manipule des fichiers HTML, ce flag peut aider à améliorer les performances et accélérer les opérations en activant certaines fonctionnalités d'optimisation de la JVM qui, par défaut, ne sont pas activées. Par exemple, dans le parsing, cela serait pertinent pour optimiser l'exécution des boucles ou des structures de données. Comme jsoup peut être utilisé dans des environnements multithreads, notamment lorsqu'il est intégré dans des applications web traitant de nombreuses requêtes, ce flag peut améliorer la gestion du traitement multithread. En gros, il y a des avantages à utiliser ce flag pour améliorer la vitesse d'exécution. La qualité du logiciel peut également être améliorée, car cela permet de rendre l'application plus rapide.


# Critère de la tâche 3 :

flags	l'action exécute la compilation et les tests avec 5 flags différents de la JVM *
structure	l'action génère des logs clairs qui documentent quels flags sont exécutés
documentation	le repo inclut une page qui documente les changements apportés à la Github action pour permettre l'exécution avec cinq flags
motivation	la documentation inclut une section qui justifie le choix de chaque flag vis-à-vis de son impact possible sur la qualité, la performance, l'observabilité
qualité	la mesure de la couverture est automatisée et le taux de couverture est mesuré à chaque build avec un flag différent; cinq taux de couverture sont mesurés par l'action
humour	le repo inclut un élément d'humour responsable et documenté
                                                                                        
                                                                                                                                                                               
                   *#####*=                                        =+######*+ ###*               
           *#**######################+##+            ++++++##########################            
        *#################################*         ################################*#*          
      +##########=           =############            *############=           *#####*           
        ####*                 ***                        ****************         *####          
         *           ##################                 #+++++++++++++++*#           +*          
                  *################+  ###             ###= +#################+                   
               +#*+---#####+*#+-+#*+# *##*            ##  ++##*--######+*+---+##                 
              *+------########+--+#*   #*             *#*  +#+---########+-----=**               
             #**#*++++########=---=+=  #               +*  *=====*#######*****#* +               
                  +##################                      ==================                    
                                                                                         
                                                                                  *#             
                                                                                 ##              
                                                                                ##               
                                                                               ##                
                                                                              ##*                
                                                                     *##     ##+                 
                                                                   *##*     ###                  
                                                                #####=      ##                   
                                                         ==*#######        *##                   
                         *##*********************############*+            ##                    
                         *###########################*+=                  =#*                    
                          *#*                                             ##+                    
                                                                          #*                     
                                                                          **                     
                                                                                                 
                                                                                       