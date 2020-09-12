package tadp

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.{Scene, Group => FxGroup}
import tadp.drawing.{Circle, Drawable, Group, Rectangle, Samples, ShapeProperties, Triangle}

object TADPDrawApp extends JFXApp {
  val canvas = new Canvas(800, 600)
  stage = new PrimaryStage {
    //    initStyle(StageStyle.Unified)
    title = "TADP Draw"
    scene = new Scene {
      content = new FxGroup {
        children = Seq(canvas)
      }
    }
  }

  def draw(drawable: Drawable): Unit = {
    def configureShapeProperties(properties: ShapeProperties): Unit = {
      canvas.graphicsContext2D.fill = properties.fill.getOrElse(Transparent)
      canvas.graphicsContext2D.stroke = properties.stroke.map(_.paint).getOrElse(Transparent)
      canvas.graphicsContext2D.lineWidth = properties.stroke.map(_.width).getOrElse(0)
    }

    drawable match {
      case Rectangle(pos, width, height, properties) =>
        configureShapeProperties(properties)
        canvas.graphicsContext2D.fillRect(pos.x, pos.y, width, height)
        canvas.graphicsContext2D.strokeRect(pos.x, pos.y, width, height)
      case Circle(pos, radius, properties) =>
        configureShapeProperties(properties)
        canvas.graphicsContext2D.fillOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2)
        canvas.graphicsContext2D.strokeOval(pos.x - radius, pos.y - radius, radius * 2, radius * 2)
      case Triangle(p1, p2, p3, properties) =>
        configureShapeProperties(properties)
        canvas.graphicsContext2D.fillPolygon(Seq(p1.tuple, p2.tuple, p3.tuple))
        canvas.graphicsContext2D.strokePolygon(Seq(p1.tuple, p2.tuple, p3.tuple))
      case Group(position, children) =>
        val previousTransform = canvas.graphicsContext2D.getTransform()
        canvas.graphicsContext2D.translate(position.x, position.y)
        children.foreach(draw)
        canvas.graphicsContext2D.setTransform(previousTransform)
    }

  }

  canvas.graphicsContext2D.setFill(Color.Black)
  canvas.graphicsContext2D.fillRect(0, 0, canvas.width.value, canvas.height.value)

  draw(Samples.robot.root)
}
