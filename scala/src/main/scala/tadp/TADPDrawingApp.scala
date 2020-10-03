package tadp

import scalafx.scene.paint.{Color => C}

object TADPDrawingApp extends App {
  TADPDrawingAdapter
    .forScreen { adapter =>
      adapter
        .beginColor(C.rgb(100, 100, 100))
        .rectangle((200, 200), (400, 400))
        .end()
    }

  //  TADPDrawingAdapter
  //    .forImage("test.png") { adapter =>
  //      adapter
  //        .beginColor(C.rgb(100, 100, 100))
  //        .rectangle((200, 200), (400, 400))
  //        .end()
  //    }
}
