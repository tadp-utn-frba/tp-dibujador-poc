package tadp.drawing

import scalafx.scene.paint.Color
import tadp.ast._

object Dibujador {
  def reset(adapter: TADPDrawingAdapter): Unit = {
    adapter.
      beginColor(Color.rgb(255,255,255)).
      rectangle((0, 0), (1000, 1000))
      .end()
  }

  def resetAndDraw(adapter: TADPDrawingAdapter, imagen: Imagen): Unit = {
    reset(adapter)
    draw(adapter, imagen)
  }

  def draw(adapter: TADPDrawingAdapter, imagen: Imagen) = {
    imagen match {
      case ImagenVacia => adapter
      case ImagenCon(figura) => {
        figura match {
          case Triangulo(p1, p2, p3) => adapter.beginColor(Color.rgb(125,125,140)).triangle(p1, p2, p3).end()
        }
      }
    }
  }
}
