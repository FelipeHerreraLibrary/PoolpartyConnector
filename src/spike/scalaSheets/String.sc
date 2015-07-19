import org.iadb.poolpartyconnector.dspacextension.dspacedataoperationadaptation.authoritykeyoperation.AuthorityKeyUtils
import org.iadb.poolpartyconnector.utils.ConceptUriChecker

case class Value(authority: String)

val value = Value("9f55bb73-f60e-49bc-83f7-20bc87b452c6")

if (value.authority.contains("say/")) {

  println("yes")

}

"http://thesaurus.iadb.org/publicthesauri/IdBCountries" == "http://thesaurus.iadb.org/publicthesauri/IdBCountries"

val str = "http://xxx:http://xxx2"

str.indexOf(':', 5)

str.splitAt(str.indexOf(':', 5))

AuthorityKeyUtils.splitAuthrotiyandKey(str)


ConceptUriChecker.isValidConceptUri("")

//ConceptUriChecker.isValidConceptUri("http://thesaurus.iadb.org")

def langfallbackifrequired(lang: String): String = {

  lang match {
    case "en" => "en"
    case "es" => "es"
    case "fr" => "fr"
    case "pt" => "pt"
    case _ => "en"
  }
}

langfallbackifrequired("*")