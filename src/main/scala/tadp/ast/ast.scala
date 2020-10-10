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

  case class Transformacion(dibujable: Dibujable, tipoTransformacion: TipoTransformacion) extends Dibujable

  trait TipoTransformacion

  case class TransformacionDeColor(color: ColorRGB) extends TipoTransformacion
  case class TransformacionDeEscala(escalado: (Double, Double)) extends TipoTransformacion
  case class TransformacionDeRotacion(angulo: Double) extends TipoTransformacion
  case class TransformacionDeTraslacion(traslacion: (Double, Double)) extends TipoTransformacion

  object Color {
    def apply(color: ColorRGB, dibujable: Dibujable) =
      Transformacion(dibujable, TransformacionDeColor(color))
    def unapply(transformacion: Transformacion) = transformacion match {
      case Transformacion(dibujable, TransformacionDeColor(color)) => Some(color, dibujable)
      case _ => None
    }
  }
  object Rotacion {
    def apply(angulo: Double, dibujable: Dibujable) =
      Transformacion(dibujable, TransformacionDeRotacion(angulo))
    def unapply(transformacion: Transformacion) = transformacion match {
      case Transformacion(dibujable, TransformacionDeRotacion(angulo)) => Some(angulo, dibujable)
      case _ => None
    }
  }
  object Traslacion {
    def apply(traslacion: (Double, Double), dibujable: Dibujable) =
      Transformacion(dibujable, TransformacionDeTraslacion(traslacion))
    def unapply(transformacion: Transformacion) = transformacion match {
      case Transformacion(dibujable, TransformacionDeTraslacion(traslacion)) => Some(traslacion, dibujable)
      case _ => None
    }
  }
  object Escala {
    def apply(escalado: (Double, Double), dibujable: Dibujable) =
      Transformacion(dibujable, TransformacionDeEscala(escalado))
    def unapply(transformacion: Transformacion) = transformacion match {
      case Transformacion(dibujable, TransformacionDeEscala(escala)) => Some(escala, dibujable)
      case _ => None
    }
  }

  object GrupoConTransformaciones {
    def dibujableATransformacion(dibujable: Dibujable) = dibujable match {
      case Transformacion(unDibujable, tipoTransformacion) => Some(Transformacion(unDibujable, tipoTransformacion))
      case _ => None
    }

    def unapply(grupo: Grupo): Option[Seq[Transformacion]] = grupo match {
      case Grupo(dibujables) => dibujables.map(dibujableATransformacion).foldLeft(Some(Seq()): Option[Seq[Transformacion]]) {
        case (Some(transformaciones), Some(transformacion)) => Some(transformaciones :+ transformacion)
        case (_, None) => None
        case (None, _) => None
      }
      case _ => None
    }
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
      case GrupoConTransformaciones(transformaciones) if transformaciones.map(_.tipoTransformacion).toSet.size == 1 =>
        transformaciones.head.copy(dibujable = Grupo(transformaciones.map(_.dibujable)))
      case otro => otro
    }
  }
}
