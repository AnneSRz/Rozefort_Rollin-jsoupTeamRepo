## Équipes
Emanuel Rollin 20106951
Anne Sophie Rozefort 20189221

### Tests #1 et #2 classe 'HtmlTreeBuilderTest'

Testés via HtmlTreeBuilderTest1.java.

1. test 'casePlaintextTest'


On cherche à tester la ligne 125 de initialiseParseFragment
...
->Les classes TreeBuilder et StreamParser appellent HTMLTreeBuilder.parseFragment() (utilise initialiseParseFragment)
->Parser.parseFragmentInput,parseFragment,parseXmlFragment appellent TreeBuilder.parseFragment()
->Parser.parseBodyFragment appelle Parser.parseFragment
->Jsoup.parseBodyFragment appelle Parser.parseBodyFragment()
...
Donc appeler Jsoup.parseBodyFragment de la bonne façon peut déclencher le cas "plaintext"



2. test 'closeElement'

...
->HTMLTreeBuilderState.inBodyStartTag() appelle HTMLTreeBuilder.closeElement dans case 'form'
->le state InBody de HTMLTreeBuilderState.process() appelle inBodyStartTag()
->
...

Il faut un code HTML contenant un body tag, un form tag et un p tag non fermé pour déclencher closeElement("p")

### Tests #3 de la classe 'HtmlTreeBuilderStateTest1'

3. test 'inBodyStartTag'

...
->HTMLTreeBuilderState.Inbody declenche le case 'nobr'
->HTMLTreeBuilder.resetInsertionMode() et .resetBody() et .resetInsertionMode()
->Sont des méthodes employées dépendemment du HTMLTreeBuilderState
...

Il faut un code HTML contenant un body tag ouvert, un nobr tag ouvert pour declencher le case 'nobr' qui s'occupe de reparer le formatage



### Tests #4 et 5 de la classe 'HtmlTreeBuilderStateTest1'

Liste des test "inColumnGroup"

Augmente le coverage entre les lignes 1175 et 1232. S'intéresse aux edge cases suivants qui ne respectent pas la syntaxe HTML, mais qui sont résolus par JSoup sans commettre d'erreurs:


- inColumnGroup1 : tag table manquant autour d'un colgroup
- inColumnGroup2 : tag colgroups nestés
- inColumnGroup3 : tag doctype dans un colgroup
- inColumnGroup4 : tag html dans un colgroup
- inColumnGroup5 : tag template dans un colgroup


Devraient augmenter le coverage sur 4 cases de l'état InColumnGroup de HtmlTreeBuilderState. Ces edges cases ne surviennet que lorsque du code html mal formatté est parsé.


### Tests #6 et #7 de la classe 'TokeniserStateTest1'
6. ScriptDataDoubleEscapedDash

7. ScriptDataDoubleEscapedDashDash

### Tests #8 et #9 de la classe 'HttpConnectionTest'
8.  Connection data(Map<String, String> data)
Vérifie la méhode pour s'assurer que les données sont accesseible via la requete request.data après avoir été stockée dans l'objet connection. On regarde en même temps qu'elles ont bien été sauvegardées. 
On commence par initialiser une instance MAP et établir une connexion HTTP. 
On appelle ensuite la méthode data avec les valeurs insérées dans l'instance du MAP.On vérifie la récupération des données pour s'assurer qu'une paire de données est bien renvoyée. 
9. Map<String, String> headers() 



