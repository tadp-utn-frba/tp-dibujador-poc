package tadp

object ast {
  type Punto2D = (Double, Double)

  trait Imagen

  case class ImagenCon(figura: Figura) extends Imagen
  case object ImagenVacia extends Imagen

  trait Figura
  case class Triangulo(p1: Punto2D, p2: Punto2D, p3: Punto2D) extends Figura
  case class Rectangulo(arribaIzquierda: Punto2D, abajoDerecha: Punto2D) extends Figura
}
