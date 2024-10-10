Emanuel Rollin 20106951
Anne Sophie Rozefort 



# Tests de la classe 'HtmlTreeBuilderTest'

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

# Tests de la classe ''