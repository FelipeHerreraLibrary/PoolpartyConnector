import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

val value = for (j <- Future {2} ) yield (j + 1)

value

value.value

Future {2}.flatMap(e => Future{e + 1})