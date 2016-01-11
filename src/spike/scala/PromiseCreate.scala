import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by Daniel Maatari Okouya on 10/14/15.
 */
object PromiseCreate extends App {
  val p = Promise[ String]
  val q = Promise[ String]
  p.future foreach { case x => println( s" p succeeded with '$x'") }
  Thread.sleep( 1000)
  p success "assigned"
  q failure new Exception(" not kept")
  Thread.sleep( 1000)
  q.future.failed foreach { case t => println( s" q failed with $t") }
  Thread.sleep( 1000)

}