package tadp.drawing

import scalafx.scene.paint.{Color => C}
import tadp.ast._
import tadp.drawing.internal.TADPDrawingAdapter

object Dibujador {
  def draw(adapter: TADPDrawingAdapter, imagen: Imagen) = {
    def drawDibujable(adapter: TADPDrawingAdapter, dibujable: Dibujable): TADPDrawingAdapter = {
      dibujable match {
        case Triangulo(p1, p2, p3) => adapter.triangle(p1, p2, p3)
        case Circulo(centro, radio) => adapter.circle(centro, radio)
        case Rectangulo(arribaIzquierda, abajoDerecha) => adapter.rectangle(arribaIzquierda, abajoDerecha)
        case Grupo(dibujables) => dibujables.foldLeft(adapter)((adapterPrevio, dibujable) => drawDibujable(adapterPrevio, dibujable))
        case Color((r, g, b), dibujable) => drawDibujable(adapter.beginColor(C.rgb(r, g, b)), dibujable).end()
        case Escala((escalaEnX, escalaEnY), dibujable) => drawDibujable(adapter.beginScale(escalaEnX, escalaEnY), dibujable).end()
        case Rotacion(angulo, dibujable) => drawDibujable(adapter.beginRotate(angulo), dibujable).end()
        case Traslacion((trasladoEnX, trasladoEnY), dibujable) => drawDibujable(adapter.beginTranslate(trasladoEnX, trasladoEnY), dibujable).end()
      }
    }

    imagen match {
      case ImagenVacia => adapter
      case ImagenCon(dibujable) => {
        drawDibujable(adapter.beginColor(C.rgb(125,125,140)), dibujable).end()
      }
    }
  }
}
