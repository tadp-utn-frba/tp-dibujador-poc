package tadp.drawing

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Group, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color

object TADPDrawingApp extends JFXApp {
  val canvas = new Canvas(1000, 750)
  stage = new PrimaryStage {
    title = "TADP Draw"
    scene = new Scene {
      content = new Group {
        children = Seq(canvas)
      }
    }
  }

  canvas.graphicsContext2D.setFill(Color.Black)
  canvas.graphicsContext2D.fillRect(0, 0, canvas.width.value, canvas.height.value)
  canvas.graphicsContext2D.setFill(Color.White)

  val adapter = new TADPDrawingAdapter(canvas)
  Drawing.draw(adapter)
}
