package tadp

object ast {
  type Punto2D = (Double, Double)
  type ColorRGB = (Int, Int, Int)

  trait Imagen

  case class ImagenCon(figura: Dibujable) extends Imagen
  case object ImagenVacia extends Imagen

  trait Dibujable
  case class Triangulo(p1: Punto2D, p2: Punto2D, p3: Punto2D) extends Dibujable
  case class Rectangulo(arribaIzquierda: Punto2D, abajoDerecha: Punto2D) extends Dibujable
  case class Circulo(centro: Punto2D, radio: Double) extends Dibujable
  case class Grupo(dibujables: Seq[Dibujable]) extends Dibujable

  trait Transformacion extends Dibujable {
    def dibujable: Dibujable
  }

  case class Color(color: ColorRGB, dibujable: Dibujable) extends Transformacion
  case class Escala(escalado: (Double, Double), dibujable: Dibujable) extends Transformacion
  case class Rotacion(angulo: Double, dibujable: Dibujable) extends Transformacion
  case class Traslacion(traslacion: (Double, Double), dibujable: Dibujable) extends Transformacion

  def esTransformacion(dibujable: Dibujable) = {
    dibujable match {
      case _ : Transformacion => true
      case _ => false
    }
  }

  def sonMismaTransformacion(dibujables: Seq[Dibujable]) = {
    dibujables.forall(esTransformacion) && dibujables.forall(dibujable => (dibujable, dibujables.head) match {
      case (Color(color, _), Color(otroColor, _)) => color == otroColor
      case (Escala(escalado, _), Escala(otroEscalado, _)) => escalado == otroEscalado
      case (Rotacion(angulo, _), Rotacion(otroAngulo, _)) => angulo == otroAngulo
      case (Traslacion(traslacion, _), Traslacion(otraTraslacion, _)) => traslacion == otraTraslacion
    })
  }

  def simplificar(imagen: Imagen): Imagen = {
    imagen match {
      case ImagenCon(dibujable) => ImagenCon(simplificarDibujable(dibujable))
      case ImagenVacia => ImagenVacia
    }
  }

  def simplificarDibujable(dibujable: Dibujable): Dibujable = {
    dibujable match {
      case Color(_, colorInterno@Color(_, _)) => colorInterno
      case Grupo(transformaciones: Seq[Transformacion]) if sonMismaTransformacion(transformaciones) => transformaciones.head match {
        case Color(color, _) => Color(color, Grupo(transformaciones.map(_.dibujable)))
        case Escala(escalado, _) => Escala(escalado, Grupo(transformaciones.map(_.dibujable)))
        case Rotacion(angulo, _) => Rotacion(angulo, Grupo(transformaciones.map(_.dibujable)))
        case Traslacion(traslacion, _) => Traslacion(traslacion, Grupo(transformaciones.map(_.dibujable)))
        }
      case otro => otro
    }
  }
}
