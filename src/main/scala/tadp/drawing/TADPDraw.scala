package tadp.drawing

import tadp.combinators.Success
import tadp.drawing.internal.TADPDrawingAdapter
import tadp.parser._

object TADPDraw extends App {
  TADPDrawingAdapter
    .forInteractiveScreen { (imageDescription, adapter) =>
      imagenParser(imageDescription) match {
        case Success(imagen, _) => Dibujador.draw(adapter, imagen)
      }
    }
}
