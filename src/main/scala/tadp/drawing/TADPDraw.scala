package tadp.drawing

import tadp.ast.{Grupo, ImagenCon, Rectangulo}
import tadp.combinators.Success
import tadp.drawing.internal.TADPDrawingAdapter
import tadp.parser._
import scalafx.scene.paint.{Color => C}

object TADPDraw extends App {
// Acá es donde los alumnos tendrían que escribir su código por si quieren hacer pruebas manuales, por ej:
// para mostrar la imagen
//    TADPDrawingAdapter.forScreen {
//      Dibujador.draw(_, ImagenCon(
//        Grupo(
//          Seq(
//            Rectangulo((100, 200), (200, 300))
//          )
//        )
//      ))
//    }
// o para guardar la imagen
//  TADPDrawingAdapter.forImage("rectangulo.png") {
//    Dibujador.draw(_, ImagenCon(
//      Grupo(
//        Seq(
//          Rectangulo((100, 200), (200, 300))
//        )
//      )
//    ))
//  }
// o para levantar la ventana donde poder dibujar la imagen a medida que la escriben
//  TADPDrawingAdapter
//    .forInteractiveScreen { (imageDescription, adapter) =>
//      imagenParser(imageDescription) match {
//        case Success(imagen, _) => Dibujador.draw(adapter, imagen)
//      }
//    }
// Como código default podemos poner algo que interactue directamente con el adapter:
  TADPDrawingAdapter.forScreen { adapter =>
    adapter
      .beginColor(C.rgb(100, 100, 100))
      .beginColor(C.rgb(100, 100, 100))
      .rectangle((200, 200), (400, 400))
      .rectangle((200, 200), (400, 400))
      .end()
      .end()
  }
}
