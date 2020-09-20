package tadp

import collection.mutable.Stack
import org.scalatest._
import flatspec._
import matchers._
import tadp.ast._
import tadp.combinators.{Error, Success}
import tadp.parser._

class ParserSpec extends AnyFlatSpec with should.Matchers {

  def beSuccess[T] = (value : T) =>
    be(Success(value = value, leftToParse = ""))

  it should "parsear un string vacío en una imagen vacía" in {
    val vacio = ""

    imagenParser(vacio) should beSuccess(ImagenVacia)
  }

  it should "parsear 'triangulo' seguido de 3 vertices entre corchetes a una imagen que contiene un triangulo" in {
    val triangulo = "triangulo[0 @ 1, 1 @ 2, 2 @ 3]"

    imagenParser(triangulo) should beSuccess(ImagenCon(
                                                Triangulo(p1 = (0, 1),
                                                          p2 = (1, 2),
                                                          p3 = (2, 3))
                                                )
                                            )
  }

  it should "parsear algo que no es ni una figura ni un grupo resulta en error" in {
    val programaInvalido = "def foo { 2 + 2 }"

    imagenParser(programaInvalido) should be(Error(leftToParse = programaInvalido))
  }
}
