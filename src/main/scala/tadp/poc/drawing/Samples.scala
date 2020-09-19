package tadp.poc.drawing

import scalafx.scene.paint.Color
import scala.language.implicitConversions

object Samples {
  implicit def tupleToVector[N1: Numeric, N2: Numeric](t: (N1, N2)): Vector2D = Vector2D(Numeric[N1].toDouble(t._1), Numeric[N2].toDouble(t._2))

  implicit def toSome[T](t: T): Option[T] = Some(t)

  val robot = {
    val eyesDiameter = 65.0
    val eyeProto = Circle((0, 0), eyesDiameter / 2, ShapeProperties(Color.IndianRed, Stroke(2, Color.DarkGray)))
    Drawing(Group((200, 200), Seq(
      Rectangle((0, 0), 400, 300, ShapeProperties(Color.Gray, Stroke(4, Color.DarkGray))),
      Rectangle((180, -90), 40, 90, ShapeProperties(Color.Gray, Stroke(4, Color.DarkGray))),
      Circle((200, -90), 35, ShapeProperties(Color.Gold, Stroke(4, Color.DarkGoldenrod))),
      Triangle((180,170), (200,130), (220,170), ShapeProperties(Color.LightBlue, Stroke(4, Color.Blue))),
      eyeProto.copy(center = (90 + eyesDiameter / 2, 70)),
      eyeProto.copy(center = (180 + eyesDiameter + eyesDiameter / 2, 70)),
      Rectangle((90, 180), 220, 60, ShapeProperties(Color.Gold, Stroke(2, Color.DarkGray)))
    )))
  }
}
