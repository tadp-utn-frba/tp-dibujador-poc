package tadp

object ast {
  type Punto2D = (Double, Double)

  trait Imagen

  case class ImagenCon(figura: Triangulo) extends Imagen
  case object ImagenVacia extends Imagen

  case class Triangulo(p1: Punto2D, p2: Punto2D, p3: Punto2D)
}
