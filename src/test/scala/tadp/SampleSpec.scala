package tadp

import collection.mutable.Stack
import org.scalatest._
import flatspec._
import matchers._

class SampleSpec extends AnyFlatSpec with should.Matchers {

  val simpleDrawing =
    """
      |grupo {
      |	triangulo {
      |		vertices 1 @ 2, 2 @ 4, 0 @ 0
      |		color 255 @ 0 @ 100 @ 255
      |    }
      |	cuadrado {
      |		vertices 0 @ 0, 2 @ 4
      |		color 255 @ 0 @ 100
      |    }
      |}
      |
      |""".stripMargin

  "simpleDrawing" should "parse" in {
    fail()
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a [NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}