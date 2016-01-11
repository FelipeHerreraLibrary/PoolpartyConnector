
//Linearization

trait Readonly {

  val num: Int

  def thenum = num

}


case class Reader (val num: Int) extends Readonly

Reader(2).thenum

trait Writer

new Reader(5) with Writer


trait MsgWriter {

  def write: String = {
    "MsgWriter"
  }
}

trait MsgWriterModifier extends MsgWriter {

  override def write: String = {
    "MsgWriterModifier"
  }

}

trait MsgWriterModifier2 extends MsgWriter {

  override def write: String = {
    "MsgWriterModifier2"
  }

}
object MyWriter extends MsgWriter  with MsgWriterModifier2 with MsgWriterModifier

MyWriter.write


//Mixin

trait BComponent {

  def foo: String
}

trait BComponentImpl extends BComponent {
  override def foo: String = "foo"
}

class A {
  self: BComponent =>
  def fooable = "foobale" + foo
}

new A with BComponentImpl

