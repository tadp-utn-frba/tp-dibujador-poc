package tadp.drawing

import scalafx.scene.paint.Color

object Drawing {
  // Acá iría el código del alumno
  def draw(adapter: TADPDrawingAdapter) = {
    adapter
      .beginTranslate(200, 75)
        .beginScale(1.5)
        .beginColor(Color.rgb(125,125,140))
          .rectangle((0,0), (400,400))
        .end()
        .beginColor(Color.rgb(255,255,125))
          .circle((100,100),50)
          .circle((300,100),50)
        .end()
        .beginColor(Color.rgb(100,100,100))
          .beginTranslate(200,200)
            .beginRotate(45)
              .rectangle((-20, -20), (20, 20))
            .end()
          .end()
        .end()
        .beginColor(Color.rgb(150,150,230))
          .triangle((80, 300), (320, 300), (200, 350))
        .end()
      .end()
    .end()
  }
}
