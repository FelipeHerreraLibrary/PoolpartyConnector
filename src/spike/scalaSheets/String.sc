
case class Value(authority: String)

val value = Value("9f55bb73-f60e-49bc-83f7-20bc87b452c6")

if (value.authority.contains("say/")) {

  println("yes")

}

"http://thesaurus.iadb.org/publicthesauri/IdBCountries" == "http://thesaurus.iadb.org/publicthesauri/IdBCountries"