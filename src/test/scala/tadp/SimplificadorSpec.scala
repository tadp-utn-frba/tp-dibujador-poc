package tadp

import org.scalatest.flatspec._
import org.scalatest.matchers._
import tadp.ast.{_}

class SimplificadorSpec extends AnyFlatSpec with should.Matchers {

  it should "tomar una transformacion de color aplicada a otra transformacion de color y dejar solo la de adentro" in {
    val colorInterno = Color(color = (255, 0, 0), dibujable = Circulo((0, 0), 1))
    val colorDeColor = Color(color = (1, 1, 1), dibujable = colorInterno)

    simplificador(ImagenCon(colorDeColor)) shouldBe (ImagenCon(colorInterno))
  }

  def assertFactorComun(transformacion: Dibujable => Transformacion): Unit = {
    val unCirculo = Circulo(centro=(0,0), radio=1)
    val unRectangulo = Rectangulo((0,0), (1,1))

    simplificador(
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

  it should "si tras sacar factor comun de las simplificaciones de un grupo, queda otra estructura tambien simplificable se simplifica" in {
    val conEscalado = Escala((2, 2), _)
    val conEscaladoInverso = Escala((0.5, 0.5), _)
    val rectangulo = Rectangulo((200, 200), (300, 300))
    val circulo = Circulo((200, 300), 500)

    simplificador(ImagenCon(
      conEscaladoInverso(
        Grupo(
          Seq(
            conEscalado(rectangulo),
            conEscalado(circulo)
          )
        )
      )
    )) shouldBe(ImagenCon(
      Grupo(
        Seq(
          rectangulo,
          circulo
        )
      )
    ))
  }

  it should "tomar una transformacion aplicada a algunos hijos de un grupo pero no todos lo deja igual" in {
    val unCirculo = Circulo(centro=(0,0), radio=1)
    val unRectangulo = Rectangulo((0,0), (1,1))

    val imagen = ImagenCon(
      Grupo(Seq(
        unCirculo,
        Traslacion((200, 300), unRectangulo)
      ))
    )

    simplificador(imagen) shouldBe(imagen)
  }

  it should "tomar un grupo donde cada hijo tiene aplicada una transformacion diferente lo deja igual" in {
    val unCirculo = Circulo(centro=(0,0), radio=1)
    val unRectangulo = Rectangulo((0,0), (1,1))

    val imagen = ImagenCon(
      Grupo(Seq(
        Escala((100, 200), unCirculo),
        Traslacion((200, 300), unRectangulo)
      ))
    )

    simplificador(imagen) shouldBe(imagen)
  }

  it should "tomar un escalado que contiene a otro escalado y reemplazarlos por la union de los escalados" in {
    simplificador(ImagenCon(
      Escala((2, 3),
        Escala((3, 5),
          Rectangulo((100, 200), (200, 300))
        )
      )
    )) shouldBe(ImagenCon(
      Escala((6, 15),
        Rectangulo((100, 200), (200, 300))
      )
    ))
  }

  it should "tomar una rotacion que contiene a otra rotacion y reemplazarla por la union de las rotaciones" in {
    simplificador(ImagenCon(
      Rotacion(300,
        Rotacion(10,
          Rectangulo((100, 200), (200, 300))
        )
      )
    )) shouldBe(ImagenCon(
      Rotacion(310,
        Rectangulo((100, 200), (200, 300))
      )
    ))
  }

  it should "tomar una traslacion que contiene a otra traslacion y reemplazarla por la union de las traslaciones" in {
    simplificador(ImagenCon(
      Traslacion((10, 20),
        Traslacion((50, 10),
          Rectangulo((100, 200), (200, 300))
        )
      )
    )) shouldBe(ImagenCon(
      Traslacion((60, 30),
        Rectangulo((100, 200), (200, 300))
      )
    ))
  }

  it should "eliminar traslaciones de (0,0)" in {
    simplificador(ImagenCon(
      Traslacion((0, 0),
        Rectangulo((100, 200), (200, 300))
      )
    )) shouldBe(ImagenCon(
      Rectangulo((100, 200), (200, 300))
    ))
  }

  it should "eliminar rotaciones de 0" in {
    simplificador(ImagenCon(
      Rotacion(0,
        Rectangulo((100, 200), (200, 300))
      )
    )) shouldBe(ImagenCon(
      Rectangulo((100, 200), (200, 300))
    ))
  }

  it should "eliminar escalados de (1, 1)" in {
    simplificador(ImagenCon(
      Escala((1, 1),
        Rectangulo((100, 200), (200, 300))
      )
    )) shouldBe(ImagenCon(
      Rectangulo((100, 200), (200, 300))
    ))
  }

  it should "aplicar las simplificaciones aun si están anidadas en una transformacion que no se simplifica" in {
    val rectangulo = Rectangulo((100, 200), (300, 400))
    val rectanguloConRotacionIdentidad = Rotacion(0, rectangulo)

    simplificador(ImagenCon(
      Escala((2,2), rectanguloConRotacionIdentidad))
    ) shouldBe(ImagenCon(
      Escala((2, 2), rectangulo)
    ))
  }

  it should "aplicar las simplificaciones aun si están anidadas en un grupo que no se simplifica" in {
    val rectangulo = Rectangulo((100, 200), (300, 400))
    val circulo = Circulo((100, 200), 300)
    val conRotacionIdentidad = Rotacion(0, _)
    val conEscaladoIdentidad = Escala((1, 1), _)

    simplificador(ImagenCon(
      Grupo(Seq(
        conRotacionIdentidad(rectangulo),
        conEscaladoIdentidad(circulo))))
    ) shouldBe(ImagenCon(
      Grupo(Seq(
        rectangulo,
        circulo))
    ))
  }

  it should "aplicar las simplificaciones aun si están anidadas en otra estructura que tambien se simplifica" in {
    val rectangulo = Rectangulo((100, 200), (300, 400))
    val conRotacionIdentidad = Rotacion(0, _)
    val conEscaladoIdentidad = Escala((1, 1), _)

    simplificador(ImagenCon(
      conEscaladoIdentidad(
        conRotacionIdentidad(
          rectangulo
        )
      )
    )) shouldBe(
      ImagenCon(
        rectangulo
      )
    )
  }
}
