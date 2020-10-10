package tadp.drawing

import scalafx.scene.paint.{Color => C}
import tadp.ast._

object Dibujador {
  def reset(adapter: TADPDrawingAdapter): Unit = {
    adapter.
      beginColor(C.rgb(255,255,255)).
      rectangle((0, 0), (1000, 1000))
      .end()
  }

  def resetAndDraw(adapter: TADPDrawingAdapter, imagen: Imagen): Unit = {
    reset(adapter)
    draw(adapter, imagen)
  }

  def draw(adapter: TADPDrawingAdapter, imagen: Imagen) = {
    def drawDibujable(adapter: TADPDrawingAdapter, dibujable: Dibujable): TADPDrawingAdapter = {
      dibujable match {
        case Triangulo(p1, p2, p3) => adapter.triangle(p1, p2, p3)
        case Circulo(centro, radio) => adapter.circle(centro, radio)
        case Rectangulo(arribaIzquierda, abajoDerecha) => adapter.rectangle(arribaIzquierda, abajoDerecha)
        case Grupo(dibujables) => dibujables.foldLeft(adapter)((adapterPrevio, dibujable) => drawDibujable(adapterPrevio, dibujable))
        case Color((r, g, b), dibujable) => drawDibujable(adapter.beginColor(C.rgb(r, g, b)), dibujable).end()
        case Escala((escalaEnX, escalaEnY), dibujable) => drawDibujable(adapter.beginScale(escalaEnX, escalaEnY), dibujable).end()
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