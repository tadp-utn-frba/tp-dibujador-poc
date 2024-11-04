package tadp.drawing

import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.{Group, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.image.WritableImage

import java.io.FileOutputStream
import javax.imageio.ImageIO

class TADPDrawingScreen(adapterInstructions: TADPDrawingAdapter => Any, toImage: Option[String] = None) extends JFXApp3 {
  def start() = {
    val appWidth = 1024
    val appHeight = 640

    val canvas = new Canvas(appWidth, appHeight)

    val adapter = new TADPDrawingAdapter(canvas)
    stage = new PrimaryStage {
      title = "TADP Draw"
      scene = new Scene(appWidth, appHeight) {
        content = new Group {
          children = Seq(canvas)
        }
      }
    }

    adapterInstructions.apply(adapter)
    toImage.foreach(imageName => {
      val image = new WritableImage(canvas.width.intValue(), canvas.height.intValue())
      canvas.snapshot(null, image)
      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new FileOutputStream("out/" +
        imageName))
      Platform.exit()
    })
  }
}