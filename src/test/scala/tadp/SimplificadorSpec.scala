package tadp

import org.scalatest._
import org.scalatest.flatspec._
import org.scalatest.matchers._
import tadp.ast._

class SimplificadorSpec extends AnyFlatSpec with should.Matchers {

  it should "tomar una transformacion de color aplicada a otra transformacion de color y dejar solo la de adentro" in {
    val colorInterno = Color(color = (255, 0, 0), dibujable = Circulo((0, 0), 1))
    val colorDeColor = Color(color = (1, 1, 1), dibujable = colorInterno)

    simplificar(ImagenCon(colorDeColor)) shouldBe (ImagenCon(colorInterno))
  }

  def assertFactorComun(transformacion: Dibujable => Transformacion): Unit = {
    val unCirculo = Circulo(centro=(0,0), radio=1)
    val unRectangulo = Rectangulo((0,0), (1,1))

    simplificar(
      ImagenCon(
        Grupo(Seq(transformacion(unCirculo), transformacion(unRectangulo)))
      )
    ) shouldBe(
      ImagenCon(
        transformacion(Grupo(Seq(unCirculo, unRectangulo))))
      )
  }

  it should "tomar una transformacion de color aplicada a todos los hijos de un grupo y aplicarla al grupo en su lugar" in {
    val color = (dibujable: Dibujable) => Color(color = (255, 0, 0), dibujable = dibujable)

    assertFactorComun(color)
  }

  it should "tomar un escalado aplicado a todos los hijos de un grupo y aplicarla al grupo en su lugar" in {
    val escalado = (dibujable: Dibujable) => Escala(escalado = (1.5, 2), dibujable = dibujable)

    assertFactorComun(escalado)
  }

  it should "tomar una rotacion aplicada a todos los hijos de un grupo y aplicarla al grupo en su lugar" in {
    val rotacion = (dibujable: Dibujable) => Rotacion(angulo = 1.5, dibujable = dibujable)

    assertFactorComun(rotacion)
  }

  it should "tomar una traslacion aplicada a todos los hijos de un grupo y aplicarla al grupo en su lugar" in {
    val traslacion = (dibujable: Dibujable) => Traslacion((200, 500), dibujable = dibujable)

    assertFactorComun(traslacion)
  }
//
//  it should "tomar una transformacion aplicada a algunos hijos de un grupo pero no todos y dejarlo igual" in {
//    val traslacion =
//  }
}
