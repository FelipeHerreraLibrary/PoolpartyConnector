import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

val value = for (j <- Future {2} ) yield (j)

value.value