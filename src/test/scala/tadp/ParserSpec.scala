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

  it should "parsear 'rectangulo' seguido de dos puntos entre corchetes a una imagen que contiene un rectangulo con esos puntos como superior izquierdo e inferior derecho" in {
    val rectangulo = "rectangulo[100 @ 100, 300 @ 300]"

    imagenParser(rectangulo) should beSuccess(ImagenCon(
                                              Rectangulo(arribaIzquierda = (100, 100),
                                                         abajoDerecha = (300, 300))
                                              ))
  }

  it should "parsear 'grupo' seguido de parentesis y nada adentro devuelve una imagen con un grupo vacio" in {
    val grupo = "grupo()"

    imagenParser(grupo) should beSuccess(ImagenCon(Grupo(Seq())))
  }

  it should "parsear 'grupo' seguido de una figura entre parentesis como una imagen con un grupo que contiene esa figura" in {
    val grupoConTriangulo = """grupo(
                               triangulo[0 @ 100, 200 @ 300, 500 @ 200]
                               )"""

    imagenParser(grupoConTriangulo) should beSuccess(
      ImagenCon(
        Grupo(
          Seq(Triangulo((0, 100), (200, 300), (500, 200)))
        )
      )
    )
  }

  it should "parsear 'grupo' seguido de varias figuras separadas por coma entre parentesis como una imagen con un grupo que contiene todas esas figuras" in {
    val grupoConRectanguloYTriangulo = """grupo(
                               triangulo[0 @ 100, 100 @ 200, 200 @ 300],
                               rectangulo[200 @ 300, 150 @ 500]
                               )"""

    imagenParser(grupoConRectanguloYTriangulo) should beSuccess(
      ImagenCon(
        Grupo(
          Seq(
            Triangulo((0, 100), (100, 200), (200, 300)),
            Rectangulo((200, 300), (150, 500))
          )
        )
      )
    )
  }

  it should "parsear grupos anidados como grupos con grupos adentro" in {
    val grupoConGrupo =
      """grupo(
        |  triangulo[0 @ 100, 100 @ 200, 200 @ 300],
        |  grupo(
        |    rectangulo[0 @ 100, 100 @ 200]
        |  )
        |)""".stripMargin

    imagenParser(grupoConGrupo) should beSuccess(
      ImagenCon(
        Grupo(
          Seq(
            Triangulo((0, 100), (100, 200), (200, 300)),
            Grupo(
              Seq(
                Rectangulo((0, 100), (100, 200))
              )
            )
          )
        )
      )
    )
  }

  it should "parsear 'color[r, g, b]' con numeros en r, g y b seguido de un dibujable entre parentesis como una imagen con una transformacion de color que contiene ese dibujable" in {
    val trianguloRojo =
      """color[255, 0, 0](
        | triangulo[0 @ 200, 300 @ 500, 400 @ 700]
        |)""".stripMargin

    imagenParser(trianguloRojo) should beSuccess(
      ImagenCon(
        Color(
          (255, 0, 0),
          Triangulo((0, 200), (300, 500), (400, 700))
        )
      )
    )
  }
}
