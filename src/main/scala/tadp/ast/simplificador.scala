package tadp
import tadp.ast._

object simplificador {
  def apply(imagen: Imagen): Imagen = {
    imagen match {
      case ImagenCon(dibujable) => ImagenCon(apply(dibujable))
      case ImagenVacia => ImagenVacia
    }
  }

  def apply(dibujable: Dibujable): Dibujable = {
    val nuevoDibujable = dibujable match {
      case GrupoConTransformaciones(transformaciones) if transformaciones.map(_.tipoTransformacion).toSet.size == 1 =>
        transformaciones.head.copy(dibujable = Grupo(transformaciones.map(_.dibujable)))

      case Color(_, Color(otroColor, dibujable)) => Color(otroColor, dibujable)
      case Escala((x1, y1), Escala((x2,y2), dibujable)) => Escala((x1*x2, y1*y2), dibujable)
      case Rotacion(angulo, Rotacion(otroAngulo, dibujable)) => Rotacion(angulo + otroAngulo, dibujable)
      case Traslacion((x1, y1), Traslacion((x2, y2), dibujable)) => Traslacion((x1 + x2, y1 + y2), dibujable)

      case Traslacion((0, 0), dibujable) => dibujable
      case Rotacion(0, dibujable) => dibujable
      case Escala((1, 1), dibujable) => dibujable

      case Grupo(dibujables) => Grupo(dibujables.map(apply))
      case Transformacion(dibujable, tipoTransformacion) => Transformacion(apply(dibujable), tipoTransformacion)

      case otro => otro
    }

    if(nuevoDibujable == dibujable) nuevoDibujable else apply(nuevoDibujable)
  }
}
