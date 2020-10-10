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

  it should "tomar una transformacion aplicada a todos los hijos de un grupo y aplicarla al grupo en su lugar" in {
    val transformacion = (dibujable: Dibujable) => Color(color = (255, 0, 0), dibujable = dibujable)
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
}
